import { Component, inject, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink, Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { Producto } from '../producto';
import { ProductoService } from '../producto.service';

@Component({
  selector: 'app-producto-lista',
  standalone: true, 
  imports: [CommonModule, RouterLink, FormsModule], 
  templateUrl: './producto-lista.html',
})
export class ProductoLista implements OnInit { 
  productos: Producto[] = [];

  // Modal de venta
  mostrarModalVenta = false;
  productoSeleccionado: Producto | null = null;
  cantidadVenta = 1;

  private productoServicio = inject(ProductoService);
  private cdr = inject(ChangeDetectorRef);
  private enrutador = inject(Router);

  ngOnInit() {
    this.obtenerProductos();
  }

  private obtenerProductos(): void {
    this.productoServicio.obtenerProductosLista().subscribe({
      next: (datos) => {
        this.productos = datos;
        this.cdr.detectChanges();
      },
      error: (error) => {
        console.error("Error al obtener los productos: ", error);
      }
    });
  }

  editarProducto(id: number){
    this.enrutador.navigate(['editar-producto', id]);
  }

  eliminarProducto(id: number){
    this.productoServicio.eliminarProducto(id).subscribe({
      next: (datos) => this.obtenerProductos(),
      error: (errores) => {
        console.error("Error al eliminar el producto: ", errores);
      }
    });
  }

  abrirModalVenta(producto: Producto): void {
    this.productoSeleccionado = producto;
    this.cantidadVenta = 1;
    this.mostrarModalVenta = true;
  }

  cerrarModalVenta(): void {
    this.mostrarModalVenta = false;
    this.productoSeleccionado = null;
    this.cantidadVenta = 1;
  }

  confirmarVenta(): void {
    if (!this.productoSeleccionado || this.cantidadVenta <= 0) return;

    const nuevoStock = this.productoSeleccionado.existencia - this.cantidadVenta;
    
    const productoActualizado = {
      ...this.productoSeleccionado,
      existencia: nuevoStock
    };

    this.productoServicio.editarProducto(this.productoSeleccionado.id, productoActualizado).subscribe({
      next: () => {
        this.cerrarModalVenta();
        this.obtenerProductos();
      },
      error: (err) => console.error("Error al registrar venta:", err)
    });
  }
}