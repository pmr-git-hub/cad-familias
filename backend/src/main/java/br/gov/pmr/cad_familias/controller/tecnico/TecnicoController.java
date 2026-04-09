package br.gov.pmr.cad_familias.controller.tecnico;

import br.gov.pmr.cad_familias.dto.tecnico.TecnicoDTO;
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
    public ResponseEntity<TecnicoDTO> criarTecnico(@Valid @RequestBody TecnicoDTO tecnicoDTO, HttpServletRequest request) {
        Long usuarioId = (Long) request.getAttribute(Constantes.USUARIO_ID);
        return ResponseEntity.status(201).body(tecnicoService.criarTecnico(tecnicoDTO, usuarioId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TecnicoDTO> atualizarTecnico(
            @PathVariable Long id,
            @RequestBody TecnicoDTO tecnicoDTO,
            HttpServletRequest request) {
        Long usuarioId = (Long) request.getAttribute(Constantes.USUARIO_ID);
        TecnicoDTO tecnicoAtualizado = tecnicoService.atualizarTecnico(id, tecnicoDTO, usuarioId);
        return ResponseEntity.ok(tecnicoAtualizado);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desativar(@PathVariable Long id) {
        tecnicoService.desativar(id);
        return ResponseEntity.noContent().build();
    }
}
