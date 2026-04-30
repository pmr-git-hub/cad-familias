package br.gov.pmr.cad_familias.service.equipamento;

import br.gov.pmr.cad_familias.domain.audit.AcaoAudit;
import br.gov.pmr.cad_familias.domain.equipamento.Equipamento;
import br.gov.pmr.cad_familias.domain.tecnico.TecnicoEquipamento;
import br.gov.pmr.cad_familias.domain.usuario.Usuario;
import br.gov.pmr.cad_familias.dto.equipamento.EquipamentoAtualizacaoDTO;
import br.gov.pmr.cad_familias.excecao.UsuarioNaoEncontradoException;
import br.gov.pmr.cad_familias.repository.equipamento.EquipamentoRepository;
import br.gov.pmr.cad_familias.repository.tecnico.TecnicoEquipamentoRepository;
import br.gov.pmr.cad_familias.repository.usuario.UsuarioRepository;
import br.gov.pmr.cad_familias.service.audit.AuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EquipamentoService {

    @Autowired
    private EquipamentoRepository equipamentoRepository;

    @Autowired
    private TecnicoEquipamentoRepository tecnicoEquipamentoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private AuditService auditService;

    @Transactional
    public Equipamento criarEquipamento(Equipamento equipamento, Long usuarioId) {
        equipamento.setCriadoEm(LocalDateTime.now());
        equipamento.setCriadoPor(usuarioId);
        equipamento.setAtualizadoEm(LocalDateTime.now());
        equipamento.setAtualizadoPor(usuarioId);

        // ✅ Salva
        Equipamento equipamentoSalvo = equipamentoRepository.save(equipamento);

        // ✅ Auditoria (INSERT)
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new UsuarioNaoEncontradoException());

        auditService.registrar(
                "equipamento",
                equipamentoSalvo.getId(),
                AcaoAudit.INSERT,
                null,
                equipamentoSalvo,
                usuario
        );

        return equipamentoSalvo;
    }

    public List<Equipamento> listarEquipamentos() {
        return equipamentoRepository.findAll();
    }

    public Equipamento buscarEquipamentoPorId(Long id) {
        return equipamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Equipamento não encontrado"));
    }

    public List<Equipamento> listarEquipamentosTecnico(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId).orElseThrow();
        List<TecnicoEquipamento> vinculos =
                tecnicoEquipamentoRepository.findByTecnicoIdAndAtivoTrue(usuario.getTecnico().getId());

        return vinculos.stream().map(TecnicoEquipamento::getEquipamento).toList();
    }

    @Transactional
    public Equipamento atualizarEquipamentoParcial(Long id, EquipamentoAtualizacaoDTO equipamentoDTO, Long usuarioId) {
        Equipamento equipamento = buscarEquipamentoPorId(id);

        // ✅ Captura estado ANTES (cópia simples dos campos relevantes)
        Equipamento estadoAnterior = copiarEquipamento(equipamento);

        if (equipamentoDTO.getNome() != null) {
            equipamento.setNome(equipamentoDTO.getNome());
        }
        if (equipamentoDTO.getTipo() != null) {
            equipamento.setTipo(equipamentoDTO.getTipo());
        }
        if (equipamentoDTO.getCep() != null) {
            equipamento.setCep(equipamentoDTO.getCep());
        }
        if (equipamentoDTO.getLogradouro() != null) {
            equipamento.setLogradouro(equipamentoDTO.getLogradouro());
        }
        if (equipamentoDTO.getNumero() != null) {
            equipamento.setNumero(equipamentoDTO.getNumero());
        }
        if (equipamentoDTO.getComplemento() != null) {
            equipamento.setComplemento(equipamentoDTO.getComplemento());
        }
        if (equipamentoDTO.getBairro() != null) {
            equipamento.setBairro(equipamentoDTO.getBairro());
        }
        if (equipamentoDTO.getCidade() != null) {
            equipamento.setCidade(equipamentoDTO.getCidade());
        }
        if (equipamentoDTO.getEstado() != null) {
            equipamento.setEstado(equipamentoDTO.getEstado());
        }
        if (equipamentoDTO.getTelefone() != null) {
            equipamento.setTelefone(equipamentoDTO.getTelefone());
        }
        if (equipamentoDTO.getEmail() != null) {
            equipamento.setEmail(equipamentoDTO.getEmail());
        }
        if (equipamentoDTO.isAtivo() != null) {
            equipamento.setAtivo(equipamentoDTO.isAtivo());
        }

        equipamento.setAtualizadoEm(LocalDateTime.now());
        equipamento.setAtualizadoPor(usuarioId);

        // ✅ Salva
        Equipamento equipamentoSalvo = equipamentoRepository.save(equipamento);

        // ✅ Auditoria (UPDATE)
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new UsuarioNaoEncontradoException());

        auditService.registrar(
                "equipamento",
                equipamentoSalvo.getId(),
                AcaoAudit.UPDATE,
                estadoAnterior,
                equipamentoSalvo,
                usuario
        );

        return equipamentoSalvo;
    }

    // ✅ Método auxiliar para copiar equipamento (snapshot simples)
    private Equipamento copiarEquipamento(Equipamento original) {
        Equipamento copia = new Equipamento();
        copia.setId(original.getId());
        copia.setNome(original.getNome());
        copia.setTipo(original.getTipo());
        copia.setCep(original.getCep());
        copia.setLogradouro(original.getLogradouro());
        copia.setNumero(original.getNumero());
        copia.setComplemento(original.getComplemento());
        copia.setBairro(original.getBairro());
        copia.setCidade(original.getCidade());
        copia.setEstado(original.getEstado());
        copia.setTelefone(original.getTelefone());
        copia.setEmail(original.getEmail());
        copia.setAtivo(original.isAtivo());
        return copia;
    }
}
