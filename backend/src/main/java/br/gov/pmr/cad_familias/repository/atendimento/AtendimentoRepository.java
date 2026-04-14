package br.gov.pmr.cad_familias.repository.atendimento;

import br.gov.pmr.cad_familias.domain.atendimento.Atendimento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AtendimentoRepository extends JpaRepository<Atendimento, Long> {

    List<Atendimento> findByProntuarioIdOrderByDataDesc(Long prontuarioId);

    List<Atendimento> findByTecnicoIdOrderByDataDesc(Long tecnicoId);

    List<Atendimento> findByPessoaIdOrderByDataDesc(Long pessoaId);
}
