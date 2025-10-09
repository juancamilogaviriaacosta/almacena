import { Routes } from '@angular/router';
import { Movements } from './movements/movements';
import { Home } from './home/home';
import { Log } from './log/log';
import { Products } from './products/products';
import { ProductManagement } from './product-management/product-management';
import { ManualMovement } from './manual-movement/manual-movement';
import { Login } from './login/login';

export const routes: Routes = [
    { path: '', redirectTo: '/login', pathMatch: 'full' },
    { path: 'login', component: Login },
    { path: 'home', component: Home},
    { path: 'movements', component: Movements},
    { path: 'products', component: Products},
    { path: 'product-management/:id', component: ProductManagement},
    { path: 'manual-movement/:id', component: ManualMovement},
    { path: 'log', component: Log}
];


