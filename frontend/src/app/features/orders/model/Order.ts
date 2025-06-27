export interface Order {
    orderId: number;
    inventoryId: Number,
    orderName: string,
    status: string,
    totalPrice: number;
    
  }
export interface CreateOrderRequest {
  inventoryId: number;
  quantity: number;
}