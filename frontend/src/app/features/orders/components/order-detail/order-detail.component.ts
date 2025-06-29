import { Component, inject, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import * as OrderActions from '../../store/order.action';
import { Observable } from 'rxjs';
import { Order } from '../../model/Order';
import { Store } from '@ngrx/store';
import { selectOrder } from '../../store/order.selector';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-order-detail',
  imports: [CommonModule],
  templateUrl: './order-detail.component.html',
  styleUrl: './order-detail.component.css'
})
export class OrderDetailComponent implements OnInit{

  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);

  order$: Observable<Order | null>;

  constructor(private store: Store) {
    this.order$ = this.store.select(selectOrder);
  }

  ngOnInit() {
    const orderIdString = this.route.snapshot.paramMap.get('id');
    const orderId = orderIdString ? Number(orderIdString) : 0;  // Convert to number or 0 if null
    this.store.dispatch(OrderActions.loadOrderById({ orderId: orderId }));


  }

}
