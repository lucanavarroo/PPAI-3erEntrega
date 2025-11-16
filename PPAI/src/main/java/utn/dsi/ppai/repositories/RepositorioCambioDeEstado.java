package utn.dsi.ppai.repositories;

import utn.dsi.ppai.entity.CambioDeEstado;

public class RepositorioCambioDeEstado extends RepositorioBase<CambioDeEstado, Integer> {
    
    public RepositorioCambioDeEstado() {
        super(CambioDeEstado.class);
    }
}