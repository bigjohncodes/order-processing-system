package com.InventoryOrder.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@Slf4j
@Component
public class WebSocketInventoryHandler extends TextWebSocketHandler {

    private final Set<WebSocketSession> sessions = new CopyOnWriteArraySet<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.add(session);
        log.info("Inventory WebSocket connected: {}", session.getId());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(session);
        log.info("Inventory WebSocket disconnected: {}", session.getId());
    }

    public void broadcastStockUpdate(Object stockUpdate) {
        // try {
        //     String message = objectMapper.writeValueAsString(stockUpdate);
        //     for (WebSocketSession session : sessions) {
        //         if (session.isOpen()) {
        //             session.sendMessage(new TextMessage(message));
        //         }
        //     }
        // } catch (Exception e) {
        //     log.error("Error broadcasting stock update", e);
        // }

        try {
            String payload = objectMapper.writeValueAsString(
                Map.of("event", "stockUpdate", "data", stockUpdate)
            );

            for (WebSocketSession session : sessions) {
                if (session.isOpen()) {
                    session.sendMessage(new TextMessage(payload));
                }
            }
        } catch (Exception e) {
            log.error("Error broadcasting stock update", e);
        }
    }
}
