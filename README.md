# Proyecto Reto: Repositorio Clinico Regional SOLCA

Arquitectura inicial con Angular no standalone y cinco microservicios Spring Boot separados para SOLCA Cuenca, Manabi y Quito.

## Estructura

```text
paciente-maestro-service/
consulta-clinica-service/
laboratorio-clinico-service/
imagenologia-service/
repositorio-clinico-regional-service/
solca-repositorio-angular/
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
| Angular | 4200 | No aplica |

## Levantar backend completo

Requisito: Docker Desktop.

```powershell
docker compose up --build
```

## Autenticacion JWT

Credenciales de demostracion:

```text
usuario: admin
clave: admin123
```

Primero solicitar token:

```text
POST http://localhost:8085/auth/login
```

Body:

```json
{
  "username": "admin",
  "password": "admin123"
}
```

Luego enviar el token en cada endpoint protegido:

```text
Authorization: Bearer TOKEN
```

Endpoint obligatorio del Avance 2:

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

## Levantar frontend

```powershell
cd solca-repositorio-angular
npm install
npm start
```

Abrir:

```text
http://localhost:4200
```

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
