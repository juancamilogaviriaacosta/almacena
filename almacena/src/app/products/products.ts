import { Component } from '@angular/core';
import { SemicolonBreakPipe } from "../pipes/semicolon-break.pipe";
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { environment } from '../../environments/environment';
import { DecimalPipe } from '@angular/common';

@Component({
  selector: 'app-products',
  imports: [SemicolonBreakPipe, DecimalPipe],
  templateUrl: './products.html',
  styleUrl: './products.css'
})
export class Products {
  
  table:any;  

  constructor(private http: HttpClient, private router: Router) {}

  ngOnInit(): void {
    let tmp = this.http.get(environment.apiUrl+'/api/getProducts', { responseType: 'json' });
    tmp.subscribe(table => {
      this.table = table;
    });
  }

  editProduct(id: any) {
    this.router.navigate(['/product-management', id]);
  }
}
