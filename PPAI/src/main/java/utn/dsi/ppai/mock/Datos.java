package utn.dsi.ppai.mock;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import utn.dsi.ppai.entity.*;

public class Datos {
    public static List<Empleado> listEmpleados = new ArrayList<>();
    public static List<Sismografo> listSismografos = new ArrayList<>();
    public static List<EstacionSismologica> listEstaciones = new ArrayList<>();
    public static List<Rol> listRoles = new ArrayList<>();
    public static List<MotivoTipo> listMotivosTipo = new ArrayList<>();
    public static List<CambioDeEstado> listCambiosDeEstado = new ArrayList<>();
    public static List<OrdenDeInspeccion> listOrdenesDeInspeccion = new ArrayList<>();
    public static List<Estado> listaDeTodosLosEstados = new ArrayList<>(); // Corregido
    public static Usuario usuario;
    public static Sesion sesion;


    public static void inicializarDatos() {
        // Limpiar listas para evitar duplicados si se llama varias veces
        listEmpleados.clear();
        listSismografos.clear();
        listEstaciones.clear();
        listRoles.clear();
        listMotivosTipo.clear();
        listCambiosDeEstado.clear();
        listOrdenesDeInspeccion.clear();
        usuario = null; // Reinicia el usuario logueado

        // 1. Crear Roles
        Rol rolAdmin = new Rol("Rol con privilegios completos", "Administrador");
        Rol rolInspector = new Rol("Rol para realizar inspecciones", "Inspector");
        Rol rolResponsableReparacion = new Rol("Rol para los responsables de reparacion", "Responsable Reparacion");
        listRoles.add(rolAdmin);
        listRoles.add(rolInspector);
        listRoles.add(rolResponsableReparacion);

        // 2. Crear Empleados
        Empleado emp1 = new Empleado("Perez", "juan.perez@redsismica.com", "Juan", "123456789", rolInspector);
        Empleado emp2 = new Empleado("Gomez", "maria.gomez@redsismica.com", "Maria", "987654321", rolResponsableReparacion);
        Empleado emp3 = new Empleado("Lopez", "mario.lopez@redsismica.com", "Mario", "123456789", rolResponsableReparacion);
        Empleado emp4 = new Empleado("Navarro","lucanavarro@gmail.com","Luca","123213123",rolResponsableReparacion);
        Empleado emp5 = new Empleado("Gardella","tomasitogardella@hotmail.com","TOMI","12321321",rolResponsableReparacion);
        Empleado emp6 = new Empleado("Peralta","santiperalta@outlook.com","Santi","12321321",rolResponsableReparacion);

        listEmpleados.add(emp1);
        listEmpleados.add(emp2);
        listEmpleados.add(emp3);
        listEmpleados.add(emp4);
        listEmpleados.add(emp5);
        listEmpleados.add(emp6);

        // 3. Crear un Usuario asignado a un empleado
        usuario = new Usuario("mlopez", "pass123", emp3);

        // 4. Crear Estados
        Estado estadoCompletamenteRealizado = new Estado(Estado.AMBITO_OI, Estado.ESTADO_COMPLETAMENTE_REALIZADO);
        Estado estadoCerrado = new Estado(Estado.AMBITO_OI, Estado.ESTADO_CERRADA);
        Estado estadoPendiente = new Estado(Estado.AMBITO_OI, "Pendiente");
        Estado estadoFueraDeServicio = new Estado(Estado.AMBITO_SISMOGRAFO, Estado.ESTADO_FUERA_SERVICIO);
        Estado estadoCompletamenteRechazado = new Estado(Estado.AMBITO_OI, Estado.ESTADO_COMPLETAMENTE_RECHAZADO);
        Estado estadoActivo = new Estado(Estado.AMBITO_SISMOGRAFO, Estado.ESTADO_ACTIVO);
        Estado estadoOnline = new Estado(Estado.AMBITO_SISMOGRAFO, Estado.ESTADO_ONLINE);
        listaDeTodosLosEstados.add(estadoCompletamenteRealizado);
        listaDeTodosLosEstados.add(estadoCerrado);
        listaDeTodosLosEstados.add(estadoPendiente);
        listaDeTodosLosEstados.add(estadoFueraDeServicio);
        listaDeTodosLosEstados.add(estadoCompletamenteRechazado);
        listaDeTodosLosEstados.add(estadoActivo);
        listaDeTodosLosEstados.add(estadoOnline);


        // 5. Crear Motivos de Tipo
        MotivoTipo mt1 = new MotivoTipo("Mantenimiento");
        MotivoTipo mt2 = new MotivoTipo("Falla de Equipo");
        MotivoTipo mt3 = new MotivoTipo("Calibración");
        listMotivosTipo.add(mt1);
        listMotivosTipo.add(mt2);
        listMotivosTipo.add(mt3);

        //6. Crear Sesion
        sesion = new Sesion(null, LocalDateTime.now(), usuario);

        //7. Crear Cambios de Estado
        CambioDeEstado cambio1 = new CambioDeEstado(LocalDateTime.of(2020,5,26,12,20,20), LocalDateTime.of(2020,5,23,12,20,20), estadoActivo, null, null);
        CambioDeEstado cambio2 = new CambioDeEstado( null , LocalDateTime.of(2020,5,26,12,20,21), estadoFueraDeServicio, null, null);

        List <CambioDeEstado> cambiosDeEstado = new ArrayList<>();
        cambiosDeEstado.add(cambio1);
        cambiosDeEstado.add(cambio2);


        //8. Crear Sismografos
        Sismografo sismo1 = new Sismografo(LocalDate.of(2020,5,26), 1, 10, estadoOnline, null,null);
        Sismografo sismo2 = new Sismografo(LocalDate.of(2021,7,4), 2, 20, estadoOnline, null,null);
        Sismografo sismo3 = new Sismografo(LocalDate.of(2019,10,15), 3, 30, estadoOnline, cambiosDeEstado,null);
        Sismografo sismo4 = new Sismografo(LocalDate.of(2023,2,8), 4, 40, estadoFueraDeServicio, cambiosDeEstado,null);
        Sismografo sismo5 = new Sismografo(LocalDate.of(2023,2,8), 5, 50, estadoOnline, cambiosDeEstado,null);
        Sismografo sismo6 = new Sismografo(LocalDate.of(2023,2,8), 6, 60, estadoOnline, cambiosDeEstado,null);
        Sismografo sismo7 = new Sismografo(LocalDate.of(2023,2,8), 7, 70, estadoOnline, cambiosDeEstado,null);
 

        // 9. Crear Estacion Sismologicas
        EstacionSismologica est1 = new EstacionSismologica(1, null, LocalDateTime.of(2020,5,26,12,20,20), 10, 10, "Estacion 2", 1, sismo1);
        EstacionSismologica est2 = new EstacionSismologica(2, null, LocalDateTime.of(2025,5,26,12,20,20), 20, 35, "Estacion 4", 1, sismo3);
        EstacionSismologica est3 = new EstacionSismologica(3, null, LocalDateTime.of(2021,5,26,12,20,20), 30, 20, "Estacion 3", 1, sismo2);
        EstacionSismologica est4 = new EstacionSismologica(4, null, LocalDateTime.of(2019,5,26,12,20,20), 40, -60, "Estacion 1", 1, sismo4);
        EstacionSismologica est5 = new EstacionSismologica(5, null, LocalDateTime.of(2019,5,26,12,20,20), 40, -60, "Estacion 5", 1, sismo5);
        EstacionSismologica est6 = new EstacionSismologica(6, null, LocalDateTime.of(2019,5,26,12,20,20), 40, -60, "Estacion 6", 1, sismo6);
        EstacionSismologica est7 = new EstacionSismologica(7, null, LocalDateTime.of(2019,5,26,12,20,20), 40, -60, "Estacion 7", 1, sismo7);
        listEstaciones.add(est1);
        listEstaciones.add(est2);
        listEstaciones.add(est3);
        listEstaciones.add(est4);
        listEstaciones.add(est5);
        listEstaciones.add(est6);
        listEstaciones.add(est7);
        sismo1.setEstacion(est1);
        sismo2.setEstacion(est3);
        sismo3.setEstacion(est2);
        sismo4.setEstacion(est4);
        sismo5.setEstacion(est5);
        sismo6.setEstacion(est6);
        sismo7.setEstacion(est7);
        
        // 10. Crear Ordenes de Inspeccion
        OrdenDeInspeccion orden1 = new OrdenDeInspeccion(
                1, // nroOrden
                LocalDateTime.of(2024,9,20,9,30,0), // fechaCreacion
                null, // fechaFin
                null, // fechaCierre
                null, // observacioCierre
                estadoPendiente, // estado
                est1, // estacionSismologica,
                emp1 // empleado
        );

        // Orden completamente Realizada
        OrdenDeInspeccion ordenCRealizada1 = new OrdenDeInspeccion(
                2, // nroOrden
                LocalDateTime.of(2020,1,18,7,30,0), // fechaCreacion
                LocalDateTime.of(2022,3,5,18,0,0), // fechaFin
                null, // fechaCierre
                null, // observacioCierre
                estadoCompletamenteRealizado, // estado
                est4, // estacionSismologica,
                emp3 // empleado
        );

        OrdenDeInspeccion ordenCRealizada2 = new OrdenDeInspeccion(
                3, // nroOrden
                LocalDateTime.of(2022,1,18,7,30,0), // fechaCreacion
                LocalDateTime.of(2024,9,20,18,0,0), // fechaFin
                null, // fechaCierre
                null, // observacionCierre
                estadoCompletamenteRealizado, // estado
                est3, // estacionSismologica,
                emp3 // empleado
        );

        OrdenDeInspeccion ordenCRealizada3 = new OrdenDeInspeccion(
                4, // nroOrden
                LocalDateTime.of(2019,1,18,7,30,0), // fechaCreacion
                LocalDateTime.of(2025,5,20,18,0,0), // fechaFin
                null, // fechaCierre
                null, // observacionCierre
                estadoCompletamenteRealizado, // estado
                est2, // estacionSismologica,
                emp3 // empleado
        );

        OrdenDeInspeccion ordenCRealizada4 = new OrdenDeInspeccion(
                5, // nroOrden
                LocalDateTime.of(2015,1,18,7,30,0), // fechaCreacion
                LocalDateTime.of(2020,1,20,18,0,0), // fechaFin
                null, // fechaCierre
                null, // observacionCierre
                estadoCompletamenteRealizado, // estado
                est5, // estacionSismologica,
                emp3 // empleado
        );


        OrdenDeInspeccion ordenCRealizada = new OrdenDeInspeccion(
                6, // nroOrden
                LocalDateTime.of(2022,1,18,7,30,0), // fechaCreacion
                LocalDateTime.of(2024,9,20,18,0,0), // fechaFin
                null, // fechaCierre
                null, // observacioCierre
                estadoCompletamenteRealizado, // estado
                est6, // estacionSismologica,
                emp3 // empleado
        );

        // Orden cerrada
        OrdenDeInspeccion ordenCerrada = new OrdenDeInspeccion(
                7, // nroOrden
                LocalDateTime.of(2022,1,18,7,30,0), // fechaCreacion
                LocalDateTime.of(2025,5,14,15,0,0), // fechaFin
                LocalDateTime.of(2025,5,26,10,21,0), // fechaCierre
                "Cerrada", // observacionCierre
                estadoCerrado, // estado
                est7, // estacionSismologica,
                emp2 // empleado
        );

        listOrdenesDeInspeccion.add(orden1);
        listOrdenesDeInspeccion.add(ordenCRealizada);
        listOrdenesDeInspeccion.add(ordenCRealizada1);
        listOrdenesDeInspeccion.add(ordenCRealizada2);
        listOrdenesDeInspeccion.add(ordenCRealizada3);
        listOrdenesDeInspeccion.add(ordenCRealizada4);
        listOrdenesDeInspeccion.add(ordenCerrada);

        listSismografos.add(sismo1);
        listSismografos.add(sismo2);
        listSismografos.add(sismo3);
        listSismografos.add(sismo4);
        listSismografos.add(sismo5);
        listSismografos.add(sismo6);
        listSismografos.add(sismo7);







        System.out.println("MockDatabase inicializado con datos de prueba.");
    }

    public List <Estado> getListaDeTodosLosEstados() {
        return listaDeTodosLosEstados;
    }

}