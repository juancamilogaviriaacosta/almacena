import { HttpClient } from '@angular/common/http';
import { inject } from '@angular/core';
import { ResolveFn } from '@angular/router';
import { forkJoin } from 'rxjs';
import { Utils } from '../utils/utils';

export const movementsResolver: ResolveFn<any> = (route, state) => {
  const http = inject(HttpClient);
  return forkJoin({
    warehouses: http.get('/api/getWarehouse', { responseType: 'json' }),
  });
};
