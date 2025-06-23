package com.InventoryOrder.server;

import com.google.protobuf.Empty;
import com.InventoryOrder.CreateInventoryRequest;
import com.InventoryOrder.Inventory;
import com.InventoryOrder.InventoryListResponse;
import com.InventoryOrder.InventoryRequest;
import com.InventoryOrder.InventoryResponse;
import com.InventoryOrder.InventoryServiceGrpc;
import com.InventoryOrder.Repository.InventoryRepository;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

@GrpcService
public class GrpcInventoryService extends InventoryServiceGrpc.InventoryServiceImplBase {

    @Autowired
    private InventoryRepository InventoryRepository;

    @Override
    public void getInventoryById(InventoryRequest request, StreamObserver<InventoryResponse> responseObserver) {
        Inventory Inventory = InventoryRepository.findById(request.getInventoryId())
                .orElse(null);
        InventoryResponse.Builder responseBuilder = InventoryResponse.newBuilder();
        if (Inventory != null) {
            responseBuilder
                    .setInventoryId(Inventory.getInventoryId())
                    .setName(Inventory.getName())
                    .setStock(Inventory.getStock())
                    .setPrice(Inventory.getPrice());
        } else {
            responseBuilder
                    .setInventoryId(request.getInventoryId())
                    .setName("Inventory not found")
                    .setStock(0)
                    .setPrice(0.0);
        }

        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }
    @Override
    public void createInventory(CreateInventoryRequest request, StreamObserver<InventoryResponse> responseObserver) {
        // Check if inventory with same ID exists
        boolean existsById = InventoryRepository.existsById(request.getInventoryId());

        // Check if inventory with same name exists (case insensitive)
        boolean existsByName = InventoryRepository.existsByNameIgnoreCase(request.getName());

        InventoryResponse.Builder responseBuilder = InventoryResponse.newBuilder();

        if (existsById) {
            responseBuilder
                .setInventoryId(request.getInventoryId())
                .setName("Inventory with this ID already exists")
                .setStock(0)
                .setPrice(0.0);
            responseObserver.onNext(responseBuilder.build());
            responseObserver.onCompleted();
            return;
        }

        if (existsByName) {
            responseBuilder
                .setInventoryId(0) // ID unknown since duplicate by name
                .setName("Inventory with this name already exists")
                .setStock(0)
                .setPrice(0.0);
            responseObserver.onNext(responseBuilder.build());
            responseObserver.onCompleted();
            return;
        }

        // If checks pass, create and save inventory
        Inventory inventory = new Inventory();
        inventory.setInventoryId(request.getInventoryId());
        inventory.setName(request.getName());
        inventory.setStock(request.getStock());
        inventory.setPrice(request.getPrice());

        InventoryRepository.save(inventory);
        InventoryResponse response = InventoryResponse.newBuilder()
                .setInventoryId(inventory.getInventoryId())
                .setName(inventory.getName())
                .setStock(inventory.getStock())
                .setPrice(inventory.getPrice())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
    
    @Override
    public void getAllInventories(Empty request, StreamObserver<InventoryListResponse> responseObserver) {
        List<Inventory> inventoryList = InventoryRepository.findAll();

        InventoryListResponse.Builder responseBuilder = InventoryListResponse.newBuilder();

        for (Inventory inventory : inventoryList) {
            InventoryResponse inventoryResponse = InventoryResponse.newBuilder()
                    .setInventoryId(inventory.getInventoryId())
                    .setName(inventory.getName())
                    .setStock(inventory.getStock())
                    .setPrice(inventory.getPrice())
                    .build();

            responseBuilder.addInventories(inventoryResponse);
        }

        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }

}