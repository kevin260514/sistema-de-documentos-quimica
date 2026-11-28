# Implementación inicial del workflow

Estado: matriz estructural implementada; integración pendiente.

- T01–T14, T16, T17 y T19: matriz estructural de estados y roles.
- T15/T18 y A05: denegadas hasta implementar casos de uso excepcionales.
- A01–A04: matriz estructural. A01 usa origen nulo exclusivamente para creación.
- RolOperativo debe derivarse de identidad autenticada y suplencia vigente, jamás de un campo enviado por cliente.
- Ninguna matriz autoriza por sí sola operaciones HTTP ni cambia datos. Antes de integrarla: propiedad RN3, elegibilidad RN5, RN6, plazos RN14/RN17, horas RN21, aval vigente RN22, doble control RN27, suplencias RN28, auditoría RN30 y notificaciones RN31.
- T13/A02, T14/A03, T19/A04 y T15/A05 deben ejecutarse atómicamente, con control de concurrencia y persistencia.
- Pruebas JUnit exhaustivas para la matriz estructural; ejecutar Maven/CI antes de merge. No constituyen cobertura completa de CA-14.
- Pendiente: inventario de migraciones existentes, esquema Flyway, guardas, servicios de aplicación, autorización y pruebas de integración.
