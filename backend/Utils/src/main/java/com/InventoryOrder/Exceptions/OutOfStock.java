package com.InventoryOrder.Exceptions;

public class OutOfStock extends RuntimeException {
    public OutOfStock(String message) {
        super(message);
    }
}
