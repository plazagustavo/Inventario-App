# AGENTS.md - Proyecto Inventario SaaS Multiusuario

## PROJECT OVERVIEW

- **Tipo**: SaaS multi-tenant basado en usuarios (single DB, separación lógica por usuario)
- **Stack**: Java 21 + Spring Boot 3.4.2 + MySQL + JWT + Angular
- **Objetivo**: Portfolio profesional para conseguired trabajo como desarrollador backend

## CURRENT STATE

### ✅ Implementado

- **Entidades JPA**: User y Producto con relación OneToMany/ManyToOne correcta
- **Auth**: Register + Login con JWT token (stateless)
- **Security**: Spring Security con filtros, CORS, roles (USER/ADMIN)
- **API Docs**: Swagger/OpenAPI integrado
- **Arquitectura**: Capas controller → service → repository con DTOs
- **Filtrado multi-usuario**: Productos filtrados por usuario autenticado
- **UsuarioActual**: Componente para obtener usuario desde SecurityContext

### ⚠️ Pendiente

- Docker + docker-compose
- Despliegue a la nube (Render/Railway)

## COMMANDS TO RUN

```bash
# Backend
cd backend/inventarios && mvn spring-boot:run

# Swagger UI
http://localhost:8080/swagger-ui/index.html

# Recursos disponibles en:
# http://localhost:8080/v3/api-docs
```

## API ENDPOINTS ACTUALES

| Método | Endpoint | Auth | Descripción |
|--------|----------|------|-------------|
| POST | `/api/auth/register` | ❌ | Registrar nuevo usuario |
| POST | `/api/auth/login` | ❌ | Login, retorna JWT |
| GET | `/inventario-app/productos` | ✅ | Listar productos (filtrado por usuario) |
| POST | `/inventario-app/productos` | ✅ | Crear producto |
| GET | `/inventario-app/productos/{id}` | ✅ | Obtener por ID |
| PUT | `/inventario-app/productos/{id}` | ✅ | Actualizar producto |
| DELETE | `/inventario-app/productos/{id}` | ✅ | Eliminar producto |

**Headers requeridos**: `Authorization: Bearer <token_jwt>`

---

## PROGRESS - Lo que hicimos

### ✅ Completado: Filtrado multi-usuario

1. **UsuarioActual.java** - Componente para obtener usuario desde SecurityContextHolder
   - Ubicación: `gm.inventarios.security.UsuarioActual`
   - Métodos: `obtener()` → User, `obtenerId()` → Integer

2. **ProductoRepositorio.java** - Métodos existentes:
   - `findByUsuario(User usuario)` - Listar productos del usuario
   - `findByIdAndUsuario(Integer id, User usuario)` - Buscar por ID y usuario

3. **ProductoServicio.java** - Modificado:
   - `listarProductos()` → usa `findByUsuario()`
   - `buscarProductoPorId()` → verifica pertenencia al usuario
   - `guardarProducto()` → asigna usuario autenticado
   - `eliminarProducto()` → verifica pertenencia antes de eliminar

4. **ProductoControlador.java** - Ajustado:
   - Sacados `toString()` del logger que causaban ConcurrentModificationException

5. **Entidades corregidas** - Fix bugs JSON:
   - `Producto.java`: `@JsonIgnore` en campo `usuario`
   - `User.java`: `@JsonIgnore` en campo `productos`

### ✅ Testing funcional verificado

- GET `/inventario-app/productos` → retorna solo productos del usuario
- POST crea producto con usuario asignado
- PUT actualiza solo productos del usuario
- DELETE elimina solo productos del usuario
- Aislamiento entre usuarios confirmado (usuario otro ve lista vacía)

### ✅ Completado: GlobalExceptionHandler

1. **ErrorRespuesta.java** - DTO para respuestas de error consistentes
   - `codigo`, `mensaje`, `path`, `fecha`, `detalles`

2. **GlobalExceptionHandler.java** - Manejo centralizado de errores
   - `RecursoNoEncontradoExcepcion` → 404
   - `MethodArgumentNotValidException` → 400 con detalles de campos
   - `HttpMessageNotReadableException` → 400
   - `AuthenticationException` → 401
   - `AccessDeniedException` → 403
   - Exception genérica → 500

