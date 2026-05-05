package br.gov.pmr.cad_familias.service.atendimento;

import br.gov.pmr.cad_familias.domain.atendimento.Atendimento;
import br.gov.pmr.cad_familias.domain.atendimento.Prontuario;
import br.gov.pmr.cad_familias.domain.atendimento.StatusProntuario;
import br.gov.pmr.cad_familias.domain.audit.AcaoAudit;
import br.gov.pmr.cad_familias.domain.familia.Pessoa;
import br.gov.pmr.cad_familias.domain.programa.ProgramaSocial;
import br.gov.pmr.cad_familias.domain.servico.Servico;
import br.gov.pmr.cad_familias.domain.tecnico.Tecnico;
import br.gov.pmr.cad_familias.domain.usuario.Usuario;
import br.gov.pmr.cad_familias.dto.atendimento.AtendimentoAtualizacaoDTO;
import br.gov.pmr.cad_familias.dto.atendimento.AtendimentoCadastroDTO;
import br.gov.pmr.cad_familias.dto.atendimento.AtendimentoRespostaDTO;
import br.gov.pmr.cad_familias.excecao.TecnicoNaoEncontradoException;
import br.gov.pmr.cad_familias.excecao.UsuarioNaoEncontradoException;
import br.gov.pmr.cad_familias.repository.atendimento.AtendimentoRepository;
import br.gov.pmr.cad_familias.repository.atendimento.ProntuarioRepository;
import br.gov.pmr.cad_familias.repository.familia.PessoaRepository;
import br.gov.pmr.cad_familias.repository.programa.ProgramaSocialRepository;
import br.gov.pmr.cad_familias.repository.servico.ServicoRepository;
import br.gov.pmr.cad_familias.repository.tecnico.TecnicoEquipamentoRepository;
import br.gov.pmr.cad_familias.repository.tecnico.TecnicoRepository;
import br.gov.pmr.cad_familias.repository.usuario.UsuarioRepository;
import br.gov.pmr.cad_familias.service.audit.AuditService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AtendimentoService {

    private final AtendimentoRepository atendimentoRepository;
    private final ProntuarioRepository prontuarioRepository;
    private final TecnicoRepository tecnicoRepository;
    private final TecnicoEquipamentoRepository tecnicoEquipamentoRepository;
    private final PessoaRepository pessoaRepository;
    private final ServicoRepository servicoRepository;           
    private final ProgramaSocialRepository programaRepository;   
    private final AuditService auditService;
    private final UsuarioRepository usuarioRepository;

    public AtendimentoService(AtendimentoRepository atendimentoRepository,
                              ProntuarioRepository prontuarioRepository,
                              TecnicoRepository tecnicoRepository,
                              TecnicoEquipamentoRepository tecnicoEquipamentoRepository,
                              PessoaRepository pessoaRepository,
                              ServicoRepository servicoRepository,
                              ProgramaSocialRepository programaRepository,
                              AuditService auditService,
                              UsuarioRepository usuarioRepository) {
        this.atendimentoRepository = atendimentoRepository;
        this.prontuarioRepository = prontuarioRepository;
        this.tecnicoRepository = tecnicoRepository;
        this.tecnicoEquipamentoRepository = tecnicoEquipamentoRepository;
        this.pessoaRepository = pessoaRepository;
        this.servicoRepository = servicoRepository;
        this.programaRepository = programaRepository;
        this.auditService = auditService;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public AtendimentoRespostaDTO cadastrar(AtendimentoCadastroDTO dto, Long usuarioId) {

        Prontuario prontuario = prontuarioRepository.findById(dto.prontuarioId())
                .orElseThrow(() -> new EntityNotFoundException("Prontuário não encontrado."));

        if (prontuario.getStatus() != StatusProntuario.ABERTO) {
            throw new IllegalStateException("Não é possível registrar atendimento em prontuário que não está aberto.");
        }

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new UsuarioNaoEncontradoException());

        Tecnico tecnico = tecnicoRepository.findById(usuario.getTecnico().getId())
                .orElseThrow(() -> new TecnicoNaoEncontradoException());

        boolean vinculado = tecnicoEquipamentoRepository
                .existsByTecnicoIdAndEquipamentoIdAndAtivoTrue(
                        tecnico.getId(),
                        prontuario.getEquipamento().getId()
                );

        if (!vinculado) {
            throw new IllegalStateException("Técnico não está vinculado ao equipamento deste prontuário.");
        }

        // ✅ Busca pessoa (se informada)
        Pessoa pessoa = null;
        if (dto.pessoaId() != null) {
            pessoa = pessoaRepository.findById(dto.pessoaId())
                    .orElseThrow(() -> new EntityNotFoundException("Pessoa não encontrada."));
        }

        // ✅ Busca serviço (se informado)
        Servico servico = null;
        if (dto.servicoId() != null) {
            servico = servicoRepository.findById(dto.servicoId())
                    .orElseThrow(() -> new EntityNotFoundException("Serviço não encontrado."));
        }

        // ✅ Busca programa social (se informado)
        ProgramaSocial programa = null;
        if (dto.programaId() != null) {
            programa = programaRepository.findById(dto.programaId())
                    .orElseThrow(() -> new EntityNotFoundException("Programa social não encontrado."));
        }

        Atendimento atendimento = new Atendimento();
        atendimento.setProntuario(prontuario);
        atendimento.setTecnico(tecnico);
        atendimento.setPessoa(pessoa);
        atendimento.setServico(servico);      
        atendimento.setPrograma(programa);    
        atendimento.setData(dto.data());
        atendimento.setTipo(dto.tipo());
        atendimento.setModalidade(dto.modalidade());
        atendimento.setDescricao(dto.descricao());
        atendimento.setCriadoPor(usuarioId);

        Atendimento atendimentoSalvo = atendimentoRepository.save(atendimento);

        AtendimentoRespostaDTO resultado = AtendimentoRespostaDTO.fromEntity(atendimentoSalvo);

        auditService.registrar(
                "atendimento",
                atendimentoSalvo.getId(),
                AcaoAudit.INSERT,
                null,
                resultado,
                usuario
        );

        return resultado;
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

        // ✅ Captura estado ANTES
        AtendimentoRespostaDTO estadoAnterior = AtendimentoRespostaDTO.fromEntity(atendimento);

        // ✅ Atualiza pessoa
        Pessoa pessoa = null;
        if (dto.pessoaId() != null) {
            pessoa = pessoaRepository.findById(dto.pessoaId())
                    .orElseThrow(() -> new EntityNotFoundException("Pessoa não encontrada."));
        }

        // ✅ Atualiza serviço
        Servico servico = null;
        if (dto.servicoId() != null) {
            servico = servicoRepository.findById(dto.servicoId())
                    .orElseThrow(() -> new EntityNotFoundException("Serviço não encontrado."));
        }

        // ✅ Atualiza programa
        ProgramaSocial programa = null;
        if (dto.programaId() != null) {
            programa = programaRepository.findById(dto.programaId())
                    .orElseThrow(() -> new EntityNotFoundException("Programa social não encontrado."));
        }

        atendimento.setPessoa(pessoa);
        atendimento.setServico(servico);      
        atendimento.setPrograma(programa);    
        atendimento.setData(dto.data());
        atendimento.setTipo(dto.tipo());
        atendimento.setModalidade(dto.modalidade());
        atendimento.setDescricao(dto.descricao());
        atendimento.setAtualizadoPor(usuarioId);

        // ✅ Salva
        Atendimento atendimentoSalvo = atendimentoRepository.save(atendimento);

        // ✅ Estado DEPOIS
        AtendimentoRespostaDTO estadoNovo = AtendimentoRespostaDTO.fromEntity(atendimentoSalvo);

        // ✅ Auditoria
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new UsuarioNaoEncontradoException());

        auditService.registrar(
                "atendimento",
                atendimentoSalvo.getId(),
                AcaoAudit.UPDATE,
                estadoAnterior,
                estadoNovo,
                usuario
        );

        return estadoNovo;
    }
}
