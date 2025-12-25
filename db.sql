-- Tablas
CREATE TABLE categorias (
    id                  BIGSERIAL PRIMARY KEY,
    categoria           VARCHAR(255) NOT NULL,
    categoria_id        BIGINT NULL,
    activo              BOOLEAN NOT NULL DEFAULT TRUE,

    aud_ts_ins          TIMESTAMPTZ NOT NULL DEFAULT now(),
    aud_ts_ins_user     VARCHAR(255) NOT NULL,
    aud_ts_upd          TIMESTAMPTZ,
    aud_ts_upd_user     VARCHAR(255),

    CONSTRAINT fk_categoria_padre
        FOREIGN KEY (categoria_id)
        REFERENCES categorias(id)
);

ALTER TABLE categorias
ADD COLUMN icono VARCHAR(50);

UPDATE categorias SET icono = 'shopping-cart' WHERE categoria = 'Alimentación';
UPDATE categorias SET icono = 'shopping-bag' WHERE categoria = 'Moda y accesorios';
UPDATE categorias SET icono = 'film' WHERE categoria = 'Ocio';
UPDATE categorias SET icono = 'map-pin' WHERE categoria = 'Salidas';
UPDATE categorias SET icono = 'truck' WHERE categoria = 'Auto';
UPDATE categorias SET icono = 'briefcase' WHERE categoria = 'Oficina';
UPDATE categorias SET icono = 'home' WHERE categoria = 'Casa';
UPDATE categorias SET icono = 'building-office-2' WHERE categoria = 'Casacuberta';
UPDATE categorias SET icono = 'wrench-screwdriver' WHERE categoria = 'Servicios';
UPDATE categorias SET icono = 'paper-airplane' WHERE categoria = 'Delivery';
UPDATE categorias SET icono = 'globe-americas' WHERE categoria = 'Viajes';

CREATE TABLE gastos (
    id                  BIGSERIAL PRIMARY KEY,
    fecha               DATE NOT NULL,
    categoria_id        BIGINT NOT NULL,
    concepto            VARCHAR(255) NOT NULL,

    importe             NUMERIC(14,2) NOT NULL,
    activo              BOOLEAN NOT NULL DEFAULT TRUE,

    aud_ts_ins          TIMESTAMPTZ NOT NULL DEFAULT now(),
    aud_ts_ins_user     VARCHAR(255) NOT NULL,
    aud_ts_upd          TIMESTAMPTZ,
    aud_ts_upd_user     VARCHAR(255),

    CONSTRAINT fk_gastos_categoria
        FOREIGN KEY (categoria_id)
        REFERENCES categorias(id)
);

CREATE TABLE usuarios (
    id                  BIGSERIAL PRIMARY KEY,

    nombre              VARCHAR(255) NOT NULL,      -- nombre visible
    usuario             VARCHAR(100) NOT NULL,      -- username / login
    imagen_url          VARCHAR(500),               -- foto de perfil

    activo              BOOLEAN NOT NULL DEFAULT TRUE,

    aud_ts_ins          TIMESTAMPTZ NOT NULL DEFAULT now(),
    aud_ts_ins_user     VARCHAR(255) NOT NULL,
    aud_ts_upd          TIMESTAMPTZ,
    aud_ts_upd_user     VARCHAR(255),

    CONSTRAINT uq_usuarios_usuario
        UNIQUE (usuario)
);

-- Indices
CREATE INDEX idx_gastos_fecha ON gastos(fecha);
CREATE INDEX idx_gastos_categoria ON gastos(categoria_id);
CREATE INDEX idx_gastos_activo ON gastos(activo);
CREATE INDEX idx_categorias_activo ON categorias(activo);

CREATE INDEX idx_usuarios_usuario ON usuarios(usuario);
CREATE INDEX idx_usuarios_activo ON usuarios(activo);

-- Restricciones
ALTER TABLE gastos
ADD CONSTRAINT chk_importe_positivo
CHECK (importe >= 0);

-- Funciones
CREATE OR REPLACE FUNCTION set_auditoria_update()
RETURNS TRIGGER AS $$
BEGIN
    NEW.aud_ts_upd = now();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Triggers
CREATE TRIGGER trg_gastos_auditoria
BEFORE UPDATE ON gastos
FOR EACH ROW
EXECUTE FUNCTION set_auditoria_update();

CREATE TRIGGER trg_categorias_auditoria
BEFORE UPDATE ON categorias
FOR EACH ROW
EXECUTE FUNCTION set_auditoria_update();

CREATE TRIGGER trg_usuarios_auditoria
BEFORE UPDATE ON usuarios
FOR EACH ROW
EXECUTE FUNCTION set_auditoria_update();

-- Usuario de ejemplo
INSERT INTO usuarios (
    nombre,
    usuario,
    imagen_url,
    activo,
    aud_ts_ins_user
) VALUES (
    'Mariela Montaldo',
    'mmontaldo',
    '/assets/images/usuarios/mmontaldo.png',
    TRUE,
    'system'
);
