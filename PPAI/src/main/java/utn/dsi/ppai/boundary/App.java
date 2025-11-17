package utn.dsi.ppai.boundary;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class App extends Application {
    
    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/InterfazCierreInspeccionFXML.fxml"));
            javafx.scene.Parent root = loader.load();
            
            Scene scene = new Scene(root, 900, 750);
            scene.getStylesheets().add(getClass().getResource("/NatureStyles.css").toExternalForm());
            
            stage.setTitle("Cierre de Orden de Inspección Sísmica");
            stage.setScene(scene);
            stage.setMinWidth(800);
            stage.setMinHeight(600);
            stage.setMaximized(true);
            stage.show();
            
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error de Inicialización");
            alert.setHeaderText("No se pudo cargar la interfaz");
            alert.setContentText("Error: " + e.getMessage());
            alert.showAndWait();
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
