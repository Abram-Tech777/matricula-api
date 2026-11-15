package pe.edu.cibertec.matricula_api.exception;

import org.springframework.http.HttpStatus;

public class ApiException extends RuntimeException {

    private final HttpStatus status;
    private final String codigo;

    public ApiException(HttpStatus status, String codigo, String mensaje) {
        super(mensaje);
        this.status = status;
        this.codigo = codigo;
    }

    public static ApiException noEncontrado(String codigo, String mensaje) {
        return new ApiException(HttpStatus.NOT_FOUND, codigo, mensaje);
    }

    public static ApiException conflicto(String codigo, String mensaje) {
        return new ApiException(HttpStatus.CONFLICT, codigo, mensaje);
    }

    public static ApiException noProcesable(String codigo, String mensaje) {
        return new ApiException(HttpStatus.UNPROCESSABLE_CONTENT, codigo, mensaje);
    }

    public HttpStatus getStatus() { return status; }
    public String getCodigo() { return codigo; }
}
