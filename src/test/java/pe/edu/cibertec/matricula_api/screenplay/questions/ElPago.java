package pe.edu.cibertec.matricula_api.screenplay.questions;

import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Question;

import java.util.Map;

public class ElPago {

    public static Question<Boolean> existePara(Long idMatricula, String metodo) {
        return Question.about("el pago de la matricula " + idMatricula)
                .answeredBy(actor -> SerenityRest.lastResponse()
                        .jsonPath()
                        .getList("$", Map.class)
                        .stream()
                        .anyMatch(pago -> {
                            Map<?, ?> fila = (Map<?, ?>) pago;
                            Object id = fila.get("idMatricula");
                            return id instanceof Number
                                    && ((Number) id).longValue() == idMatricula
                                    && metodo.equals(fila.get("metodo"));
                        })
                );
    }
}
