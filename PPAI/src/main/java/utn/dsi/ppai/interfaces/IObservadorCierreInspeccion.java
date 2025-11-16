package utn.dsi.ppai.interfaces;


import java.time.LocalDateTime;
import java.util.List;




public interface IObservadorCierreInspeccion {
    void actualizar(List<String> listEmails, int idSismografo, String estadoFueraServicio, LocalDateTime fechaHoraRegistro, List<String> motivos, List<String> comentarios);
}
