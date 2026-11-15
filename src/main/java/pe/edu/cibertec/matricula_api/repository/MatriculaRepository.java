package pe.edu.cibertec.matricula_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.edu.cibertec.matricula_api.model.Alumno;
import pe.edu.cibertec.matricula_api.model.Curso;
import pe.edu.cibertec.matricula_api.model.EstadoMatricula;
import pe.edu.cibertec.matricula_api.model.Matricula;

import java.util.List;

public interface MatriculaRepository extends JpaRepository<Matricula, Long> {

    @Query("""
            select m from Matricula m
            where (:alumno is null or m.alumno.codigo = :alumno)
              and (:curso is null or m.curso.codigo = :curso)
              and (:estado is null or m.estado = :estado)
            order by m.id
            """)
    List<Matricula> buscar(@Param("alumno") String alumno,
                           @Param("curso") String curso,
                           @Param("estado") EstadoMatricula estado);

    List<Matricula> findByAlumnoAndEstadoNotOrderById(Alumno alumno, EstadoMatricula estado);

    int countByCursoAndEstadoNot(Curso curso, EstadoMatricula estado);

    boolean existsByAlumno(Alumno alumno);

    boolean existsByCurso(Curso curso);
}
