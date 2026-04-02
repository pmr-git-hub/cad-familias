package br.gov.pmr.cad_familias.controller.tecnico;

import br.gov.pmr.cad_familias.VO.tecnico.TecnicoVO;
import br.gov.pmr.cad_familias.service.tecnico.TecnicoService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tecnico/v1")
public class TecnicoController {

    private final TecnicoService tecnicoService;

    public TecnicoController(TecnicoService tecnicoService) {
        this.tecnicoService = tecnicoService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TecnicoVO>> listarTecnicos() {
        return ResponseEntity.ok(tecnicoService.listarTecnicos());
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TecnicoVO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(tecnicoService.buscarPorId(id));
    }

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<TecnicoVO> salvar(@Valid @RequestBody TecnicoVO tecnicoVO) {
        return ResponseEntity.status(201).body(tecnicoService.salvar(tecnicoVO));
    }

    @PutMapping(
            value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<TecnicoVO> editar(
            @PathVariable Long id,
            @Valid @RequestBody TecnicoVO tecnicoVO) {
        return ResponseEntity.ok(tecnicoService.editar(id, tecnicoVO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desativar(@PathVariable Long id) {
        tecnicoService.desativar(id);
        return ResponseEntity.noContent().build();
    }
}
