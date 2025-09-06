import { Routes } from '@angular/router';
import { Movements } from './movements/movements';
import { Home } from './home/home';

export const routes: Routes = [
    { path: 'home', component: Home},
    { path: 'movements', component: Movements}
];


