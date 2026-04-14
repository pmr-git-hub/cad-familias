package br.gov.pmr.cad_familias.service.equipamento;

import br.gov.pmr.cad_familias.domain.equipamento.Equipamento;
import br.gov.pmr.cad_familias.dto.equipamento.EquipamentoAtualizacaoDTO;
import br.gov.pmr.cad_familias.repository.equipamento.EquipamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class EquipamentoService {

    @Autowired
    private EquipamentoRepository equipamentoRepository;

    public Equipamento criarEquipamento(Equipamento equipamento, Long usuarioId) {
        equipamento.setCriadoEm(LocalDateTime.now());
        equipamento.setCriadoPor(usuarioId);
        equipamento.setAtualizadoEm(LocalDateTime.now());
        equipamento.setAtualizadoPor(usuarioId);
        return equipamentoRepository.save(equipamento);
    }

    public List<Equipamento> listarEquipamentos() {
        return equipamentoRepository.findAll();
    }

    public Equipamento buscarEquipamentoPorId(Long id) {
        return equipamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Equipamento não encontrado"));
    }

    public Equipamento atualizarEquipamentoParcial(Long id, EquipamentoAtualizacaoDTO equipamentoDTO, Long usuarioId) {
        Equipamento equipamento = buscarEquipamentoPorId(id);

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
        return equipamentoRepository.save(equipamento);
    }


}
