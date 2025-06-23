package com.InventoryOrder.config;

import com.InventoryOrder.controller.WebSocketInventoryHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final WebSocketInventoryHandler inventoryHandler;

    public WebSocketConfig(WebSocketInventoryHandler inventoryHandler) {
        this.inventoryHandler = inventoryHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(inventoryHandler, "/ws/inventory").setAllowedOrigins("*");
    }
}
