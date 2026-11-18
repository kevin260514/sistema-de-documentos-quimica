-- =====================================================================
-- V1: esquema inicial del sistema de pasantias (Facultad de Quimica, UCE)
-- Nunca se edita una migracion ya subida: los cambios van en V2, V3...
-- =====================================================================

-- ---------- Catalogos ----------
CREATE TABLE carrera (
    id     BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL UNIQUE
);

CREATE TABLE periodo_academico (
    id           BIGSERIAL PRIMARY KEY,
    nombre       VARCHAR(60) NOT NULL UNIQUE,
    fecha_inicio DATE        NOT NULL,
    fecha_fin    DATE        NOT NULL,
    activo       BOOLEAN     NOT NULL DEFAULT FALSE,
    CONSTRAINT ck_periodo_fechas CHECK (fecha_fin > fecha_inicio)
);
-- Solo puede haber un periodo activo a la vez (RN7)
CREATE UNIQUE INDEX ux_periodo_un_activo ON periodo_academico (activo) WHERE activo;

CREATE TABLE entidad_receptora (
    id                BIGSERIAL PRIMARY KEY,
    nombre            VARCHAR(200) NOT NULL,
    ruc               VARCHAR(13),
    direccion         VARCHAR(250),
    contacto_nombre   VARCHAR(150),
    contacto_correo   VARCHAR(150),
    contacto_telefono VARCHAR(30)
);

-- ---------- Usuarios ----------
CREATE TABLE usuario (
    id                BIGSERIAL PRIMARY KEY,
    cedula            VARCHAR(10)  NOT NULL UNIQUE,
    nombres           VARCHAR(100) NOT NULL,
    apellidos         VARCHAR(100) NOT NULL,
    correo            VARCHAR(150) NOT NULL,
    password_hash     VARCHAR(100) NOT NULL,
    activo            BOOLEAN      NOT NULL DEFAULT TRUE,
    intentos_fallidos SMALLINT     NOT NULL DEFAULT 0,
    bloqueado_hasta   TIMESTAMPTZ,
    creado_en         TIMESTAMPTZ  NOT NULL DEFAULT now()
);
CREATE UNIQUE INDEX ux_usuario_correo ON usuario (lower(correo));

CREATE TABLE usuario_rol (
    usuario_id BIGINT      NOT NULL REFERENCES usuario (id),
    rol        VARCHAR(20) NOT NULL,
    PRIMARY KEY (usuario_id, rol),
    CONSTRAINT ck_usuario_rol CHECK (rol IN ('ESTUDIANTE', 'SECRETARIO', 'SUBDECANO', 'ADMIN'))
);

CREATE TABLE estudiante (
    usuario_id BIGINT   PRIMARY KEY REFERENCES usuario (id),
    carrera_id BIGINT   NOT NULL REFERENCES carrera (id),
    nivel      SMALLINT NOT NULL CHECK (nivel BETWEEN 1 AND 12),
    codigo     VARCHAR(30) UNIQUE
);

-- ---------- Pasantia ----------
-- Los campos del formulario pueden estar vacios solo mientras es BORRADOR (RN9).
CREATE TABLE pasantia (
    id                BIGSERIAL PRIMARY KEY,
    estudiante_id     BIGINT      NOT NULL REFERENCES estudiante (usuario_id),
    periodo_id        BIGINT      NOT NULL REFERENCES periodo_academico (id),
    entidad_id        BIGINT      REFERENCES entidad_receptora (id),
    tutor_empresarial VARCHAR(150),
    tutor_academico   VARCHAR(150),
    fecha_inicio      DATE,
    fecha_fin         DATE,
    horas             INTEGER,
    estado            VARCHAR(30) NOT NULL DEFAULT 'BORRADOR',
    version           BIGINT      NOT NULL DEFAULT 0,   -- bloqueo optimista (JPA)
    creado_en         TIMESTAMPTZ NOT NULL DEFAULT now(),
    actualizado_en    TIMESTAMPTZ NOT NULL DEFAULT now(),
    CONSTRAINT ck_pasantia_estado CHECK (estado IN (
        'BORRADOR', 'ENVIADA', 'OBSERVADA', 'APROBADA',
        'EVIDENCIAS_ENVIADAS', 'EVIDENCIAS_OBSERVADAS', 'EVIDENCIAS_APROBADAS',
        'AVAL_PENDIENTE_FIRMA', 'FINALIZADA', 'VENCIDA')),
    CONSTRAINT ck_pasantia_fechas CHECK (fecha_fin IS NULL OR fecha_inicio IS NULL OR fecha_fin > fecha_inicio),
    CONSTRAINT ck_pasantia_horas  CHECK (horas IS NULL OR horas > 0),
    CONSTRAINT ck_pasantia_completa CHECK (
        estado = 'BORRADOR' OR (
            entidad_id IS NOT NULL AND tutor_empresarial IS NOT NULL AND
            fecha_inicio IS NOT NULL AND fecha_fin IS NOT NULL AND horas IS NOT NULL))
);
-- Un estudiante tiene una sola pasantia activa a la vez (RN6)
CREATE UNIQUE INDEX ux_pasantia_una_activa
    ON pasantia (estudiante_id) WHERE estado NOT IN ('FINALIZADA', 'VENCIDA');
