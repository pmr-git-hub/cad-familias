-- V20__add_tipo_servico.sql
ALTER TABLE servicos
    ADD COLUMN tipo VARCHAR(50) NOT NULL DEFAULT 'GERAL';
