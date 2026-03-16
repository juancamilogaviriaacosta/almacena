import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Router } from "@angular/router";
import { jwtDecode } from "jwt-decode";

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private router: Router, private http: HttpClient) {    
  }

  isAdmin(): boolean {
    let token = localStorage.getItem('token');
    return token ? jwtDecode<any>(token).role === 'Admin' : false;
  }

  isUser(): boolean {
    let token = localStorage.getItem('token');
    return token ? jwtDecode<any>(token).role === 'User' : false;
  }

  isLoggedIn(): boolean {
    return !!localStorage.getItem('token');
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
        if(response && response.token) {
          localStorage.setItem('token', response.token);
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