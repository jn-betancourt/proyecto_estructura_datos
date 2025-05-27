package com.bindr.dao;

import com.bindr.modelos.SolicitudAyuda;
import com.bindr.persistencia.HibernateConfig;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class SolicitudAyudaDao {

    // Crear una nueva solicitud de ayuda
    public static boolean crear(SolicitudAyuda solicitud) {
        EntityManager manager = HibernateConfig.getEntityManager();
        try {
            manager.getTransaction().begin();
            manager.persist(solicitud);
            manager.getTransaction().commit();
            return true;
        } catch (Exception e) {
            if (manager.getTransaction().isActive()) manager.getTransaction().rollback();
            e.printStackTrace();
            return false;
        } finally {
            HibernateConfig.closeEntityManager();
        }
    }

    // Buscar una solicitud por ID
    public static SolicitudAyuda buscarPorId(Integer id) {
        EntityManager manager = HibernateConfig.getEntityManager();
        try {
            return manager.find(SolicitudAyuda.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            HibernateConfig.closeEntityManager();
        }
    }

    // Actualizar una solicitud existente
    public static boolean actualizar(SolicitudAyuda solicitud) {
        EntityManager manager = HibernateConfig.getEntityManager();
        try {
            manager.getTransaction().begin();
            manager.merge(solicitud);
            manager.getTransaction().commit();
            return true;
        } catch (Exception e) {
            if (manager.getTransaction().isActive()) manager.getTransaction().rollback();
            e.printStackTrace();
            return false;
        } finally {
            HibernateConfig.closeEntityManager();
        }
    }

    // Eliminar una solicitud por ID
    public static boolean eliminar(Integer id) {
        EntityManager manager = HibernateConfig.getEntityManager();
        try {
            manager.getTransaction().begin();
            SolicitudAyuda solicitud = manager.find(SolicitudAyuda.class, id);
            if (solicitud != null) {
                manager.remove(solicitud);
            }
            manager.getTransaction().commit();
            return true;
        } catch (Exception e) {
            if (manager.getTransaction().isActive()) manager.getTransaction().rollback();
            e.printStackTrace();
            return false;
        } finally {
            HibernateConfig.closeEntityManager();
        }
    }

    // Obtener todas las solicitudes de ayuda
    public static List<SolicitudAyuda> obtenerTodas() {
        EntityManager manager = HibernateConfig.getEntityManager();
        try {
            TypedQuery<SolicitudAyuda> query = manager.createQuery(
                "SELECT s FROM SolicitudAyuda s", SolicitudAyuda.class);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        } finally {
            HibernateConfig.closeEntityManager();
        }
    }

    // Obtener solicitudes por correo del remitente
    public static List<SolicitudAyuda> obtenerPorCorreoRemitente(String correo) {
        EntityManager manager = HibernateConfig.getEntityManager();
        try {
            TypedQuery<SolicitudAyuda> query = manager.createQuery(
                "SELECT s FROM SolicitudAyuda s WHERE s.remitente.correo = :correo", SolicitudAyuda.class);
            query.setParameter("correo", correo);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        } finally {
            HibernateConfig.closeEntityManager();
        }
    }

    // Obtener solicitudes por correo del destinatario
    public static List<SolicitudAyuda> obtenerPorCorreoDestinatario(String correo) {
        EntityManager manager = HibernateConfig.getEntityManager();
        try {
            TypedQuery<SolicitudAyuda> query = manager.createQuery(
                "SELECT s FROM SolicitudAyuda s WHERE s.destinatario.correo = :correo", SolicitudAyuda.class);
            query.setParameter("correo", correo);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        } finally {
            HibernateConfig.closeEntityManager();
        }
    }
}