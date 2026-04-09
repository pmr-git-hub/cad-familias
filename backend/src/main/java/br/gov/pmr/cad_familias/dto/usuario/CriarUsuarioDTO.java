package br.gov.pmr.cad_familias.dto.usuario;

import br.gov.pmr.cad_familias.domain.usuario.Perfil;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class CriarUsuarioDTO implements Serializable {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotNull
    private Perfil perfil;

    @NotNull
    private Long tecnicoId;

    @NotNull
    private boolean ativo;
}
