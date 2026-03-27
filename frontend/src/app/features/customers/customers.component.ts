import { Component, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../../core/services/api.service';
import { ToastService } from '../../core/services/toast.service';

const TIER_COLORS: Record<string, string> = { GOLD: 'amber', SILVER: 'default', BRONZE: 'default' };
const TIER_ICONS:  Record<string, string>  = { GOLD: '🥇', SILVER: '🥈', BRONZE: '🥉' };

@Component({
  selector: 'app-customers',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './customers.component.html',
  styleUrl: './customers.component.scss'
})
export class CustomersComponent implements OnInit {
  private api   = inject(ApiService);
  private toast = inject(ToastService);

  customers  = signal<any[]>([]);
  filtered   = signal<any[]>([]);
  showModal  = signal(false);
  search     = '';
  form: any  = {};
  tierColors = TIER_COLORS;
  tierIcons  = TIER_ICONS;

  ngOnInit(): void {
    this.api.getCustomers().subscribe({
      next: d => { this.customers.set(d); this.filtered.set(d); },
      error: () => this.toast.error('Failed to load customers')
    });
  }

  applySearch(): void {
    const s = this.search.toLowerCase();
    this.filtered.set(!s
      ? this.customers()
      : this.customers().filter(c => c.name.toLowerCase().includes(s) || c.phone?.includes(s))
    );
  }

  openAdd(): void  { this.form = { name: '', phone: '', email: '' }; this.showModal.set(true); }
  closeModal(): void { this.showModal.set(false); }

  save(): void {
    if (!this.form.name || !this.form.phone) { this.toast.warn('Name and phone required'); return; }
    this.api.createCustomer(this.form).subscribe({
      next: c => { this.customers.update(list => [c, ...list]); this.filtered.update(list => [c, ...list]); this.toast.success('Customer added!'); this.closeModal(); },
      error: () => this.toast.error('Failed')
    });
  }
}
