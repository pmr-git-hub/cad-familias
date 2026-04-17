-- V10__insert_table_servico.sql

CREATE TABLE servicos (
    id              BIGINT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    equipamento_id  BIGINT          NOT NULL,
    nome            VARCHAR(300)    NOT NULL,
    descricao       TEXT,
    publico_alvo    VARCHAR(300),
    faixa_etaria_min INT,
    faixa_etaria_max INT,
    dia_semana      VARCHAR(100),
    horario         VARCHAR(100),
    ativo           BOOLEAN         NOT NULL DEFAULT true,
    criado_em       TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    criado_por      BIGINT          NOT NULL,
    atualizado_em   TIMESTAMP       NULL,
    atualizado_por  BIGINT,

    CONSTRAINT fk_servico_equipamento FOREIGN KEY (equipamento_id) REFERENCES equipamento(id),
    CONSTRAINT uk_servico_nome_equipamento UNIQUE (nome, equipamento_id),
    CONSTRAINT ck_servico_faixa_etaria CHECK (
        faixa_etaria_min IS NULL
        OR faixa_etaria_max IS NULL
        OR faixa_etaria_min <= faixa_etaria_max
    )
);

CREATE INDEX idx_servicos_equipamento_id ON servicos(equipamento_id);
CREATE INDEX idx_servicos_ativo ON servicos(ativo);
CREATE INDEX idx_servicos_nome ON servicos(nome);
