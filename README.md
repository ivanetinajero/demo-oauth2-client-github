# Demo OAuth2 Client - Spring Boot + GitHub

Una aplicación web moderna que demuestra la integración de **Spring Boot 4**, **OAuth2** con autenticación mediante **GitHub** y **Bootstrap 5** para crear una webapp segura y responsiva.

## 📋 Tabla de Contenidos

- [Características](#-características)
- [Tecnologías](#-tecnologías)
- [Estructura del Proyecto](#-estructura-del-proyecto)
- [Configuración](#-configuración)
- [Base de Datos](#-base-de-datos)
- [Flujo de Autenticación OAuth2](#-flujo-de-autenticación-oauth2)
- [Instalación y Ejecución](#-instalación-y-ejecución)
- [Uso](#-uso)
- [Componentes Principales](#-componentes-principales)
- [Personalización](#-personalización)

## 🚀 Características

- **Autenticación Segura**: Login con GitHub usando OAuth2, sin contraseñas propias
- **Gestión de Productos**: CRUD completo para administrar inventario
- **Control de Roles**: Sistema de permisos diferenciado (ADMIN/SUPERVISOR/USUARIO)
- **Interfaz Moderna**: Diseño responsivo con Bootstrap 5
- **Arquitectura Limpia**: Separación clara entre capas (Controller, Service, Repository)
- **Base de Datos**: Integración con MySQL usando JPA/Hibernate
- **Dos Modos de Registro**: Automático o solo usuarios pre-autorizados

## 🛠️ Tecnologías

- **Backend**: Spring Boot 4, Spring Security, Spring Data JPA
- **Autenticación**: OAuth2 + GitHub
- **Frontend**: Thymeleaf, Bootstrap 5, Bootstrap Icons
- **Base de Datos**: MySQL 8.0+
- **Build Tool**: Maven

## 📁 Estructura del Proyecto

```
src/main/java/dev/itinajero/app/
├── controller/          # Controladores web
│   ├── HomeController.java
│   ├── ProductosController.java
│   └── UsuariosController.java
├── model/              # Entidades de datos
│   ├── Perfil.java
│   ├── Producto.java
│   └── Usuario.java
├── repository/         # Acceso a datos
│   ├── IPerfilesRepository.java
│   ├── IProductosRepository.java
│   └── IUsuariosRepository.java
├── service/           # Lógica de negocio
│   ├── CustomOAuth2UserService.java      # Registro automático
│   ├── StrictOAuth2UserService.java      # Solo pre-autorizados
│   ├── ProductosServiceImpl.java
│   └── UsuariosServiceImpl.java
└── security/          # Configuración de seguridad
    └── SecurityConfig.java

src/main/resources/
├── application.properties    # Configuración de la app
├── templates/               # Plantillas Thymeleaf
│   ├── fragments/           # Componentes reutilizables
│   ├── productos/           # Páginas de productos
│   ├── usuarios/            # Páginas de usuarios
│   ├── home.html            # Página principal
│   └── acerca.html          # Información del proyecto
├── static/                  # Recursos estáticos
│   ├── css/                 # Estilos personalizados
│   ├── js/                  # Scripts JavaScript
│   ├── images/              # Imágenes
│   └── ajax/                # Scripts AJAX para productos y usuarios
└── docs/
    └── database.sql         # Script completo de base de datos
```

## ⚙️ Configuración

### 1. Configuración de Base de Datos (application.properties)

```properties
# Base de datos MySQL
spring.datasource.url=jdbc:mysql://127.0.0.1:3308/test?useSSL=false&serverTimezone=America/Mexico_City&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=admin
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# Configuración de JPA/Hibernate
spring.jpa.show-sql=false
spring.jpa.hibernate.naming.physical-strategy=org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl
spring.jpa.open-in-view=false
```

### 2. Configuración OAuth2 con GitHub

```properties
# ID de cliente de la app registrada en GitHub
spring.security.oauth2.client.registration.github.client-id=TU_CLIENT_ID

# Secreto de cliente de la app registrada en GitHub
spring.security.oauth2.client.registration.github.client-secret=TU_CLIENT_SECRET

# Permisos que se solicitan al usuario al autenticarse
spring.security.oauth2.client.registration.github.scope=read:user,user:email

# URL a la que GitHub redirige tras la autenticación
spring.security.oauth2.client.registration.github.redirect-uri={baseUrl}/login/oauth2/code/{registrationId}

# URLs oficiales de GitHub OAuth2
spring.security.oauth2.client.provider.github.authorization-uri=https://github.com/login/oauth/authorize
spring.security.oauth2.client.provider.github.token-uri=https://github.com/login/oauth/access_token
spring.security.oauth2.client.provider.github.user-info-uri=https://api.github.com/user
spring.security.oauth2.client.provider.github.user-name-attribute=login
```

### 3. Registrar App en GitHub

1. Ve a **GitHub** → **Settings** → **Developer settings** → **OAuth Apps**
2. Crea una nueva OAuth App con:
   - **Application name**: Demo OAuth2 Client
   - **Homepage URL**: `http://localhost:8080`
   - **Authorization callback URL**: `http://localhost:8080/login/oauth2/code/github`
3. Copia el `Client ID` y `Client Secret` generados y úsalos en application.properties

## 🗄️ Base de Datos

El proyecto incluye un script completo de base de datos en `docs/database.sql` que crea:

### Estructura de Tablas

- **Perfiles**: Roles del sistema (ADMIN, SUPERVISOR, USUARIO)
- **Usuarios**: Información de usuarios con GitHub login
- **UsuariosPerfiles**: Tabla intermedia para relación many-to-many
- **Productos**: Catálogo de productos para demostrar CRUD

### Datos de Prueba Incluidos

- **3 Perfiles**: ADMIN, SUPERVISOR, USUARIO
- **20 Productos**: Electrónicos con precios y cantidades
- **20 Usuarios de ejemplo**: Con perfil USUARIO por defecto

### Comandos para Configurar la BD

```sql
-- Crear base de datos
CREATE DATABASE test;

-- Ejecutar el script completo
SOURCE docs/database.sql;
```

## 🔐 Flujo de Autenticación OAuth2

### Paso a Paso del Proceso de Login

1. **Usuario accede a la aplicación** (`http://localhost:8080`)
   - **Componente**: `HomeController`

2. **Spring Security intercepta la petición** y verifica autenticación
   - **Componente**: `SecurityConfig`

3. **Si no está autenticado**, redirige automáticamente a GitHub
   - **URL destino**: `https://github.com/login/oauth/authorize`
   - **Componente**: Configuración OAuth2 en `SecurityConfig`

4. **Usuario autoriza la aplicación** en GitHub
   - GitHub solicita permisos: `read:user` y `user:email`

5. **GitHub redirige de vuelta** con código de autorización
   - **URL callback**: `http://localhost:8080/login/oauth2/code/github`
   - **Componente**: Endpoint automático de Spring Security

6. **Spring Security procesa el token** usando servicio personalizado
   - **Componente**: `CustomOAuth2UserService` o `StrictOAuth2UserService`
   - Extrae información del usuario de GitHub (login, name, email)
   - Busca/crea usuario en base de datos local

7. **Usuario autenticado** puede acceder a la aplicación
   - **Componentes**: Controladores con datos de sesión y roles

### Servicios OAuth2 Disponibles

#### CustomOAuth2UserService (Registro Automático)
- Registra automáticamente cualquier usuario de GitHub
- Asigna perfil "USUARIO" por defecto
- Ideal para aplicaciones abiertas

#### StrictOAuth2UserService (Solo Usuarios Pre-registrados)
- Solo permite acceso a usuarios previamente registrados en el catálogo
- Rechaza usuarios no autorizados con excepción personalizada
- Ideal para aplicaciones restringidas

**Para alternar entre ambos**: Comenta/descomenta la anotación `@Service` en la clase que desees usar.

## 🚀 Instalación y Ejecución

### Prerrequisitos
- Java 17+
- Maven 3.6+
- MySQL 8.0+

### Pasos

1. **Clona el repositorio**
```bash
git clone [URL_DEL_REPOSITORIO]
cd demo-oauth2-client
```

2. **Configura la base de datos**
```sql
CREATE DATABASE test;
```

3. **Ejecuta el script SQL**
```bash
mysql -u root -p test < docs/database.sql
```

4. **Configura OAuth2**
- Registra tu app en GitHub (ver sección Configuración)
- Actualiza `src/main/resources/application.properties` con tus credenciales

5. **Ejecuta la aplicación**
```bash
mvn spring-boot:run
```

6. **Accede a la aplicación**
```
http://localhost:8080
```

## 📖 Uso

### Pantalla Principal
- **Sin autenticación**: Botón "Ingresar con GitHub"
- **Con autenticación**: Muestra usuario y roles actuales

### Gestión de Productos
- **URL**: `/productos`
- **Permisos**: Requiere autenticación
- **Funciones**: Crear, editar, eliminar productos
- **AJAX**: Interfaz dinámica sin recargas de página

### Gestión de Usuarios
- **URL**: `/usuarios`
- **Permisos**: Solo administradores (ADMIN)
- **Funciones**: CRUD de usuarios, asignar roles y GitHub login

### Control de Acceso por Roles

```java
// En SecurityConfig.java
.requestMatchers("/productos/eliminar/**").hasAnyAuthority("ADMIN")
.requestMatchers("/productos/**").hasAnyAuthority("ADMIN", "SUPERVISOR")
.requestMatchers("/usuarios/**").hasAnyAuthority("ADMIN")
```

### Navegación
- **Navbar responsiva** con menús dinámicos según autenticación
- **Logout** disponible en dropdown del usuario
- **Bootstrap 5** para diseño moderno y responsivo

## 🏗️ Componentes Principales

### SecurityConfig
Configuración central de seguridad que:
- Define rutas protegidas y públicas
- Configura OAuth2 con GitHub
- Inyecta servicio personalizado de usuarios
- Maneja logout y limpieza de sesión

### Servicios OAuth2
- **CustomOAuth2UserService**: Registro automático de usuarios
- **StrictOAuth2UserService**: Solo usuarios pre-autorizados

### Controladores
- **HomeController**: Página principal y acerca
- **ProductosController**: CRUD de productos con validaciones
- **UsuariosController**: Administración de usuarios y roles

### Modelos
- **Usuario**: Entidad con GitHub login y perfiles many-to-many
- **Producto**: Gestión de inventario con validaciones
- **Perfil**: Roles del sistema (ADMIN/SUPERVISOR/USUARIO)

## 🔧 Personalización

### Cambiar Proveedor OAuth2
Para usar Google en lugar de GitHub:
1. Registra app en Google Cloud Console
2. Actualiza `application.properties`:
```properties
spring.security.oauth2.client.registration.google.client-id=TU_GOOGLE_CLIENT_ID
spring.security.oauth2.client.registration.google.client-secret=TU_GOOGLE_CLIENT_SECRET
spring.security.oauth2.client.registration.google.scope=openid,profile,email
```

### Agregar Nuevos Roles
1. Inserta en tabla `Perfiles`:
```sql
INSERT INTO Perfiles (perfil) VALUES ('NUEVO_ROL');
```
2. Asigna a usuarios en tabla `UsuariosPerfiles`
3. Actualiza lógica de autorización en `SecurityConfig`

### Personalizar Páginas de Error
Crea templates en `src/main/resources/templates/error/`:
- `403.html` - Acceso denegado
- `404.html` - Página no encontrada
- `500.html` - Error interno del servidor

## 📚 Documentación Adicional

- [Spring Security OAuth2](https://docs.spring.io/spring-security/reference/servlet/oauth2/index.html)
- [GitHub OAuth Apps](https://docs.github.com/en/apps/oauth-apps/building-oauth-apps/authorizing-oauth-apps)
- [Thymeleaf + Spring Security](https://www.thymeleaf.org/doc/articles/springsecurity.html)
- [Bootstrap 5 Documentation](https://getbootstrap.com/docs/5.0/getting-started/introduction/)

## 🤝 Contribuir

1. Fork del proyecto
2. Crea una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abre un Pull Request

## 📄 Licencia

Este proyecto está bajo la Licencia MIT. Ver el archivo `LICENSE` para más detalles.

---

**Desarrollado con ❤️ usando Spring Boot y OAuth2**
