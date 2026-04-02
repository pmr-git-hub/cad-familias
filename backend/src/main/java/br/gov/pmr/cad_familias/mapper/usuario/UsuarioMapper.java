package br.gov.pmr.cad_familias.mapper.usuario;

import br.gov.pmr.cad_familias.VO.tecnico.TecnicoResumoVO;
import br.gov.pmr.cad_familias.VO.usuario.CriarUsuarioDTO;
import br.gov.pmr.cad_familias.VO.usuario.UsuarioVO;
import br.gov.pmr.cad_familias.domain.tecnico.Tecnico;
import br.gov.pmr.cad_familias.domain.usuario.Usuario;

import java.util.List;

public class UsuarioMapper {

    public static UsuarioVO usuarioToUsuarioVO(Usuario usuario) {
        if (usuario == null) return null;

        UsuarioVO vo = new UsuarioVO();
        vo.setId(usuario.getId());
        vo.setUsername(usuario.getUsername());
        vo.setNome(usuario.getNome());
        vo.setPerfil(usuario.getPerfil());
        vo.setAtivo(usuario.isAtivo());
        vo.setUltimoAcesso(usuario.getUltimoAcesso());

        if (usuario.getTecnico() != null) {
            TecnicoResumoVO tecnicoResumo = new TecnicoResumoVO();
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
        usuario.setNome(dto.getNome());
        usuario.setPassword(dto.getPassword()); // encoding fica no service
        usuario.setPerfil(dto.getPerfil());
        usuario.setTecnico(tecnico);

        return usuario;
    }

    public static List<UsuarioVO> listaUsuariosToVO(List<Usuario> usuarios) {
        return usuarios.stream()
                .map(UsuarioMapper::usuarioToUsuarioVO)
                .toList();
    }
}
