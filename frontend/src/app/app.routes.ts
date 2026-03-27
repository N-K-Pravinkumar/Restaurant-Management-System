import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';

export const routes: Routes = [
  { path:'login', loadComponent:()=>import('./features/auth/login.component').then(m=>m.LoginComponent) },
  {
    path:'',
    loadComponent:()=>import('./app-shell.component').then(m=>m.AppShellComponent),
    canActivate:[authGuard],
    children:[
      { path:'',           redirectTo:'dashboard', pathMatch:'full' },
      { path:'dashboard',  loadComponent:()=>import('./features/dashboard/dashboard.component').then(m=>m.DashboardComponent) },
      { path:'menu',       loadComponent:()=>import('./features/menu/menu.component').then(m=>m.MenuComponent) },
      { path:'orders',     loadComponent:()=>import('./features/orders/orders.component').then(m=>m.OrdersComponent) },
      { path:'orders/new', loadComponent:()=>import('./features/orders/new-order.component').then(m=>m.NewOrderComponent) },
      { path:'customers',  loadComponent:()=>import('./features/customers/customers.component').then(m=>m.CustomersComponent) },
      { path:'billing',    loadComponent:()=>import('./features/billing/billing.component').then(m=>m.BillingComponent) },
      { path:'analytics',  loadComponent:()=>import('./features/analytics/analytics.component').then(m=>m.AnalyticsComponent) },
    ]
  },
  { path:'**', redirectTo:'login' }
];