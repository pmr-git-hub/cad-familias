package br.com.pmr.cad_familias.domain;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "familia")
public class Familia implements Serializable{
	
	private static final long serialVersionUID = 7660693721478356023L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "familia", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<Pessoa> membrosDaFamilia;
    
    
    @Column(name = "renda_familia")
    private Long rendaFamiliar;
    
    

	public Long getRendaFamiliar() {
		return rendaFamiliar;
	}

	public void setRendaFamiliar(Long rendaFamiliar) {
		this.rendaFamiliar = rendaFamiliar;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<Pessoa> getMembrosDaFamilia() {
		return membrosDaFamilia;
	}

	public void setMembrosDaFamilia(List<Pessoa> membrosDaFamilia) {
		this.membrosDaFamilia = membrosDaFamilia;
	}


}
