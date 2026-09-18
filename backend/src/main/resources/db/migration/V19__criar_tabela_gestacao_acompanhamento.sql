CREATE TABLE IF NOT EXISTS gestacao_acompanhamento (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    gestacao_id BIGINT NOT NULL,
    data_registro DATE NOT NULL,
    semanas_gestacao INT,
    numero_consultas INT,
    alto_risco BOOLEAN NOT NULL DEFAULT FALSE,
    motivo_alto_risco VARCHAR(500),
    peso_kg DECIMAL(5,2),
    pressao_arterial VARCHAR(20),
    observacoes TEXT,
    criado_em DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    criado_por BIGINT NOT NULL,

    CONSTRAINT fk_acompanhamento_gestacao
        FOREIGN KEY (gestacao_id) REFERENCES gestacao(id)
        ON DELETE RESTRICT,

    CONSTRAINT ck_motivo_alto_risco
        CHECK (alto_risco = FALSE OR motivo_alto_risco IS NOT NULL)
);

CREATE INDEX idx_gestacao_acomp_gestacao ON gestacao_acompanhamento(gestacao_id);
CREATE INDEX idx_gestacao_acomp_data ON gestacao_acompanhamento(gestacao_id, data_registro);
