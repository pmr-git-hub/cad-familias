package br.gov.pmr.cad_familias.service.familia;

import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.pmr.cad_familias.dto.familia.FamiliaDTO;
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

	public List<FamiliaDTO> listarFamilias() {
		return FamiliaMapper.listaFamiliasToListaFamiliasVO(repositorioFamilia.findAll());
	}

	public FamiliaDTO buscarPorId(Long id) {
		Familia familia = repositorioFamilia.findById(id)
				.orElseThrow(FamiliaNaoEncontradaException::new);
		return FamiliaMapper.familiaToFamiliaVo(familia);
	}

	@Transactional
	public FamiliaDTO salvar(FamiliaDTO familiaDTO, Long usuarioId) {
		Familia familia = FamiliaMapper.familiaVoToFamilia(familiaDTO);

		// Validação: exatamente uma pessoa de referência
		long countReferencias = familia.getMembrosDaFamilia().stream()
				.filter(Pessoa::isReferencia).count();
		if (countReferencias != 1) {
			throw new IllegalArgumentException("A família deve ter exatamente uma pessoa de referência.");
		}

		// Validação: pessoa de referência deve ter endereço
		Pessoa referencia = familia.getMembrosDaFamilia().stream()
				.filter(Pessoa::isReferencia)
				.findFirst()
				.orElseThrow(() -> new IllegalArgumentException("Pessoa de referência não encontrada."));

		if (referencia.getEndereco() == null || referencia.getEndereco().getLogradouro() == null) {
			throw new IllegalArgumentException("A pessoa de referência deve ter endereço preenchido.");
		}

		// Validação: pessoa de referência não tem parentesco
		referencia.setParentesco(null);

		// Auditoria
		familia.setCriadoPor(usuarioId);
		familia.getMembrosDaFamilia().forEach(membro -> {
			membro.setCriadoPor(usuarioId);
		});

		return FamiliaMapper.familiaToFamiliaVo(repositorioFamilia.save(familia));
	}

	@Transactional
	public FamiliaDTO editarFamilia(Long id, FamiliaDTO familiaEditar, Long usuarioId) {
		Familia familiaAtual = repositorioFamilia.findById(id)
				.orElseThrow(FamiliaNaoEncontradaException::new);

		// === 1. Atualiza campos simples da família (parcial) ===
		if (familiaEditar.getCodigoCadunico() != null) {
			familiaAtual.setCodigoCadunico(familiaEditar.getCodigoCadunico());
		}
		if (familiaEditar.getSituacao() != null) {
			familiaAtual.setSituacao(familiaEditar.getSituacao());
		}

		// === 2. Só atualiza membros se veio pessoaReferencia no DTO ===
		boolean temAtualizacaoDeMembros = familiaEditar.getPessoaReferencia() != null;

		if (temAtualizacaoDeMembros) {
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

			// Validação: endereço obrigatório
			if (referenciaEditada.getEndereco() == null || referenciaEditada.getEndereco().getLogradouro() == null) {
				throw new IllegalArgumentException("A pessoa de referência deve ter endereço preenchido.");
			}

			// Pessoa de referência não tem parentesco
			referenciaEditada.setParentesco(null);

			Pessoa referenciaAtual = familiaAtual.getMembrosDaFamilia().stream()
					.filter(Pessoa::isReferencia)
					.findFirst()
					.orElseThrow(() -> new IllegalStateException("Família sem pessoa de referência."));

			atualizarDadosPessoa(referenciaAtual, referenciaEditada);
			referenciaAtual.setAtualizadoPor(usuarioId);

			// Substitui membros não referência
			familiaAtual.getMembrosDaFamilia().removeIf(p -> !p.isReferencia());

			novosMembros.stream()
					.filter(p -> !p.isReferencia())
					.forEach(novo -> {
						novo.setFamilia(familiaAtual);
						novo.setCriadoPor(usuarioId);
						familiaAtual.getMembrosDaFamilia().add(novo);
					});
		}

		// Auditoria da família
		familiaAtual.setAtualizadoPor(usuarioId);

		return FamiliaMapper.familiaToFamiliaVo(repositorioFamilia.save(familiaAtual));
	}


	private void atualizarDadosPessoa(Pessoa existente, Pessoa editada) {
		existente.setNome(editada.getNome());
		existente.setCpf(editada.getCpf());
		existente.setNis(editada.getNis());
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

