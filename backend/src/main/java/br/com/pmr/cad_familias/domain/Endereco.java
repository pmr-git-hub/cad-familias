package br.com.pmr.cad_familias.domain;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Embeddable
public class Endereco implements Serializable{

	private static final long serialVersionUID = -1164884646668558188L;

	@Column
    private String logradouro;

	@Column(name = "numero_endereco")
    private int numero;

    @Column
    private String bairro;

    @Column 
    private String cidade;

    @Column 
    private String uf;

    @Column 
    private String cep;

    @Column(name = "ponto_referencia")
	private String pontoReferencia;


    @Enumerated(EnumType.STRING)
	@Column(name = "localizacao_dominicio")
	private LocalizacaoDomicilio localizacaoDomicilio;

	public String getLogradouro() {
		return logradouro;
	}

	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}

	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getPontoReferencia() {
		return pontoReferencia;
	}

	public void setPontoReferencia(String pontoReferencia) {
		this.pontoReferencia = pontoReferencia;
	}

	
	public LocalizacaoDomicilio getLocalizacaoDomicilio() {
        return localizacaoDomicilio;
    }

    public void setLocalizacaoDomicilio(LocalizacaoDomicilio localizacaoDomicilio) {
        this.localizacaoDomicilio = localizacaoDomicilio;
    }
}
