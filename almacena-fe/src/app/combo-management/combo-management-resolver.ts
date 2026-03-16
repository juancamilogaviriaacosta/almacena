import { HttpClient } from '@angular/common/http';
import { inject } from '@angular/core';
import { ResolveFn } from '@angular/router';
import { forkJoin } from 'rxjs';

export const comboManagementResolver: ResolveFn<any> = (route, state) => {
  const http = inject(HttpClient);
  const id = route.paramMap.get('id');
  if (id === 'new') {
    return [];
  } else {
    return forkJoin({
      combo: http.get('/api/getCombo?id=' + id),
    });
  }
};
