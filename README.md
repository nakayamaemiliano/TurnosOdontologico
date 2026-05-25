# Turnos Odontologico API

API REST para administrar turnos de una clinica odontologica. El proyecto cubre la gestion de pacientes, odontologos, turnos, usuarios, roles y permisos, con autenticacion mediante JWT.

La aplicacion esta preparada para trabajar tanto en entorno local como con Docker Compose, e incluye documentacion interactiva con Swagger/OpenAPI y tests automatizados.

## Funcionalidades

- Alta, listado, busqueda, actualizacion y eliminacion de pacientes.
- Alta, listado, busqueda, actualizacion y eliminacion de odontologos.
- Creacion, listado, actualizacion y eliminacion de turnos.
- Busqueda de turnos por fecha, paciente u odontologo.
- Confirmacion y cancelacion de turnos.
- Resumen de turnos por estado en una fecha determinada.
- Consulta del odontologo mas solicitado.
- Autenticacion con JWT.
- Autorizacion por roles `ADMIN` y `USER`.
- Gestion de usuarios, roles y permisos.
- Documentacion de endpoints con Swagger.
- Docker Compose para levantar backend y MySQL.

## Tecnologias

- Java 17
- Spring Boot 4
- Spring Web MVC
- Spring Data JPA
- Spring Security
- JWT
- MySQL
- H2 para contexto de tests
- JUnit 5
- Mockito
- Swagger / OpenAPI
- Docker

## Arquitectura general

El proyecto sigue una estructura por capas:

```text
Controller -> Service -> Repository -> Database
```

Ademas cuenta con:

- DTOs para entrada y salida de datos.
- Mappers para separar entidades de respuestas HTTP.
- Manejo centralizado de excepciones.
- Filtro JWT para validar requests autenticadas.
- Seed inicial opcional para crear datos de seguridad.

## Requisitos

Para ejecucion local:

- JDK 17
- MySQL local, por ejemplo XAMPP
- Base de datos `turnosOdontologico`

Para ejecucion con Docker:

- Docker Desktop
- Docker Compose

## Variables de entorno

La aplicacion no guarda credenciales sensibles en el codigo. Las variables necesarias estan documentadas en:

```text
.env.example
```

Ejemplo para PowerShell usando MySQL local:

```powershell
$env:JAVA_HOME='C:\Program Files\Java\jdk-17'
$env:DB_URL='jdbc:mysql://localhost:3306/turnosOdontologico?useSSL=false&serverTimezone=America/Argentina/Cordoba&allowPublicKeyRetrieval=true'
$env:DB_USERNAME='root'
$env:DB_PASSWORD=''
$env:JWT_PRIVATE_KEY='change-this-secret-key-before-production'
$env:JWT_USER_GENERATOR='TURNOS_ODONTOLOGICO_API'
$env:CORS_ALLOWED_ORIGINS='http://localhost:3000,http://localhost:5173'
$env:APP_SEED_ENABLED='true'
$env:APP_SEED_ADMIN_USERNAME='codex_admin'
$env:APP_SEED_ADMIN_PASSWORD='admin123'
```

Si se crea un archivo `.env` real, no debe subirse al repositorio.

## Ejecucion local

Crear la base de datos:

```sql
CREATE DATABASE turnosOdontologico;
```

Levantar la aplicacion:

```powershell
.\mvnw.cmd spring-boot:run
```

URL base:

```text
http://localhost:8080
```

## Ejecucion con Docker

El proyecto incluye `Dockerfile` y `docker-compose.yml`. Esto permite levantar la API y MySQL con un solo comando.

Primero abrir Docker Desktop y esperar a que quede corriendo.

```powershell
docker compose up --build
```

Servicios:

```text
Backend: http://localhost:8080
Swagger: http://localhost:8080/swagger-ui/index.html
MySQL interno: mysql:3306
MySQL desde la maquina host: localhost:3307
```

