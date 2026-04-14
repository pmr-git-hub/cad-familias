package br.gov.pmr.cad_familias.service.tecnico;

import br.gov.pmr.cad_familias.dto.tecnico.TecnicoDTO;
import br.gov.pmr.cad_familias.domain.tecnico.Tecnico;
import br.gov.pmr.cad_familias.excecao.TecnicoNaoEncontradoException;
import br.gov.pmr.cad_familias.mapper.tecnico.TecnicoMapper;
import br.gov.pmr.cad_familias.repository.tecnico.TecnicoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TecnicoService {

    private final TecnicoRepository tecnicoRepository;

    public TecnicoService(TecnicoRepository tecnicoRepository) {
        this.tecnicoRepository = tecnicoRepository;
    }

    public List<TecnicoDTO> listarTecnicos() {
        return TecnicoMapper.listToDTO(tecnicoRepository.findAll());
    }

    public TecnicoDTO buscarPorId(Long id) {
        return tecnicoRepository.findById(id)
                .map(TecnicoMapper::tecnicoToDTO)
                .orElseThrow(TecnicoNaoEncontradoException::new);
    }

    @Transactional
    public TecnicoDTO criarTecnico(TecnicoDTO dto, Long usuarioId) {
        Tecnico tecnico = TecnicoMapper.dtoToTecnico(dto);
        tecnico.setCriadoPor(usuarioId);
        tecnico.setAtualizadoPor(usuarioId);

        return TecnicoMapper.tecnicoToDTO(tecnicoRepository.save(tecnico));
    }

    @Transactional
    public TecnicoDTO atualizarTecnico(Long id, TecnicoDTO dto, Long usuarioId) {
        Tecnico tecnico = tecnicoRepository.findById(id)
                .orElseThrow(TecnicoNaoEncontradoException::new);

        if (dto.getNome() != null) {
            tecnico.setNome(dto.getNome());
        }
        if (dto.getCpf() != null) {
            tecnico.setCpf(dto.getCpf());
        }
        if (dto.getRegistroProfissional() != null) {
            tecnico.setRegistroProfissional(dto.getRegistroProfissional());
        }
        if (dto.getEspecialidade() != null) {
            tecnico.setEspecialidade(dto.getEspecialidade());
        }
        if (dto.isAtivo() != tecnico.isAtivo()) {
            tecnico.setAtivo(dto.isAtivo());
        }

        tecnico.setAtualizadoPor(usuarioId);

        return TecnicoMapper.tecnicoToDTO(tecnicoRepository.save(tecnico));
    }

    @Transactional
    public void desativar(Long id) {
        Tecnico tecnico = tecnicoRepository.findById(id)
                .orElseThrow(TecnicoNaoEncontradoException::new);
        tecnico.setAtivo(false);
        tecnicoRepository.save(tecnico);
    }
}
