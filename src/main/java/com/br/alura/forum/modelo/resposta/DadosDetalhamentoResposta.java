
package com.br.alura.forum.modelo.resposta;

import java.time.LocalDateTime;

public record DadosDetalhamentoResposta(Long id, String mensagem, String topico, LocalDateTime dataCriacao,
		String autor, Boolean solucao) {

	public DadosDetalhamentoResposta(Resposta resposta) {

		this(resposta.getId(), resposta.getMensagem(), resposta.getTopico().getTitulo(), resposta.getDataCriacao(),
				resposta.getAutor().getNome(), resposta.getSolucao());

	}

}
