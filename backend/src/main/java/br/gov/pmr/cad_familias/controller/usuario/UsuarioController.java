package br.gov.pmr.cad_familias.controller.usuario;

import br.gov.pmr.cad_familias.dto.usuario.AtualizarUsuarioDTO;
import br.gov.pmr.cad_familias.dto.usuario.CriarUsuarioDTO;
import br.gov.pmr.cad_familias.dto.usuario.UsuarioDTO;
import br.gov.pmr.cad_familias.service.auth.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> listar() {
        return ResponseEntity.ok(usuarioService.listarUsuarios());
    }

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<UsuarioDTO> criar(@Valid @RequestBody CriarUsuarioDTO dto) {
        return ResponseEntity.status(201).body(usuarioService.criarUsuario(dto));
    }

    @PutMapping(
            value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<UsuarioDTO> atualizar(@PathVariable Long id,
                                                @Valid @RequestBody AtualizarUsuarioDTO dto) {
        return ResponseEntity.ok(usuarioService.atualizarUsuario(id, dto));
    }
}
