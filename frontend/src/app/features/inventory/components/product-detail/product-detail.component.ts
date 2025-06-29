import { Component, inject, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import * as ProductActions from '../../store/product.action';
import { Observable } from 'rxjs';
import { Product } from '../../model/Product';
import { Store } from '@ngrx/store';
import { selectProduct } from '../../store/product.selector';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-product-detail',
  imports: [CommonModule],
  templateUrl: './product-detail.component.html',
  styleUrl: './product-detail.component.css'
})
export class ProductDetailComponent implements OnInit{

  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);

  product$: Observable<Product | null>;

  constructor(private store: Store) {
    this.product$ = this.store.select(selectProduct);
  }

  ngOnInit() {
    const productIdString = this.route.snapshot.paramMap.get('id');
    const productId = productIdString ? Number(productIdString) : 0;  // Convert to number or 0 if null
    this.store.dispatch(ProductActions.loadProductById({ id: productId }));

  }

}
