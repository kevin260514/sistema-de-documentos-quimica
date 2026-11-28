# Auditoría y sincronización MVP v1.2
Estado: propuesta documental para Sprint 0; no certifica implementación ni despliegue.

## Diferencias verificadas en main/dev
- README anterior: firma PAdES en fase 1; v1.2 exige firma manuscrita escaneada. README corregido en esta rama.
- docker-compose.yml y frontend/nginx.conf: HTTP, falta configuración TLS; PostgreSQL publicado en loopback, contrario al perfil institucional sin puertos BD.
- .env.example, docker-compose.yml y application.yml: JWT 60 min frente a 15 min / refresh 8 h requeridos; configuración de desarrollo no apta para producción.
- application.yml: secreto JWT de respaldo local; eliminar fallback en perfil institucional.
- Compose: Mailpit en dev frente a fase2, volúmenes sin evidencia de LUKS2, mismo usuario de inicialización y aplicación. Separar propietario/migrador y usuario de ejecución.
- pom.xml: Spring Boot 4.1.2, README anterior indicaba 3. Confirmar build y compatibilidad; PDF/firma aún aparecen como tareas.
- V1__init.sql y docs/api/openapi.yaml no encontrados en las rutas esperadas, tanto main como dev; comprobar inventario antes de afirmar ausencia total.

## Backlog de sincronización (Anexo B / HU-31)
| ID | Entregable | Criterio de cierre |
|---|---|---|
| a | contador_aval | unicidad anual, concurrencia, rollback |
| b | firma y futura firma_electronica | escaneo inmutable, PAdES diferido |
| c | notificaciones | estado, lectura, entidad |
| d | tutor y suplencias | vigencia y no solapamiento |
| e | Compose | perfiles fase2/remote/av, URL y CORS |
| f | sesión | JWT 15m, refresh 8h revocable |
| g | ADR 001–004 | decisiones versionadas |
| h | estados | T-01..19 y A-01..05, pruebas exhaustivas |
| i | HTTPS y almacenamiento | pruebas TLS y evidencia de cifrado |
| j | modelo y permisos | Flyway, configuración, horas, auditoría append-only |

## Decisiones pendientes propuestas
- P0: Resolver interacción P-15 con índice único parcial y reaperturas T-15/T-18; proteger invariantes en concurrencia.
- P0: Definir semántica exacta de expiración, reenvío, cambio de parámetros y bloqueo de transiciones concurrentes; completar diagrama T-17/T-18.
- P0: Aclarar si titular mantiene atribuciones durante suplencia, segregación de funciones y revocación inmediata de autorizaciones.
- P0: Especificar transacción y reconciliación entre archivos, BD y notificaciones; respaldos coherentes y restauración probada.
- P0: Especificar cotejo de escaneo con borrador, confirmación humana y custodia; hash no acredita autenticidad de firma.
- P0: Aclarar revocación de JWT de acceso y rotación/reutilización de refresh; protección CSRF y matriz de propiedad de recursos.
- P1: Versionar parámetros aplicables a cada expediente y requisitos; validar cambios administrativos.
- P1: Añadir trazabilidad RF/RN/CA -> OpenAPI -> pruebas; criterios explícitos para cancelación, reactivación, prórrogas y regeneración.
- P1: Definir SOLID, SDD (especificación antes del código), Postman y Definition of Done; IA fuera del MVP según v1.2.
- P1: Revisar privacidad, retención, acceso privilegiado, monitoreo, continuidad y capacidad real del equipo antes del piloto.
- P1: Resolver conflicto C-12 (reescribir V1) vs CONTRIBUTING (no editar migraciones publicadas): solo si se acredita que no fue aplicada a bases persistentes compartidas.

## Estado
No se ejecutaron builds, pruebas, migraciones, despliegues ni escaneos. No introducir datos reales hasta cumplir Go/No-Go. La sincronización de configuración y código debe realizarse por PR independientes con pruebas. Este archivo no sustituye la especificación institucional original.
