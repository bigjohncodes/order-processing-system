import { Routes } from '@angular/router';
import { ListProductsComponent } from './components/list-products/list-products.component';
import { ProductDetailComponent } from './components/product-detail/product-detail.component';
import { provideState } from '@ngrx/store';
import { provideEffects } from '@ngrx/effects';
import { productReducer } from './store/product.reducer';
import { ProductEffects } from './store/product.effect';

export default [
  {
    path: '',
    component: ListProductsComponent,
    providers: [
      provideState('products', productReducer),
      provideEffects([ProductEffects])
    ]
  },
  {
    path: 'detail/:name',
    component: ProductDetailComponent
  }
] satisfies Routes;
