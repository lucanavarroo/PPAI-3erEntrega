package utn.dsi.ppai.repositories;

import utn.dsi.ppai.entity.Estado;

public class RepositorioEstado extends RepositorioBase<Estado, Integer> {
    
    public RepositorioEstado() {
        super(Estado.class);
    }
}