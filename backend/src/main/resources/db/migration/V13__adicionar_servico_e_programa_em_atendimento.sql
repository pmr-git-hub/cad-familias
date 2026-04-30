-- V12__adicionar_servico_e_programa_em_atendimento.sql

-- Adiciona coluna servico_id
ALTER TABLE atendimento
ADD COLUMN servico_id BIGINT NULL,
ADD CONSTRAINT fk_atendimento_servico
    FOREIGN KEY (servico_id) REFERENCES servicos(id) ON DELETE SET NULL;

-- Adiciona coluna programa_id
ALTER TABLE atendimento
ADD COLUMN programa_id BIGINT NULL,
ADD CONSTRAINT fk_atendimento_programa
    FOREIGN KEY (programa_id) REFERENCES programa_social(id) ON DELETE SET NULL;

-- Índices para performance
CREATE INDEX idx_atendimento_servico ON atendimento(servico_id);
CREATE INDEX idx_atendimento_programa ON atendimento(programa_id);
