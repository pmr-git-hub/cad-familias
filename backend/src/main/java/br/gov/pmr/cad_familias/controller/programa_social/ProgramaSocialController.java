package br.gov.pmr.cad_familias.controller.programa_social;

import br.gov.pmr.cad_familias.dto.programa.ProgramaSocialCreateRequest;
import br.gov.pmr.cad_familias.dto.programa.ProgramaSocialUpdateRequest;
import br.gov.pmr.cad_familias.dto.programa.ProgramaSocialResponse;
import br.gov.pmr.cad_familias.infra.seguranca.usuario.UsuarioLogado;
import br.gov.pmr.cad_familias.service.programa.ProgramaSocialService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/programas-sociais")
public class ProgramaSocialController {

    private final ProgramaSocialService service;

    public ProgramaSocialController(ProgramaSocialService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ProgramaSocialResponse> criar(
            @Valid @RequestBody ProgramaSocialCreateRequest request,
            @UsuarioLogado Long usuarioId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(request, usuarioId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProgramaSocialResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ProgramaSocialUpdateRequest request,
            @UsuarioLogado Long usuarioId) {
        return ResponseEntity.ok(service.atualizar(id, request, usuarioId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProgramaSocialResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<ProgramaSocialResponse>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/ativos")
    public ResponseEntity<List<ProgramaSocialResponse>> listarAtivos() {
        return ResponseEntity.ok(service.listarAtivos());
    }

    @GetMapping("/busca")
    public ResponseEntity<List<ProgramaSocialResponse>> buscarPorNome(@RequestParam String nome) {
        return ResponseEntity.ok(service.buscarPorNome(nome));
    }

    @PatchMapping("/{id}/desativar")
    public ResponseEntity<ProgramaSocialResponse> desativar(
            @PathVariable Long id,
            @UsuarioLogado Long usuarioId) {
        return ResponseEntity.ok(service.desativar(id, usuarioId));
    }
}
