package br.gov.pmr.cad_familias.controller.atendimento;

import br.gov.pmr.cad_familias.dto.atendimento.AtendimentoAtualizacaoDTO;
import br.gov.pmr.cad_familias.dto.atendimento.AtendimentoCadastroDTO;
import br.gov.pmr.cad_familias.dto.atendimento.AtendimentoRespostaDTO;
import br.gov.pmr.cad_familias.infra.seguranca.usuario.UsuarioLogado;
import br.gov.pmr.cad_familias.service.atendimento.AtendimentoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/atendimentos")
public class AtendimentoController {

    private final AtendimentoService atendimentoService;

    public AtendimentoController(AtendimentoService atendimentoService) {
        this.atendimentoService = atendimentoService;
    }

    @PostMapping
    public ResponseEntity<AtendimentoRespostaDTO> cadastrar(
            @RequestBody @Valid AtendimentoCadastroDTO dto,
            @UsuarioLogado Long usuarioId,
            UriComponentsBuilder uriBuilder) {

        AtendimentoRespostaDTO resposta = atendimentoService.cadastrar(dto, usuarioId);
        var uri = uriBuilder.path("/atendimentos/{id}").buildAndExpand(resposta.id()).toUri();
        return ResponseEntity.created(uri).body(resposta);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AtendimentoRespostaDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(atendimentoService.buscarPorId(id));
    }

    @GetMapping("/prontuario/{prontuarioId}")
    public ResponseEntity<List<AtendimentoRespostaDTO>> listarPorProntuario(@PathVariable Long prontuarioId) {
        return ResponseEntity.ok(atendimentoService.listarPorProntuario(prontuarioId));
    }

    @GetMapping("/tecnico/{tecnicoId}")
    public ResponseEntity<List<AtendimentoRespostaDTO>> listarPorTecnico(@PathVariable Long tecnicoId) {
        return ResponseEntity.ok(atendimentoService.listarPorTecnico(tecnicoId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AtendimentoRespostaDTO> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid AtendimentoAtualizacaoDTO dto,
            @UsuarioLogado Long usuarioId) {

        return ResponseEntity.ok(atendimentoService.atualizar(id, dto, usuarioId));
    }
}
