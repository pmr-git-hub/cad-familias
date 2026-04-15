package br.gov.pmr.cad_familias.domain.usuario;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Perfil {
	ADMIN("ADMIN", "Administrador"),
	USUARIO("USUARIO", "Usuário");

	private final String codigo;
	private final String descricao;

	Perfil(String codigo, String descricao) {
		this.codigo = codigo;
		this.descricao = descricao;
	}

	@JsonValue
	public String getCodigo() {
		return codigo;
	}

	public String getDescricao() {
		return descricao;
	}

	@JsonCreator
	public static Perfil fromCodigo(String codigo) {
		for (Perfil perfil : Perfil.values()) {
			if (perfil.getCodigo().equalsIgnoreCase(codigo)) {
				return perfil;
			}
		}
		return null;
	}
}
