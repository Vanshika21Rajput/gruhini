package com.example.Gruhani.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SelectedItemsByAdmin {
    private List<Long> selectedProducts=new ArrayList<>();
    private String message;


}
