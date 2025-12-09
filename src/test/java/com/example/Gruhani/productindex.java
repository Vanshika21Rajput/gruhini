package com.example.Gruhani;

import com.example.Gruhani.dtos.productdto;
import com.example.Gruhani.service.addproduct_db;
import org.apache.lucene.document.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.io.IOException;

@SpringBootTest
public class productindex {

@Autowired
addproduct_db db;


    @Test
    public void addindexo() throws IOException {
        productdto p=new productdto();
        p.setCategory("homemade");
        p.setStatus("accepted");
        p.setDescription("very good");
        p.setName("kaju katli");
        p.setQuantity(2);
        p.setSubcategory("homemade pro");
        p.setStock(2);
        p.setImage("no");
        p.setPrice("900");

        Assertions.assertEquals(1,db.addindex(p));
   ;

    }
}
