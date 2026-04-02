package br.gov.pmr.cad_familias.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import br.gov.pmr.cad_familias.VO.FamiliaVO;
import br.gov.pmr.cad_familias.domain.familia.Familia;
import br.gov.pmr.cad_familias.domain.familia.Pessoa;

public class FamiliaMapper {
    
    public static Familia familiaVoToFamilia(FamiliaVO familiaVO) {
        if (familiaVO == null) {
            throw new IllegalArgumentException("FamiliaVO não pode ser null.");
        }
        Familia familia = new Familia();
//        familia.setId(familiaVO.getId());
        familia.setMembrosDaFamilia(new ArrayList<>());

        List<Pessoa> membros = Optional.ofNullable(familiaVO.getMembrosDaFamilia()).orElse(new ArrayList<>());

        for (Pessoa pessoa : membros) {
            if (pessoa.isReferencia()) {
                throw new IllegalArgumentException("A lista de membros não deve incluir a pessoa de referência.");
            }
            pessoa.setFamilia(familia);
            if (pessoa.getCpf() != null && pessoa.getCpf().trim().isEmpty()) {
                pessoa.setCpf(null);
            }
            familia.getMembrosDaFamilia().add(pessoa);
        }

        Pessoa pessoaReferencia = familiaVO.getPessoaReferencia();
        pessoaReferencia.setFamilia(familia);
        familia.getMembrosDaFamilia().add(pessoaReferencia);

//        familia.setRendaFamiliar(familiaVO.getRendaFamiliar());
        return familia;
    }

    public static FamiliaVO familiaToFamiliaVo(Familia familia) {
        if (familia == null) {
            throw new IllegalArgumentException("Familia não pode ser null.");
        }
        FamiliaVO familiaVO = new FamiliaVO();
//        familiaVO.setId(familia.getId());
//        familiaVO.setRendaFamiliar(familia.getRendaFamiliar());
        List<Pessoa> membrosFamilia = new ArrayList<>();
        Pessoa referencia = null;

        familia.getMembrosDaFamilia().forEach(membro -> {
            if(membro.isReferencia()) {
                familiaVO.setPessoaReferencia(membro);
            } else {
                membrosFamilia.add(membro);
            }
        });
        familiaVO.setMembrosDaFamilia(membrosFamilia);
        return familiaVO;
    }
    
    public static List<FamiliaVO> listaFamiliasToListaFamiliasVO(List<Familia> listFamilias) {
        List<FamiliaVO> familiasVO = new ArrayList<>();
        listFamilias.forEach(familia -> {
            familiasVO.add(familiaToFamiliaVo(familia));
        });
        return familiasVO;
    }
    
    public static List<Familia> listaFamiliasVOToListaFamilias(List<FamiliaVO> listFamiliasVO) {
        List<Familia> familias = new ArrayList<>();
        listFamiliasVO.forEach(familiaVO -> {
        	familias.add(familiaVoToFamilia(familiaVO));
        });
        return familias;
    }
}
