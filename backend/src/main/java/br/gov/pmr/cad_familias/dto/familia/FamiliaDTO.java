package br.gov.pmr.cad_familias.dto.familia;

import br.gov.pmr.cad_familias.domain.familia.SituacaoFamilia;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class FamiliaDTO implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	private Long id;
	private PessoaDTO pessoaReferencia;
	private List<PessoaDTO> membrosDaFamilia;
	private Long rendaFamiliar;
	private String codigoCadunico;
	private SituacaoFamilia situacao;


}
