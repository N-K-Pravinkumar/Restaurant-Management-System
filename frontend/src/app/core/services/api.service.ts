import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

const API = 'http://localhost:8080/api';

@Injectable({ providedIn:'root' })
export class ApiService {
  constructor(private http:HttpClient){}

  // Menu
  getMenu():Observable<any[]>               { return this.http.get<any[]>(`${API}/menu`); }
  getMenuAvailable():Observable<any[]>      { return this.http.get<any[]>(`${API}/menu/available`); }
  createMenuItem(d:any):Observable<any>     { return this.http.post<any>(`${API}/menu`, d); }
  updateMenuItem(id:number,d:any):Observable<any>{ return this.http.put<any>(`${API}/menu/${id}`,d); }
  deleteMenuItem(id:number):Observable<any> { return this.http.delete<any>(`${API}/menu/${id}`); }
  toggleMenuItem(id:number):Observable<any> { return this.http.patch<any>(`${API}/menu/${id}/toggle`,{}); }

  // Orders
  getOrders():Observable<any[]>             { return this.http.get<any[]>(`${API}/orders`); }
  getOrdersByStatus(s:string):Observable<any[]>{ return this.http.get<any[]>(`${API}/orders/status/${s}`); }
  createOrder(d:any):Observable<any>        { return this.http.post<any>(`${API}/orders`, d); }
  updateOrderStatus(id:number,status:string):Observable<any>{ return this.http.patch<any>(`${API}/orders/${id}/status`,{status}); }
  billOrder(id:number,pm:string):Observable<any>{ return this.http.post<any>(`${API}/orders/${id}/bill`,{paymentMethod:pm}); }

  // Customers
  getCustomers():Observable<any[]>          { return this.http.get<any[]>(`${API}/customers`); }
  searchCustomers(name:string):Observable<any[]>{ return this.http.get<any[]>(`${API}/customers/search?name=${name}`); }
  searchCustomersByPhone(phone:string):Observable<any[]>{ return this.http.get<any[]>(`${API}/customers/search-phone?phone=${phone}`); }
  createCustomer(d:any):Observable<any>     { return this.http.post<any>(`${API}/customers`, d); }

  // Analytics
  getDashboard():Observable<any>            { return this.http.get<any>(`${API}/analytics/dashboard`); }
}