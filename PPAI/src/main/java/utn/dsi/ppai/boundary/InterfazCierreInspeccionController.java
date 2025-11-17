package utn.dsi.ppai.boundary;

import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import utn.dsi.ppai.control.GestorCierreInspeccion;

public class InterfazCierreInspeccionController implements InterfazCierreInspeccion {

    @FXML
    private Label lblUsuarioNombre;
    
    @FXML
    private Label lblUsuarioRol;
    
    @FXML
    private StackPane screenContainer;
    
    @FXML
    private VBox pantallaBienvenida;
    
    @FXML
    private VBox pantallaOrden;
    
    @FXML
    private ComboBox<String> comboOrdenes;
    
    @FXML
    private VBox pantallaObservacion;
    
    @FXML
    private TextArea txtObservacion;
    
    @FXML
    private Label errorObservacion;
    
    @FXML
    private VBox pantallaMotivos;
    
    @FXML
    private VBox motivosCheckBoxContainer;
    
    @FXML
    private Label motivosError;
    
    @FXML
    private VBox pantallaResumen;
    
    @FXML
    private Label lblResumenOrden;
    
    @FXML
    private Label lblResumenObservacion;
    
    @FXML
    private VBox resumenMotivos;
    
    @FXML
    private CheckBox chkNotificarMail;
    
    @FXML
    private CheckBox chkNotificarPantalla;
    
    @FXML
    private Label errorNotificacion;
    
    private boolean ignorarCambios = false;
    
    @FXML
    private javafx.scene.control.Button btnCancelarGlobal;

    @FXML
    private javafx.scene.control.Button btnConfirmarFinal;

    // Breadcrumb Labels
    @FXML
    private Label breadcrumb1;
    
    @FXML
    private Label breadcrumb2;
    
    @FXML
    private Label breadcrumb3;
    
    @FXML
    private Label breadcrumb4;
    
    @FXML
    private Label breadcrumb5;

    private GestorCierreInspeccion gestor;
    private List<CheckBox> checkBoxesMotivos;
    private List<String> motivosSeleccionados;
    private List<TextArea> textAreasComentarios;
    private java.util.Map<String, String> comentariosPorMotivo;
    private String ordenActual;
    private String observacionActual;

    public InterfazCierreInspeccionController() {
        checkBoxesMotivos = new ArrayList<>();
        motivosSeleccionados = new ArrayList<>();
        textAreasComentarios = new ArrayList<>();
        comentariosPorMotivo = new java.util.HashMap<>();
        setupGestor();
    }

    @FXML
    private void initialize() {
        // Ocultar todas las pantallas excepto la de bienvenida
        pantallaBienvenida.setVisible(true);
        pantallaOrden.setVisible(false);
        pantallaObservacion.setVisible(false);
        pantallaMotivos.setVisible(false);
        pantallaResumen.setVisible(false);
        btnCancelarGlobal.setVisible(false);
        errorObservacion.setVisible(false);
        motivosError.setVisible(false);

        // Configurar validación de observación
        txtObservacion.textProperty().addListener((obs, old, newValue) -> {
            errorObservacion.setVisible(false);
        });
        
        // Configurar checkboxes exclusivos de notificación
        configurarCheckboxExclusivos();

        // Iniciar la pantalla
        habilitarPantalla();
        
        // Iniciar el gestor
        try {
            gestor.iniciarCierreOI();
        } catch (Exception e) {
            mostrarError("Error al inicializar: " + e.getMessage());
        }
    }

