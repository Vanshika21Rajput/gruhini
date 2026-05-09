package com.example.Gruhani.service;

import com.example.Gruhani.Enums.OrderStatus;
import com.example.Gruhani.Exceptions.*;
import com.example.Gruhani.Repositories.*;
import com.example.Gruhani.dtos.*;
import com.example.Gruhani.models.OrderItem;

import com.example.Gruhani.models.*;
import com.example.Gruhani.models.Users;
import com.google.firebase.messaging.FirebaseMessagingException;
import jakarta.persistence.OptimisticLockException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class OrderService {

    @Autowired
    UserRepo userRepo;
    @Autowired
    ProductRepo productRepo;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    UsernameFromContext usernameFromContext;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    SellerRepo sellerRepo;
    @Autowired
    FeedBackRepo feedBackRepo;
    @Autowired
    AddressRepo addressRepo;

    @Autowired
    MailService emailService;


    public List<OrderItem> MaptoOrderItem(List<CartItem> cartItemList, Order order) {
        List<OrderItem> orderItemList = new ArrayList<>();
        for (CartItem i : cartItemList) {
            OrderItem orderItem = new OrderItem();

            orderItem.setProduct(i.getProduct());
            orderItem.setQuantity(i.getQuantity());
            orderItem.setPriceAtOrderTime(i.getPriceAtAddTime());
            orderItem.setOrder(order);
            orderItemList.add(orderItem);
        }
        return orderItemList;

    }


    @Transactional
    public OrderSellerResponseDto processOrder(orderReceiveDto receiveDto, HttpServletRequest req) throws FirebaseMessagingException {
        String username = usernameFromContext.fetchUsername();
        Users user = userRepo.findByEmail(username).orElseThrow(() -> new UserNotFoundException("USER NOT FOUND"));
        Cart cart = user.getCart();
        System.out.println("inside cart process order");
        if (cart == null) {

            throw new InvalidCart("Cart Doesn't Exist");
        }

        Order order = new Order();
        List<OrderItem> list = MaptoOrderItem(cart.getCartItems(), order);
        if (list.isEmpty()) {
            throw new IllegalArgumentException("Cart cannot be empty");
        }
        order.setUser(user);
        order.setDeliveryTime("3-4 Days");
        Address address=getAddress(user,receiveDto.getAddressId());
        order.setDeliveryAddress(address);
        AddressDto addressDto=maptoAddressDto(address);
        int otp = ThreadLocalRandom.current().nextInt(100000, 1_000_000);
        String hashedOtp = bCryptPasswordEncoder.encode(String.valueOf(otp));
        order.setHashedOtp(hashedOtp);
        order.setExpiration(LocalDateTime.now().plusDays(5));
        order.setOtpVerified(false);
        order.setOrderItemList(list);
        order.setOrderStatus(OrderStatus.PENDING);
        order.setPlacedAt(LocalDateTime.now());//we could have used @prepersist on field itself
        order.setOrderValue(calculateOrderValue(list));
        try {
            validateAndDecreaseStock(order.getOrderItemList());
        } catch (OptimisticLockException exception) {
            throw new OptimisticLockException("Other user has updated this product's stock! Kindly Retry");
        }
        order.setSeller(list.get(0).getProduct().getSeller());

        order.setMessage("Order placed Successfully");
        orderRepository.save(order);
        boolean emailSent;
        try {
            emailService.sendOtpEmailHtml(
                    user.getEmail(),
                    String.valueOf(otp),
                    String.valueOf(order.getId())
            );
            emailSent=true;
        } catch (Exception e) {
            emailSent=false;
            e.printStackTrace();
            //throw new RuntimeException("Email failed again, please check your email address");

        }
        try {
            emailService.sendSellerOrderUpdateEmail(
                    order.getSeller().getUser().getEmail(),
                    order.getSeller().getUser().getName(),
                    String.valueOf(order.getId()),
                    "PLACED"
            );
        } catch (Exception e) {
            System.out.println("Seller email failed: " + e.getMessage());
        }

        cart.getCartItems().clear();
        //Setting seller details to send to user
        SellerDetailsDto sellerDetailsDto = new SellerDetailsDto();
        sellerDetailsDto.setAddress(addressDto);
        sellerDetailsDto.setName(order.getSeller().getUser().getName());
        sellerDetailsDto.setContact(order.getSeller().getContactNo());
        sellerDetailsDto.setBusinessName(order.getSeller().getBusinessName());

        List<OrderItemDto> orderItemDtos = OrderItemtoDto(list);

        return new OrderSellerResponseDto(order.getId(), order.getOrderValue(), order.getPlacedAt(), order.getMessage(), sellerDetailsDto, OrderStatus.PENDING, order.getDeliveryTime(), addressDto, orderItemDtos,emailSent);
//frontend must show placed order and pending both

    }

    private AddressDto maptoAddressDto(Address address) {
        AddressDto addressDto=new AddressDto();
        addressDto.setAddressLine(address.getAddressLine());
        addressDto.setId(address.getId());
        addressDto.setCity(address.getCity());
        addressDto.setState(address.getState());
        addressDto.setPincode(address.getPincode());
        return  addressDto;
    }

    private Address getAddress(Users users,Long id) {
       Address address=addressRepo.findById(id).orElseThrow(()->new AddressNotFoundException("NO SUCH ADDRESS EXISTS"));
        return address;
    }

    private List<OrderItemDto> OrderItemtoDto(List<OrderItem> list) {
        List<OrderItemDto> orderItemDtos = new ArrayList<>();
        for (OrderItem orderItem : list) {
            OrderItemDto orderItemDto = new OrderItemDto();
            orderItemDto.setPriceAtOrderTime(orderItem.getPriceAtOrderTime());
            orderItemDto.setQuantity(orderItem.getQuantity());
            orderItemDto.setProductImage(orderItem.getProduct().getImage());
            orderItemDto.setProductName(orderItem.getProduct().getName());
            orderItemDtos.add(orderItemDto);

        }
        return orderItemDtos;
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


    private BigDecimal calculateOrderValue(List<OrderItem> cartItemList) {
        BigDecimal total = BigDecimal.valueOf(0);

        for (OrderItem i : cartItemList) {
            BigDecimal price = i.getPriceAtOrderTime();
            int quantity = i.getQuantity();
            BigDecimal totalOfAnItem = price.multiply(BigDecimal.valueOf(quantity));
            total = total.add(totalOfAnItem);
        }
        return total;
    }

    @Transactional
    public void cancelOrder(Long id) {
        String username = usernameFromContext.fetchUsername();
        Users user = userRepo.findByEmail(username).orElseThrow(() -> new UserNotFoundException("USER NOT FOUND"));
        Order order = orderRepository.findById(id).orElseThrow(() -> new InvalidOrder("ORDER NOT FOUND"));
        Long requestUserId = order.getUser().getId();
        Long currUserId = user.getId();
        if (!requestUserId.equals(currUserId)) {
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
            throw new OptimisticLockException("Retry ORDERING");
        }
        try {
            emailService.sendOrderStatusEmail(
                    order.getSeller().getUser().getEmail(),
                    order.getSeller().getUser().getName(),
                    String.valueOf(order.getId()),
                    "CANCELLED_BY_USER"
            );
        } catch (Exception e) {
            System.err.println("Email failed for order " + order.getId() + ": " + e.getMessage());
        }
        try {
            emailService.sendSellerOrderUpdateEmail(
                    order.getSeller().getUser().getEmail(),
                    order.getSeller().getUser().getName(),
                    String.valueOf(order.getId()),
                    "CANCELLED"
            );
        } catch (Exception e) {
            System.out.println("Seller email failed: " + e.getMessage());
        }
    }


    public List<OrderSellerResponseDto >viewOrdersToUser(String orderStatus) {
        String username = usernameFromContext.fetchUsername();
        Users users = userRepo.findByEmail(username)
                .orElseThrow(() -> new UserNotFoundException("USER NOT FOUND"));
        List<Order> orders;
        if (orderStatus==null) {
            orders = orderRepository.findByUserId(users.getId());
        } else {
            try {
                orders = orderRepository.findByUserIdAndOrderStatus(
                        users.getId(),
                        OrderStatus.valueOf(orderStatus.toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new InvalidOrder("Invalid status: " + orderStatus);
            }
        }
        List<OrderSellerResponseDto> response = mapToDto(orders);
        return response;
    }

    private List<OrderSellerResponseDto> mapToDto(List<Order> orders) {
        List<OrderSellerResponseDto> dtoList = new ArrayList<>();
        for (Order order : orders) {

            // Map OrderItems
            List<OrderItemDto> orderItemDtos = new ArrayList<>();
            for (OrderItem item : order.getOrderItemList()) {
                OrderItemDto itemDto = new OrderItemDto();
                itemDto.setProductName(item.getProduct().getName());
                itemDto.setQuantity(item.getQuantity());
                itemDto.setPriceAtOrderTime(item.getPriceAtOrderTime());

                orderItemDtos.add(itemDto);
            }

            // Map Seller Details
            SellerDetailsDto sellerDetailsDto = new SellerDetailsDto();
            sellerDetailsDto.setName(order.getSeller().getUser().getName());
            sellerDetailsDto.setBusinessName(order.getSeller().getBusinessName());
            sellerDetailsDto.setContact(order.getSeller().getContactNo());
           AddressDto addressDto= maptoAddressDto(order.getDeliveryAddress());
            sellerDetailsDto.setAddress(addressDto);

            // Build Response DTO
            OrderSellerResponseDto dto = new OrderSellerResponseDto(
                    order.getId(),
                    order.getOrderValue(),
                    order.getPlacedAt(),
                    order.getMessage(),
                    sellerDetailsDto,
                    order.getOrderStatus(),
                    order.getDeliveryTime(),
                    maptoAddressDto(order.getDeliveryAddress()),
                    orderItemDtos,
                    false
            );
            dtoList.add(dto);
        }
        return dtoList;
    }

    public  List<OrderUserResponseDto>viewOrderToSeller(String orderStatus)
    {
        String username=usernameFromContext.fetchUsername();
        List<Order> orders=new ArrayList<>();
        Seller seller = sellerRepo.findByuser_email(username);
        if (seller == null) {
            throw new UserNotFoundException("Seller not found");
        }
        if(orderStatus==null)
        {
            orders=  orderRepository.findBySellerId(seller.getId());
        }
        else {
            try {
                orders = orderRepository.findBySellerIdAndOrderStatus(
                        seller.getId(),
                        OrderStatus.valueOf(orderStatus.toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new InvalidOrder("Invalid status: " + orderStatus);
            }

        }
        List<OrderUserResponseDto>orderUserResponses=touserResponseDto(orders);
         return orderUserResponses;

    }
    public  List<OrderUserResponseDto>viewSellerOrderToAdmin(Long id,String orderStatus)
    {
        List<Order> orders=new ArrayList<>();
        if(orderStatus==null)
        {
            orders=  orderRepository.findBySellerId(id);
        }
        else {
            try {
                orders = orderRepository.findBySellerIdAndOrderStatus(
                        id,
                        OrderStatus.valueOf(orderStatus.toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new InvalidOrder("Invalid status: " + orderStatus);
            }

        }
        List<OrderUserResponseDto>orderUserResponses=touserResponseDto(orders);
        return orderUserResponses;

    }

    private List<OrderUserResponseDto> touserResponseDto(List<Order> orders) {
        List<OrderUserResponseDto> dtoList = new ArrayList<>();
        for (Order order : orders) {

            // Map OrderItems
            List<OrderItemDto> orderItemDtos = new ArrayList<>();
            for (OrderItem item : order.getOrderItemList()) {
                OrderItemDto itemDto = new OrderItemDto();
                itemDto.setProductName(item.getProduct().getName());
                itemDto.setQuantity(item.getQuantity());
                itemDto.setPriceAtOrderTime(item.getPriceAtOrderTime());

                orderItemDtos.add(itemDto);
            }

            // Map Seller Details
            UserDetailsDto userDetailsDto = new UserDetailsDto();
            userDetailsDto.setName(order.getUser().getName());
            userDetailsDto.setEmail(order.getUser().getEmail());
            userDetailsDto.setContact(order.getUser().getContact());
            // Build Response DTO
            OrderUserResponseDto dto = new OrderUserResponseDto(
                    order.getId(),
                    order.getOrderValue(),
                    order.getPlacedAt(),
                    order.getMessage(),
                    userDetailsDto,
                    order.getOrderStatus(),
                    order.getDeliveryTime(),
                    maptoAddressDto(order.getDeliveryAddress()),
                    orderItemDtos
            );
            dtoList.add(dto);
        }
        return dtoList;

    }
    @Transactional
    public void acceptOrder(List<Long> orderIds) {
        String username = usernameFromContext.fetchUsername();
        Seller seller = sellerRepo.findByuser_email(username);
        if (seller == null) throw new UserNotFoundException("Seller not found");

        List<Order> orders = orderRepository.findAllById(orderIds);
        if (orders.size() != orderIds.size()) {
            throw new InvalidOrder("Some orders not found");
        }
        for (Order order : orders) {
            if (!order.getSeller().getId().equals(seller.getId())) {
                throw new RuntimeException("Not authorized to update this order");
            }
            order.setOrderStatus(OrderStatus.ACCEPTED);
            try {
                emailService.sendOrderStatusEmail(
                        order.getUser().getEmail(),
                        order.getUser().getName(),
                        String.valueOf(order.getId()),
                        "ACCEPTED"
                );
            } catch (Exception e) {
                System.err.println("Email failed for order " + order.getId() + ": " + e.getMessage());
            }
        }
    }
    @Transactional
    public void rejectOrder(List<Long> orderIds) {
        String username = usernameFromContext.fetchUsername();
        Seller seller = sellerRepo.findByuser_email(username);
        if (seller == null) throw new UserNotFoundException("Seller not found");

        List<Order> orders = orderRepository.findAllById(orderIds);
        if (orders.size() != orderIds.size()) {
            throw new InvalidOrder("Some orders not found");
        }
        for (Order order : orders) {
            if (!order.getSeller().getId().equals(seller.getId())) {
                throw new RuntimeException("Not authorized to update this order");
            }
            order.setOrderStatus(OrderStatus.REJECTED);
            try {
                emailService.sendOrderStatusEmail(
                        order.getUser().getEmail(),
                        order.getUser().getName(),
                        String.valueOf(order.getId()),
                        "REJECTED"
                );
            } catch (Exception e) {
                System.err.println("Email failed for order " + order.getId() + ": " + e.getMessage());
            }
        }
    }
@Transactional
    public Boolean verifyOtp(Long id, String otp) {
    Order order = orderRepository.findById(id).orElseThrow(() -> new InvalidOrder("Order Not Found"));
    LocalDateTime now = LocalDateTime.now();
    LocalDateTime expiration = order.getExpiration();
    if (now.isAfter(expiration)) {
        throw new RuntimeException("Order OTP has expired");
    }

        if  (bCryptPasswordEncoder.matches(otp, order.getHashedOtp())) {
            order.setOrderStatus(OrderStatus.DELIVERED);
            order.setOtpVerified(true);
            int orderCount=order.getSeller().getTotalOrderCount();
            order.getSeller().setTotalOrderCount(orderCount+1);
            try {
                emailService.sendOrderStatusEmail(
                        order.getUser().getEmail(),
                        order.getUser().getName(),
                        String.valueOf(order.getId()),
                        "DELIVERED"
                );
            } catch (Exception e) {
                System.err.println("Email failed for order " + id + ": " + e.getMessage());
            }
            return true;

        }
    return false;
}

    @Transactional
    public void feedback(@Valid FeedBackDto feedBackDto) {
        String username = usernameFromContext.fetchUsername();
        Users user = userRepo.findByEmail(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        // Verify order belongs to this user
        Order order = orderRepository.findById(feedBackDto.getOrderId())
                .orElseThrow(() -> new InvalidOrder("Order not found"));

        if (!order.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Not authorized to review this order");
        }

        // Only allow feedback after delivery
        if (order.getOrderStatus() != OrderStatus.DELIVERED) {
            throw new RuntimeException("Can only review after order is delivered");
        }


        if (feedBackRepo.existsByOrderId(feedBackDto.getOrderId())) {
            throw new RuntimeException("Feedback already submitted for this order");
        }

        Feedback feedback = new Feedback();
        feedback.setOrder(order);
        feedback.setSeller(order.getSeller());
        feedback.setUser(user);
        feedback.setRating(feedBackDto.getRating());
        feedback.setComment(feedBackDto.getReview() );
        feedback.setCreatedAt(LocalDateTime.now());
        feedBackRepo.save(feedback);

        // Update seller's average rating
        updateSellerRating(order.getSeller());
    }

    private void updateSellerRating(Seller seller) {
        List<Feedback> feedbacks = feedBackRepo.findBySellerId(seller.getId());
        double avgRating = feedbacks.stream()
                .mapToInt(Feedback::getRating)
                .average()
                .orElse(0.0);
        seller.setRating((float) avgRating); // dirty checking ✅
    }

        @Transactional
        public void resendOtp(Long orderId) {
            String username = usernameFromContext.fetchUsername();
            Users user = userRepo.findByEmail(username)
                    .orElseThrow(() -> new UserNotFoundException("User not found"));

            Order order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new InvalidOrder("Order not found"));

            // Security check - only order owner can resend
            if (!order.getUser().getId().equals(user.getId())) {
                throw new RuntimeException("Not authorized");
            }

            // Don't resend if already delivered
            if (order.getOrderStatus() == OrderStatus.DELIVERED) {
                throw new RuntimeException("Order already delivered");
            }

            // Generate fresh OTP
            int newOtp = ThreadLocalRandom.current().nextInt(100000, 1_000_000);
            String newHashedOtp = bCryptPasswordEncoder.encode(String.valueOf(newOtp));

            // Replace old OTP in DB
            order.setHashedOtp(newHashedOtp);
            order.setExpiration(LocalDateTime.now().plusDays(5)); // reset expiry too

            // Send fresh email
            try {
                emailService.sendOtpEmailHtml(
                        user.getEmail(),
                        String.valueOf(newOtp),
                        String.valueOf(orderId)
                );
            } catch (Exception e) {
                throw new RuntimeException("Email failed again, please check your email address");
            }
        }
    }

