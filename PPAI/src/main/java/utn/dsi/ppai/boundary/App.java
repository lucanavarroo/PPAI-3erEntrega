package utn.dsi.ppai.boundary;

import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;
import javafx.stage.Stage;
import utn.dsi.ppai.control.GestorCierreInspeccion;

public class App extends Application implements InterfazCierreInspeccion {
    
    // Componentes JavaFX
    private Stage primaryStage;
    private VBox mainContainer;
    private Label lblUsuario;
    private VBox startPanel;
    private VBox ordenPanel;
    private VBox observacionPanel;
    private VBox motivosPanel;
    private HBox buttonPanel;
    
    private ComboBox<String> comboOrdenes;
    private TextArea txtObservacion;
    private List<CheckBox> checkBoxesMotivos = new ArrayList<>();
    private Button btnConfirmarOI;
    private Button btnEnviarObs;
    private Button btnTomarMotivo;
    private Button btnConfirmarCierre;
    private Button btnCancelarCierre;
    
    private GestorCierreInspeccion gestor;

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        
        try {
            setupGestor();
            initializeComponents();
            setupResponsiveLayout();
            
            primaryStage.setTitle("Cierre de Orden de Inspección - JavaFX");
            primaryStage.centerOnScreen();
            primaryStage.show();
            
        } catch (Exception e) {
            showAlert("Error", "Error al inicializar la aplicación: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private void setupGestor() {
        gestor = new GestorCierreInspeccion(
            java.time.LocalDateTime.now(),
            "",
            this
        );
        gestor.iniciarCierreOI();
    }

    // ✅ NUEVO - Configuración responsive
    private void setupResponsiveLayout() {
        // Obtener tamaño de pantalla
        Screen screen = Screen.getPrimary();
        double screenWidth = screen.getVisualBounds().getWidth();
        double screenHeight = screen.getVisualBounds().getHeight();
        
        // Calcular tamaño de ventana (80% de pantalla, mínimo 700x600) - ✅ AUMENTAR altura mínima
        double windowWidth = Math.max(700, screenWidth * 0.8);
        double windowHeight = Math.max(600, screenHeight * 0.8);
        
        primaryStage.setWidth(windowWidth);
        primaryStage.setHeight(windowHeight);
        primaryStage.setMinWidth(700); // ✅ AUMENTAR ancho mínimo
        primaryStage.setMinHeight(600); // ✅ AUMENTAR altura mínima
        primaryStage.setMaxWidth(screenWidth);
        primaryStage.setMaxHeight(screenHeight);
        
        // Listener para cambios de tamaño
        primaryStage.widthProperty().addListener((obs, oldVal, newVal) -> {
            adjustComponentSizes(newVal.doubleValue());
        });
    }

    // ✅ NUEVO - Ajustar componentes según tamaño de ventana
    private void adjustComponentSizes(double windowWidth) {
        // Mantener tamaños fijos para evitar que se expandan
        if (comboOrdenes != null) {
            comboOrdenes.setPrefWidth(400);
            comboOrdenes.setMaxWidth(400);
        }
        
        if (txtObservacion != null) {
            txtObservacion.setPrefWidth(560);
            txtObservacion.setMaxWidth(560);
        }
        
        // Ajustar padding solo si es necesario
        double padding = windowWidth < 800 ? 15 : 20;
        mainContainer.setPadding(new Insets(padding));
    }

    private void initializeComponents() {
        // Container principal con alineación TOP_LEFT
        mainContainer = new VBox(20);
        mainContainer.setPadding(new Insets(20));
        mainContainer.setAlignment(Pos.TOP_LEFT);
        mainContainer.setFillWidth(false);
        
        setupUserPanel();
        setupStartPanel();
        setupOrdenPanel();
        setupObservacionPanel();
        setupMotivosPanel();
        setupButtonPanel();

        // Inicialmente solo mostrar usuario y botón inicio
        ordenPanel.setVisible(false);
        observacionPanel.setVisible(false);
        motivosPanel.setVisible(false);
        buttonPanel.setVisible(false);

        // ✅ CORREGIR - Volver a usar ScrollPane pero configurado correctamente
        ScrollPane scrollPane = new ScrollPane(mainContainer);
        scrollPane.setFitToWidth(false); // No ajustar al ancho completo
        scrollPane.setFitToHeight(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); // Sin scroll horizontal
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED); // Scroll vertical cuando sea necesario
        scrollPane.setPannable(true); // Permitir hacer pan con el mouse
        
        // ✅ AGREGAR - Configurar el contenido del ScrollPane
        scrollPane.setContent(mainContainer);
        scrollPane.setPadding(new Insets(0)); // Sin padding en el ScrollPane
        
        // ✅ CAMBIAR - Usar VBox como wrapper para controlar el posicionamiento
        VBox rootContainer = new VBox();
        rootContainer.setAlignment(Pos.TOP_LEFT);
        rootContainer.setFillWidth(false);
        rootContainer.getChildren().add(scrollPane);
        
        // ✅ CONFIGURAR - Hacer que el ScrollPane crezca para llenar la ventana
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        Scene scene = new Scene(rootContainer); // ✅ CAMBIO - Usar rootContainer
        
        // Cargar CSS con try-catch
        try {
            scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        } catch (Exception e) {
            System.err.println("No se pudo cargar el archivo CSS: " + e.getMessage());
        }
        
        primaryStage.setScene(scene);
    }

