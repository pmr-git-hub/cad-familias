package br.gov.pmr.cad_familias.dto.gestacao;

import br.gov.pmr.cad_familias.domain.gestacao.GestacaoAcompanhamento;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record GestacaoAcompanhamentoRespostaDTO(
        Long id,
        Long gestacaoId,
        LocalDate dataRegistro,
        Integer semanasGestacao,
        Integer numeroConsultas,
        Boolean altoRisco,
        String motivoAltoRisco,
        BigDecimal pesoKg,
        String pressaoArterial,
        String observacoes,
        LocalDateTime criadoEm,
        Long criadoPor
) {
    public static GestacaoAcompanhamentoRespostaDTO fromEntity(GestacaoAcompanhamento a) {
        return new GestacaoAcompanhamentoRespostaDTO(
                a.getId(),
                a.getGestacao().getId(),
                a.getDataRegistro(),
                a.getSemanasGestacao(),
                a.getNumeroConsultas(),
                a.getAltoRisco(),
                a.getMotivoAltoRisco(),
                a.getPesoKg(),
                a.getPressaoArterial(),
                a.getObservacoes(),
                a.getCriadoEm(),
                a.getCriadoPor()
        );
    }
}
