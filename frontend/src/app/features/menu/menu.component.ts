import { Component, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../../core/services/api.service';
import { ToastService } from '../../core/services/toast.service';

const CATS = ['ALL', 'STARTER', 'MAIN', 'DESSERT', 'BEVERAGE'];
const CAT_ICONS: Record<string, string> = { STARTER: '🥗', MAIN: '🍛', DESSERT: '🍮', BEVERAGE: '🥤', ALL: '🍽️' };

@Component({
  selector: 'app-menu',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './menu.component.html',
  styleUrl: './menu.component.scss'
})
export class MenuComponent implements OnInit {
  private api   = inject(ApiService);
  private toast = inject(ToastService);

  items          = signal<any[]>([]);
  activeCategory = signal('ALL');
  showModal      = signal(false);
  editing        = signal<any>(null);
  deleteTarget   = signal<any>(null);
  saving         = signal(false);
  cats           = CATS;
  catIcons       = CAT_ICONS;
  form: any      = {};

  available(): number { return this.items().filter(i => i.available).length; }

  filtered(): any[] {
    const cat = this.activeCategory();
    return cat === 'ALL' ? this.items() : this.items().filter(i => i.category === cat);
  }

  setCategory(c: string): void { this.activeCategory.set(c); }

  ngOnInit(): void {
    this.api.getMenu().subscribe({
      next: d => this.items.set(d),
      error: () => this.toast.error('Failed to load menu')
    });
  }

  openAdd(): void {
    this.editing.set(null);
    this.form = { name: '', description: '', price: 0, category: 'MAIN', preparationTime: 15, veg: false, available: true };
    this.showModal.set(true);
  }

  openEdit(item: any): void { this.editing.set(item); this.form = { ...item }; this.showModal.set(true); }
  closeModal(): void { this.showModal.set(false); this.editing.set(null); }

  save(): void {
    if (!this.form.name || !this.form.price) { this.toast.warn('Name and price required'); return; }
    this.saving.set(true);
    const obs = this.editing()
      ? this.api.updateMenuItem(this.editing().id, this.form)
      : this.api.createMenuItem(this.form);
    obs.subscribe({
      next: item => {
        if (this.editing()) this.items.update(list => list.map(i => i.id === item.id ? item : i));
        else this.items.update(list => [...list, item]);
        this.toast.success(this.editing() ? 'Item updated!' : 'Item added!');
        this.saving.set(false); this.closeModal();
      },
      error: () => { this.toast.error('Failed to save'); this.saving.set(false); }
    });
  }

  toggleItem(item: any): void {
    this.api.toggleMenuItem(item.id).subscribe({
      next: updated => { this.items.update(list => list.map(i => i.id === updated.id ? updated : i)); this.toast.success(`${updated.name} ${updated.available ? 'enabled' : 'disabled'}`); },
      error: () => this.toast.error('Failed')
    });
  }

  confirmDelete(item: any): void { this.deleteTarget.set(item); }

  doDelete(): void {
    this.api.deleteMenuItem(this.deleteTarget()!.id).subscribe({
      next: () => { this.items.update(list => list.filter(i => i.id !== this.deleteTarget()!.id)); this.toast.success('Deleted!'); this.deleteTarget.set(null); },
      error: () => this.toast.error('Failed')
    });
  }
}
