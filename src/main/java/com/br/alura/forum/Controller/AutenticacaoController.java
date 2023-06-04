
package com.br.alura.forum.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.br.alura.forum.infra.security.DadosTokenJWT;
import com.br.alura.forum.infra.security.TokenService;
import com.br.alura.forum.modelo.usuario.DadosAutenticacao;
import com.br.alura.forum.modelo.usuario.Usuario;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/login")
public class AutenticacaoController {

	@Autowired
	private AuthenticationManager manager;

	@Autowired
	private TokenService tokenService;

	@PostMapping
	public ResponseEntity<DadosTokenJWT> efetuarLogin(@RequestBody @Valid DadosAutenticacao dados) {

		var authenticationToken = new UsernamePasswordAuthenticationToken(dados.email(), dados.senha());
		var authentication = manager.authenticate(authenticationToken);
		var tokenJWT = tokenService.gerarToken((Usuario) authentication.getPrincipal());

		return ResponseEntity.ok(new DadosTokenJWT(tokenJWT));

	}

}
