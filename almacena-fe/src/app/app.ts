import { Component, signal } from '@angular/core';
import { Router, RouterLink, RouterOutlet, RouterLinkActive } from '@angular/router';
import { AuthService } from './guards/auth-service';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, RouterLink, RouterLinkActive],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected readonly title = signal('almacena-fe');

  constructor(public auth: AuthService, public router: Router) {
  }

  isProductsSection() {
    return this.router.url.startsWith('/products') ||
      this.router.url.startsWith('/product-management');
  }

  isMovementSection() {
    return this.router.url.startsWith('/movements') ||
      this.router.url.startsWith('/manual-movement');
  }
}
