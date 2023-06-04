
package com.br.alura.forum.modelo.resposta;

import java.time.LocalDateTime;

public record DadosListagemResposta(String mensagem, LocalDateTime data_criacao, String topico, String autor,
		Boolean solucao) {

	public DadosListagemResposta(Resposta resposta) {

		this(resposta.getMensagem(), resposta.getDataCriacao(), resposta.getTopico().getTitulo(),
				resposta.getAutor().getNome(), resposta.getSolucao());

	}

}
