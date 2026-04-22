package br.gov.pmr.cad_familias.controller.equipamento;

import br.gov.pmr.cad_familias.domain.equipamento.Equipamento;
import br.gov.pmr.cad_familias.dto.equipamento.EquipamentoAtualizacaoDTO;
import br.gov.pmr.cad_familias.infra.seguranca.usuario.UsuarioLogado;
import br.gov.pmr.cad_familias.service.equipamento.EquipamentoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/equipamentos")
public class EquipamentoController {

    private final EquipamentoService equipamentoService;

    public EquipamentoController(EquipamentoService equipamentoService) {
        this.equipamentoService = equipamentoService;
    }

    @PostMapping
    public ResponseEntity<Equipamento> criarEquipamento(
            @Valid @RequestBody Equipamento equipamento,
            @UsuarioLogado Long usuarioId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(equipamentoService.criarEquipamento(equipamento, usuarioId));
    }

    @GetMapping
    public ResponseEntity<List<Equipamento>> listarEquipamentos() {
        return ResponseEntity.ok(equipamentoService.listarEquipamentos());
    }

    @GetMapping("/meusEquipamentos")
    public ResponseEntity<List<Equipamento>> listarEquipamentosPorTecnico(@UsuarioLogado Long usuarioId) {
        return ResponseEntity.ok(equipamentoService.listarEquipamentosTecnico(usuarioId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Equipamento> buscarEquipamentoPorId(@PathVariable Long id) {
        return ResponseEntity.ok(equipamentoService.buscarEquipamentoPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Equipamento> atualizarEquipamento(
            @PathVariable Long id,
            @Valid @RequestBody EquipamentoAtualizacaoDTO equipamentoDTO,
            @UsuarioLogado Long usuarioId) {
        return ResponseEntity.ok(equipamentoService.atualizarEquipamentoParcial(id, equipamentoDTO, usuarioId));
    }
}
