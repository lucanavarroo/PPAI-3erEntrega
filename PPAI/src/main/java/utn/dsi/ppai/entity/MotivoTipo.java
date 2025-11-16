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
@Table(name = "motivo_tipo")
@Data
@NoArgsConstructor
public class MotivoTipo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_motivo_tipo")
    @EqualsAndHashCode.Include
    private int idMotivoTipo;
    @Column(name = "descripcion")
    private String descripcion;

    public MotivoTipo(String descripcion) {
        this.descripcion = descripcion;
    }

}
