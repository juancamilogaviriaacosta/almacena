import { HttpClient } from '@angular/common/http';
import { inject } from '@angular/core';
import { ResolveFn } from '@angular/router';
import { forkJoin } from 'rxjs';
import { Utils } from '../utils/utils';

export const manualMovementResolver: ResolveFn<any> = (route, state) => {
  const http = inject(HttpClient);
  const params = {
    filterDate: Utils.today(),
    warehouseId: '-1',
    productId: '-1'
  };
  return forkJoin({
    table: http.get('/api/getInventory', { params }),
  });
};
