import { Component, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { ApiService } from '../../core/services/api.service';
import { ToastService } from '../../core/services/toast.service';

const STATUS_FLOW   = ['PENDING','PREPARING','READY','SERVED','BILLED','CANCELLED'];
const STATUS_COLORS: Record<string,string> = { PENDING:'warning', PREPARING:'teal', READY:'success', SERVED:'default', BILLED:'amber', CANCELLED:'danger' };
const STATUS_ICONS:  Record<string,string> = { PENDING:'⏳', PREPARING:'👨‍🍳', READY:'✅', SERVED:'🍽️', BILLED:'💳', CANCELLED:'✕' };

@Component({
  selector: 'app-orders',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './orders.component.html',
  styleUrl: './orders.component.scss'
})
export class OrdersComponent implements OnInit {
  private api   = inject(ApiService);
  private toast = inject(ToastService);

  orders       = signal<any[]>([]);
  loading      = signal(true);
  activeTab    = signal('ALL');
  billingOrder = signal<any>(null);
  billing      = signal(false);
  paymentMethod = 'CASH';
  statuses     = STATUS_FLOW;
  statusColors = STATUS_COLORS;
  statusIcons  = STATUS_ICONS;

  filtered(): any[]     { const t = this.activeTab(); return t === 'ALL' ? this.orders() : this.orders().filter(o => o.status === t); }
  activeCount(): number { return this.orders().filter(o => !['BILLED','CANCELLED'].includes(o.status)).length; }
  countByStatus(s: string): number { return this.orders().filter(o => o.status === s).length; }
  nextStatus(s: string): string | null { const i = STATUS_FLOW.indexOf(s); return (i >= 0 && i < 3) ? STATUS_FLOW[i + 1] : null; }
  setTab(t: string): void { this.activeTab.set(t); }

  ngOnInit(): void { this.load(); }

  load(): void {
    this.api.getOrders().subscribe({
      next: d => { this.orders.set(d.sort((a: any, b: any) => new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime())); this.loading.set(false); },
      error: () => this.loading.set(false)
    });
  }

  advance(order: any): void {
    const next = this.nextStatus(order.status);
    if (!next) return;
    this.api.updateOrderStatus(order.id, next).subscribe({
      next: u => { this.orders.update(list => list.map(o => o.id === u.id ? u : o)); this.toast.success(`Order ${u.orderNumber} → ${next}`); },
      error: () => this.toast.error('Failed')
    });
  }

  cancel(order: any): void {
    this.api.updateOrderStatus(order.id, 'CANCELLED').subscribe({
      next: u => { this.orders.update(list => list.map(o => o.id === u.id ? u : o)); this.toast.success('Order cancelled'); },
      error: () => this.toast.error('Failed')
    });
  }

  openBill(order: any): void { this.billingOrder.set(order); this.paymentMethod = 'CASH'; }

  confirmBill(): void {
    const order = this.billingOrder();
    if (!order) return;
    this.billing.set(true);
    this.api.billOrder(order.id, this.paymentMethod).subscribe({
      next: bill => { this.toast.success(`Bill ₹${bill.grandTotal} · ${bill.paymentMethod} · ${bill.transactionId}`); this.load(); this.billing.set(false); this.billingOrder.set(null); },
      error: () => { this.toast.error('Billing failed'); this.billing.set(false); }
    });
  }
}
