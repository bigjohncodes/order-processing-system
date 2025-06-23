package com.InventoryOrder.config;

import com.InventoryOrder.controller.WebSocketOrderHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final WebSocketOrderHandler orderHandler;

    public WebSocketConfig(WebSocketOrderHandler orderHandler) {
        this.orderHandler = orderHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(orderHandler, "/ws/orders").setAllowedOrigins("*");
    }
}
