# Despliegue en VPS compartido

Este despliegue usa nombres y puertos propios para no chocar con otros proyectos del VPS.

## Repositorios

```bash
mkdir -p ~/solca-avance3
cd ~/solca-avance3
git clone https://github.com/nichel3002/solca-repositorio-backend.git
git clone https://github.com/nichel3002/solca-repositorio-frontend.git
```

## Variables

```bash
cd ~/solca-avance3/solca-repositorio-backend
cp .env.vps.example .env
nano .env
```

Puertos por defecto:

- Frontend publico: `19080`
- Backend publico para pruebas: `19085`
- PostgreSQL y microservicios clinicos: solo red interna Docker

## Levantar

```bash
docker compose -f docker-compose.vps.yml up -d --build
```

## Verificar

```bash
docker compose -f docker-compose.vps.yml ps
curl http://localhost:19080
curl -X POST http://localhost:19085/auth/login \
  -H 'Content-Type: application/json' \
  -d '{"username":"medico@solca.local","role":"MEDICO"}'
```

Abrir en navegador:

```text
http://IP_DEL_VPS:19080
```

## Actualizar despliegue

```bash
cd ~/solca-avance3/solca-repositorio-backend
git pull
cd ../solca-repositorio-frontend
git pull
cd ../solca-repositorio-backend
docker compose -f docker-compose.vps.yml up -d --build
```

## Detener solo este proyecto

```bash
docker compose -f docker-compose.vps.yml down
```

Para eliminar tambien la base de datos de este proyecto:

```bash
docker compose -f docker-compose.vps.yml down -v
```
