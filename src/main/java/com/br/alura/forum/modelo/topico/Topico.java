
package com.br.alura.forum.modelo.topico;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.br.alura.forum.modelo.curso.Curso;
import com.br.alura.forum.modelo.resposta.Resposta;
import com.br.alura.forum.modelo.usuario.Usuario;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "topicos")
@Entity(name = "Topico")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Topico {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String titulo;

	private String mensagem;

	@Column(name = "data_criacao")
	private LocalDateTime dataCriacao = LocalDateTime.now();

	@Enumerated(EnumType.STRING)
	private StatusTopico status = StatusTopico.NAO_RESPONDIDO;

	@ManyToOne(fetch = FetchType.LAZY)
	private Usuario autor;

	@ManyToOne(fetch = FetchType.LAZY)
	private Curso curso;

	@OneToMany(mappedBy = "topico", cascade = CascadeType.ALL)
	private List<Resposta> respostas = new ArrayList<>();

	private Boolean ativo;

	public Topico(String titulo, String mensagem, Usuario autor, Curso curso) {

		this.titulo = titulo;
		this.mensagem = mensagem;
		this.autor = autor;
		this.curso = curso;
		this.ativo = true;

	}

	public void atualizarInformacoes(DadosAtualizacaoTopico dados, Usuario autor, Curso curso) {

		if (dados.titulo() != null) {

			this.titulo = dados.titulo();

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

		if (curso != null) {

			this.curso = curso;

		}

	}

	public void inativar() {

		this.ativo = false;

	}

	public void adicionarResposta(Resposta resposta) {

		resposta.setTopico(this);
		this.respostas.add(resposta);

	}

}
