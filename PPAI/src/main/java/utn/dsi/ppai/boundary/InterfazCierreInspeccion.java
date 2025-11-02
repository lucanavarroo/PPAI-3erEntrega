package utn.dsi.ppai.boundary;

import utn.dsi.ppai.control.GestorCierreInspeccion;  // Añade este import
import java.util.List;

public interface InterfazCierreInspeccion {
    void setGestor(GestorCierreInspeccion gestor);
    void solicitarSeleccionOI(List<String> listaOI);
    void seleccionarOI(String seleccionada);
    void pedirObservacionOrdenCierre();
    void ingresarObservacionOrdenCierre(String observacion);
    void solicitarSeleccionMotivo(List<String> listaMotivos);
    void solicitarComentario(String motivo);
    void tomarSeleccionMotivo(List<String> motivos);
    void tomarComentario(String comentarioMotivo);
    void solicitarConfirmacionCierre();
    void confirmarCierreOI();
    void mostrarError(String mensaje);
    void habilitarPantalla();
}
