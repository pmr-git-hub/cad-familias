package br.gov.pmr.cad_familias.repository.servico;

import br.gov.pmr.cad_familias.domain.programa.StatusVinculo;
import br.gov.pmr.cad_familias.domain.servico.VinculoPessoaServico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VinculoPessoaServicoRepository extends JpaRepository<VinculoPessoaServico, Long> {

    List<VinculoPessoaServico> findByPessoaId(Long pessoaId);

    List<VinculoPessoaServico> findByServicoId(Long servicoId);

    List<VinculoPessoaServico> findByPessoaIdAndStatus(Long pessoaId, StatusVinculo status);

    List<VinculoPessoaServico> findByServicoIdAndStatus(Long servicoId, StatusVinculo status);

    Optional<VinculoPessoaServico> findByPessoaIdAndServicoId(Long pessoaId, Long servicoId);

    boolean existsByPessoaIdAndServicoIdAndStatus(Long pessoaId, Long servicoId, StatusVinculo status);
}
