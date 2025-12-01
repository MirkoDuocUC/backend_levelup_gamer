# Level-Up Gamer Backend - Spring Boot 3.5.0 con JWT + Spring Security

## 🚀 Descripción

Backend REST API profesional para e-commerce de productos gaming, desarrollado con:
- **Spring Boot 3.5.0**
- **Java 21**
- **Spring Security + JWT**
- **MySQL 8.0+**
- **Swagger/OpenAPI 3.0**
- **Lombok**

## 📋 Características

✅ Autenticación JWT con Spring Security
✅ Roles de usuario (admin / cliente)
✅ CRUD completo de productos, categorías, órdenes, carrito
✅ Sistema de reseñas de productos
✅ Blog integrado
✅ Gestión de regiones y comunas de Chile
✅ Descuento automático del 20% para emails @duocuc.cl
✅ Documentación Swagger automática
✅ Manejo global de excepciones
✅ Validación de datos con Bean Validation

## 🛠️ Requisitos Previos

- **Java 21** (JDK 21+)
- **Maven 3.8+**
- **MySQL 8.0+**
- IDE recomendado: IntelliJ IDEA o VS Code con extensión Java

## 📦 Instalación

### 1. Clonar el repositorio
```bash
git clone <url-del-repositorio>
cd levelupgamer
```

### 2. Configurar la base de datos

Ejecuta el script `schema.sql` en MySQL para crear la base de datos:

```bash
mysql -u root -p < schema.sql
```

Esto creará:
- Base de datos `levelup_gamer`
- Todas las tablas con sus relaciones
- Datos de ejemplo (regiones, comunas, productos, usuarios)
- Usuario admin por defecto: `admin@levelup.com` / `1234`

### 3. Configurar credenciales de MySQL

Edita `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/levelup_gamer
spring.datasource.username=root
spring.datasource.password=TU_PASSWORD
```

### 4. Compilar el proyecto

```bash
mvn clean install
```

### 5. Ejecutar la aplicación

```bash
mvn spring-boot:run
```

El servidor iniciará en: `http://localhost:8080`

## 📚 Documentación API (Swagger)

Una vez iniciada la aplicación, accede a:

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **API Docs (JSON)**: http://localhost:8080/api-docs

## 🔐 Autenticación

### Registro de usuario

```http
POST /auth/registro
Content-Type: application/json

{
  "email": "usuario@example.com",
  "password": "1234",
  "nombre": "Juan",
  "apellido": "Pérez",
  "fechaNacimiento": "2000-01-15",
  "telefono": "+56912345678",
  "direccion": "Calle Principal 123",
  "comunaId": 1,
  "regionId": 7,
  "newsletter": false
}
```

**Respuesta:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "tipo": "Bearer",
  "email": "usuario@example.com",
  "rol": "cliente",
  "nombre": "Juan Pérez",
  "id": 2,
  "mensaje": "Registro exitoso"
}
```

### Login

```http
POST /auth/login
Content-Type: application/json

{
  "email": "admin@levelup.com",
  "password": "1234"
}
```

**Respuesta:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "tipo": "Bearer",
  "email": "admin@levelup.com",
  "rol": "admin",
  "nombre": "Admin Level-Up",
  "id": 1,
  "mensaje": "Login exitoso"
}
```

### Usar el token JWT

En todas las peticiones autenticadas, incluye el header:

```
Authorization: Bearer <tu-token-jwt>
```

## 🎯 Endpoints Principales

### Productos
- `GET /api/productos` - Listar productos activos
- `GET /api/productos/{id}` - Obtener producto por ID
- `GET /api/productos/destacados` - Productos destacados
- `GET /api/productos/buscar?q=texto` - Buscar productos
- `POST /api/productos/crear` - Crear producto (ADMIN)
- `PUT /api/productos/{id}` - Actualizar producto (ADMIN)
- `DELETE /api/productos/{id}` - Eliminar producto (ADMIN)

### Categorías
- `GET /api/categorias` - Listar categorías activas
- `GET /api/categorias/{id}` - Obtener categoría
- `POST /api/categorias` - Crear categoría (ADMIN)
- `PUT /api/categorias/{id}` - Actualizar categoría (ADMIN)
- `DELETE /api/categorias/{id}` - Eliminar categoría (ADMIN)

### Usuarios
- `GET /api/usuarios` - Listar usuarios (ADMIN)
- `GET /api/usuarios/perfil` - Ver perfil propio
- `PUT /api/usuarios/perfil` - Actualizar perfil

