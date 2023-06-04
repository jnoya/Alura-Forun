
package com.br.alura.forum.modelo.resposta;

import java.time.LocalDateTime;

import com.br.alura.forum.modelo.topico.Topico;
import com.br.alura.forum.modelo.usuario.Usuario;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "respostas")
@Entity(name = "Resposta")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Resposta {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String mensagem;

	@ManyToOne(fetch = FetchType.LAZY)
	private Topico topico;

	@Column(name = "data_criacao")
	private LocalDateTime dataCriacao = LocalDateTime.now();

	@ManyToOne(fetch = FetchType.LAZY)
	private Usuario autor;

	private Boolean solucao = false;

	public void setTopico(Topico topico) {

		this.topico = topico;

	}

	public Resposta(@Valid String mensagem, Usuario autor, Topico topico) {

		this.mensagem = mensagem;
		this.topico = topico;
		this.autor = autor;

	}

	public void atualizarInformacoes(DadosAtualizacaoResposta dados, Usuario autor, Topico topico) {

		if (dados.solucao() != null) {

			this.solucao = dados.solucao();

		}

		if (dados.mensagem() != null) {

			this.mensagem = dados.mensagem();

		}

		if (dados.dataAtualizacao() != null) {

			this.dataCriacao = dados.dataAtualizacao();

		}

		if (autor != null) {

			this.autor = autor;

		}

		if (topico != null) {

			this.topico = topico;

		}

	}

}
