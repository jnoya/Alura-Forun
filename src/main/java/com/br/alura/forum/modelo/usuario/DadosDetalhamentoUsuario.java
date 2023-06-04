
package com.br.alura.forum.modelo.usuario;

public record DadosDetalhamentoUsuario(Long id, String nome, String email) {

	public DadosDetalhamentoUsuario(Usuario usuario) {

		this(usuario.getId(), usuario.getNome(), usuario.getEmail());

	}

}
