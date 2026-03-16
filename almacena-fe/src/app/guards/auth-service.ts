import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Router } from "@angular/router";

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private router: Router, private http: HttpClient) {
    
  }

  isAdmin(): boolean {
    return localStorage.getItem('role') === 'Admin';
  }

  isUser(): boolean {
    return localStorage.getItem('role') === 'User';
  }

  isloggedIn(): boolean {
    return this.isAdmin() || this.isUser();
  }

  isLoginRoute(): boolean {
    return this.router.url === '/login';
  }

  toLogin() {
    this.router.navigate(['/login']);
  }

  login(form: any) {
    this.http.post('/api/auth', form).subscribe({
      next: (response:any) => {
        if(response && response.id && response.role && response.tenant) {
          localStorage.setItem('role', response.role);
          localStorage.setItem('id', response.id);
          localStorage.setItem('tenantId', response.tenant.id);
          localStorage.setItem('tenantName', response.tenant.name);
          this.router.navigate(['/dashboard']);
        } else {
          alert('Error en usuario o contraseña');
        }
      },
      error: (error) => {
        alert('Error en usuario o contraseña');
      }
    });
  
  }

  logout() {    
    localStorage.clear();
    this.router.navigate(['/']);
  }
}