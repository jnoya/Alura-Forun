
package com.br.alura.forum.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.br.alura.forum.modelo.resposta.DadosAtualizacaoResposta;
import com.br.alura.forum.modelo.resposta.DadosCadastroResposta;
import com.br.alura.forum.modelo.resposta.DadosDetalhamentoResposta;
import com.br.alura.forum.modelo.resposta.DadosListagemResposta;
import com.br.alura.forum.modelo.resposta.Resposta;
import com.br.alura.forum.modelo.resposta.RespostaRepository;
import com.br.alura.forum.modelo.topico.Topico;
import com.br.alura.forum.modelo.usuario.Usuario;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/respostas")
public class RespostaController {

	@Autowired
	private RespostaRepository repository;

	@Autowired
	private UsuarioController usuarioController;

	@Autowired
	private TopicoController topicoController;

	@Operation(summary = "Cadastra uma resposta")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Resposta cadastrado",
					content = { @Content(mediaType = "application/json",
							schema = @Schema(implementation = Resposta.class)) }),
			@ApiResponse(responseCode = "400", description = "Dados da resposta invalidos", content = @Content),
			@ApiResponse(responseCode = "404", description = "Resposta não cadastrada", content = @Content) })

	@PostMapping
	@Transactional
	public ResponseEntity<DadosDetalhamentoResposta> cadastrar(@RequestBody @Valid DadosCadastroResposta dados,
			UriComponentsBuilder uriBuilder) {

		Usuario autor = usuarioController.getUsuarioComEmail(dados.autor());
		Topico topico = topicoController.getTopicoComTitulo(dados.topico());
		String mensagem = dados.mensagem();

		var resposta = new Resposta(mensagem, autor, topico);
		repository.save(resposta);

		var uri = uriBuilder.path("/respostas/{id}").buildAndExpand(resposta.getId()).toUri();
		return ResponseEntity.created(uri).body(new DadosDetalhamentoResposta(resposta));

	}

	@Operation(summary = "Lista as respostas existentes para um tópico")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Listado com exito",
					content = { @Content(mediaType = "application/json",
							schema = @Schema(implementation = Resposta.class)) }),
			@ApiResponse(responseCode = "400", description = "Tabela Respostas não existe", content = @Content),
			@ApiResponse(responseCode = "404", description = "Não há respostas para listar", content = @Content),
			@ApiResponse(responseCode = "403", description = "Acesso não autorizado", content = @Content) })
	@GetMapping
	public Page<DadosListagemResposta>
			listar(@PageableDefault(size = 10, sort = { "dataCriacao" }) Pageable paginacao) {

		return repository.findAll(paginacao).map(DadosListagemResposta::new);

	}

	@Operation(summary = "Busca uma resposta por seu Id")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Resposta encontrada",
					content = { @Content(mediaType = "application/json",
							schema = @Schema(implementation = Resposta.class)) }),
			@ApiResponse(responseCode = "400", description = "Id da resposta invalido", content = @Content),
			@ApiResponse(responseCode = "404", description = "Resposta não encontrada", content = @Content),
			@ApiResponse(responseCode = "403", description = "Acesso não autorizado", content = @Content) })
	@GetMapping("/{id}")
	public ResponseEntity<DadosDetalhamentoResposta> detalhar(@PathVariable Long id) {

		var resposta = repository.getReferenceById(id);
		return ResponseEntity.ok(new DadosDetalhamentoResposta(resposta));

	}

	@Operation(summary = "Atualiza uma resposta")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Resposta atualizado",
					content = { @Content(mediaType = "application/json",
							schema = @Schema(implementation = Resposta.class)) }),
			@ApiResponse(responseCode = "400", description = "Dados da resposta invalidos", content = @Content),
			@ApiResponse(responseCode = "404", description = "Resposta não encontrado", content = @Content),
			@ApiResponse(responseCode = "403", description = "Acesso não autorizado", content = @Content) })
	@PutMapping
	@Transactional
	public ResponseEntity<DadosDetalhamentoResposta> atualizar(@RequestBody @Valid DadosAtualizacaoResposta dados) {

		var resposta = repository.getReferenceById(dados.id());
		Usuario autor = new Usuario();
		Topico topico = new Topico();

		if (dados.autor() != null) {

			autor = usuarioController.getUsuarioComEmail(dados.autor());

		} else {

			autor = null;

		}

		if (dados.topico() != null) {

			topico = topicoController.getTopicoComTitulo(dados.topico());

		} else {

			topico = null;

		}

		resposta.atualizarInformacoes(dados, autor, topico);
		return ResponseEntity.ok(new DadosDetalhamentoResposta(resposta));

	}

	@Operation(summary = "Elimina uma resposta por seu Id")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Resposta eliminada",
					content = { @Content(mediaType = "application/json",
							schema = @Schema(implementation = Resposta.class)) }),
			@ApiResponse(responseCode = "400", description = "Id da resposta invalido", content = @Content),
			@ApiResponse(responseCode = "404", description = "Resposta não encontrada", content = @Content),
			@ApiResponse(responseCode = "403", description = "Acesso não autorizado", content = @Content) })
	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity<?> excluir(@PathVariable Long id) {

		repository.deleteById(id);
		return ResponseEntity.noContent().build();

	}

}
