package pe.edu.cibertec.matricula_api.dto;

import pe.edu.cibertec.matricula_api.model.Curso;
import pe.edu.cibertec.matricula_api.model.DiaSemana;
import pe.edu.cibertec.matricula_api.model.Modalidad;

import java.time.LocalTime;

public record CursoResponse(
        String codigo,
        String nombre,
        int ciclo,
        int creditos,
        Modalidad modalidad,
        DiaSemana dia,
        LocalTime horaInicio,
        LocalTime horaFin,
        int vacantes,
        int disponibles) {

    public static CursoResponse of(Curso c, int ocupadas) {
        return new CursoResponse(c.getCodigo(), c.getNombre(), c.getCiclo(), c.getCreditos(),
                c.getModalidad(), c.getDia(), c.getHoraInicio(), c.getHoraFin(),
                c.getVacantes(), c.getVacantes() - ocupadas);
    }
}