    private void setupGestor() {
        try {
            gestor = new GestorCierreInspeccion(
                java.time.LocalDateTime.now(),
                "",
                this
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ============ MÉTODOS DE NAVEGACIÓN ENTRE PANTALLAS ============

    @FXML
    private void irAPantalla1() {
        // Cambiar de pantalla de Bienvenida a Orden
        mostrarPantalla(pantallaOrden);
        actualizarBreadcrumb(2);
        btnCancelarGlobal.setVisible(true);
    }

    @FXML
    private void irAPantalla2() {
        // Validar que se haya seleccionado una orden
        String ordenSeleccionada = comboOrdenes.getSelectionModel().getSelectedItem();
        if (ordenSeleccionada == null || ordenSeleccionada.isEmpty()) {
            mostrarError("Debe seleccionar una orden");
            return;
        }
        
        ordenActual = ordenSeleccionada;
        System.out.println("DEBUG irAPantalla2 - Orden seleccionada: " + ordenSeleccionada);
        
        // IMPORTANTE: Informar al gestor cuál orden se seleccionó
        gestor.tomarSeleccionOI(ordenSeleccionada);
        
        // Cambiar de pantalla de Orden a Observación
        mostrarPantalla(pantallaObservacion);
        actualizarBreadcrumb(3);
        txtObservacion.clear();
        errorObservacion.setVisible(false);
    }

    @FXML
    private void irAPantalla3() {
        // Validar que se haya ingresado una observación
        String observacion = txtObservacion.getText().trim();
        if (observacion.isEmpty()) {
            errorObservacion.setText("Debe ingresar una observación");
            errorObservacion.setVisible(true);
            return;
        }
        
        System.out.println("DEBUG irAPantalla3 - Observación válida: " + observacion);
        observacionActual = observacion;
        
        // AQUÍ: Enviar la observación al gestor, que es quien llamará a solicitarSeleccionMotivo()
        System.out.println("DEBUG irAPantalla3 - Llamando al gestor para procesar observación...");
        gestor.tomarObservacionOrdenCierre(observacion);
        
        // NO cambiar de pantalla aquí - el gestor lo hará llamando a solicitarSeleccionMotivo()
    }

    @FXML
    private void irAPantalla4() {
        // Validar que se haya seleccionado al menos un motivo
        motivosSeleccionados.clear();
        for (CheckBox checkBox : checkBoxesMotivos) {
            if (checkBox.isSelected()) {
                motivosSeleccionados.add(checkBox.getText());
            }
        }

        if (motivosSeleccionados.isEmpty()) {
            motivosError.setText("Debe seleccionar al menos un motivo");
            motivosError.setVisible(true);
            return;
        }

        motivosError.setVisible(false);
        System.out.println("DEBUG irAPantalla4 - Enviando " + motivosSeleccionados.size() + " motivos al gestor");
        
        // ENVIAR la selección de motivos al gestor
        // El gestor iterará y solicitará comentarios uno por uno
        this.tomarSeleccionMotivo(motivosSeleccionados);
        
        // NO cambiar de pantalla aquí - el gestor lo hará cuando complete la iteración
    }

    @FXML
    private void irAPantallaBienvenida() {
        // Volver a pantalla de Bienvenida (reset completo)
        mostrarPantalla(pantallaBienvenida);
        btnCancelarGlobal.setVisible(false);
        reiniciarPantalla();
    }

    private void mostrarPantalla(VBox pantalla) {
        Platform.runLater(() -> {
            // Ocultar todas las pantallas
            pantallaBienvenida.setVisible(false);
            pantallaOrden.setVisible(false);
            pantallaObservacion.setVisible(false);
            pantallaMotivos.setVisible(false);
            pantallaResumen.setVisible(false);
            
            // Mostrar la pantalla especificada
            pantalla.setVisible(true);
        });
    }

    private void actualizarBreadcrumb(int pasoActual) {
        Platform.runLater(() -> {
            // Colores mejorados
            String completado = "-fx-text-fill: #ffffff; -fx-font-weight: bold;";   // Blanco completado
            String actual = "-fx-text-fill: #ffffff; -fx-font-weight: bold;";       // Blanco actual
            String pendiente = "-fx-text-fill: #6b7280;";                           // Gris oscuro
            
            // Resetear todos
            breadcrumb1.setStyle(pendiente);
            breadcrumb2.setStyle(pendiente);
            breadcrumb3.setStyle(pendiente);
            breadcrumb4.setStyle(pendiente);
            breadcrumb5.setStyle(pendiente);
            
            // Marcar pasos completados con emoticon a la derecha
            if (pasoActual >= 1) {
                breadcrumb1.setText("Inicio ✅");
                breadcrumb1.setStyle(completado);
            }
            if (pasoActual > 1) {
                breadcrumb2.setText("Seleccionar Orden ✅");
                breadcrumb2.setStyle(completado);
            }
            if (pasoActual > 2) {
                breadcrumb3.setText("Observación ✅");
                breadcrumb3.setStyle(completado);
            }
            if (pasoActual > 3) {
                breadcrumb4.setText("Motivos ✅");
                breadcrumb4.setStyle(completado);
            }
            if (pasoActual > 4) {
                breadcrumb5.setText("Resumen ✅");
                breadcrumb5.setStyle(completado);
            }
            
            // Marcar paso actual sin emoticon, en blanco
            switch (pasoActual) {
                case 1:
                    breadcrumb1.setText("Inicio");
                    breadcrumb1.setStyle(actual);
                    break;
                case 2:
                    breadcrumb2.setText("Seleccionar Orden");
                    breadcrumb2.setStyle(actual);
                    break;
                case 3:
                    breadcrumb3.setText("Observación");
                    breadcrumb3.setStyle(actual);
                    break;
                case 4:
                    breadcrumb4.setText("Motivos");
                    breadcrumb4.setStyle(actual);
                    break;
                case 5:
                    breadcrumb5.setText("Resumen");
                    breadcrumb5.setStyle(actual);
                    break;
            }
        });
    }

    private void llenarResumen() {
        Platform.runLater(() -> {
            // Llenar información de orden
            lblResumenOrden.setText(ordenActual);
            
            // Llenar información de observación
            lblResumenObservacion.setText(observacionActual);
            
            // Llenar información de motivos con comentarios
            resumenMotivos.getChildren().clear();
            for (String motivo : motivosSeleccionados) {
                VBox motivoBox = new VBox(5);
                motivoBox.setStyle("-fx-padding: 10; -fx-border-color: #2F6B73; -fx-border-radius: 3; -fx-background-color: #1B311D;");
                
                // Mostrar el motivo
                Label lblMotivo = new Label("• " + motivo);
                lblMotivo.setStyle("-fx-font-size: 12; -fx-font-weight: bold; -fx-text-fill: #E6E6E6;");
                
                // Mostrar el comentario si existe
                String comentario = comentariosPorMotivo.getOrDefault(motivo, "(sin comentario)");
                Label lblComentario = new Label("   Comentario: " + comentario);
                lblComentario.setStyle("-fx-font-size: 11; -fx-text-fill: #A2A2A2; -fx-wrap-text: true;");
                
                motivoBox.getChildren().addAll(lblMotivo, lblComentario);
                resumenMotivos.getChildren().add(motivoBox);
            }
            
            // Resetear checkboxes de notificación
            chkNotificarMail.setSelected(false);
            chkNotificarPantalla.setSelected(false);
            errorNotificacion.setText("");
        });
    }
    
    // Método para configurar exclusión mutua entre checkboxes
    private void configurarCheckboxExclusivos() {
        chkNotificarMail.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (!ignorarCambios && newVal) {
                ignorarCambios = true;
                chkNotificarPantalla.setSelected(false);
                ignorarCambios = false;
            }
        });
        
        chkNotificarPantalla.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (!ignorarCambios && newVal) {
                ignorarCambios = true;
                chkNotificarMail.setSelected(false);
                ignorarCambios = false;
            }
        });
    }

    // ============ MÉTODOS DE DELEGACIÓN (llamados desde FXML) ============

    @FXML
    private void iniciarProceso() {
        irAPantalla1();
    }

    @FXML
    private void confirmarOrden() {
        irAPantalla2();
    }

    @FXML
    private void enviarObservacion() {
        irAPantalla3();
    }

    @FXML
    private void confirmarMotivos() {
        irAPantalla4();
    }

    @FXML
    private void cancelarProceso() {
        if (mostrarConfirmacionCancelacion()) {
            System.out.println("DEBUG cancelarProceso - Llamando finCU del gestor");
            gestor.finCU();
            // Cerrar la aplicación
            Platform.exit();
        }
    }

    @FXML
    public void confirmarCierreOI() {
        System.out.println("DEBUG confirmarCierreOI - Procesando notificaciones");
        
        // Limpiar error anterior
        errorNotificacion.setText("");
        
        // Determinar el parámetro de notificación
        boolean mailSeleccionado = chkNotificarMail.isSelected();
        boolean pantallaSeleccionada = chkNotificarPantalla.isSelected();
        
        String parametroNotificacion;
        
        // Si ninguno está seleccionado, se asume ambos por defecto
        if (!mailSeleccionado && !pantallaSeleccionada) {
            parametroNotificacion = "A";  // Ambos (por defecto)
            System.out.println("DEBUG confirmarCierreOI - Parámetro: A (Ambos por defecto)");
        } else if (mailSeleccionado) {
            parametroNotificacion = "M";  // Solo Mail
            System.out.println("DEBUG confirmarCierreOI - Parámetro: M (Solo Mail)");
        } else {
            parametroNotificacion = "C";  // Solo Pantalla
            System.out.println("DEBUG confirmarCierreOI - Parámetro: C (Solo Pantalla)");
        }
        
        System.out.println("DEBUG confirmarCierreOI - Enviando confirmación al gestor con parámetro: " + parametroNotificacion);
        if (gestor != null) {
            // Mostrar pop-up y LUEGO procesar el cierre
            mostrarConfirmacionCierreYCerrar(parametroNotificacion);
        } else {
            mostrarError("Error: Gestor no inicializado");
        }
    }
    
    private void mostrarConfirmacionCierreYCerrar(String parametroNotificacion) {
        // Construir el mensaje dinámico según el parámetro de notificación
        StringBuilder mensaje = new StringBuilder();
        mensaje.append("✓ Cierre de orden registrado exitosamente\n\n");
        mensaje.append("Se ha actualizado el estado del sismógrafo asociado.\n\n");
        
        // Agregar mensajes según el tipo de notificación
        if (parametroNotificacion.equals("A")) {
            // Ambos: Mail y Pantalla
            mensaje.append("Notificaciones enviadas:\n");
            mensaje.append("  • Email: Empleados responsables de reparación\n");
            mensaje.append("  • Pantalla CCRS: Sistema de monitoreo central");
        } else if (parametroNotificacion.equals("M")) {
            // Solo Mail
            mensaje.append("Notificaciones enviadas:\n");
            mensaje.append("  • Email: Empleados responsables de reparación");
        } else if (parametroNotificacion.equals("C")) {
            // Solo Pantalla
            mensaje.append("Notificaciones enviadas:\n");
            mensaje.append("  • Pantalla CCRS: Sistema de monitoreo central");
        }
        
        // Mostrar alerta de información en el thread principal
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
            javafx.scene.control.Alert.AlertType.INFORMATION
        );
        alert.setTitle("Operación Completada");
        alert.setHeaderText("Cierre de Orden Confirmado");
        alert.setContentText(mensaje.toString());
        
        // IMPORTANTE: showAndWait() bloquea SINCRONAMENTE hasta que el usuario cierre el diálogo
        // No es necesario Thread.sleep() porque el flujo ya está bloqueado
        alert.showAndWait();
        
        // AHORA que el usuario cerró el pop-up, procesar el cierre del gestor
        // Esto se ejecuta INMEDIATAMENTE después de que se cierre el alert, en el mismo thread UI
        System.out.println("DEBUG - Pop-up cerrado, procesando cierre del gestor");
        
        // Procesar el cierre del gestor sin delay
        if (gestor != null) {
            System.out.println("DEBUG - Llamando tomarConfirmacionOI");
            gestor.tomarConfirmacionOI(true, parametroNotificacion);
        }
    }

    
    private void reiniciarPantalla() {
        Platform.runLater(() -> {
            // Limpiar formulario
            comboOrdenes.getSelectionModel().clearSelection();
            txtObservacion.clear();
            motivosCheckBoxContainer.getChildren().clear();
            checkBoxesMotivos.clear();
            motivosSeleccionados.clear();
            resumenMotivos.getChildren().clear();
            
            // Resetear errores
            errorObservacion.setVisible(false);
            motivosError.setVisible(false);
        });
    }

    private boolean mostrarConfirmacionCancelacion() {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
            javafx.scene.control.Alert.AlertType.CONFIRMATION
        );
        alert.setTitle("Confirmación");
        alert.setHeaderText("¿Desea cancelar el proceso?");
        alert.setContentText("Los datos ingresados se perderán.");
        
        java.util.Optional<javafx.scene.control.ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == javafx.scene.control.ButtonType.OK;
    }

    // ============ IMPLEMENTACIÓN DE InterfazCierreInspeccion ============

    @Override
    public void setGestor(GestorCierreInspeccion gestor) {
        this.gestor = gestor;
    }

    @Override
    public void solicitarSeleccionOI(List<String> listaOI) {
        Platform.runLater(() -> {
            comboOrdenes.getItems().clear();
            comboOrdenes.getItems().addAll(listaOI);
        });
    }

    @Override
    public void seleccionarOI(String seleccionada) {
        Platform.runLater(() -> {
            ordenActual = seleccionada;
            comboOrdenes.getSelectionModel().select(seleccionada);
        });
    }

    @Override
    public void pedirObservacionOrdenCierre() {
        Platform.runLater(() -> {
            txtObservacion.clear();
            errorObservacion.setVisible(false);
        });
    }

    @Override
    public void ingresarObservacionOrdenCierre(String observacion) {
        Platform.runLater(() -> {
            txtObservacion.setText(observacion);
        });
    }

    @Override
    public void solicitarSeleccionMotivo(List<String> listaMotivos) {
        System.out.println("DEBUG - solicitarSeleccionMotivo llamado con: " + listaMotivos.size() + " motivos");
        for (String m : listaMotivos) {
            System.out.println("  - " + m);
        }
        
        Platform.runLater(() -> {
            System.out.println("DEBUG - Cambiando a pantalla de motivos para selección");
            
            // CAMBIAR PANTALLA AQUÍ - el gestor llamará a este método cuando esté listo
            mostrarPantalla(pantallaMotivos);
            actualizarBreadcrumb(4);
            
            // Limpiar contenedor y listas
            motivosCheckBoxContainer.getChildren().clear();
            checkBoxesMotivos.clear();
            textAreasComentarios.clear();
            comentariosPorMotivo.clear();
            motivosError.setVisible(false);

            // Crear solo CheckBoxes simples (sin TextAreas)
            for (String motivo : listaMotivos) {
                System.out.println("DEBUG - Creando CheckBox para: " + motivo);
                
                CheckBox checkBox = new CheckBox(motivo);
                checkBox.setStyle("-fx-font-size: 12; -fx-padding: 8; -fx-text-fill: #E6E6E6;");
                checkBoxesMotivos.add(checkBox);
                
                // Contenedor simple solo para el CheckBox
                VBox container = new VBox(8);
                container.setStyle("-fx-padding: 10; -fx-border-color: #2F6B73; -fx-border-radius: 3; -fx-background-color: #1B311D;");
                container.getChildren().add(checkBox);
                motivosCheckBoxContainer.getChildren().add(container);
                
                System.out.println("DEBUG - CheckBox agregado al contenedor");
            }
            System.out.println("DEBUG - Total CheckBoxes creados: " + checkBoxesMotivos.size());
        });
    }

    @Override
    public void solicitarComentario(String motivo) {
        // NO usar Platform.runLater() aquí - el Dialog es bloqueante
        System.out.println("DEBUG solicitarComentario - Pidiendo comentario para: " + motivo);
        javafx.scene.control.TextInputDialog dialog = new javafx.scene.control.TextInputDialog();
        dialog.setTitle("Comentario");
        dialog.setHeaderText("Ingrese comentario para: " + motivo);
        dialog.setContentText("Comentario:");

        java.util.Optional<String> result = dialog.showAndWait();
        if (result.isPresent() && !result.get().trim().isEmpty()) {
            String comentario = result.get().trim();
            System.out.println("DEBUG solicitarComentario - Guardando comentario: " + comentario);
            
            // GUARDAR en el mapa para mostrarlo después en el resumen
            comentariosPorMotivo.put(motivo, comentario);
            
            // PASAR al gestor para su registro
            this.tomarComentario(comentario);
        } else {
            mostrarError("Debe ingresar un comentario válido");
            solicitarComentario(motivo);
        }
    }

    @Override
    public void tomarSeleccionMotivo(List<String> motivos) {
        System.out.println("DEBUG tomarSeleccionMotivo - Enviando " + motivos.size() + " motivos al gestor");
        gestor.tomarSeleccionMotivo(motivos);
    }

    @Override
    public void tomarComentario(String comentarioMotivo) {
        gestor.tomarComentario(comentarioMotivo);
    }

    @Override
    public void solicitarConfirmacionCierre() {
        Platform.runLater(() -> {
            System.out.println("DEBUG solicitarConfirmacionCierre - Mostrando resumen");
            // Cambiar a pantalla de resumen después de que el gestor completó la iteración
            mostrarPantalla(pantallaResumen);
            actualizarBreadcrumb(5);
            llenarResumen();
            // Mostrar botones en la barra inferior
            btnCancelarGlobal.setVisible(true);
            btnConfirmarFinal.setVisible(true);
        });
    }

    @Override
    public void mostrarError(String mensaje) {
        Platform.runLater(() -> {
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                javafx.scene.control.Alert.AlertType.ERROR
            );
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText(mensaje);
            alert.showAndWait();
        });
    }

    public void actualizarUsuarioEnPantalla(String nombreUsuario) {
        Platform.runLater(() -> {
            lblUsuarioNombre.setText(nombreUsuario);
            lblUsuarioRol.setText("Inspector");
        });
    }

    @Override
    public void habilitarPantalla() {
        Platform.runLater(() -> {
            pantallaBienvenida.setVisible(true);
        });
    }
}
