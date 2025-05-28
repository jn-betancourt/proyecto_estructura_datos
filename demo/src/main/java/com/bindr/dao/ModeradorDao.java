package com.bindr.dao;

import com.bindr.modelos.Moderador;
import com.bindr.persistencia.HibernateConfig;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

import java.util.List;

public class ModeradorDao {

    public Moderador buscarPorCorreo(String correo) {
        EntityManager em = HibernateConfig.getEntityManager();
        try {
            return em.createQuery("SELECT m FROM Moderador m WHERE m.correo = :correo", Moderador.class)
                     .setParameter("correo", correo)
                     .getSingleResult();
        } catch (NoResultException e) {
            e.printStackTrace();
            return null; // No se encontró el moderador
        } finally {
            em.close();
        }
    }

    public void crear(Moderador moderador) {
        EntityManager em = HibernateConfig.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(moderador);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public Moderador buscarPorId(Integer id) {
        EntityManager em = HibernateConfig.getEntityManager();
        try {
            return em.find(Moderador.class, id);
        } finally {
            em.close();
        }
    }

    public List<Moderador> listarTodos() {
        EntityManager em = HibernateConfig.getEntityManager();
        try {
            return em.createQuery("SELECT m FROM Moderador m", Moderador.class).getResultList();
        } finally {
            em.close();
        }
    }

    public void actualizar(Moderador moderador) {
        EntityManager em = HibernateConfig.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(moderador);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public void eliminar(Integer id) {
        EntityManager em = HibernateConfig.getEntityManager();
        try {
            em.getTransaction().begin();
            Moderador moderador = em.find(Moderador.class, id);
            if (moderador != null) {
                em.remove(moderador);
            }
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
}