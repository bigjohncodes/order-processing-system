import { Routes } from '@angular/router';


export const routes: Routes = [
    {
      path: '',
      loadChildren: () =>
        import('./features/orders/orders.routes').then(m => m.default),
    },
    {
        path: 'inventory',
        loadChildren: () =>
          import('./features/inventory/inventory.routes').then(m => m.default),
    }
];
