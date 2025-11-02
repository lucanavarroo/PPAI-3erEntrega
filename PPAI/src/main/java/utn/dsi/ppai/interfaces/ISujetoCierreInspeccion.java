package utn.dsi.ppai.interfaces;

import java.util.List;

public interface ISujetoCierreInspeccion {
    void notificar();
    void suscribir(List<IObservadorCierreInspeccion> observador);
    void desuscribir(IObservadorCierreInspeccion observador);
}
