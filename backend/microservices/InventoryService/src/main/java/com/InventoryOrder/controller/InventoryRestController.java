package com.InventoryOrder.controller;

import java.lang.reflect.Field;
import com.InventoryOrder.CreateInventoryRequest;
import com.InventoryOrder.InventoryRequest;
import com.InventoryOrder.InventoryResponse;
import com.InventoryOrder.InventoryListResponse;
import com.InventoryOrder.InventoryServiceGrpc;
import com.InventoryOrder.UpdateInventoryRequest;
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

        validateDto(dto); 

         // Validate required fields
        if (dto.name() == null || dto.name().isBlank()) {
            throw new IllegalArgumentException("Name is required");
        }

        if (dto.description() == null || dto.description().isBlank()) {
            throw new IllegalArgumentException("Description is required");
        }
        CreateInventoryRequest request = CreateInventoryRequest.newBuilder()
                .setName(dto.name())
                .setStock(dto.stock())
                .setPrice(dto.price())
                .setDescription(dto.description())
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
                .setId(id)
                .build();

        InventoryResponse response = inventoryStub.getInventoryById(request);
        return mapToDto(response);
    }

    private InventoryDto mapToDto(InventoryResponse response) {
        return new InventoryDto(
                response.getId(),
                response.getName(),
                response.getStock(),
                response.getPrice(),
                response.getDescription()

        );
    }




    @PutMapping("/{id}")
    public InventoryDto updateInventory(@PathVariable("id") Long id, @RequestBody CreateInventoryDto dto) {
        validateDto(dto);

        InventoryRequest request = InventoryRequest.newBuilder()
                .setId(id)
                .build();

        // Fetch current inventory to ensure it exists
        InventoryResponse existing = inventoryStub.getInventoryById(request);
        if ("Inventory not found".equals(existing.getName())) {
            throw new IllegalArgumentException("Inventory with ID " + id + " not found");
        }

        UpdateInventoryRequest updateRequest = UpdateInventoryRequest.newBuilder()
                .setName(dto.name())
                .setStock(dto.stock())
                .setPrice(dto.price())
                .setDescription(dto.description())
                .build();

        InventoryResponse updated = inventoryStub.updateInventory(updateRequest);
        return mapToDto(updated);
    }

    @DeleteMapping("/{id}")
    public String deleteInventory(@PathVariable("id") Long id) {
        InventoryRequest request = InventoryRequest.newBuilder()
                .setId(id)
                .build();

        inventoryStub.deleteInventory(request);
        return "Inventory deleted successfully";
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



    // Incoming payload for POST
    public record CreateInventoryDto(String name, int stock, double price, String description) {}

    // Outgoing DTO for response
    public record InventoryDto(long id, String name, int stock, double price,  String description) {}
}
