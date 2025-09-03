import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-home',
  imports: [],
  templateUrl: './home.html',
  styleUrl: './home.css'
})
export class Home implements OnInit{

  products:any;

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    let products = this.http.get('http://localhost:8080/api/products', { responseType: 'json' });
    products.subscribe(products => {
      console.log('products: ' + products);
      this.products = products;
    });
  }

}
