package utn.dsi.ppai.entity;

import lombok.Data;


@Data
public class Empleado {
    private String apellido;
    private String mail;
    private String nombre;
    private String telefono;
    private Rol rol;

    public Empleado( String apellido, String mail, String nombre, String telefono, Rol rol) {
        this.apellido = apellido;
        this.mail = mail;
        this.nombre = nombre;
        this.telefono = telefono;
        this.rol = rol;
    }

    // Metodos Unicos
    public boolean sosResponsableReparacion() {
        return rol != null && rol.sosResponsableReparacion();
    }

    public String obtenerMail() {
        return mail;
    }

}

// Completo
