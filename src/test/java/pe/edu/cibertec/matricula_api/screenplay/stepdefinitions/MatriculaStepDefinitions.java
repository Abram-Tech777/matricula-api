package pe.edu.cibertec.matricula_api.screenplay.stepdefinitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import pe.edu.cibertec.matricula_api.screenplay.questions.ElMonto;
import pe.edu.cibertec.matricula_api.screenplay.questions.ElPago;
import pe.edu.cibertec.matricula_api.screenplay.questions.LaRespuesta;
import pe.edu.cibertec.matricula_api.screenplay.task.ConsultarConstancia;
import pe.edu.cibertec.matricula_api.screenplay.task.ConsultarPagos;
import pe.edu.cibertec.matricula_api.screenplay.task.CotizarCurso;
import pe.edu.cibertec.matricula_api.screenplay.task.Matricularse;
import pe.edu.cibertec.matricula_api.screenplay.task.PagarMatricula;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.is;

public class MatriculaStepDefinitions {

    @When("cotiza el curso {string} del alumno {string}")
    public void cotizaElCurso(String curso, String alumno) {
        theActorInTheSpotlight().attemptsTo(CotizarCurso.conCurso(curso, alumno));
    }

    @And("la cotizacion muestra {int} creditos, subtotal {word}, descuento {int} y total {word}")
    public void laCotizacionMuestra(int creditos, String subtotal, int descuento, String total) {
        theActorInTheSpotlight().should(seeThat(LaRespuesta.campoEntero("creditos"), equalTo(creditos)));
        theActorInTheSpotlight().should(seeThat(LaRespuesta.campoDecimal("subtotal"), equalTo(Decimales.parse(subtotal))));
        theActorInTheSpotlight().should(seeThat(LaRespuesta.campoEntero("descuento"), equalTo(descuento)));
        theActorInTheSpotlight().should(seeThat(LaRespuesta.campoDecimal("total"), equalTo(Decimales.parse(total))));
    }

    @When("se matricula en el curso {string} con el alumno {string}")
    public void seMatriculaEnElCurso(String curso, String alumno) {
        theActorInTheSpotlight().attemptsTo(Matricularse.en(alumno, curso));
    }

    @And("el estado de la matricula es {string}")
    public void elEstadoDeLaMatriculaEs(String estado) {
        theActorInTheSpotlight().should(seeThat(LaRespuesta.campo("estado"), equalTo(estado)));
    }

    @And("el monto de la matricula es {word}")
    public void elMontoDeLaMatriculaEs(String monto) {
        theActorInTheSpotlight().should(seeThat(ElMonto.deLaMatricula(), equalTo(Decimales.parse(monto))));
    }

    @When("paga la matricula con {string}")
    public void pagaLaMatricula(String metodo) {
        theActorInTheSpotlight().attemptsTo(PagarMatricula.conMetodo(metodo));
    }

    @And("la matricula tiene fecha de pago")
    public void laMatriculaTieneFechaDePago() {
        theActorInTheSpotlight().should(seeThat(LaRespuesta.campo("fechaPago"), notNullValue()));
    }

    @When("consulta los pagos de {string} con metodo {string}")
    public void consultaLosPagos(String alumno, String metodo) {
        theActorInTheSpotlight().attemptsTo(ConsultarPagos.deAlumno(alumno, metodo));
    }

    @And("existe el pago de la matricula recordada con el metodo {string}")
    public void existeElPagoDeLaMatricula(String metodo) {
        Long idMatricula = theActorInTheSpotlight().recall("idMatricula");
        theActorInTheSpotlight().should(seeThat(ElPago.existePara(idMatricula, metodo), is(true)));
    }

    @When("consulta la constancia de {string}")
    public void consultaLaConstancia(String codigo) {
        theActorInTheSpotlight().attemptsTo(ConsultarConstancia.de(codigo));
    }
}
