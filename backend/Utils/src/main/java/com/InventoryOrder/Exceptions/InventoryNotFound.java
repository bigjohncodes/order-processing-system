package com.InventoryOrder.Exceptions;

public class InventoryNotFound extends RuntimeException {
    public InventoryNotFound(String message) {
        super(message);
    }
}
