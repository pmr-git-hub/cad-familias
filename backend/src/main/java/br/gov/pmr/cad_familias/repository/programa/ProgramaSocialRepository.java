package br.gov.pmr.cad_familias.repository.programa;

import br.gov.pmr.cad_familias.domain.programa.ProgramaSocial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProgramaSocialRepository extends JpaRepository<ProgramaSocial, Long> {

    List<ProgramaSocial> findByAtivoTrue();

    List<ProgramaSocial> findByNomeContainingIgnoreCase(String nome);

    boolean existsByNomeIgnoreCase(String nome);

    boolean existsByNomeIgnoreCaseAndIdNot(String nome, Long id);
}
