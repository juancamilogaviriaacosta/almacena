import { DatePipe, DecimalPipe } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { ChangeDetectorRef, Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SemicolonBreakPipe } from '../pipes/semicolon-break-pipe';
import { ActivatedRoute } from '@angular/router';
import { Utils } from '../utils/utils';

@Component({
  selector: 'app-inventory',
  imports: [SemicolonBreakPipe, DecimalPipe, DatePipe, FormsModule],
  templateUrl: './inventory.html',
  styleUrl: './inventory.css',
})
export class Inventory {
  filterDate: string = '';
  table: any[] = [];
  warehouses: any[] = [];
  selectedWarehouse: number = -1;
  totalQuantity = 0;
  totalPrice = 0;

  constructor(private http: HttpClient,
    private ar: ActivatedRoute,
    private cd: ChangeDetectorRef) {
  }

  ngOnInit(): void {
    this.filterDate = Utils.today();
    this.warehouses = this.ar.snapshot.data['preload'].warehouses;
    this.table = this.ar.snapshot.data['preload'].table;
    this.calculateTotal();
  }

  loadInventory() {
    const params = {
      filterDate: this.filterDate,
      warehouseId: this.selectedWarehouse
    };

    this.http.get<any[]>('/api/getInventory', { params }).subscribe(table => {
      this.table = table;
      this.calculateTotal();
      this.cd.detectChanges();
    });
  }

  calculateTotal() {
    this.totalQuantity = 0;
    this.totalPrice = 0;
    for (let item of this.table) {
      this.totalQuantity += item.quantity;
      this.totalPrice += item.price;
    }
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
    
    /*const workbook = new Workbook();
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
    });*/
  }
}
