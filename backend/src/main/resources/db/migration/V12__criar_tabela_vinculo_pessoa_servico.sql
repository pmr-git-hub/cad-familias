-- V11__criar_tabela_vinculo_pessoa_servico.sql

CREATE TABLE IF NOT EXISTS vinculo_pessoa_servico (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    pessoa_id BIGINT NOT NULL,
    servico_id BIGINT NOT NULL,
    data_entrada DATE NOT NULL DEFAULT (CURRENT_DATE),
    data_saida DATE,
    status VARCHAR(20) NOT NULL DEFAULT 'ATIVO',
    motivo_saida TEXT,
    criado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    criado_por BIGINT NOT NULL,
    atualizado_em TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    atualizado_por BIGINT,

    -- Chaves estrangeiras
    CONSTRAINT fk_vinculo_pessoa FOREIGN KEY (pessoa_id) REFERENCES pessoa(id) ON DELETE CASCADE,
    CONSTRAINT fk_vinculo_servico FOREIGN KEY (servico_id) REFERENCES servicos(id) ON DELETE CASCADE,

    -- Constraint única: pessoa + serviço + status
    CONSTRAINT uk_pessoa_servico_ativo UNIQUE (pessoa_id, servico_id, status)
);

-- Índices para performance
CREATE INDEX idx_vinculo_pessoa_servico_pessoa ON vinculo_pessoa_servico(pessoa_id);
CREATE INDEX idx_vinculo_pessoa_servico_servico ON vinculo_pessoa_servico(servico_id);
CREATE INDEX idx_vinculo_pessoa_servico_status ON vinculo_pessoa_servico(status);
