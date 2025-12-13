import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { environment } from '../../environments/environment';

@Component({
  selector: 'app-combo-management',
  imports: [FormsModule],
  templateUrl: './combo-management.html',
  styleUrl: './combo-management.css'
})
export class ComboManagement {
  id:string | null = null;
  combo: any;
  uploading = false;
  mensajeError = '';

  allProducts: any[] = [];
  filteredProducts: any[] = [];
  selectedProduct: string = '';

  constructor(private http: HttpClient, private route: ActivatedRoute) {}

  ngOnInit() {
    this.loadProductSuggestions();
    this.id = this.route.snapshot.paramMap.get('id');
    if(this.id === 'new') {
      this.combo = {id: null, name: null};
      this.combo.code = [];
      this.combo.code.push({ code: '' });
    } else {
      let tmp = this.http.get(environment.apiUrl+'/api/getCombo?id=' + this.id, { responseType: 'json' });
      tmp.subscribe(value => {
        this.combo = value;
        if(!(this.combo.code)) {
          this.combo.code = [];
          this.combo.code.push({ code: '' });
        }
      });
    }
  }
  
  save() {
    this.uploading = true;
    this.http.post(environment.apiUrl+'/api/updateCombo', this.combo, { responseType: 'text' })
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
    this.combo.code.push({ code: '' });
  }

  cancel() {
    window.history.back();
  }

  closeError() {
    this.mensajeError = '';
  }

  loadProductSuggestions() {
    let tmp = this.http.get<any[]>(environment.apiUrl+'/api/getProducts', { responseType: 'json' });
    tmp.subscribe((products: any[]) => {
      this.allProducts = products;
      this.filteredProducts = this.allProducts;
    }, () => {
      this.allProducts = [];
      this.filteredProducts = [];
    });
  }

  onProductInput() {
    const q = (this.selectedProduct || '').toLowerCase();
    this.filteredProducts = q
      ? this.allProducts.filter(p => (p.name || '').toLowerCase().includes(q) || (p.sku || '').toLowerCase().includes(q))
      : this.allProducts;
  }

  addProductFromAutocomplete() {
    if (!this.selectedProduct) {
      return;
    }
    const match = this.allProducts.find(p => p.name === this.selectedProduct);
    if (!this.combo.productDetail) {
      this.combo.productDetail = [];
    }

    const item = this.combo.productDetail.find((pd: { product: { id: any; }; }) => pd.product.id === match.id);
    if (item) {
      item.quantity++;
    } else {
      this.combo.productDetail.push({quantity: 1, product: {id: match.id, name: match.name}});
    }
    
    this.selectedProduct = '';
    this.filteredProducts = this.allProducts;
  }

  removeProductFromAutocomplete(i: number) {
    this.combo.productDetail.splice(i, 1);
  }
}
