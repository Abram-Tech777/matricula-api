package pe.edu.cibertec.matricula_api.screenplay.task;

import io.restassured.http.ContentType;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Patch;

import java.util.Map;

public class CalificarMatricula implements Task {

    private final Long id;
    private final Double nota;

    public CalificarMatricula(Long id, Double nota) {
        this.id = id;
        this.nota = nota;
    }

    public static CalificarMatricula conId(Long id, Double nota) {
        return new CalificarMatricula(id, nota);
    }

    @Override
    @Step("{0} registra la nota #nota en la matricula #id")
    public <T extends Actor> void performAs(T actor) {
        String token = actor.recall("token");
        actor.attemptsTo(Patch.to("/api/matriculas/{id}/nota")
                .with(req -> req.pathParam("id", id)
                        .contentType(ContentType.JSON)
                        .header("Authorization", "Bearer " + token)
                        .body(Map.of("nota", nota))));
    }
}
