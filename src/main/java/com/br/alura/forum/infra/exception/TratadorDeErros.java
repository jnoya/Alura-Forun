
package com.br.alura.forum.infra.exception;

import java.sql.SQLException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.persistence.EntityNotFoundException;

@RestControllerAdvice
public class TratadorDeErros {

	@ExceptionHandler(EntityNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ResponseEntity<?> tratarErro404() {

		return ResponseEntity.notFound().build();

	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<List<DadosErroValidacao>> tratarErro400(MethodArgumentNotValidException ex) {

		var erros = ex.getFieldErrors();
		return ResponseEntity.badRequest().body(erros.stream().map(DadosErroValidacao::new).toList());

	}

	private record DadosErroValidacao(String campo, String mensagem) {

		public DadosErroValidacao(FieldError erro) {

			this(erro.getField(), erro.getDefaultMessage());

		}

	}

	@ExceptionHandler(AccessDeniedException.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public ResponseEntity<String> tratarErroAcessoNegado() {

		return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acesso negado");

	}

	@ExceptionHandler(SQLException.class)
	public ResponseEntity<String> tratarErroSql(SQLException ex) {

		var erros = ex.getMessage();
		return ResponseEntity.badRequest().body(erros);

	}

}
