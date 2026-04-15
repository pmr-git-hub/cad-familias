package br.gov.pmr.cad_familias.controller.programa_social;

import br.gov.pmr.cad_familias.dto.programa.VinculoDesligamentoRequest;
import br.gov.pmr.cad_familias.dto.programa.VinculoFamiliaProgramaRequest;
import br.gov.pmr.cad_familias.dto.programa.VinculoFamiliaProgramaResponse;
import br.gov.pmr.cad_familias.service.programa.VinculoFamiliaProgramaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vinculos-programa")
public class VinculoFamiliaProgramaController {

    private final VinculoFamiliaProgramaService service;

    public VinculoFamiliaProgramaController(VinculoFamiliaProgramaService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<VinculoFamiliaProgramaResponse> vincular(
            @Valid @RequestBody VinculoFamiliaProgramaRequest request,
            @RequestHeader("X-Usuario-Id") Long usuarioId
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.vincular(request, usuarioId));
    }

    @PatchMapping("/{id}/desligar")
    public ResponseEntity<VinculoFamiliaProgramaResponse> desligar(
            @PathVariable Long id,
            @Valid @RequestBody VinculoDesligamentoRequest request,
            @RequestHeader("X-Usuario-Id") Long usuarioId
    ) {
        return ResponseEntity.ok(service.desligar(id, request, usuarioId));
    }

    @PatchMapping("/{id}/suspender")
    public ResponseEntity<VinculoFamiliaProgramaResponse> suspender(
            @PathVariable Long id,
            @RequestHeader("X-Usuario-Id") Long usuarioId
    ) {
        return ResponseEntity.ok(service.suspender(id, usuarioId));
    }

    @PatchMapping("/{id}/reativar")
    public ResponseEntity<VinculoFamiliaProgramaResponse> reativar(
            @PathVariable Long id,
            @RequestHeader("X-Usuario-Id") Long usuarioId
    ) {
        return ResponseEntity.ok(service.reativar(id, usuarioId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<VinculoFamiliaProgramaResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping("/familia/{familiaId}")
    public ResponseEntity<List<VinculoFamiliaProgramaResponse>> listarPorFamilia(
            @PathVariable Long familiaId
    ) {
        return ResponseEntity.ok(service.listarPorFamilia(familiaId));
    }

    @GetMapping("/familia/{familiaId}/ativos")
    public ResponseEntity<List<VinculoFamiliaProgramaResponse>> listarAtivosPorFamilia(
            @PathVariable Long familiaId
    ) {
        return ResponseEntity.ok(service.listarAtivosPorFamilia(familiaId));
    }

    @GetMapping("/programa/{programaId}")
    public ResponseEntity<List<VinculoFamiliaProgramaResponse>> listarPorPrograma(
            @PathVariable Long programaId
    ) {
        return ResponseEntity.ok(service.listarPorPrograma(programaId));
    }

    @GetMapping("/programa/{programaId}/ativos")
    public ResponseEntity<List<VinculoFamiliaProgramaResponse>> listarAtivosPorPrograma(
            @PathVariable Long programaId
    ) {
        return ResponseEntity.ok(service.listarAtivosPorPrograma(programaId));
    }
}
