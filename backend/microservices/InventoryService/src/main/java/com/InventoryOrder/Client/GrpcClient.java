package com.InventoryOrder.Client;

import com.InventoryOrder.CreateInventoryRequest;
import com.InventoryOrder.InventoryResponse;
import com.InventoryOrder.InventoryServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class GrpcClient {
    public static void main(String[] args) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9090)
                .usePlaintext()
                .build();

        InventoryServiceGrpc.InventoryServiceBlockingStub stub = InventoryServiceGrpc.newBlockingStub(channel);

        CreateInventoryRequest request = CreateInventoryRequest.newBuilder()
        .setName("Laptop")              // product name
        .setStock(10)                  // stock quantity
        .setPrice(550.0)               // price
        .build();
       
        InventoryResponse response = stub.createInventory(request);

        System.out.println("Command create : ID = " + response.getId() +
                ", StatuName: " + response.getName() +
                ", Stock: " + response.getStock() +
                ", Price: " + response.getPrice()
                ); 

        channel.shutdown();
    }
}
