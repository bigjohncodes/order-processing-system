import { Routes } from '@angular/router';
import { ListOrdersComponent } from './components/list-orders/list-orders.component';
import { OrderDetailComponent } from './components/order-detail/order-detail.component';
import { provideState } from '@ngrx/store';
import { provideEffects } from '@ngrx/effects';
import { orderReducer } from './store/order.reducer';
import { OrderEffects } from './store/order.effect';
import { productReducer } from '../inventory/store/product.reducer';
import { ProductEffects } from '../inventory/store/product.effect';

export default [
  {
    path: '',
    component: ListOrdersComponent,
    providers: [
      provideState('orders', orderReducer),
      provideState('products', productReducer),
      provideEffects([OrderEffects, ProductEffects])
    ]
  },
  {
    path: 'order-detail/:id',
    component: OrderDetailComponent
  }
] satisfies Routes;
