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
- Despliegue a la nube (Railway)

### ✅ Completado: Frontend con autenticación

1. **auth.service.ts** - Login/register con JWT, manejo de token en localStorage
2. **login/** - Componente de login
3. **register/** - Componente de registro
4. **auth.guard.ts** - Protege rutas
5. **app.routes.ts** - Actualizado con auth
6. **producto.service.ts** - Envía token en headers
7. **Botón vender** - Modal para registrar ventas y reducir stock
8. **Estados de stock** - badges (Sin stock/Stock bajo/Stock OK)
9. **Botón agregar** - Agregar productos sin stock inicial

---

## PRODUCTION DEPLOY

### Railway (Backend)
- URL: Configurada en Railway dashboard
- Root Directory: `backend/inventarios`
- MySQL: Provisionado en Railway
- Variables de entorno: SPRING_DATASOURCE_URL, SPRING_DATASOURCE_USERNAME, SPRING_DATASOURCE_PASSWORD

### Pendiente: Frontend deploy
- Cambiar URLs de localhost:8080 a URL de Railway en:
  - auth.service.ts
  - producto.service.ts
- Deploy en Vercel o Netlify

---

## NEXT STEPS (Orden de prioridad)

### 🟢 PENDING - Deploy frontend

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
# Desarrollo local
spring.datasource.url=jdbc:mysql://localhost:3306/inventario_db?createDatabaseIfNotExist=true
spring.datasource.username=${DB_USER:root}
spring.datasource.password=${DB_PASSWORD}
spring.jpa.hibernate.ddl-auto=update
```

**Para producción (Railway):** usar variables de entorno, NO hardcodear credenciales.

---

## WORKFLOW SUGERIDO PARA AGENTES

1. **SIEMPRE** verificar que los cambios respeten el filtrado por usuario
2. **ANTES** de modificar entidades o repositorios, revisar relaciones JPA existentes
3. **NUNCA** hardcodear IDs de usuario - siempre obtener del SecurityContext
4. **ANTE LA DUDA**, leer primero los archivos en `gm.inventarios/user/model/` y `gm.inventarios/producto/model/`
5. **PROXIMO paso**: Docker + docker-compose para contenerización