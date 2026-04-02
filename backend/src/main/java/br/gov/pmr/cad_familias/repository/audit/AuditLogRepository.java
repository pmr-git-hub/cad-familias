package br.gov.pmr.cad_familias.repository.audit;

import br.gov.pmr.cad_familias.domain.audit.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    List<AuditLog> findByTabelaAndRegistroId(String tabela, Long registroId);

    List<AuditLog> findByUsuarioId(Long usuarioId);
}
