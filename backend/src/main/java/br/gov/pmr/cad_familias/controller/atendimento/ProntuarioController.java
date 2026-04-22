package br.gov.pmr.cad_familias.controller.atendimento;

import br.gov.pmr.cad_familias.dto.atendimento.*;
import br.gov.pmr.cad_familias.infra.seguranca.usuario.UsuarioLogado;
import br.gov.pmr.cad_familias.service.atendimento.ProntuarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/prontuarios")
public class ProntuarioController {

    private final ProntuarioService prontuarioService;

    public ProntuarioController(ProntuarioService prontuarioService) {
        this.prontuarioService = prontuarioService;
    }

    // ── Abertura ────────────────────────────────────────────────────────────

    @PostMapping
    public ResponseEntity<ProntuarioRespostaDTO> cadastrar(
            @RequestBody @Valid ProntuarioCadastroDTO dto,
            @UsuarioLogado Long usuarioId,
            UriComponentsBuilder uriBuilder) {

        ProntuarioRespostaDTO resposta = prontuarioService.cadastrar(dto, usuarioId);
        var uri = uriBuilder.path("/api/prontuarios/{id}").buildAndExpand(resposta.id()).toUri();
        return ResponseEntity.created(uri).body(resposta);
    }

    // ── Consultas ───────────────────────────────────────────────────────────

    @GetMapping("/{id}")
    public ResponseEntity<ProntuarioRespostaDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(prontuarioService.buscarPorId(id));
    }

    @GetMapping("/familia/{familiaId}")
    public ResponseEntity<List<ProntuarioRespostaDTO>> listarPorFamilia(@PathVariable Long familiaId) {
        return ResponseEntity.ok(prontuarioService.listarPorFamilia(familiaId));
    }

    @GetMapping("/equipamento/{equipamentoId}")
    public ResponseEntity<List<ProntuarioRespostaDTO>> listarPorEquipamento(@PathVariable Long equipamentoId) {
        return ResponseEntity.ok(prontuarioService.listarPorEquipamento(equipamentoId));
    }

    @GetMapping("/tecnico/{tecnicoId}")
    public ResponseEntity<List<ProntuarioRespostaDTO>> listarPorTecnico(@PathVariable Long tecnicoId) {
        return ResponseEntity.ok(prontuarioService.listarPorTecnico(tecnicoId));
    }

    // ── Ações de status ─────────────────────────────────────────────────────

    @PatchMapping("/{id}/responsavel")
    public ResponseEntity<ProntuarioRespostaDTO> trocarResponsavel(
            @PathVariable Long id,
            @RequestBody @Valid ProntuarioResponsavelDTO dto,
            @UsuarioLogado Long usuarioId) {

        return ResponseEntity.ok(prontuarioService.trocarResponsavel(id, dto, usuarioId));
    }

    @PostMapping("/{id}/encerrar")
    public ResponseEntity<ProntuarioRespostaDTO> encerrar(
            @PathVariable Long id,
            @RequestBody @Valid ProntuarioEncerramentoDTO dto,
            @UsuarioLogado Long usuarioId) {

        return ResponseEntity.ok(prontuarioService.encerrar(id, dto, usuarioId));
    }

    @PostMapping("/{id}/suspender")
    public ResponseEntity<ProntuarioRespostaDTO> suspender(
            @PathVariable Long id,
            @UsuarioLogado Long usuarioId) {

        return ResponseEntity.ok(prontuarioService.suspender(id, usuarioId));
    }

    @PostMapping("/{id}/reabrir")
    public ResponseEntity<ProntuarioRespostaDTO> reabrir(
            @PathVariable Long id,
            @UsuarioLogado Long usuarioId) {

        return ResponseEntity.ok(prontuarioService.reabrir(id, usuarioId));
    }
}
