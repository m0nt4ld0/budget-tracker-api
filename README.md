# Budget Tracker API

API REST construida con Spring Boot para registrar, consultar y categorizar gastos personales. Permite filtrar por rangos de fechas, paginar resultados y mantener auditoría básica de quién crea cada registro.

## Tabla de contenidos
1. [Tecnologías](#tecnologías)
2. [Arquitectura rápida](#arquitectura-rápida)
3. [Requisitos previos](#requisitos-previos)
4. [Configuración local](#configuración-local)
5. [Variables y perfiles](#variables-y-perfiles)
6. [Estructura de base de datos](#estructura-de-base-de-datos)
7. [Endpoints principales](#endpoints-principales)
8. [Manejo de errores](#manejo-de-errores)
9. [Pruebas](#pruebas)
10. [Próximos pasos](#próximos-pasos)
11. [Licencia](#licencia)

## Tecnologías
- **Java 21**
- **Spring Boot 3 / Spring Web**
- **Spring Data JPA**
- **PostgreSQL**
- **Lombok**
- **Maven**

## Arquitectura rápida
- `controller`: expone los endpoints REST para gastos (`GastoController`) y categorías (`CategoriaController`).
- `service`: encapsula la lógica de negocio, validaciones y auditoría ligera (`GastoService`, `CategoriaService`).
- `repository`: interfaces JPA para acceder a la base (`GastoRepository`, `CategoriaRepository`).
- `model/dto`: objetos de intercambio con el cliente para no exponer entidades.
- `entity`: mapea tablas `gastos` y `categorias`.
- `config`: `AuditConfig` obtiene parámetros `app.audit.*` usados para registrar el usuario auditor.

## Requisitos previos
- Git
- Java 21 (JDK)
- Maven 3.9+
- PostgreSQL 15+

## Configuración local
1. **Clonar el repositorio**
   ```bash
   git clone <url-del-repo>
   cd budget-tracker-api
   ```
2. **Configurar base de datos**
   - Crear una base llamada `budget_tracker` (o la que prefieras).
   - Crear un usuario con permisos sobre esa base.
   - Ejecutar el script [`db.sql`](./db.sql) para crear tablas, índices y triggers.
3. **Actualizar credenciales**
   - Edita `src/main/resources/application-dev.yml` y reemplaza `url`, `username` y `password` con tus datos locales.
4. **Levantar el perfil `dev`**
   - El archivo `application.yml` ya activa el perfil `dev`. Puedes cambiarlo vía variable `SPRING_PROFILES_ACTIVE` si necesitas otro.
5. **Ejecutar la aplicación**
   ```bash
   ./mvnw spring-boot:run
   ```
   La API quedará disponible en `http://localhost:8080`.

## Variables y perfiles
- `spring.profiles.active`: controla la configuración cargada. Por defecto `dev`.
- `app.audit.enabled` y `app.audit.default-user`: habilitan la auditoría y fijan el usuario que se registra en `aud_ts_ins_user`. En `dev` toma el mismo valor que `spring.datasource.username`.

## Estructura de base de datos
El archivo [`db.sql`](./db.sql) define:
- Tabla `categorias` auto-relacionada para soportar jerarquías.
- Tabla `gastos` con montos positivos (`chk_importe_positivo`).
- Índices por fecha, categoría y bandera `activo` para optimizar consultas.
- Triggers de auditoría que actualizan `aud_ts_upd*` ante modificaciones.

## Endpoints principales

### Categorías
| Método | Ruta                   | Descripción                     |
|--------|-----------------------|---------------------------------|
| GET    | `/api/categorias/`    | Lista categorías activas.       |
| POST   | `/api/categorias/crear` | Crea una nueva categoría.      |

**Solicitud POST ejemplo**
```json
{
  "categoria": "Transporte"
}
```

### Gastos
| Método | Ruta                 | Descripción                                                |
|--------|---------------------|------------------------------------------------------------|
| GET    | `/api/gastos/`      | Lista gastos entre `fechaDesde` y `fechaHasta` (opcionales) con paginación (`page`, `size`, `sort`). |
| POST   | `/api/gastos/crear` | Crea un gasto validando fecha no futura e importe >= 0.    |

**Solicitud POST ejemplo**
```json
{
  "fecha": "2025-01-05",
  "concepto": "Pasaje de bus",
  "importe": 2.5,
  "categoria": { "id": 1 }
}
```

**Respuesta GET paginada**
```json
{
  "content": [
    {
      "id": 15,
      "fecha": "2025-01-05",
      "concepto": "Pasaje de bus",
      "importe": 2.5,
      "categoria": { "id": 1, "categoria": "Transporte" }
    }
  ],
  "pageable": { "pageNumber": 0, "pageSize": 20 },
  "totalElements": 42
}
```

## Manejo de errores
Las principales validaciones arrojan excepciones personalizadas:
- `GastoFechaFuturaException`: la fecha no puede ser posterior a hoy.
- `GastoImporteNegativoException`: el importe debe ser positivo.
- `FechaInvalidaException`: `fechaDesde` no puede ser mayor a `fechaHasta`.
- `CategoriaNotFoundException`: se requiere una categoría válida.
- `DatabaseConnectionException`: errores al persistir datos.
- `RequestBodyInvalidException`: payloads incompletos.

Todas son interceptadas por el `GlobalExceptionHandler` (Spring `@ControllerAdvice`) para devolver respuestas consistentes.

## Pruebas
Actualmente se cuenta con la dependencia `spring-boot-starter-test`. Ejecuta todas las pruebas con:
```bash
./mvnw test
```
> Aún no hay suites agregadas; se recomienda incorporarlas conforme se añada lógica.

## Próximos pasos
1. Autenticación y multiusuario.
2. Exportar reportes a CSV/Excel.
3. Cobertura de pruebas unitarias y de integración.

## Licencia
Distribuido bajo la licencia [MIT](./LICENSE).