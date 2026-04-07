package gm.inventarios.auth.dto;

import gm.inventarios.user.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private String tipo = "Bearer";
    private UserDTO usuario;
    private String mensaje;
}
