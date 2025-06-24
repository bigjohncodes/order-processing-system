import { Component, Input } from '@angular/core';
import { OrderFormComponent } from '../order-form/order-form.component';
import { Order } from '../../model/Order';

@Component({
  selector: 'app-add-modal-form',
  standalone: true,
  imports: [OrderFormComponent],
  templateUrl: './add-modal-form.component.html',
  styleUrl: './add-modal-form.component.css'
})
export class AddModalFormComponent {

  @Input() order!: Order;
  @Input() label!: string;

  constructor() {}


}
