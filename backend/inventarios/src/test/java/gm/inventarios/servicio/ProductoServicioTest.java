package gm.inventarios.servicio;

import gm.inventarios.producto.model.Producto;
import gm.inventarios.producto.repository.ProductoRepositorio;
import gm.inventarios.security.UsuarioActual;
import gm.inventarios.user.model.RoleEnum;
import gm.inventarios.user.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductoServicioTest {

    @Mock
    private ProductoRepositorio productoRepositorio;

    @Mock
    private UsuarioActual usuarioActual;

    @InjectMocks
    private ProductoServicio productoServicio;

    private User testUser;
    private Producto testProducto;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1)
                .email("test@test.com")
                .nombre("Test")
                .role(RoleEnum.ROLE_USER)
                .activo(true)
                .build();

        testProducto = Producto.builder()
                .id(1)
                .descripcion("Producto test")
                .precio(100.0)
                .existencia(10)
                .usuario(testUser)
                .fechaCreacion(LocalDateTime.now())
                .build();
    }

    @Test
    void listarProductos_UsuarioAutenticado_RetornaListaProductos() {
        when(usuarioActual.obtener()).thenReturn(testUser);
        when(productoRepositorio.findByUsuario(testUser)).thenReturn(Arrays.asList(testProducto));

        List<Producto> productos = productoServicio.listarProductos();

        assertNotNull(productos);
        assertEquals(1, productos.size());
        assertEquals("Producto test", productos.get(0).getDescripcion());
        verify(productoRepositorio).findByUsuario(testUser);
    }

    @Test
    void listarProductos_UsuarioNoAutenticado_ThrowsException() {
        when(usuarioActual.obtener()).thenReturn(null);

        assertThrows(RuntimeException.class, () -> productoServicio.listarProductos());
    }

    @Test
    void buscarProductoPorId_ProductoDelUsuario_RetornaProducto() {
        when(usuarioActual.obtener()).thenReturn(testUser);
        when(productoRepositorio.findByIdAndUsuario(1, testUser)).thenReturn(Optional.of(testProducto));

        Producto producto = productoServicio.buscarProductoPorId(1);

        assertNotNull(producto);
        assertEquals(1, producto.getId());
    }

    @Test
    void buscarProductoPorId_ProductoNoExiste_RetornaNull() {
        when(usuarioActual.obtener()).thenReturn(testUser);
        when(productoRepositorio.findByIdAndUsuario(1, testUser)).thenReturn(Optional.empty());

        Producto producto = productoServicio.buscarProductoPorId(1);

        assertNull(producto);
    }

    @Test
    void guardarProducto_UsuarioAutenticado_AsignaUsuarioYGuarda() {
        when(usuarioActual.obtener()).thenReturn(testUser);
        when(productoRepositorio.save(any(Producto.class))).thenReturn(testProducto);

        Producto nuevoProducto = Producto.builder()
                .descripcion("Nuevo producto")
                .precio(50.0)
                .existencia(5)
                .build();

        Producto saved = productoServicio.guardarProducto(nuevoProducto);

        assertNotNull(saved);
        assertEquals(testUser, saved.getUsuario());
        verify(productoRepositorio).save(any(Producto.class));
    }

    @Test
    void guardarProducto_UsuarioNoAutenticado_ThrowsException() {
        when(usuarioActual.obtener()).thenReturn(null);

        assertThrows(RuntimeException.class, () -> productoServicio.guardarProducto(new Producto()));
    }

    @Test
    void eliminarProducto_ProductoDelUsuario_EliminaCorrectamente() {
        when(usuarioActual.obtener()).thenReturn(testUser);
        when(productoRepositorio.findByIdAndUsuario(1, testUser)).thenReturn(Optional.of(testProducto));

        productoServicio.eliminarProducto(1);

        verify(productoRepositorio).delete(testProducto);
    }

    @Test
    void eliminarProducto_ProductoNoEncontrado_ThrowsException() {
        when(usuarioActual.obtener()).thenReturn(testUser);
        when(productoRepositorio.findByIdAndUsuario(1, testUser)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> productoServicio.eliminarProducto(1));
    }
}