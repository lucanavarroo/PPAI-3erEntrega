package utn.dsi.ppai.interfaces;


import java.time.LocalDateTime;
import java.util.List;

import utn.dsi.ppai.entity.MotivoTipo;



public interface IObservadorCierreInspeccion {
    void actualizar(List<String> listEmails, int idSismografo, String estadoFueraServicio, LocalDateTime fechaHoraRegistro, List<MotivoTipo> motivos, List<String> comentarios);
}
