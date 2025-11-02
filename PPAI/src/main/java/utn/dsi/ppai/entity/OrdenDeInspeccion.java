package utn.dsi.ppai.entity;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrdenDeInspeccion {
    private int numeroOrden;
    private LocalDateTime fechaHoraInicio;
    private LocalDateTime fechaHoraFinalizacion;
    private LocalDateTime fechaHoraCierre;
    private String observacionCierre;
    private Estado estado;
    private EstacionSismologica estacionSismologica;
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


    public void cerrar(Estado estadoCerrado) {
        setEstado(estadoCerrado);
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