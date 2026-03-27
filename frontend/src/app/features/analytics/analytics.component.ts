import { Component, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ApiService } from '../../core/services/api.service';

@Component({
  selector: 'app-analytics',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './analytics.component.html',
  styleUrl: './analytics.component.scss'
})
export class AnalyticsComponent implements OnInit {
  private api = inject(ApiService);

  data = signal<any>({
    todayRevenue: 0, monthRevenue: 0, totalOrders: 0, totalCustomers: 0,
    topSellingItems: [], pendingOrders: 0, preparingOrders: 0, readyOrders: 0, activeOrders: 0
  });

  statusList = [
    { label: 'Pending',   key: 'pendingOrders',   icon: '⏳', color: '#c8860a' },
    { label: 'Preparing', key: 'preparingOrders', icon: '👨‍🍳', color: '#0d7377' },
    { label: 'Ready',     key: 'readyOrders',     icon: '✅', color: '#1a6b3a' },
  ];

  ngOnInit(): void {
    this.api.getDashboard().subscribe({ next: d => this.data.set(d), error: () => {} });
  }

  getCount(key: string): number { return this.data()[key] || 0; }

  getStatusPct(key: string): number {
    const total = (this.data().pendingOrders || 0) + (this.data().preparingOrders || 0) + (this.data().readyOrders || 0);
    return total > 0 ? ((this.data()[key] || 0) / total) * 100 : 0;
  }

  topCategory(): string {
    const items = this.data().topSellingItems || [];
    if (items.length === 0) return 'N/A';
    return items[0]?.name?.includes('Biryani') || items[0]?.name?.includes('Curry') ? 'Main Course' : 'Starters';
  }
}
