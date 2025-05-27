package com.bindr.dao;

import com.bindr.persistencia.HibernateConfig;
import com.bindr.modelos.GrupoEstudio;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class GrupoEstudioDao {

    // Crear un grupo de estudio
    public static boolean crear(GrupoEstudio grupo) {
        EntityManager manager = HibernateConfig.getEntityManager();
        try {
            manager.getTransaction().begin();
            manager.persist(grupo);
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
    public static GrupoEstudio buscarPorId(Long id) {
        EntityManager manager = HibernateConfig.getEntityManager();
        try {
            return manager.find(GrupoEstudio.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Actualizar grupo
    public static boolean actualizar(GrupoEstudio grupo) {
        EntityManager manager = HibernateConfig.getEntityManager();
        try {
            manager.getTransaction().begin();
            manager.merge(grupo);
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

    // Eliminar grupo
    public static boolean eliminar(Long id) {
        EntityManager manager = HibernateConfig.getEntityManager();
        try {
            manager.getTransaction().begin();
            GrupoEstudio grupo = manager.find(GrupoEstudio.class, id);
            if (grupo != null) {
                manager.remove(grupo);
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

    // Listar todos los grupos de estudio
    public static List<GrupoEstudio> listarTodos() {
        EntityManager manager = HibernateConfig.getEntityManager();
        try {
            TypedQuery<GrupoEstudio> query = manager.createQuery(
                "SELECT g FROM GrupoEstudio g", GrupoEstudio.class
            );
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of(); // lista vacía si hay error
        }
    }

    private static void rollbackTransaction(EntityManager manager) {
        if (manager != null && manager.getTransaction().isActive()) {
            manager.getTransaction().rollback();
        }
    }
}
