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
import { Products } from './products/products';
import { Combos } from './combos/combos';
import { Logs } from './logs/logs';
import { ProductManagement } from './product-management/product-management';
import { productManagementResolver } from './product-management/product-management-resolver';
import { ComboManagement } from './combo-management/combo-management';
import { comboManagementResolver } from './combo-management/combo-management-resolver';

export const routes: Routes = [
    { path: '', component: Home },
    { path: 'login', component: Login },
    { path: 'dashboard', component: Dashboard },
    { path: 'inventory', component: Inventory, resolve: { preload: inventoryResolver } },
    { path: 'movements', component: Movements, resolve: { preload: movementsResolver } },
    { path: 'manual-movement/:id', component: ManualMovement, resolve: { preload: manualMovementResolver }},
    { path: 'products', component: Products },
    { path: 'combos', component: Combos },
    { path: 'logs', component: Logs },
    { path: 'product-management/:id', component: ProductManagement, resolve: { preload: productManagementResolver }},
    { path: 'combo-management/:id', component: ComboManagement, resolve: { preload: comboManagementResolver }},
];
