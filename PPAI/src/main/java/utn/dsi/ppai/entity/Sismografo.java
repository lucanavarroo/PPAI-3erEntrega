package utn.dsi.ppai.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class Sismografo {
    private LocalDate fechaAdquisicion;
    private int identificadorSismografo;
    private int nroSerie;
    private Estado estadoActual;
    private List<CambioDeEstado> cambiosDeEstado = new ArrayList<>();
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