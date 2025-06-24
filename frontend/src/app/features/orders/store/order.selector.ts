import { createFeatureSelector, createSelector } from '@ngrx/store';
import { OrderState } from './order.reducer';

export const selectOrderState = createFeatureSelector<OrderState>('orders');

export const selectAllOrders = createSelector(
  selectOrderState,
  (state) => state.orders
);

export const selectLoading = createSelector(
  selectOrderState,
  (state) => state.loading
);

export const selectError = createSelector(
  selectOrderState,
  (state) => state.error
);

export const selectOrder = createSelector(
  selectOrderState,
  (state) => state.order
);