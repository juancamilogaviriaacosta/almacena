import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-product-management',
  imports: [FormsModule],
  templateUrl: './product-management.html',
  styleUrl: './product-management.css',
})
export class ProductManagement implements OnInit {

  id:string | null = null;
  product: any;
  uploading = false;
  mensajeError = '';

  constructor(private http: HttpClient,
    private ar: ActivatedRoute,
    private router: Router) {}

  ngOnInit() {
    this.id = this.ar.snapshot.paramMap.get('id');
    if(this.id === 'new') {
      this.product = {id: null, name: null};
      this.product.code = [];
      this.product.code.push({ code: '' });
      this.product.status = 'Activo';
    } else {
      this.product = this.ar.snapshot.data['preload'].product;
      if(!this.product.code) {
        this.product.code = [];
        this.product.code.push({ code: '' });
      }
    }
  }
  
  save() {
    this.uploading = true;
    this.http.post('/api/updateProduct', this.product, { responseType: 'text' })
      .subscribe({
        next: (response) => {
          setTimeout(() => {
            this.uploading = false;
            if("Ok" === response) {
              window.history.back();
            } else {
              this.mensajeError = response as string;
            }
          }, 500);
        },
        error: (error) => {
          setTimeout(() => {
            this.uploading = false;
            this.mensajeError = JSON.stringify(error);
          }, 500);
        }
      });
  }

  addCode() {
    this.product.code.push({ code: '' });
  }

  cancel() {
    this.router.navigate(['/products']);
  }

  closeError() {
    this.mensajeError = '';
  }
}
