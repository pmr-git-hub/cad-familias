package br.gov.pmr.cad_familias.VO.familia;

import java.io.Serializable;
import java.util.List;

public class FamiliaVO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private PessoaVO pessoaReferencia;
	private List<PessoaVO> membrosDaFamilia;
	private Long rendaFamiliar;

	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }

	public PessoaVO getPessoaReferencia() { return pessoaReferencia; }
	public void setPessoaReferencia(PessoaVO pessoaReferencia) { this.pessoaReferencia = pessoaReferencia; }

	public List<PessoaVO> getMembrosDaFamilia() { return membrosDaFamilia; }
	public void setMembrosDaFamilia(List<PessoaVO> membrosDaFamilia) { this.membrosDaFamilia = membrosDaFamilia; }

	public Long getRendaFamiliar() { return rendaFamiliar; }
	public void setRendaFamiliar(Long rendaFamiliar) { this.rendaFamiliar = rendaFamiliar; }
}
