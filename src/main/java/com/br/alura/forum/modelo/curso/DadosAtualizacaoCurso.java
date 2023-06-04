
package com.br.alura.forum.modelo.curso;

import jakarta.validation.constraints.NotNull;

public record DadosAtualizacaoCurso(

		@NotNull Long id,

		String nome,

		String categoria

) {}
