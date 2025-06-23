// package com.InventoryOrder.controller;

// import com.InventoryOrder.OrderRequest;
// import com.InventoryOrder.OrderResponse;
// import com.InventoryOrder.OrderListResponse;
// import com.InventoryOrder.OrderServiceGrpc;
// import com.google.protobuf.Empty;
// import lombok.RequiredArgsConstructor;
// import org.springframework.web.bind.annotation.*;
// import io.grpc.ManagedChannel;
// import io.grpc.ManagedChannelBuilder;

// import java.util.List;

// @RestController
// @RequestMapping("/api/orders")
// @RequiredArgsConstructor
// public class OrderRestController {

//    // private final OrderServiceGrpc.OrderServiceBlockingStub orderStub;
//     private final OrderServiceGrpc.OrderServiceBlockingStub orderStub;
    
//     public OrderRestController() {
//         ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9095)
//                 .usePlaintext()
//                 .build();
//         this.orderStub = OrderServiceGrpc.newBlockingStub(channel);
//     }

//     @PostMapping
//     public OrderDto createOrder(@RequestBody OrderRequestDto dto) {

//         OrderRequest grpcRequest = OrderRequest.newBuilder()
//                 .setInventoryId(dto.inventoryId())
//                 .setQuantity(dto.quantity())
//                 .build();

//         OrderResponse grpcResponse = orderStub.createOrder(grpcRequest);

//         return new OrderDto(grpcResponse.getOrderId(), grpcResponse.getStatus(), grpcResponse.getTotalPrice());
//     }

//     @GetMapping
//     public List<OrderDto> getAllOrders() {
//         OrderListResponse response = orderStub.getAllOrders(Empty.getDefaultInstance());

//         return response.getOrdersList().stream()
//                 .map(order -> new OrderDto(order.getOrderId(), order.getStatus(), order.getTotalPrice()))
//                 .toList();
//     }

//     // DTO to receive data from the client
//     public record OrderRequestDto(long inventoryId, int quantity) {}

//     // DTO to return to the client (Jackson serializable)
//     public record OrderDto(long orderId, String status, double totalPrice) {}
// }

package com.InventoryOrder.controller;

import com.InventoryOrder.OrderRequest;
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
        OrderRequest grpcRequest = OrderRequest.newBuilder()
                .setInventoryId(dto.inventoryId())
                .setQuantity(dto.quantity())
                .build();

        OrderResponse grpcResponse = orderStub.createOrder(grpcRequest);

        return new OrderDto(grpcResponse.getOrderId(), grpcResponse.getStatus(), grpcResponse.getTotalPrice());
    }

    @GetMapping
    public List<OrderDto> getAllOrders() {
        OrderListResponse response = orderStub.getAllOrders(Empty.getDefaultInstance());

        return response.getOrdersList().stream()
                .map(order -> new OrderDto(order.getOrderId(), order.getStatus(), order.getTotalPrice()))
                .toList();
    }

    public record OrderRequestDto(long inventoryId, int quantity) {}

    public record OrderDto(long orderId, String status, double totalPrice) {}
}
