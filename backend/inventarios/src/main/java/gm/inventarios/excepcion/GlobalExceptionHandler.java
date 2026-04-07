package gm.inventarios.excepcion;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RecursoNoEncontradoExcepcion.class)
    public ResponseEntity<ErrorRespuesta> handleRecursoNoEncontrado(
            RecursoNoEncontradoExcepcion ex, WebRequest request) {
        log.warn("Recurso no encontrado: {}", ex.getMessage());
        
        ErrorRespuesta error = new ErrorRespuesta(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorRespuesta> handleEntityNotFound(
            EntityNotFoundException ex, WebRequest request) {
        log.warn("Entidad no encontrada: {}", ex.getMessage());
        
        ErrorRespuesta error = new ErrorRespuesta(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorRespuesta> handleValidacion(
            MethodArgumentNotValidException ex, WebRequest request) {
        log.warn("Error de validación: {}", ex.getMessage());
        
        List<Map<String, String>> errores = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> {
                    Map<String, String> error = new HashMap<>();
                    error.put("campo", fieldError.getField());
                    error.put("mensaje", fieldError.getDefaultMessage());
                    return error;
                })
                .collect(Collectors.toList());
        
        ErrorRespuesta error = new ErrorRespuesta(
                HttpStatus.BAD_REQUEST.value(),
                "Error de validación en los datos enviados",
                request.getDescription(false).replace("uri=", ""),
                errores
        );
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorRespuesta> handleMensajeIlegible(
            HttpMessageNotReadableException ex, WebRequest request) {
        log.warn("Mensaje ilegible: {}", ex.getMessage());
        
        ErrorRespuesta error = new ErrorRespuesta(
                HttpStatus.BAD_REQUEST.value(),
                "JSON inválido o malformado",
                request.getDescription(false).replace("uri=", "")
        );
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorRespuesta> handleAuthenticationException(
            AuthenticationException ex, WebRequest request) {
        log.warn("Error de autenticación: {}", ex.getMessage());
        
        ErrorRespuesta error = new ErrorRespuesta(
                HttpStatus.UNAUTHORIZED.value(),
                "Credenciales inválidas",
                request.getDescription(false).replace("uri=", "")
        );
        
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorRespuesta> handleAccessDenied(
            AccessDeniedException ex, WebRequest request) {
        log.warn("Acceso denegado: {}", ex.getMessage());
        
        ErrorRespuesta error = new ErrorRespuesta(
                HttpStatus.FORBIDDEN.value(),
                "No tienes permiso para acceder a este recurso",
                request.getDescription(false).replace("uri=", "")
        );
        
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorRespuesta> handleExcepcionGeneral(
            Exception ex, WebRequest request) {
        log.error("Error inesperado: ", ex);
        
        ErrorRespuesta error = new ErrorRespuesta(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Error interno del servidor",
                request.getDescription(false).replace("uri=", "")
        );
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}