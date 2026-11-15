package pe.edu.cibertec.matricula_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.edu.cibertec.matricula_api.model.Curso;
import pe.edu.cibertec.matricula_api.model.DiaSemana;
import pe.edu.cibertec.matricula_api.model.Modalidad;

import java.util.List;
import java.util.Optional;

public interface CursoRepository extends JpaRepository<Curso, Long> {

    Optional<Curso> findByCodigo(String codigo);

    boolean existsByCodigo(String codigo);

    @Query("""
            select c from Curso c
            where (:ciclo is null or c.ciclo = :ciclo)
              and (:modalidad is null or c.modalidad = :modalidad)
              and (:dia is null or c.dia = :dia)
              and (:nombre is null or lower(c.nombre) like lower(concat('%', :nombre, '%')))
            order by c.codigo
            """)
    List<Curso> buscar(@Param("ciclo") Integer ciclo,
                       @Param("modalidad") Modalidad modalidad,
                       @Param("dia") DiaSemana dia,
                       @Param("nombre") String nombre);
}
