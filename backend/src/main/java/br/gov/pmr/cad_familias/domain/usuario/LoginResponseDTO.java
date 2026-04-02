package br.gov.pmr.cad_familias.domain.usuario;

public record LoginResponseDTO(
        Long id,
        String nomeUsuario,
        String token,
        String refreshToken
) {
}
