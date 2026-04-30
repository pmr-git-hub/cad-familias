package br.gov.pmr.cad_familias.repository.servico;

import br.gov.pmr.cad_familias.domain.servico.Servico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServicoRepository extends JpaRepository<Servico, Long> {

    List<Servico> findByAtivoTrue();

    List<Servico> findByEquipamentoId(Long equipamentoId);

    List<Servico> findByEquipamentoIdAndAtivoTrue(Long equipamentoId);

    List<Servico> findByNomeContainingIgnoreCase(String nome);

    boolean existsByNomeIgnoreCaseAndEquipamentoId(String nome, Long equipamentoId);

    boolean existsByNomeIgnoreCaseAndEquipamentoIdAndIdNot(String nome, Long equipamentoId, Long id);
}
