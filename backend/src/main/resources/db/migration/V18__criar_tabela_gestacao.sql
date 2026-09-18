CREATE TABLE IF NOT EXISTS gestacao (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    vinculo_id BIGINT NOT NULL,
    data_ultima_menstruacao DATE,
    data_prevista_parto DATE,
    status ENUM('EM_ACOMPANHAMENTO','POS_PARTO','ENCERRADO','INTERRUPCAO')
        NOT NULL DEFAULT 'EM_ACOMPANHAMENTO',
    criado_em DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    criado_por BIGINT NOT NULL,
    atualizado_em DATETIME,
    atualizado_por BIGINT,

    CONSTRAINT uq_gestacao_vinculo UNIQUE (vinculo_id),

    CONSTRAINT fk_gestacao_vinculo
        FOREIGN KEY (vinculo_id) REFERENCES vinculo_pessoa_servico(id)
        ON DELETE RESTRICT
);

CREATE INDEX idx_gestacao_status ON gestacao(status);
