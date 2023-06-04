
package com.br.alura.forum.modelo.resposta;

import jakarta.validation.constraints.NotBlank;

public record DadosCadastroResposta(

		@NotBlank(message = "Mensagem é obrigatória") String mensagem,

		@NotBlank(message = "Topico é obrigatório") String topico,

		@NotBlank(message = "Autor é obrigatorio") String autor

) {}
