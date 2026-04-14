package br.gov.pmr.cad_familias.dto.encaminhamento;

import br.gov.pmr.cad_familias.domain.encaminhamento.Encaminhamento;
import br.gov.pmr.cad_familias.domain.encaminhamento.StatusEncaminhamento;

import java.time.LocalDateTime;

public record EncaminhamentoRespostaDTO(
        Long id,
        Long familiaId,
        String familiaNomeReferencia,
        Long equipamentoOrigemId,
        String equipamentoOrigemNome,
        Long equipamentoDestinoId,
        String equipamentoDestinoNome,
        Long tecnicoId,
        String tecnicoNome,
        LocalDateTime data,
        String motivo,
        StatusEncaminhamento status,
        LocalDateTime criadoEm
) {
    public static EncaminhamentoRespostaDTO fromEntity(Encaminhamento e) {
        return new EncaminhamentoRespostaDTO(
                e.getId(),
                e.getFamilia().getId(),
                e.getFamilia().getNomeReferencia(),
                e.getEquipamentoOrigem().getId(),
                e.getEquipamentoOrigem().getNome(),
                e.getEquipamentoDestino().getId(),
                e.getEquipamentoDestino().getNome(),
                e.getTecnico().getId(),
                e.getTecnico().getNome(),
                e.getData(),
                e.getMotivo(),
                e.getStatus(),
                e.getCriadoEm()
        );
    }
}
