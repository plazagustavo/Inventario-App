import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../auth.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  template: `
    <div class="register-container">
      <h2>Crear Cuenta</h2>
      
      <form (ngSubmit)="onSubmit()" *ngIf="!loading">
        <div class="form-group">
          <label for="nombre">Nombre</label>
          <input 
            type="text" 
            id="nombre" 
            [(ngModel)]="nombre" 
            name="nombre" 
            required
            placeholder="Tu nombre"
          />
        </div>
        
        <div class="form-group">
          <label for="apellido">Apellido</label>
          <input 
            type="text" 
            id="apellido" 
            [(ngModel)]="apellido" 
            name="apellido" 
            placeholder="Tu apellido"
          />
        </div>
        
        <div class="form-group">
          <label for="email">Email</label>
          <input 
            type="email" 
            id="email" 
            [(ngModel)]="email" 
            name="email" 
            required
            placeholder="tu@email.com"
          />
        </div>
        
        <div class="form-group">
          <label for="password">Contraseña</label>
          <input 
            type="password" 
            id="password" 
            [(ngModel)]="password" 
            name="password" 
            required
            placeholder="••••••••"
          />
        </div>
        
        <div *ngIf="error" class="error">{{ error }}</div>
        
        <button type="submit" [disabled]="!formValid()">
          {{ loading ? 'Creando cuenta...' : 'Crear Cuenta' }}
        </button>
      </form>
      
      <p class="login-link">
        ¿Ya tienes cuenta? <a routerLink="/login">Inicia Sesión</a>
      </p>
    </div>
  `,
  styles: [`
    .register-container {
      max-width: 400px;
      margin: 50px auto;
      padding: 20px;
    }
    .form-group {
      margin-bottom: 15px;
    }
    label {
      display: block;
      margin-bottom: 5px;
      font-weight: bold;
    }
    input {
      width: 100%;
      padding: 10px;
      border: 1px solid #ccc;
      border-radius: 4px;
    }
    button {
      width: 100%;
      padding: 10px;
      background: #28a745;
      color: white;
      border: none;
      border-radius: 4px;
      cursor: pointer;
    }
    button:disabled {
      background: #ccc;
    }
    .error {
      color: red;
      margin: 10px 0;
    }
    .login-link {
      text-align: center;
      margin-top: 15px;
    }
  `]
})
export class RegisterComponent {
  private authService = inject(AuthService);
  private router = inject(Router);
  
  nombre = '';
  apellido = '';
  email = '';
  password = '';
  loading = false;
  error = '';

  formValid(): boolean {
    return !!(this.nombre && this.email && this.password);
  }

  onSubmit() {
    if (!this.formValid()) return;
    
    this.loading = true;
    this.error = '';
    
    this.authService.register({ 
      nombre: this.nombre, 
      apellido: this.apellido,
      email: this.email, 
      password: this.password 
    }).subscribe({
      next: () => {
        this.router.navigate(['/login']);
      },
      error: (err) => {
        this.loading = false;
        this.error = err.error?.mensaje || 'Error al registrar';
      }
    });
  }
}