import { Component, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { ApiService } from '../../core/services/api.service';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss'
})
export class DashboardComponent implements OnInit {
  auth = inject(AuthService);
  private api = inject(ApiService);

  today = new Date();
  greeting = (() => {
    const h = new Date().getHours();
    return h < 12 ? 'Good morning' : h < 17 ? 'Good afternoon' : 'Good evening';
  })();

  stats = signal<any>({
    todayRevenue: 0, monthRevenue: 0, totalOrders: 0,
    activeOrders: 0, totalCustomers: 0,
    pendingOrders: 0, preparingOrders: 0, topSellingItems: []
  });

  orderStatuses = [
    { label: 'Pending',   icon: '⏳', count: 0, color: 'amber' },
    { label: 'Preparing', icon: '👨‍🍳', count: 0, color: 'teal' },
    { label: 'Ready',     icon: '✅', count: 0, color: 'success' },
  ];

  firstName(): string {
    const name = this.auth.user()?.fullName;
    return name ? name.split(' ')[0] : 'there';
  }

  barWidth(item: any, items: any[]): number {
    if (!items || items.length === 0) return 0;
    const max = items[0]?.quantity || 1;
    return (item.quantity / max) * 100;
  }

  ngOnInit(): void {
    this.api.getDashboard().subscribe({
      next: d => {
        this.stats.set(d);
        this.orderStatuses[0].count = d.pendingOrders   || 0;
        this.orderStatuses[1].count = d.preparingOrders || 0;
        this.orderStatuses[2].count = d.readyOrders     || 0;
      },
      error: () => {}
    });
  }
}
