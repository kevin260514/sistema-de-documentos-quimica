# 0001. El aval se firma fuera del sistema

- Estado: aceptada
- Fecha: 2026-11-17

## Contexto
El subdecano debe firmar electrónicamente cada aval con su certificado `.p12`.
Hacerlo dentro del sistema obligaría a recibir el `.p12` y su clave en el servidor.

## Decisión
El subdecano descarga el aval en PDF, lo firma con una herramienta externa
(FirmaEC, Adobe Acrobat u otra) y sube el PDF ya firmado. El sistema **no recibe
ni guarda nunca el `.p12` ni su clave**.

## Qué verifica el sistema al recibir el PDF firmado (RN26)
1. Contiene una firma digital íntegra (nadie modificó el PDF tras firmarlo).
2. El certificado está vigente y pertenece al subdecano (cédula o nombre registrados).
3. Es el aval generado: los primeros bytes coinciden con el borrador guardado
   (`aval.hash_borrador`), porque una firma estándar se añade al final del archivo.

Si algo falla, se rechaza con un motivo claro y el aval sigue pendiente de firma.

## Consecuencias
- Más seguro y más simple de programar (se verifica, no se firma).
- El subdecano da un paso manual más.
- Alcance de la Fase 1: integridad, titular y vigencia. La validación de la cadena de
  confianza con las entidades certificadoras del Ecuador queda a cargo de FirmaEC.
- Probar pronto con el PDF firmado real del subdecano: algunos programas reescriben
  el archivo completo en lugar de añadir la firma al final.
