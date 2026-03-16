import { Routes } from '@angular/router';
import { Login } from './login/login';
import { Home } from './home/home';
import { Dashboard } from './dashboard/dashboard';
import { Inventory } from './inventory/inventory';
import { inventoryResolver } from './inventory/inventory-resolver';
import { Movements } from './movements/movements';
import { movementsResolver } from './movements/movements-resolver';
import { ManualMovement } from './manual-movement/manual-movement';
import { manualMovementResolver } from './manual-movement/manual-movement-resolver';

export const routes: Routes = [
    { path: '', component: Home },
    { path: 'login', component: Login },
    { path: 'dashboard', component: Dashboard },
    { path: 'inventory', component: Inventory, resolve: { preload: inventoryResolver } },
    { path: 'movements', component: Movements, resolve: { preload: movementsResolver } },
    { path: 'manual-movement/:id', component: ManualMovement, resolve: { preload: manualMovementResolver }},
];
