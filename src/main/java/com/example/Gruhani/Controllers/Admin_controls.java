package com.example.Gruhani.Controllers;

import com.example.Gruhani.Repositories.ProductRepo;
import com.example.Gruhani.Repositories.SellerRepo;
import com.example.Gruhani.models.Idclass;
import com.example.Gruhani.models.SelectedOrderadmin;
import com.example.Gruhani.models.product;
import com.example.Gruhani.service.addproduct_db;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class Admin_controls {
    @Autowired
    SellerRepo sr;
    @Autowired
    ProductRepo prepo;

    @Autowired
    addproduct_db db;


    //PENDING REQUESTS FOR PRODUCT APPROVAL
    @GetMapping("/view-pending")
    public ResponseEntity<?> methods() {

        List<product> l = prepo.findAllBystatus("pending");
        System.out.print("list0" + l);
      /*  List<productdto>s= l.stream()
                .map(product -> {
                    productdto dto = new productdto();
                    BeanUtils.copyProperties(product, dto);
                    return dto;
                })
                .collect(Collectors.toList());*/

        return ResponseEntity.ok().body(l);
    }


    @PostMapping("/accept-item")
    public ResponseEntity<String> meth(@RequestBody SelectedOrderadmin sb) {
        List<Idclass> selectedOrders = sb.getSelectedOrders();

        System.out.print("lullu" + selectedOrders);
        for (Idclass i : selectedOrders) {
            String id = i.getId();
            product p = prepo.findByid(id);
            p.setStatus("approved");
            System.out.print("product" + p);

            prepo.save(p);
        }
        return ResponseEntity.ok().body("ok");
    }

    @PostMapping("/reject-item")
    public ResponseEntity<String> methods(@RequestBody SelectedOrderadmin sb) {
        List<Idclass> selectedOrders = sb.getSelectedOrders();

        System.out.print("lullu" + selectedOrders);
        for (Idclass i : selectedOrders) {
            String id = i.getId();
            product p = prepo.findByid(id);
            p.setStatus("rejected");
            System.out.print("product" + p);

            prepo.save(p);
        }
        return ResponseEntity.ok().body("ok");
    }

}
