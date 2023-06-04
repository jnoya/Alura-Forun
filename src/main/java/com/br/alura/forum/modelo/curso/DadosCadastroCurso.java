
package com.br.alura.forum.modelo.curso;

import jakarta.validation.constraints.NotBlank;

public record DadosCadastroCurso(

		@NotBlank(message = "Nome é obrigatório") String nome,

		@NotBlank(message = "Categoria é obrigatório") String categoria

) {}
