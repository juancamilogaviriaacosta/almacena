import { Routes } from '@angular/router';
import { Movements } from './movements/movements';
import { Home } from './home/home';
import { Log } from './log/log';
import { Products } from './products/products';
import { ProductManagement } from './product-management/product-management';
import { ManualMovement } from './manual-movement/manual-movement';
import { Login } from './login/login';
import { Combos } from './combos/combos';
import { ComboManagement } from './combo-management/combo-management';

export const routes: Routes = [
    { path: '', redirectTo: '/login', pathMatch: 'full' },
    { path: 'login', component: Login },
    { path: 'home', component: Home},
    { path: 'movements', component: Movements},
    { path: 'products', component: Products},
    { path: 'product-management/:id', component: ProductManagement},
    { path: 'combos', component: Combos},
    { path: 'combo-management/:id', component: ComboManagement},
    { path: 'manual-movement/:id', component: ManualMovement},
    { path: 'log', component: Log}
];


