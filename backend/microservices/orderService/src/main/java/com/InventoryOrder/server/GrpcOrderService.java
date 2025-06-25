package com.InventoryOrder.server;

import com.google.protobuf.Empty;
import com.InventoryOrder.OrderServiceGrpc;
import com.InventoryOrder.OrderRequest;
import com.InventoryOrder.OrderRequestById;
import com.InventoryOrder.OrderResponse;
import com.InventoryOrder.OrderListResponse;
import com.InventoryOrder.Events.OrderEvent;
import com.InventoryOrder.Repository.OrderRepository;
import com.InventoryOrder.broker.OrderEventProducer;
import com.InventoryOrder.model.Order;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

import java.time.LocalDateTime;
import java.util.List;

@GrpcService
@RequiredArgsConstructor
public class GrpcOrderService extends OrderServiceGrpc.OrderServiceImplBase {

    private final OrderRepository orderRepository;
    private final OrderEventProducer orderEventProducer;

    @Override
    public void createOrder(OrderRequest request, StreamObserver<OrderResponse> responseObserver) {
        long orderId = System.currentTimeMillis();

        Order order = Order.builder()
                .orderId(Long.toString(orderId))
                .InventoryId(request.getInventoryId())
                .status("PENDING")
                .totalAmount(0.0)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .transactionId("TXN_" + orderId)
                .build();

        orderRepository.save(order);

        OrderEvent orderEvent = new OrderEvent();
        orderEvent.setOrderId(orderId);
        orderEvent.setInventoryId(request.getInventoryId());
        orderEvent.setQuantity(request.getQuantity());
        orderEventProducer.sendOrderEvent(orderEvent);

        OrderResponse response = OrderResponse.newBuilder()
                .setOrderId(orderId)
                .setStatus("PENDING")
                .setTotalPrice(0.0)
                .setOrderName(order.getOrderName() == null ? "" : order.getOrderName())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getAllOrders(Empty request, StreamObserver<OrderListResponse> responseObserver) {
        List<Order> orders = orderRepository.findAll();

        OrderListResponse.Builder responseBuilder = OrderListResponse.newBuilder();

        for (Order order : orders) {
            OrderResponse response = OrderResponse.newBuilder()
                    .setOrderId(Long.parseLong(order.getOrderId()))
                    .setStatus(order.getStatus())
                    .setTotalPrice(order.getTotalAmount())
                    .setOrderName(order.getOrderName())
                    .build();

            responseBuilder.addOrders(response);
        }

        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }

    @Override
    public void getOrderById(OrderRequestById request, StreamObserver<OrderResponse> responseObserver) {
        Order order = orderRepository.findById(Long.toString(request.getOrderId()))
        .orElse(null);     
        
        OrderResponse.Builder responseBuilder = OrderResponse.newBuilder();
        if (order != null) {
            responseBuilder
                    .setOrderId(Long.parseLong(order.getOrderId()))
                    .setStatus(order.getStatus())
                    .setTotalPrice(order.getTotalAmount())
                    .setOrderName(order.getOrderName())
                    .build();
        } else {
            responseBuilder
                .setOrderId(request.getOrderId())
                .setStatus("PENDING")
                .setTotalPrice(0.0)
                .setOrderName("")
                .build();
                        
        }

        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }
   
}

