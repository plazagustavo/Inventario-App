package gm.inventarios.producto.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateProductoRequest {

    @NotBlank(message = "La descripción es requerida")
    private String descripcion;

    @NotNull(message = "El precio es requerido")
    @Positive(message = "El precio debe ser mayor a 0")
    private Double precio;

    @NotNull(message = "La existencia es requerida")
    @PositiveOrZero(message = "La existencia no puede ser negativa")
    private Integer existencia;
}
