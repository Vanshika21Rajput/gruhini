package com.example.Gruhani.models;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Getter
@Setter
@NoArgsConstructor
public class JwtClaims {

   List<String> roles=new ArrayList<>();
    Long userid;


}
