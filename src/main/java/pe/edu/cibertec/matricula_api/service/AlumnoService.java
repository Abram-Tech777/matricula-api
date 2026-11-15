package pe.edu.cibertec.matricula_api.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.cibertec.matricula_api.dto.AlumnoRequest;
import pe.edu.cibertec.matricula_api.dto.AlumnoResponse;
import pe.edu.cibertec.matricula_api.exception.ApiException;
import pe.edu.cibertec.matricula_api.model.Alumno;
import pe.edu.cibertec.matricula_api.repository.AlumnoRepository;
import pe.edu.cibertec.matricula_api.repository.MatriculaRepository;

import java.util.List;

@Service
public class AlumnoService {

    private final AlumnoRepository alumnos;
    private final MatriculaRepository matriculas;

    public AlumnoService(AlumnoRepository alumnos, MatriculaRepository matriculas) {
        this.alumnos = alumnos;
        this.matriculas = matriculas;
    }

    public List<AlumnoResponse> listar(String nombre, Boolean deuda) {
        return alumnos.buscar(nombre, deuda).stream().map(AlumnoResponse::of).toList();
    }

    public AlumnoResponse obtener(String codigo) {
        return AlumnoResponse.of(buscar(codigo));
    }

    @Transactional
    public AlumnoResponse crear(AlumnoRequest req) {
        if (alumnos.existsByCodigo(req.codigo())) {
            throw ApiException.conflicto("ALUMNO_DUPLICADO", "Ya existe el alumno " + req.codigo());
        }
        validarEmail(req.email(), null);
        Alumno alumno = new Alumno(req.codigo(), req.nombres(), req.apellidos(), req.email(),
                req.promedio(), req.conDeuda());
        return AlumnoResponse.of(alumnos.save(alumno));
    }

    @Transactional
    public AlumnoResponse actualizar(String codigo, AlumnoRequest req) {
        Alumno alumno = buscar(codigo);
        validarEmail(req.email(), alumno);
        alumno.actualizar(req.nombres(), req.apellidos(), req.email(), req.promedio(), req.conDeuda());
        return AlumnoResponse.of(alumno);
    }

    @Transactional
    public void eliminar(String codigo) {
        Alumno alumno = buscar(codigo);
        if (matriculas.existsByAlumno(alumno)) {
            throw ApiException.conflicto("ALUMNO_CON_MATRICULAS",
                    "El alumno " + codigo + " tiene matrículas registradas");
        }
        alumnos.delete(alumno);
    }

    Alumno buscar(String codigo) {
        return alumnos.findByCodigo(codigo)
                .orElseThrow(() -> ApiException.noEncontrado("ALUMNO_NO_ENCONTRADO",
                        "No existe el alumno " + codigo));
    }

    private void validarEmail(String email, Alumno actual) {
        alumnos.findByEmail(email)
                .filter(otro -> actual == null || !otro.getId().equals(actual.getId()))
                .ifPresent(otro -> {
                    throw ApiException.conflicto("EMAIL_DUPLICADO", "El email " + email + " ya está registrado");
                });
    }
}
