package utn.dsi.ppai.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import utn.dsi.ppai.entity.Sesion;

public class RepositorioSesion extends RepositorioBase<Sesion, Integer> {
    
    public RepositorioSesion() {
        super(Sesion.class);
    }


    public Sesion obtenerSesionActual() {
        EntityManager em = getEntityManager(); 
        try {
            return em.createQuery(
                "SELECT s FROM Sesion s WHERE s.fechaHoraFin IS NULL", 
                Sesion.class)
                .getSingleResult(); // ✅ Devuelve Sesion directa
                
        } catch (NoResultException e) {
            // ✅ No hay sesión activa - devuelve null
            return null;
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener sesión actual: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }
}