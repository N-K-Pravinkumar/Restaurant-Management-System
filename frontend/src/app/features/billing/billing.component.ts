import { Component, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../../core/services/api.service';
import { ToastService } from '../../core/services/toast.service';

@Component({
  selector: 'app-billing',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './billing.component.html',
  styleUrl: './billing.component.scss'
})
export class BillingComponent implements OnInit {
  private api   = inject(ApiService);
  private toast = inject(ToastService);

  orders         = signal<any[]>([]);
  selectedMethod: Record<number, string> = {};
  processing:     Record<number, boolean> = {};

  pendingOrders(): any[] { return this.orders().filter(o => o.status === 'SERVED'); }
  billedOrders():  any[] {
    return this.orders()
      .filter(o => o.status === 'BILLED')
      .sort((a: any, b: any) => new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime());
  }

  ngOnInit(): void {
    this.api.getOrders().subscribe({
      next: d => {
        this.orders.set(d);
        d.forEach((o: any) => { if (!this.selectedMethod[o.id]) this.selectedMethod[o.id] = 'CASH'; });
      },
      error: () => this.toast.error('Failed to load orders')
    });
  }

  setMethod(orderId: number, method: string): void {
    this.selectedMethod = { ...this.selectedMethod, [orderId]: method };
  }

  bill(order: any): void {
    this.processing = { ...this.processing, [order.id]: true };
    this.api.billOrder(order.id, this.selectedMethod[order.id] || 'CASH').subscribe({
      next: bill => {
        this.toast.success(`₹${bill.grandTotal} collected via ${bill.paymentMethod} · TXN: ${bill.transactionId}`);
        this.orders.update(list => list.map(o => o.id === order.id ? { ...o, status: 'BILLED', paymentMethod: bill.paymentMethod } : o));
        this.processing = { ...this.processing, [order.id]: false };
      },
      error: () => { this.toast.error('Billing failed'); this.processing = { ...this.processing, [order.id]: false }; }
    });
  }
}
