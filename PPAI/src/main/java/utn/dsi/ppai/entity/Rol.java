package utn.dsi.ppai.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Rol {
    private String descripcionRol;
    private String nombre;

    public Rol(String descripcionRol, String nombre) {
        this.descripcionRol = descripcionRol;
        this.nombre = nombre;
    }

    //Metodos unicos
    public boolean sosResponsableReparacion() {
        return "Responsable Reparacion".equalsIgnoreCase(nombre);
    }
    public String getNombreRol() {
        return nombre;
    }
    }

// Completo 
