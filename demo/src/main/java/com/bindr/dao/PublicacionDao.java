package com.bindr.dao;

import com.bindr.modelos.Publicacion;
import com.bindr.persistencia.HibernateConfig;
import jakarta.persistence.*;

import java.util.List;

public class PublicacionDao {

    public static List<Publicacion> buscarPorPublicadorId(Long publicadorId) {
        EntityManager manager = HibernateConfig.getEntityManager();
        try {
            TypedQuery<Publicacion> query = manager.createQuery(
                    "SELECT p FROM Publicacion p WHERE p.publicador.id = :publicadorId", Publicacion.class);
            query.setParameter("publicadorId", publicadorId);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }finally {
            HibernateConfig.closeEntityManager();
        }
    }

    // Crear una publicación
    public static boolean crear(Publicacion publicacion) {
        EntityManager manager = HibernateConfig.getEntityManager();
        try {
            manager.getTransaction().begin();
            manager.persist(publicacion);
            manager.getTransaction().commit();
            return true;
        } catch (Exception e) {
            rollbackTransaction(manager);
            e.printStackTrace();
            return false;
        } finally {
            HibernateConfig.closeEntityManager();
        }
    }

    // Buscar por ID
    public static Publicacion buscarPorId(Long id) {
        EntityManager manager = HibernateConfig.getEntityManager();
        try {
            return manager.find(Publicacion.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Actualizar
    public static boolean actualizar(Publicacion publicacion) {
        EntityManager manager = HibernateConfig.getEntityManager();
        try {
            manager.getTransaction().begin();
            manager.merge(publicacion);
            manager.getTransaction().commit();
            return true;
        } catch (Exception e) {
            rollbackTransaction(manager);
            e.printStackTrace();
            return false;
        } finally {
            HibernateConfig.closeEntityManager();
        }
    }

    // Eliminar
    public static boolean eliminar(Long id) {
        EntityManager manager = HibernateConfig.getEntityManager();
        try {
            manager.getTransaction().begin();
            Publicacion publicacion = manager.find(Publicacion.class, id);
            if (publicacion != null) {
                manager.remove(publicacion);
            }
            manager.getTransaction().commit();
            return true;
        } catch (Exception e) {
            rollbackTransaction(manager);
            e.printStackTrace();
            return false;
        } finally {
            HibernateConfig.closeEntityManager();
        }
    }

    // Listar todas las publicaciones
    public static List<Publicacion> listarTodas() {
        EntityManager manager = HibernateConfig.getEntityManager();
        try {
            TypedQuery<Publicacion> query = manager.createQuery(
                "SELECT p FROM Publicacion p", Publicacion.class);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of(); // Lista vacía si hay error
        }
    }

    // ---- Método auxiliar ----
    private static void rollbackTransaction(EntityManager manager) {
        if (manager != null && manager.getTransaction().isActive()) {
            manager.getTransaction().rollback();
        }
    }
}

