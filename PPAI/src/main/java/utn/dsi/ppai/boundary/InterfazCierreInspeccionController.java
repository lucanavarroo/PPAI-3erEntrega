package utn.dsi.ppai.boundary;

import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import utn.dsi.ppai.control.GestorCierreInspeccion;

public class InterfazCierreInspeccionController implements InterfazCierreInspeccion {

    @FXML
    private Label lblUsuario;
    
    @FXML
    private VBox startPanel;
    
    @FXML
    private ScrollPane formPanel;
    
    @FXML
    private VBox panelOrden;
    
    @FXML
    private ComboBox<String> comboOrdenes;
    
    @FXML
    private Button btnConfirmarOI;
    
    @FXML
    private VBox panelObs;
    
    @FXML
    private TextArea txtObservacion;
    
    @FXML
    private Button btnEnviarObs;
    
    @FXML
    private VBox panelMotivos;
    
    @FXML
    private VBox motivosCheckBoxContainer;
    
    @FXML
    private Label motivosError;
    
    @FXML
    private Button btnTomarMotivo;
    
    @FXML
    private VBox panelConfirmacion;
    
    @FXML
    private VBox resumenPanel;
    
    @FXML
    private Button btnConfirmarCierre;
    
    @FXML
    private Button btnCancelarCierre;
    
    @FXML
    private Button btnIniciarCierre;
    
    private GestorCierreInspeccion gestor;
    private List<CheckBox> checkBoxesMotivos;
    private List<String> motivosSeleccionados;
    private String ordenActual;
    private String observacionActual;

    public InterfazCierreInspeccionController() {
        checkBoxesMotivos = new ArrayList<>();
        motivosSeleccionados = new ArrayList<>();
        setupGestor();
    }

