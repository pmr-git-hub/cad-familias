package br.gov.pmr.cad_familias.dto.atendimento;

import br.gov.pmr.cad_familias.domain.atendimento.Atendimento;
import br.gov.pmr.cad_familias.domain.atendimento.ModalidadeAtendimento;
import br.gov.pmr.cad_familias.domain.atendimento.TipoAtendimento;

import java.time.LocalDateTime;

public record AtendimentoRespostaDTO(
        Long id,
        Long prontuarioId,
        Long tecnicoId,
        String tecnicoNome,
        Long pessoaId,
        String pessoaNome,
        LocalDateTime data,
        TipoAtendimento tipo,
        ModalidadeAtendimento modalidade,
        String descricao,
        LocalDateTime criadoEm
) {
    public static AtendimentoRespostaDTO fromEntity(Atendimento a) {
        return new AtendimentoRespostaDTO(
                a.getId(),
                a.getProntuario().getId(),
                a.getTecnico().getId(),
                a.getTecnico().getNome(),
                a.getPessoa() != null ? a.getPessoa().getId() : null,
                a.getPessoa() != null ? a.getPessoa().getNome() : null,
                a.getData(),
                a.getTipo(),
                a.getModalidade(),
                a.getDescricao(),
                a.getCriadoEm()
        );
    }
}
