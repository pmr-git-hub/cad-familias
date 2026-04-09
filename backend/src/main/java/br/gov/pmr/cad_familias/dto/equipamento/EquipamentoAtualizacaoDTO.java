package br.gov.pmr.cad_familias.dto.equipamento;

import br.gov.pmr.cad_familias.domain.equipamento.TipoEquipamento;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EquipamentoAtualizacaoDTO {
    private String nome;
    private TipoEquipamento tipo;
    private String cep;
    private String logradouro;
    private String numero;
    private String complemento;
    private String bairro;
    private String cidade;
    private String estado;
    private String telefone;
    private String email;
    private Boolean ativo;

    public Boolean isAtivo() {
        return ativo;
    }
}
