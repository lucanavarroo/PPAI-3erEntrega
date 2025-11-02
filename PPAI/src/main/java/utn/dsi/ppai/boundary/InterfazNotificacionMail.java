package utn.dsi.ppai.boundary;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;
import utn.dsi.ppai.entity.MotivoTipo;
import utn.dsi.ppai.interfaces.IObservadorCierreInspeccion;

@Data
public class InterfazNotificacionMail implements IObservadorCierreInspeccion {
    private List<String> mails;
    private int idSismografo;
    private String estadoFueraServicio;
    private LocalDateTime fechaHoraRegistro;
    private List<MotivoTipo> motivos;
    private  List<String> comentarios;

    public InterfazNotificacionMail() {
    }

    @Override
    public void actualizar(List<String> listEmails, int idSismografo, String estadoFueraServicio, LocalDateTime fechaHoraRegistro, List<MotivoTipo> motivos, List<String> comentarios){
        setMails(listEmails);
        setIdSismografo(idSismografo);
        setEstadoFueraServicio(estadoFueraServicio);
        setFechaHoraRegistro(fechaHoraRegistro);
        setMotivos(motivos);
        setComentarios(comentarios);
        this.enviarMails();
    }

    public void enviarMails(){
        for(String email : mails){
            System.out.println("Enviando mail a: " + email);
        }
    }
     
    }
