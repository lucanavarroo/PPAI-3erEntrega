package utn.dsi.ppai.services;

import java.util.HashMap;
import java.util.Map;

import utn.dsi.ppai.entity.CambioDeEstado;
import utn.dsi.ppai.entity.Empleado;
import utn.dsi.ppai.entity.EstacionSismologica;
import utn.dsi.ppai.entity.Estado;
import utn.dsi.ppai.entity.MotivoFueraServicio;
import utn.dsi.ppai.entity.MotivoTipo;
import utn.dsi.ppai.entity.OrdenDeInspeccion;
import utn.dsi.ppai.entity.Rol;
import utn.dsi.ppai.entity.Sismografo;
import utn.dsi.ppai.mock.Datos;
import utn.dsi.ppai.entity.Usuario;

public class InitializadorDatos {
    
    private ServicioPersistencia servicioPersistencia;
    
    // Maps para mantener referencias de entidades guardadas
    private Map<Integer, Estado> estadosGuardados = new HashMap<>();
    private Map<Integer, Rol> rolesGuardados = new HashMap<>();
    private Map<Integer, MotivoTipo> motivosGuardados = new HashMap<>();
    private Map<Integer, Empleado> empleadosGuardados = new HashMap<>();
    
    public InitializadorDatos() {
        this.servicioPersistencia = new ServicioPersistencia();
    }
    
    /**
     * Puebla la base de datos con datos mock si está vacía
     */
    public void poblarBaseDeDatos() {
        System.out.println("🔄 Verificando si la base de datos necesita ser poblada...");
        
        try {
            // Verificar si ya hay datos
            if (servicioPersistencia.tieneEmpleados()) {
                System.out.println("✅ La base de datos ya contiene datos");
                return;
            }
            
            System.out.println("📊 Base de datos vacía, cargando datos mock...");
            
            // Inicializar datos mock
            Datos.inicializarDatos();
            
            // ✅ DEBUG - Verificar datos mock
            System.out.println("🔍 Órdenes en mock: " + (Datos.listOrdenesDeInspeccion != null ? Datos.listOrdenesDeInspeccion.size() : "NULL"));
            
            // Cargar en orden correcto (respetando dependencias)
            cargarDatosBasicos();
            cargarUsuarioYSesion();
            cargarEstacionesYSismografos();
            
            System.out.println("🔄 Iniciando carga de órdenes...");
            cargarOrdenesDeInspeccion();
            
            System.out.println("✅ Base de datos poblada exitosamente con datos mock");
            
        } catch (Exception e) {
            System.err.println("❌ Error poblando base de datos: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error cargando datos mock", e);
        }
    }
    
    private void cargarDatosBasicos() {
        System.out.println("  📋 Cargando datos básicos...");
        
        // ✅ CORREGIDO - Usar nombres correctos de Datos.java
        // Cargar Estados
        for (Estado estado : Datos.listaDeTodosLosEstados) {
            Integer idOriginal = estado.getIdEstado();
            Estado estadoGuardado = servicioPersistencia.guardarEstado(estado);
            estadosGuardados.put(idOriginal, estadoGuardado);
        }
        
        // Cargar Roles
        for (Rol rol : Datos.listRoles) {
            Integer idOriginal = rol.getIdRol();
            Rol rolGuardado = servicioPersistencia.guardarRol(rol);
            rolesGuardados.put(idOriginal, rolGuardado);
        }
        
        // ✅ CORREGIDO - Usar nombre correcto
        // Cargar MotivoTipos
        for (MotivoTipo motivoTipo : Datos.listMotivosTipo) {
            Integer idOriginal = motivoTipo.getIdMotivoTipo();
            MotivoTipo motivoGuardado = servicioPersistencia.guardarMotivoTipo(motivoTipo);
            motivosGuardados.put(idOriginal, motivoGuardado);
        }
        
        System.out.println("  ✅ Datos básicos cargados");
    }
    
