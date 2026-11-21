package pe.edu.cibertec.matricula_api.screenplay.questions;

import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;

import java.util.List;
import java.util.Map;

public class ElHistorial {

    public static Question<String> condicion(String codigoCurso) {
        return Question.about("condicion de " + codigoCurso)
                .answeredBy(actor -> cursos().stream()
                        .map(curso -> (Map<?, ?>) curso)
                        .filter(curso -> codigoCurso.equals(curso.get("codigoCurso")))
                        .map(curso -> String.valueOf(curso.get("condicion")))
                        .findFirst()
                        .orElse(""));
    }

    public static Question<Integer> creditosAprobados() {
        return Question.about("creditos aprobados")
                .answeredBy(actor -> SerenityRest.lastResponse().jsonPath().getInt("creditosAprobados"));
    }

    public static Question<Double> promedioPonderado() {
        return Question.about("promedio ponderado")
                .answeredBy(actor -> SerenityRest.lastResponse().jsonPath().getDouble("promedioPonderado"));
    }

    private static List<?> cursos() {
        return SerenityRest.lastResponse().jsonPath().getList("cursos", Map.class);
    }
}
