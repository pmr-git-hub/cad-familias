package br.gov.pmr.cad_familias.dto.usuario;

import br.gov.pmr.cad_familias.domain.usuario.Perfil;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class AtualizarUsuarioDTO implements Serializable {

    @NotBlank
    private String username;

    private String password;

    @NotNull
    private Perfil perfil;

    @NotNull
    private Long tecnicoId;

    @NotNull
    private Boolean ativo;
}
