# Guia de trabajo en equipo

## Ramas

| Rama | Uso |
|---|---|
| `main` | Version estable, lista para instalar en secretaria. Solo recibe merges desde `dev`. |
| `dev` | Integracion: aqui se junta el trabajo de ambos. |
| `feature/<nombre>` | Una funcionalidad nueva. Sale de `dev` y vuelve a `dev`. |
| `fix/<nombre>` | Correccion de un error. Sale de `dev` y vuelve a `dev`. |
| `hotfix/<nombre>` | Error urgente en produccion. Sale de `main` y vuelve a `main` y `dev`. |

Nadie hace push directo a `main` ni a `dev`: todo entra por Pull Request.

## Flujo diario

```bash
git switch dev
git pull
git switch -c feature/solicitud-pasantia

# ... trabajar y hacer commits ...

git push -u origin feature/solicitud-pasantia
# Abrir Pull Request hacia dev y pedir revision al companero
```

## Mensajes de commit

Formato: `tipo: descripcion corta en minusculas`

| Tipo | Cuando |
|---|---|
| `feat` | Funcionalidad nueva |
| `fix` | Correccion de error |
| `docs` | Documentacion |
| `refactor` | Cambio interno sin cambiar el comportamiento |
| `test` | Pruebas |
| `chore` | Configuracion, dependencias, Docker |

Ejemplos: `feat: agregar formulario de solicitud`, `fix: validar fecha de fin`.

## Pull Requests

- Deben ser pequenos y enfocados en una sola cosa.
- Los revisa la otra persona del equipo antes de unirse (merge).
- Usar "Squash and merge" para mantener el historial limpio.
- Borrar la rama despues de unirla.

## Reglas importantes

- Nunca subir `.env`, claves, ni archivos `.p12`.
- Cada cambio en la base de datos va en una migracion nueva (Flyway). Nunca se edita una migracion ya subida.
- Si cambia el contrato de la API, se actualiza primero en `docs/api` y se avisa al companero.
