import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';


@Component({
  selector: 'app-login',
  imports: [FormsModule],
  templateUrl: './login.html',
  styleUrl: './login.css'
})
export class Login {
  usuario = '';
  password = '';
  mensajeError = '';

  constructor(private http: HttpClient, private router: Router) {}

  login() {
    this.router.navigate(['/home']);
    /*this.http.post(environment.apiUrl+'/api/login', {
      usuario: this.usuario,
      password: this.password
    }, { responseType: 'text' }).subscribe({
      next: (response) => {
        if (response === 'Ok') {
          this.router.navigate(['/home']);
        } else {
          this.mensajeError = 'Usuario o contraseña incorrectos';
        }
      },
      error: () => {
        this.mensajeError = 'Error de conexión';
      }
    });*/
  }
}
