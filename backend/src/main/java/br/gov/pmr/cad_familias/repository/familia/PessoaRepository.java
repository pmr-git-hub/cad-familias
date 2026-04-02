package br.gov.pmr.cad_familias.repository.familia;

import org.springframework.data.jpa.repository.JpaRepository;

import br.gov.pmr.cad_familias.domain.familia.Pessoa;

public interface PessoaRepository extends JpaRepository<Pessoa, Long>{}
