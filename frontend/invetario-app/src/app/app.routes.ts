import { Routes } from '@angular/router';
import { ProductoLista } from './producto-lista/producto-lista';
import { AgregarProducto } from './agregar-producto/agregar-producto';
import { EditarProducto } from './editar-producto/editar-producto';
import { LoginComponent } from './login/login';
import { RegisterComponent } from './register/register';
import { authGuard } from './auth.guard';

export const routes: Routes = [
    {path: '', redirectTo: 'productos', pathMatch: 'full'},
    
    // Rutas públicas
    {path: 'login', component: LoginComponent},
    {path: 'register', component: RegisterComponent},
    
    // Rutas protegidas
    {path: 'productos', component: ProductoLista, canActivate: [authGuard]},
    {path: 'agregar-producto', component: AgregarProducto, canActivate: [authGuard]},
    {path: 'editar-producto/:id', component: EditarProducto, canActivate: [authGuard]}
];