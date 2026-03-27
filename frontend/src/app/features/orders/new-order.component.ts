import { Component, inject, OnInit, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { ApiService } from '../../core/services/api.service';
import { AuthService } from '../../core/services/auth.service';
import { ToastService } from '../../core/services/toast.service';

const CAT_ICONS: Record<string, string> = { STARTER: '🥗', MAIN: '🍛', DESSERT: '🍮', BEVERAGE: '🥤' };

@Component({
  selector: 'app-new-order',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './new-order.component.html',
  styleUrl: './new-order.component.scss'
})
export class NewOrderComponent implements OnInit {
  router        = inject(Router);
  private api   = inject(ApiService);
  private auth  = inject(AuthService);
  private toast = inject(ToastService);

  menuItems      = signal<any[]>([]);
  filteredMenu   = signal<any[]>([]);
  cart           = signal<any[]>([]);
  customer       = signal<any>(null);
  activeCategory = signal('ALL');
  cats           = ['ALL', 'STARTER', 'MAIN', 'DESSERT', 'BEVERAGE'];
  catIcons       = CAT_ICONS;

  search = ''; tableNumber = 1; customerPhone = ''; notes = '';
  placing = signal(false);

  subtotal = computed(() => this.cart().reduce((s, i) => s + i.price * i.qty, 0));
  tax      = computed(() => this.subtotal() * 0.05);
  total    = computed(() => this.subtotal() + this.tax());

  ngOnInit(): void {
    this.api.getMenuAvailable().subscribe({
      next: d => { this.menuItems.set(d); this.filteredMenu.set(d); },
      error: () => this.toast.error('Could not load menu')
    });
  }

  filterMenu(): void {
    const s   = this.search.toLowerCase();
    const cat = this.activeCategory();
    this.filteredMenu.set(this.menuItems().filter(i =>
      (cat === 'ALL' || i.category === cat) && (!s || i.name.toLowerCase().includes(s))
    ));
  }

  addToCart(item: any): void {
    this.cart.update(c => {
      const existing = c.find(i => i.id === item.id);
      if (existing) return c.map(i => i.id === item.id ? { ...i, qty: i.qty + 1 } : i);
      return [...c, { id: item.id, name: item.name, price: Number(item.price), qty: 1 }];
    });
  }

  removeFromCart(id: number): void {
    this.cart.update(c => {
      const item = c.find(i => i.id === id);
      if (item && item.qty > 1) return c.map(i => i.id === id ? { ...i, qty: i.qty - 1 } : i);
      return c.filter(i => i.id !== id);
    });
  }

  getCartQty(id: number): number { return this.cart().find(i => i.id === id)?.qty || 0; }

  lookupCustomer(): void {
    if (!this.customerPhone) return;
    this.api.searchCustomersByPhone(this.customerPhone).subscribe({
      next: list => { if (list.length > 0) this.customer.set(list[0]); },
      error: () => {}
    });
  }

  placeOrder(): void {
    if (this.cart().length === 0) return;
    this.placing.set(true);
    const payload = {
      tableNumber: this.tableNumber,
      customerId:  this.customer()?.id  || null,
      waiterId:    this.auth.user()?.userId || null,
      notes:       this.notes,
      items:       this.cart().map(i => ({ menuItemId: i.id, quantity: i.qty, specialInstructions: '' }))
    };
    this.api.createOrder(payload).subscribe({
      next: order => { this.toast.success(`Order #${order.orderNumber} placed!`); this.router.navigate(['/orders']); },
      error: () => { this.toast.error('Failed to place order'); this.placing.set(false); }
    });
  }
}
