package br.gov.pmr.cad_familias.dto.familia;

import java.io.Serializable;

import br.gov.pmr.cad_familias.domain.familia.LocalizacaoDomicilio;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EnderecoDTO implements Serializable {

    private String logradouro;
    private String numero;
    private String bairro;
    private String cidade;
    private String uf;
    private String cep;
    private String pontoReferencia;
    private LocalizacaoDomicilio localizacaoDomicilio;
}
