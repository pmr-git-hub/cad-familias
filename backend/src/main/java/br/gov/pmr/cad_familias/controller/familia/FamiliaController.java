package br.gov.pmr.cad_familias.controller.familia;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.gov.pmr.cad_familias.dto.familia.FamiliaDTO;
import br.gov.pmr.cad_familias.service.familia.FamiliaService;

@RestController
@RequestMapping("/api/familia")
public class FamiliaController {

	private final FamiliaService familiaService;

	public FamiliaController(FamiliaService familiaService) {
		this.familiaService = familiaService;
	}

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<FamiliaDTO>> listarFamilias() {
		return ResponseEntity.ok(familiaService.listarFamilias());
	}

	@GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<FamiliaDTO> buscarPorId(@PathVariable Long id) {
		return ResponseEntity.ok(familiaService.buscarPorId(id));
	}

	@PostMapping(
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public ResponseEntity<FamiliaDTO> criarFamilia(
			@RequestBody FamiliaDTO familiaDTO,
			HttpServletRequest request) {
		Long usuarioId = (Long) request.getAttribute("usuarioId");
		return ResponseEntity.status(HttpStatus.CREATED).body(familiaService.salvar(familiaDTO, usuarioId));
	}

	@PutMapping(
			value = "/{id}",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public ResponseEntity<FamiliaDTO> editar(
			@PathVariable Long id,
			@RequestBody FamiliaDTO familiaEditada,
			HttpServletRequest request) {
		Long usuarioId = (Long) request.getAttribute("usuarioId");
		return ResponseEntity.ok(familiaService.editarFamilia(id, familiaEditada, usuarioId));
	}
}

