import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { SemicolonBreakPipe } from '../pipes/semicolon-break.pipe';
import { environment } from '../../environments/environment';
import { DatePipe, DecimalPipe } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-home',
  imports: [SemicolonBreakPipe, DecimalPipe, DatePipe, FormsModule],
  templateUrl: './home.html',
  styleUrl: './home.css'
})
export class Home implements OnInit {

  filterDate: string = '';
  table:any;
  warehouses:any;
  selectedWarehouse: string = '-1';

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    const today = new Date();
    const offsetDate = new Date(today.getTime() - today.getTimezoneOffset() * 60000);
    this.filterDate = offsetDate.toISOString().split('T')[0];

    this.loadWarehouses();
    this.loadInventory();
  }

  loadInventory() {
    const params = {
      filterDate: this.filterDate,
      warehouseId: this.selectedWarehouse
    };

    let tmp = this.http.get(environment.apiUrl + '/api/getInventory', { params, responseType: 'json'});
    tmp.subscribe(table => {
      this.table = table;
    });
  }

  loadWarehouses() {
    let tmp = this.http.get(environment.apiUrl+'/api/getWarehouse', { responseType: 'json' });
    tmp.subscribe(warehouses => {
      this.warehouses = warehouses;
    });
  }

  exportToExcel(): void {

  }
}
