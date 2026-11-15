package pe.edu.cibertec.matricula_api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.cibertec.matricula_api.dto.AlumnoRequest;
import pe.edu.cibertec.matricula_api.dto.AlumnoResponse;
import pe.edu.cibertec.matricula_api.dto.ConstanciaResponse;
import pe.edu.cibertec.matricula_api.service.AlumnoService;
import pe.edu.cibertec.matricula_api.service.MatriculaService;

import java.net.URI;
import java.util.List;

@Tag(name = "Alumnos")
@RestController
@RequestMapping("/api/alumnos")
public class AlumnoController {

    private final AlumnoService service;
    private final MatriculaService matriculaService;

    public AlumnoController(AlumnoService service, MatriculaService matriculaService) {
        this.service = service;
        this.matriculaService = matriculaService;
    }

    @Operation(summary = "Listar alumnos, opcionalmente por nombre o deuda")
    @GetMapping
    public List<AlumnoResponse> listar(@RequestParam(required = false) String nombre,
                                       @RequestParam(required = false) Boolean deuda) {
        return service.listar(nombre, deuda);
    }

    @Operation(summary = "Obtener un alumno por código")
    @ApiResponse(responseCode = "200", description = "Alumno encontrado")
    @ApiResponse(responseCode = "404", description = "ALUMNO_NO_ENCONTRADO")
    @GetMapping("/{codigo}")
    public AlumnoResponse obtener(@PathVariable String codigo) {
        return service.obtener(codigo);
    }

    @Operation(summary = "Constancia de matrícula: cursos vigentes, créditos y montos")
    @ApiResponse(responseCode = "200", description = "Constancia generada")
    @ApiResponse(responseCode = "404", description = "ALUMNO_NO_ENCONTRADO")
    @GetMapping("/{codigo}/constancia")
    public ConstanciaResponse constancia(@PathVariable String codigo) {
        return matriculaService.constancia(codigo);
    }

    @Operation(summary = "Registrar un alumno")
    @ApiResponse(responseCode = "201", description = "Alumno registrado")
    @ApiResponse(responseCode = "400", description = "DATOS_INVALIDOS")
    @ApiResponse(responseCode = "409", description = "ALUMNO_DUPLICADO o EMAIL_DUPLICADO")
    @PostMapping
    public ResponseEntity<AlumnoResponse> crear(@Valid @RequestBody AlumnoRequest req) {
        AlumnoResponse res = service.crear(req);
        return ResponseEntity.created(URI.create("/api/alumnos/" + res.codigo())).body(res);
    }

    @Operation(summary = "Actualizar un alumno (el código no cambia)")
    @ApiResponse(responseCode = "200", description = "Alumno actualizado")
    @ApiResponse(responseCode = "400", description = "DATOS_INVALIDOS")
    @ApiResponse(responseCode = "404", description = "ALUMNO_NO_ENCONTRADO")
    @ApiResponse(responseCode = "409", description = "EMAIL_DUPLICADO")
    @PutMapping("/{codigo}")
    public AlumnoResponse actualizar(@PathVariable String codigo, @Valid @RequestBody AlumnoRequest req) {
        return service.actualizar(codigo, req);
    }

    @Operation(summary = "Eliminar un alumno sin matrículas")
    @ApiResponse(responseCode = "204", description = "Alumno eliminado")
    @ApiResponse(responseCode = "404", description = "ALUMNO_NO_ENCONTRADO")
    @ApiResponse(responseCode = "409", description = "ALUMNO_CON_MATRICULAS")
    @DeleteMapping("/{codigo}")
    public ResponseEntity<Void> eliminar(@PathVariable String codigo) {
        service.eliminar(codigo);
        return ResponseEntity.noContent().build();
    }
}
