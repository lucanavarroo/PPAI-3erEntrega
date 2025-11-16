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
        System.out.println("Se actualizo la pantalla con los siguientes datos:");
        System.out.println("ID Sismografo: " + idSismografo);
        System.out.println("Estado: " + estadoFueraServicio);
        System.out.println("Fecha y Hora de Registro: " + fechaHoraRegistro);
        System.out.println("Motivos y Comentarios: ");
        for (int i = 0; i < motivos.size(); i++) {
            System.out.println("Motivo: " + motivos.get(i) + " - Comentario: " + comentarios.get(i));
        }
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
