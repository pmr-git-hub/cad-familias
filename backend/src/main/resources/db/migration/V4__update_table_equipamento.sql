-- V5__update_table_equipamento.sql
ALTER TABLE equipamento
ADD COLUMN cep VARCHAR(9),
ADD COLUMN logradouro VARCHAR(255),
ADD COLUMN numero VARCHAR(20),
ADD COLUMN complemento VARCHAR(255),
ADD COLUMN bairro VARCHAR(255),
ADD COLUMN cidade VARCHAR(255),
ADD COLUMN estado VARCHAR(2),
ADD COLUMN telefone VARCHAR(20),
ADD COLUMN email VARCHAR(255);

-- Remover o campo endereco (opcional, se não for mais necessário)
ALTER TABLE equipamento DROP COLUMN endereco;
