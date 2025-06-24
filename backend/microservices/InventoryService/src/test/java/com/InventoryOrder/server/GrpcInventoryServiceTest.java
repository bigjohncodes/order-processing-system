package com.InventoryOrder.server;

import com.InventoryOrder.InventoryRequest;
import com.InventoryOrder.InventoryResponse;
import com.InventoryOrder.Repository.InventoryRepository;
import com.InventoryOrder.Inventory;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class GrpcInventoryServiceTest {

    @Mock
    private InventoryRepository InventoryRepository;  // Mock database interaction

    @InjectMocks
    private GrpcInventoryService grpcInventoryService; // Service to be tested

    private InventoryRequest request;
    private StreamObserver<InventoryResponse> responseObserver;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);  // Initialize mocks
        request = InventoryRequest.newBuilder().setInventoryId(1L).build();
        responseObserver = mock(StreamObserver.class);  // Mock StreamObserver
    }

    @Test
    void testGetInventoryById_InventoryFound() {
        // Arrange: Mock InventoryRepository behavior (no DB call needed)
        Inventory inventory = Inventory.builder()
                .InventoryId(1L)
                .name("Inventory 1")
                .stock(100)
                .price(10.0)
                .build();

        // Mock the findById method to return the Inventory
        when(InventoryRepository.findById(1L)).thenReturn(java.util.Optional.of(inventory));

        // Act: Simulate the getInventoryById method
        grpcInventoryService.getInventoryById(request, responseObserver);

        // Assert: Verify that the mocked method was called
        verify(InventoryRepository).findById(1L);

        // Build the expected response (using real InventoryResponse)
        InventoryResponse expectedResponse = InventoryResponse.newBuilder()
                .setInventoryId(1L)
                .setName("Inventory 1")
                .setStock(100)
                .setPrice(10.0)
                .build();

        // Capture the argument passed to the onNext method using ArgumentCaptor
        ArgumentCaptor<InventoryResponse> captor = ArgumentCaptor.forClass(InventoryResponse.class);
        verify(responseObserver).onNext(captor.capture());

        // Assert the captured response matches the expected response
        InventoryResponse capturedResponse = captor.getValue();
        assertEquals(expectedResponse.getInventoryId(), capturedResponse.getInventoryId());
        assertEquals(expectedResponse.getName(), capturedResponse.getName());
        assertEquals(expectedResponse.getStock(), capturedResponse.getStock());
        assertEquals(expectedResponse.getPrice(), capturedResponse.getPrice());

        // Ensure the onCompleted method is called
        verify(responseObserver).onCompleted();
    }

    @Test
    void testGetInventoryById_InventoryNotFound() {
        // Arrange: Mock InventoryRepository behavior for non-existent Inventory
        when(InventoryRepository.findById(1L)).thenReturn(java.util.Optional.empty());

        // Act: Simulate the getInventoryById method
        grpcInventoryService.getInventoryById(request, responseObserver);

        // Assert: Verify that the mocked method was called
        verify(InventoryRepository).findById(1L);

        // Build the expected response for a "Inventory not found" scenario
        InventoryResponse expectedResponse = InventoryResponse.newBuilder()
                .setInventoryId(1L)
                .setName("Inventory not found")
                .setStock(0)
                .setPrice(0.0)
                .build();

        // Capture the argument passed to the onNext method using ArgumentCaptor
        ArgumentCaptor<InventoryResponse> captor = ArgumentCaptor.forClass(InventoryResponse.class);
        verify(responseObserver).onNext(captor.capture());

        // Assert the captured response matches the expected response
        InventoryResponse capturedResponse = captor.getValue();
        assertEquals(expectedResponse.getInventoryId(), capturedResponse.getInventoryId());
        assertEquals(expectedResponse.getName(), capturedResponse.getName());
        assertEquals(expectedResponse.getStock(), capturedResponse.getStock());
        assertEquals(expectedResponse.getPrice(), capturedResponse.getPrice());

        // Ensure the onCompleted method is called
        verify(responseObserver).onCompleted();
    }
}
