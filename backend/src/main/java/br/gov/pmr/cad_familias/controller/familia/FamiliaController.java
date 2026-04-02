package br.gov.pmr.cad_familias.controller.familia;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.gov.pmr.cad_familias.VO.familia.FamiliaVO;
import br.gov.pmr.cad_familias.service.familia.FamiliaService;

@RestController
@RequestMapping("/api/familia/v1")
public class FamiliaController {

	private final FamiliaService familiaService;

	public FamiliaController(FamiliaService familiaService) {
		this.familiaService = familiaService;
	}

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<FamiliaVO>> listarFamilias() {
		return ResponseEntity.ok(familiaService.listarFamilias());
	}

	@PostMapping(
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public ResponseEntity<FamiliaVO> salvar(@RequestBody FamiliaVO familiaVO) {
		return ResponseEntity.status(HttpStatus.CREATED).body(familiaService.salvar(familiaVO));
	}

	@PutMapping(
			value = "/{id}",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public ResponseEntity<FamiliaVO> editar(
			@PathVariable Long id,
			@RequestBody FamiliaVO familiaEditada) {
		return ResponseEntity.ok(familiaService.editarFamilia(id, familiaEditada));
	}
}
