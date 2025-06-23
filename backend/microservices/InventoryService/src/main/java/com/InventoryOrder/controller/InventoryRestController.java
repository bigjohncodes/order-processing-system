package com.InventoryOrder.controller;

import com.InventoryOrder.CreateInventoryRequest;
import com.InventoryOrder.InventoryRequest;
import com.InventoryOrder.InventoryResponse;
import com.InventoryOrder.InventoryListResponse;
import com.InventoryOrder.InventoryServiceGrpc;
import com.google.protobuf.Empty;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryRestController {

    private final InventoryServiceGrpc.InventoryServiceBlockingStub inventoryStub;

    // Fallback constructor if dependency injection isn't used
    public InventoryRestController() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9091)
                .usePlaintext()
                .build();
        this.inventoryStub = InventoryServiceGrpc.newBlockingStub(channel);
    }

    @PostMapping
    public InventoryDto createInventory(@RequestBody CreateInventoryDto dto) {
        CreateInventoryRequest request = CreateInventoryRequest.newBuilder()
                .setInventoryId(dto.inventoryId())
                .setName(dto.name())
                .setStock(dto.stock())
                .setPrice(dto.price())
                .build();

        InventoryResponse response = inventoryStub.createInventory(request);
        return mapToDto(response);
    }

    @GetMapping
    public List<InventoryDto> getAllInventories() {
        InventoryListResponse response = inventoryStub.getAllInventories(Empty.getDefaultInstance());

        return response.getInventoriesList().stream()
                .map(this::mapToDto)
                .toList();
    }

    @GetMapping("/{id}")
    public InventoryDto getInventoryById(@PathVariable("id") Long id) {
        InventoryRequest request = InventoryRequest.newBuilder()
                .setInventoryId(id)
                .build();

        InventoryResponse response = inventoryStub.getInventoryById(request);
        return mapToDto(response);
    }

    private InventoryDto mapToDto(InventoryResponse response) {
        return new InventoryDto(
                response.getInventoryId(),
                response.getName(),
                response.getStock(),
                response.getPrice()
        );
    }

    // Incoming payload for POST
    public record CreateInventoryDto(long inventoryId, String name, int stock, double price) {}

    // Outgoing DTO for response
    public record InventoryDto(long inventoryId, String name, int stock, double price) {}
}
