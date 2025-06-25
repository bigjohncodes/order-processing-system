package com.InventoryOrder.controller;
import java.lang.reflect.Field;
import com.InventoryOrder.OrderRequest;
import com.InventoryOrder.OrderRequestById;
import com.InventoryOrder.OrderResponse;
import com.InventoryOrder.OrderListResponse;
import com.InventoryOrder.OrderServiceGrpc;
import com.google.protobuf.Empty;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderRestController {

    private final OrderServiceGrpc.OrderServiceBlockingStub orderStub;

    @PostMapping
    public OrderDto createOrder(@RequestBody OrderRequestDto dto) {
        validateDto(dto); 
        OrderRequest grpcRequest = OrderRequest.newBuilder()
                .setInventoryId(dto.inventoryId())
                .setQuantity(dto.quantity())
                .build();

        OrderResponse grpcResponse = orderStub.createOrder(grpcRequest);

        return new OrderDto(grpcResponse.getOrderId(), grpcResponse.getStatus(), grpcResponse.getTotalPrice(), grpcResponse.getOrderName());
    }

    @GetMapping
    public List<OrderDto> getAllOrders() {
        OrderListResponse response = orderStub.getAllOrders(Empty.getDefaultInstance());

        return response.getOrdersList().stream()
                .map(order -> new OrderDto(order.getOrderId(), order.getStatus(), order.getTotalPrice(), order.getOrderName()))
                .toList();
    }

    // @GetMapping("/{orderId}")
    // public OrderDto getOrderById(@PathVariable long orderId) {
    //     OrderResponse order = orderStub.getOrderById(
    //         com.InventoryOrder.OrderIdRequest.newBuilder()
    //             .setOrderId(orderId)
    //             .build()
    //     );

    //     return new OrderDto(order.getOrderId(), order.getStatus(), order.getTotalPrice(), order.getOrderName());
    // }

    @GetMapping("/{id}")
    public OrderDto getOrderById(@PathVariable("id") Long id) {
        OrderRequestById request = OrderRequestById.newBuilder()
                .setOrderId(id)
                .build();

        OrderResponse response = orderStub.getOrderById(request);
        return mapToDto(response);
    }
    private void validateDto(Object dto) {
        for (Field field : dto.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                Object value = field.get(dto);
                if (value == null) {
                    throw new IllegalArgumentException(field.getName() + " is required");
                }
                if (value instanceof String str && str.isBlank()) {
                    throw new IllegalArgumentException(field.getName() + " cannot be blank");
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Failed to access field: " + field.getName(), e);
            }
        }
    }
    private OrderDto mapToDto(OrderResponse response) {
        return new OrderDto(
                response.getOrderId(),
                response.getStatus(),
                response.getTotalPrice(),
                response.getOrderName()
        );
    }

    public record OrderRequestDto(long inventoryId, int quantity) {}

    public record OrderDto(long orderId, String status, double totalPrice, String orderName) {}
}
