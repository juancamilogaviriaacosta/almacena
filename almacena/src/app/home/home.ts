import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { SemicolonBreakPipe } from '../pipes/semicolon-break.pipe';
import { environment } from '../../environments/environment';
import { DatePipe, DecimalPipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Workbook } from 'exceljs';
import * as fs from 'file-saver';

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
      let totalQuantity = 0;
      let totalPrice = 0;
      for(let item of this.table) {
        totalQuantity = totalQuantity + item.quantity;
        totalPrice = totalPrice + item.price;
      }
      this.table.push({
        fechahora: '',
        codes: '',
        name: 'Total',
        quantity: totalQuantity,
        price: totalPrice,
        warehouse: ''
      });
    });
  }

  loadWarehouses() {
    let tmp = this.http.get(environment.apiUrl+'/api/getWarehouse', { responseType: 'json' });
    tmp.subscribe(warehouses => {
      this.warehouses = warehouses;
    });
  }

  exportToExcel() {
    const today = new Date();
    const now = new Date(today.getTime() - today.getTimezoneOffset() * 60000);
    const pad = (n: number) => n.toString().padStart(2, '0');
    const timestamp =
    `${now.getFullYear()}-` +
    `${pad(now.getMonth() + 1)}-` +
    `${pad(now.getDate())} ` +
    `${pad(now.getHours())}-` +
    `${pad(now.getMinutes())}-` +
    `${pad(now.getSeconds())}`;
    
    const workbook = new Workbook();
    const worksheet = workbook.addWorksheet('Data');

    worksheet.columns = [
      { header: 'Último movimiento', key: 'fechahora', width: 30 },
      { header: 'Códigos', key: 'codes', width: 30 },
      { header: 'Producto', key: 'name', width: 30 },
      { header: 'Cantidad', key: 'quantity', width: 30 },
      { header: 'Precio', key: 'price', width: 30 },
      { header: 'Ubicación', key: 'warehouse', width: 30 }
    ];

    worksheet.addRows(this.table);    
    workbook.xlsx.writeBuffer().then(buffer => {
      fs.saveAs(
        new Blob([buffer], {
          type:
            'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
        }),
        `inventario ${timestamp}.xlsx`
      );
    });
  }
}
