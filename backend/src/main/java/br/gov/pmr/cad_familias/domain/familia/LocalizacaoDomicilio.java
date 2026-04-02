package br.gov.pmr.cad_familias.domain.familia;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum LocalizacaoDomicilio {

	ZONA_URBANA("zonaUrbana", "Zona Urbana"),
	ZONA_RURAL("zonaRural", "Zona Rural"),
	ABRIGO("abrigo", "Abrigo");
			
	private final String descricao;
	
	private final String codigo;
	
	private LocalizacaoDomicilio(String codigo, String descricao) {
		this.descricao = descricao;
		this.codigo = codigo;
	}
	
	public String getDescricao() {
		return descricao;
	}
	
	@JsonValue
	public String getCodigo() {
		return codigo;
	}
	@JsonCreator
	public static LocalizacaoDomicilio fromCodigo(String codigo) {
		for (LocalizacaoDomicilio localizacao : LocalizacaoDomicilio.values()) {
			if(localizacao.getCodigo().equalsIgnoreCase(codigo)) {
				return localizacao;
			}
		}

		return null;
	}
}
