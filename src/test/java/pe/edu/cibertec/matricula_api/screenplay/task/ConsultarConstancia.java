package pe.edu.cibertec.matricula_api.screenplay.task;

import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Get;

public class ConsultarConstancia implements Task {

    private final String codigo;

    public ConsultarConstancia(String codigo) {
        this.codigo = codigo;
    }

    public static ConsultarConstancia de(String codigo) {
        return new ConsultarConstancia(codigo);
    }

    @Override
    @Step("{0} consulta la constancia de #codigo")
    public <T extends Actor> void performAs(T actor) {
        String token = actor.recall("token");
        actor.attemptsTo(
                Get.resource("/api/alumnos/{codigo}/constancia")
                        .with(req -> req.header("Authorization", "Bearer " + token)
                                .pathParam("codigo", codigo)
                        )
        );
    }
}
