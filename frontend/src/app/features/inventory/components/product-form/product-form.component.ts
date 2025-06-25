import { Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import * as ProductActions from '../../store/product.action';
import { Store } from '@ngrx/store';
import { Product } from '../../model/Product';


@Component({
  selector: 'app-product-form',
  standalone: true, // or use `@NgModule` in parent module
  imports: [ReactiveFormsModule],
  templateUrl: './product-form.component.html',
  styleUrls: ['./product-form.component.css'],
})
export class ProductFormComponent implements OnChanges {
  @Input() product!: Product;
  productForm: FormGroup;

  constructor(private fb: FormBuilder, private store: Store) {
    this.productForm = this.fb.group({
      name: ['', Validators.required],
      stock: [0, [Validators.required, Validators.min(1)]],
      price: [0, [Validators.required, Validators.min(0)]],
      description: ['', Validators.maxLength(500)],
    });
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes['product'] && changes['product'].currentValue) {
      this.productForm.patchValue(this.product);
    }
  }

  onSubmit() {
    if (this.productForm.valid) {
      if (this.product.id > -1) {
        this.store.dispatch(ProductActions.updateProduct({ product: { id: this.product.id, ...this.productForm.value } }));
      } else {
        this.store.dispatch(ProductActions.addProduct({ product: this.productForm.value }));
      }
      this.productForm.reset();
    }
  }
}
