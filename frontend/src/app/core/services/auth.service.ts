import { Injectable, signal, computed } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { tap } from 'rxjs/operators';
import { Observable } from 'rxjs';

const API = 'http://localhost:8080/api';

export interface LoginUser { userId:number; username:string; role:string; fullName:string; token:string; }

@Injectable({ providedIn:'root' })
export class AuthService {
  private _user = signal<LoginUser|null>(null);
  readonly user       = this._user.asReadonly();
  readonly isLoggedIn = computed(() => !!this._user());
  readonly isAdmin    = computed(() => ['ADMIN','MANAGER'].includes(this._user()?.role ?? ''));

  constructor(private http:HttpClient, private router:Router) { this.restore(); }

  private restore(): void {
    const stored = localStorage.getItem('savora_user');
    if (stored) {
      try { this._user.set(JSON.parse(stored)); } catch { this.clearSession(); }
    }
  }

  login(username:string, password:string): Observable<LoginUser> {
    return this.http.post<LoginUser>(`${API}/auth/login`, {username, password}).pipe(
      tap((res: LoginUser) => {
        localStorage.setItem('savora_token', res.token);
        localStorage.setItem('savora_user', JSON.stringify(res));
        this._user.set(res);
      })
    );
  }

  logout(): void { this.clearSession(); this.router.navigate(['/login']); }

  private clearSession(): void {
    localStorage.removeItem('savora_token');
    localStorage.removeItem('savora_user');
    this._user.set(null);
  }

  getToken(): string | null { return localStorage.getItem('savora_token'); }
}