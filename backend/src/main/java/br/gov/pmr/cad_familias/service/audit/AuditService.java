package br.gov.pmr.cad_familias.service.audit;

import br.gov.pmr.cad_familias.domain.audit.AcaoAudit;
import br.gov.pmr.cad_familias.domain.audit.AuditLog;
import br.gov.pmr.cad_familias.domain.usuario.Usuario;
import br.gov.pmr.cad_familias.repository.audit.AuditLogRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

@Service
public class AuditService {

    private final AuditLogRepository auditLogRepository;
    private final ObjectMapper objectMapper;

    public AuditService(AuditLogRepository auditLogRepository, ObjectMapper objectMapper) {
        this.auditLogRepository = auditLogRepository;
        this.objectMapper = objectMapper;
    }

    public void registrar(String tabela, Long registroId, AcaoAudit acao,
                          Object dadosAntes, Object dadosDepois, Usuario usuario) {
        AuditLog log = new AuditLog();
        log.setTabela(tabela);
        log.setRegistroId(registroId);
        log.setAcao(acao);
        log.setUsuario(usuario);
        log.setDadosAntes(toJson(dadosAntes));
        log.setDadosDepois(toJson(dadosDepois));

        auditLogRepository.save(log);
    }

    private String toJson(Object obj) {
        if (obj == null) return null;
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            return "{\"erro\": \"falha ao serializar\"}";
        }
    }
}