CREATE INDEX ix_pasantia_estado  ON pasantia (estado);
CREATE INDEX ix_pasantia_periodo ON pasantia (periodo_id);

-- ---------- Documentos y revisiones ----------
CREATE TABLE documento (
    id              BIGSERIAL PRIMARY KEY,
    pasantia_id     BIGINT       NOT NULL REFERENCES pasantia (id),
    etapa           VARCHAR(12)  NOT NULL,
    tipo            VARCHAR(50)  NOT NULL,
    nombre_original VARCHAR(255) NOT NULL,
    ruta            VARCHAR(500) NOT NULL,
    tipo_mime       VARCHAR(100) NOT NULL,
    tamano_bytes    BIGINT       NOT NULL CHECK (tamano_bytes > 0),
    hash_sha256     CHAR(64)     NOT NULL,
    version         INTEGER      NOT NULL DEFAULT 1 CHECK (version >= 1),
    subido_por      BIGINT       NOT NULL REFERENCES usuario (id),
    subido_en       TIMESTAMPTZ  NOT NULL DEFAULT now(),
    CONSTRAINT ck_documento_etapa CHECK (etapa IN ('SOLICITUD', 'EVIDENCIAS')),
    CONSTRAINT ux_documento_version UNIQUE (pasantia_id, tipo, version)
);
CREATE INDEX ix_documento_pasantia ON documento (pasantia_id);

CREATE TABLE revision (
    id          BIGSERIAL PRIMARY KEY,
    pasantia_id BIGINT      NOT NULL REFERENCES pasantia (id),
    etapa       VARCHAR(12) NOT NULL,
    revisor_id  BIGINT      NOT NULL REFERENCES usuario (id),
    decision    VARCHAR(10) NOT NULL,
    comentario  TEXT,
    fecha       TIMESTAMPTZ NOT NULL DEFAULT now(),
    CONSTRAINT ck_revision_etapa    CHECK (etapa IN ('SOLICITUD', 'EVIDENCIAS')),
    CONSTRAINT ck_revision_decision CHECK (decision IN ('APROBADO', 'OBSERVADO')),
    -- Al observar, el comentario es obligatorio (RN12)
    CONSTRAINT ck_revision_comentario CHECK (
        decision = 'APROBADO' OR (comentario IS NOT NULL AND length(btrim(comentario)) > 0))
);
CREATE INDEX ix_revision_pasantia ON revision (pasantia_id);

-- ---------- Aval y firma ----------
CREATE TABLE secuencia_aval (
    anio   SMALLINT PRIMARY KEY,
    ultimo INTEGER  NOT NULL DEFAULT 0
);

CREATE TABLE aval (
    id            BIGSERIAL PRIMARY KEY,
    pasantia_id   BIGINT       NOT NULL REFERENCES pasantia (id),
    anio          SMALLINT     NOT NULL,
    secuencia     INTEGER      NOT NULL,
    numero        VARCHAR(30)  NOT NULL UNIQUE,          -- ej. AVAL-FQ-2026-001
    estado        VARCHAR(20)  NOT NULL DEFAULT 'PENDIENTE_FIRMA',
    ruta_borrador VARCHAR(500) NOT NULL,
    hash_borrador CHAR(64)     NOT NULL,
    ruta_firmado  VARCHAR(500),
    hash_firmado  CHAR(64),
    motivo        TEXT,
    creado_por    BIGINT       NOT NULL REFERENCES usuario (id),
    creado_en     TIMESTAMPTZ  NOT NULL DEFAULT now(),
    CONSTRAINT ck_aval_estado CHECK (estado IN ('PENDIENTE_FIRMA', 'FIRMADO', 'RECHAZADO', 'ANULADO')),
    CONSTRAINT ux_aval_anio_secuencia UNIQUE (anio, secuencia),
    CONSTRAINT ck_aval_firmado CHECK (
        estado <> 'FIRMADO' OR (ruta_firmado IS NOT NULL AND hash_firmado IS NOT NULL)),
    CONSTRAINT ck_aval_motivo CHECK (
        estado NOT IN ('RECHAZADO', 'ANULADO') OR (motivo IS NOT NULL AND length(btrim(motivo)) > 0))
);
-- Una pasantia tiene un solo aval vigente a la vez
CREATE UNIQUE INDEX ux_aval_uno_vigente
    ON aval (pasantia_id) WHERE estado IN ('PENDIENTE_FIRMA', 'FIRMADO');

