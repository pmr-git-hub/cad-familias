package br.com.pmr.cad_familias.domain;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Perfil {
	ADMIN("ADMIN", "Administrador"), USER("USUARIO", "Usuário");
	
	public String codigo;
	
	public String descricao;

	private Perfil(String codigo, String descricao) {
		this.codigo = codigo;
		this.descricao = descricao;
	}

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
	public static Perfil fromCodigo(String codigo) {
		for (Perfil perfil : Perfil.values()) {
			if(perfil.getCodigo().equalsIgnoreCase(codigo)) {
				return perfil;
			}
		}
		return null;
	}
	
}
