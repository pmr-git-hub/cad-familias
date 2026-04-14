package br.gov.pmr.cad_familias.repository.encaminhamento;

import br.gov.pmr.cad_familias.domain.encaminhamento.Encaminhamento;
import br.gov.pmr.cad_familias.domain.encaminhamento.StatusEncaminhamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EncaminhamentoRepository extends JpaRepository<Encaminhamento, Long> {

    List<Encaminhamento> findByFamiliaIdOrderByDataDesc(Long familiaId);

    List<Encaminhamento> findByEquipamentoDestinoIdAndStatusOrderByDataDesc(Long equipamentoDestinoId, StatusEncaminhamento status);

    List<Encaminhamento> findByEquipamentoOrigemIdOrderByDataDesc(Long equipamentoOrigemId);

    List<Encaminhamento> findByTecnicoIdOrderByDataDesc(Long tecnicoId);
}
