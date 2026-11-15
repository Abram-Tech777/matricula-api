package pe.edu.cibertec.matricula_api.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.cibertec.matricula_api.dto.CursoRequest;
import pe.edu.cibertec.matricula_api.dto.CursoResponse;
import pe.edu.cibertec.matricula_api.exception.ApiException;
import pe.edu.cibertec.matricula_api.model.Curso;
import pe.edu.cibertec.matricula_api.model.DiaSemana;
import pe.edu.cibertec.matricula_api.model.EstadoMatricula;
import pe.edu.cibertec.matricula_api.model.Modalidad;
import pe.edu.cibertec.matricula_api.repository.CursoRepository;
import pe.edu.cibertec.matricula_api.repository.MatriculaRepository;

import java.util.List;

@Service
public class CursoService {

    private final CursoRepository cursos;
    private final MatriculaRepository matriculas;

    public CursoService(CursoRepository cursos, MatriculaRepository matriculas) {
        this.cursos = cursos;
        this.matriculas = matriculas;
    }

    public List<CursoResponse> listar(Integer ciclo, Modalidad modalidad, DiaSemana dia, String nombre) {
        return cursos.buscar(ciclo, modalidad, dia, nombre).stream().map(this::toResponse).toList();
    }

    public CursoResponse obtener(String codigo) {
        return toResponse(buscar(codigo));
    }

    @Transactional
    public CursoResponse crear(CursoRequest req) {
        if (cursos.existsByCodigo(req.codigo())) {
            throw ApiException.conflicto("CURSO_DUPLICADO", "Ya existe el curso " + req.codigo());
        }
        validarHorario(req);
        Curso curso = new Curso(req.codigo(), req.nombre(), req.ciclo(), req.creditos(), req.vacantes(),
                req.modalidad(), req.dia(), req.horaInicio(), req.horaFin());
        return toResponse(cursos.save(curso));
    }

    @Transactional
    public CursoResponse actualizar(String codigo, CursoRequest req) {
        Curso curso = buscar(codigo);
        validarHorario(req);
        int ocupadas = ocupadas(curso);
        if (req.vacantes() < ocupadas) {
            throw ApiException.noProcesable("VACANTES_INSUFICIENTES",
                    "El curso ya tiene " + ocupadas + " matriculados");
        }
        curso.actualizar(req.nombre(), req.ciclo(), req.creditos(), req.vacantes(),
                req.modalidad(), req.dia(), req.horaInicio(), req.horaFin());
        return CursoResponse.of(curso, ocupadas);
    }

    @Transactional
    public void eliminar(String codigo) {
        Curso curso = buscar(codigo);
        if (matriculas.existsByCurso(curso)) {
            throw ApiException.conflicto("CURSO_CON_MATRICULAS",
                    "El curso " + codigo + " tiene matrículas registradas");
        }
        cursos.delete(curso);
    }

    Curso buscar(String codigo) {
        return cursos.findByCodigo(codigo)
                .orElseThrow(() -> ApiException.noEncontrado("CURSO_NO_ENCONTRADO",
                        "No existe el curso " + codigo));
    }

    int ocupadas(Curso curso) {
        return matriculas.countByCursoAndEstadoNot(curso, EstadoMatricula.ANULADA);
    }

    private void validarHorario(CursoRequest req) {
        if (!req.horaInicio().isBefore(req.horaFin())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "HORARIO_INVALIDO",
                    "La hora de inicio debe ser anterior a la hora de fin");
        }
    }

    private CursoResponse toResponse(Curso c) {
        return CursoResponse.of(c, ocupadas(c));
    }
}
