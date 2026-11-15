package pe.edu.cibertec.matricula_api.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Matricula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Alumno alumno;

    @ManyToOne(optional = false)
    private Curso curso;

    private double monto;

    @Enumerated(EnumType.STRING)
    private EstadoMatricula estado;

    private LocalDateTime fechaRegistro;
    private LocalDateTime fechaPago;

    protected Matricula() {
    }

    public Matricula(Alumno alumno, Curso curso, double monto) {
        this.alumno = alumno;
        this.curso = curso;
        this.monto = monto;
        this.estado = EstadoMatricula.REGISTRADA;
        this.fechaRegistro = LocalDateTime.now();
    }

    public void pagar() {
        estado = EstadoMatricula.PAGADA;
        fechaPago = LocalDateTime.now();
    }

    public void anular() {
        estado = EstadoMatricula.ANULADA;
    }

    public Long getId() { return id; }
    public Alumno getAlumno() { return alumno; }
    public Curso getCurso() { return curso; }
    public double getMonto() { return monto; }
    public EstadoMatricula getEstado() { return estado; }
    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public LocalDateTime getFechaPago() { return fechaPago; }
}
