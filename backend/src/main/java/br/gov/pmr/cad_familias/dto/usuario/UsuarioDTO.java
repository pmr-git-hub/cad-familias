package br.gov.pmr.cad_familias.dto.usuario;

import br.gov.pmr.cad_familias.dto.tecnico.TecnicoResumoDTO;
import br.gov.pmr.cad_familias.domain.usuario.Perfil;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Setter
@Getter
public class UsuarioDTO implements Serializable {

    private Long id;
    private String username;
    private Perfil perfil;
    private boolean ativo;
    private LocalDateTime ultimoAcesso;
    private TecnicoResumoDTO tecnico;

}
