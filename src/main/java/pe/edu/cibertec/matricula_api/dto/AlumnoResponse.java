package pe.edu.cibertec.matricula_api.dto;

import pe.edu.cibertec.matricula_api.model.Alumno;

public record AlumnoResponse(
        String codigo,
        String nombres,
        String apellidos,
        String email,
        double promedio,
        boolean tieneDeuda) {

    public static AlumnoResponse of(Alumno a) {
        return new AlumnoResponse(a.getCodigo(), a.getNombres(), a.getApellidos(),
                a.getEmail(), a.getPromedio(), a.isTieneDeuda());
    }
}