    private void setupUserPanel() {
        lblUsuario = new Label("Usuario: Inspector Sísmico");
        lblUsuario.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        lblUsuario.getStyleClass().add("user-label");
        
        // Panel de usuario sin ancho fijo
        HBox userPanel = new HBox();
        userPanel.setAlignment(Pos.CENTER_RIGHT);
        userPanel.setPadding(new Insets(0, 20, 10, 0)); // ✅ CAMBIO - Padding derecho
        
        // Spacer para empujar el usuario a la derecha
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        userPanel.getChildren().addAll(spacer, lblUsuario);
        mainContainer.getChildren().add(userPanel);
    }

    private void setupStartPanel() {
        startPanel = new VBox(20);
        startPanel.setAlignment(Pos.CENTER); // Mantener centrado solo para la pantalla inicial
        startPanel.setPadding(new Insets(40));
        startPanel.setPrefWidth(400); // ✅ AGREGAR - Ancho fijo para pantalla inicial
        startPanel.setMaxWidth(400);
        
        Label titleLabel = new Label("Sistema de Gestión Sísmica");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titleLabel.getStyleClass().add("title-label");
        titleLabel.setWrapText(true);
        
        Button btnIniciarCierre = new Button("Cerrar Orden de Inspección");
        btnIniciarCierre.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        btnIniciarCierre.setPrefWidth(300);
        btnIniciarCierre.setMaxWidth(300); // ✅ CAMBIO - Ancho fijo
        btnIniciarCierre.setMinHeight(60);
        btnIniciarCierre.getStyleClass().add("primary-button");
        
        btnIniciarCierre.setOnAction(e -> {
            startPanel.setVisible(false);
            ordenPanel.setVisible(true);
            buttonPanel.setVisible(true);
        });
        
        startPanel.getChildren().addAll(titleLabel, btnIniciarCierre);
        // ✅ QUITAR - No hacer que crezca verticalmente
        // VBox.setVgrow(startPanel, Priority.ALWAYS);
        mainContainer.getChildren().add(startPanel);
    }

