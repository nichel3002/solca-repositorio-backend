# Proyecto Reto: Repositorio Clinico Regional SOLCA

Arquitectura inicial con Angular no standalone y cinco microservicios Spring Boot separados para SOLCA Cuenca, Manabi y Quito.

## Estructura

```text
paciente-maestro-service/
consulta-clinica-service/
laboratorio-clinico-service/
imagenologia-service/
repositorio-clinico-regional-service/
database/
postman/
docker-compose.yml
```

Cada carpeta de microservicio puede subirse a un repositorio independiente.

## Puertos

| Servicio | Puerto | Base de datos |
| --- | --- | --- |
| Paciente Maestro Regional | 8081 | paciente_maestro_db |
| Consulta Clinica | 8082 | consulta_clinica_db |
| Laboratorio Clinico | 8083 | laboratorio_clinico_db |
| Imagenologia / PACS basico | 8084 | imagenologia_db |
| Repositorio Clinico Regional | 8085 | repositorio_clinico_regional_db |

## Levantar backend completo

Requisito: Docker Desktop.

```powershell
docker compose up --build
```

Endpoint de consulta consolidada, protegido con JWT desde el Avance 3:

```powershell
$login = Invoke-RestMethod -Method Post -Uri http://localhost:8085/auth/login -ContentType 'application/json' -Body '{"username":"medico@solca.local","role":"MEDICO"}'
Invoke-RestMethod -Headers @{ Authorization = "Bearer $($login.token)" } -Uri http://localhost:8085/repositorio/paciente/REG-0001
```

Ruta:

```text
GET http://localhost:8085/repositorio/paciente/REG-0001
```

Respuesta esperada:

```json
{
  "paciente": {},
  "consultas": [],
  "laboratorio": [],
  "imagenes": [],
  "errores": {}
}
```

El repositorio regional consume los otros microservicios por REST y no accede directamente a sus bases de datos.

## Avance 3

El avance 3 agrega:

- Frontend con inicio de sesion por rol, busqueda por ID regional o cedula y auditoria para ADMIN.
- JWT con Spring Security en todos los microservicios.
- Roles `ADMIN`, `MEDICO` y `LABORATORIO`.
- Control de acceso por endpoint.
- Auditoria basica de consultas consolidadas.
- Docker Compose de backend con JWT compartido. El Dockerfile de Angular vive en el repositorio frontend `solca-repositorio-frontend`.
- Documentacion de arquitectura, riesgos, respaldo y plan cloud en `docs/avance-3-frontend-seguridad-contenedores.md`.

## Evidencia para Postman

Importar:

```text
postman/SOLCA-Repositorio-Clinico-Regional.postman_collection.json
```

Tomar capturas de:

- `GET /pacientes`
- `GET /consultas/paciente/REG-0001`
- `GET /laboratorio/paciente/REG-0001`
- `GET /imagenes/paciente/REG-0001`
- `GET /repositorio/paciente/REG-0001`

## Datos de prueba

Los servicios cargan registros para:

- `REG-0001`: SOLCA Cuenca
- `REG-0002`: SOLCA Manabi
- `REG-0003`: SOLCA Quito

## Ejecucion individual

Cada microservicio puede ejecutarse por separado con Maven si se tiene JDK 17 y Maven instalados:

```powershell
cd paciente-maestro-service
mvn spring-boot:run
```

Cambiar la carpeta para ejecutar los demas servicios.
