import { Injectable } from "@angular/core";

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  isAdmin(): boolean {
    return localStorage.getItem('role') === 'Admin';
  }

  isUser(): boolean {
    return localStorage.getItem('role') === 'User';
  }

  isloggedIn(): boolean {
    return this.isAdmin() || this.isUser();
  }

  logout() {
    localStorage.removeItem('id');
    localStorage.removeItem('role');
    window.location.href = '/';
  }
}