    private void setupOrdenPanel() {
        ordenPanel = new VBox(15);
        ordenPanel.getStyleClass().add("panel");
        ordenPanel.setPadding(new Insets(20));
        ordenPanel.setPrefWidth(600); // ✅ AGREGAR - Ancho fijo adecuado
        ordenPanel.setMaxWidth(600);
        
        Label lblOrden = new Label("Seleccionar Orden de Inspección:");
        lblOrden.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        
        // ✅ CAMBIO - ComboBox con ancho fijo
        comboOrdenes = new ComboBox<>();
        comboOrdenes.setPrefWidth(400); // ✅ CAMBIO - Ancho más pequeño
        comboOrdenes.setMaxWidth(400);
        comboOrdenes.setPromptText("-- Seleccionar orden --");
        
        btnConfirmarOI = new Button("Confirmar Orden");
        btnConfirmarOI.setDisable(true);
        btnConfirmarOI.getStyleClass().add("secondary-button");
        btnConfirmarOI.setMinWidth(120);
        
        comboOrdenes.setOnAction(e -> {
            btnConfirmarOI.setDisable(comboOrdenes.getSelectionModel().getSelectedIndex() <= 0);
        });
        
        btnConfirmarOI.setOnAction(e -> {
            String seleccionada = comboOrdenes.getSelectionModel().getSelectedItem();
            gestor.tomarSeleccionOI(seleccionada);
            observacionPanel.setVisible(true);
        });
        
        // ✅ CAMBIO - HBox con alineación izquierda
        HBox ordenBox = new HBox(10);
        ordenBox.setAlignment(Pos.CENTER_LEFT);
        ordenBox.getChildren().addAll(comboOrdenes, btnConfirmarOI);
        
        ordenPanel.getChildren().addAll(lblOrden, ordenBox);
        mainContainer.getChildren().add(ordenPanel);
    }

    private void setupObservacionPanel() {
        observacionPanel = new VBox(15);
        observacionPanel.getStyleClass().add("panel");
        observacionPanel.setPadding(new Insets(20));
        observacionPanel.setPrefWidth(600); // ✅ AGREGAR - Mismo ancho que orden panel
        observacionPanel.setMaxWidth(600);
        
        Label lblObs = new Label("Observación General:");
        lblObs.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        
        // ✅ CAMBIO - TextArea con ancho fijo
        txtObservacion = new TextArea();
        txtObservacion.setPrefRowCount(4);
        txtObservacion.setPrefWidth(560); // ✅ AGREGAR - Ancho fijo (panel width - padding)
        txtObservacion.setMaxWidth(560);
        txtObservacion.setPromptText("Ingrese observación sobre el cierre de la orden...");
        txtObservacion.setWrapText(true);
        
        btnEnviarObs = new Button("Enviar Observación");
        btnEnviarObs.getStyleClass().add("secondary-button");
        btnEnviarObs.setMinWidth(150);
        
        btnEnviarObs.setOnAction(e -> {
            String texto = txtObservacion.getText().trim();
            if (texto.isEmpty()) {
                showAlert("Error", "Debe ingresar una observación", Alert.AlertType.WARNING);
            } else {
                gestor.tomarObservacionOrdenCierre(texto);
                motivosPanel.setVisible(true);
            }
        });
        
        observacionPanel.getChildren().addAll(lblObs, txtObservacion, btnEnviarObs);
        observacionPanel.setVisible(false);
        mainContainer.getChildren().add(observacionPanel);
    }

    private void setupMotivosPanel() {
        motivosPanel = new VBox(15);
        motivosPanel.getStyleClass().add("panel");
        motivosPanel.setPadding(new Insets(20));
        motivosPanel.setPrefWidth(600); // ✅ AGREGAR - Mismo ancho consistente
        motivosPanel.setMaxWidth(600);
        
        Label lblMotivos = new Label("Seleccionar Motivos:");
        lblMotivos.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        
        motivosPanel.getChildren().add(lblMotivos);
        motivosPanel.setVisible(false);
        // ✅ QUITAR - No hacer que crezca verticalmente
        // VBox.setVgrow(motivosPanel, Priority.ALWAYS);
        mainContainer.getChildren().add(motivosPanel);
    }

