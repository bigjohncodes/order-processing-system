import { inject, Injectable } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { OrderService } from '../services/order.service';
import * as OrderActions from './order.action';
import { catchError, map, mergeMap, of } from 'rxjs';

@Injectable()
export class OrderEffects {

  private actions$ =  inject(Actions);

  constructor(
    //private actions$: Actions,
    private orderService: OrderService
  ) {}

  loadOrders$ = createEffect(() =>
    this.actions$.pipe(
      ofType(OrderActions.loadOrders),
      mergeMap(() =>
        this.orderService.getAllOrders().pipe(
          map((orders) =>
            OrderActions.loadOrdersSuccess({ orders })
          ),
          catchError((error) =>
            of(OrderActions.loadOrdersFailure({ error }))
          )
        )
      )
    )
  );

  addOrder$ = createEffect(() =>
    this.actions$.pipe(
      ofType(OrderActions.addOrder),
      mergeMap(({ order }) =>
        this.orderService.addOrder(order).pipe(
          map((newOrder) =>
            OrderActions.addOrderSuccess({ order: newOrder })
          ),
          catchError((error) =>
            of(OrderActions.addOrderFailure({ error }))
          )
        )
      )
    )
    
  );
  
  updateOrder$ = createEffect(() =>
    this.actions$.pipe(
      ofType(OrderActions.updateOrder),
      mergeMap(({ order }) =>
        this.orderService.updateOrder(order).pipe(
          map((updatedOrder) =>
            OrderActions.updateOrderSuccess({ order: updatedOrder })
          ),
          catchError((error) =>
            of(OrderActions.updateOrderFailure({ error }))
          )
        )
      )
    )
  );

  deleteOrder$ = createEffect(() =>
    this.actions$.pipe(
      ofType(OrderActions.deleteOrder),
      mergeMap(({ orderId }) =>
        this.orderService.deleteOrder(orderId).pipe(
          map(() =>
            OrderActions.deleteOrderSuccess({ orderId })
          ),
          catchError((error) =>
            of(OrderActions.deleteOrderFailure({ error }))
          )
        )
      )
    )
  );

  loadOrderById$ = createEffect(() =>
    this.actions$.pipe(
      ofType(OrderActions.loadOrderById),
      mergeMap(action =>
        this.orderService.findOrderById(action.orderId).pipe(
          map(order => OrderActions.loadOrderByIdSuccess({ order })),
          catchError(error => of(OrderActions.loadOrderByIdFailure({ error })))
        )
      )
    )
  );


}
