import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-login',
  imports: [FormsModule, CommonModule],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {

  model: any = {
    username: '',
    password: '',
  };

  constructor(private http: HttpClient) {
  }

  login(loginForm: any) {
    this.http.post('/api/auth', this.model).subscribe({
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
    });
    
  }
}