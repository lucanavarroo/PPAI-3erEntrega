package utn.dsi.ppai.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "orden_de_inspeccion")
@Data
@NoArgsConstructor
public class OrdenDeInspeccion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "numero_orden")
    @EqualsAndHashCode.Include
    private int numeroOrden;
    @Column(name = "fecha_hora_inicio")
    private LocalDateTime fechaHoraInicio;
    @Column(name = "fecha_hora_finalizacion")
    private LocalDateTime fechaHoraFinalizacion;
    @Column(name = "fecha_hora_cierre")
    private LocalDateTime fechaHoraCierre;
    @Column(name = "observacion_cierre")
    private String observacionCierre;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_estado", referencedColumnName = "id_estado", nullable = false)
    @ToString.Exclude
    private Estado estado;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "codigo_estacion", referencedColumnName = "codigo_estacion", nullable = false)
    @ToString.Exclude
    private EstacionSismologica estacionSismologica;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_empleado", referencedColumnName = "id_empleado", nullable = false)
    @ToString.Exclude
    private Empleado empleado;

    public OrdenDeInspeccion(int numeroOrden, LocalDateTime fechaHoraInicio, LocalDateTime fechaHoraFinalizacion,
                             LocalDateTime fechaHoraCierre, String observacionCierre, Estado estado,
                             EstacionSismologica estacionSismologica, Empleado empleado) {
        this.numeroOrden = numeroOrden;
        this.fechaHoraInicio = fechaHoraInicio;
        this.fechaHoraFinalizacion = fechaHoraFinalizacion;
        this.fechaHoraCierre = fechaHoraCierre;
        this.observacionCierre = observacionCierre;
        this.estado = estado;
        this.estacionSismologica = estacionSismologica;
        this.empleado = empleado;
    }

    // Metodos unicos
    public boolean sosCompletamenteRealizado() {
        return estado != null && estado.sosCompletamenteRealizado();
    }

    public String obtenerDatosOI() {
        int numero = this.getNumeroOrden();
        String fechaFinal = this.getFechaHoraFinalizacion().toString();
        String nombreEstacion = this.getEstacionSismologica().getNombre();

        return String.format("Orden: %d | Finaliza: %s | Estación: %s",
                            numero, fechaFinal, nombreEstacion);
    }   

    // Metodos unicos

    public boolean sosDeEmpleado(Empleado empleadoX) {
        return empleado.equals(empleadoX);
    }


    public void cerrar(Estado estadoCerrado, String obs) {
        setEstado(estadoCerrado);
        setObservacionCierre(obs);
    }

public void actualizarSismografo(List <MotivoTipo> motivos,
                                List <String> comentarios,
                                Estado estado, 
                                Empleado responsable, 
                                LocalDateTime fechaHora) {
    this.estacionSismologica.cerrarServicio(motivos, comentarios, estado, responsable, fechaHora);
}

    // Completo
}