package br.com.pmr.cad_familias.domain;

public record LoginResponseDTO(
        Long id,
        String nomeUsuario,
        String token,
        String refreshToken
) {
}
