package pe.edu.cibertec.matricula_api.screenplay.stepdefinitions;

public final class Decimales {

    private Decimales() {
    }

    public static Double parse(String valor) {
        if (valor.contains(",") && valor.contains(".")) {
            return Double.parseDouble(valor.replace(".", "").replace(",", "."));
        }
        return Double.parseDouble(valor.replace(",", "."));
    }
}
