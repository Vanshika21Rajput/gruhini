package com.example.Gruhani.service;

import com.example.Gruhani.Enums.OrderStatus;
import com.example.Gruhani.Package.*;
import com.example.Gruhani.Repositories.OrderRepository;
import com.example.Gruhani.Repositories.ProductRepo;
import com.example.Gruhani.Repositories.UserRepo;
import com.example.Gruhani.dtos.SellerDetailsDto;
import com.example.Gruhani.models.OrderItem;
import com.example.Gruhani.dtos.orderReceiveDto;
import com.example.Gruhani.dtos.orderResponseDto;
import com.example.Gruhani.models.*;
import jakarta.persistence.OptimisticLockException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.Min;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.management.BadAttributeValueExpException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class orderService {

    @Autowired
    UserRepo userRepo;
    @Autowired
    ProductRepo productRepo;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    usernameFromContext usernameFromContext;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    public  List<OrderItem>  MaptoOrderItem(List<CartItem>cartItemList,Order order)
    {
        List<OrderItem>orderItemList=new ArrayList<>();
        for(CartItem i:cartItemList)
        {
            OrderItem orderItem=new OrderItem();

            orderItem.setProduct(i.getProduct());
            orderItem.setQuantity(i.getQuantity());
            orderItem.setPriceAtOrderTime(i.getPriceAtAddTime());
            orderItem.setOrder(order);
            orderItemList.add(orderItem);
        }
        return orderItemList;

    }
@Transactional
    public orderResponseDto processOrder(orderReceiveDto receiveDto, HttpServletRequest req)
    {
        String username=usernameFromContext.fetchUsername();
        Users user=userRepo.findByemail(username).orElseThrow(()->new RuntimeException("USER NOT FOUND"));
        Cart cart=user.getCart();
        if(cart==null)
        {
            throw new InvalidCart("Cart Doesn't Exist");
        }
        Order order=new Order();
        List<OrderItem> list=MaptoOrderItem(cart.getCartItems(),order);
        if (list.isEmpty()) {
            throw new IllegalArgumentException("Cart cannot be empty");
        }
        order.setUser(user);
        order.setDeliveryTime("3-4 Days");
        order.setDeliveryAddress(receiveDto.getAddress());
        int otp = ThreadLocalRandom.current().nextInt(100000, 1_000_000);
        String hashedOtp=bCryptPasswordEncoder.encode(String.valueOf(otp));
        order.setHashedOtp(hashedOtp);
        order.setExpiration(LocalDateTime.now().plusDays(5));
        order.setOtpVerified(false);
        order.setOrderItemList(list);
        order.setOrderStatus(OrderStatus.PENDING);
        order.setPlacedAt(LocalDateTime.now());//we could have used @prepersist on field itself
        order.setOrderValue(calculateOrderValue(list));
        try {
            validateAndDecreaseStock(order.getOrderItemList());
        }
        catch(OptimisticLockException exception)
        {
            throw new OptimisticLockException("Other user has updated this product's stock! Kindly Retry");
        }
        order.setSeller(list.get(0).getProduct().getSeller());

        order.setMessage("Order placed Successfully");
        orderRepository.save(order);
        cart.getCartItems().clear();
        //Setting seller details to send to user
        SellerDetailsDto sellerDetailsDto=new SellerDetailsDto();
        sellerDetailsDto.setAddress(order.getSeller().getAddress());
        sellerDetailsDto.setName(order.getSeller().getUser().getName());
        sellerDetailsDto.setContact(order.getSeller().getContactNo());
        sellerDetailsDto.setBusinessName(order.getSeller().getBusinessName());

return  new orderResponseDto(order.getId(),order.getOrderValue(),order.getPlacedAt(),order.getMessage(),sellerDetailsDto,OrderStatus.PENDING);
//frotend must show placed order and pending both

    }

@Transactional
    public void validateAndDecreaseStock(List<OrderItem> list) {
        for (OrderItem orderItem : list) {
            Product p = productRepo.findById(orderItem.getProduct().getId())
                    .orElseThrow(() -> new UserNotFoundException("Product not found"));
            if (orderItem.getQuantity() > p.getStock()) {
                throw new StockNotAvailable("Insufficient stock for: " + p.getName());
            }
            p.setStock(p.getStock() - orderItem.getQuantity());
            productRepo.save(p);
        }
    }
    @Transactional
    public void validateAndIncreaseStock(List<OrderItem> list) {
        for (OrderItem orderItem : list) {
            Product p = productRepo.findById(orderItem.getProduct().getId())
                    .orElseThrow(() -> new ProductNotFoundException("Product not found"));

              p.setStock(p.getStock() + orderItem.getQuantity());

        }
    }


//    private boolean validateStock(Product p, int quantity)
//{
//    Product product1=productRepo.findById(p.getId()).get();
//    if(quantity>product1.getStock())
//    {
//return false;
//    }
//    return true;
//}
    private BigDecimal calculateOrderValue(List<OrderItem> cartItemList) {
        BigDecimal total= BigDecimal.valueOf(0);

        for(OrderItem i:cartItemList) {
                BigDecimal price = i.getProduct().getPrice();
                int quantity = i.getQuantity();
                BigDecimal totalOfAnItem = price.multiply(BigDecimal.valueOf(quantity));
                total = total.add(totalOfAnItem);
        }
        return total;
    }

    @Transactional
    public void cancelOrder(Long id)
    {
        String username=usernameFromContext.fetchUsername();
        Users user=userRepo.findByemail(username).orElseThrow(()->new UserNotFoundException("USER NOT FOUND"));
        Order order=orderRepository.findById(id).orElseThrow(()->new InvalidOrder("ORDER NOT FOUND"));
        Long requestUserId=order.getUser().getId();
        Long currUserId=user.getId();
        if(!requestUserId.equals(currUserId))
        {
            throw new RuntimeException("User Not Allowed to cancel this Order");
        }
        LocalDateTime placedAt = order.getPlacedAt();
        LocalDateTime now = LocalDateTime.now();

        long hoursDifference = ChronoUnit.HOURS.between(placedAt, now);

        if (hoursDifference > 36) {
            throw new RuntimeException("Order cannot be cancelled after 36 hours");
        }
        order.setOrderStatus(OrderStatus.CANCELLED);
        try {
            validateAndIncreaseStock(order.getOrderItemList());
        } catch (OptimisticLockException e) {
          throw  new OptimisticLockException("Retry ORDERING");
        }









    }
}
