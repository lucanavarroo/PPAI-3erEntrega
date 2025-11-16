package utn.dsi.ppai.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "estado")
@Data
@NoArgsConstructor
public class Estado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estado")
    @EqualsAndHashCode.Include
    private int idEstado;
    @Column(name = "ambito")
    private String ambito;
    @Column(name = "nombre_estado")
    private String nombreEstado;

    // Constantes para los estados
    public static final String ESTADO_COMPLETAMENTE_REALIZADO = "Completamente Realizado";
    public static final String ESTADO_COMPLETAMENTE_RECHAZADO = "Completamente Rechazado";
    public static final String ESTADO_CERRADA = "Cerrada";
    public static final String ESTADO_FUERA_SERVICIO = "Fuera de Servicio";
    public static final String ESTADO_ACTIVO = "ACTIVO";
    public static final String ESTADO_ONLINE = "ON-LINE";

    // Constantes para los ámbitos
    public static final String AMBITO_OI = "OI";
    public static final String AMBITO_SISMOGRAFO = "Sismografo";


    // Constructor
    public Estado(String ambito, String nombreEstado) {
        this.ambito = ambito;
        this.nombreEstado = nombreEstado;
    }

    
    // Metodos unicos
    public boolean sosCompletamenteRealizado() {
        return ESTADO_COMPLETAMENTE_REALIZADO.equalsIgnoreCase(nombreEstado);
    }

    public boolean sosCompletamenteRechazado() {
        return ESTADO_COMPLETAMENTE_RECHAZADO.equalsIgnoreCase(nombreEstado);
    }

    public boolean sosCerrada() {
        return ESTADO_CERRADA.equalsIgnoreCase(nombreEstado);
    }

    public boolean sosAmbitoOI() {
        return AMBITO_OI.equalsIgnoreCase(ambito);
    }

    public boolean sosAmbitoSismografo() {
        return AMBITO_SISMOGRAFO.equalsIgnoreCase(ambito);
    }

    public boolean sosFueraDeServicio() {
        return ESTADO_FUERA_SERVICIO.equalsIgnoreCase(nombreEstado);
    }
}

