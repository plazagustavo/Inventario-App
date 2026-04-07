package gm.inventarios.auth.service;

import gm.inventarios.auth.dto.AuthResponse;
import gm.inventarios.auth.dto.LoginRequest;
import gm.inventarios.auth.dto.RegisterRequest;
import gm.inventarios.auth.jwt.JwtTokenProvider;
import gm.inventarios.user.dto.UserDTO;
import gm.inventarios.user.model.RoleEnum;
import gm.inventarios.user.model.User;
import gm.inventarios.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    /**
     * Registra un nuevo usuario
     */
    public AuthResponse register(RegisterRequest registerRequest) {
        log.info("Registrando nuevo usuario con email: {}", registerRequest.getEmail());

        // Validar que el email no exista
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            log.warn("Intento de registro con email duplicado: {}", registerRequest.getEmail());
            throw new RuntimeException("El email ya está registrado");
        }

        // Crear nuevo usuario
        User user = User.builder()
                .email(registerRequest.getEmail())
                .nombre(registerRequest.getNombre())
                .apellido(registerRequest.getApellido())
                .password(passwordEncoder.encode(registerRequest.getPassword())) // ⭐ BCrypt
                .role(RoleEnum.ROLE_USER) // Por defecto, rol USER
                .activo(true)
                .build();

        // Guardar en BD
        User savedUser = userRepository.save(user);
        log.info("Usuario registrado exitosamente con ID: {}", savedUser.getId());

        // Generar token JWT
        String token = jwtTokenProvider.generateTokenFromEmail(savedUser.getEmail());

        // Construir UserDTO sin password
        UserDTO userDTO = UserDTO.builder()
                .id(savedUser.getId())
                .email(savedUser.getEmail())
                .nombre(savedUser.getNombre())
                .apellido(savedUser.getApellido())
                .role(savedUser.getRole().toString())
                .fechaCreacion(savedUser.getFechaCreacion())
                .build();

        // Retornar respuesta
        return AuthResponse.builder()
                .token(token)
                .tipo("Bearer")
                .usuario(userDTO)
                .mensaje("Usuario registrado exitosamente")
                .build();
    }

    /**
     * Autentica un usuario y retorna JWT
     */
    public AuthResponse login(LoginRequest loginRequest) {
        log.info("Intento de login para email: {}", loginRequest.getEmail());

        // Autenticar credenciales
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        log.info("Credenciales válidas para: {}", loginRequest.getEmail());

        // Generar token JWT
        String token = jwtTokenProvider.generateToken(authentication);

        // Cargar usuario de BD
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Construir UserDTO
        UserDTO userDTO = UserDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .nombre(user.getNombre())
                .apellido(user.getApellido())
                .role(user.getRole().toString())
                .fechaCreacion(user.getFechaCreacion())
                .build();

        log.info("Login exitoso para usuario: {}", user.getId());

        // Retornar respuesta
        return AuthResponse.builder()
                .token(token)
                .tipo("Bearer")
                .usuario(userDTO)
                .mensaje("Login exitoso")
                .build();
    }
}
