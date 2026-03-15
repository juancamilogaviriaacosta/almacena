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

  login() {
    localStorage.setItem("role", "Admin");
    this.router.navigate(['/dashboard']);

    /*this.http.post('/api/auth', this.model).subscribe({
      next: (response:any) => {
        if(response && response['id'] && response['role']) {
          localStorage.setItem('id', response['id']);
          localStorage.setItem('role', response['role']);
          window.location.href = '/';
        } else {
          alert('Error en usuario o contraseña');
        }
      },
      error: (error) => {
        alert('Error en usuario o contraseña');
      }
    });*/
  
  }

  logout() {    
    localStorage.removeItem('id');
    localStorage.removeItem("role");
    this.router.navigate(['/']);
  }
}