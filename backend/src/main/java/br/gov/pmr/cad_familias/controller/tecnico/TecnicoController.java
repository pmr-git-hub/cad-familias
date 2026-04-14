package br.gov.pmr.cad_familias.controller.tecnico;

import br.gov.pmr.cad_familias.dto.tecnico.TecnicoDTO;
import br.gov.pmr.cad_familias.dto.tecnico.VincularEquipamentoDTO;
import br.gov.pmr.cad_familias.service.tecnico.TecnicoService;
import br.gov.pmr.cad_familias.util.Constantes;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
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

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TecnicoDTO>> listarTecnicos() {
        return ResponseEntity.ok(tecnicoService.listarTecnicos());
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TecnicoDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(tecnicoService.buscarPorId(id));
    }

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<TecnicoDTO> criarTecnico(
            @Valid @RequestBody TecnicoDTO tecnicoDTO,
            HttpServletRequest request
    ) {
        Long usuarioId = (Long) request.getAttribute(Constantes.USUARIO_ID);
        return ResponseEntity.status(201).body(tecnicoService.criarTecnico(tecnicoDTO, usuarioId));
    }

    @PutMapping(value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<TecnicoDTO> atualizarTecnico(
            @PathVariable Long id,
            @Valid @RequestBody TecnicoDTO tecnicoDTO,
            HttpServletRequest request
    ) {
        Long usuarioId = (Long) request.getAttribute(Constantes.USUARIO_ID);
        return ResponseEntity.ok(tecnicoService.atualizarTecnico(id, tecnicoDTO, usuarioId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desativar(@PathVariable Long id) {
        tecnicoService.desativar(id);
        return ResponseEntity.noContent().build();
    }

    // ─── Vínculos com Equipamento ───────────────────────────

    @PostMapping(value = "/{id}/equipamento",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<TecnicoDTO> vincularEquipamento(
            @PathVariable Long id,
            @Valid @RequestBody VincularEquipamentoDTO dto,
            HttpServletRequest request
    ) {
        Long usuarioId = (Long) request.getAttribute(Constantes.USUARIO_ID);
        return ResponseEntity.status(201).body(tecnicoService.vincularEquipamento(id, dto, usuarioId));
    }

    @DeleteMapping("/{tecnicoId}/equipamento/{equipamentoId}")
    public ResponseEntity<TecnicoDTO> desvincularEquipamento(
            @PathVariable Long tecnicoId,
            @PathVariable Long equipamentoId
    ) {
        return ResponseEntity.ok(tecnicoService.desvincularEquipamento(tecnicoId, equipamentoId));
    }
}
