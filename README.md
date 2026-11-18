# Sistema de Gestion de Pasantias - Facultad de Quimica (UCE)

Sistema web para digitalizar el tramite de pasantias de la Facultad de Quimica de la Universidad Central del Ecuador: desde la solicitud del estudiante hasta el aval firmado electronicamente por el subdecano.

> Estado: **Fase 1 en desarrollo** (solo pasantias).

## Flujo general

1. El estudiante llena la solicitud digital y adjunta documentos.
2. El secretario la revisa y la aprueba u observa.
3. El estudiante realiza la pasantia y sube las evidencias.
4. El secretario revisa las evidencias y genera el aval en PDF.
5. El subdecano firma el aval con su certificado `.p12` (PAdES).
6. El estudiante descarga el aval firmado.

## Roles

Estudiante, Secretario, Subdecano y Administrador.

## Tecnologias

| Capa | Tecnologia |
|---|---|
| Backend | Java 21, Spring Boot 3, Spring Security + JWT, Spring Data JPA, Flyway |
| Firma y PDF | Apache PDFBox, BouncyCastle, Thymeleaf + OpenHTMLtoPDF |
| Frontend | React + TypeScript (Vite), React Router, TanStack Query |
| Base de datos | PostgreSQL 16 |
| Infraestructura | Docker Compose, nginx |

## Estructura del repositorio

```
pasantias-fq/
├── backend/     API REST con Spring Boot
├── frontend/    Aplicacion web con React
├── docs/        API, reglas de negocio, manuales y decisiones
├── infra/       nginx y respaldos
├── .env.example Variables de entorno de ejemplo
└── CONTRIBUTING.md  Guia de ramas y commits
```

## Requisitos previos

- Git
- Docker Desktop
- JDK 21 (para desarrollar el backend)
- Node.js 20 o superior (para desarrollar el frontend)
- IntelliJ IDEA o similar

## Como arrancar (cuando el Sprint 0 este listo)

```bash
# 1. Clonar
git clone <URL-DEL-REPOSITORIO>
cd pasantias-fq

# 2. Variables de entorno
cp .env.example .env      # en PowerShell: Copy-Item .env.example .env
# editar .env con tus valores

# 3. Levantar todo
docker compose up --build
```

El sistema quedara disponible en `http://localhost`.

## Documentacion

- Reglas de negocio: `docs/negocio/`
- Contrato de la API: `docs/api/`
- Decisiones tecnicas: `docs/decisiones/`
- Manuales de usuario: `docs/manuales/`

## Trabajo en equipo

Lee [CONTRIBUTING.md](CONTRIBUTING.md) antes de hacer tu primer commit.

## Seguridad

- Nunca subir `.env`, claves ni archivos `.p12`.
- La clave del certificado de firma nunca se guarda: se pide en cada firma.

## Equipo

- Persona A - Backend: _completar_
- Persona B - Frontend e infraestructura: _completar_

## Licencia

_Por definir con la facultad._
