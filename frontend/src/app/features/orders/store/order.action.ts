import { createAction, props } from '@ngrx/store';
import { CreateOrderRequest, Order } from '../model/Order';


export const loadOrders = createAction('[Order] Load Orders');
export const loadOrdersSuccess = createAction(
  '[Order] Load Orders Success',
  props<{ orders: Order[] }>()
);
export const loadOrdersFailure = createAction(
  '[Order] Load Orders Failure',
  props<{ error: any }>()
);

export const loadOrderById = createAction('[Order] Load Order by name',
   props<{ orderId: number }>()
  );
export const loadOrderByIdSuccess = createAction(
  '[Order] Load Order By Name Success',
  props<{ order: Order | null}>()
);
export const loadOrderByIdFailure = createAction(
  '[Order] Load Order By Name Failure',
  props<{ error: any }>()
);

export const addOrder = createAction(
  '[Order] Add Order',
  props<{ order: CreateOrderRequest }>()
);
export const addOrderSuccess = createAction(
  '[Order] Add Order Success',
  props<{ order: Order }>()
);
export const addOrderFailure = createAction(
  '[Order] Add Order Failure',
  props<{ error: any }>()
);


export const updateOrder = createAction(
  '[Order] Update Order',
  props<{ order: Order }>()
);
export const updateOrderSuccess = createAction(
  '[Order] Update Order Success',
  props<{ order: Order }>()
);
export const updateOrderFailure = createAction(
  '[Order] Update Order Failure',
  props<{ error: any }>()
);


export const deleteOrder = createAction(
  '[Order] Delete Order',
  props<{ orderId: number }>()
);
export const deleteOrderSuccess = createAction(
  '[Order] Delete Order Success',
  props<{ orderId: number }>()
);
export const deleteOrderFailure = createAction(
  '[Order] Delete Order Failure',
  props<{ error: any }>()
);
