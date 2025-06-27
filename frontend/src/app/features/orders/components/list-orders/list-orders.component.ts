import { Component, inject } from '@angular/core';
import { AddModalFormComponent } from "../add-modal-form/add-modal-form.component";
import * as OrderActions from '../../store/order.action';
import { CommonModule } from '@angular/common';
import { Observable } from 'rxjs';
import { Order } from '../../model/Order';
import { Store } from '@ngrx/store';
import { selectAllOrders, selectLoading } from '../../store/order.selector';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-list-orders',
  standalone: true,
  imports: [AddModalFormComponent, CommonModule],
  templateUrl: './list-orders.component.html',
  styleUrl: './list-orders.component.css'
})
export class ListOrdersComponent {

  orders$: Observable<Order[]>;
  loading$: Observable<boolean>;

  order!: Order;
  label!: string;

  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);

  constructor(private store: Store) {
    this.orders$ = this.store.select(selectAllOrders);
    this.loading$ = this.store.select(selectLoading);
  }

  ngOnInit(): void {
    this.store.dispatch(OrderActions.loadOrders());
  }

  deleteOrder(id: number) {
    this.store.dispatch(OrderActions.deleteOrder({orderId: id }));
  }

  openModalAdd(): void {
    this.order = {orderId: -1, inventoryId: -1, orderName: '', totalPrice: 0, status:''};
    this.label = 'Add';
  }

  openModalUpdate(order: Order): void {
    this.order = order;
    this.label = 'Update';
  }

  goToDetail(orderId: number) {
    console.log(orderId);
    this.router.navigate(['/order-detail', orderId]);

  }



}
