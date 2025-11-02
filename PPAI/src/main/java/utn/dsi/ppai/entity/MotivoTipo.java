package utn.dsi.ppai.entity;

import lombok.Data;

@Data
public class MotivoTipo {
    private String descripcion;

    public MotivoTipo(String descripcion) {
        this.descripcion = descripcion;
    }

}
