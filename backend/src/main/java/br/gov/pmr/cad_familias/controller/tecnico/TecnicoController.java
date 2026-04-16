package br.gov.pmr.cad_familias.controller.tecnico;

import br.gov.pmr.cad_familias.dto.tecnico.TecnicoDTO;
import br.gov.pmr.cad_familias.dto.tecnico.VincularEquipamentoDTO;
import br.gov.pmr.cad_familias.infra.seguranca.usuario.UsuarioLogado;
import br.gov.pmr.cad_familias.service.tecnico.TecnicoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tecnico")
public class TecnicoController {

    private final TecnicoService tecnicoService;

    public TecnicoController(TecnicoService tecnicoService) {
        this.tecnicoService = tecnicoService;
    }

    @GetMapping
    public ResponseEntity<List<TecnicoDTO>> listarTecnicos() {
        return ResponseEntity.ok(tecnicoService.listarTecnicos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TecnicoDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(tecnicoService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<TecnicoDTO> criarTecnico(
            @Valid @RequestBody TecnicoDTO tecnicoDTO,
            @UsuarioLogado Long usuarioId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(tecnicoService.criarTecnico(tecnicoDTO, usuarioId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TecnicoDTO> atualizarTecnico(
            @PathVariable Long id,
            @Valid @RequestBody TecnicoDTO tecnicoDTO,
            @UsuarioLogado Long usuarioId) {
        return ResponseEntity.ok(tecnicoService.atualizarTecnico(id, tecnicoDTO, usuarioId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desativar(@PathVariable Long id) {
        tecnicoService.desativar(id);
        return ResponseEntity.noContent().build();
    }

    // ─── Vínculos com Equipamento ───────────────────────────

    @PostMapping("/{id}/equipamento")
    public ResponseEntity<TecnicoDTO> vincularEquipamento(
            @PathVariable Long id,
            @Valid @RequestBody VincularEquipamentoDTO dto,
            @UsuarioLogado Long usuarioId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(tecnicoService.vincularEquipamento(id, dto, usuarioId));
    }

    @DeleteMapping("/{tecnicoId}/equipamento/{equipamentoId}")
    public ResponseEntity<TecnicoDTO> desvincularEquipamento(
            @PathVariable Long tecnicoId,
            @PathVariable Long equipamentoId) {
        return ResponseEntity.ok(tecnicoService.desvincularEquipamento(tecnicoId, equipamentoId));
    }
}
