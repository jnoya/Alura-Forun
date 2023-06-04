
package com.br.alura.forum.modelo.topico;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TopicoRepository extends JpaRepository<Topico, Long> {

	Page<DadosListagemTopico> findAllByAtivoTrue(Pageable paginacao);

	Topico getReferencByTitulo(String titulo);

}
