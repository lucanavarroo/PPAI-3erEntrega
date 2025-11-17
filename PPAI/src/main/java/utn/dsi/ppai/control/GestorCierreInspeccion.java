package utn.dsi.ppai.control;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import lombok.Data;
import utn.dsi.ppai.boundary.InterfazCierreInspeccion;
import utn.dsi.ppai.boundary.InterfazNotificacionMail;
import utn.dsi.ppai.boundary.PantallaCCRS;
import utn.dsi.ppai.entity.CambioDeEstado;
import utn.dsi.ppai.entity.Empleado;
import utn.dsi.ppai.entity.Estado;
import utn.dsi.ppai.entity.MotivoTipo;
import utn.dsi.ppai.entity.OrdenDeInspeccion;
import utn.dsi.ppai.entity.Sesion;
import utn.dsi.ppai.entity.Sismografo;
import utn.dsi.ppai.entity.Usuario;
import utn.dsi.ppai.interfaces.IObservadorCierreInspeccion;
import utn.dsi.ppai.interfaces.ISujetoCierreInspeccion;
import utn.dsi.ppai.persistence.DataBaseManager;
import utn.dsi.ppai.services.InitializadorDatos;
import utn.dsi.ppai.services.ServicioPersistencia;


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
    private List<MotivoTipo> listSeleccionMotivo = new ArrayList<>();
    private List<String> listComentarioParaMotivo = new ArrayList<>();
    private Estado estadoCerrado;
    private Estado estadoFueraDeServicio;
    private List<MotivoTipo> listMotivoTipo = new ArrayList<>();
    private List<IObservadorCierreInspeccion> observadores = new ArrayList<>();
    private String notif;
    //atributos auxiliares ya que debemos inyectar los datos desde el mock
    private List<String> listMailsResponsables = new ArrayList<>();
    private List<OrdenDeInspeccion> listaDeTodasLasOrdenes = new ArrayList<>();
    private List<Estado> listaTodosLosEstados = new ArrayList<>();
    private List<MotivoTipo> listaTodosLosMotivos = new ArrayList<>();
    private List<Empleado> listaDeEmpleados = new ArrayList<>();
    private List<Sismografo> listaDeSismografos = new ArrayList<>();
    private List<OrdenDeInspeccion> listaFiltradaYOrdenada = new ArrayList<>();
    private ServicioPersistencia servicioPersistencia = new ServicioPersistencia();

    public GestorCierreInspeccion(
            LocalDateTime fechaHoraActual,
            String observacionCierre,
            InterfazCierreInspeccion pantallaCierreInspeccion
    ) {
        this.inicializarPersistencia();
        this.servicioPersistencia = new ServicioPersistencia();
        this.sesion = this.servicioPersistencia.obtenerSesionActiva();
        this.usuarioLogueado = this.sesion.getUsuario();
        this.empleadoLogueado = null;
        this.fechaHoraActual = fechaHoraActual;
        this.observacionCierre = observacionCierre;
        this.pantallaCierreInspeccion = pantallaCierreInspeccion;
        this.listaTodosLosEstados = this.servicioPersistencia.todosLosEstados();
        this.listaTodosLosMotivos = this.servicioPersistencia.todosLosMotivos();
        this.listaDeEmpleados = this.servicioPersistencia.todosLosEmpleados();
        this.listaDeTodasLasOrdenes = this.servicioPersistencia.todasLasOrdenes();
        this.listaDeSismografos = this.servicioPersistencia.todosLosSismografos();
    }

    private void inicializarPersistencia() {
    try {
        System.out.println("🔄 Inicializando sistema de persistencia...");
        
        // Inicializar base de datos
        DataBaseManager.inicializarBaseDeDatos();
        
        // Poblar si es necesario
        ServicioPersistencia servicioTemp = new ServicioPersistencia();
        if (!servicioTemp.tieneEmpleados()) {
            System.out.println("📊 Base de datos vacía, poblando con datos mock...");
            InitializadorDatos inicializador = new InitializadorDatos();
            inicializador.poblarBaseDeDatos();
        } else {
            System.out.println("✅ Base de datos ya contiene datos");
        }
        
        System.out.println("✅ Sistema de persistencia inicializado correctamente");
        
    } catch (Exception e) {
        System.err.println("❌ Error inicializando persistencia: " + e.getMessage());
        throw new RuntimeException("No se pudo inicializar el sistema de persistencia", e);
    }
}
     

    // Metodos unicos
    public void buscarRILogueado() {
        setEmpleadoLogueado(sesion.obtenerRILogueado());
        this.buscarOIDeRI();
    }


    public void buscarOIDeRI() {
        List<OrdenDeInspeccion> ordenesFiltradas = new ArrayList<>();

        System.out.println("🔍 DEBUG - Total órdenes disponibles: " + this.listaDeTodasLasOrdenes.size());
        System.out.println("🔍 DEBUG - Empleado logueado: " + (this.empleadoLogueado != null ? this.empleadoLogueado.getNombre() : "NULL"));
        System.out.println("🔍 DEBUG - Constante ESTADO_COMPLETAMENTE_REALIZADO: '" + Estado.ESTADO_COMPLETAMENTE_REALIZADO + "'");

        for (OrdenDeInspeccion orden : this.listaDeTodasLasOrdenes) {
            // DEBUG ADICIONAL - Ver el estado de cada orden
            String estadoNombre = orden.getEstado() != null ? orden.getEstado().getNombreEstado() : "NULL";
            String estadoAmbito = orden.getEstado() != null ? orden.getEstado().getAmbito() : "NULL";
            
            System.out.println("🔍 DEBUG - Orden " + orden.getNumeroOrden() + 
                              " - Estado: '" + estadoNombre + "'" +
                              " - Ámbito: '" + estadoAmbito + "'" +
                              " - sosDeEmpleado: " + orden.sosDeEmpleado(this.empleadoLogueado) + 
                              " - sosCompletamenteRealizado: " + orden.sosCompletamenteRealizado());
            
            // Comparación manual para debug
            if (orden.getEstado() != null) {
                boolean comparacionManual = Estado.ESTADO_COMPLETAMENTE_REALIZADO.equalsIgnoreCase(estadoNombre);
                System.out.println("    🔬 Comparación manual: '" + Estado.ESTADO_COMPLETAMENTE_REALIZADO + "' vs '" + estadoNombre + "' = " + comparacionManual);
            }
            
            if (orden.sosDeEmpleado(this.empleadoLogueado) && orden.sosCompletamenteRealizado()) {
                ordenesFiltradas.add(orden);
            }
        }

        System.out.println("🔍 DEBUG - Órdenes después del filtro: " + ordenesFiltradas.size());
        
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
        this.observacionCierre = observacion;
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


    public void tomarConfirmacionOI(boolean confirmacion, String notif) {
        setNotif(notif);
        if (confirmacion) {
            this.validarDatosMinimosRequeridos();
        } else {
            pantallaCierreInspeccion.mostrarError("Se cancelo el CU 'Dar cierre a orden de inspección de ES'");
        }
    }


    public void validarDatosMinimosRequeridos() {
        if (this.observacionCierre.isEmpty() ||
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
        this.ordenInspeccionSeleccionada.cerrar(this.estadoCerrado, this.observacionCierre);
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
        this.servicioPersistencia.actualizarOrden(this.ordenInspeccionSeleccionada);
        
        Sismografo sismografo = this.ordenInspeccionSeleccionada.getEstacionSismologica().getSismografo();
        this.servicioPersistencia.actualizarSismografo(sismografo);
        
        // ✅ VALIDAR SI EXISTE CAMBIO DE ESTADO ANTERIOR
        CambioDeEstado cambioAnterior = sismografo.obtenerCambioDeEstadoAnterior();
        if (cambioAnterior != null) {
            this.servicioPersistencia.actualizarCambioDeEstado(cambioAnterior);
        }
        
        this.obtenerMailResponsableReparacion();
    }

    public void obtenerMailResponsableReparacion() {
        for (Empleado empleado : this.listaDeEmpleados) {
            if (empleado.sosResponsableReparacion()) {
                this.listMailsResponsables.add(empleado.getMail());
            }
        }

         List<IObservadorCierreInspeccion> auxObservadores = new ArrayList<>();
        if(this.notif.equals("A")){
            // Ambos: Mail y CCRS
            this.pantallaMail = new InterfazNotificacionMail();
            this.pantallaCCRS = new PantallaCCRS();
            auxObservadores.add(pantallaMail);
            auxObservadores.add(pantallaCCRS);
        } else if(this.notif.equals("M")){
            // Solo Mail
            this.pantallaMail = new InterfazNotificacionMail();
            auxObservadores.add(pantallaMail);
        } else if(this.notif.equals("C")){
            // Solo CCRS
            this.pantallaCCRS = new PantallaCCRS();
            auxObservadores.add(pantallaCCRS);
        }

        this.suscribir(auxObservadores);
        this.notificar();
        this.finCU();
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
        List<String> listaMotivos = new ArrayList<>();

        for (MotivoTipo motivo : this.listSeleccionMotivo){
            listaMotivos.add(motivo.getDescripcion());
        }

        for(IObservadorCierreInspeccion obs : this.observadores){
            obs.actualizar(this.listMailsResponsables, this.ordenInspeccionSeleccionada.getEstacionSismologica().getSismografo().getIdentificadorSismografo(), this.estadoFueraDeServicio.getNombreEstado(), this.getFechaHoraActual(), listaMotivos, this.listComentarioParaMotivo);
        }
}

    public void iniciarCierreOI() {
        this.buscarRILogueado();

}
    public void finCU() {
        System.exit(0);
    }

    }