import { Component } from '@angular/core';
import { AsyncPipe, DecimalPipe } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';

@Component({
  selector: 'app-products',
  imports: [ DecimalPipe, AsyncPipe],
  templateUrl: './products.html',
  styleUrl: './products.css',
})
export class Products {
  
  table: any = [];

  constructor(private http: HttpClient, private router: Router) {}

  ngOnInit(): void {
    this.table = this.http.get<any[]>('/api/getProducts');
  }

  editProduct(id: any) {
    this.router.navigate(['/product-management', id]);
  }
}
