
package com.br.alura.forum.modelo.topico;

import jakarta.validation.constraints.NotBlank;

public record DadosCadastroTopico(

		@NotBlank(message = "Titulo é obrigatório") String titulo,

		@NotBlank(message = "Mensagem é obrigatório") String mensagem,

		@NotBlank(message = "Usuario é obrigatorio") String autor,

		@NotBlank String curso

) {}
