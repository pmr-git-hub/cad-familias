package br.gov.pmr.cad_familias.mapper.usuario;

import br.gov.pmr.cad_familias.dto.tecnico.TecnicoResumoDTO;
import br.gov.pmr.cad_familias.dto.usuario.CriarUsuarioDTO;
import br.gov.pmr.cad_familias.dto.usuario.UsuarioDTO;
import br.gov.pmr.cad_familias.domain.tecnico.Tecnico;
import br.gov.pmr.cad_familias.domain.usuario.Usuario;

import java.util.List;

public class UsuarioMapper {

    public static UsuarioDTO usuarioToUsuarioVO(Usuario usuario) {
        if (usuario == null) return null;

        UsuarioDTO vo = new UsuarioDTO();
        vo.setId(usuario.getId());
        vo.setUsername(usuario.getUsername());
        vo.setPerfil(usuario.getPerfil());
        vo.setAtivo(usuario.isAtivo());
        vo.setUltimoAcesso(usuario.getUltimoAcesso());

        if (usuario.getTecnico() != null) {
            TecnicoResumoDTO tecnicoResumo = new TecnicoResumoDTO();
            tecnicoResumo.setId(usuario.getTecnico().getId());
            tecnicoResumo.setNome(usuario.getTecnico().getNome());
            tecnicoResumo.setEspecialidade(usuario.getTecnico().getEspecialidade());
            vo.setTecnico(tecnicoResumo);
        }

        return vo;
    }

    public static Usuario criarUsuarioDTOToUsuario(CriarUsuarioDTO dto, Tecnico tecnico) {
        if (dto == null) return null;

        Usuario usuario = new Usuario();
        usuario.setUsername(dto.getUsername());
        usuario.setPassword(dto.getPassword()); // encoding fica no service
        usuario.setPerfil(dto.getPerfil());
        usuario.setTecnico(tecnico);

        return usuario;
    }

    public static List<UsuarioDTO> listaUsuariosToVO(List<Usuario> usuarios) {
        return usuarios.stream()
                .map(UsuarioMapper::usuarioToUsuarioVO)
                .toList();
    }
}
