package br.gov.pmr.cad_familias.controller.audit;

import br.gov.pmr.cad_familias.domain.audit.AuditLog;
import br.gov.pmr.cad_familias.repository.audit.AuditLogRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/audit")
public class AuditLogController {

    private final AuditLogRepository auditLogRepository;

    public AuditLogController(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @GetMapping("/tabela/{tabela}/registro/{registroId}")
    public ResponseEntity<List<AuditLog>> buscarPorRegistro(
            @PathVariable String tabela,
            @PathVariable Long registroId) {
        return ResponseEntity.ok(
                auditLogRepository.findByTabelaAndRegistroId(tabela, registroId)
        );
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<AuditLog>> buscarPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(auditLogRepository.findByUsuarioId(usuarioId));
    }
}
