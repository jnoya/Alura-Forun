
package com.br.alura.forum.infra.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.br.alura.forum.modelo.usuario.UsuarioRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityFilter extends OncePerRequestFilter {

	@Autowired
	private TokenService tokenService;

	@Autowired
	private UsuarioRepository repository;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws IOException, ServletException {

		var tokenJWT = recuperarToken(request);

		if (tokenJWT != null) {

			String email = tokenService.getSubject(tokenJWT);
			var usuario = repository.findByEmail(email);
			var authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());

			SecurityContextHolder.getContext().setAuthentication(authentication);

		}

		filterChain.doFilter(request, response);

	}

	private String recuperarToken(HttpServletRequest request) {

		var authorizationHeader = request.getHeader("Authorization");

		if (authorizationHeader != null) {

			String autorizacao = authorizationHeader.replace("Bearer ", "");

			if (autorizacao.equalsIgnoreCase("undefined")) {

				return null;

			} else {

				return autorizacao;

			}

		}

		return null;

	}

}
