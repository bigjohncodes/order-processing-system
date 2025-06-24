import { Routes } from '@angular/router';
import { ListOrdersComponent } from './components/list-orders/list-orders.component';
import { OrderDetailComponent } from './components/order-detail/order-detail.component';
import { provideState } from '@ngrx/store';
import { provideEffects } from '@ngrx/effects';
import { orderReducer } from './store/order.reducer';
import { OrderEffects } from './store/order.effect';

export default [
  {
    path: '',
    component: ListOrdersComponent,
    providers: [
      provideState('orders', orderReducer),
      provideEffects([OrderEffects])
    ]
  },
  {
    path: 'detail/:name',
    component: OrderDetailComponent
  }
] satisfies Routes;
