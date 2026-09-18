-- V15__criar_tabela_vinculo_familia_programa.sql

CREATE TABLE IF NOT EXISTS vinculo_familia_programa (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    familia_id BIGINT NOT NULL,
    programa_id BIGINT NOT NULL,
    data_entrada DATE NOT NULL,
    data_saida DATE,
    status ENUM('ATIVO','SUSPENSO','CANCELADO') NOT NULL DEFAULT 'ATIVO',
    motivo_saida TEXT,
    criado_em DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    criado_por BIGINT NOT NULL,
    atualizado_em DATETIME,
    atualizado_por BIGINT,

    CONSTRAINT uk_familia_programa UNIQUE (familia_id, programa_id),

    CONSTRAINT fk_vfp_familia
        FOREIGN KEY (familia_id) REFERENCES familia(id)
        ON DELETE RESTRICT,

    CONSTRAINT fk_vfp_programa
        FOREIGN KEY (programa_id) REFERENCES programa_social(id)
        ON DELETE RESTRICT
);

CREATE INDEX idx_vfp_familia ON vinculo_familia_programa(familia_id);
CREATE INDEX idx_vfp_programa ON vinculo_familia_programa(programa_id);
CREATE INDEX idx_vfp_status ON vinculo_familia_programa(status);
