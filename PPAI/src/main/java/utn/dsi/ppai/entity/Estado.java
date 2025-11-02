package utn.dsi.ppai.entity;

import lombok.Data;

@Data
public class Estado {
    private String ambito;
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

