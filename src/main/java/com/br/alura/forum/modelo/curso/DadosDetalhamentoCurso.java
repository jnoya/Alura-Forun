
package com.br.alura.forum.modelo.curso;

public record DadosDetalhamentoCurso(Long id, String nome, String categoria) {

	public DadosDetalhamentoCurso(Curso curso) {

		this(curso.getId(), curso.getNome(), curso.getCategoria());

	}

}
