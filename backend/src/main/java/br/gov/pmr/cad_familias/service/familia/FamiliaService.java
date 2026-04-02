package br.gov.pmr.cad_familias.service.familia;

import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.pmr.cad_familias.VO.familia.FamiliaVO;
import br.gov.pmr.cad_familias.VO.familia.PessoaVO;
import br.gov.pmr.cad_familias.domain.familia.Endereco;
import br.gov.pmr.cad_familias.domain.familia.Familia;
import br.gov.pmr.cad_familias.domain.familia.Pessoa;
import br.gov.pmr.cad_familias.excecao.FamiliaNaoEncontradaException;
import br.gov.pmr.cad_familias.mapper.familia.FamiliaMapper;
import br.gov.pmr.cad_familias.repository.familia.FamiliaRepository;
import jakarta.transaction.Transactional;

@Service
public class FamiliaService {

	private final FamiliaRepository repositorioFamilia;

	public FamiliaService(FamiliaRepository repositorioFamilia) {
		this.repositorioFamilia = repositorioFamilia;
	}

	@Transactional
	public FamiliaVO salvar(FamiliaVO familiaVO) {
		Familia familia = FamiliaMapper.familiaVoToFamilia(familiaVO);
		return FamiliaMapper.familiaToFamiliaVo(repositorioFamilia.save(familia));
	}

	public List<FamiliaVO> listarFamilias() {
		return FamiliaMapper.listaFamiliasToListaFamiliasVO(repositorioFamilia.findAll());
	}

	@Transactional
	public FamiliaVO editarFamilia(Long id, FamiliaVO familiaEditar) {
		Familia familiaAtual = repositorioFamilia.findById(id)
				.orElseThrow(FamiliaNaoEncontradaException::new);

		Familia familiaConvertida = FamiliaMapper.familiaVoToFamilia(familiaEditar);
		List<Pessoa> novosMembros = familiaConvertida.getMembrosDaFamilia();

		// Valida que existe exatamente uma referência
		long countReferencias = novosMembros.stream().filter(Pessoa::isReferencia).count();
		if (countReferencias != 1) {
			throw new IllegalArgumentException("A família deve ter exatamente uma pessoa de referência.");
		}

		// Atualiza a pessoa de referência
		Pessoa referenciaEditada = novosMembros.stream()
				.filter(Pessoa::isReferencia)
				.findFirst()
				.orElseThrow(() -> new IllegalArgumentException("Pessoa de referência não encontrada."));

		Pessoa referenciaAtual = familiaAtual.getMembrosDaFamilia().stream()
				.filter(Pessoa::isReferencia)
				.findFirst()
				.orElseThrow(() -> new IllegalStateException("Família sem pessoa de referência."));

		atualizarDadosPessoa(referenciaAtual, referenciaEditada);

		// Substitui membros não-referência
		familiaAtual.getMembrosDaFamilia().removeIf(p -> !p.isReferencia());

		novosMembros.stream()
				.filter(p -> !p.isReferencia())
				.forEach(novo -> {
					novo.setFamilia(familiaAtual);
					familiaAtual.getMembrosDaFamilia().add(novo);
				});

		return FamiliaMapper.familiaToFamiliaVo(repositorioFamilia.save(familiaAtual));
	}

	private void atualizarDadosPessoa(Pessoa existente, Pessoa editada) {
		existente.setNome(editada.getNome());
		existente.setCpf(editada.getCpf());
		existente.setTelefone(editada.getTelefone());
		existente.setSexo(editada.getSexo());
		existente.setParentesco(editada.getParentesco());
		existente.setRendaMensal(editada.getRendaMensal());
		existente.setDataNascimento(editada.getDataNascimento());
		existente.setNumeroRg(editada.getNumeroRg());
		existente.setOrgaoExpeditorRg(editada.getOrgaoExpeditorRg());
		existente.setDataExpedicaoRg(editada.getDataExpedicaoRg());

		if (existente.getEndereco() != null && editada.getEndereco() != null) {
			atualizarEndereco(existente.getEndereco(), editada.getEndereco());
		} else if (editada.getEndereco() != null) {
			existente.setEndereco(editada.getEndereco());
		}
	}

	private void atualizarEndereco(Endereco existente, Endereco editado) {
		existente.setLogradouro(editado.getLogradouro());
		existente.setNumero(editado.getNumero());
		existente.setBairro(editado.getBairro());
		existente.setCidade(editado.getCidade());
		existente.setUf(editado.getUf());
		existente.setCep(editado.getCep());
		existente.setPontoReferencia(editado.getPontoReferencia());
		existente.setLocalizacaoDomicilio(editado.getLocalizacaoDomicilio());
	}
}
