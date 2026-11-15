package pe.edu.cibertec.matricula_api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.cibertec.matricula_api.dto.CotizacionRequest;
import pe.edu.cibertec.matricula_api.dto.CotizacionResponse;
import pe.edu.cibertec.matricula_api.dto.MatriculaRequest;
import pe.edu.cibertec.matricula_api.dto.MatriculaResponse;
import pe.edu.cibertec.matricula_api.model.EstadoMatricula;
import pe.edu.cibertec.matricula_api.service.MatriculaService;

import java.net.URI;
import java.util.List;

@Tag(name = "Matrículas")
@RestController
@RequestMapping("/api/matriculas")
public class MatriculaController {

    private final MatriculaService service;

    public MatriculaController(MatriculaService service) {
        this.service = service;
    }

    @Operation(summary = "Cotizar cursos antes de matricular (no guarda nada)")
    @ApiResponse(responseCode = "200", description = "Cotización calculada")
    @ApiResponse(responseCode = "400", description = "DATOS_INVALIDOS")
    @ApiResponse(responseCode = "404", description = "ALUMNO_NO_ENCONTRADO o CURSO_NO_ENCONTRADO")
    @ApiResponse(responseCode = "422", description = "LIMITE_CREDITOS")
    @PostMapping("/cotizacion")
    public CotizacionResponse cotizar(@Valid @RequestBody CotizacionRequest req) {
        return service.cotizar(req);
    }

    @Operation(summary = "Registrar matrícula de un alumno en un curso")
    @ApiResponse(responseCode = "201", description = "Matrícula registrada")
    @ApiResponse(responseCode = "400", description = "DATOS_INVALIDOS")
    @ApiResponse(responseCode = "404", description = "ALUMNO_NO_ENCONTRADO o CURSO_NO_ENCONTRADO")
    @ApiResponse(responseCode = "409", description = "MATRICULA_DUPLICADA, SIN_VACANTES o CRUCE_HORARIO")
    @ApiResponse(responseCode = "422", description = "ALUMNO_CON_DEUDA o LIMITE_CREDITOS")
    @PostMapping
    public ResponseEntity<MatriculaResponse> registrar(@Valid @RequestBody MatriculaRequest req) {
        MatriculaResponse res = service.registrar(req);
        return ResponseEntity.created(URI.create("/api/matriculas/" + res.id())).body(res);
    }

    @Operation(summary = "Listar matrículas con filtros opcionales")
    @GetMapping
    public List<MatriculaResponse> listar(@RequestParam(required = false) String alumno,
                                          @RequestParam(required = false) String curso,
                                          @RequestParam(required = false) EstadoMatricula estado) {
        return service.listar(alumno, curso, estado);
    }

    @Operation(summary = "Obtener una matrícula por id")
    @ApiResponse(responseCode = "200", description = "Matrícula encontrada")
    @ApiResponse(responseCode = "404", description = "MATRICULA_NO_ENCONTRADA")
    @GetMapping("/{id}")
    public MatriculaResponse obtener(@PathVariable Long id) {
        return service.obtener(id);
    }

    @Operation(summary = "Pagar una matrícula registrada")
    @ApiResponse(responseCode = "200", description = "Matrícula pagada")
    @ApiResponse(responseCode = "404", description = "MATRICULA_NO_ENCONTRADA")
    @ApiResponse(responseCode = "409", description = "MATRICULA_YA_PAGADA o MATRICULA_ANULADA")
    @PatchMapping("/{id}/pagar")
    public MatriculaResponse pagar(@PathVariable Long id) {
        return service.pagar(id);
    }

    @Operation(summary = "Anular una matrícula no pagada y liberar la vacante")
    @ApiResponse(responseCode = "200", description = "Matrícula anulada")
    @ApiResponse(responseCode = "404", description = "MATRICULA_NO_ENCONTRADA")
    @ApiResponse(responseCode = "409", description = "MATRICULA_YA_ANULADA o MATRICULA_PAGADA")
    @PatchMapping("/{id}/anular")
    public MatriculaResponse anular(@PathVariable Long id) {
        return service.anular(id);
    }
}
