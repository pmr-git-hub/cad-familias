package br.gov.pmr.cad_familias.repository.tecnico;

import br.gov.pmr.cad_familias.domain.tecnico.TecnicoEquipamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TecnicoEquipamentoRepository extends JpaRepository<TecnicoEquipamento, Long> {

    List<TecnicoEquipamento> findByTecnicoIdAndAtivoTrue(Long tecnicoId);

    List<TecnicoEquipamento> findByEquipamentoIdAndAtivoTrue(Long equipamentoId);

    boolean existsByTecnicoIdAndEquipamentoIdAndAtivoTrue(Long tecnicoId, Long equipamentoId);
}