    private void cargarUsuarioYSesion() {
        System.out.println("  👥 Cargando empleados, usuarios y sesiones...");
        
        // Cargar Empleados con referencias actualizadas
        for (Empleado empleado : Datos.listEmpleados) {
            if (empleado.getRol() != null) {
                Integer idRolOriginal = empleado.getRol().getIdRol();
                Rol rolActualizado = rolesGuardados.get(idRolOriginal);
                if (rolActualizado != null) {
                    empleado.setRol(rolActualizado);
                }
            }
            
            Integer idOriginal = empleado.getIdEmpleado();
            Empleado empleadoGuardado = servicioPersistencia.guardarEmpleado(empleado);
            empleadosGuardados.put(idOriginal, empleadoGuardado);
        }
        
        // ✅ CORREGIDO - Guardar Usuario y mantener la referencia
        Usuario usuarioGuardado = null;
        if (Datos.usuario != null) {
            if (Datos.usuario.getEmpleado() != null) {
                Integer idEmpleadoOriginal = Datos.usuario.getEmpleado().getIdEmpleado();
                Empleado empleadoActualizado = empleadosGuardados.get(idEmpleadoOriginal);
                if (empleadoActualizado != null) {
                    Datos.usuario.setEmpleado(empleadoActualizado);
                }
            }
            usuarioGuardado = servicioPersistencia.guardarUsuario(Datos.usuario);
            System.out.println("✅ Usuario guardado con ID: " + usuarioGuardado.getIdUsuario());
        }
        
        // ✅ CORREGIDO - Usar el usuario GUARDADO en la Sesión
        if (Datos.sesion != null && usuarioGuardado != null) {
            // IMPORTANTE: Usar el usuario guardado (con ID válido)
            Datos.sesion.setUsuario(usuarioGuardado);
            servicioPersistencia.guardarSesion(Datos.sesion);
            System.out.println("✅ Sesión guardada correctamente");
        } else if (Datos.sesion != null && usuarioGuardado == null) {
            System.err.println("⚠️ No se pudo guardar la sesión: usuario no guardado");
        }
        
        System.out.println("  ✅ Empleados, usuarios y sesiones cargados");
    }
    
    private void cargarEstacionesYSismografos() {
        System.out.println("  🏗️ Cargando estaciones y sismógrafos...");
        
        // MAPEAR estaciones guardadas
        Map<Integer, EstacionSismologica> estacionesGuardadas = new HashMap<>();
        
        for (EstacionSismologica estacion : Datos.listEstaciones) {
            Integer idOriginal = estacion.getCodigoEstacion();
            EstacionSismologica estacionGuardada = servicioPersistencia.guardarEstacionSismologica(estacion);
            estacionesGuardadas.put(idOriginal, estacionGuardada);
        }
        
        // ACTUALIZAR referencias en Sismógrafos
        for (Sismografo sismografo : Datos.listSismografos) {
            // Actualizar referencia de EstacionSismologica
            if (sismografo.getEstacion() != null) {
                Integer idEstacionOriginal = sismografo.getEstacion().getCodigoEstacion();
                EstacionSismologica estacionActualizada = estacionesGuardadas.get(idEstacionOriginal);
                if (estacionActualizada != null) {
                    sismografo.setEstacion(estacionActualizada);
                }
            }
            
            // Actualizar referencia de Estado
            if (sismografo.getEstadoActual() != null) {
                Integer idEstadoOriginal = sismografo.getEstadoActual().getIdEstado();
                Estado estadoActualizado = estadosGuardados.get(idEstadoOriginal);
                if (estadoActualizado != null) {
                    sismografo.setEstadoActual(estadoActualizado);
                }
            }
            
            // ACTUALIZAR referencias en CambiosDeEstado
            if (sismografo.getCambiosDeEstado() != null && !sismografo.getCambiosDeEstado().isEmpty()) {
                for (CambioDeEstado cambio : sismografo.getCambiosDeEstado()) {
                    
                    // Actualizar Estado en CambioDeEstado
                    if (cambio.getEstado() != null) {
                        Integer idEstadoCambioOriginal = cambio.getEstado().getIdEstado();
                        Estado estadoCambioActualizado = estadosGuardados.get(idEstadoCambioOriginal);
                        if (estadoCambioActualizado != null) {
                            cambio.setEstado(estadoCambioActualizado);
                        }
                    }
                    
                    // Actualizar Empleado SOLO si no es null
                    if (cambio.getResponsableInspeccion() != null) {
                        Integer idEmpleadoOriginal = cambio.getResponsableInspeccion().getIdEmpleado();
                        Empleado empleadoActualizado = empleadosGuardados.get(idEmpleadoOriginal);
                        if (empleadoActualizado != null) {
                            cambio.setResponsableInspeccion(empleadoActualizado);
                        }
                    }
                    
                    // Actualizar MotivoFueraServicio si existe
                    if (cambio.getMotivoFueraServicio() != null && !cambio.getMotivoFueraServicio().isEmpty()) {
                        for (MotivoFueraServicio motivo : cambio.getMotivoFueraServicio()) {
                            if (motivo.getMotivoTipo() != null) {
                                Integer idMotivoTipoOriginal = motivo.getMotivoTipo().getIdMotivoTipo();
                                MotivoTipo motivoTipoActualizado = motivosGuardados.get(idMotivoTipoOriginal);
                                if (motivoTipoActualizado != null) {
                                    motivo.setMotivoTipo(motivoTipoActualizado);
                                }
                            }
                        }
                    }
                }
            }
            
            servicioPersistencia.guardarSismografo(sismografo);
        }
        
        System.out.println("  ✅ Estaciones y sismógrafos cargados");
    }
    
