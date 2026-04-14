package br.gov.pmr.cad_familias.repository.atendimento;

import br.gov.pmr.cad_familias.domain.atendimento.Prontuario;
import br.gov.pmr.cad_familias.domain.atendimento.StatusProntuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProntuarioRepository extends JpaRepository<Prontuario, Long> {

    List<Prontuario> findByFamiliaId(Long familiaId);

    List<Prontuario> findByEquipamentoId(Long equipamentoId);

    List<Prontuario> findByTecnicoId(Long tecnicoId);

    List<Prontuario> findByFamiliaIdAndStatus(Long familiaId, StatusProntuario status);

    boolean existsByFamiliaIdAndEquipamentoIdAndStatus(Long familiaId, Long equipamentoId, StatusProntuario status);
}