### ✅ Completado: Tests unitarios

1. **AuthServiceTest** (4 tests)
   - `register_NuevoUsuario_ReturnsAuthResponse` ✅
   - `register_EmailDuplicated_ThrowsException` ✅
   - `login_CredencialesValidas_ReturnsAuthResponse` ✅
   - `login_UsuarioNoEncontrado_ThrowsException` ✅

2. **ProductoServicioTest** (8 tests)
   - `listarProductos_UsuarioAutenticado_RetornaListaProductos` ✅
   - `listarProductos_UsuarioNoAutenticado_ThrowsException` ✅
   - `buscarProductoPorId_ProductoDelUsuario_RetornaProducto` ✅
   - `buscarProductoPorId_ProductoNoExiste_RetornaNull` ✅
   - `guardarProducto_UsuarioAutenticado_AsignaUsuarioYGuarda` ✅
   - `guardarProducto_UsuarioNoAutenticado_ThrowsException` ✅
   - `eliminarProducto_ProductoDelUsuario_EliminaCorrectamente` ✅
   - `eliminarProducto_ProductoNoEncontrado_ThrowsException` ✅

**Total: 13 tests - BUILD SUCCESS**

### ✅ Completado: Docker + docker-compose

1. **backend/inventarios/Dockerfile**
   - Multi-stage build (Maven build → JRE runtime)
   - Expone puerto 8080

2. **docker-compose.yml**
   - MySQL 8.0 con volumen persistente
   - App Java espera a que MySQL esté sano
   - Variables de entorno configurables

3. **application.properties**
   - Actualizado con variables de entorno para docker
   - Valores por defecto para desarrollo local

**Ejecutar:** `docker-compose up --build`

---

## NEXT STEPS (Orden de prioridad)

### 🟡 MEDIUM - Testing

1. **Tests unitarios**
   - `AuthServiceTest` (register, login)
   - `ProductoServicioTest` (CRUD con usuario)
   - Usar JUnit 5 + Mockito

2. **Tests de controladores**
   - `ProductoControladorTest` con MockMvc
   - Proteger endpoints con token

### 🟢 LOW - DevOps

5. **Docker**
   - Crear `backend/Dockerfile`
   - Crear `docker-compose.yml` con MySQL

6. **Despliegue**
   - Configurar para Render/Railway
   - Variables de entorno para DB

---

## KEY QUIRKS

| Aspecto | Detalle |
|---------|---------|
| **Endpoint productos** | NO es `/api/productos` sino `/inventario-app/productos` |
| **Nombre tabla usuarios** | `usuarios` (no `users`) |
| **FK en Producto** | `usuario_id` referencia a `usuarios.id` |
| **Filtro crítico** | ✅ YA IMPLEMENTADO - ProductoServicio filtra por usuario |
| **CORS** | Configurado solo para `http://localhost:4200` (Angular) |
| **Password encoding** | BCrypt via `PasswordEncoder` bean |
| **UsuarioActual** | `gm.inventarios.security.UsuarioActual` - para obtener usuario autenticado |

## LOGGING

- Configurado en `application.properties`
- Nivel raíz: INFO
- Paquete configurable: `loggin.level.gm.inventarios=DEBUG`
- Pattern: `[%thread] %-5level: %logger - %msg%n`

## DATABASE

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/inventario_db?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=1111
spring.jpa.hibernate.ddl-auto=update
```

---

## WORKFLOW SUGERIDO PARA AGENTES

1. **SIEMPRE** verificar que los cambios respeten el filtrado por usuario
2. **ANTES** de modificar entidades o repositorios, revisar relaciones JPA existentes
3. **NUNCA** hardcodear IDs de usuario - siempre obtener del SecurityContext
4. **ANTE LA DUDA**, leer primero los archivos en `gm.inventarios/user/model/` y `gm.inventarios/producto/model/`
5. **PROXIMO paso**: Docker + docker-compose para contenerización