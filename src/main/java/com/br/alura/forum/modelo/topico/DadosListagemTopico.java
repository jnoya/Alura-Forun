
package com.br.alura.forum.modelo.topico;

import java.time.LocalDateTime;

public record DadosListagemTopico(String titulo, String mensagem, LocalDateTime data_criacao, StatusTopico status,
		String autor, String curso) {

	public DadosListagemTopico(Topico topico) {

		this(topico.getTitulo(), topico.getMensagem(), topico.getDataCriacao(), topico.getStatus(),
				topico.getAutor().getNome(), topico.getCurso().getNome());

	}

}
