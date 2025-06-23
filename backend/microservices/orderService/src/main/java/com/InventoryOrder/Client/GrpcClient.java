package com.InventoryOrder.Client;

import com.InventoryOrder.OrderRequest;
import com.InventoryOrder.OrderResponse;
import com.InventoryOrder.OrderServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class GrpcClient {
    public static void main(String[] args) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9090)
                .usePlaintext()
                .build();
        OrderServiceGrpc.OrderServiceBlockingStub stub = OrderServiceGrpc.newBlockingStub(channel);
        OrderRequest request = OrderRequest.newBuilder()
                .setInventoryId(1)
                .setQuantity(3)
                .build();
        OrderResponse response = stub.createOrder(request);
        System.out.println("Command create : ID = " + response.getOrderId() +
                ", Status = " + response.getStatus() +
                ", Price total = " + response.getTotalPrice());

        channel.shutdown();
    }
}
