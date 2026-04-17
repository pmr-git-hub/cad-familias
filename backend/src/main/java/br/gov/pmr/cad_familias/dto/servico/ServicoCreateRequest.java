package br.gov.pmr.cad_familias.dto.servico;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServicoCreateRequest {

    @NotNull(message = "ID do equipamento é obrigatório")
    private Long equipamentoId;

    @NotBlank(message = "Nome do serviço é obrigatório")
    @Size(max = 300, message = "Nome deve ter no máximo 300 caracteres")
    private String nome;

    private String descricao;

    @Size(max = 300, message = "Público-alvo deve ter no máximo 300 caracteres")
    private String publicoAlvo;

    @Min(value = 0, message = "Faixa etária mínima não pode ser negativa")
    private Integer faixaEtariaMin;

    @Min(value = 0, message = "Faixa etária máxima não pode ser negativa")
    private Integer faixaEtariaMax;

    @Size(max = 100, message = "Dia da semana deve ter no máximo 100 caracteres")
    private String diaSemana;

    @Size(max = 100, message = "Horário deve ter no máximo 100 caracteres")
    private String horario;

    private Boolean ativo;
}
