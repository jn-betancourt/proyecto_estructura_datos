package com.bindr.dao;

import com.bindr.modelos.Publicacion;
import com.bindr.modelos.Valoracion;
import com.bindr.persistencia.HibernateConfig;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.ArrayList;
import java.util.List;

public class PublicacionDao {

    // ObjectMapper configurado para fechas Java 8
    private static final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    //listar todas las publicaciones
    public static List<Publicacion> listarTodas() {
        EntityManager manager = HibernateConfig.getEntityManager();
        try {
            TypedQuery<Publicacion> query = manager.createQuery("SELECT p FROM Publicacion p", Publicacion.class);
            List<Publicacion> publicaciones = query.getResultList();
            publicaciones.forEach(PublicacionDao::deserializarValoraciones);
            return publicaciones;
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        } finally {
            HibernateConfig.closeEntityManager();
        }
    }

    public static List<Publicacion> buscarPorPublicadorId(Long publicadorId) {
        EntityManager manager = HibernateConfig.getEntityManager();
        try {
            TypedQuery<Publicacion> query = manager.createQuery(
                    "SELECT p FROM Publicacion p WHERE p.publicador.id = :publicadorId", Publicacion.class);
            query.setParameter("publicadorId", publicadorId);
            List<Publicacion> publicaciones = query.getResultList();
            publicaciones.forEach(PublicacionDao::deserializarValoraciones);
            return publicaciones;
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        } finally {
            HibernateConfig.closeEntityManager();
        }
    }

    public static List<Publicacion> buscarPorGrupoId(Long grupoId) {
        EntityManager manager = HibernateConfig.getEntityManager();
        try {
            TypedQuery<Publicacion> query = manager.createQuery(
                "SELECT p FROM Publicacion p WHERE p.grupoEstudio.id = :grupoId", Publicacion.class);
            query.setParameter("grupoId", grupoId);
            List<Publicacion> publicaciones = query.getResultList();
            publicaciones.forEach(PublicacionDao::deserializarValoraciones);
            return publicaciones;
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        } finally {
            HibernateConfig.closeEntityManager();
        }
    }

    // Crear una publicación
    public static boolean crear(Publicacion publicacion) {
        EntityManager manager = HibernateConfig.getEntityManager();
        try {
            manager.getTransaction().begin();
            serializarValoraciones(publicacion);
            manager.persist(publicacion);
            manager.getTransaction().commit();
            return true;
        } catch (Exception e) {
            if (manager.getTransaction().isActive()) {
                manager.getTransaction().rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            HibernateConfig.closeEntityManager();
        }
    }

    public static Publicacion buscarPorId(Long id) {
        EntityManager manager = HibernateConfig.getEntityManager();
        try {
            Publicacion pub = manager.find(Publicacion.class, id);
            if (pub != null) {
                deserializarValoraciones(pub);
            }
            return pub;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean actualizar(Publicacion publicacion) {
        EntityManager manager = HibernateConfig.getEntityManager();
        try {
            manager.getTransaction().begin();
            serializarValoraciones(publicacion); // Serializa antes de guardar
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

    // Serializar valoraciones a JSON antes de guardar
    private static void serializarValoraciones(Publicacion publicacion) {
        if (publicacion.getValoraciones() != null) {
            try {
                String json = mapper.writeValueAsString(publicacion.getValoraciones());
                publicacion.setValoracionesJson(json);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Deserializar valoraciones de JSON al leer
    private static void deserializarValoraciones(Publicacion publicacion) {
        String json = publicacion.getValoracionesJson();
        if (json != null && !json.isBlank()) {
            try {
                List<Valoracion> valoraciones = mapper.readValue(json, new TypeReference<List<Valoracion>>() {});
                publicacion.setValoraciones(valoraciones);
            } catch (Exception e) {
                e.printStackTrace();
                publicacion.setValoraciones(new ArrayList<>());
            }
        } else {
            publicacion.setValoraciones(new ArrayList<>());
        }
    }

    // ---- Auxiliar ----
    private static void rollbackTransaction(EntityManager em) {
        if (em.getTransaction().isActive()) {
            em.getTransaction().rollback();
        }
    }
}

