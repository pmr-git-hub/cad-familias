package br.gov.pmr.cad_familias.service.atendimento;

import br.gov.pmr.cad_familias.domain.atendimento.Atendimento;
import br.gov.pmr.cad_familias.domain.atendimento.Prontuario;
import br.gov.pmr.cad_familias.domain.atendimento.StatusProntuario;
import br.gov.pmr.cad_familias.domain.familia.Pessoa;
import br.gov.pmr.cad_familias.domain.tecnico.Tecnico;
import br.gov.pmr.cad_familias.dto.atendimento.AtendimentoAtualizacaoDTO;
import br.gov.pmr.cad_familias.dto.atendimento.AtendimentoCadastroDTO;
import br.gov.pmr.cad_familias.dto.atendimento.AtendimentoRespostaDTO;
import br.gov.pmr.cad_familias.repository.atendimento.AtendimentoRepository;
import br.gov.pmr.cad_familias.repository.atendimento.ProntuarioRepository;
import br.gov.pmr.cad_familias.repository.familia.PessoaRepository;
import br.gov.pmr.cad_familias.repository.tecnico.TecnicoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AtendimentoService {

    private final AtendimentoRepository atendimentoRepository;
    private final ProntuarioRepository prontuarioRepository;
    private final TecnicoRepository tecnicoRepository;
    private final PessoaRepository pessoaRepository;

    public AtendimentoService(AtendimentoRepository atendimentoRepository,
                              ProntuarioRepository prontuarioRepository,
                              TecnicoRepository tecnicoRepository,
                              PessoaRepository pessoaRepository) {
        this.atendimentoRepository = atendimentoRepository;
        this.prontuarioRepository = prontuarioRepository;
        this.tecnicoRepository = tecnicoRepository;
        this.pessoaRepository = pessoaRepository;
    }

    @Transactional
    public AtendimentoRespostaDTO cadastrar(AtendimentoCadastroDTO dto, Long usuarioId) {

        Prontuario prontuario = prontuarioRepository.findById(dto.prontuarioId())
                .orElseThrow(() -> new EntityNotFoundException("Prontuário não encontrado."));

        if (prontuario.getStatus() != StatusProntuario.ABERTO) {
            throw new IllegalStateException("Não é possível registrar atendimento em prontuário que não está aberto.");
        }

        Tecnico tecnico = tecnicoRepository.findById(dto.tecnicoId())
                .orElseThrow(() -> new EntityNotFoundException("Técnico não encontrado."));

        Pessoa pessoa = null;
        if (dto.pessoaId() != null) {
            pessoa = pessoaRepository.findById(dto.pessoaId())
                    .orElseThrow(() -> new EntityNotFoundException("Pessoa não encontrada."));
        }

        Atendimento atendimento = new Atendimento();
        atendimento.setProntuario(prontuario);
        atendimento.setTecnico(tecnico);
        atendimento.setPessoa(pessoa);
        atendimento.setData(dto.data());
        atendimento.setTipo(dto.tipo());
        atendimento.setModalidade(dto.modalidade());
        atendimento.setDescricao(dto.descricao());
        atendimento.setCriadoPor(usuarioId);

        atendimentoRepository.save(atendimento);

        return AtendimentoRespostaDTO.fromEntity(atendimento);
    }

    @Transactional(readOnly = true)
    public AtendimentoRespostaDTO buscarPorId(Long id) {
        Atendimento atendimento = atendimentoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Atendimento não encontrado."));
        return AtendimentoRespostaDTO.fromEntity(atendimento);
    }

    @Transactional(readOnly = true)
    public List<AtendimentoRespostaDTO> listarPorProntuario(Long prontuarioId) {
        return atendimentoRepository.findByProntuarioIdOrderByDataDesc(prontuarioId).stream()
                .map(AtendimentoRespostaDTO::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<AtendimentoRespostaDTO> listarPorTecnico(Long tecnicoId) {
        return atendimentoRepository.findByTecnicoIdOrderByDataDesc(tecnicoId).stream()
                .map(AtendimentoRespostaDTO::fromEntity)
                .toList();
    }

    @Transactional
    public AtendimentoRespostaDTO atualizar(Long id, AtendimentoAtualizacaoDTO dto, Long usuarioId) {
        Atendimento atendimento = atendimentoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Atendimento não encontrado."));

        Pessoa pessoa = null;
        if (dto.pessoaId() != null) {
            pessoa = pessoaRepository.findById(dto.pessoaId())
                    .orElseThrow(() -> new EntityNotFoundException("Pessoa não encontrada."));
        }

        atendimento.setPessoa(pessoa);
        atendimento.setData(dto.data());
        atendimento.setTipo(dto.tipo());
        atendimento.setModalidade(dto.modalidade());
        atendimento.setDescricao(dto.descricao());
        atendimento.setAtualizadoPor(usuarioId);

        atendimentoRepository.save(atendimento);

        return AtendimentoRespostaDTO.fromEntity(atendimento);
    }
}
