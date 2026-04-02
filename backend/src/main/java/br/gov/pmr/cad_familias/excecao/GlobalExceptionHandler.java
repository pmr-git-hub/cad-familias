package br.gov.pmr.cad_familias.excecao;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UsuarioOuSenhaInvalidoException.class)
    public ResponseEntity<ErrorResponse> handleUsuarioOuSenhaInvalido(UsuarioOuSenhaInvalidoException exception){
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.UNAUTHORIZED.value(),
                "Acesso não autorizado",
                exception.getMessage()
        );
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(errorResponse);
    }

    @ExceptionHandler(FamiliaNaoEncontradaException.class)
    public ResponseEntity<ErrorResponse> handlerFamiliaNaoEncontrada(FamiliaNaoEncontradaException exception){
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.FOUND.value(),
                "Família não encontrada.",
                exception.getMessage()
        );
        return ResponseEntity
                .status(HttpStatus.FOUND)
                .body(errorResponse);
    }
}

