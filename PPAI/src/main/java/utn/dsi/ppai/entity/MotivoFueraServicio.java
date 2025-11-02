package utn.dsi.ppai.entity;

import lombok.Data;

@Data
public class MotivoFueraServicio {
    private String comentario;
    private MotivoTipo motivoTipo;

    public MotivoFueraServicio(String comentario, MotivoTipo motivoTipo) {
        this.comentario = comentario;
        this.motivoTipo = motivoTipo;
    }

}

