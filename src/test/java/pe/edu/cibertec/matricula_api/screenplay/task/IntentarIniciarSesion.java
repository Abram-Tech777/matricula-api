package pe.edu.cibertec.matricula_api.screenplay.task;

import io.restassured.http.ContentType;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Post;

import java.util.Map;

public class IntentarIniciarSesion implements Task {

    private final String username;
    private final String password;

    public IntentarIniciarSesion(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public static IntentarIniciarSesion como(String username, String password) {
        return new IntentarIniciarSesion(username, password);
    }

    @Override
    @Step("{0} intenta iniciar sesion como #username")
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(Post.to("/api/auth/login")
                .with(req -> req.contentType(ContentType.JSON)
                        .body(Map.of("username", username, "password", password))));
    }
}
