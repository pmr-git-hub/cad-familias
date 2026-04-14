package br.gov.pmr.cad_familias.controller.atendimento;

import br.gov.pmr.cad_familias.dto.atendimento.ProntuarioAtualizacaoDTO;
import br.gov.pmr.cad_familias.dto.atendimento.ProntuarioCadastroDTO;
import br.gov.pmr.cad_familias.dto.atendimento.ProntuarioRespostaDTO;
import br.gov.pmr.cad_familias.infra.seguranca.usuario.UsuarioLogado;
import br.gov.pmr.cad_familias.service.atendimento.ProntuarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/prontuarios")
public class ProntuarioController {

    private final ProntuarioService prontuarioService;

    public ProntuarioController(ProntuarioService prontuarioService) {
        this.prontuarioService = prontuarioService;
    }

    @PostMapping
    public ResponseEntity<ProntuarioRespostaDTO> cadastrar(
            @RequestBody @Valid ProntuarioCadastroDTO dto,
            @UsuarioLogado Long usuarioId,
            UriComponentsBuilder uriBuilder) {

        ProntuarioRespostaDTO resposta = prontuarioService.cadastrar(dto, usuarioId);
        var uri = uriBuilder.path("/prontuarios/{id}").buildAndExpand(resposta.id()).toUri();
        return ResponseEntity.created(uri).body(resposta);
    }

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

    @PutMapping("/{id}")
    public ResponseEntity<ProntuarioRespostaDTO> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid ProntuarioAtualizacaoDTO dto,
            @UsuarioLogado Long usuarioId) {

        return ResponseEntity.ok(prontuarioService.atualizar(id, dto, usuarioId));
    }
}