    // ✅ AGREGAR ESTE MÉTODO FALTANTE
    private void cargarOrdenesDeInspeccion() {
        System.out.println("  📋 Cargando órdenes de inspección...");
        
        // ✅ CORRECCIÓN - Usar Map con clave compuesta (nombre + ámbito)
        Map<String, Estado> estadosPorNombreYAmbito = new HashMap<>();
        for (Estado estado : estadosGuardados.values()) {
            String clave = estado.getNombreEstado() + "|" + estado.getAmbito();
            estadosPorNombreYAmbito.put(clave, estado);
            System.out.println("🔧 Estado disponible: '" + estado.getNombreEstado() + "' - Ámbito: '" + estado.getAmbito() + "'");
        }
        
        // Obtener todas las estaciones guardadas
        Map<Integer, EstacionSismologica> estacionesGuardadas = new HashMap<>();
        for (EstacionSismologica estacion : servicioPersistencia.obtenerTodasLasEstaciones()) {
            estacionesGuardadas.put(estacion.getCodigoEstacion(), estacion);
        }
        
        int ordenesGuardadas = 0;
        for (OrdenDeInspeccion orden : Datos.listOrdenesDeInspeccion) {
            System.out.println("🔧 Procesando orden " + orden.getNumeroOrden() + 
                              " - Estado original: '" + orden.getEstado().getNombreEstado() + 
                              "' - Ámbito: '" + orden.getEstado().getAmbito() + "'");
            
            // ✅ CORRECCIÓN - Buscar estado usando la clave compuesta
            if (orden.getEstado() != null) {
                String claveEstado = orden.getEstado().getNombreEstado() + "|" + orden.getEstado().getAmbito();
                Estado estadoCorregido = estadosPorNombreYAmbito.get(claveEstado);
                
                if (estadoCorregido != null) {
                    orden.setEstado(estadoCorregido);
                    System.out.println("✅ Estado actualizado para orden " + orden.getNumeroOrden() + 
                                      ": '" + estadoCorregido.getNombreEstado() + "' - Ámbito: '" + estadoCorregido.getAmbito() + "'");
                } else {
                    System.err.println("❌ No se encontró estado para clave: " + claveEstado);
                }
            }
            
            // Actualizar Empleado
            if (orden.getEmpleado() != null) {
                Integer idEmpleadoOriginal = orden.getEmpleado().getIdEmpleado();
                Empleado empleadoActualizado = empleadosGuardados.get(idEmpleadoOriginal);
                if (empleadoActualizado != null) {
                    orden.setEmpleado(empleadoActualizado);
                }
            }
            
            // Actualizar EstacionSismologica
            if (orden.getEstacionSismologica() != null) {
                Integer codigoEstacionOriginal = orden.getEstacionSismologica().getCodigoEstacion();
                EstacionSismologica estacionActualizada = estacionesGuardadas.get(codigoEstacionOriginal);
                if (estacionActualizada != null) {
                    orden.setEstacionSismologica(estacionActualizada);
                }
            }
            
            servicioPersistencia.guardarOrden(orden);
            ordenesGuardadas++;
        }
        
        System.out.println("  ✅ Órdenes de inspección cargadas: " + ordenesGuardadas);
    }
}
