package br.gov.pmr.cad_familias.dto.gestacao;

import br.gov.pmr.cad_familias.domain.gestacao.Gestacao;
import br.gov.pmr.cad_familias.domain.gestacao.StatusGestacao;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record GestacaoRespostaDTO(
        Long id,
        Long vinculoId,
        Long pessoaId,
        String pessoaNome,
        LocalDate dataUltimaMenstruacao,
        LocalDate dataPrevistaParto,
        StatusGestacao status,
        LocalDateTime criadoEm,
        Long criadoPor,
        LocalDateTime atualizadoEm,
        Long atualizadoPor
) {
    public static GestacaoRespostaDTO fromEntity(Gestacao g) {
        return new GestacaoRespostaDTO(
                g.getId(),
                g.getVinculo().getId(),
                g.getVinculo().getPessoa().getId(),
                g.getVinculo().getPessoa().getNome(),
                g.getDataUltimaMenstruacao(),
                g.getDataPrevistaParto(),
                g.getStatus(),
                g.getCriadoEm(),
                g.getCriadoPor(),
                g.getAtualizadoEm(),
                g.getAtualizadoPor()
        );
    }
}
