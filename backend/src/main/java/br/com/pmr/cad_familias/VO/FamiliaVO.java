package br.com.pmr.cad_familias.VO;

import java.io.Serializable;
import java.util.List;

import br.com.pmr.cad_familias.domain.Pessoa;

public class FamiliaVO implements Serializable{

	private static final long serialVersionUID = -3449542886973377881L;
	
	private Long id;

	private Pessoa pessoaReferencia;
	
	private List<Pessoa> membrosDaFamilia;
	
	private Long rendaFamiliar;
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getRendaFamiliar() {
		return rendaFamiliar;
	}

	public void setRendaFamiliar(Long rendaFamiliar) {
		this.rendaFamiliar = rendaFamiliar;
	}

	public Pessoa getPessoaReferencia() {
		return pessoaReferencia;
	}

	public void setPessoaReferencia(Pessoa pessoaReferencia) {
		this.pessoaReferencia = pessoaReferencia;
	}

	public List<Pessoa> getMembrosDaFamilia() {
		return membrosDaFamilia;
	}

	public void setMembrosDaFamilia(List<Pessoa> membrosDaFamilia) {
		this.membrosDaFamilia = membrosDaFamilia;
	}
	
	
	
	
}
