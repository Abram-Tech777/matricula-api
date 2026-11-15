package pe.edu.cibertec.matricula_api.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.cibertec.matricula_api.dto.*;
import pe.edu.cibertec.matricula_api.exception.ApiException;
import pe.edu.cibertec.matricula_api.model.Alumno;
import pe.edu.cibertec.matricula_api.model.Curso;
import pe.edu.cibertec.matricula_api.model.EstadoMatricula;
import pe.edu.cibertec.matricula_api.model.Matricula;
import pe.edu.cibertec.matricula_api.repository.MatriculaRepository;

import java.util.List;

import static pe.edu.cibertec.matricula_api.model.EstadoMatricula.*;
import static pe.edu.cibertec.matricula_api.service.CalculadoraMatricula.MAX_CREDITOS;
import static pe.edu.cibertec.matricula_api.service.CalculadoraMatricula.redondear;

@Service
public class MatriculaService {

    private final MatriculaRepository matriculas;
    private final AlumnoService alumnoService;
    private final CursoService cursoService;
    private final CalculadoraMatricula calculadora;

    public MatriculaService(MatriculaRepository matriculas, AlumnoService alumnoService,
                            CursoService cursoService, CalculadoraMatricula calculadora) {
        this.matriculas = matriculas;
        this.alumnoService = alumnoService;
        this.cursoService = cursoService;
        this.calculadora = calculadora;
    }

    public CotizacionResponse cotizar(CotizacionRequest req) {
        Alumno alumno = alumnoService.buscar(req.codigoAlumno());
        List<Curso> cursos = req.cursos().stream().distinct().map(cursoService::buscar).toList();

        int creditos = cursos.stream().mapToInt(Curso::getCreditos).sum();
        validarLimite(creditosVigentes(vigentes(alumno)) + creditos);

        return new CotizacionResponse(
                alumno.getCodigo(),
                cursos.stream().map(Curso::getCodigo).toList(),
                creditos,
                calculadora.subtotal(creditos),
                porcentaje(alumno),
                calculadora.monto(creditos, alumno.getPromedio()));
    }

    @Transactional
    public MatriculaResponse registrar(MatriculaRequest req) {
        Alumno alumno = alumnoService.buscar(req.codigoAlumno());
        Curso curso = cursoService.buscar(req.codigoCurso());
        List<Matricula> vigentes = vigentes(alumno);

        if (alumno.isTieneDeuda()) {
            throw ApiException.noProcesable("ALUMNO_CON_DEUDA",
                    "El alumno " + alumno.getCodigo() + " tiene deuda pendiente");
        }
        if (vigentes.stream().anyMatch(m -> m.getCurso().getId().equals(curso.getId()))) {
            throw ApiException.conflicto("MATRICULA_DUPLICADA",
                    "El alumno ya está matriculado en " + curso.getCodigo());
        }
        if (cursoService.ocupadas(curso) >= curso.getVacantes()) {
            throw ApiException.conflicto("SIN_VACANTES",
                    "El curso " + curso.getCodigo() + " no tiene vacantes");
        }
        vigentes.stream()
                .map(Matricula::getCurso)
                .filter(curso::seCruzaCon)
                .findFirst()
                .ifPresent(otro -> {
                    throw ApiException.conflicto("CRUCE_HORARIO", "El horario de " + curso.getCodigo()
                            + " se cruza con " + otro.getCodigo() + " (" + otro.horario() + ")");
                });
        validarLimite(creditosVigentes(vigentes) + curso.getCreditos());

        double monto = calculadora.monto(curso.getCreditos(), alumno.getPromedio());
        return MatriculaResponse.of(matriculas.save(new Matricula(alumno, curso, monto)));
    }

    public List<MatriculaResponse> listar(String alumno, String curso, EstadoMatricula estado) {
        return matriculas.buscar(alumno, curso, estado).stream().map(MatriculaResponse::of).toList();
    }

    public MatriculaResponse obtener(Long id) {
        return MatriculaResponse.of(buscar(id));
    }

    @Transactional
    public MatriculaResponse pagar(Long id) {
        Matricula m = buscar(id);
        if (m.getEstado() == ANULADA) {
            throw ApiException.conflicto("MATRICULA_ANULADA", "La matrícula " + id + " está anulada");
        }
        if (m.getEstado() == PAGADA) {
            throw ApiException.conflicto("MATRICULA_YA_PAGADA", "La matrícula " + id + " ya fue pagada");
        }
        m.pagar();
        return MatriculaResponse.of(m);
    }

    @Transactional
    public MatriculaResponse anular(Long id) {
        Matricula m = buscar(id);
        if (m.getEstado() == ANULADA) {
            throw ApiException.conflicto("MATRICULA_YA_ANULADA", "La matrícula " + id + " ya fue anulada");
        }
        if (m.getEstado() == PAGADA) {
            throw ApiException.conflicto("MATRICULA_PAGADA",
                    "La matrícula " + id + " ya fue pagada y no se puede anular");
        }
        m.anular();
        return MatriculaResponse.of(m);
    }

    public ConstanciaResponse constancia(String codigoAlumno) {
        Alumno alumno = alumnoService.buscar(codigoAlumno);
        List<Matricula> vigentes = vigentes(alumno);

        double total = vigentes.stream().mapToDouble(Matricula::getMonto).sum();
        double pagado = vigentes.stream()
                .filter(m -> m.getEstado() == PAGADA)
                .mapToDouble(Matricula::getMonto)
                .sum();

        return new ConstanciaResponse(
                alumno.getCodigo(),
                alumno.nombreCompleto(),
                alumno.getPromedio(),
                porcentaje(alumno),
                vigentes.stream().map(MatriculaResponse::of).toList(),
                creditosVigentes(vigentes),
                redondear(total),
                redondear(pagado),
                redondear(total - pagado));
    }

    private List<Matricula> vigentes(Alumno alumno) {
        return matriculas.findByAlumnoAndEstadoNotOrderById(alumno, ANULADA);
    }

    private int creditosVigentes(List<Matricula> vigentes) {
        return vigentes.stream().mapToInt(m -> m.getCurso().getCreditos()).sum();
    }

    private int porcentaje(Alumno alumno) {
        return (int) Math.round(calculadora.descuento(alumno.getPromedio()) * 100);
    }

    private void validarLimite(int creditos) {
        if (creditos > MAX_CREDITOS) {
            throw ApiException.noProcesable("LIMITE_CREDITOS",
                    "El alumno sumaría " + creditos + " créditos; el máximo es " + MAX_CREDITOS);
        }
    }

    private Matricula buscar(Long id) {
        return matriculas.findById(id)
                .orElseThrow(() -> ApiException.noEncontrado("MATRICULA_NO_ENCONTRADA",
                        "No existe la matrícula " + id));
    }
}
