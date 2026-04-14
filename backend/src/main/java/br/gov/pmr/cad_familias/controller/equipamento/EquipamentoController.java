package br.gov.pmr.cad_familias.controller.equipamento;

import br.gov.pmr.cad_familias.domain.equipamento.Equipamento;
import br.gov.pmr.cad_familias.dto.equipamento.EquipamentoAtualizacaoDTO;
import br.gov.pmr.cad_familias.service.equipamento.EquipamentoService;
import br.gov.pmr.cad_familias.util.Constantes;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/equipamentos")
public class EquipamentoController {

    @Autowired
    private EquipamentoService equipamentoService;

    @PostMapping
    public ResponseEntity<Equipamento> criarEquipamento(
            @RequestBody Equipamento equipamento, HttpServletRequest request) {
        Long usuarioId = (Long) request.getAttribute(Constantes.USUARIO_ID);
        Equipamento novoEquipamento = equipamentoService.criarEquipamento(equipamento, usuarioId);
        return ResponseEntity.ok(novoEquipamento);
    }

    @GetMapping
    public ResponseEntity<List<Equipamento>> listarEquipamentos() {
        List<Equipamento> equipamentos = equipamentoService.listarEquipamentos();
        return ResponseEntity.ok(equipamentos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Equipamento> buscarEquipamentoPorId(@PathVariable Long id) {
        Equipamento equipamento = equipamentoService.buscarEquipamentoPorId(id);
        return ResponseEntity.ok(equipamento);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Equipamento> atualizarEquipamento(
            @PathVariable Long id,
            @RequestBody EquipamentoAtualizacaoDTO equipamentoDTO,
            HttpServletRequest request) {
        Long usuarioId = (Long) request.getAttribute(Constantes.USUARIO_ID);
        Equipamento equipamentoAtualizado = equipamentoService.atualizarEquipamentoParcial(id, equipamentoDTO, usuarioId);
        return ResponseEntity.ok(equipamentoAtualizado);
    }
}
