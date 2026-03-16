import { HttpClient } from '@angular/common/http';
import { inject } from '@angular/core';
import { ResolveFn } from '@angular/router';
import { forkJoin } from 'rxjs';
import { Utils } from '../utils/utils';

export const inventoryResolver: ResolveFn<any> = (route, state) => {
  const http = inject(HttpClient);
  const params = {
    filterDate: Utils.today(),
    warehouseId: -1
  };
  return forkJoin({
    table: http.get('/api/getInventory', { params, responseType: 'json'}),
    warehouses: http.get('/api/getWarehouse', { responseType: 'json' })
  });
};
