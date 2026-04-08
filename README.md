# 📦 Sistema de Gestión de Inventario - SaaS Multiusuario

Aplicación **Full Stack** para la gestión de productos de inventario desarrollada con **Angular en el frontend** y **Spring Boot en el backend**, utilizando una arquitectura REST para la comunicación entre cliente y servidor.

Este proyecto es una **plataforma SaaS multiusuario** donde cada usuario puede registrarse, iniciar sesión y gestionar sus propios productos de manera aislada.

---

# 🧠 Arquitectura del Sistema
-Frontend: Cliente de Angular (v21) alojado en Vercel, encargado de la lógica de presentación y gestión de sesiones mediante JWT.

-Backend: API REST construida con Spring Boot alojada en Railway, que implementa Spring Security para la protección de recursos.

-Persistencia: Base de datos MySQL (Railway) con aislamiento de datos a nivel de registro por usuario.

---

# 🚀 Tecnologías Utilizadas

## Backend
- **Java 21** con Spring Boot 3.4.2
- Spring Security + JWT (autenticación stateless)
- Spring Data JPA + Hibernate
- MySQL 8.0
- Maven

## Frontend
- **Angular 21** (standalone components)
- TypeScript
- Bootstrap 5

## Despliegue
- **Backend**: Railway
- **Frontend**: Vercel

## Herramientas
- Postman (testing de endpoints)
- Git / GitHub
- IntelliJ IDEA / VS Code

---

# 📸 Interfaz de Usuario

## 🔐 Registro de Usuario

Pantalla de registro para crear una nueva cuenta de usuario.

![Registro](docs/images/register.png)

---

## 🔐 Inicio de Sesión

Pantalla de login con autenticación JWT.

![Login](docs/images/Login.png)

---

## 📋 Lista de Productos

Vista principal donde se muestran todos los productos del usuario autenticado.
- Estados de stock con badges visuales (Sin stock, Stock bajo, Stock OK)
- Botones de acción: Vender, Editar, Eliminar

![Lista de productos](docs/images/Inicio.png)

---

## ➕ Agregar Producto

Formulario para registrar nuevos productos en el sistema.

![Agregar producto](docs/images/agregar-producto.png)

---

## ✏️ Editar Producto

Formulario para modificar los datos de un producto existente.

![Editar producto](docs/images/editar-producto.png)

---

# 💡 Características del Sistema

## ✅ Autenticación y Seguridad
- Registro y login de usuarios con JWT
- Contraseñas encriptadas con BCrypt
- Roles de usuario (USER, ADMIN)
- Rutas protegidas con Auth Guard

## ✅ Sistema Multiusuario
- Aislamiento de datos por usuario
- Cada usuario solo ve sus propios productos
- Filtrado automático en todas las operaciones CRUD

## ✅ Gestión de Inventario
- CRUD completo de productos
- Estados de stock visual (verde/amarillo/rojo)
- Modal de venta para registrar salidas
- Validación de datos en formularios

## ✅ APIs RESTful
- Endpoints REST estándar
- Manejo de errores con GlobalExceptionHandler

## ✅ Testing
- Tests unitarios con JUnit 5 y Mockito
- 13 tests pasando en AuthService y ProductoServicio

## ✅ Despliegue
- Backend deployado en Railway
- Frontend deployado en Vercel
- Configuración para producción

---

# 📂 Estructura del Proyecto
```text
inventario-fullstack/
│
├── backend/
│   └── inventarios/
│       ├── src/main/java/gm/inventarios/
│       │   ├── auth/          # JWT, AuthController, AuthService
│       │   ├── config/        # SecurityConfig
│       │   ├── controlador/   # ProductoControlador
│       │   ├── servicio/      # ProductoServicio, IProductoServicio
│       │   ├── repositorio/   # ProductoRepositorio
│       │   ├── modelo/        # Entidades JPA
│       │   ├── excepciones/   # GlobalExceptionHandler
│       │   └── security/      # UsuarioActual, UserDetailsService
│       │
│       ├── src/main/resources/
│       │   └── application.properties
│       │
│       └── pom.xml
│
├── frontend/
│   └── inventario-app/
│       ├── src/app/
│       │   ├── login/         # Componente de login
│       │   ├── register/      # Componente de registro
│       │   ├── producto-lista/ # Lista de productos
│       │   ├── agregar-producto/
│       │   ├── editar-producto/
│       │   ├── auth.service.ts
│       │   └── producto.service.ts
│       │
│       └── angular.json
│
└── README.md
```
---

# 🌐 Deploy en Producción

🔗 **Demo:** [https://inventario-app-tawny.vercel.app](https://inventario-app-tawny.vercel.app)

---

# 📌 Endpoints de la API

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/api/auth/register` | Registrar nuevo usuario |
| POST | `/api/auth/login` | Iniciar sesión |
| GET | `/inventario-app/productos` | Listar productos del usuario |
| POST | `/inventario-app/productos` | Crear producto |
| GET | `/inventario-app/productos/{id}` | Obtener producto por ID |
| PUT | `/inventario-app/productos/{id}` | Actualizar producto |
| DELETE | `/inventario-app/productos/{id}` | Eliminar producto |

**Nota**: Todos los endpoints de productos requieren autenticación JWT (excepto register y login).

---

# 👨‍💻 Autor

 **Gustavo Plaza** 
- Desarrollo Full Stack con Java/Spring Boot y Angular
- Arquitecturas RESTful seguras
- Sistemas multiusuario con JWT
- Despliegue en la nube (Railway, Vercel)
- Buenas prácticas de código y testing
