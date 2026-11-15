package pe.edu.cibertec.matricula_api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.cibertec.matricula_api.dto.CursoRequest;
import pe.edu.cibertec.matricula_api.dto.CursoResponse;
import pe.edu.cibertec.matricula_api.model.DiaSemana;
import pe.edu.cibertec.matricula_api.model.Modalidad;
import pe.edu.cibertec.matricula_api.service.CursoService;

import java.net.URI;
import java.util.List;

@Tag(name = "Cursos")
@RestController
@RequestMapping("/api/cursos")
public class CursoController {

    private final CursoService service;

    public CursoController(CursoService service) {
        this.service = service;
    }

    @Operation(summary = "Listar cursos con filtros opcionales")
    @ApiResponse(responseCode = "200", description = "Lista de cursos")
    @ApiResponse(responseCode = "400", description = "Filtro inválido")
    @GetMapping
    public List<CursoResponse> listar(@RequestParam(required = false) @Min(1) @Max(6) Integer ciclo,
                                      @RequestParam(required = false) Modalidad modalidad,
                                      @RequestParam(required = false) DiaSemana dia,
                                      @RequestParam(required = false) String nombre) {
        return service.listar(ciclo, modalidad, dia, nombre);
    }

    @Operation(summary = "Obtener un curso por código")
    @ApiResponse(responseCode = "200", description = "Curso encontrado")
    @ApiResponse(responseCode = "404", description = "CURSO_NO_ENCONTRADO")
    @GetMapping("/{codigo}")
    public CursoResponse obtener(@PathVariable String codigo) {
        return service.obtener(codigo);
    }

    @Operation(summary = "Registrar un curso")
    @ApiResponse(responseCode = "201", description = "Curso registrado")
    @ApiResponse(responseCode = "400", description = "DATOS_INVALIDOS o HORARIO_INVALIDO")
    @ApiResponse(responseCode = "409", description = "CURSO_DUPLICADO")
    @PostMapping
    public ResponseEntity<CursoResponse> crear(@Valid @RequestBody CursoRequest req) {
        CursoResponse res = service.crear(req);
        return ResponseEntity.created(URI.create("/api/cursos/" + res.codigo())).body(res);
    }

    @Operation(summary = "Actualizar un curso (el código no cambia)")
    @ApiResponse(responseCode = "200", description = "Curso actualizado")
    @ApiResponse(responseCode = "400", description = "DATOS_INVALIDOS o HORARIO_INVALIDO")
    @ApiResponse(responseCode = "404", description = "CURSO_NO_ENCONTRADO")
    @ApiResponse(responseCode = "422", description = "VACANTES_INSUFICIENTES")
    @PutMapping("/{codigo}")
    public CursoResponse actualizar(@PathVariable String codigo, @Valid @RequestBody CursoRequest req) {
        return service.actualizar(codigo, req);
    }

    @Operation(summary = "Eliminar un curso sin matrículas")
    @ApiResponse(responseCode = "204", description = "Curso eliminado")
    @ApiResponse(responseCode = "404", description = "CURSO_NO_ENCONTRADO")
    @ApiResponse(responseCode = "409", description = "CURSO_CON_MATRICULAS")
    @DeleteMapping("/{codigo}")
    public ResponseEntity<Void> eliminar(@PathVariable String codigo) {
        service.eliminar(codigo);
        return ResponseEntity.noContent().build();
    }
}
