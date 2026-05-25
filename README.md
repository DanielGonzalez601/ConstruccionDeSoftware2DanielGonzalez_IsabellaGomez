# Bank Management System
### Construcción de Software 2 — Lunes y Miércoles 8:00PM - 10:00PM

Sistema bancario completo construido con **Java Spring Boot**, **Arquitectura Hexagonal (Ports & Adapters)** y principios de **Domain-Driven Design (DDD)**. Permite gestionar clientes (personas naturales y empresas), cuentas bancarias, préstamos y transferencias con flujos de aprobación, autenticación JWT y bitácora de auditoría.

---

## Integrantes del Equipo

| Nombre | GitHub |
|---|---|
| Isabella Gómez Parra | [@IsabellaGomezP](https://github.com/IsabellaGomezP) |
| Daniel Eduardo González Palacio | [@DanielGonzalez601](https://github.com/DanielGonzalez601) |
| Yorlando David Montiel Guerra | — |

---

## Tabla de Contenido

1. [Tecnologías](#tecnologías)
2. [Requisitos previos](#requisitos-previos)
3. [Configuración de la base de datos](#configuración-de-la-base-de-datos)
4. [Cómo correr el proyecto](#cómo-correr-el-proyecto)
5. [Credenciales de prueba](#credenciales-de-prueba)
6. [Documentación de la API](#documentación-de-la-api)
7. [Endpoints disponibles](#endpoints-disponibles)
8. [Flujos de negocio](#flujos-de-negocio)
9. [Reglas de negocio](#reglas-de-negocio)
10. [Permisos por rol](#permisos-por-rol)
11. [Arquitectura del proyecto](#arquitectura-del-proyecto)
12. [Estructura de carpetas](#estructura-de-carpetas)

---

## Tecnologías

| Tecnología | Versión | Uso |
|---|---|---|
| **Java** | 17 | Lenguaje principal |
| **Spring Boot** | 4.0.6 | Framework principal |
| **Spring Security** | — | Autenticación y autorización |
| **Spring Data JPA** | — | Persistencia ORM |
| **MySQL** | 8.x | Base de datos relacional |
| **JWT (jjwt)** | 0.12.5 | Tokens de autenticación |
| **SpringDoc OpenAPI** | 2.8.8 | Documentación Swagger |
| **Maven** | 3.8+ | Gestión de dependencias |

---

## Requisitos Previos

Antes de ejecutar el proyecto asegúrate de tener instalado:

| Herramienta | Versión mínima | Descarga |
|---|---|---|
| **Java JDK** | 17 o superior | https://adoptium.net |
| **Maven** | 3.8 o superior | https://maven.apache.org/download.cgi |
| **MySQL** | 8.x | https://dev.mysql.com/downloads |
| **Git** | Cualquier versión | https://git-scm.com |

Verifica la instalación abriendo una terminal:
```bash
java -version   # debe mostrar: openjdk 17.x.x o superior
mvn -version    # debe mostrar: Apache Maven 3.x.x
mysql --version # debe mostrar: mysql  Ver 8.x.x
```

---

## Configuración de la Base de Datos

### Paso 1 — Crear la base de datos en MySQL

Abre MySQL Workbench o la terminal de MySQL y ejecuta:
```sql
CREATE DATABASE bankdb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### Paso 2 — Configurar `application.properties`

Abre el archivo `banco-app/src/main/resources/application.properties` y ajusta los valores según tu entorno:

```properties
spring.application.name=bank-app

# Base de datos (MySQL)
spring.datasource.url=jdbc:mysql://localhost:3306/bankdb
spring.datasource.username=root
spring.datasource.password=TU_CONTRASEÑA
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect

# JWT Configuration
app.jwt.secret=bankHexagonalSecretKeyParaFirmarTokenssDGconHS256Seguro
app.jwt.expiration-ms=86400000

# Umbral de aprobación de transferencias
app.transfer.approval-threshold=2000000
```

> **Importante:** El puerto por defecto de MySQL es `3306`. Verifica el tuyo antes de correr el proyecto.

---

## Cómo Correr el Proyecto

### Paso 1 — Clonar el repositorio
```bash
git clone https://github.com/DanielGonzalez601/ConstruccionDeSoftware2DanielGonzalez_IsabellaGomez.git
cd ConstruccionDeSoftware2DanielGonzalez_IsabellaGomez/banco-app
```

### Paso 2 — Configurar la base de datos
Sigue los pasos de la sección [Configuración de la Base de Datos](#configuración-de-la-base-de-datos).

### Paso 3 — Ejecutar el proyecto
```bash
mvn spring-boot:run
```

### Paso 4 — Verificar que inició correctamente

Verás en la consola:
```
[SEED] CREDENCIALES DE PRUEBA (POST /api/auth/login):
[SEED]   Analyst:      ID=DAN001  | Pass=daniel123
[SEED]   Teller:       ID=TEL001  | Pass=teller123
[SEED]   Commercial:   ID=ISA001  | Pass=isabella123
[SEED]   Client 1:     ID=YOR001  | Pass=yorlando123  | Acc=ACC0000000001 $15,000
[SEED]   Client 2:     ID=CLI002  | Pass=vale123      | Acc=ACC0000000002 $8,500
[SEED]   Corp Admin:   ID=CORP001 | Pass=empresa123   | Acc=ACC0000000003 $250,000
[SEED]   Supervisor:   ID=SUP001  | Pass=supervisor123
[SEED]   Co. Employee: ID=EMP001  | Pass=mariana123
Started BankApplication in X.XXX seconds
```

El servidor estará listo en: `http://localhost:8080`

### Errores comunes

| Error | Causa | Solución |
|---|---|---|
| `Communications link failure` | MySQL no está corriendo | Inicia el servicio MySQL |
| `Access denied for user 'root'` | Contraseña incorrecta | Verifica `spring.datasource.password` |
| `Unknown database 'bankdb'` | La BD no existe | Ejecuta `CREATE DATABASE bankdb;` |
| `Port 8080 already in use` | Puerto ocupado (ej: Apache) | Ejecuta `netstat -ano \| findstr :8080` y mata el proceso con `taskkill /PID [número] /F` |
| `Port 3306 connection refused` | Puerto MySQL incorrecto | Verifica el puerto en XAMPP/MySQL y ajusta el `application.properties` |

---

## Credenciales de Prueba

Estas credenciales se crean automáticamente al iniciar el servidor por primera vez:

| Rol | Nombre | ID | Contraseña | Cuenta | Saldo inicial |
|---|---|---|---|---|---|
| **Analista Interno** | Daniel Gonzalez | `DAN001` | `daniel123` | — | — |
| **Cajero (Teller)** | Carlos Restrepo | `TEL001` | `teller123` | — | — |
| **Empleado Comercial** | Isabella Gomez | `ISA001` | `isabella123` | — | — |
| **Cliente Individual — activo** | Yorlando Montiel | `YOR001` | `yorlando123` | `ACC0000000001` | $15,000 |
| **Cliente Individual — inactivo** | Valentina Torres | `CLI002` | `vale123` | `ACC0000000002` | $8,500 |
| **Admin Empresa** | Empresas Andinas | `CORP001` | `empresa123` | `ACC0000000003` | $250,000 |
| **Supervisor Empresa** | Alejandro Rios | `SUP001` | `supervisor123` | — | — |
| **Empleado Empresa** | Mariana Castillo | `EMP001` | `mariana123` | — | — |

---

## Documentación de la API

Con el servidor corriendo, accede a:

| URL | Descripción |
|---|---|
| `http://localhost:8080/swagger-ui.html` | Interfaz visual Swagger — prueba todos los endpoints |
| `http://localhost:8080/v3/api-docs` | JSON de la especificación OpenAPI (para importar en Postman) |

### Autenticarse en Swagger

1. Abre `http://localhost:8080/swagger-ui.html`
2. Expande **Authentication → POST /api/auth/login → Try it out**
3. Ingresa las credenciales y haz clic en **Execute**
4. Copia el `token` de la respuesta
5. Clic en el botón **Authorize** (esquina superior derecha)
6. Pega el token en el campo **bearerAuth** y clic en **Authorize**

### Importar en Postman

1. Asegúrate de que el servidor está corriendo
2. Abre Postman → **Import → Link**
3. Pega: `http://localhost:8080/v3/api-docs`
4. Clic en **Continue → Import**

Para configurar el token automático, en la request de Login ve a la pestaña **Tests** y pega:
```javascript
const response = pm.response.json();
if (response.data && response.data.token) {
    pm.collectionVariables.set("token", response.data.token);
}
```

---

## Endpoints Disponibles

**Base URL:** `http://localhost:8080`

### Autenticación (pública — no requiere token)
| Método | Endpoint | Descripción |
|---|---|---|
| `POST` | `/api/auth/login` | Login — obtiene token JWT |
| `POST` | `/api/auth/register` | Registrar nuevo usuario |

### Usuarios
| Método | Endpoint | Roles permitidos |
|---|---|---|
| `GET` | `/api/users` | `INTERNAL_ANALYST` |
| `GET` | `/api/users/{id}` | Todos (solo propio para clientes) |
| `GET` | `/api/users/by-identification/{idNumber}` | Todos |
| `PATCH` | `/api/users/{id}/status` | `INTERNAL_ANALYST` |

### Cuentas
| Método | Endpoint | Roles permitidos |
|---|---|---|
| `POST` | `/api/accounts` | `TELLER`, `COMMERCIAL`, `ANALYST`, Clientes |
| `GET` | `/api/accounts` | `ANALYST`, `TELLER`, `COMMERCIAL` |
| `GET` | `/api/accounts/{accountNumber}` | Todos (solo propias para clientes) |
| `GET` | `/api/accounts/owner/{idNumber}` | Todos (solo propias para clientes) |
| `POST` | `/api/accounts/{number}/deposit` | `TELLER`, `ANALYST` |
| `POST` | `/api/accounts/{number}/withdraw` | `TELLER`, `ANALYST` |
| `PATCH` | `/api/accounts/{number}/block` | `ANALYST` |
| `PATCH` | `/api/accounts/{number}/unblock` | `ANALYST` |

### Prestamos
| Método | Endpoint | Roles permitidos |
|---|---|---|
| `POST` | `/api/loans` | Clientes, `COMMERCIAL`, `ANALYST` |
| `GET` | `/api/loans` | `ANALYST`, `COMMERCIAL` |
| `GET` | `/api/loans/status/{status}` | `ANALYST`, `COMMERCIAL` |
| `GET` | `/api/loans/{id}` | Todos (solo propios para clientes) |
| `GET` | `/api/loans/client/{idNumber}` | Todos (solo propios para clientes) |
| `POST` | `/api/loans/{id}/approve` | `ANALYST` |
| `POST` | `/api/loans/{id}/reject` | `ANALYST` |
| `POST` | `/api/loans/{id}/disburse` | `ANALYST` |

### Transferencias
| Método | Endpoint | Roles permitidos |
|---|---|---|
| `POST` | `/api/transfers` | Clientes, `COMPANY_EMPLOYEE`, `ANALYST` |
| `GET` | `/api/transfers` | `ANALYST` |
| `GET` | `/api/transfers/pending` | `SUPERVISOR`, `CLIENT_COMPANY`, `ANALYST` |
| `GET` | `/api/transfers/{id}` | Todos |
| `GET` | `/api/transfers/account/{number}` | Todos (solo propias para clientes) |
| `POST` | `/api/transfers/{id}/approve` | `SUPERVISOR`, `CLIENT_COMPANY`, `ANALYST` |
| `POST` | `/api/transfers/{id}/reject` | `SUPERVISOR`, `CLIENT_COMPANY`, `ANALYST` |
| `POST` | `/api/transfers/process-expired` | `ANALYST` |

### Bitacora de Auditoria
| Método | Endpoint | Roles permitidos |
|---|---|---|
| `GET` | `/api/audit-log` | `ANALYST` |
| `GET` | `/api/audit-log/product/{productId}` | Todos |
| `GET` | `/api/audit-log/user/{userId}` | `ANALYST` |

---

## Flujos de Negocio

### Flujo 1 — Solicitar y aprobar un prestamo

```
1. Login como YOR001 / yorlando123

2. POST /api/loans
   {
     "loanType": "Personal",
     "requestedAmount": 5000,
     "currency": "USD",
     "termMonths": 12,
     "disbursementAccountNumber": "ACC0000000001"
   }
   → Estado: UNDER_REVIEW

3. Login como DAN001 / daniel123

4. GET /api/loans/status/UNDER_REVIEW   <- ver pendientes

5. POST /api/loans/{id}/approve
   {
     "approvedAmount": 4500,
     "currency": "USD",
     "interestRate": 8.5,
     "termMonths": 12
   }
   → Estado: APPROVED

6. POST /api/loans/{id}/disburse        <- sin body
   → Estado: DISBURSED
   → Saldo de ACC0000000001 aumenta en $4,500
```

### Flujo 2 — Transferencia empresarial con aprobacion

```
1. Login como EMP001 / mariana123

2. POST /api/transfers
   {
     "sourceAccount": "ACC0000000003",
     "destinationAccount": "ACC0000000001",
     "amount": 10000
   }
   → Estado: PENDING_APPROVAL (supera el umbral de $2,000,000)

3. Login como SUP001 / supervisor123

4. GET /api/transfers/pending           <- ver pendientes

5. POST /api/transfers/{id}/approve     <- sin body
   → Estado: EXECUTED
   → Saldos actualizados en ambas cuentas
```

### Flujo 3 — Transferencia directa entre clientes

```
1. Login como YOR001 / yorlando123

2. POST /api/transfers
   {
     "sourceAccount": "ACC0000000001",
     "destinationAccount": "ACC0000000002",
     "amount": 500
   }
   → Estado: EXECUTED inmediatamente (no requiere aprobacion)
```

### Flujo 4 — Vencimiento automatico de transferencias

Las transferencias en estado `PENDING_APPROVAL` que no se aprueben en **60 minutos** cambian automaticamente a `EXPIRED`. El scheduler lo revisa cada **5 minutos**.

Para forzarlo manualmente:
```
1. Login como DAN001 / daniel123
2. POST /api/transfers/process-expired
   → Las transferencias vencidas pasan a EXPIRED y se registra en la bitacora
```

---

## Reglas de Negocio

### Lo que el sistema permite

| Operacion | Condicion requerida |
|---|---|
| Registrar cliente | Edad minima 18 años para `CLIENT_INDIVIDUAL` |
| Abrir cuenta | Cliente en estado `ACTIVE` |
| Solicitar prestamo | Cliente activo con cuenta destino valida |
| Aprobar prestamo | Solo desde estado `UNDER_REVIEW` |
| Desembolsar prestamo | Solo desde estado `APPROVED` |
| Transferencia directa | Cuenta origen activa y saldo suficiente |
| Transferencia con aprobacion | Empleados de empresa con monto > umbral configurado |
| Aprobar transferencia | Solo si esta en `PENDING_APPROVAL` y dentro de 60 minutos |

### Lo que el sistema bloquea

| Accion bloqueada | Motivo |
|---|---|
| Transferir desde cuenta `BLOQUEADA` | Cuenta no operativa |
| Transferir sin saldo suficiente | Fondos insuficientes |
| Aprobar prestamo ya aprobado | Solo se aprueba desde `UNDER_REVIEW` |
| Desembolsar sin aprobar primero | El prestamo debe estar en `APPROVED` |
| Aprobar transferencia vencida | Pasaron mas de 60 minutos |
| Ver cuentas de otros clientes | Un cliente solo ve las suyas |
| Aprobar transferencia de otra empresa | El supervisor solo aprueba las de su empresa |
| Crear usuario con ID duplicado | El numero de identificacion es unico |
| Email sin `@` | Formato invalido |
| Telefono fuera de 7-15 digitos | Longitud invalida |

---

## Permisos por Rol

| Operacion | Analyst | Teller | Commercial | Client Ind. | Client Comp. | Co. Employee | Supervisor |
|---|:---:|:---:|:---:|:---:|:---:|:---:|:---:|
| Ver todos los usuarios | Si | No | No | No | No | No | No |
| Ver su propio perfil | Si | Si | Si | Si | Si | Si | Si |
| Abrir cuenta | Si | Si | Si | Si | Si | No | No |
| Depositar / Retirar | Si | Si | No | No | No | No | No |
| Bloquear / Desbloquear cuenta | Si | No | No | No | No | No | No |
| Solicitar prestamo | Si | No | Si | Si | Si | No | No |
| Aprobar / Rechazar / Desembolsar prestamo | Si | No | No | No | No | No | No |
| Crear transferencia | Si | No | No | Si | Si | Si | No |
| Aprobar / Rechazar transferencia | Si | No | No | No | Si | No | Si |
| Ver bitacora completa | Si | No | No | No | No | No | No |
| Ver bitacora de sus operaciones | Si | Si | Si | Si | Si | Si | Si |

---

## Arquitectura del Proyecto

Este proyecto implementa **Arquitectura Hexagonal (Ports & Adapters)** con principios **DDD**:

```
+-----------------------------------------------------+
|  ADAPTER LAYER - IN  (adapter/in/web)               |
|  REST Controllers -> reciben requests HTTP           |
|  TransferExpiryScheduler -> dispara expiracion       |
+----------------------+------------------------------+
                       | llama a Input Ports
+----------------------v------------------------------+
|  APPLICATION LAYER  (application/)                  |
|  Use Cases: UserUseCase, AccountUseCase,            |
|             LoanUseCase, TransferUseCase            |
|  Input Ports: interfaces de entrada                 |
|  Output Ports: interfaces de salida                 |
|  DTOs: Commands y Responses                         |
+----------------------+------------------------------+
                       | usa el dominio
+----------------------v------------------------------+
|  DOMAIN LAYER  (domain/)  <- SIN Spring, SIN JPA   |
|  Aggregates: BankAccount, Loan, Transfer            |
|  Entities: User, AuditLog                           |
|  Value Objects: Money, Email, PhoneNumber           |
|  Domain Services: TransferDomainService,            |
|                   LoanDisbursementDomainService     |
|  Exceptions: DomainValidationException, etc.        |
+----------------------+------------------------------+
                       | implementado por
+----------------------v------------------------------+
|  ADAPTER LAYER - OUT  (adapter/out/persistence)     |
|  JPA Entities + Spring Data Repositories            |
|  Persistence Adapters implementan Output Ports      |
|  Mappers: Domain <-> JPA Entity                     |
+-----------------------------------------------------+
```

### Conceptos DDD aplicados

| Concepto DDD | Clase en el proyecto |
|---|---|
| **Aggregate Root** | `BankAccount`, `Loan`, `Transfer` |
| **Entity** | `User`, `AuditLog` |
| **Value Object** | `Money`, `Email`, `PhoneNumber`, `AccountStatus`, `LoanStatus`, `TransferStatus`, `UserRole` |
| **Domain Service** | `TransferDomainService`, `LoanDisbursementDomainService` |
| **Input Port** | `UserInputPort`, `AccountInputPort`, `LoanInputPort`, `TransferInputPort` |
| **Output Port** | `UserRepositoryPort`, `BankAccountRepositoryPort`, `LoanRepositoryPort`, `TransferRepositoryPort`, `AuditLogRepositoryPort` |
| **Driving Adapter** | `AuthController`, `AccountController`, `LoanController`, `TransferController`, `TransferExpiryScheduler` |
| **Driven Adapter** | `UserPersistenceAdapter`, `BankAccountPersistenceAdapter`, `LoanPersistenceAdapter`, `TransferPersistenceAdapter`, `AuditLogPersistenceAdapter` |

---

## Estructura de Carpetas

```
banco-app/
├── pom.xml
└── src/main/java/com/bank/
    ├── BankApplication.java               <- Punto de entrada
    ├── domain/                            <- Logica pura de negocio (sin Spring)
    │   ├── model/
    │   │   ├── aggregate/                 <- BankAccount, Loan, Transfer
    │   │   ├── entity/                    <- User, AuditLog
    │   │   └── valueobject/               <- Money, Email, Enums
    │   ├── service/                       <- TransferDomainService, LoanDisbursementDomainService
    │   ├── repository/                    <- Interfaces de repositorio del dominio
    │   └── exception/                     <- Excepciones de dominio
    ├── application/                       <- Casos de uso
    │   ├── usecase/                       <- UserUseCase, AccountUseCase, LoanUseCase, TransferUseCase
    │   ├── port/
    │   │   ├── input/                     <- Interfaces de entrada (Input Ports)
    │   │   └── output/                    <- Interfaces de salida (Output Ports)
    │   └── dto/                           <- BankingDto, UserDto (Commands y Responses)
    ├── adapter/
    │   ├── in/
    │   │   ├── web/controller/            <- AuthController, AccountController, LoanController...
    │   │   └── scheduler/                 <- TransferExpiryScheduler
    │   └── out/persistence/               <- JPA Entities, Repositories, Mappers, Adapters
    ├── config/                            <- SecurityConfig, JwtTokenProvider, DataSeeder, AppConfig
    └── shared/                            <- SecurityContextHelper, AccountNumberGenerator
```