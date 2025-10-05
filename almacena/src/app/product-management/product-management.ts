import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-product-management',
  imports: [FormsModule],
  templateUrl: './product-management.html',
  styleUrl: './product-management.css'
})
export class ProductManagement {

  id:string | null = null;
  product: any;
  uploading = false;

  constructor(private http: HttpClient, private route: ActivatedRoute) {}

  ngOnInit(): void {
    this.id = this.route.snapshot.paramMap.get('id');
    let tmp = this.http.get('http://localhost:8080/api/getProduct?id=' + this.id, { responseType: 'json' });
    tmp.subscribe(product => {
      this.product = product;
    });
  }
  
  save() {
    this.uploading = true;
    this.http.post('http://localhost:8080/api/updateProduct', this.product)
      .subscribe({
        next: (response) => {
          setTimeout(() => {
            this.uploading = false;
            window.history.back();
          }, 500);        },
        error: (error) => {
          setTimeout(() => {
            this.uploading = false;
            window.history.back();
          }, 500);        }
      });
  }

  cancel() {
    window.history.back();
  }
}
