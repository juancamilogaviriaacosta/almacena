import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { environment } from '../../environments/environment';

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
  mensajeError = '';


  constructor(private http: HttpClient, private route: ActivatedRoute) {}

  ngOnInit() {
    this.id = this.route.snapshot.paramMap.get('id');
    if(this.id === 'new') {
      this.product = {id: null, name: null};
      this.product.code = [];
      this.product.code.push({ code: '' });
      this.product.status = 'Activo';
    } else {
      let tmp = this.http.get(environment.apiUrl+'/api/getProduct?id=' + this.id, { responseType: 'json' });
      tmp.subscribe(product => {
        this.product = product;
        if(!(this.product.code)) {
          this.product.code = [];
          this.product.code.push({ code: '' });
        }
      });
    }
  }
  
  save() {
    this.uploading = true;
    this.http.post(environment.apiUrl+'/api/updateProduct', this.product, { responseType: 'text' })
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
    window.history.back();
  }

  closeError() {
    this.mensajeError = '';
  }
}
