package br.gov.pmr.cad_familias.repository.programa;

import br.gov.pmr.cad_familias.domain.programa.StatusVinculo;
import br.gov.pmr.cad_familias.domain.programa.VinculoFamiliaPrograma;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VinculoFamiliaProgramaRepository extends JpaRepository<VinculoFamiliaPrograma, Long> {

    List<VinculoFamiliaPrograma> findByFamiliaId(Long familiaId);

    List<VinculoFamiliaPrograma> findByProgramaId(Long programaId);

    List<VinculoFamiliaPrograma> findByFamiliaIdAndStatus(Long familiaId, StatusVinculo status);

    List<VinculoFamiliaPrograma> findByProgramaIdAndStatus(Long programaId, StatusVinculo status);

    Optional<VinculoFamiliaPrograma> findByFamiliaIdAndProgramaId(Long familiaId, Long programaId);

    boolean existsByFamiliaIdAndProgramaIdAndStatus(Long familiaId, Long programaId, StatusVinculo status);
}
