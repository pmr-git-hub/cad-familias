-- 1. Remove a foreign key que depende do index
-- (substitua 'FK_NOME_AQUI' pelo nome real da FK que apareceu na consulta acima)
-- Se não souber o nome, rode a query acima primeiro.

-- Alternativa: buscar dinamicamente (funciona no MySQL 8+)
SET @fk_name = (
    SELECT CONSTRAINT_NAME
    FROM information_schema.KEY_COLUMN_USAGE
    WHERE TABLE_NAME = 'pessoa'
      AND TABLE_SCHEMA = DATABASE()
      AND COLUMN_NAME = 'familia_id'
      AND REFERENCED_TABLE_NAME = 'familia'
    LIMIT 1
);

SET @sql = CONCAT('ALTER TABLE pessoa DROP FOREIGN KEY ', @fk_name);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 2. Remove o index problemático
DROP INDEX uq_referencia_por_familia ON pessoa;

-- 3. Recria a foreign key de familia_id (sem o unique constraint)
ALTER TABLE pessoa ADD CONSTRAINT fk_pessoa_familia
    FOREIGN KEY (familia_id) REFERENCES familia(id);