### Ubicación
- `GET /api/regiones` - Listar regiones de Chile
- `GET /api/comunas` - Listar todas las comunas
- `GET /api/comunas/region/{regionId}` - Comunas por región

## 👥 Roles y Permisos

### 🛡️ ADMIN
- Gestión completa de productos y categorías
- Ver y administrar todos los usuarios
- Moderar reseñas
- Gestionar blog
- Ver todas las órdenes

### 🛒 CLIENTE
- Navegar catálogo de productos
- Gestionar carrito de compras
- Crear órdenes de compra
- Escribir reseñas
- Gestionar perfil propio
- Ver historial de órdenes propias

## 💡 Reglas de Negocio Implementadas

1. **Descuento DUOC**: Usuarios con email `@duocuc.cl` reciben 20% de descuento automático
2. **Edad mínima**: Usuarios deben tener al menos 18 años para registrarse
3. **Email único**: No se permiten emails duplicados
4. **Password seguro**: Encriptado con BCrypt
5. **JWT válido**: Tokens expiran en 24 horas
6. **Stock validation**: Se valida stock disponible al añadir al carrito
7. **Roles estrictos**: Endpoints protegidos por roles (admin/cliente)

## 🔧 Tecnologías Usadas

| Tecnología | Versión | Propósito |
|------------|---------|-----------|
| Spring Boot | 3.5.0 | Framework principal |
| Spring Security | 6.x | Autenticación y autorización |
| JWT (jjwt) | 0.11.5 | Tokens JWT |
| Spring Data JPA | 3.x | Persistencia de datos |
| MySQL | 8.0+ | Base de datos |
| Lombok | Latest | Reducir boilerplate |
| Springdoc OpenAPI | 2.2.0 | Documentación Swagger |
| Bean Validation | 3.x | Validación de datos |

## 📁 Estructura del Proyecto

```
src/main/java/com/levelup/levelupgamer/
├── config/
│   ├── SecurityConfig.java          # Configuración de seguridad
│   └── SwaggerConfig.java           # Configuración de Swagger
├── controller/
│   ├── AuthController.java          # Login y registro
│   ├── ProductoController.java
│   ├── CategoriaController.java
│   ├── UsuarioController.java
│   ├── RegionController.java
│   └── ComunaController.java
├── dto/
│   ├── AuthRequest.java
│   ├── AuthResponse.java
│   ├── RegistroRequest.java
│   └── ErrorResponseDTO.java
├── entity/
│   ├── Usuario.java                 # Implementa UserDetails
│   ├── Producto.java
│   ├── Categoria.java
│   ├── Carrito.java
│   ├── CarritoItem.java
│   ├── Orden.java
│   ├── OrdenItem.java
│   ├── Resena.java
│   ├── BlogPost.java
│   ├── Contacto.java
│   ├── Sesion.java
│   ├── Region.java
│   └── Comuna.java
├── repository/                       # Interfaces JPA
├── security/
│   ├── JwtUtil.java                 # Generación y validación JWT
│   ├── JwtAuthenticationFilter.java # Filtro de autenticación
│   └── CustomUserDetailsService.java
├── service/
│   └── AuthService.java             # Lógica de autenticación
├── util/                            # Enums
└── exception/
    └── GlobalExceptionHandler.java  # Manejo global de errores
```

## 🧪 Testing

### Probar con cURL

```bash
# Registro
curl -X POST http://localhost:8080/auth/registro \
  -H "Content-Type: application/json" \
  -d '{"email":"test@duocuc.cl","password":"1234","nombre":"Test","apellido":"User","fechaNacimiento":"2000-01-01"}'

# Login
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@levelup.com","password":"1234"}'

# Listar productos (sin autenticación)
curl -X GET http://localhost:8080/api/productos

# Ver perfil (con autenticación)
curl -X GET http://localhost:8080/api/usuarios/perfil \
  -H "Authorization: Bearer <tu-token>"
```

## 🐛 Troubleshooting

### Error: "Access Denied"
- Verifica que el token JWT esté incluido en el header `Authorization: Bearer <token>`
- Verifica que el usuario tenga el rol adecuado (admin/cliente)

### Error: "MySQL Connection Failed"
- Verifica que MySQL esté ejecutando: `systemctl status mysql`
- Verifica credenciales en `application.properties`

### Error: "Port 8080 already in use"
- Cambia el puerto en `application.properties`: `server.port=8081`

## 📄 Licencia

MIT License

## 👨‍💻 Autor

Level-Up Gamer Team - Backend con Spring Boot 3.5.0

---

**¡Backend listo para producción! 🚀**
