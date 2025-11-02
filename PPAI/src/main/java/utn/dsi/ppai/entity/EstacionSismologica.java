package utn.dsi.ppai.entity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EstacionSismologica {
    private int codigoEstacion;
    private String documentoCertificacionAdq;
    private LocalDateTime fechaSolicitudCreacion;
    private int latitud;
    private int longitud;
    private String nombre;
    private int nroCertificacionAdquisicion;
    private Sismografo sismografo;

    public EstacionSismologica(int codigoEstacion, String documentoCertificacionAdq, LocalDateTime fechaSolicitudCreacion, int latitud, int longitud, String nombre, int nroCertificacionAdquisicion, Sismografo sismografo) {
        this.codigoEstacion = codigoEstacion;
        this.documentoCertificacionAdq = documentoCertificacionAdq;
        this.fechaSolicitudCreacion = fechaSolicitudCreacion;
        this.latitud = latitud;
        this.longitud = longitud;
        this.nombre = nombre;
        this.nroCertificacionAdquisicion = nroCertificacionAdquisicion;
        this.sismografo = sismografo;
    }

    // Metodos Unicos

    public int getIdentificadorSismografo() {
        // Verificar si el sismografo es nulo
        if (sismografo != null) {
            return sismografo.getIdentificadorSismografo();
        } else {
            System.out.println("No hay un sismografo asociado");
            return -1;
        }
    }

public void cerrarServicio(List<MotivoTipo> motivos, 
                          List<String> comentarios, 
                          Estado estado, 
                          Empleado responsable, 
                          LocalDateTime fechaHora) {
    if (sismografo != null) {
        sismografo.cerrarServicio(motivos, comentarios, estado, responsable, fechaHora);
    }
}
}