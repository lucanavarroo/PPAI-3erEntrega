package utn.dsi.ppai.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "sismografo")
@Data
@NoArgsConstructor
public class Sismografo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_sismografo")
    @EqualsAndHashCode.Include
    private int identificadorSismografo;
    @Column(name = "fecha_adquisicion")
    private LocalDate fechaAdquisicion;
    @Column(name = "nro_serie")
    private int nroSerie;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_estado", referencedColumnName = "id_estado", nullable = false)
    @ToString.Exclude
    private Estado estadoActual;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "id_sismografo") // ✅ JPA maneja la FK sin atributo en CambioDeEstado
    @ToString.Exclude
    private List<CambioDeEstado> cambiosDeEstado = new ArrayList<>();

    
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "estacion_id", nullable = false, unique = true)
    @ToString.Exclude
    private EstacionSismologica estacion;

    public Sismografo(LocalDate fechaAdquisicion, int identificadorSismografo, int nroSerie, Estado estadoActual, List<CambioDeEstado> cambiosDeEstado, EstacionSismologica estacion) {
        this.fechaAdquisicion = fechaAdquisicion;
        this.identificadorSismografo = identificadorSismografo;
        this.nroSerie = nroSerie;
        this.estadoActual = estadoActual;
        this.cambiosDeEstado = cambiosDeEstado != null ? new ArrayList<>(cambiosDeEstado) : new ArrayList<>();
        this.estacion = estacion;
    }


    public void crearCambioEstado(Estado estado, LocalDateTime fechaHoraActual , Empleado responsableInspeccion, List<MotivoTipo> motivosFueraServicio , List <String> comentarios) {
        CambioDeEstado nuevoCambio = new CambioDeEstado(null , fechaHoraActual, estado , null, responsableInspeccion);
        for (int i = 0; i < motivosFueraServicio.size(); i++) {
            nuevoCambio.crearMotivoFueraServicio( comentarios.get(i) ,motivosFueraServicio.get(i));
        }
        this.cambiosDeEstado.add(nuevoCambio);
    }

    public CambioDeEstado obtenerCambioDeEstadoAnterior(){
        CambioDeEstado cambioAnterior = null;
        for (CambioDeEstado cambio : this.cambiosDeEstado) {
            if (!cambio.sosActual()) {
                if (cambioAnterior == null || cambio.getFechaHoraInicio().isAfter(cambioAnterior.getFechaHoraInicio())) {
                    cambioAnterior = cambio;
                }
            }
        }
        return cambioAnterior;
    }

    public CambioDeEstado obtenerCambioDeEstadoActual(){
        for (CambioDeEstado cambio : this.cambiosDeEstado) {
            if (cambio.sosActual()) {
                return cambio;
            }
        }
        return null; 
    }

    public void cerrarServicio(List<MotivoTipo> motivos, 
                          List<String> comentarios, 
                          Estado estado, 
                          Empleado responsable, 
                          LocalDateTime fechaHora) {
    for (CambioDeEstado cambioEstado : this.cambiosDeEstado) {
        if (cambioEstado.sosActual()) {
            cambioEstado.setFechaHoraFin(fechaHora);
        }
    }
    this.setEstadoActual(estado);
    this.crearCambioEstado(estado, fechaHora, responsable, motivos, comentarios);
}
    public boolean esMiEstacion(EstacionSismologica estacionX) {
        return this.estacion.equals(estacionX);
    }
}