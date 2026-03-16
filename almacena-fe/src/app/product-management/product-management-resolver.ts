import { HttpClient } from '@angular/common/http';
import { inject } from '@angular/core';
import { ResolveFn } from '@angular/router';
import { forkJoin } from 'rxjs';
import { Utils } from '../utils/utils';

export const productManagementResolver: ResolveFn<any> = (route, state) => {
  const http = inject(HttpClient);
  const id = route.paramMap.get('id');
  if (id === 'new') {
    return [];
  } else {
    return forkJoin({
      product: http.get('/api/getProduct?id=' + id),
    });
  }
};
