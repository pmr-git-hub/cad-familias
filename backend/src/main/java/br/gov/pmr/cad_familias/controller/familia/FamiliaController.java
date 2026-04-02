package br.gov.pmr.cad_familias.controller.familia;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.gov.pmr.cad_familias.VO.FamiliaVO;
import br.gov.pmr.cad_familias.service.familia.FamiliaService;

@RestController
@RequestMapping("/api/familia/v1")
public class FamiliaController {
	
	@Autowired
	private FamiliaService familiaService;
	
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public List<FamiliaVO> listarFamilias(){
		return this.familiaService.listarFamilias();
	}
	
	@PostMapping
	public FamiliaVO salvar(@RequestBody FamiliaVO familiaVO) {
		return familiaService.salvar(familiaVO);
	}
	
	@PutMapping(value="/{id}", consumes = {MediaType.APPLICATION_JSON_VALUE},
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public FamiliaVO editar(@RequestBody FamiliaVO familiaEditada) throws Exception {
		return this.familiaService.editarFamilia(familiaEditada);
	}

}
