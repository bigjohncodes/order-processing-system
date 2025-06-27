
import { Component, Input, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Store } from '@ngrx/store';
import { Observable, combineLatest } from 'rxjs';
import { CommonModule } from '@angular/common';

import { Product } from '../../../inventory/model/Product';
import { Order } from '../../model/Order';
import * as ProductActions from '../../../inventory/store/product.action';
import * as ProductSelectors from '../../../inventory/store/product.selector';
import * as OrderActions from '../../store/order.action';

@Component({
  selector: 'app-order-form',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './order-form.component.html',
  styleUrls: ['./order-form.component.css']
})
export class OrderFormComponent implements OnInit {

  @Input() order!: Order;

  orderForm: FormGroup;
  products$: Observable<Product[]>;
  productList: Product[] = [];

  maxQuantity = 1000;
  minQuantity = 1;
  totalPrice: number = 0;

  constructor(private fb: FormBuilder, private store: Store) {
    this.orderForm = this.fb.group({
      inventoryId: [null, Validators.required],
      stock: [0, [Validators.required, Validators.min(1)]],
      price: [0, [Validators.required, Validators.min(0)]]
    });

    this.products$ = this.store.select(ProductSelectors.selectAllProducts);
  }

  ngOnInit(): void {
    this.store.dispatch(ProductActions.loadProducts());

    this.products$.subscribe(products => {
      this.productList = products;
    });

    this.orderForm.get('inventoryId')?.valueChanges.subscribe(productId => {
      const selectedProduct = this.productList.find(p => p.id === +productId);
      if (selectedProduct) {
        this.orderForm.get('price')?.setValue(selectedProduct.price);

        this.maxQuantity = selectedProduct.stock;
        this.minQuantity = 1;

        const quantityControl = this.orderForm.get('stock');
        quantityControl?.setValidators([
          Validators.required,
          Validators.min(this.minQuantity),
          Validators.max(this.maxQuantity)
        ]);
        quantityControl?.updateValueAndValidity();
      }
    });

    // 👇 Auto-calculate total price
    combineLatest([
      this.orderForm.get('price')!.valueChanges,
      this.orderForm.get('stock')!.valueChanges,
    ]).subscribe(([price, quantity]) => {
      this.totalPrice = (Number(price) || 0) * (Number(quantity) || 0);
    });
  }

  // onSubmit(): void {
  //   if (this.orderForm.valid) {
  //     const formValue = this.orderForm.getRawValue();
  //     const payload = {
  //       inventoryId: formValue.inventoryId,
  //       quantity: formValue.stock
  //     };
      
  //     if (this.order?.orderId > -1) {
  //       this.store.dispatch(OrderActions.updateOrder({ order: payload }));
  //     } else {
  //       this.store.dispatch(OrderActions.addOrder({ order: payload }));
  //     }

  //     this.orderForm.reset();
  //     this.totalPrice = 0;
  //   }
  // }
  onSubmit(): void {
    if (this.orderForm.valid) {
      const formValue = this.orderForm.getRawValue();
      const payload = {
        inventoryId: formValue.inventoryId,
        quantity: formValue.stock
      };
  
      this.store.dispatch(OrderActions.addOrder({ order: payload }));
  
      this.orderForm.reset();
      this.totalPrice = 0;
    }
  }
  
}
