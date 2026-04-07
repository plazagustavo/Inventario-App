
package gm.inventarios.servicio;

import gm.inventarios.producto.model.Producto;
import gm.inventarios.producto.repository.ProductoRepositorio;
import gm.inventarios.security.UsuarioActual;
import gm.inventarios.user.model.User;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductoServicio implements IProductoServicio {
    
    @Autowired
    private ProductoRepositorio productoRepositorio;

    @Autowired
    private UsuarioActual usuarioActual;

    @Override
    public List<Producto> listarProductos() {
        User usuario = usuarioActual.obtener();
        if (usuario == null) {
            throw new RuntimeException("Usuario no autenticado");
        }
        return this.productoRepositorio.findByUsuario(usuario);
    }

    @Override
    public Producto buscarProductoPorId(Integer idProducto) {
        User usuario = usuarioActual.obtener();
        if (usuario == null) {
            throw new RuntimeException("Usuario no autenticado");
        }
        return this.productoRepositorio.findByIdAndUsuario(idProducto, usuario).orElse(null);
    }

    @Override
    public Producto guardarProducto(Producto producto) {
        User usuario = usuarioActual.obtener();
        if (usuario == null) {
            throw new RuntimeException("Usuario no autenticado");
        }
        producto.setUsuario(usuario);
        return this.productoRepositorio.save(producto);
    }

    @Override
    public void eliminarProducto(Integer idProducto) {
        User usuario = usuarioActual.obtener();
        if (usuario == null) {
            throw new RuntimeException("Usuario no autenticado");
        }
        Producto producto = this.productoRepositorio.findByIdAndUsuario(idProducto, usuario)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        this.productoRepositorio.delete(producto);
    }
    
}
