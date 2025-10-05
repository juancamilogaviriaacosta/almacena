import { Routes } from '@angular/router';
import { Movements } from './movements/movements';
import { Home } from './home/home';
import { Log } from './log/log';
import { Products } from './products/products';
import { ProductManagement } from './product-management/product-management';

export const routes: Routes = [
    { path: 'home', component: Home},
    { path: 'movements', component: Movements},
    { path: 'products', component: Products},
    { path: 'product-management/:id', component: ProductManagement},
    { path: 'log', component: Log}
];


