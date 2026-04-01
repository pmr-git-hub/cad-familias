package br.com.pmr.cad_familias.domain;

import jakarta.validation.constraints.NotBlank;
import org.aspectj.weaver.ast.Not;

public record DadosLogin(@NotBlank String nomeUsuario, @NotBlank String senha) {
}
