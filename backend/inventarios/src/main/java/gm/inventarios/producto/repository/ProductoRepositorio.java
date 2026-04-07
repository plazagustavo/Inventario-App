package gm.inventarios.producto.repository;

import gm.inventarios.producto.model.Producto;
import gm.inventarios.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepositorio extends JpaRepository<Producto, Integer> {

    List<Producto> findByUsuario(User usuario);

    Optional<Producto> findByIdAndUsuario(Integer id, User usuario);
}
