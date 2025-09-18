import { Routes } from '@angular/router';
import { Movements } from './movements/movements';
import { Home } from './home/home';
import { Log } from './log/log';

export const routes: Routes = [
    { path: 'home', component: Home},
    { path: 'movements', component: Movements},
    { path: 'log', component: Log}
];


