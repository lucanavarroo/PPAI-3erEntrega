package utn.dsi.ppai.entity;

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
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "cambio_de_estado")
@Data
@NoArgsConstructor
public class CambioDeEstado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cambio_estado")
    @EqualsAndHashCode.Include
    private int idCambioEstado;
    @Column(name = "fecha_hora_fin")
    private LocalDateTime fechaHoraFin;
    @Column(name = "fecha_hora_inicio")
    private LocalDateTime fechaHoraInicio;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_estado", referencedColumnName = "id_estado", nullable = false)
    @ToString.Exclude
    private Estado estado;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "cambio_estado_id") // JPA maneja la FK automáticamente
    @ToString.Exclude
    private List<MotivoFueraServicio> motivoFueraServicio = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_empleado", referencedColumnName = "id_empleado", nullable = true)
    @ToString.Exclude
    private Empleado responsableInspeccion;


    public CambioDeEstado(LocalDateTime fechaHoraFin, LocalDateTime fechaHoraInicio, Estado estado, List<MotivoFueraServicio> motivoFueraServicio, Empleado responsableInspeccion) {
        this.fechaHoraFin = fechaHoraFin;
        this.fechaHoraInicio = fechaHoraInicio;
        this.estado = estado;
        this.motivoFueraServicio = (motivoFueraServicio != null) ? motivoFueraServicio : new ArrayList<>();
        this.responsableInspeccion = responsableInspeccion;
    }

    // Setters y Getters

    public boolean sosActual() {
        return this.fechaHoraFin == null;
    }
    
    public boolean esEstadoActual() {
        return this.fechaHoraFin == null;
    }

    public void crearMotivoFueraServicio(String comentario, MotivoTipo motivoTipo) {
        MotivoFueraServicio motivo = new MotivoFueraServicio(comentario, motivoTipo);// ✅ Establecer la relación
        this.motivoFueraServicio.add(motivo);
    }

}
