import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Order } from '../model/Order';

@Injectable({ providedIn: 'root' })
export class OrderService {

  
  private apiUrl = 'http://localhost:8080/order';

  constructor(private http: HttpClient) {}

  getAllOrders(): Observable<Order[]> {
    return this.http.get<Order[]>(`${this.apiUrl}/listOrder`);
  }

  addOrder(order: Order): Observable<Order> {
    return this.http.post<Order>(`${this.apiUrl}/addOrder`, order);
  }

  updateOrder(order: Order): Observable<Order> {
    return this.http.put<Order>(
      `${this.apiUrl}/updateOrder/${order.id}`,
      order
    );
  }

  deleteOrder(orderId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/deleteOrder/${orderId}`);
  }

  findOrderByName(name: string): Observable<Order> {
    return this.http.get<Order>(`${this.apiUrl}/getOrderByName/${name}`);
  }

}
