-- V14__criar_tabela_programa_social.sql

CREATE TABLE IF NOT EXISTS programa_social (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(300) NOT NULL,
    criterios TEXT,
    orgao_gestor VARCHAR(300),
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    criado_em DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    criado_por BIGINT NOT NULL,
    atualizado_em DATETIME,
    atualizado_por BIGINT
);

CREATE INDEX idx_programa_social_ativo ON programa_social(ativo);
