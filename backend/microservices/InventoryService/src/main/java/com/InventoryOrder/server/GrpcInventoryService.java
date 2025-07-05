package com.InventoryOrder.server;

import com.google.protobuf.Empty;
import com.InventoryOrder.CreateInventoryRequest;
import com.InventoryOrder.Inventory;
import com.InventoryOrder.InventoryListResponse;
import com.InventoryOrder.InventoryRequest;
import com.InventoryOrder.InventoryResponse;
import com.InventoryOrder.InventoryServiceGrpc;
import com.InventoryOrder.PaginatedInventoryRequest;
import com.InventoryOrder.PaginatedInventoryResponse;
import com.InventoryOrder.UpdateInventoryRequest;
import com.InventoryOrder.Repository.InventoryRepository;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


@GrpcService
public class GrpcInventoryService extends InventoryServiceGrpc.InventoryServiceImplBase {

    @Autowired
    private InventoryRepository InventoryRepository;

    @Override
    public void getInventoryById(InventoryRequest request, StreamObserver<InventoryResponse> responseObserver) {
        Inventory Inventory = InventoryRepository.findById(request.getId())
                .orElse(null);
        InventoryResponse.Builder responseBuilder = InventoryResponse.newBuilder();
        if (Inventory != null) {
            responseBuilder
                    .setId(Inventory.getId())
                    .setName(Inventory.getName())
                    .setStock(Inventory.getStock())
                    .setPrice(Inventory.getPrice())
                    .setDescription(Inventory.getDescription());
        } else {
            responseBuilder
                    .setId(request.getId())
                    .setName("Inventory not found")
                    .setStock(0)
                    .setPrice(0.0)
                    .setDescription("");
        }

        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();

    }
    @Override
    public void createInventory(CreateInventoryRequest request, StreamObserver<InventoryResponse> responseObserver) {
        // Check if inventory with same ID exists
        
        // Check if inventory with same name exists (case insensitive)
        boolean existsByName = InventoryRepository.existsByNameIgnoreCase(request.getName());

        InventoryResponse.Builder responseBuilder = InventoryResponse.newBuilder();

        if (existsByName) {
            responseBuilder
                .setName("Inventory with this ID already exists")
                .setStock(0)
                .setPrice(0.0);
            responseObserver.onNext(responseBuilder.build());
            responseObserver.onCompleted();
            return;
        }

        if (existsByName) {
            responseBuilder
                .setId(0) // ID unknown since duplicate by name
                .setName("Inventory with this name already exists")
                .setStock(0)
                .setPrice(0.0);
            responseObserver.onNext(responseBuilder.build());
            responseObserver.onCompleted();
            return;
        }

        // If checks pass, create and save inventory
        Inventory inventory = new Inventory();
        inventory.setName(request.getName());
        inventory.setStock(request.getStock());
        inventory.setPrice(request.getPrice());
        inventory.setDescription(request.getDescription());

        InventoryRepository.save(inventory);
        InventoryResponse response = InventoryResponse.newBuilder()
                .setId(inventory.getId())
                .setName(inventory.getName())
                .setStock(inventory.getStock())
                .setPrice(inventory.getPrice())
                .setDescription(inventory.getDescription())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
    
    @Override
    public void getAllInventories(Empty request,  StreamObserver<InventoryListResponse> responseObserver) {
        List<Inventory> inventoryList = InventoryRepository.findAll();

        InventoryListResponse.Builder responseBuilder = InventoryListResponse.newBuilder();

        for (Inventory inventory : inventoryList) {
            InventoryResponse inventoryResponse = InventoryResponse.newBuilder()
                    .setId(inventory.getId())
                    .setName(inventory.getName())
                    .setStock(inventory.getStock())
                    .setPrice(inventory.getPrice())
                    .setDescription(inventory.getDescription())
                    .build();

            responseBuilder.addInventories(inventoryResponse);
        }

        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }


    @Override
    public void getPaginatedInventory(PaginatedInventoryRequest request, StreamObserver<PaginatedInventoryResponse> responseObserver) {
        int page = request.getPage();
        int size = request.getSize();

        Pageable pageable = PageRequest.of(page, size);
        Page<Inventory> inventoryPage = InventoryRepository.findAll(pageable);

        PaginatedInventoryResponse.Builder responseBuilder = PaginatedInventoryResponse.newBuilder()
                .setCurrentPage(page)
                .setTotalPages(inventoryPage.getTotalPages())
                .setTotalElements(inventoryPage.getTotalElements());

        for (Inventory inventory : inventoryPage.getContent()) {
            InventoryResponse inventoryResponse = InventoryResponse.newBuilder()
                    .setId(inventory.getId())
                    .setName(inventory.getName())
                    .setStock(inventory.getStock())
                    .setPrice(inventory.getPrice())
                    .setDescription(inventory.getDescription())
                    .build();
            responseBuilder.addInventories(inventoryResponse);
        }

        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }



    @Override
    public void updateInventory(UpdateInventoryRequest request, StreamObserver<InventoryResponse> responseObserver) {
        Inventory inventory = InventoryRepository.findByNameIgnoreCase(request.getName()).orElse(null);

        if (inventory == null) {
            responseObserver.onError(new Throwable("Inventory not found"));
            return;
        }

        inventory.setName(request.getName());
        inventory.setStock(request.getStock());
        inventory.setPrice(request.getPrice());
        inventory.setDescription(request.getDescription());

        InventoryRepository.save(inventory);

        InventoryResponse response = InventoryResponse.newBuilder()
                .setId(inventory.getId())
                .setName(inventory.getName())
                .setStock(inventory.getStock())
                .setPrice(inventory.getPrice())
                .setDescription(inventory.getDescription())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void deleteInventory(InventoryRequest request, StreamObserver<Empty> responseObserver) {
        InventoryRepository.deleteById(request.getId());
        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }


}