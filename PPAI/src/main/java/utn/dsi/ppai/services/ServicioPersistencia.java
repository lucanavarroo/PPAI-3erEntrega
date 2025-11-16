package utn.dsi.ppai.services;

import java.util.List;
import java.util.Optional;

import utn.dsi.ppai.entity.CambioDeEstado;
import utn.dsi.ppai.entity.Empleado;
import utn.dsi.ppai.entity.EstacionSismologica;
import utn.dsi.ppai.entity.Estado;
import utn.dsi.ppai.entity.MotivoFueraServicio;
import utn.dsi.ppai.entity.MotivoTipo;
import utn.dsi.ppai.entity.OrdenDeInspeccion;
import utn.dsi.ppai.entity.Rol;
import utn.dsi.ppai.entity.Sesion;
import utn.dsi.ppai.entity.Sismografo;
import utn.dsi.ppai.entity.Usuario;
import utn.dsi.ppai.repositories.RepositorioCambioDeEstado;
import utn.dsi.ppai.repositories.RepositorioEmpleado;
import utn.dsi.ppai.repositories.RepositorioEstacionSismologica;
import utn.dsi.ppai.repositories.RepositorioEstado;
import utn.dsi.ppai.repositories.RepositorioMotivoFueraServicio;
import utn.dsi.ppai.repositories.RepositorioMotivoTipo;
import utn.dsi.ppai.repositories.RepositorioOrdenDeInspeccion;
import utn.dsi.ppai.repositories.RepositorioRol;
import utn.dsi.ppai.repositories.RepositorioSesion;
import utn.dsi.ppai.repositories.RepositorioSismografo;
import utn.dsi.ppai.repositories.RepositorioUsuario;


public class ServicioPersistencia {
    

    private final RepositorioEmpleado repositorioEmpleado;
    private final RepositorioEstado repositorioEstado;
    private final RepositorioMotivoTipo repositorioMotivoTipo;
    private final RepositorioOrdenDeInspeccion repositorioOrdenDeInspeccion;
    private final RepositorioSismografo repositorioSismografo;
    private final RepositorioCambioDeEstado repositorioCambioDeEstado;
    private final RepositorioMotivoFueraServicio repositorioMotivoFueraServicio;
    private final RepositorioEstacionSismologica repositorioEstacionSismologica;
    private final RepositorioUsuario repositorioUsuario;
    private final RepositorioSesion repositorioSesion;
    private final RepositorioRol repositorioRol;
    
    public ServicioPersistencia() {
        this.repositorioEmpleado = new RepositorioEmpleado();
        this.repositorioEstado = new RepositorioEstado();
        this.repositorioMotivoTipo = new RepositorioMotivoTipo();
        this.repositorioOrdenDeInspeccion = new RepositorioOrdenDeInspeccion();
        this.repositorioSismografo = new RepositorioSismografo();
        this.repositorioCambioDeEstado = new RepositorioCambioDeEstado();
        this.repositorioMotivoFueraServicio = new RepositorioMotivoFueraServicio();
        this.repositorioEstacionSismologica = new RepositorioEstacionSismologica();
        this.repositorioUsuario = new RepositorioUsuario();
        this.repositorioSesion = new RepositorioSesion();
        this.repositorioRol = new RepositorioRol();
    }
    
    // =============== MÉTODOS DE CONSULTA ===============
    
    public List<Empleado> todosLosEmpleados() {
        return repositorioEmpleado.findAll();
    }
    
    public List<Estado> todosLosEstados() {
        return repositorioEstado.findAll();
    }
    
    public List<MotivoTipo> todosLosMotivos() {
        return repositorioMotivoTipo.findAll();
    }
    
    public List<OrdenDeInspeccion> todasLasOrdenes() {
        return repositorioOrdenDeInspeccion.findAll();
    }
    
    public List<Sismografo> todosLosSismografos() {
        return repositorioSismografo.findAll();
    }
    
    public List<EstacionSismologica> obtenerTodasLasEstaciones() {
        return repositorioEstacionSismologica.findAll();
    }
    
    public List<Usuario> todosLosUsuarios() {
        return repositorioUsuario.findAll();
    }
    
    public List<Sesion> todasLasSesiones() {
        return repositorioSesion.findAll();
    }
    
    public List<Rol> todosLosRoles() {
        return repositorioRol.findAll();
    }

    public Sesion obtenerSesionActiva(){
        return repositorioSesion.obtenerSesionActual();
    }
    
    // =============== MÉTODOS DE BÚSQUEDA POR ID ===============
    
    public Optional<Empleado> buscarEmpleado(Integer id) {
        return repositorioEmpleado.findById(id);
    }
    
    public Optional<Estado> buscarEstado(Integer id) {
        return repositorioEstado.findById(id);
    }
    
    public Optional<MotivoTipo> buscarMotivoTipo(Integer id) {
        return repositorioMotivoTipo.findById(id);
    }
    
    public Optional<OrdenDeInspeccion> buscarOrden(Integer id) {
        return repositorioOrdenDeInspeccion.findById(id);
    }
    
    public Optional<Sismografo> buscarSismografo(Integer id) {
        return repositorioSismografo.findById(id);
    }
    
    // =============== MÉTODOS DE MODIFICACIÓN  ===============
    
    public Sismografo actualizarSismografo(Sismografo sismografo) {
        return repositorioSismografo.update(sismografo);
    }
    
    public OrdenDeInspeccion actualizarOrden(OrdenDeInspeccion orden) {
        return repositorioOrdenDeInspeccion.update(orden);
    }

    public CambioDeEstado actualizarCambioDeEstado(CambioDeEstado cambio) {
        return repositorioCambioDeEstado.update(cambio);
    }
    
    public CambioDeEstado guardarCambioDeEstado(CambioDeEstado cambio) {
        return repositorioCambioDeEstado.save(cambio);
    }
    
    public MotivoFueraServicio guardarMotivoFueraServicio(MotivoFueraServicio motivo) {
        return repositorioMotivoFueraServicio.save(motivo);
    }
    
    // =============== MÉTODOS DE GUARDADO GENÉRICOS ===============
    
    public Empleado guardarEmpleado(Empleado empleado) {
        return repositorioEmpleado.save(empleado);
    }
    
    public Estado guardarEstado(Estado estado) {
        return repositorioEstado.save(estado);
    }
    
    public MotivoTipo guardarMotivoTipo(MotivoTipo motivoTipo) {
        return repositorioMotivoTipo.save(motivoTipo);
    }
    
    public OrdenDeInspeccion guardarOrden(OrdenDeInspeccion orden) {
        return repositorioOrdenDeInspeccion.save(orden);
    }
    
    public Sismografo guardarSismografo(Sismografo sismografo) {
        return repositorioSismografo.save(sismografo);
    }

    public Rol guardarRol(Rol rol) {
        return repositorioRol.save(rol);
    }

    public Usuario guardarUsuario(Usuario usuario) {
        return repositorioUsuario.save(usuario);
    }

    public Sesion guardarSesion(Sesion sesion) {
        return repositorioSesion.save(sesion);
    }

    public EstacionSismologica guardarEstacionSismologica(EstacionSismologica estacion) {
        return repositorioEstacionSismologica.save(estacion);
    }

    public boolean tieneEmpleados() {
        return repositorioEmpleado.count() > 0;
    }
}

