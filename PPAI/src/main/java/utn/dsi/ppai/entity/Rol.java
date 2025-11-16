package utn.dsi.ppai.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "rol")
@Data
@NoArgsConstructor
public class Rol {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rol")
    @EqualsAndHashCode.Include
    private int idRol;
    @Column(name = "descripcion_rol")
    private String descripcionRol;
    @Column(name = "nombre")
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
