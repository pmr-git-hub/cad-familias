// TecnicoRepository.java
package br.gov.pmr.cad_familias.repository.tecnico;

import br.gov.pmr.cad_familias.domain.tecnico.Tecnico;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TecnicoRepository extends JpaRepository<Tecnico, Long> {
    boolean existsByCpf(String cpf);
}
