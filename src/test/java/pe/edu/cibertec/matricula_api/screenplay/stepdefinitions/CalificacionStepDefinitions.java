package pe.edu.cibertec.matricula_api.screenplay.stepdefinitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import pe.edu.cibertec.matricula_api.screenplay.questions.ElHistorial;
import pe.edu.cibertec.matricula_api.screenplay.task.CalificarMatricula;
import pe.edu.cibertec.matricula_api.screenplay.task.ConsultarHistorial;
import pe.edu.cibertec.matricula_api.screenplay.task.GuardarIdMatricula;
import pe.edu.cibertec.matricula_api.screenplay.task.IniciarSesion;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.actors.OnStage.theActorCalled;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.equalTo;

public class CalificacionStepDefinitions {

    @And("{word} guarda el id de su matricula pagada de {string}")
    public void rosaGuardaElIdPagado(String nombre, String curso) {
        theActorCalled(nombre).attemptsTo(GuardarIdMatricula.pagadaDe(curso));
    }

    @When("{word} guarda el id de su matricula registrada de {string}")
    public void rosaGuardaElIdRegistrado(String nombre, String curso) {
        theActorCalled(nombre).attemptsTo(GuardarIdMatricula.registradaDe(curso));
    }

    @And("{word} registra la nota {word} en la matricula pagada de Rosa")
    public void registraLaNotaPagada(String docente, String nota) {
        Long id = theActorCalled("Rosa").recall("idMatriculaPagada");
        theActorCalled(docente).attemptsTo(CalificarMatricula.conId(id, Decimales.parse(nota)));
    }

    @And("{word} registra la nota {word} en la matricula registrada de Rosa")
    public void registraLaNotaRegistrada(String docente, String nota) {
        Long id = theActorCalled("Rosa").recall("idMatriculaRegistrada");
        theActorCalled(docente).attemptsTo(CalificarMatricula.conId(id, Decimales.parse(nota)));
    }

    @When("{word} vuelve a iniciar sesion como {string} con clave {string}")
    public void vuelveAIniciarSesion(String nombre, String username, String password) {
        theActorCalled(nombre).attemptsTo(IniciarSesion.como(username, password));
    }

    @And("{word} consulta su historial")
    public void rosaConsultaSuHistorial(String nombre) {
        theActorCalled(nombre).attemptsTo(ConsultarHistorial.de("i202610005"));
    }

    @And("el historial muestra la condicion {string} para {string}, {int} creditos aprobados y promedio {word}")
    public void elHistorialMuestra(String condicion, String curso, int creditos, String promedio) {
        theActorInTheSpotlight().should(seeThat(ElHistorial.condicion(curso), equalTo(condicion)));
        theActorInTheSpotlight().should(seeThat(ElHistorial.creditosAprobados(), equalTo(creditos)));
        theActorInTheSpotlight().should(seeThat(ElHistorial.promedioPonderado(),
                closeTo(Decimales.parse(promedio), 0.01)));
    }
}
