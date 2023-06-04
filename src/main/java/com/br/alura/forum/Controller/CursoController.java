
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
import com.br.alura.forum.modelo.curso.CursoRepository;
import com.br.alura.forum.modelo.curso.DadosAtualizacaoCurso;
import com.br.alura.forum.modelo.curso.DadosCadastroCurso;
import com.br.alura.forum.modelo.curso.DadosDetalhamentoCurso;
import com.br.alura.forum.modelo.curso.DadosListagemCurso;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/cursos")
public class CursoController {

	@Autowired
	private CursoRepository repository;

	public Curso getCursoComNome(String curso) {

		return repository.getReferenceByNome(curso);

	}

	@Operation(summary = "Cadastra um curso")
	@ApiResponses(
			value = {
					@ApiResponse(responseCode = "200", description = "Curso cadastrado",
							content = { @Content(mediaType = "application/json",
									schema = @Schema(implementation = Curso.class)) }),
					@ApiResponse(responseCode = "400", description = "Dados do curso invalido", content = @Content),
					@ApiResponse(responseCode = "404", description = "Curso não cadastrado", content = @Content),
					@ApiResponse(responseCode = "403", description = "Acesso não autorizado", content = @Content) })
	@PostMapping
	@Transactional
	public ResponseEntity<DadosDetalhamentoCurso> cadastrar(@RequestBody @Valid DadosCadastroCurso dados,
			UriComponentsBuilder uriBuilder) {

		var curso = new Curso(dados);
		repository.save(curso);

		var uri = uriBuilder.path("/cursos/{id}").buildAndExpand(curso.getId()).toUri();
		return ResponseEntity.created(uri).body(new DadosDetalhamentoCurso(curso));

	}

	@Operation(summary = "Lista os cursos existentes")
	@ApiResponses(
			value = {
					@ApiResponse(responseCode = "200", description = "Listado com exito",
							content = { @Content(mediaType = "application/json",
									schema = @Schema(implementation = Curso.class)) }),
					@ApiResponse(responseCode = "400", description = "Tabela Cursos não existe", content = @Content),
					@ApiResponse(responseCode = "404", description = "Não há cursos para listar", content = @Content),
					@ApiResponse(responseCode = "403", description = "Acesso não autorizado", content = @Content) })
	@GetMapping
	public Page<DadosListagemCurso> listar(@PageableDefault(size = 10, sort = { "nome" }) Pageable paginacao) {

		return repository.findAll(paginacao).map(DadosListagemCurso::new);

	}

	@Operation(summary = "Busca um curso por seu Id")
	@ApiResponses(
			value = {
					@ApiResponse(responseCode = "200", description = "Curso encontrado",
							content = { @Content(mediaType = "application/json",
									schema = @Schema(implementation = Curso.class)) }),
					@ApiResponse(responseCode = "400", description = "Id do curso invalido", content = @Content),
					@ApiResponse(responseCode = "404", description = "Curso não encontrado", content = @Content),
					@ApiResponse(responseCode = "403", description = "Acesso não autorizado", content = @Content) })
	@GetMapping("/{id}")
	public ResponseEntity<DadosDetalhamentoCurso> detalhar(@PathVariable Long id) {

		var curso = repository.getReferenceById(id);
		return ResponseEntity.ok(new DadosDetalhamentoCurso(curso));

	}

	@Operation(summary = "Atualiza um curso")
	@ApiResponses(
			value = {
					@ApiResponse(responseCode = "200", description = "Curso atualizado",
							content = { @Content(mediaType = "application/json",
									schema = @Schema(implementation = Curso.class)) }),
					@ApiResponse(responseCode = "400", description = "Dados do curso invalidos", content = @Content),
					@ApiResponse(responseCode = "404", description = "Curso não encontrado", content = @Content),
					@ApiResponse(responseCode = "403", description = "Acesso não autorizado", content = @Content) })
	@PutMapping
	@Transactional
	public ResponseEntity<DadosDetalhamentoCurso> atualizar(@RequestBody @Valid DadosAtualizacaoCurso dados) {

		var curso = repository.getReferenceById(dados.id());

		curso.atualizarInformacoes(dados);
		return ResponseEntity.ok(new DadosDetalhamentoCurso(curso));

	}

	@Operation(summary = "Elimina um curso por seu Id")
	@ApiResponses(
			value = {
					@ApiResponse(responseCode = "200", description = "Curso eliminado",
							content = { @Content(mediaType = "application/json",
									schema = @Schema(implementation = Curso.class)) }),
					@ApiResponse(responseCode = "400", description = "Id do curso invalido", content = @Content),
					@ApiResponse(responseCode = "404", description = "Curso não encontrado", content = @Content),
					@ApiResponse(responseCode = "403", description = "Acesso não autorizado", content = @Content) })
	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity<?> excluir(@PathVariable Long id) {

		repository.deleteById(id);
		return ResponseEntity.noContent().build();

	}

}
