package utn.dsi.ppai.control;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import lombok.Data;
import utn.dsi.ppai.boundary.InterfazCierreInspeccion;
import utn.dsi.ppai.boundary.InterfazNotificacionMail;
import utn.dsi.ppai.boundary.PantallaCCRS;
import utn.dsi.ppai.entity.Empleado;
import utn.dsi.ppai.entity.Estado;
import utn.dsi.ppai.entity.MotivoTipo;
import utn.dsi.ppai.entity.OrdenDeInspeccion;
import utn.dsi.ppai.entity.Sesion;
import utn.dsi.ppai.entity.Sismografo;
import utn.dsi.ppai.entity.Usuario;
import utn.dsi.ppai.interfaces.IObservadorCierreInspeccion;
import utn.dsi.ppai.interfaces.ISujetoCierreInspeccion;



@Data
public class GestorCierreInspeccion implements ISujetoCierreInspeccion {
    private Empleado empleadoLogueado;
    private LocalDateTime fechaHoraActual;
    private String observacionCierre;
    private Sesion sesion;
    private InterfazCierreInspeccion pantallaCierreInspeccion;
    private InterfazNotificacionMail pantallaMail;
    private PantallaCCRS pantallaCCRS;
    private Usuario usuarioLogueado;
    private List<String> listOrdenesInspeccion = new ArrayList<>();
    private Estado estadoCompletamenteRealizado;
    private OrdenDeInspeccion ordenInspeccionSeleccionada;
    private String observacionOrdenCierre;
    private List<MotivoTipo> listSeleccionMotivo = new ArrayList<>();
    private List<String> listComentarioParaMotivo = new ArrayList<>();
    private Estado estadoCerrado;
    private Estado estadoFueraDeServicio;
    private List<MotivoTipo> listMotivoTipo = new ArrayList<>();
    private List<IObservadorCierreInspeccion> observadores = new ArrayList<>();
    //atributos auxiliares ya que debemos inyectar los datos desde el mock
    private List<String> listMailsResponsables = new ArrayList<>();
    private List<OrdenDeInspeccion> listaDeTodasLasOrdenes = new ArrayList<>();
    private List<Estado> listaTodosLosEstados = new ArrayList<>();
    private List<MotivoTipo> listaTodosLosMotivos = new ArrayList<>();
    private List<Empleado> listaDeEmpleados = new ArrayList<>();
    private List<Sismografo> listaDeSismografos = new ArrayList<>();
    private List<OrdenDeInspeccion> listaFiltradaYOrdenada = new ArrayList<>();

    public GestorCierreInspeccion(
            Empleado empleadoLogueado,
            LocalDateTime fechaHoraActual,
            String observacionCierre,
            Sesion sesion,
            InterfazCierreInspeccion pantallaCierreInspeccion,
            InterfazNotificacionMail pantallaMail,
            PantallaCCRS pantallaCCRS,
            Usuario usuarioLogueado
    ) {
        this.empleadoLogueado = empleadoLogueado;
        this.fechaHoraActual = fechaHoraActual;
        this.observacionCierre = observacionCierre;
        this.sesion = sesion;
        this.pantallaCierreInspeccion = pantallaCierreInspeccion;
        this.pantallaMail = pantallaMail;
        this.pantallaCCRS = pantallaCCRS;
        this.usuarioLogueado = usuarioLogueado;
    }


    // Metodos unicos
    public void buscarRILogueado() {
        setEmpleadoLogueado(sesion.obtenerRILogueado());
        this.buscarOIDeRI();
    }


    public void buscarOIDeRI() {
        List<OrdenDeInspeccion> ordenesFiltradas = new ArrayList<>();

        for (OrdenDeInspeccion orden : this.listaDeTodasLasOrdenes) {
            if (orden.sosDeEmpleado(this.empleadoLogueado) && orden.sosCompletamenteRealizado()) {
                ordenesFiltradas.add(orden);
            }
        }

        List<OrdenDeInspeccion> ordenadas = ordenarPorFechaDeFin(ordenesFiltradas);

        setListaFiltradaYOrdenada(ordenadas);

        List<String> listOrdenesInspeccion = new ArrayList<>();
        for (OrdenDeInspeccion orden : ordenadas) {
            listOrdenesInspeccion.add(orden.obtenerDatosOI());
        }


        for (int i = 0; i < ordenesFiltradas.size(); i++){
            for (int j = 0; j < this.listaDeSismografos.size(); j++){
                if (listaDeSismografos.get(j).esMiEstacion(ordenesFiltradas.get(i).getEstacionSismologica())) {
                    listOrdenesInspeccion.set(i, listOrdenesInspeccion.get(i) + " | Estación: " + listaDeSismografos.get(j).getEstacion().getIdentificadorSismografo());
                    break;
                }
            }

        }

        pantallaCierreInspeccion.solicitarSeleccionOI(listOrdenesInspeccion);
        setListOrdenesInspeccion(listOrdenesInspeccion);
    }
    

    public List<OrdenDeInspeccion> ordenarPorFechaDeFin(List<OrdenDeInspeccion> lista) {
        lista.sort(Comparator.comparing(OrdenDeInspeccion::getFechaHoraFinalizacion).reversed());
        return lista;
    }

    public void tomarSeleccionOI(String seleccion) {
        for (int i = 0; i < this.listOrdenesInspeccion.size(); i++) {
            if (this.listOrdenesInspeccion.get(i).equals(seleccion)) {
                this.setOrdenInspeccionSeleccionada(this.listaFiltradaYOrdenada.get(i));
                break;
            }
        }
        this.pedirObservacionOrdenCierre();
    }


