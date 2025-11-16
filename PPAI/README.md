# Sistema PPAI - Interfaz JavaFX

## Descripción
Sistema de Cierre de Órdenes de Inspección desarrollado con JavaFX y FXML.

## Tecnologías Utilizadas
- **Java 17**
- **JavaFX 21.0.1** 
- **Maven** para gestión de dependencias
- **FXML** para diseño de interfaces
- **Hibernate** para persistencia de datos
- **H2 Database** para base de datos

## Estructura del Proyecto

```
src/main/java/utn/dsi/ppai/
├── boundary/
│   ├── App.java                           # Clase principal JavaFX
│   ├── InterfazCierreInspeccion.java      # Interfaz de contrato
│   └── javafx/
│       ├── InterfazFX.java                # Implementación JavaFX
│       └── controllers/
│           └── MainController.java         # Controlador principal FXML
├── control/
│   └── GestorCierreInspeccion.java        # Lógica de negocio
├── entity/                                # Entidades del dominio
├── repositories/                          # Repositorios de datos
└── services/                             # Servicios auxiliares

src/main/resources/
├── fxml/
│   └── MainView.fxml                     # Diseño principal de la interfaz
├── styles/
│   └── nature-theme.css                  # Estilos CSS personalizados
└── META-INF/
    └── persistence.xml                   # Configuración JPA
```

## Funcionalidades Implementadas

### Interfaz Gráfica
- ✅ **Selección de Orden de Inspección** - ComboBox con órdenes disponibles
- ✅ **Ingreso de Observaciones** - TextArea para comentarios del cierre
- ✅ **Selección de Motivos** - CheckBoxes para múltiples motivos de cierre
- ✅ **Confirmación de Cierre** - Dialog de confirmación antes de procesar
- ✅ **Manejo de Errores** - Alertas informativas y de error
- ✅ **Tema Visual Personalizado** - Diseño basado en colores verdes (tema naturaleza)

### Arquitectura
- ✅ **Patrón MVC** - Separación clara entre vista, controlador y modelo
- ✅ **Interfaz de Contrato** - `InterfazCierreInspeccion` define el contrato
- ✅ **Gestor de Lógica** - `GestorCierreInspeccion` maneja la lógica de negocio
- ✅ **Controlador FXML** - `MainController` vincula la vista con la lógica

## Cómo Ejecutar

### Opción 1: Maven JavaFX Plugin (Recomendado)
```bash
mvn clean compile
mvn javafx:run
```

### Opción 2: Maven Exec Plugin
```bash
mvn clean compile exec:java
```

### Opción 3: Desde IDE
Ejecutar la clase `utn.dsi.ppai.boundary.App` como aplicación Java.

## Configuración del Proyecto

### Dependencias Principales (pom.xml)
- `openjfx:javafx-controls:21.0.1`
- `openjfx:javafx-fxml:21.0.1` 
- `hibernate-core:6.4.4.Final`
- `h2:2.2.224`

### Plugins Maven
- `javafx-maven-plugin:0.0.8` - Para ejecutar aplicaciones JavaFX
- `exec-maven-plugin:3.5.0` - Plugin alternativo de ejecución
- `maven-compiler-plugin:3.13.0` - Compilación Java 17

## Flujo de la Aplicación

1. **Inicio**: La aplicación carga las órdenes de inspección pendientes
2. **Selección**: El usuario selecciona una orden del ComboBox
3. **Observación**: Se ingresa la observación del cierre en el TextArea
4. **Motivos**: Se seleccionan uno o más motivos mediante CheckBoxes
5. **Confirmación**: Dialog de confirmación antes del cierre definitivo
6. **Procesamiento**: Se procesa el cierre y se muestra mensaje de éxito

## Validaciones Implementadas

- ✅ Orden de inspección debe estar seleccionada
- ✅ Observación no puede estar vacía
- ✅ Al menos un motivo debe estar seleccionado
- ✅ Confirmación explícita del usuario antes del cierre

## Mejoras Futuras

- [ ] Integración completa con base de datos real
- [ ] Notificaciones por email (ya existe `InterfazNotificacionMail`)
- [ ] Reportes de órdenes cerradas
- [ ] Filtros avanzados en la lista de órdenes
- [ ] Historial de cierres de órdenes

## Notas Técnicas

- **JavaFX Module Path**: Configurado automáticamente por Maven
- **FXML Loading**: Archivos FXML cargados desde classpath `/fxml/`
- **CSS Styling**: Estilos aplicados desde `/styles/nature-theme.css`
- **Base de Datos**: H2 embebida configurada en `persistence.xml`

---
**Desarrollado para PPAI - Sistema de Gestión de Inspecciones Sismológicas**