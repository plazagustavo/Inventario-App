package gm.inventarios.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class UserDTO {
    private Integer id;
    private String email;
    private String nombre;
    private String apellido;
    private String role;
    private LocalDateTime fechaCreacion;
}
