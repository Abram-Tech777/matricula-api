# matricula-api

API REST de matrícula para el curso Pruebas de Software (CIBERTEC). Se usa como sistema bajo prueba en los ejercicios de BDD con Gherkin, Cucumber, Serenity y Rest Assured.

Stack: Java 21, Spring Boot 4.1, Spring Data JPA, MySQL, springdoc-openapi.

## Cómo ejecutarla

1. Copia `.env.example` como `.env` y completa los datos de tu MySQL:

   ```properties
   DB_URL=jdbc:mysql://localhost:3306/matricula_db?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=America/Lima
   DB_USER=root
   DB_PASSWORD=tu_clave
   ```

2. Arranca la aplicación desde IntelliJ o con `./mvnw spring-boot:run` (en Windows, `mvnw.cmd spring-boot:run`). El `.env` se lee desde la carpeta del proyecto.

La base `matricula_db` se crea sola. En cada arranque se borran y recrean las tablas y se cargan los datos de `src/main/resources/data.sql`, así que siempre se parte del mismo estado. Si una prueba deja datos que estorban, reinicia la API.

Con la API arriba:

- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI en JSON: http://localhost:8080/v3/api-docs
- Estado: http://localhost:8080/actuator/health

## Endpoints

Cursos

| Método | Ruta | Respuestas |
|---|---|---|
| GET | `/api/cursos?ciclo=&modalidad=&dia=&nombre=` | 200, 400 |
| GET | `/api/cursos/{codigo}` | 200, 404 |
| POST | `/api/cursos` | 201, 400, 409 |
| PUT | `/api/cursos/{codigo}` | 200, 400, 404, 422 |
| DELETE | `/api/cursos/{codigo}` | 204, 404, 409 |

Alumnos

| Método | Ruta | Respuestas |
|---|---|---|
| GET | `/api/alumnos?nombre=&deuda=` | 200 |
| GET | `/api/alumnos/{codigo}` | 200, 404 |
| GET | `/api/alumnos/{codigo}/constancia` | 200, 404 |
| POST | `/api/alumnos` | 201, 400, 409 |
| PUT | `/api/alumnos/{codigo}` | 200, 400, 404, 409 |
| DELETE | `/api/alumnos/{codigo}` | 204, 404, 409 |

Matrículas

| Método | Ruta | Respuestas |
|---|---|---|
| POST | `/api/matriculas/cotizacion` | 200, 400, 404, 422 |
| POST | `/api/matriculas` | 201, 400, 404, 409, 422 |
| GET | `/api/matriculas?alumno=&curso=&estado=` | 200, 400 |
| GET | `/api/matriculas/{id}` | 200, 400, 404 |
| PATCH | `/api/matriculas/{id}/pagar` | 200, 404, 409 |
| PATCH | `/api/matriculas/{id}/anular` | 200, 404, 409 |

Todos los filtros son opcionales. Los valores válidos son `modalidad`: PRESENCIAL, VIRTUAL, HIBRIDO; `dia`: LUNES a SABADO; `estado`: REGISTRADA, PAGADA, ANULADA.

## Flujo de una matrícula

1. Cotizar: `POST /api/matriculas/cotizacion` con `{"codigoAlumno": "i202610001", "cursos": ["PS5511", "TS5514"]}`. Calcula créditos, subtotal, descuento y total sin guardar nada.
2. Matricular: `POST /api/matriculas` con `{"codigoAlumno": "i202610001", "codigoCurso": "PS5511"}`. Queda en estado REGISTRADA y ocupa una vacante.
3. Pagar: `PATCH /api/matriculas/{id}/pagar`. Pasa a PAGADA.
4. Anular: `PATCH /api/matriculas/{id}/anular`. Solo si no está pagada. Libera la vacante.
5. Constancia: `GET /api/alumnos/{codigo}/constancia`. Cursos vigentes, créditos, monto total, pagado y pendiente.

## Reglas

El monto de cada curso es créditos × S/ 120. Con promedio 17 o más se descuenta 20 %; con 15 o más, 10 %. Es la misma lógica de `CalculadoraMatricula` que se trabajó con TDD.

Al matricular, la API revisa en este orden:

| Caso | HTTP | codigo |
|---|---|---|
| El alumno no existe | 404 | ALUMNO_NO_ENCONTRADO |
| El curso no existe | 404 | CURSO_NO_ENCONTRADO |
| El alumno tiene deuda | 422 | ALUMNO_CON_DEUDA |
| Ya está matriculado en ese curso | 409 | MATRICULA_DUPLICADA |
| El curso no tiene vacantes | 409 | SIN_VACANTES |
| El horario se cruza con otro curso suyo | 409 | CRUCE_HORARIO |
| Pasaría de 24 créditos | 422 | LIMITE_CREDITOS |

Otras reglas:

