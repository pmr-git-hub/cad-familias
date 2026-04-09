package br.gov.pmr.cad_familias.mapper.familia;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import br.gov.pmr.cad_familias.dto.familia.EnderecoDTO;
import br.gov.pmr.cad_familias.dto.familia.FamiliaDTO;
import br.gov.pmr.cad_familias.dto.familia.PessoaDTO;
import br.gov.pmr.cad_familias.domain.familia.Endereco;
import br.gov.pmr.cad_familias.domain.familia.Familia;
import br.gov.pmr.cad_familias.domain.familia.Pessoa;

public class FamiliaMapper {

    // VO → Entidade
    public static Familia familiaVoToFamilia(FamiliaDTO familiaDTO) {
        if (familiaDTO == null) throw new IllegalArgumentException("FamiliaVO não pode ser null.");

        Familia familia = new Familia();
        familia.setCodigoCadunico(familiaDTO.getCodigoCadunico());
        familia.setSituacao(familiaDTO.getSituacao());
        familia.setMembrosDaFamilia(new ArrayList<>());

        familia.setMembrosDaFamilia(new ArrayList<>());

        // Mapeia membros comuns
        List<PessoaDTO> membros = Optional.ofNullable(familiaDTO.getMembrosDaFamilia()).orElse(new ArrayList<>());
        for (PessoaDTO pessoaDTO : membros) {
            Pessoa pessoa = pessoaVoToPessoa(pessoaDTO);
            pessoa.setFamilia(familia);
            familia.getMembrosDaFamilia().add(pessoa);
        }

        // Mapeia pessoa de referência
        if (familiaDTO.getPessoaReferencia() != null) {
            Pessoa referencia = pessoaVoToPessoa(familiaDTO.getPessoaReferencia());
            referencia.setReferencia(true);
            referencia.setFamilia(familia);
            familia.getMembrosDaFamilia().add(referencia);
        }

        return familia;
    }

    // Entidade → VO
    public static FamiliaDTO familiaToFamiliaVo(Familia familia) {
        if (familia == null) throw new IllegalArgumentException("Familia não pode ser null.");

        FamiliaDTO familiaDTO = new FamiliaDTO();
        familiaDTO.setId(familia.getId());
        familiaDTO.setCodigoCadunico(familia.getCodigoCadunico());
        familiaDTO.setSituacao(familia.getSituacao());

        List<PessoaDTO> membros = new ArrayList<>();

        familia.getMembrosDaFamilia().forEach(membro -> {
            PessoaDTO pessoaDTO = pessoaToPessoaVo(membro);
            if (membro.isReferencia()) {
                familiaDTO.setPessoaReferencia(pessoaDTO);
            } else {
                membros.add(pessoaDTO);
            }
        });

        familiaDTO.setMembrosDaFamilia(membros);

        // Calcula renda familiar
        long rendaFamiliar = familia.getMembrosDaFamilia().stream()
                .mapToLong(p -> p.getRendaMensal() != null ? p.getRendaMensal() : 0L)
                .sum();
        familiaDTO.setRendaFamiliar(rendaFamiliar);

        return familiaDTO;
    }

    public static List<FamiliaDTO> listaFamiliasToListaFamiliasVO(List<Familia> familias) {
        return familias.stream()
                .map(FamiliaMapper::familiaToFamiliaVo)
                .toList();
    }

    // Pessoa VO → Entidade
    public static Pessoa pessoaVoToPessoa(PessoaDTO vo) {
        if (vo == null) return null;
        Pessoa pessoa = new Pessoa();
        pessoa.setId(vo.getId());
        pessoa.setNome(vo.getNome());
        pessoa.setCpf(vo.getCpf() != null && vo.getCpf().trim().isEmpty() ? null : vo.getCpf());
        pessoa.setTelefone(vo.getTelefone());
        pessoa.setSexo(vo.getSexo());
        pessoa.setParentesco(vo.getParentesco());
        pessoa.setRendaMensal(vo.getRendaMensal());
        pessoa.setDataNascimento(vo.getDataNascimento());
        pessoa.setNumeroRg(vo.getNumeroRg());
        pessoa.setOrgaoExpeditorRg(vo.getOrgaoExpeditorRg());
        pessoa.setDataExpedicaoRg(vo.getDataExpedicaoRg());
        pessoa.setReferencia(vo.isReferencia());
        pessoa.setEndereco(enderecoVoToEndereco(vo.getEndereco()));
        return pessoa;
    }

    // Pessoa Entidade → VO
    public static PessoaDTO pessoaToPessoaVo(Pessoa pessoa) {
        if (pessoa == null) return null;
        PessoaDTO vo = new PessoaDTO();
        vo.setId(pessoa.getId());
        vo.setNome(pessoa.getNome());
        vo.setCpf(pessoa.getCpf());
        vo.setTelefone(pessoa.getTelefone());
        vo.setSexo(pessoa.getSexo());
        vo.setParentesco(pessoa.getParentesco());
        vo.setRendaMensal(pessoa.getRendaMensal());
        vo.setDataNascimento(pessoa.getDataNascimento());
        vo.setNumeroRg(pessoa.getNumeroRg());
        vo.setOrgaoExpeditorRg(pessoa.getOrgaoExpeditorRg());
        vo.setDataExpedicaoRg(pessoa.getDataExpedicaoRg());
        vo.setReferencia(pessoa.isReferencia());
        vo.setEndereco(enderecoToEnderecoVo(pessoa.getEndereco()));
        return vo;
    }

    // Endereco VO → Entidade
    public static Endereco enderecoVoToEndereco(EnderecoDTO vo) {
        if (vo == null) return null;
        Endereco endereco = new Endereco();
        endereco.setLogradouro(vo.getLogradouro());
        endereco.setNumero(vo.getNumero());
        endereco.setBairro(vo.getBairro());
        endereco.setCidade(vo.getCidade());
        endereco.setUf(vo.getUf());
        endereco.setCep(vo.getCep());
        endereco.setPontoReferencia(vo.getPontoReferencia());
        endereco.setLocalizacaoDomicilio(vo.getLocalizacaoDomicilio());
        return endereco;
    }

    // Endereco Entidade → VO
    public static EnderecoDTO enderecoToEnderecoVo(Endereco endereco) {
        if (endereco == null) return null;
        EnderecoDTO vo = new EnderecoDTO();
        vo.setLogradouro(endereco.getLogradouro());
        vo.setNumero(endereco.getNumero());
        vo.setBairro(endereco.getBairro());
        vo.setCidade(endereco.getCidade());
        vo.setUf(endereco.getUf());
        vo.setCep(endereco.getCep());
        vo.setPontoReferencia(endereco.getPontoReferencia());
        vo.setLocalizacaoDomicilio(endereco.getLocalizacaoDomicilio());
        return vo;
    }
}
