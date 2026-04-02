package br.gov.pmr.cad_familias.service.familia;

import java.util.List;

import br.gov.pmr.cad_familias.excecao.FamiliaNaoEncontradaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.pmr.cad_familias.VO.FamiliaVO;
import br.gov.pmr.cad_familias.mapper.FamiliaMapper;
import br.gov.pmr.cad_familias.domain.familia.Familia;
import br.gov.pmr.cad_familias.domain.familia.Pessoa;
import br.gov.pmr.cad_familias.repository.familia.FamiliaRepository;
import br.gov.pmr.cad_familias.repository.familia.PessoaRepository;
import jakarta.transaction.Transactional;

@Service
public class FamiliaService {

	@Autowired
	FamiliaRepository repositorioFamilia;

	@Autowired
	PessoaRepository repositorioPessoa;

	@Transactional
	public FamiliaVO salvar(FamiliaVO familiaVO) {
		Familia familiaSalva = repositorioFamilia.save(FamiliaMapper.familiaVoToFamilia(familiaVO));
		FamiliaVO familiaVOSalva = FamiliaMapper.familiaToFamiliaVo(familiaSalva);
		return familiaVOSalva;
	}

	public List<FamiliaVO> listarFamilias() {
		List<Familia> listaFamilias = repositorioFamilia.findAll();
		return FamiliaMapper.listaFamiliasToListaFamiliasVO(listaFamilias);
	}

	@Transactional
	public FamiliaVO editarFamilia(FamiliaVO familiaEditar) {
		return repositorioFamilia.findById(familiaEditar.getId()).map(familiaAtual -> {

			Familia familiaConvertida = FamiliaMapper.familiaVoToFamilia(familiaEditar);
			List<Pessoa> novosMembros = familiaConvertida.getMembrosDaFamilia();

			// Validação: Deve ter exatamente uma referência nos dados editados
			long countReferencias = novosMembros.stream().filter(Pessoa::isReferencia).count();
			if (countReferencias != 1) {
				throw new IllegalArgumentException("A família deve ter exatamente uma pessoa de referência, e ela não pode ser alterada.");
			}

			// Localiza a pessoa de referência nos novos dados
			Pessoa referenciaEditada = novosMembros.stream()
					.filter(Pessoa::isReferencia)
					.findFirst()
					.orElseThrow(() -> new IllegalArgumentException("Pessoa de referência não encontrada"));

			// Atualiza os dados da pessoa de referência existente
			Pessoa referenciaAtual = familiaAtual.getMembrosDaFamilia().stream()
					.filter(Pessoa::isReferencia)
					.findFirst()
					.orElseThrow(() -> new IllegalStateException("Família sem pessoa de referência"));

			atualizarDadosPessoa(referenciaAtual, referenciaEditada);

			// Separa membros que não são referência
			List<Pessoa> novosMembrosNaoReferencia = novosMembros.stream()
					.filter(p -> !p.isReferencia())
					.toList();

			// Remove membros antigos que não são referência
			List<Pessoa> membrosParaRemover = familiaAtual.getMembrosDaFamilia().stream()
					.filter(p -> !p.isReferencia())
					.toList();

			// ✅ Usa clear() ao invés de substituir a lista
			familiaAtual.getMembrosDaFamilia().removeAll(membrosParaRemover);

			// Adiciona os novos membros
			novosMembrosNaoReferencia.forEach(novoMembro -> {
				novoMembro.setFamilia(familiaAtual); // Define relacionamento bidirecional
				familiaAtual.getMembrosDaFamilia().add(novoMembro);
			});

			// Calcula renda familiar
			long rendaFamiliar = familiaAtual.getMembrosDaFamilia().stream()
					.mapToLong(pessoa -> pessoa.getRendaMensal() != null ? pessoa.getRendaMensal() : 0L)
					.sum();
//			familiaAtual.setRendaFamiliar(rendaFamiliar);

			// Salva a família (cascade vai salvar os membros)
			Familia familiaSalva = repositorioFamilia.save(familiaAtual);
			return FamiliaMapper.familiaToFamiliaVo(familiaSalva);

		}).orElseThrow(() -> new FamiliaNaoEncontradaException());
	}

	/**
	 * Atualiza os dados de uma pessoa existente com os dados de uma pessoa editada
	 */
	private void atualizarDadosPessoa(Pessoa pessoaExistente, Pessoa pessoaEditada) {
		pessoaExistente.setNome(pessoaEditada.getNome());
		pessoaExistente.setCpf(pessoaEditada.getCpf());
		pessoaExistente.setTelefone(pessoaEditada.getTelefone());
		pessoaExistente.setSexo(pessoaEditada.getSexo());
		pessoaExistente.setParentesco(pessoaEditada.getParentesco());
		pessoaExistente.setRendaMensal(pessoaEditada.getRendaMensal());
		pessoaExistente.setDataNascimento(pessoaEditada.getDataNascimento());
//		pessoaExistente.setIdade(pessoaEditada.getIdade());
		pessoaExistente.setNumeroRg(pessoaEditada.getNumeroRg());
		pessoaExistente.setOrgaoExpeditorRg(pessoaEditada.getOrgaoExpeditorRg());
		pessoaExistente.setDataExpedicaoRg(pessoaEditada.getDataExpedicaoRg());

		// Atualiza endereço se existir
		if (pessoaExistente.getEndereco() != null && pessoaEditada.getEndereco() != null) {
			pessoaExistente.getEndereco().setLogradouro(pessoaEditada.getEndereco().getLogradouro());
			pessoaExistente.getEndereco().setNumero(pessoaEditada.getEndereco().getNumero());
			pessoaExistente.getEndereco().setBairro(pessoaEditada.getEndereco().getBairro());
			pessoaExistente.getEndereco().setCidade(pessoaEditada.getEndereco().getCidade());
			pessoaExistente.getEndereco().setUf(pessoaEditada.getEndereco().getUf());
			pessoaExistente.getEndereco().setCep(pessoaEditada.getEndereco().getCep());
			pessoaExistente.getEndereco().setPontoReferencia(pessoaEditada.getEndereco().getPontoReferencia());
			pessoaExistente.getEndereco().setLocalizacaoDomicilio(pessoaEditada.getEndereco().getLocalizacaoDomicilio());
		} else if (pessoaEditada.getEndereco() != null) {
			// Se não tinha endereço e agora tem, adiciona
			pessoaExistente.setEndereco(pessoaEditada.getEndereco());
		}
	}
}
