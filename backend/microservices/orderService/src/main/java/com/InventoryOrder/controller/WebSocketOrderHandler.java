// package com.InventoryOrder.controller;

// import com.InventoryOrder.OrderRequest;
// import com.InventoryOrder.OrderResponse;
// import com.InventoryOrder.OrderListResponse;
// import com.InventoryOrder.OrderServiceGrpc;
// import com.google.protobuf.Empty;
// import com.fasterxml.jackson.databind.JsonNode;
// import com.fasterxml.jackson.databind.ObjectMapper;
// import lombok.extern.slf4j.Slf4j;
// import org.springframework.stereotype.Component;
// import org.springframework.web.socket.*;
// import org.springframework.web.socket.handler.TextWebSocketHandler;
// import io.grpc.ManagedChannel;
// import io.grpc.ManagedChannelBuilder;

// import java.util.List;

// @Slf4j
// @Component
// public class WebSocketOrderHandler extends TextWebSocketHandler {

//     // private final OrderServiceGrpc.OrderServiceBlockingStub orderStub;
//     // private final ObjectMapper objectMapper = new ObjectMapper();
//     private final OrderServiceGrpc.OrderServiceBlockingStub orderStub;
//     private final ObjectMapper objectMapper = new ObjectMapper();


//     public WebSocketOrderHandler() {
//         ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9095) // ✅ Order gRPC port
//                 .usePlaintext()
//                 .build();
//         this.orderStub = OrderServiceGrpc.newBlockingStub(channel);
//     }

//     @Override
//     public void afterConnectionEstablished(WebSocketSession session) {
//         log.info("WebSocket connected: {}", session.getId());
//     }

//     @Override
//     public void handleTextMessage(WebSocketSession session, TextMessage message) {
//         try {
//             JsonNode root = objectMapper.readTree(message.getPayload());
//             String action = root.get("action").asText();

//             switch (action) {
//                 case "createOrder" -> handleCreateOrder(session, root);
//                 case "getAllOrders" -> handleGetAllOrders(session);
//                 default -> session.sendMessage(new TextMessage("{\"error\": \"Unknown action: " + action + "\"}"));
//             }
//         } catch (Exception e) {
//             log.error("WebSocket error", e);
//             try {
//                 session.sendMessage(new TextMessage("{\"error\": \"" + e.getMessage() + "\"}"));
//             } catch (Exception ignored) {}
//         }
//     }

//     private void handleCreateOrder(WebSocketSession session, JsonNode root) throws Exception {
//         OrderRequestDto dto = objectMapper.treeToValue(root.get("data"), OrderRequestDto.class);

//         OrderRequest grpcRequest = OrderRequest.newBuilder()
//                 .setInventoryId(dto.inventoryId())
//                 .setQuantity(dto.quantity())
//                 .build();

//         OrderResponse grpcResponse = orderStub.createOrder(grpcRequest);

//         OrderDto responseDto = new OrderDto(
//                 grpcResponse.getOrderId(),
//                 grpcResponse.getStatus(),
//                 grpcResponse.getTotalPrice()
//         );

//         session.sendMessage(new TextMessage(objectMapper.writeValueAsString(responseDto)));
//     }

//     private void handleGetAllOrders(WebSocketSession session) throws Exception {
//         OrderListResponse response = orderStub.getAllOrders(Empty.getDefaultInstance());

//         List<OrderDto> orders = response.getOrdersList().stream()
//                 .map(order -> new OrderDto(
//                         order.getOrderId(),
//                         order.getStatus(),
//                         order.getTotalPrice()
//                 ))
//                 .toList();

//         String json = objectMapper.writeValueAsString(orders);
//         session.sendMessage(new TextMessage(json));
//     }

//     // Incoming WebSocket payload structure
//     public record OrderRequestDto(long inventoryId, int quantity) {}

//     // Outgoing response structure (for JSON serialization)
//     public record OrderDto(long orderId, String status, double totalPrice) {}
// }

package com.InventoryOrder.controller;

import com.InventoryOrder.OrderRequest;
import com.InventoryOrder.OrderResponse;
import com.InventoryOrder.OrderListResponse;
import com.InventoryOrder.OrderServiceGrpc;
import com.google.protobuf.Empty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketOrderHandler extends TextWebSocketHandler {

    private final OrderServiceGrpc.OrderServiceBlockingStub orderStub;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        log.info("WebSocket connected: {}", session.getId());
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        try {
            JsonNode root = objectMapper.readTree(message.getPayload());
            String action = root.get("action").asText();

            switch (action) {
                case "createOrder" -> handleCreateOrder(session, root);
                case "getAllOrders" -> handleGetAllOrders(session);
                default -> session.sendMessage(new TextMessage("{\"error\": \"Unknown action: " + action + "\"}"));
            }
        } catch (Exception e) {
            log.error("WebSocket error", e);
            try {
                session.sendMessage(new TextMessage("{\"error\": \"" + e.getMessage() + "\"}"));
            } catch (Exception ignored) {}
        }
    }

    private void handleCreateOrder(WebSocketSession session, JsonNode root) throws Exception {
        OrderRequestDto dto = objectMapper.treeToValue(root.get("data"), OrderRequestDto.class);

        OrderRequest grpcRequest = OrderRequest.newBuilder()
                .setInventoryId(dto.inventoryId())
                .setQuantity(dto.quantity())
                .build();

        OrderResponse grpcResponse = orderStub.createOrder(grpcRequest);

        OrderDto responseDto = new OrderDto(
                grpcResponse.getOrderId(),
                grpcResponse.getStatus(),
                grpcResponse.getTotalPrice()
        );

        session.sendMessage(new TextMessage(objectMapper.writeValueAsString(responseDto)));
    }

    private void handleGetAllOrders(WebSocketSession session) throws Exception {
        OrderListResponse response = orderStub.getAllOrders(Empty.getDefaultInstance());

        List<OrderDto> orders = response.getOrdersList().stream()
                .map(order -> new OrderDto(
                        order.getOrderId(),
                        order.getStatus(),
                        order.getTotalPrice()
                ))
                .toList();

        session.sendMessage(new TextMessage(objectMapper.writeValueAsString(orders)));
    }

    public record OrderRequestDto(long inventoryId, int quantity) {}
    public record OrderDto(long orderId, String status, double totalPrice) {}
}
