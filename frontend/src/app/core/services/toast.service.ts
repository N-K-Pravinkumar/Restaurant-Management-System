import { Injectable, signal } from '@angular/core';
export interface Toast { id:string; message:string; type:'success'|'error'|'warn'|'info'; }
@Injectable({ providedIn:'root' })
export class ToastService {
  readonly toasts = signal<Toast[]>([]);
  show(message:string, type:Toast['type']='info', duration=3500) {
    const id = Math.random().toString(36).slice(2);
    this.toasts.update(t=>[...t,{id,message,type}]);
    setTimeout(()=>this.dismiss(id), duration);
  }
  success(m:string){ this.show(m,'success'); }
  error(m:string)  { this.show(m,'error'); }
  warn(m:string)   { this.show(m,'warn'); }
  dismiss(id:string){ this.toasts.update(t=>t.filter(x=>x.id!==id)); }
}