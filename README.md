

# 💰 FinSight – Personal Finance Dashboard API

FinSight es una API REST desarrollada en Java con Spring Boot 3 para la gestión de finanzas personales. Permite a usuarios autenticados registrar, consultar y analizar sus ingresos y gastos.

---

## 🚀 Tecnologías utilizadas

- Java 17
- Spring Boot 3
- Spring Security con JWT
- Spring Data JPA (Hibernate)
- PostgreSQL
- Swagger/OpenAPI 3 (via Springdoc)
- Maven

---

## 🛠️ Instalación local

### 1. Clonar el repositorio

```bash
git clone git@github.com:GerardM5/finsight.git
cd finsight
```

### 2. Levantar PostgreSQL con Docker

```bash
docker run --name finsight-postgres \
  -e POSTGRES_DB=finsight_db \
  -e POSTGRES_USER=finsight \
  -e POSTGRES_PASSWORD=finsight123 \
  -p 5432:5432 \
  -d postgres:15
```

### 3. Configuración de base de datos

En `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/finsight_db
spring.datasource.username=finsight
spring.datasource.password=finsight123
```

---

## 🔐 Autenticación

FinSight usa JWT para proteger los endpoints. Los endpoints públicos son:

- `POST /auth/register`
- `POST /auth/login`

Una vez autenticado, recibirás un token que debes usar en las siguientes peticiones:

```
Authorization: Bearer <tu_token>
```

---

## 🧪 Documentación con Swagger

Swagger UI está disponible en:

👉 [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

---

## 📦 Endpoints protegidos de ejemplo

- `GET /transactions` → devuelve las transacciones del usuario autenticado.
- `POST /transactions` → crea una nueva transacción.

---

## 🔐 Autenticación en Swagger

Para probar endpoints protegidos con JWT en Swagger:

1. Usa `/auth/register` o `/auth/login` para obtener un token.
2. Copia el JWT y haz clic en `Authorize` (candado).
3. Pega el token con el prefijo `Bearer `:

```
Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6...
```

4. Haz clic en `Authorize` y luego en `Close`.

Ya puedes probar endpoints autenticados desde Swagger.

---

## 🧠 Autor

Desarrollado por Gerard.