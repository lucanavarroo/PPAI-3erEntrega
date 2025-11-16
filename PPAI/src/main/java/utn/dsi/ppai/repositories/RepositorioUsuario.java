package utn.dsi.ppai.repositories;

import utn.dsi.ppai.entity.Usuario;

public class RepositorioUsuario extends RepositorioBase<Usuario, Integer> {
    public RepositorioUsuario() {
        super(Usuario.class);
    }
}