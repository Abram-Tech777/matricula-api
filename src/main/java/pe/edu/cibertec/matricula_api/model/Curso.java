package pe.edu.cibertec.matricula_api.model;

import jakarta.persistence.*;

import java.time.LocalTime;

@Entity
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 10)
    private String codigo;

    @Column(nullable = false)
    private String nombre;

    private int ciclo;
    private int creditos;
    private int vacantes;

    @Enumerated(EnumType.STRING)
    private Modalidad modalidad;

    @Enumerated(EnumType.STRING)
    private DiaSemana dia;

    private LocalTime horaInicio;
    private LocalTime horaFin;

    protected Curso() {
    }

    public Curso(String codigo, String nombre, int ciclo, int creditos, int vacantes,
                 Modalidad modalidad, DiaSemana dia, LocalTime horaInicio, LocalTime horaFin) {
        this.codigo = codigo;
        actualizar(nombre, ciclo, creditos, vacantes, modalidad, dia, horaInicio, horaFin);
    }

    public void actualizar(String nombre, int ciclo, int creditos, int vacantes,
                           Modalidad modalidad, DiaSemana dia, LocalTime horaInicio, LocalTime horaFin) {
        this.nombre = nombre;
        this.ciclo = ciclo;
        this.creditos = creditos;
        this.vacantes = vacantes;
        this.modalidad = modalidad;
        this.dia = dia;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
    }

    public boolean seCruzaCon(Curso otro) {
        return dia == otro.dia
                && horaInicio.isBefore(otro.horaFin)
                && otro.horaInicio.isBefore(horaFin);
    }

    public String horario() {
        return dia + " " + horaInicio + "-" + horaFin;
    }

    public Long getId() { return id; }
    public String getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
    public int getCiclo() { return ciclo; }
    public int getCreditos() { return creditos; }
    public int getVacantes() { return vacantes; }
    public Modalidad getModalidad() { return modalidad; }
    public DiaSemana getDia() { return dia; }
    public LocalTime getHoraInicio() { return horaInicio; }
    public LocalTime getHoraFin() { return horaFin; }
}
