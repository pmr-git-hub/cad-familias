package br.gov.pmr.cad_familias.repository.gestacao;

import br.gov.pmr.cad_familias.domain.gestacao.Gestacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GestacaoRepository extends JpaRepository<Gestacao, Long> {
    Optional<Gestacao> findByVinculoId(Long vinculoId);
    boolean existsByVinculoId(Long vinculoId);
}
