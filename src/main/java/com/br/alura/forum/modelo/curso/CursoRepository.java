
package com.br.alura.forum.modelo.curso;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CursoRepository extends JpaRepository<Curso, Long> {

	Curso getReferenceByNome(String curso);

}