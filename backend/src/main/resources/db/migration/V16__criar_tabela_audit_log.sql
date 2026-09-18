-- V16__criar_tabela_audit_log.sql

CREATE TABLE IF NOT EXISTS audit_log (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    tabela VARCHAR(100) NOT NULL,
    registro_id BIGINT NOT NULL,
    acao ENUM('INSERT','UPDATE','DELETE') NOT NULL,
    dados_antes JSON,
    dados_depois JSON,
    usuario_id BIGINT NOT NULL,
    feito_em DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_audit_usuario
        FOREIGN KEY (usuario_id) REFERENCES usuario(id)
        ON DELETE RESTRICT
);

CREATE INDEX idx_audit_tabela ON audit_log(tabela);
CREATE INDEX idx_audit_registro ON audit_log(tabela, registro_id);
CREATE INDEX idx_audit_usuario ON audit_log(usuario_id);
CREATE INDEX idx_audit_feito_em ON audit_log(feito_em);
