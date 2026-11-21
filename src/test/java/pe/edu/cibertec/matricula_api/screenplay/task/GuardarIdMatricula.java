package pe.edu.cibertec.matricula_api.screenplay.task;

import net.serenitybdd.annotations.Step;
import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Get;

import java.util.List;
import java.util.Map;

public class GuardarIdMatricula implements Task {

    private final String curso;
    private final String estado;
    private final String clave;

    public GuardarIdMatricula(String curso, String estado, String clave) {
        this.curso = curso;
        this.estado = estado;
        this.clave = clave;
    }

    public static GuardarIdMatricula pagadaDe(String curso) {
        return new GuardarIdMatricula(curso, "PAGADA", "idMatriculaPagada");
    }

    public static GuardarIdMatricula registradaDe(String curso) {
        return new GuardarIdMatricula(curso, "REGISTRADA", "idMatriculaRegistrada");
    }

    @Override
    @Step("{0} guarda el id de su matricula #estado de #curso")
    public <T extends Actor> void performAs(T actor) {
        String token = actor.recall("token");
        actor.attemptsTo(Get.resource("/api/matriculas")
                .with(req -> req.header("Authorization", "Bearer " + token)
                        .queryParam("curso", curso)
                        .queryParam("estado", estado)));
        List<?> filas = SerenityRest.lastResponse().jsonPath().getList("$", Map.class);
        Number id = (Number) filas.stream()
                .map(fila -> (Map<?, ?>) fila)
                .filter(fila -> curso.equals(fila.get("codigoCurso")) && estado.equals(fila.get("estado")))
                .map(fila -> fila.get("id"))
                .findFirst()
                .orElseThrow();
        actor.remember(clave, id.longValue());
    }
}
