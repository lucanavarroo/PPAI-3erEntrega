package utn.dsi.ppai.boundary;

import java.util.List;  // Añade este import

import utn.dsi.ppai.control.GestorCierreInspeccion;

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
    void mostrarError(String mensaje);
    void habilitarPantalla();
}
