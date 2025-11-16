package utn.dsi.ppai.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "estacion_sismologica")
@Data
@NoArgsConstructor
public class EstacionSismologica {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codigo_estacion")
    @EqualsAndHashCode.Include
    private int codigoEstacion;
    @Column(name = "documento_certificacion_adq")
    private String documentoCertificacionAdq;
    @Column(name = "fecha_solicitud_creacion")
    private LocalDateTime fechaSolicitudCreacion;
    @Column(name = "latitud")
    private int latitud;
    @Column(name = "longitud")
    private int longitud;
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "nro_certificacion_adquisicion")
    private int nroCertificacionAdquisicion;

    @OneToOne(mappedBy = "estacion", fetch = FetchType.EAGER)
    @ToString.Exclude
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