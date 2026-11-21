package pe.edu.cibertec.matricula_api.screenplay.stepdefinitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import pe.edu.cibertec.matricula_api.screenplay.questions.ElCodigoError;
import pe.edu.cibertec.matricula_api.screenplay.task.CerrarSesion;
import pe.edu.cibertec.matricula_api.screenplay.task.ConsultarPerfil;
import pe.edu.cibertec.matricula_api.screenplay.task.IniciarSesion;
import pe.edu.cibertec.matricula_api.screenplay.task.IntentarIniciarSesion;
import pe.edu.cibertec.matricula_api.screenplay.task.ListarAlumnos;
import pe.edu.cibertec.matricula_api.screenplay.task.RenovarToken;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.actors.OnStage.theActorCalled;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;
import static org.hamcrest.Matchers.equalTo;

public class SeguridadStepDefinitions {

    @When("{word} inicia sesion como {string} con clave {string}")
    public void iniciaSesion(String nombre, String username, String password) {
        theActorCalled(nombre).attemptsTo(IniciarSesion.como(username, password));
    }

    @When("renueva tokens con el refresh guardado")
    public void renuevaTokensConElRefreshGuardado() {
        theActorInTheSpotlight().attemptsTo(RenovarToken.conElRefreshGuardado());
    }

    @When("renueva tokens con el refresh original")
    public void renuevaTokensConElRefreshOriginal() {
        theActorInTheSpotlight().attemptsTo(RenovarToken.conElRefreshOriginal());
    }

    @When("consulta el perfil con el token nuevo")
    public void consultaElPerfilConElTokenNuevo() {
        theActorInTheSpotlight().attemptsTo(ConsultarPerfil.conElTokenNuevo());
    }

    @When("consulta el perfil con el token actual")
    public void consultaElPerfilConElTokenActual() {
        theActorInTheSpotlight().attemptsTo(ConsultarPerfil.conElTokenActual());
    }

    @When("cierra la sesion actual")
    public void cierraLaSesionActual() {
        theActorInTheSpotlight().attemptsTo(new CerrarSesion());
    }

    @When("{word} intenta iniciar sesion como {string} con clave {string}")
    public void intentaIniciarSesion(String nombre, String username, String password) {
        theActorCalled(nombre).attemptsTo(IntentarIniciarSesion.como(username, password));
    }

    @And("intenta listar todos los alumnos")
    public void intentaListarTodosLosAlumnos() {
        theActorInTheSpotlight().attemptsTo(new ListarAlumnos());
    }

    @And("el error es {string}")
    public void elErrorEs(String codigo) {
        theActorInTheSpotlight().should(seeThat(ElCodigoError.delServidor(), equalTo(codigo)));
    }
}