-- Resultado de verificar el PDF firmado que sube el subdecano (RN26)
CREATE TABLE firma (
    id            BIGSERIAL PRIMARY KEY,
    aval_id       BIGINT       NOT NULL REFERENCES aval (id),
    firmante_id   BIGINT       NOT NULL REFERENCES usuario (id),
    fecha_firma   TIMESTAMPTZ,
    cert_titular  VARCHAR(300),
    cert_serial   VARCHAR(100),
    cert_emisor   VARCHAR(300),
    resultado     VARCHAR(10)  NOT NULL,
    detalle       TEXT,
    verificado_en TIMESTAMPTZ  NOT NULL DEFAULT now(),
    CONSTRAINT ck_firma_resultado CHECK (resultado IN ('VALIDA', 'INVALIDA'))
);
CREATE INDEX ix_firma_aval ON firma (aval_id);

-- ---------- Plantillas, notificaciones y auditoria ----------
CREATE TABLE plantilla_documento (
    id        BIGSERIAL PRIMARY KEY,
    tipo      VARCHAR(50) NOT NULL,
    version   INTEGER     NOT NULL DEFAULT 1,
    contenido TEXT        NOT NULL,
    activa    BOOLEAN     NOT NULL DEFAULT TRUE,
    CONSTRAINT ux_plantilla_version UNIQUE (tipo, version)
);

CREATE TABLE notificacion (
    id          BIGSERIAL PRIMARY KEY,
    usuario_id  BIGINT       NOT NULL REFERENCES usuario (id),
    pasantia_id BIGINT       REFERENCES pasantia (id),
    mensaje     VARCHAR(500) NOT NULL,
    leida       BOOLEAN      NOT NULL DEFAULT FALSE,
    creada_en   TIMESTAMPTZ  NOT NULL DEFAULT now()
);
CREATE INDEX ix_notificacion_usuario ON notificacion (usuario_id, leida);

CREATE TABLE auditoria (
    id         BIGSERIAL PRIMARY KEY,
    usuario_id BIGINT REFERENCES usuario (id),
    accion     VARCHAR(60) NOT NULL,
    entidad    VARCHAR(60),
    entidad_id BIGINT,
    detalle    TEXT,
    ip         VARCHAR(45),
    fecha      TIMESTAMPTZ NOT NULL DEFAULT now()
);
CREATE INDEX ix_auditoria_entidad ON auditoria (entidad, entidad_id);
CREATE INDEX ix_auditoria_fecha   ON auditoria (fecha);

-- ---------- Tablas de solo insercion (RN15, RN30, RN31) ----------
-- Ninguna de estas filas se puede modificar ni borrar, ni siquiera desde el codigo.
CREATE FUNCTION bloquear_modificacion() RETURNS trigger AS $$
BEGIN
    RAISE EXCEPTION 'La tabla % es de solo insercion', TG_TABLE_NAME;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER tg_auditoria_inmutable BEFORE UPDATE OR DELETE ON auditoria
    FOR EACH ROW EXECUTE FUNCTION bloquear_modificacion();
CREATE TRIGGER tg_revision_inmutable BEFORE UPDATE OR DELETE ON revision
    FOR EACH ROW EXECUTE FUNCTION bloquear_modificacion();
CREATE TRIGGER tg_documento_inmutable BEFORE UPDATE OR DELETE ON documento
    FOR EACH ROW EXECUTE FUNCTION bloquear_modificacion();
CREATE TRIGGER tg_firma_inmutable BEFORE UPDATE OR DELETE ON firma
    FOR EACH ROW EXECUTE FUNCTION bloquear_modificacion();
