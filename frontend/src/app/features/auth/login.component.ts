import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../core/services/auth.service';
import { ToastService } from '../../core/services/toast.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {
  private auth  = inject(AuthService);
  private router = inject(Router);
  private toast = inject(ToastService);

  username = '';
  password = '';
  loading  = signal(false);
  showPass = signal(false);

  togglePass(): void { this.showPass.set(!this.showPass()); }
  fill(u: string, p: string): void { this.username = u; this.password = p; }

  onSubmit(): void {
    if (!this.username || !this.password) return;
    this.loading.set(true);
    this.auth.login(this.username, this.password).subscribe({
      next: () => this.router.navigate(['/dashboard']),
      error: (e) => { this.toast.error(e.error?.error || 'Invalid credentials'); this.loading.set(false); }
    });
  }
}
