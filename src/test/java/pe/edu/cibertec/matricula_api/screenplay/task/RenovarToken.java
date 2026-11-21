package pe.edu.cibertec.matricula_api.screenplay.task;

import io.restassured.http.ContentType;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Post;

import java.util.Map;

public class RenovarToken implements Task {

    private final String refreshKey;

    public RenovarToken(String refreshKey) {
        this.refreshKey = refreshKey;
    }

    public static RenovarToken conElRefreshGuardado() {
        return new RenovarToken("refreshToken");
    }

    public static RenovarToken conElRefreshOriginal() {
        return new RenovarToken("refreshTokenOriginal");
    }

    @Override
    @Step("{0} renueva tokens")
    public <T extends Actor> void performAs(T actor) {
        String refresh = actor.recall(refreshKey);
        actor.attemptsTo(Post.to("/api/auth/refresh")
                .with(req -> req.contentType(ContentType.JSON)
                        .body(Map.of("refreshToken", refresh))));
        if (SerenityRest.lastResponse().statusCode() == 200) {
            actor.remember("tokenNuevo", SerenityRest.lastResponse().jsonPath().getString("accessToken"));
            actor.remember("token", SerenityRest.lastResponse().jsonPath().getString("accessToken"));
            actor.remember("refreshToken", SerenityRest.lastResponse().jsonPath().getString("refreshToken"));
        }
    }
}
