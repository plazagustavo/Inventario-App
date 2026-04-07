import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ProductoService } from '../producto.service';
import { Producto } from '../producto';
import { Router } from '@angular/router';

@Component({
  selector: 'app-agregar-producto',
  imports: [FormsModule],
  templateUrl: './agregar-producto.html',
})
export class AgregarProducto {

  producto: Producto = new Producto();

  private productoServicio = inject(ProductoService);
  private enrutador = inject(Router);

  onSubmit() {
    this.guardarProducto();
  }

  guardarProducto(){
    this.productoServicio.agregarProducto(this.producto).subscribe({
      next: (datos) => {
        console.log("¡Producto agregado!", datos);
        this.irListaProductos();
      },
      error: (error: any) => {
        console.error("Error al agregar el producto: ", error);
      }

    });
  }

  irListaProductos(){
    this.enrutador.navigate(['/productos']);
  }
}