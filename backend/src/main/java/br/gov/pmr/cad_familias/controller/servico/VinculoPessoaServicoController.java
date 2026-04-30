package br.gov.pmr.cad_familias.controller.servico;

import br.gov.pmr.cad_familias.dto.programa.VinculoDesligamentoRequest;
import br.gov.pmr.cad_familias.dto.servico.VinculoPessoaServicoRequest;
import br.gov.pmr.cad_familias.dto.servico.VinculoPessoaServicoResponse;
import br.gov.pmr.cad_familias.infra.seguranca.usuario.UsuarioLogado;
import br.gov.pmr.cad_familias.service.servico.VinculoPessoaServicoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/vinculos-servico")
public class VinculoPessoaServicoController {

    private final VinculoPessoaServicoService service;

    public VinculoPessoaServicoController(VinculoPessoaServicoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<VinculoPessoaServicoResponse> vincular(
            @Valid @RequestBody VinculoPessoaServicoRequest request,
            @UsuarioLogado Long usuarioId,
            UriComponentsBuilder uriBuilder
    ) {
        VinculoPessoaServicoResponse resposta = service.vincular(request, usuarioId);
        var uri = uriBuilder.path("/api/vinculos-servico/{id}").buildAndExpand(resposta.getId()).toUri();
        return ResponseEntity.created(uri).body(resposta);
    }

    @PatchMapping("/{id}/desligar")
    public ResponseEntity<VinculoPessoaServicoResponse> desligar(
            @PathVariable Long id,
            @Valid @RequestBody VinculoDesligamentoRequest request,
            @UsuarioLogado Long usuarioId
    ) {
        return ResponseEntity.ok(service.desligar(id, request, usuarioId));
    }

    @PatchMapping("/{id}/suspender")
    public ResponseEntity<VinculoPessoaServicoResponse> suspender(
            @PathVariable Long id,
            @UsuarioLogado Long usuarioId
    ) {
        return ResponseEntity.ok(service.suspender(id, usuarioId));
    }

    @PatchMapping("/{id}/reativar")
    public ResponseEntity<VinculoPessoaServicoResponse> reativar(
            @PathVariable Long id,
            @UsuarioLogado Long usuarioId
    ) {
        return ResponseEntity.ok(service.reativar(id, usuarioId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<VinculoPessoaServicoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping("/pessoa/{pessoaId}")
    public ResponseEntity<List<VinculoPessoaServicoResponse>> listarPorPessoa(
            @PathVariable Long pessoaId
    ) {
        return ResponseEntity.ok(service.listarPorPessoa(pessoaId));
    }

    @GetMapping("/pessoa/{pessoaId}/ativos")
    public ResponseEntity<List<VinculoPessoaServicoResponse>> listarAtivosPorPessoa(
            @PathVariable Long pessoaId
    ) {
        return ResponseEntity.ok(service.listarAtivosPorPessoa(pessoaId));
    }

    @GetMapping("/servico/{servicoId}")
    public ResponseEntity<List<VinculoPessoaServicoResponse>> listarPorServico(
            @PathVariable Long servicoId
    ) {
        return ResponseEntity.ok(service.listarPorServico(servicoId));
    }

    @GetMapping("/servico/{servicoId}/ativos")
    public ResponseEntity<List<VinculoPessoaServicoResponse>> listarAtivosPorServico(
            @PathVariable Long servicoId
    ) {
        return ResponseEntity.ok(service.listarAtivosPorServico(servicoId));
    }
}
