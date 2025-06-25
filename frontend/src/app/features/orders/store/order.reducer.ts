import { createReducer, on } from '@ngrx/store';
import * as OrderActions from './order.action';
import { Order } from '../model/Order';

export interface OrderState {
  orders: Order[];
  order: Order | null;
  loading: boolean;
  error: any;
}

const initialState: OrderState = {
  orders: [],
  order: null,
  loading: false,
  error: null,
};

export const orderReducer = createReducer(
  initialState,

  on(OrderActions.loadOrders, (state) => ({
    ...state,
    loading: true,
    error: null,
  })),
  on(OrderActions.loadOrdersSuccess, (state, { orders }) => ({
    ...state,
    orders,
    loading: false,
  })),
  on(OrderActions.loadOrdersFailure, (state, { error }) => ({
    ...state,
    loading: false,
    error,
  })),


  on(OrderActions.loadOrderById, (state,{orderId}) => ({
    ...state,
    loading: true,
    error: null,
  })),
  on(OrderActions.loadOrderByIdSuccess, (state, { order }) => ({
    ...state,
    order,
    loading: false,
  })),
  on(OrderActions.loadOrderByIdFailure, (state, { error }) => ({
    ...state,
    loading: false,
    error,
  })),

  
  on(OrderActions.addOrderSuccess, (state, { order }) => ({
    ...state,
    orders: [...state.orders, order],
  })),
  
  on(OrderActions.updateOrderSuccess, (state, { order }) => ({
    ...state,
    orders: state.orders.map((p) =>
      p.orderId === order.orderId ? { ...p, ...order } : p
    ),
  })),
  
  on(OrderActions.deleteOrderSuccess, (state, { orderId }) => ({
    ...state,
    orders: state.orders.filter((p) => p.orderId !== orderId),
  }))
);
