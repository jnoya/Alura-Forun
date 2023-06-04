
package com.br.alura.forum.modelo.usuario;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCrypt;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "usuarios")
@Entity(name = "Usuario")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Usuario implements UserDetails {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String nome;

	private String email;

	private String senha;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {

		return List.of(new SimpleGrantedAuthority("ROLE_USER"));

	}

	public Usuario(DadosCadastroUsuario dados) {

		this.nome = dados.nome();
		this.email = dados.email();
		this.senha = dados.senha();

	}

	@Override
	public String getPassword() {

		return senha;

	}

	@Override
	public String getUsername() {

		return email;

	}

	@Override
	public boolean isAccountNonExpired() {

		return true;

	}

	@Override
	public boolean isAccountNonLocked() {

		return true;

	}

	@Override
	public boolean isCredentialsNonExpired() {

		return true;

	}

	@Override
	public boolean isEnabled() {

		return true;

	}

	public void setSenha(String senha) {

		this.senha = senha;

	}

	public void atualizarInformacoes(@Valid DadosAtualizacaoUsuario dados) {

		if (dados.nome() != null) {

			this.nome = dados.nome();

		}

		if (dados.email() != null) {

			this.email = dados.email();

		}

		if (dados.senha() != null) {

			String salt = BCrypt.gensalt();
			var bcript = BCrypt.hashpw(dados.senha(), salt);
			this.senha = bcript;

		}

	}

}
