-- Remove a FK (nome pode variar — verifique com SHOW CREATE TABLE tecnico)
SET @fk_exists = (
    SELECT COUNT(*)
    FROM information_schema.TABLE_CONSTRAINTS
    WHERE CONSTRAINT_SCHEMA = DATABASE()
      AND TABLE_NAME = 'tecnico'
      AND CONSTRAINT_NAME = 'FKfid7akef5nso288teagi3ge11'
      AND CONSTRAINT_TYPE = 'FOREIGN KEY'
);

SET @sql = IF(@fk_exists > 0,
    'ALTER TABLE tecnico DROP FOREIGN KEY FKfid7akef5nso288teagi3ge11',
    'SELECT 1'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Remove a coluna
ALTER TABLE tecnico DROP COLUMN equipamento_id;