| Caso | HTTP | codigo |
|---|---|---|
| Pagar una matrícula ya pagada | 409 | MATRICULA_YA_PAGADA |
| Pagar una matrícula anulada | 409 | MATRICULA_ANULADA |
| Anular una matrícula pagada | 409 | MATRICULA_PAGADA |
| Anular una matrícula ya anulada | 409 | MATRICULA_YA_ANULADA |
| Matrícula inexistente | 404 | MATRICULA_NO_ENCONTRADA |
| Alumno o curso con código repetido | 409 | ALUMNO_DUPLICADO, CURSO_DUPLICADO |
| Email usado por otro alumno | 409 | EMAIL_DUPLICADO |
| Hora de inicio igual o posterior a la de fin | 400 | HORARIO_INVALIDO |
| Bajar las vacantes por debajo de los matriculados | 422 | VACANTES_INSUFICIENTES |
| Eliminar un alumno o curso que tiene matrículas | 409 | ALUMNO_CON_MATRICULAS, CURSO_CON_MATRICULAS |
| Campos obligatorios vacíos o con formato incorrecto | 400 | DATOS_INVALIDOS |

## Formato de los errores

Los errores siguen el formato ProblemDetail (RFC 9457) y agregan el campo `codigo`:

```json
{
  "type": "about:blank",
  "title": "Conflict",
  "status": 409,
  "detail": "El curso IA5513 no tiene vacantes",
  "instance": "/api/matriculas",
  "codigo": "SIN_VACANTES"
}
```

En los errores de validación también viene `errores`, con el mensaje de cada campo. Por ejemplo, al registrar un alumno con código `X1`:

```json
{
  "status": 400,
  "detail": "Datos de entrada inválidos",
  "codigo": "DATOS_INVALIDOS",
  "errores": { "codigo": "debe tener el formato i + 9 dígitos" }
}
```

Los mensajes de las validaciones estándar (`@NotBlank`, `@Email`, `@Min`) cambian de idioma según el header `Accept-Language`: con `es` dicen "no debe estar vacío" y con `en`, "must not be blank".

## Datos de prueba

Cursos

| Código | Nombre | Ciclo | Créditos | Vacantes | Modalidad | Horario |
|---|---|---|---|---|---|---|
| PS5511 | Pruebas de Software | 6 | 3 | 30 | PRESENCIAL | LUNES 08:00-11:00 |
| DM5512 | Desarrollo de Aplicaciones Móviles | 6 | 4 | 25 | HIBRIDO | MARTES 08:00-12:00 |
| IA5513 | Inteligencia Artificial Aplicada | 6 | 3 | 1 | VIRTUAL | MIERCOLES 08:00-11:00 |
| TS5514 | Taller de Software | 6 | 4 | 30 | HIBRIDO | JUEVES 08:00-12:00 |
| QA5517 | Gestión de Calidad de Software | 6 | 3 | 30 | VIRTUAL | LUNES 09:00-12:00 |
| PI5620 | Proyecto Integrador | 6 | 6 | 20 | PRESENCIAL | VIERNES 08:00-14:00 |
| SA5516 | Seguridad de Aplicaciones | 5 | 4 | 30 | VIRTUAL | LUNES 14:00-18:00 |
| BD5410 | Base de Datos Avanzado | 5 | 4 | 30 | PRESENCIAL | MARTES 14:00-18:00 |

Alumnos

| Código | Nombre | Promedio | Deuda | Matrículas al arrancar |
|---|---|---|---|---|
| i202610001 | Ana Torres | 18.0 | no | DM5512 (anulada) |
| i202610002 | Luis Ramos | 16.0 | no | PS5511 |
| i202610003 | Carla Quispe | 12.0 | no | IA5513, que ocupa la única vacante |
| i202610004 | Jorge Huamán | 14.0 | sí | ninguna |
| i202610005 | Rosa Medina | 15.5 | no | PI5620 (pagada), PS5511, DM5512, SA5516, BD5410: 21 créditos |

Las matrículas iniciales tienen los ids 1 a 8 en este orden: Carla IA5513, Rosa PI5620, Rosa PS5511, Rosa DM5512, Rosa SA5516, Rosa BD5410, Luis PS5511 y Ana DM5512.

Casos listos para probar, partiendo de la API recién iniciada:

| Caso | Request | Resultado |
|---|---|---|
| Matrícula válida, 20 % | Ana en PS5511 | 201, monto 288.0 |
| Matrícula válida, 10 % | Luis en TS5514 | 201, monto 432.0 |
| Matrícula válida, sin descuento | Carla en PS5511 | 201, monto 360.0 |
| Volver a matricular un curso anulado | Ana en DM5512 | 201, monto 384.0 |
| Duplicada | Carla en IA5513 | 409 MATRICULA_DUPLICADA |
| Sin vacantes | Ana en IA5513 | 409 SIN_VACANTES |
| Con deuda | Jorge en PS5511 | 422 ALUMNO_CON_DEUDA |
| Cruce de horario | Luis en QA5517 | 409 CRUCE_HORARIO |
| Límite de créditos | Rosa en TS5514 | 422 LIMITE_CREDITOS |
| Cotización | Ana con PS5511 y TS5514 | 200, 7 créditos, subtotal 840.0, total 672.0 |
| Pagar | `PATCH /api/matriculas/7/pagar` | 200, estado PAGADA |
| Anular una pagada | `PATCH /api/matriculas/2/anular` | 409 MATRICULA_PAGADA |
| Anular una ya anulada | `PATCH /api/matriculas/8/anular` | 409 MATRICULA_YA_ANULADA |
| Anular una inexistente | `PATCH /api/matriculas/99999/anular` | 404 MATRICULA_NO_ENCONTRADA |
| Constancia | Rosa | 21 créditos, total 2268.0, pagado 648.0, pendiente 1620.0 |
