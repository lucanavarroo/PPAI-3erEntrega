package utn.dsi.ppai.entity;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Sesion {
    private LocalDateTime fechaHoraFin;
    private LocalDateTime fechaHoraInicio;
    private Usuario usuario;


    public Sesion(LocalDateTime fechaHoraFin, LocalDateTime fechaHoraInicio, Usuario usuario) {
        this.fechaHoraFin = fechaHoraFin;
        this.fechaHoraInicio = fechaHoraInicio;
        this.usuario = usuario;
    }

    // Metodos unicos
    public Empleado obtenerRILogueado() {
        return this.usuario.getEmpleado();
    }

}
