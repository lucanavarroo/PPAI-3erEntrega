package utn.dsi.ppai.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "empleado")
@Data
@NoArgsConstructor
public class Empleado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_empleado")
    @EqualsAndHashCode.Include
    private Integer idEmpleado;
    
    @Column(name = "apellido")
    private String apellido;
    @Column(name = "mail")
    private String mail;
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "telefono")
    private String telefono;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_rol", referencedColumnName = "id_rol", nullable = false)  // ✅ Ser explícito
    @ToString.Exclude
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
