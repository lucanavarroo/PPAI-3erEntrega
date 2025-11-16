package utn.dsi.ppai.repositories;

import java.util.List;
import java.util.Optional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import utn.dsi.ppai.persistence.DataBaseManager;

public abstract class RepositorioBase<T, ID> {
    private final Class<T> entityClass;
    
    protected RepositorioBase(Class<T> entityClass) {
        this.entityClass = entityClass;
    }
    
    // ✅ CORREGIR - Usar merge() para manejar entidades con ID
    public T save(T entity) {
        EntityManager em = DataBaseManager.getEntityManager();
        try {
            em.getTransaction().begin();
            
            // ✅ USAR MERGE en lugar de PERSIST
            // merge() maneja tanto entidades nuevas como existentes
            T savedEntity = em.merge(entity);
            
            em.getTransaction().commit();
            return savedEntity;
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new RuntimeException("Error al guardar entidad: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }
    
    public T update(T entity) {
        EntityManager em = DataBaseManager.getEntityManager();
        try {
            em.getTransaction().begin();
            T updatedEntity = em.merge(entity);
            em.getTransaction().commit();
            return updatedEntity;
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new RuntimeException("Error al actualizar entidad: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }
    
    // ✅ AGREGAR - Método específico para persistir entidades completamente nuevas
    public T persist(T entity) {
        EntityManager em = DataBaseManager.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(entity);
            em.getTransaction().commit();
            return entity;
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new RuntimeException("Error al persistir entidad: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }
    
    public Optional<T> findById(ID id) {
        EntityManager em = DataBaseManager.getEntityManager();
        try {
            T entity = em.find(entityClass, id);
            return Optional.ofNullable(entity);
        } finally {
            em.close();
        }
    }
    
    public List<T> findAll() {
        EntityManager em = DataBaseManager.getEntityManager();
        try {
            TypedQuery<T> query = em.createQuery(
                "SELECT e FROM " + entityClass.getSimpleName() + " e", 
                entityClass
            );
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    public void delete(T entity) {
        EntityManager em = DataBaseManager.getEntityManager();
        try {
            em.getTransaction().begin();
            T managedEntity = em.merge(entity);
            em.remove(managedEntity);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new RuntimeException("Error al eliminar entidad: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }
    
    public void deleteById(ID id) {
        EntityManager em = DataBaseManager.getEntityManager();
        try {
            em.getTransaction().begin();
            T entity = em.find(entityClass, id);
            if (entity != null) {
                em.remove(entity);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new RuntimeException("Error al eliminar entidad por ID: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }
    
    public boolean exists(ID id) {
        return findById(id).isPresent();
    }
    
    public long count() {
        EntityManager em = DataBaseManager.getEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                "SELECT COUNT(e) FROM " + entityClass.getSimpleName() + " e", 
                Long.class
            );
            return query.getSingleResult();
        } finally {
            em.close();
        }
    }
    
    protected EntityManager getEntityManager() {
        return DataBaseManager.getEntityManager();
    }
    
    protected String getEntityName() {
        return entityClass.getSimpleName();
    }
}
