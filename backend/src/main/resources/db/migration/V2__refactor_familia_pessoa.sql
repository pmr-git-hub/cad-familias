-- ================================
-- DROP das tabelas existentes
-- ================================
SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS `pessoa`;
DROP TABLE IF EXISTS `familia`;

SET FOREIGN_KEY_CHECKS = 1;

-- ================================
-- FAMILIA
-- ================================
CREATE TABLE `familia` (
    `id`              CHAR(36)     NOT NULL DEFAULT (UUID()),
    `codigo_cadunico` VARCHAR(30)  NULL,
    `situacao`        ENUM('ATIVA', 'INATIVA', 'SUSPENSA') NOT NULL DEFAULT 'ATIVA',
    `criado_em`       DATETIME     NOT NULL,
    `criado_por`      CHAR(36)     NOT NULL,
    `atualizado_em`   DATETIME     NULL,
    `atualizado_por`  CHAR(36)     NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ================================
-- PESSOA
-- ================================
CREATE TABLE `pessoa` (
    `id`                    CHAR(36)      NOT NULL DEFAULT (UUID()),
    `familia_id`            CHAR(36)      NOT NULL,
    `nome`                  VARCHAR(300)  NOT NULL,
    `cpf`                   VARCHAR(14)   NULL,
    `nis`                   VARCHAR(20)   NULL,
    `rg_numero`             VARCHAR(30)   NULL,
    `rg_orgao_expeditor`    VARCHAR(50)   NULL,
    `rg_data_expedicao`     DATE          NULL,
    `data_nascimento`       DATE          NOT NULL,
    `is_referencia`         TINYINT(1)    NOT NULL DEFAULT 0,
    `parentesco`            ENUM('CONJUGE', 'FILHO', 'PAI_MAE', 'OUTRO') NULL,
    `telefone`              VARCHAR(20)   NULL,
    `sexo`                  ENUM('MASCULINO', 'FEMININO') NULL,
    `renda_mensal`          BIGINT        NULL,
    -- Endereco
    `logradouro`            VARCHAR(255)  NULL,
    `numero_endereco`       VARCHAR(10)   NULL,
    `bairro`                VARCHAR(100)  NULL,
    `cidade`                VARCHAR(100)  NULL,
    `uf`                    VARCHAR(2)    NULL,
    `cep`                   VARCHAR(9)    NULL,
    `ponto_referencia`      VARCHAR(255)  NULL,
    `localizacao_domicilio` ENUM('ZONA_URBANA', 'ZONA_RURAL', 'ABRIGO') NULL,
    -- Auditoria
    `criado_em`             DATETIME      NOT NULL,
    `criado_por`            CHAR(36)      NOT NULL,
    `atualizado_em`         DATETIME      NULL,
    `atualizado_por`        CHAR(36)      NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uq_pessoa_cpf` (`cpf`),
    -- Garante apenas uma pessoa de referência por família
    UNIQUE KEY `uq_referencia_por_familia` (`familia_id`, `is_referencia`),
    CONSTRAINT `fk_pessoa_familia`
        FOREIGN KEY (`familia_id`) REFERENCES `familia` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
