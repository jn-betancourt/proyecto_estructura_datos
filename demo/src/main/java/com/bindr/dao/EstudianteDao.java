package com.bindr.dao;

import java.util.List;

import com.bindr.modelos.Estudiante;
import com.bindr.persistencia.HibernateConfig;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;


public class EstudianteDao {
    // Crear
    public static boolean crearEstudiante(Estudiante est) {
        EntityManager manager = HibernateConfig.getEntityManager();
        try {
            manager.getTransaction().begin();
            manager.persist(est);
            manager.getTransaction().commit();
            return true;
        } catch (Exception e) {
            rollbackTransaction();
            e.printStackTrace();
            return false;
        }finally{
            HibernateConfig.closeEntityManager();
        }
    }

    // Buscar por ID
    public static Estudiante buscarPorId(Long id) {
        EntityManager manager = HibernateConfig.getEntityManager();
        try {
            return manager.find(Estudiante.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }finally{
            HibernateConfig.closeEntityManager();
        }
    }

    // Buscar por email (unique)
    public static Estudiante buscarPorEmail(String email) {
        EntityManager manager = HibernateConfig.getEntityManager();
        try {
            TypedQuery<Estudiante> query = manager.createQuery(
                "SELECT e FROM Estudiante e WHERE e.correo = :email", Estudiante.class);
            query.setParameter("email", email);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null; // No existe el estudiante
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }finally{
            HibernateConfig.closeEntityManager();
        }
    }

    // Actualizar
    public static boolean actualizarEstudiante(Estudiante est) {
        EntityManager manager = HibernateConfig.getEntityManager();
        try {
            manager.getTransaction().begin();
            manager.merge(est);
            manager.getTransaction().commit();
            return true;
        } catch (Exception e) {
            rollbackTransaction();
            e.printStackTrace();
            return false;
        }finally{
            HibernateConfig.closeEntityManager();
        }
    }

    // Eliminar
    public static boolean eliminarEstudiante(Long id) {
        EntityManager manager = HibernateConfig.getEntityManager();
        try {
            manager.getTransaction().begin();
            Estudiante est = manager.find(Estudiante.class, id);
            if (est != null) {
                manager.remove(est);
            }
            manager.getTransaction().commit();
            return true;
        } catch (Exception e) {
            rollbackTransaction();
            e.printStackTrace();
            return false;
        }finally{
            HibernateConfig.closeEntityManager();
        }
    }

    // Listar todos
    public static List<Estudiante> listarTodos() {
        EntityManager manager = HibernateConfig.getEntityManager();
        try {
            TypedQuery<Estudiante> query = manager.createQuery(
                "SELECT e FROM Estudiante e", Estudiante.class);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of(); // Lista vacía si hay error
        }finally{
            HibernateConfig.closeEntityManager();
        }
    }

    // ---- Métodos auxiliares ----
    private static void rollbackTransaction() {
        EntityManager manager = HibernateConfig.getEntityManager();
        if (manager.getTransaction().isActive()) {
            manager.getTransaction().rollback();
        }
    }
}
