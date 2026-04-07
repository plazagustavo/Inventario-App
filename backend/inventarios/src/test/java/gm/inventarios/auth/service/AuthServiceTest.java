package gm.inventarios.auth.service;

import gm.inventarios.auth.dto.AuthResponse;
import gm.inventarios.auth.dto.LoginRequest;
import gm.inventarios.auth.dto.RegisterRequest;
import gm.inventarios.auth.jwt.JwtTokenProvider;
import gm.inventarios.user.dto.UserDTO;
import gm.inventarios.user.model.RoleEnum;
import gm.inventarios.user.model.User;
import gm.inventarios.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private AuthService authService;

    private User testUser;
    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1)
                .email("test@test.com")
                .password("encodedPassword")
                .nombre("Test")
                .apellido("User")
                .role(RoleEnum.ROLE_USER)
                .activo(true)
                .fechaCreacion(LocalDateTime.now())
                .build();

        registerRequest = new RegisterRequest();
        registerRequest.setEmail("test@test.com");
        registerRequest.setPassword("password123");
        registerRequest.setNombre("Test");
        registerRequest.setApellido("User");

        loginRequest = new LoginRequest();
        loginRequest.setEmail("test@test.com");
        loginRequest.setPassword("password123");
    }

    @Test
    void register_NuevoUsuario_ReturnsAuthResponse() {
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(jwtTokenProvider.generateTokenFromEmail(anyString())).thenReturn("jwtToken");

        AuthResponse response = authService.register(registerRequest);

        assertNotNull(response);
        assertEquals("jwtToken", response.getToken());
        assertEquals("Bearer", response.getTipo());
        assertEquals("test@test.com", response.getUsuario().getEmail());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void register_EmailDuplicated_ThrowsException() {
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        assertThrows(RuntimeException.class, () -> authService.register(registerRequest));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void login_CredencialesValidas_ReturnsAuthResponse() {
        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(jwtTokenProvider.generateToken(any())).thenReturn("jwtToken");
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(testUser));

        AuthResponse response = authService.login(loginRequest);

        assertNotNull(response);
        assertEquals("jwtToken", response.getToken());
        assertEquals("Bearer", response.getTipo());
        assertEquals("test@test.com", response.getUsuario().getEmail());
    }

    @Test
    void login_UsuarioNoEncontrado_ThrowsException() {
        when(authenticationManager.authenticate(any())).thenReturn(mock(Authentication.class));
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> authService.login(loginRequest));
    }
}