package utn.dsi.ppai.boundary;


import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;
import utn.dsi.ppai.interfaces.IObservadorCierreInspeccion;

@Data
public class PantallaCCRS implements IObservadorCierreInspeccion {
    private int idSismografo;
    private String estadoFueraServicio;
    private LocalDateTime fechaHoraRegistro;
    private List<String> motivos;
    private List<String> comentarios;

    
    public PantallaCCRS() {
    }
    public void publicar(){
        System.out.println("---------------------------------------------------------------");
        System.out.println("                        PANTALLA CCRS                          ");
        System.out.println("                 Centro de Control y Monitoreo                ");
        System.out.println("---------------------------------------------------------------");
        System.out.println();
        System.out.println("  ID Sismografo:              " + idSismografo);
        System.out.println("  Estado:                    " + estadoFueraServicio);
        System.out.println("  Fecha y Hora de Registro:  " + fechaHoraRegistro);
        System.out.println();
        System.out.println("  ------------------------------------------------------");
        System.out.println("  MOTIVOS Y COMENTARIOS");
        System.out.println("  ------------------------------------------------------");
        for (int i = 0; i < motivos.size(); i++) {
            System.out.println("  " + (i + 1) + ". Motivo: " + motivos.get(i));
            if (i < comentarios.size() && comentarios.get(i) != null && !comentarios.get(i).isEmpty()) {
                System.out.println("     Comentario: " + comentarios.get(i));
            }
        }
        System.out.println();
        System.out.println("---------------------------------------------------------------");
    }

    @Override
    public void actualizar(List<String> listEmails, int idSismografo, String estadoFueraServicio, LocalDateTime fechaHoraRegistro, List<String> motivos, List<String> comentarios) {
        this.idSismografo = idSismografo;
        this.estadoFueraServicio = estadoFueraServicio;
        this.fechaHoraRegistro = fechaHoraRegistro;
        this.motivos = motivos;
        this.comentarios = comentarios;
        this.publicar();
    }
}
