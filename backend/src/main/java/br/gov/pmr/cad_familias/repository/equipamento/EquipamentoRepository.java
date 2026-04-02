package br.gov.pmr.cad_familias.repository.equipamento;

import br.gov.pmr.cad_familias.domain.equipamento.Equipamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EquipamentoRepository extends JpaRepository<Equipamento, Long> {
}
