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
import { authGuard } from './guards/auth-guard';

export const routes: Routes = [
    { path: '', component: Home },
    { path: 'login', component: Login },
    { path: 'dashboard', component: Dashboard, canActivate: [authGuard] },
    { path: 'inventory', component: Inventory, canActivate: [authGuard], resolve: { preload: inventoryResolver } },
    { path: 'movements', component: Movements, canActivate: [authGuard], resolve: { preload: movementsResolver } },
    { path: 'manual-movement/:id', component: ManualMovement, canActivate: [authGuard], resolve: { preload: manualMovementResolver } },
    { path: 'products', component: Products, canActivate: [authGuard] },
    { path: 'combos', component: Combos, canActivate: [authGuard] },
    { path: 'logs', component: Logs, canActivate: [authGuard] },
    { path: 'product-management/:id', component: ProductManagement, canActivate: [authGuard], resolve: { preload: productManagementResolver } },
    { path: 'combo-management/:id', component: ComboManagement, canActivate: [authGuard], resolve: { preload: comboManagementResolver } },
];
