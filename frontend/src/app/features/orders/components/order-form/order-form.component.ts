import { Component, Input } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import * as OrderActions from '../../store/order.action';
import { Store } from '@ngrx/store';
import { Order } from '../../model/Order';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-order-form',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './order-form.component.html',
  styleUrls: ['./order-form.component.css']
})
export class OrderFormComponent {

  orderForm: FormGroup;
  @Input() order!: Order;

  constructor(private fb: FormBuilder, private store: Store) {
    this.orderForm = this.fb.group({
      name: ['', Validators.required],
      quantity: [0, [Validators.required, Validators.min(1)]],
      price: [0, [Validators.required, Validators.min(0)]],
      description: ['', Validators.maxLength(500)],
    });


  }
    

  onSubmit() {
    if (this.orderForm.valid) {
      if(this.order.id > -1) {
        this.orderForm.addControl('id', this.fb.control(this.order.id));
        this.store.dispatch(OrderActions.updateOrder({order: this.orderForm.value }));
        this.orderForm.removeControl('id');
        this.orderForm.reset();
      } else {
        this.store.dispatch(OrderActions.addOrder({order: this.orderForm.value }));
        this.orderForm.reset();
      }
    }
  }

}
