package com.example.Gruhani.models;

import java.util.ArrayList;
import java.util.List;

public class SelectedOrderadmin {
    List<Idclass> selectedOrders=new ArrayList<>();

    public List<Idclass> getSelectedOrders() {
        return selectedOrders;
    }

    public void setSelectedOrders(List<Idclass> selectedOrders) {
        this.selectedOrders = selectedOrders;
    }


}
