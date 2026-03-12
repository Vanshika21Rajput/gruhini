package com.example.Gruhani.Package;

public class StockNotAvailable extends RuntimeException {
  public StockNotAvailable(String message) {
    super(message);
  }
}
