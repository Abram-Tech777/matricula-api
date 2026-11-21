package pe.edu.cibertec.matricula_api.screenplay.task;

import io.restassured.http.ContentType;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Post;

import java.util.List;
import java.util.Map;

public class CotizarCurso implements Task {

    private final String curso;
    private final String alumno;

    public CotizarCurso(String curso, String alumno) {
        this.curso = curso;
        this.alumno = alumno;
    }

    public static CotizarCurso conCurso(String curso, String alumno) {
        return new CotizarCurso(curso, alumno);
    }

    @Override
    @Step("{0} cotiza el curso #curso del alumno #alumno")
    public <T extends Actor> void performAs(T actor) {
        String token = actor.recall("token");
        actor.attemptsTo(
                Post.to("/api/matriculas/cotizacion")
                        .with(req -> req.contentType(ContentType.JSON)
                                .header("Authorization", "Bearer " + token)
                                .body(Map.of("codigoAlumno", alumno, "cursos", List.of(curso)))
                        )
        );
    }
}
