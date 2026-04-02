package br.gov.pmr.cad_familias.domain.usuario;

import jakarta.validation.constraints.NotBlank;

public record DadosLogin(@NotBlank String nomeUsuario, @NotBlank String senha) {
}
