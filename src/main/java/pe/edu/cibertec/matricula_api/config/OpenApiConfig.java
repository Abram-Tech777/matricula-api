package pe.edu.cibertec.matricula_api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    OpenAPI matriculaOpenApi() {
        return new OpenAPI().info(new Info()
                .title("Matrícula CIBERTEC API")
                .version("1.0")
                .description("""
                        API de matrícula usada como sistema bajo prueba en el curso Pruebas de Software.
                        Reglas: S/ 120 por crédito, 20% de descuento con promedio >= 17 y 10% con >= 15,
                        máximo 24 créditos activos por alumno."""));
    }
}
