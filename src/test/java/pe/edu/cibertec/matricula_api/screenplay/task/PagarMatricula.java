package pe.edu.cibertec.matricula_api.screenplay.task;

import io.restassured.http.ContentType;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Patch;

import java.util.Map;

public class PagarMatricula implements Task {

    private final String metodo;

    public PagarMatricula(String metodo) {
        this.metodo = metodo;
    }

    public static PagarMatricula conMetodo(String metodo) {
        return new PagarMatricula(metodo);
    }

    @Override
    @Step("{0} paga la matricula recordada con #metodo")
    public <T extends Actor> void performAs(T actor) {
        String token = actor.recall("token");
        Long idMatricula = actor.recall("idMatricula");
        actor.attemptsTo(
                Patch.to("/api/matriculas/{id}/pagar")
                        .with(req -> req.pathParam("id", idMatricula)
                                .contentType(ContentType.JSON)
                                .header("Authorization", "Bearer " + token)
                                .body(Map.of("metodo", metodo))
                        )
        );
    }
}