El puerto `3307` se usa para evitar conflictos con un MySQL local en `3306`, por ejemplo XAMPP.

Para apagar los contenedores:

```powershell
docker compose down
```

Para apagar y borrar la base Docker:

```powershell
docker compose down -v
```

## Swagger / OpenAPI

La documentacion interactiva esta disponible en:

```text
http://localhost:8080/swagger-ui/index.html
```

El contrato OpenAPI en JSON esta disponible en:

```text
http://localhost:8080/v3/api-docs
```

Los endpoints estan agrupados por:

- Autenticacion
- Pacientes
- Odontologos
- Turnos
- Usuarios
- Roles
- Permisos

## Autenticacion

La API usa JWT. Para obtener un token:

```http
POST /auth/login
```

Body de ejemplo:

```json
{
  "username": "codex_admin",
  "password": "admin123"
}
```

Respuesta esperada:

```json
{
  "username": "codex_admin",
  "message": "login ok",
  "jwt": "TOKEN",
  "status": true
}
```

Para consumir endpoints protegidos:

```http
Authorization: Bearer TOKEN
```

El usuario `codex_admin` se crea automaticamente al iniciar la aplicacion si `APP_SEED_ENABLED=true`. En un entorno productivo se recomienda cambiar la password inicial o desactivar el seed.

## CORS

Los origenes permitidos para el frontend se configuran con:

```text
CORS_ALLOWED_ORIGINS
```

Valores por defecto:

```text
http://localhost:3000,http://localhost:5173
```

Si el frontend usa otro puerto, se agrega a esa variable:

```powershell
$env:CORS_ALLOWED_ORIGINS='http://localhost:3000,http://localhost:5173,http://localhost:4200'
```

## Tests

Ejecutar la suite completa:

```powershell
.\mvnw.cmd test
```

El proyecto incluye:

- Tests unitarios de services con Mockito.
- Tests de seguridad para `UserDetailsServiceImp`.
- Tests de generacion y validacion JWT.
- Test de contexto Spring con perfil `test` y base H2.

Resultado esperado:

```text
BUILD SUCCESS
```

## Uso desde frontend

URL base local:

```text
http://localhost:8080
```

Flujo recomendado:

1. Levantar el backend con Docker Compose o de forma local.
2. Hacer login en `POST /auth/login`.
3. Guardar el campo `jwt` recibido.
4. Enviar el token en cada request protegida:

```http
Authorization: Bearer TOKEN
```

5. Consumir los endpoints documentados en Swagger.

Ejemplo de configuracion para un frontend Vite:

```text
VITE_API_URL=http://localhost:8080
```

## Datos iniciales

Cuando `APP_SEED_ENABLED=true`, la aplicacion crea automaticamente:

- Permisos base: `READ`, `CREATE`, `UPDATE`, `DELETE`
- Rol `ADMIN`
- Rol `USER`
- Usuario administrador inicial

Credenciales por defecto:

```text
Usuario: codex_admin
Password: admin123
```

Estas credenciales son solo para desarrollo.

## Notas para despliegue

Antes de desplegar:

- Usar un `JWT_PRIVATE_KEY` fuerte.
- Cambiar o desactivar el usuario inicial.
- Configurar `CORS_ALLOWED_ORIGINS` con el dominio real del frontend.
- Configurar una base MySQL persistente.
- No subir archivos `.env` con credenciales reales.

Variables importantes:

```text
DB_URL
DB_USERNAME
DB_PASSWORD
JWT_PRIVATE_KEY
JWT_USER_GENERATOR
CORS_ALLOWED_ORIGINS
APP_SEED_ENABLED
APP_SEED_ADMIN_USERNAME
APP_SEED_ADMIN_PASSWORD
```

## Autor

Emiliano Nakayama

- GitHub: [nakayamaemiliano](https://github.com/nakayamaemiliano)
- LinkedIn: [emiliano-nakayama](https://www.linkedin.com/in/emiliano-nakayama/)
