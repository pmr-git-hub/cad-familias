package br.gov.pmr.cad_familias.dto.atendimento;

import br.gov.pmr.cad_familias.domain.atendimento.Prontuario;
import br.gov.pmr.cad_familias.domain.atendimento.StatusProntuario;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ProntuarioRespostaDTO(
        Long id,
        Long familiaId,
        String familiaNomeReferencia,
        Long equipamentoId,
        String equipamentoNome,
        Long tecnicoId,
        String tecnicoNome,
        LocalDate dataAbertura,
        LocalDate dataFechamento,
        StatusProntuario status,
        LocalDateTime criadoEm,
        String motivoEncerramento
) {
    public static ProntuarioRespostaDTO fromEntity(Prontuario p) {
        return new ProntuarioRespostaDTO(
                p.getId(),
                p.getFamilia().getId(),
                p.getFamilia().getNomeReferencia(),
                p.getEquipamento().getId(),
                p.getEquipamento().getNome(),
                p.getTecnico().getId(),
                p.getTecnico().getNome(),
                p.getDataAbertura(),
                p.getDataFechamento(),
                p.getStatus(),
                p.getCriadoEm(),
                p.getMotivoEncerramento()
        );
    }
}
