package utn.dsi.ppai.persistence;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class DataBaseManager {
    
    private static final String PERSISTENCE_UNIT_NAME = "ppai-pu";
    private static EntityManagerFactory emf;
    
    static {
        try {
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
            System.out.println("✅ Base de datos inicializada correctamente");
        } catch (Exception e) {
            System.err.println("❌ Error inicializando base de datos: " + e.getMessage());
            throw new RuntimeException("No se pudo inicializar la base de datos", e);
        }
    }
    
    // ✅ CAMBIAR - Método estático en lugar de getInstance()
    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
    
    public static void closeEntityManagerFactory() {
        if (emf != null && emf.isOpen()) {
            emf.close();
            System.out.println("✅ Conexión a base de datos cerrada");
        }
    }
    
    /**
     * Inicializa la base de datos con datos mock
     */
    public static void inicializarBaseDeDatos() {
        System.out.println("🔄 Inicializando base de datos...");
        
        try {
            // Solo crear el EntityManagerFactory ya inicializa las tablas (create-drop)
            EntityManager em = getEntityManager();
            em.close();
            
            System.out.println("✅ Tablas creadas exitosamente desde anotaciones");
            
        } catch (Exception e) {
            System.err.println("❌ Error creando tablas: " + e.getMessage());
            throw new RuntimeException("Error inicializando base de datos", e);
        }
    }
}
