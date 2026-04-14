CREATE TABLE tecnico_equipamento (
    id BIGINT NOT NULL AUTO_INCREMENT,
    tecnico_id BIGINT NOT NULL,
    equipamento_id BIGINT NOT NULL,
    data_inicio DATE NOT NULL,
    data_fim DATE NULL,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    criado_em DATETIME(6) NOT NULL,
    criado_por BIGINT NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_te_tecnico FOREIGN KEY (tecnico_id) REFERENCES tecnico(id),
    CONSTRAINT fk_te_equipamento FOREIGN KEY (equipamento_id) REFERENCES equipamento(id),
    CONSTRAINT uk_tecnico_equipamento_ativo UNIQUE (tecnico_id, equipamento_id, ativo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
