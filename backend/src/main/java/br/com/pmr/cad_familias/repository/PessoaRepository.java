package br.com.pmr.cad_familias.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.pmr.cad_familias.domain.Pessoa;

public interface PessoaRepository extends JpaRepository<Pessoa, Long>{}
