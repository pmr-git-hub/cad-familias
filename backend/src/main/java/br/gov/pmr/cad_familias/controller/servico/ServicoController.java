package br.gov.pmr.cad_familias.controller.servico;

import br.gov.pmr.cad_familias.dto.servico.ServicoCreateRequest;
import br.gov.pmr.cad_familias.dto.servico.ServicoResponse;
import br.gov.pmr.cad_familias.dto.servico.ServicoUpdateRequest;
import br.gov.pmr.cad_familias.infra.seguranca.usuario.UsuarioLogado;
import br.gov.pmr.cad_familias.service.servico.ServicoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/servicos")
public class ServicoController {

    private final ServicoService service;

    public ServicoController(ServicoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ServicoResponse> criar(
            @Valid @RequestBody ServicoCreateRequest request,
            @UsuarioLogado Long usuarioId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(request, usuarioId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServicoResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ServicoUpdateRequest request,
            @UsuarioLogado Long usuarioId) {
        return ResponseEntity.ok(service.atualizar(id, request, usuarioId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServicoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<ServicoResponse>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/ativos")
    public ResponseEntity<List<ServicoResponse>> listarAtivos() {
        return ResponseEntity.ok(service.listarAtivos());
    }

    @GetMapping("/equipamento/{equipamentoId}")
    public ResponseEntity<List<ServicoResponse>> listarPorEquipamento(
            @PathVariable Long equipamentoId) {
        return ResponseEntity.ok(service.listarPorEquipamento(equipamentoId));
    }

    @GetMapping("/equipamento/{equipamentoId}/ativos")
    public ResponseEntity<List<ServicoResponse>> listarAtivosPorEquipamento(
            @PathVariable Long equipamentoId) {
        return ResponseEntity.ok(service.listarAtivosPorEquipamento(equipamentoId));
    }

    @GetMapping("/busca")
    public ResponseEntity<List<ServicoResponse>> buscarPorNome(@RequestParam String nome) {
        return ResponseEntity.ok(service.buscarPorNome(nome));
    }

    @PatchMapping("/{id}/desativar")
    public ResponseEntity<ServicoResponse> desativar(
            @PathVariable Long id,
            @UsuarioLogado Long usuarioId) {
        return ResponseEntity.ok(service.desativar(id, usuarioId));
    }
}
