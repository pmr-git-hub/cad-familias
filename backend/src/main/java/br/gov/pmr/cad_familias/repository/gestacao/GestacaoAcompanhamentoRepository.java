// repository/gestacao/GestacaoAcompanhamentoRepository.java
package br.gov.pmr.cad_familias.repository.gestacao;

import br.gov.pmr.cad_familias.domain.gestacao.GestacaoAcompanhamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GestacaoAcompanhamentoRepository extends JpaRepository<GestacaoAcompanhamento, Long> {
    List<GestacaoAcompanhamento> findByGestacaoIdOrderByDataRegistroDesc(Long gestacaoId);
    Optional<GestacaoAcompanhamento> findByIdAndGestacaoId(Long id, Long gestacaoId);

}
