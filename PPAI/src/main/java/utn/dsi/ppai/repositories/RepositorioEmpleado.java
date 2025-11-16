package utn.dsi.ppai.repositories;

import utn.dsi.ppai.entity.Empleado;

public class RepositorioEmpleado extends RepositorioBase<Empleado, Integer> {
    public RepositorioEmpleado() {
        super(Empleado.class);
    }
}