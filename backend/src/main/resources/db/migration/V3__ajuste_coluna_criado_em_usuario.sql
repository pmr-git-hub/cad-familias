-- 1. Adiciona a coluna criado_em com valor default para não quebrar registros existentes
ALTER TABLE usuario
    ADD COLUMN criado_em DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6);

-- 2. Adiciona a FK de tecnico_id (se ainda não existir)
ALTER TABLE usuario
    ADD CONSTRAINT FK_usuario_tecnico
    FOREIGN KEY (tecnico_id) REFERENCES tecnico (id);
