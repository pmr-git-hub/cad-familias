package br.gov.pmr.cad_familias.dto.familia;

import java.io.Serializable;
import java.time.LocalDate;

import br.gov.pmr.cad_familias.domain.familia.Parentesco;
import br.gov.pmr.cad_familias.domain.familia.Sexo;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PessoaDTO implements Serializable {


    private Long id;
    private String nome;
    private String cpf;
    private String telefone;
    private Sexo sexo;
    private Parentesco parentesco;
    private Long rendaMensal;
    private LocalDate dataNascimento;
    private String numeroRg;
    private String orgaoExpeditorRg;
    private LocalDate dataExpedicaoRg;
    private boolean referencia;
    private EnderecoDTO endereco;

}
