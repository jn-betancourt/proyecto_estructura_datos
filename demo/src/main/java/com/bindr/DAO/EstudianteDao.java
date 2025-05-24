package com.bindr.dao;

import java.util.List;

import com.bindr.modelos.Estudiante;
import com.bindr.persistencia.HibernateConfig;

import jakarta.persistence.*;


public class EstudianteDao {
    
    private static EntityManager manager = HibernateConfig.getEntityManager();

    // Crear
    public static boolean crearEstudiante(Estudiante est) {
        try {
            manager.getTransaction().begin();
            manager.persist(est);
            manager.getTransaction().commit();
            return true;
        } catch (Exception e) {
            rollbackTransaction();
            e.printStackTrace();
            return false;
        }
    }

    // Buscar por ID
    public static Estudiante buscarPorId(Long id) {
        try {
            return manager.find(Estudiante.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Buscar por email (unique)
    public static Estudiante buscarPorEmail(String email) {
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
        }
    }

    // Actualizar
    public static boolean actualizarEstudiante(Estudiante est) {
        try {
            manager.getTransaction().begin();
            manager.merge(est);
            manager.getTransaction().commit();
            return true;
        } catch (Exception e) {
            rollbackTransaction();
            e.printStackTrace();
            return false;
        }
    }

    // Eliminar
    public static boolean eliminarEstudiante(Long id) {
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
        }
    }

    // Listar todos
    public static List<Estudiante> listarTodos() {
        try {
            TypedQuery<Estudiante> query = manager.createQuery(
                "SELECT e FROM Estudiante e", Estudiante.class);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of(); // Lista vacía si hay error
        }
    }

    // ---- Métodos auxiliares ----
    private static void rollbackTransaction() {
        if (manager.getTransaction().isActive()) {
            manager.getTransaction().rollback();
        }
    }
}