    @FXML
    private void initialize() {
        // Configurar validación de orden
        btnConfirmarOI.setDisable(true);
        comboOrdenes.setOnAction(e -> {
            btnConfirmarOI.setDisable(comboOrdenes.getSelectionModel().isEmpty());
        });

        // Configurar validación de observación
        txtObservacion.textProperty().addListener((obs, old, newValue) -> {
            btnEnviarObs.setDisable(newValue.trim().isEmpty());
        });
        btnEnviarObs.setDisable(true);

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

    @FXML
    private void iniciarProceso() {
        startPanel.setVisible(false);
        formPanel.setVisible(true);
    }

    @FXML
    private void confirmarOrden() {
        String ordenSeleccionada = comboOrdenes.getSelectionModel().getSelectedItem();
        if (ordenSeleccionada != null && !ordenSeleccionada.isEmpty()) {
            ordenActual = ordenSeleccionada;
            gestor.tomarSeleccionOI(ordenSeleccionada);
            panelOrden.setStyle("-fx-border-color: #a8d5a8; -fx-background-color: #f0fdf4;");
            panelObs.setVisible(true);
            formPanel.setVvalue(0.2);
        }
    }

    @FXML
    private void enviarObservacion() {
        String observacion = txtObservacion.getText().trim();
        if (!observacion.isEmpty()) {
            observacionActual = observacion;
            gestor.tomarObservacionOrdenCierre(observacion);
            panelObs.setStyle("-fx-border-color: #a8d5a8; -fx-background-color: #f0fdf4;");
            panelMotivos.setVisible(true);
            formPanel.setVvalue(0.4);
        } else {
            mostrarError("Debe ingresar una observación");
        }
    }

    @FXML
    private void procesarMotivos() {
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
        gestor.tomarSeleccionMotivo(motivosSeleccionados);
        panelMotivos.setStyle("-fx-border-color: #a8d5a8; -fx-background-color: #f0fdf4;");
        mostrarConfirmacion();
    }

    private void mostrarConfirmacion() {
        resumenPanel.getChildren().clear();
        
        // Resumen de orden
        HBox rowOrden = crearFilaResumen("Orden Seleccionada:", ordenActual);
        resumenPanel.getChildren().add(rowOrden);
        
        // Resumen de observación
        HBox rowObs = crearFilaResumen("Observación:", observacionActual);
        resumenPanel.getChildren().add(rowObs);
        
        // Resumen de motivos
        HBox rowMotivos = crearFilaResumen("Motivos Seleccionados:", 
            String.join(", ", motivosSeleccionados));
        resumenPanel.getChildren().add(rowMotivos);
        
        panelConfirmacion.setVisible(true);
        formPanel.setVvalue(0.6);
    }

    private HBox crearFilaResumen(String etiqueta, String valor) {
        HBox row = new HBox(20);
        row.setStyle("-fx-padding: 10; -fx-border-color: #e5e7eb; -fx-border-width: 0 0 1 0; -fx-background-color: #f9fafb;");
        
        Label label = new Label(etiqueta);
        label.setStyle("-fx-font-weight: bold; -fx-text-fill: #2d5016; -fx-min-width: 150;");
        
        Label value = new Label(valor);
        value.setStyle("-fx-text-fill: #6b7280; -fx-wrap-text: true;");
        value.setWrapText(true);
        
        row.getChildren().addAll(label, value);
        return row;
    }

    @FXML
    private void confirmarCierre() {
        gestor.tomarConfirmacionOI(true);
        mostrarExito("Orden de Inspección cerrada exitosamente.\nMails enviados a los empleados y publicados en monitores CCRS.");
        reiniciarPantalla();
    }

    @FXML
    private void cancelarProceso() {
        if (mostrarConfirmacionCancelacion()) {
            gestor.tomarConfirmacionOI(false);
            gestor.finCU();
            reiniciarPantalla();
        }
    }

    private void reiniciarPantalla() {
        Platform.runLater(() -> {
            startPanel.setVisible(true);
            formPanel.setVisible(false);
            
            // Limpiar formulario
            comboOrdenes.getSelectionModel().clearSelection();
            txtObservacion.clear();
            motivosCheckBoxContainer.getChildren().clear();
            checkBoxesMotivos.clear();
            motivosSeleccionados.clear();
            resumenPanel.getChildren().clear();
            
            // Resetear estilos
            panelOrden.setStyle("");
            panelObs.setStyle("");
            panelMotivos.setStyle("");
            
            // Ocultar paneles
            panelObs.setVisible(false);
            panelMotivos.setVisible(false);
            panelConfirmacion.setVisible(false);
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

    // Implementación de InterfazCierreInspeccion
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
            comboOrdenes.getSelectionModel().select(seleccionada);
        });
    }

    @Override
    public void pedirObservacionOrdenCierre() {
        Platform.runLater(() -> {
            panelObs.setVisible(true);
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
        Platform.runLater(() -> {
            motivosCheckBoxContainer.getChildren().clear();
            checkBoxesMotivos.clear();

            for (String motivo : listaMotivos) {
                CheckBox checkBox = new CheckBox(motivo);
                checkBox.setStyle("-fx-font-size: 12; -fx-padding: 8;");
                checkBoxesMotivos.add(checkBox);
                
                VBox container = new VBox();
                container.setStyle("-fx-padding: 5;");
                container.getChildren().add(checkBox);
                motivosCheckBoxContainer.getChildren().add(container);
            }
        });
    }

    @Override
    public void solicitarComentario(String motivo) {
        Platform.runLater(() -> {
            javafx.scene.control.TextInputDialog dialog = new javafx.scene.control.TextInputDialog();
            dialog.setTitle("Comentario");
            dialog.setHeaderText("Ingrese comentario para: " + motivo);
            dialog.setContentText("Comentario:");

            java.util.Optional<String> result = dialog.showAndWait();
            if (result.isPresent() && !result.get().trim().isEmpty()) {
                gestor.tomarComentario(result.get().trim());
            } else {
                mostrarError("Debe ingresar un comentario válido");
                solicitarComentario(motivo);
            }
        });
    }

    @Override
    public void tomarSeleccionMotivo(List<String> motivos) {
        // Ya manejado en procesarMotivos
    }

    @Override
    public void tomarComentario(String comentarioMotivo) {
        // Ya manejado en solicitarComentario
    }

    @Override
    public void solicitarConfirmacionCierre() {
        // Ya visible a través del panelConfirmacion
    }

    @Override
    public void confirmarCierreOI() {
        confirmarCierre();
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

    private void mostrarExito(String mensaje) {
        Platform.runLater(() -> {
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                javafx.scene.control.Alert.AlertType.INFORMATION
            );
            alert.setTitle("Éxito");
            alert.setHeaderText(null);
            alert.setContentText(mensaje);
            alert.showAndWait();
        });
    }

    public void actualizarUsuarioEnPantalla(String nombreUsuario) {
        Platform.runLater(() -> {
            lblUsuario.setText("Usuario: " + nombreUsuario);
        });
    }

    @Override
    public void habilitarPantalla() {
        Platform.runLater(() -> {
            startPanel.setVisible(true);
            formPanel.setVisible(false);
        });
    }
}
