package com.InventoryOrder.config;

import com.InventoryOrder.OrderServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrpcClientConfig {

    @Bean
    public ManagedChannel managedChannel() {
        return ManagedChannelBuilder.forAddress("localhost", 9095)
                .usePlaintext()
                .build();
    }

    @Bean
    public OrderServiceGrpc.OrderServiceBlockingStub orderStub(ManagedChannel channel) {
        return OrderServiceGrpc.newBlockingStub(channel);
    }
}
