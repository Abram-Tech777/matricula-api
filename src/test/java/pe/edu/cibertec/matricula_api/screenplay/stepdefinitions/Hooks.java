package pe.edu.cibertec.matricula_api.screenplay.stepdefinitions;

import io.cucumber.java.Before;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.actors.OnlineCast;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Hooks {
    @Before
    public void beforeScenario() {
        OnStage.setTheStage(new OnlineCast());
        System.out.println("Estudiante: Abram Alonso Allauca Alberto");
        System.out.println("Codigo: i202503758");
        System.out.println("Fecha y hora: "
                + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
    }
}
