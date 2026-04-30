package br.gov.pmr.cad_familias.dto.atendimento;

import br.gov.pmr.cad_familias.domain.atendimento.ModalidadeAtendimento;
import br.gov.pmr.cad_familias.domain.atendimento.TipoAtendimento;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record AtendimentoAtualizacaoDTO(

        Long pessoaId,

        Long servicoId,

        Long programaId,

        @NotNull(message = "Data do atendimento é obrigatória")
        LocalDateTime data,

        @NotNull(message = "Tipo de atendimento é obrigatório")
        TipoAtendimento tipo,

        @NotNull(message = "Modalidade é obrigatória")
        ModalidadeAtendimento modalidade,

        @NotBlank(message = "Descrição é obrigatória")
        String descricao
) {}