    private void setupButtonPanel() {
        buttonPanel = new HBox(15);
        buttonPanel.setAlignment(Pos.CENTER_LEFT);
        buttonPanel.setPadding(new Insets(20, 0, 40, 0)); // ✅ AGREGAR - Padding inferior para espacio
        buttonPanel.setPrefWidth(600);
        buttonPanel.setMaxWidth(600);
        
        btnConfirmarCierre = new Button("Confirmar Cierre");
        btnConfirmarCierre.getStyleClass().add("success-button");
        btnConfirmarCierre.setVisible(false);
        btnConfirmarCierre.setMinWidth(130);
        
        btnCancelarCierre = new Button("Cancelar");
        btnCancelarCierre.getStyleClass().add("danger-button");
        btnCancelarCierre.setMinWidth(100);
        
        btnConfirmarCierre.setOnAction(e -> {
            showAlert("Éxito", 
                "Orden de Inspección cerrada exitosamente.\n\n" +
                "✅ Mails enviados a los empleados responsables de reparación\n" +
                "✅ Información publicada en monitores CCRS\n" +
                "✅ Sismógrafo marcado como 'Fuera de Servicio'", 
                Alert.AlertType.INFORMATION);
            gestor.tomarConfirmacionOI(true);
        });
        
        btnCancelarCierre.setOnAction(e -> {
            gestor.tomarConfirmacionOI(false);
        });
        
        buttonPanel.getChildren().addAll(btnConfirmarCierre, btnCancelarCierre);
        mainContainer.getChildren().add(buttonPanel);
    }

    // ✅ IMPLEMENTACIÓN DE InterfazCierreInspeccion
    @Override
    public void setGestor(GestorCierreInspeccion gestor) {
        this.gestor = gestor;
    }

    @Override
    public void solicitarSeleccionOI(List<String> listaOI) {
        Platform.runLater(() -> {
            comboOrdenes.getItems().clear();
            comboOrdenes.getItems().add("-- Seleccionar orden --");
            comboOrdenes.getItems().addAll(listaOI);
            System.out.println("Órdenes cargadas: " + listaOI.size());
        });
    }

    @Override
    public void seleccionarOI(String seleccionada) {
        gestor.tomarSeleccionOI(seleccionada);
    }

    @Override
    public void pedirObservacionOrdenCierre() {
        Platform.runLater(() -> {
            observacionPanel.setVisible(true);
        });
    }

    @Override
    public void ingresarObservacionOrdenCierre(String observacion) {
        gestor.tomarObservacionOrdenCierre(observacion);
    }

    @Override
    public void solicitarSeleccionMotivo(List<String> listaMotivos) {
        Platform.runLater(() -> {
            // Limpiar motivos anteriores excepto el label
            motivosPanel.getChildren().removeIf(node -> !(node instanceof Label));
            checkBoxesMotivos.clear();
            
            VBox motivosContainer = new VBox(10);
            motivosContainer.setPrefWidth(560);
            motivosContainer.setMaxWidth(560);
            
            for (String motivo : listaMotivos) {
                CheckBox checkBox = new CheckBox(motivo);
                checkBox.getStyleClass().add("motivo-checkbox");
                checkBox.setWrapText(true);
                checkBox.setPrefWidth(560);
                checkBox.setMaxWidth(560);
                checkBoxesMotivos.add(checkBox);
                motivosContainer.getChildren().add(checkBox);
            }
            
            btnTomarMotivo = new Button("Tomar Motivos Seleccionados");
            btnTomarMotivo.getStyleClass().add("secondary-button");
            btnTomarMotivo.setMinWidth(200);
            
            btnTomarMotivo.setOnAction(e -> {
                List<String> motivosSeleccionados = new ArrayList<>();
                for (CheckBox checkBox : checkBoxesMotivos) {
                    if (checkBox.isSelected()) {
                        motivosSeleccionados.add(checkBox.getText());
                    }
                }
                if (motivosSeleccionados.isEmpty()) {
                    showAlert("Error", "Debe seleccionar al menos un motivo", Alert.AlertType.WARNING);
                } else {
                    gestor.tomarSeleccionMotivo(motivosSeleccionados);
                }
            });
            
            // ✅ AGREGAR - Espacio extra después del botón
            VBox espacioExtra = new VBox();
            espacioExtra.setPrefHeight(20); // Espacio para asegurar que se vea el botón cancelar
            
            motivosPanel.getChildren().addAll(motivosContainer, btnTomarMotivo, espacioExtra);
            motivosPanel.setVisible(true);
            
            // ✅ AGREGAR - Scroll automático hacia abajo para mostrar los nuevos elementos
            Platform.runLater(() -> {
                ScrollPane scrollPane = findScrollPane(motivosPanel);
                if (scrollPane != null) {
                    scrollPane.setVvalue(1.0); // Scroll hacia abajo
                }
            });
        });
    }

