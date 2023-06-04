
package com.br.alura.forum.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
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
import com.br.alura.forum.modelo.usuario.DadosAtualizacaoUsuario;
import com.br.alura.forum.modelo.usuario.DadosCadastroUsuario;
import com.br.alura.forum.modelo.usuario.DadosDetalhamentoUsuario;
import com.br.alura.forum.modelo.usuario.DadosListagemUsuario;
import com.br.alura.forum.modelo.usuario.Usuario;
import com.br.alura.forum.modelo.usuario.UsuarioRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

	@Autowired
	private UsuarioRepository repository;

	public Usuario getUsuarioComEmail(String email) {

		return repository.getReferencByEmail(email);

	}

	@Operation(summary = "Cadastra um usuario")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Usuario cadastrado",
					content = { @Content(mediaType = "application/json",
							schema = @Schema(implementation = Usuario.class)) }),
			@ApiResponse(responseCode = "400", description = "Dados do usuario invalidos", content = @Content),
			@ApiResponse(responseCode = "404", description = "Usuario não cadastrado", content = @Content),
			@ApiResponse(responseCode = "403", description = "Acesso não autorizado", content = @Content) })
	@PostMapping
	@Transactional
	public ResponseEntity<DadosDetalhamentoUsuario> cadastrar(@RequestBody @Valid DadosCadastroUsuario dados,
			UriComponentsBuilder uriBuilder) {

		System.out.println("UsuarioController");

		var usuario = new Usuario(dados);
		String salt = BCrypt.gensalt();
		var bcript = BCrypt.hashpw(dados.senha(), salt);
		usuario.setSenha(bcript);
		repository.save(usuario);

		var uri = uriBuilder.path("/usuarios/{id}").buildAndExpand(usuario.getId()).toUri();
		return ResponseEntity.created(uri).body(new DadosDetalhamentoUsuario(usuario));

	}

	@Operation(summary = "Lista os usuarios existentes")
	@ApiResponses(
			value = {
					@ApiResponse(responseCode = "200", description = "Listado com exito",
							content = { @Content(mediaType = "application/json",
									schema = @Schema(implementation = Curso.class)) }),
					@ApiResponse(responseCode = "400", description = "Tabela Usuarios não existe", content = @Content),
					@ApiResponse(responseCode = "404", description = "Não há usuarios para listar", content = @Content),
					@ApiResponse(responseCode = "403", description = "Acesso não autorizado", content = @Content) })
	@GetMapping
	public Page<DadosListagemUsuario> listar(@PageableDefault(size = 10, sort = { "nome" }) Pageable paginacao) {

		return repository.findAll(paginacao).map(DadosListagemUsuario::new);

	}

	@Operation(summary = "Busca um usuario por seu Id")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Usuario encontrado",
					content = { @Content(mediaType = "application/json",
							schema = @Schema(implementation = Usuario.class)) }),
			@ApiResponse(responseCode = "400", description = "Id do usuario invalido", content = @Content),
			@ApiResponse(responseCode = "404", description = "Usuario não encontrado", content = @Content),
			@ApiResponse(responseCode = "403", description = "Acesso não autorizado", content = @Content) })
	@GetMapping("/{id}")
	public ResponseEntity<DadosDetalhamentoUsuario> detalhar(@PathVariable Long id) {

		var usuario = repository.getReferenceById(id);
		return ResponseEntity.ok(new DadosDetalhamentoUsuario(usuario));

	}

	@Operation(summary = "Atualiza um usuario")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Usuario atualizado",
					content = { @Content(mediaType = "application/json",
							schema = @Schema(implementation = Usuario.class)) }),
			@ApiResponse(responseCode = "400", description = "Dados do usuario invalidos", content = @Content),
			@ApiResponse(responseCode = "404", description = "Usuario não encontrado", content = @Content),
			@ApiResponse(responseCode = "403", description = "Acesso não autorizado", content = @Content) })
	@PutMapping
	@Transactional
	public ResponseEntity<DadosDetalhamentoUsuario> atualizar(@RequestBody @Valid DadosAtualizacaoUsuario dados) {

		var usuario = repository.getReferenceById(dados.id());

		usuario.atualizarInformacoes(dados);
		return ResponseEntity.ok(new DadosDetalhamentoUsuario(usuario));

	}

	@Operation(summary = "Elimina um usuario por seu Id")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Usuario eliminado",
					content = { @Content(mediaType = "application/json",
							schema = @Schema(implementation = Usuario.class)) }),
			@ApiResponse(responseCode = "400", description = "Id do usuario invalido", content = @Content),
			@ApiResponse(responseCode = "404", description = "Usuario não encontrado", content = @Content),
			@ApiResponse(responseCode = "403", description = "Acesso não autorizado", content = @Content) })
	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity<?> excluir(@PathVariable Long id) {

		repository.deleteById(id);
		return ResponseEntity.noContent().build();

	}

}
