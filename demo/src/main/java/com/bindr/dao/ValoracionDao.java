package com.bindr.dao;

import com.bindr.modelos.Valoracion;
import com.bindr.persistencia.HibernateConfig;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class ValoracionDao {

    // Guardar una nueva valoración
    public static boolean crear(Valoracion valoracion) {
        EntityManager em = HibernateConfig.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(valoracion);
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            e.printStackTrace();
            return false;
        } finally {
            HibernateConfig.closeEntityManager();
        }
    }

    // Buscar valoración por ID
    public static Valoracion buscarPorId(Long id) {
        EntityManager em = HibernateConfig.getEntityManager();
        try {
            return em.find(Valoracion.class, id);
        } finally {
            HibernateConfig.closeEntityManager();
        }
    }

    // Listar todas las valoraciones
    public static List<Valoracion> listarTodas() {
        EntityManager em = HibernateConfig.getEntityManager();
        try {
            TypedQuery<Valoracion> query = em.createQuery("SELECT v FROM Valoracion v", Valoracion.class);
            return query.getResultList();
        } finally {
            HibernateConfig.closeEntityManager();
        }
    }

    // Listar valoraciones por autor
    public static List<Valoracion> buscarPorAutor(String autor) {
        EntityManager em = HibernateConfig.getEntityManager();
        try {
            TypedQuery<Valoracion> query = em.createQuery(
                "SELECT v FROM Valoracion v WHERE v.autor = :autor", Valoracion.class);
            query.setParameter("autor", autor);
            return query.getResultList();
        } finally {
            HibernateConfig.closeEntityManager();
        }
    }

    // Actualizar valoración
    public static boolean actualizar(Valoracion valoracion) {
        EntityManager em = HibernateConfig.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(valoracion);
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            e.printStackTrace();
            return false;
        } finally {
            HibernateConfig.closeEntityManager();
        }
    }

    // Eliminar valoración
    public static boolean eliminar(Long id) {
        EntityManager em = HibernateConfig.getEntityManager();
        try {
            em.getTransaction().begin();
            Valoracion valoracion = em.find(Valoracion.class, id);
            if (valoracion != null) {
                em.remove(valoracion);
            }
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            e.printStackTrace();
            return false;
        } finally {
            HibernateConfig.closeEntityManager();
        }
    }
}