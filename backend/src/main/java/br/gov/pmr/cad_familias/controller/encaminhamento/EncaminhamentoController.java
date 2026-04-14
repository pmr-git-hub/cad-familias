package br.gov.pmr.cad_familias.controller.encaminhamento;

import br.gov.pmr.cad_familias.dto.encaminhamento.EncaminhamentoAtualizacaoStatusDTO;
import br.gov.pmr.cad_familias.dto.encaminhamento.EncaminhamentoCadastroDTO;
import br.gov.pmr.cad_familias.dto.encaminhamento.EncaminhamentoRespostaDTO;
import br.gov.pmr.cad_familias.infra.seguranca.usuario.UsuarioLogado;
import br.gov.pmr.cad_familias.service.encaminhamento.EncaminhamentoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/encaminhamentos")
public class EncaminhamentoController {

    private final EncaminhamentoService encaminhamentoService;

    public EncaminhamentoController(EncaminhamentoService encaminhamentoService) {
        this.encaminhamentoService = encaminhamentoService;
    }

    @PostMapping
    public ResponseEntity<EncaminhamentoRespostaDTO> cadastrar(
            @RequestBody @Valid EncaminhamentoCadastroDTO dto,
            @UsuarioLogado Long usuarioId,
            UriComponentsBuilder uriBuilder) {

        EncaminhamentoRespostaDTO resposta = encaminhamentoService.cadastrar(dto, usuarioId);
        var uri = uriBuilder.path("/encaminhamentos/{id}").buildAndExpand(resposta.id()).toUri();
        return ResponseEntity.created(uri).body(resposta);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EncaminhamentoRespostaDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(encaminhamentoService.buscarPorId(id));
    }

    @GetMapping("/familia/{familiaId}")
    public ResponseEntity<List<EncaminhamentoRespostaDTO>> listarPorFamilia(@PathVariable Long familiaId) {
        return ResponseEntity.ok(encaminhamentoService.listarPorFamilia(familiaId));
    }

    @GetMapping("/equipamento/{equipamentoId}/pendentes")
    public ResponseEntity<List<EncaminhamentoRespostaDTO>> listarPendentesDoEquipamento(@PathVariable Long equipamentoId) {
        return ResponseEntity.ok(encaminhamentoService.listarPendentesDoEquipamento(equipamentoId));
    }

    @GetMapping("/equipamento/{equipamentoId}/enviados")
    public ResponseEntity<List<EncaminhamentoRespostaDTO>> listarEnviadosDoEquipamento(@PathVariable Long equipamentoId) {
        return ResponseEntity.ok(encaminhamentoService.listarPorEquipamentoOrigem(equipamentoId));
    }

    @GetMapping("/tecnico/{tecnicoId}")
    public ResponseEntity<List<EncaminhamentoRespostaDTO>> listarPorTecnico(@PathVariable Long tecnicoId) {
        return ResponseEntity.ok(encaminhamentoService.listarPorTecnico(tecnicoId));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<EncaminhamentoRespostaDTO> atualizarStatus(
            @PathVariable Long id,
            @RequestBody @Valid EncaminhamentoAtualizacaoStatusDTO dto,
            @UsuarioLogado Long usuarioId) {

        return ResponseEntity.ok(encaminhamentoService.atualizarStatus(id, dto, usuarioId));
    }
}
