package gm.inventarios.servicio;

import gm.inventarios.producto.model.Producto;
import java.util.List;


public interface IProductoServicio {
    /**
     * Obtiene todos los productos almacenados en la base de datos.
     * @return Una lista completa de objetos Producto.
     */
    List<Producto> listarProductos();
    /**
     * Busca un producto específico utilizando su identificador único.
     * @param idProducto El ID del producto a buscar.
     * @return El producto encontrado o null si no existe.
     */
    Producto buscarProductoPorId(Integer idProducto);
    /**
     * Guarda un producto nuevo o actualiza uno existente.
     * Si el ID es null, se crea; si el ID ya existe, se sobreescribe (update).
     * @param producto El objeto producto con la información a persistir.
     */
    Producto guardarProducto(Producto producto);
    /**
     * Elimina un producto de la base de datos de forma permanente.
     * @param idProducto El identificador del producto que se desea borrar.
     */
    void eliminarProducto(Integer idProducto);
    
}
