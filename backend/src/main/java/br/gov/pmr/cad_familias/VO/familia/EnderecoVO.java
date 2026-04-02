package br.gov.pmr.cad_familias.VO.familia;

import java.io.Serializable;

import br.gov.pmr.cad_familias.domain.familia.LocalizacaoDomicilio;

public class EnderecoVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String logradouro;
    private String numero;
    private String bairro;
    private String cidade;
    private String uf;
    private String cep;
    private String pontoReferencia;
    private LocalizacaoDomicilio localizacaoDomicilio;

    public String getLogradouro() { return logradouro; }
    public void setLogradouro(String logradouro) { this.logradouro = logradouro; }

    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }

    public String getBairro() { return bairro; }
    public void setBairro(String bairro) { this.bairro = bairro; }

    public String getCidade() { return cidade; }
    public void setCidade(String cidade) { this.cidade = cidade; }

    public String getUf() { return uf; }
    public void setUf(String uf) { this.uf = uf; }

    public String getCep() { return cep; }
    public void setCep(String cep) { this.cep = cep; }

    public String getPontoReferencia() { return pontoReferencia; }
    public void setPontoReferencia(String pontoReferencia) { this.pontoReferencia = pontoReferencia; }

    public LocalizacaoDomicilio getLocalizacaoDomicilio() { return localizacaoDomicilio; }
    public void setLocalizacaoDomicilio(LocalizacaoDomicilio localizacaoDomicilio) { this.localizacaoDomicilio = localizacaoDomicilio; }
}