    // ✅ MEJORAR - Alert responsive
    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        
        // ✅ AGREGAR - Hacer alert responsive
        alert.setResizable(true);
        alert.getDialogPane().setMinWidth(400);
        alert.getDialogPane().setMaxWidth(600);
        alert.getDialogPane().setPrefWidth(500);
        
        try {
            alert.getDialogPane().getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        } catch (Exception e) {
            System.err.println("CSS no disponible para dialogs");
        }
        
        alert.showAndWait();
    }

    @Override
    public void solicitarComentario(String motivo) {
        Platform.runLater(() -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Comentario Requerido");
            dialog.setHeaderText("Ingrese comentario para: " + motivo);
            dialog.setContentText("Comentario:");
            
            // ✅ AGREGAR - Dialog responsive
            dialog.setResizable(true);
            dialog.getDialogPane().setMinWidth(400);
            dialog.getDialogPane().setMaxWidth(600);
            dialog.getDialogPane().setPrefWidth(500);
            
            try {
                dialog.getDialogPane().getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
            } catch (Exception e) {
                // Ignorar si no hay CSS
            }
            
            boolean comentarioValido = false;
            while (!comentarioValido) {
                var resultado = dialog.showAndWait();
                if (resultado.isPresent() && !resultado.get().trim().isEmpty()) {
                    gestor.tomarComentario(resultado.get().trim());
                    comentarioValido = true;
                } else {
                    showAlert("Error", "Debe ingresar un comentario válido", Alert.AlertType.WARNING);
                }
            }
        });
    }

    @Override
    public void tomarSeleccionMotivo(List<String> motivos) {
        gestor.tomarSeleccionMotivo(motivos);
    }

    @Override
    public void tomarComentario(String comentarioMotivo) {
        gestor.tomarComentario(comentarioMotivo);
    }

    @Override
    public void solicitarConfirmacionCierre() {
        Platform.runLater(() -> {
            btnConfirmarCierre.setVisible(true);
        });
    }

    @Override
    public void confirmarCierreOI() {
        gestor.tomarConfirmacionOI(true);
    }

    @Override
    public void mostrarError(String mensaje) {
        Platform.runLater(() -> {
            showAlert("Error", mensaje, Alert.AlertType.ERROR);
        });
    }

    @Override
    public void habilitarPantalla() {
        // Ya implementado en start()
    }
    
// ✅ AGREGAR - Método auxiliar para encontrar el ScrollPane
private ScrollPane findScrollPane(javafx.scene.Node node) {
    javafx.scene.Node parent = node.getParent();
    while (parent != null) {
        if (parent instanceof ScrollPane) {
            return (ScrollPane) parent;
        }
        parent = parent.getParent();
    }
    return null;}

    public static void main(String[] args) {
        launch(args);
    }
}