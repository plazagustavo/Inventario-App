package gm.inventarios.excepcion;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorRespuesta {
    private int codigo;
    private String mensaje;
    private String path;
    private LocalDateTime fecha;
    private List<Map<String, String>> detalles;

    public ErrorRespuesta(int codigo, String mensaje, String path) {
        this.codigo = codigo;
        this.mensaje = mensaje;
        this.path = path;
        this.fecha = LocalDateTime.now();
    }

    public ErrorRespuesta(int codigo, String mensaje, String path, List<Map<String, String>> detalles) {
        this(codigo, mensaje, path);
        this.detalles = detalles;
    }
}