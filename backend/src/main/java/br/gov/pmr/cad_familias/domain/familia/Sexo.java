package br.gov.pmr.cad_familias.domain.familia;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Sexo {
	
	MASCULINO("masculino", "Masculinho"),
	FEMININO("feminino", "Feminino");
	
	private String codigo;
	
	private String descricao;
	
	private Sexo(String codigo, String descricao) {
		this.codigo = codigo;
		this.descricao = descricao;
	}
	@JsonValue
	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	
	@JsonCreator
	public static Sexo fromCodigo(String codigo) {
		for (Sexo sexo : Sexo.values()) {
			if(sexo.getCodigo().equalsIgnoreCase(codigo)) {
				return sexo;
			}
		}
		return null;
	}
	

}
