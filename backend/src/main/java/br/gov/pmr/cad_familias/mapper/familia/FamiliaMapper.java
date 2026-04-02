package br.gov.pmr.cad_familias.mapper.familia;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import br.gov.pmr.cad_familias.VO.familia.EnderecoVO;
import br.gov.pmr.cad_familias.VO.familia.FamiliaVO;
import br.gov.pmr.cad_familias.VO.familia.PessoaVO;
import br.gov.pmr.cad_familias.domain.familia.Endereco;
import br.gov.pmr.cad_familias.domain.familia.Familia;
import br.gov.pmr.cad_familias.domain.familia.Pessoa;

public class FamiliaMapper {

    // VO → Entidade
    public static Familia familiaVoToFamilia(FamiliaVO familiaVO) {
        if (familiaVO == null) throw new IllegalArgumentException("FamiliaVO não pode ser null.");

        Familia familia = new Familia();
        familia.setMembrosDaFamilia(new ArrayList<>());

        // Mapeia membros comuns
        List<PessoaVO> membros = Optional.ofNullable(familiaVO.getMembrosDaFamilia()).orElse(new ArrayList<>());
        for (PessoaVO pessoaVO : membros) {
            Pessoa pessoa = pessoaVoToPessoa(pessoaVO);
            pessoa.setFamilia(familia);
            familia.getMembrosDaFamilia().add(pessoa);
        }

        // Mapeia pessoa de referência
        if (familiaVO.getPessoaReferencia() != null) {
            Pessoa referencia = pessoaVoToPessoa(familiaVO.getPessoaReferencia());
            referencia.setReferencia(true);
            referencia.setFamilia(familia);
            familia.getMembrosDaFamilia().add(referencia);
        }

        return familia;
    }

    // Entidade → VO
    public static FamiliaVO familiaToFamiliaVo(Familia familia) {
        if (familia == null) throw new IllegalArgumentException("Familia não pode ser null.");

        FamiliaVO familiaVO = new FamiliaVO();
        familiaVO.setId(familia.getId());

        List<PessoaVO> membros = new ArrayList<>();

        familia.getMembrosDaFamilia().forEach(membro -> {
            PessoaVO pessoaVO = pessoaToPessoaVo(membro);
            if (membro.isReferencia()) {
                familiaVO.setPessoaReferencia(pessoaVO);
            } else {
                membros.add(pessoaVO);
            }
        });

        familiaVO.setMembrosDaFamilia(membros);

        // Calcula renda familiar
        long rendaFamiliar = familia.getMembrosDaFamilia().stream()
                .mapToLong(p -> p.getRendaMensal() != null ? p.getRendaMensal() : 0L)
                .sum();
        familiaVO.setRendaFamiliar(rendaFamiliar);

        return familiaVO;
    }

    public static List<FamiliaVO> listaFamiliasToListaFamiliasVO(List<Familia> familias) {
        return familias.stream()
                .map(FamiliaMapper::familiaToFamiliaVo)
                .toList();
    }

    // Pessoa VO → Entidade
    public static Pessoa pessoaVoToPessoa(PessoaVO vo) {
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
    public static PessoaVO pessoaToPessoaVo(Pessoa pessoa) {
        if (pessoa == null) return null;
        PessoaVO vo = new PessoaVO();
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
    public static Endereco enderecoVoToEndereco(EnderecoVO vo) {
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
    public static EnderecoVO enderecoToEnderecoVo(Endereco endereco) {
        if (endereco == null) return null;
        EnderecoVO vo = new EnderecoVO();
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
