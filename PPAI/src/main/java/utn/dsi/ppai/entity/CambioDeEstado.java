package utn.dsi.ppai.entity;

import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CambioDeEstado {
    private LocalDateTime fechaHoraFin;
    private LocalDateTime fechaHoraInicio;
    private Estado estado;
    private List<MotivoFueraServicio> motivoFueraServicio = new ArrayList<>();
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
        this.motivoFueraServicio.add(new MotivoFueraServicio(comentario, motivoTipo));
    }

}
