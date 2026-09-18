package br.gov.pmr.cad_familias.controller.gestacao;

import br.gov.pmr.cad_familias.dto.gestacao.*;
import br.gov.pmr.cad_familias.infra.seguranca.usuario.UsuarioLogado;
import br.gov.pmr.cad_familias.service.gestacao.GestacaoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/gestacoes")
public class GestacaoController {

    private final GestacaoService service;

    public GestacaoController(GestacaoService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public ResponseEntity<GestacaoRespostaDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping("/vinculo/{vinculoId}")
    public ResponseEntity<GestacaoRespostaDTO> buscarPorVinculo(@PathVariable Long vinculoId) {
        return ResponseEntity.ok(service.buscarPorVinculo(vinculoId));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<GestacaoRespostaDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody GestacaoAtualizacaoDTO dto,
            @UsuarioLogado Long usuarioId
    ) {
        return ResponseEntity.ok(service.atualizar(id, dto, usuarioId));
    }

    // ─── Acompanhamentos ─────────────────────────────────────────────────────────

    @PostMapping("/{id}/acompanhamentos")
    public ResponseEntity<GestacaoAcompanhamentoRespostaDTO> registrarAcompanhamento(
            @PathVariable Long id,
            @Valid @RequestBody GestacaoAcompanhamentoCadastroDTO dto,
            @UsuarioLogado Long usuarioId
    ) {
        return ResponseEntity.status(201).body(service.registrarAcompanhamento(id, dto, usuarioId));
    }

    @GetMapping("/{id}/acompanhamentos")
    public ResponseEntity<List<GestacaoAcompanhamentoRespostaDTO>> listarAcompanhamentos(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(service.listarAcompanhamentos(id));
    }

    @GetMapping("/{id}/acompanhamentos/{acompanhamentoId}")
    public ResponseEntity<GestacaoAcompanhamentoRespostaDTO> buscarAcompanhamento(
            @PathVariable Long id,
            @PathVariable Long acompanhamentoId
    ) {
        return ResponseEntity.ok(service.buscarAcompanhamento(id, acompanhamentoId));
    }
}
