package gm.inventarios.producto.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ProductoDTO {
    private Integer id;
    private String descripcion;
    private Double precio;
    private Integer existencia;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;

    public static ProductoDTO fromEntity(gm.inventarios.producto.model.Producto producto) {
        return ProductoDTO.builder()
                .id(producto.getId())
                .descripcion(producto.getDescripcion())
                .precio(producto.getPrecio())
                .existencia(producto.getExistencia())
                .fechaCreacion(producto.getFechaCreacion())
                .fechaActualizacion(producto.getFechaActualizacion())
                .build();
    }
}
