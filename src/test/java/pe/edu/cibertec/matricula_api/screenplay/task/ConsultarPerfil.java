package pe.edu.cibertec.matricula_api.screenplay.task;

import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Get;

public class ConsultarPerfil implements Task {

    private final String tokenKey;

    public ConsultarPerfil(String tokenKey) {
        this.tokenKey = tokenKey;
    }

    public static ConsultarPerfil conElTokenActual() {
        return new ConsultarPerfil("token");
    }

    public static ConsultarPerfil conElTokenNuevo() {
        return new ConsultarPerfil("tokenNuevo");
    }

    @Override
    @Step("{0} consulta su perfil")
    public <T extends Actor> void performAs(T actor) {
        String token = actor.recall(tokenKey);
        actor.attemptsTo(Get.resource("/api/auth/perfil")
                .with(req -> req.header("Authorization", "Bearer " + token)));
    }
}