    public void pedirObservacionOrdenCierre() {
        this.pantallaCierreInspeccion.pedirObservacionOrdenCierre();
    }

    public void tomarObservacionOrdenCierre(String observacion) {
        this.observacionOrdenCierre = observacion;
        this.habilitarActualizarSismografo();

    }

    public void habilitarActualizarSismografo() {
        List<String> listaAuxiliar = new ArrayList<>();
        for (MotivoTipo motivo : this.listaTodosLosMotivos) {
            listaAuxiliar.add(motivo.getDescripcion());
        }
        setListMotivoTipo(this.listaTodosLosMotivos);
        this.pantallaCierreInspeccion.solicitarSeleccionMotivo(listaAuxiliar);
    }


    public void tomarSeleccionMotivo(List<String> seleccionados) {
        for (String seleccionado : seleccionados) {
            MotivoTipo filtrado;
            filtrado = listMotivoTipo.stream()
                    .filter(motivo -> motivo.getDescripcion().equals(seleccionado))
                    .findFirst()
                    .orElse(null);

            if (filtrado != null) {
                this.listSeleccionMotivo.add(filtrado);
            }
        }
        for (String seleccionado : seleccionados) {
            this.pantallaCierreInspeccion.solicitarComentario(seleccionado);
        }
        this.obtenerConfirmacionOI();
    }

    
    public void tomarComentario(String comentario) {
        this.listComentarioParaMotivo.add(comentario);
    }

    public void obtenerConfirmacionOI() {
        pantallaCierreInspeccion.solicitarConfirmacionCierre();

    }


    public void tomarConfirmacionOI(boolean confirmacion) {
        if (confirmacion) {
            this.validarDatosMinimosRequeridos();
        } else {
            pantallaCierreInspeccion.mostrarError("Se cancelo el CU 'Dar cierre a orden de inspección de ES'");
        }
    }


    public void validarDatosMinimosRequeridos() {
        if (this.observacionOrdenCierre.isEmpty() ||
                this.listSeleccionMotivo.isEmpty() ||
                this.listComentarioParaMotivo.isEmpty()) {
            throw new IllegalStateException("Faltan datos para el cierre de la OI");
        } else {
            this.cerrarOI();
        }
    }


    public void cerrarOI() {
         this.setFechaHoraActual(LocalDateTime.now());
        for (Estado estado : this.listaTodosLosEstados) {
            if (estado.sosAmbitoOI() && estado.sosCerrada()) {
                this.setEstadoCerrado(estado);
                break;
            }
        }
        this.ordenInspeccionSeleccionada.setFechaHoraCierre(this.getFechaHoraActual());
        this.ordenInspeccionSeleccionada.cerrar(this.estadoCerrado);
        this.actualizarSismografo();
    }

    public void actualizarSismografo() {
        for (Estado estado : this.listaTodosLosEstados) {
            if (estado.sosAmbitoSismografo() && estado.sosFueraDeServicio()) {
                this.setEstadoFueraDeServicio(estado);
                break;
            }
        }

        this.ordenInspeccionSeleccionada.actualizarSismografo(this.listSeleccionMotivo, this.listComentarioParaMotivo, this.estadoFueraDeServicio, this.empleadoLogueado, this.getFechaHoraActual());
        this.obtenerMailResponsableReparacion();
    }

    public void obtenerMailResponsableReparacion() {
        for (Empleado empleado : this.listaDeEmpleados) {
            if (empleado.sosResponsableReparacion()) {
                this.listMailsResponsables.add(empleado.getMail());
            }
        }
        this.pantallaMail = new InterfazNotificacionMail();
        this.pantallaCCRS = new PantallaCCRS();
        List<IObservadorCierreInspeccion> auxObservadores = new ArrayList<>();
        auxObservadores.add(pantallaMail);
        auxObservadores.add(pantallaCCRS);
        this.suscribir(auxObservadores);
    }
    
    @Override
    public void suscribir(List<IObservadorCierreInspeccion> observadores) {
        for (IObservadorCierreInspeccion obs : observadores) {
            this.observadores.add(obs);
        }
    }


    @Override
    public void desuscribir(IObservadorCierreInspeccion observador){
        for (int i = 0; i < this.observadores.size(); i++){
            if (this.observadores.get(i).equals(observador)){
                this.observadores.remove(i);
                break;
            }
        }
    }

    @Override
    public void notificar(){
        for(IObservadorCierreInspeccion obs : this.observadores){
            obs.actualizar(this.listMailsResponsables, this.ordenInspeccionSeleccionada.getEstacionSismologica().getSismografo().getIdentificadorSismografo(), this.estadoFueraDeServicio.getNombreEstado(), this.getFechaHoraActual(),this.listSeleccionMotivo, this.listComentarioParaMotivo);
        }
}

    public void iniciarCierreOI(List<Estado> listaDeEstados, List<MotivoTipo> listaDeMotivos, List<Empleado> listaEmpleados, Sesion sesion, List<OrdenDeInspeccion> listaDeOrdenesInspeccion, List<Sismografo> listaDeSismografos) {
        setListaTodosLosEstados(listaDeEstados);
        setListaTodosLosMotivos(listaDeMotivos);
        setListaDeEmpleados(listaEmpleados);
        setListaDeTodasLasOrdenes(listaDeOrdenesInspeccion);
        setSesion(sesion);
        setListaDeSismografos(listaDeSismografos);
        this.buscarRILogueado();

}
    public void finCU() {
        System.exit(0);
    }

    }