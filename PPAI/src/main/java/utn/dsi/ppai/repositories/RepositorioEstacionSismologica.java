package utn.dsi.ppai.repositories;

import utn.dsi.ppai.entity.EstacionSismologica;

public class RepositorioEstacionSismologica extends RepositorioBase<EstacionSismologica, Integer> {
    public RepositorioEstacionSismologica() {
        super(EstacionSismologica.class);
    }
}