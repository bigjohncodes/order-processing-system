import { Component } from '@angular/core';
import { NavigationEnd, Router, RouterOutlet, RouterModule } from '@angular/router';
import { filter } from 'rxjs';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, RouterModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'Inventory Management System';

  constructor(private router: Router) {
    this.router.events
    .pipe(
      filter(event => event instanceof NavigationEnd)
    )
    .subscribe((event) => {
      const nav = event as NavigationEnd; // ✅ Type assertion
      const url = nav.urlAfterRedirects || nav.url;

      if (url === '/' || url.startsWith('/orders')) {
        this.title = 'Order Management System';
      } else if (url.startsWith('/inventory')) {
        this.title = 'Inventory Management System';
      } else {
        this.title = 'Page Not Found';
      }
    });
  
  }
}