
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

import com.br.alura.forum.modelo.curso.Curso;
import com.br.alura.forum.modelo.topico.DadosAtualizacaoTopico;
import com.br.alura.forum.modelo.topico.DadosCadastroTopico;
import com.br.alura.forum.modelo.topico.DadosDetalhamentoTopico;
import com.br.alura.forum.modelo.topico.DadosListagemTopico;
import com.br.alura.forum.modelo.topico.Topico;
import com.br.alura.forum.modelo.topico.TopicoRepository;
import com.br.alura.forum.modelo.usuario.Usuario;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/topicos")
public class TopicoController {

	@Autowired
	private TopicoRepository repository;

	@Autowired
	private UsuarioController usuarioController;

	@Autowired
	private CursoController cursoController;

	@Operation(summary = "Cadastra um tópico")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Tópico cadastrado",
					content = { @Content(mediaType = "application/json",
							schema = @Schema(implementation = Topico.class)) }),
			@ApiResponse(responseCode = "400", description = "Dados do tópico invalido", content = @Content),
			@ApiResponse(responseCode = "404", description = "Tópico não cadastrado", content = @Content),
			@ApiResponse(responseCode = "403", description = "Acesso não autorizado", content = @Content) })
	@PostMapping
	@Transactional
	public ResponseEntity<DadosDetalhamentoTopico> cadastrar(@RequestBody @Valid DadosCadastroTopico dados,
			UriComponentsBuilder uriBuilder) {

		Usuario autor = usuarioController.getUsuarioComEmail(dados.autor());
		Curso curso = cursoController.getCursoComNome(dados.curso());

		Topico topico = new Topico(dados.titulo(), dados.mensagem(), autor, curso);
		repository.save(topico);

		var uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
		return ResponseEntity.created(uri).body(new DadosDetalhamentoTopico(topico));

	}

	@Operation(summary = "Lista os tópicos existentes")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Listado com exito",
					content = { @Content(mediaType = "application/json",
							schema = @Schema(implementation = Topico.class)) }),
			@ApiResponse(responseCode = "400", description = "Tabela Tópicos não existe", content = @Content),
			@ApiResponse(responseCode = "404", description = "Não há tópicos para listar", content = @Content),
			@ApiResponse(responseCode = "403", description = "Acesso não autorizado", content = @Content) })
	@GetMapping
	public Page<DadosListagemTopico> listar(@PageableDefault(size = 10, sort = { "dataCriacao" }) Pageable paginacao) {

		return repository.findAll(paginacao).map(DadosListagemTopico::new);

	}

	@Operation(summary = "Atualiza um tópico")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Tópico atualizado",
					content = { @Content(mediaType = "application/json",
							schema = @Schema(implementation = Topico.class)) }),
			@ApiResponse(responseCode = "400", description = "Dados do tópico invalidos", content = @Content),
			@ApiResponse(responseCode = "404", description = "Tópico não encontrado", content = @Content),
			@ApiResponse(responseCode = "403", description = "Acesso não autorizado", content = @Content) })
	@PutMapping
	@Transactional
	public ResponseEntity<DadosDetalhamentoTopico> atualizar(@RequestBody @Valid DadosAtualizacaoTopico dados) {

		var topico = repository.getReferenceById(dados.id());
		Usuario autor = new Usuario();
		Curso curso = new Curso();

		if (dados.autor() != null) {

			autor = usuarioController.getUsuarioComEmail(dados.autor());

		} else {

			autor = null;

		}

		if (dados.curso() != null) {

			curso = cursoController.getCursoComNome(dados.curso());

		} else {

			curso = null;

		}

		topico.atualizarInformacoes(dados, autor, curso);
		return ResponseEntity.ok(new DadosDetalhamentoTopico(topico));

	}

	@Operation(summary = "Elimina um tópico por seu Id")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Tópico eliminado",
					content = { @Content(mediaType = "application/json",
							schema = @Schema(implementation = Topico.class)) }),
			@ApiResponse(responseCode = "400", description = "Id do tópico invalido", content = @Content),
			@ApiResponse(responseCode = "404", description = "Tópico não encontrado", content = @Content),
			@ApiResponse(responseCode = "403", description = "Acesso não autorizado", content = @Content) })
	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity<?> excluir(@PathVariable Long id) {

		repository.deleteById(id);
		return ResponseEntity.noContent().build();

	}

	@Operation(summary = "Busca um tópico por seu Id")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Tópico encontrado",
					content = { @Content(mediaType = "application/json",
							schema = @Schema(implementation = Topico.class)) }),
			@ApiResponse(responseCode = "400", description = "Id do tópico invalido", content = @Content),
			@ApiResponse(responseCode = "404", description = "Tópico não encontrado", content = @Content),
			@ApiResponse(responseCode = "403", description = "Acesso não autorizado", content = @Content) })
	@GetMapping("/{id}")
	public ResponseEntity<DadosDetalhamentoTopico> detalhar(@PathVariable Long id) {

		var topico = repository.getReferenceById(id);
		return ResponseEntity.ok(new DadosDetalhamentoTopico(topico));

	}

	public Topico getTopicoComTitulo(String titulo) {

		return repository.getReferencByTitulo(titulo);

	}

}
