package com.bindr.dao;

import com.bindr.persistencia.HibernateConfig;
import com.bindr.modelos.Conversacion;
import com.bindr.modelos.Mensaje;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

import java.io.IOException;
import java.util.List;

public class ConversacionDao {

     // ObjectMapper configurado para LocalDateTime
    private static final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

   public static List<Conversacion> obtenerPorUsuarioId(Long usuarioId) {
    EntityManager manager = HibernateConfig.getEntityManager();
    try {
        // Buscar el objeto Estudiante por su ID
        com.bindr.modelos.Estudiante estudiante = manager.find(com.bindr.modelos.Estudiante.class, usuarioId);
        if (estudiante == null) {
            return List.of();
        }
        TypedQuery<Conversacion> query = manager.createQuery(
            "SELECT DISTINCT c FROM Conversacion c " +
            "JOIN FETCH c.participantes " +
            "WHERE :estudiante MEMBER OF c.participantes",
            Conversacion.class
        );
        query.setParameter("estudiante", estudiante);
        List<Conversacion> conversaciones = query.getResultList();

        conversaciones.forEach(ConversacionDao::deserializarMensajes);

        return conversaciones;
    } catch (Exception e) {
        e.printStackTrace();
        return List.of();
    } finally {
        HibernateConfig.closeEntityManager();
    }
}

    public static Conversacion buscarPorId(Long id) {
        EntityManager manager = HibernateConfig.getEntityManager();
        try {
            System.out.println(id);
            Conversacion conversacion = manager.find(Conversacion.class, id);
            System.out.println(conversacion.getMensajesJson());
            if (conversacion != null) {
                System.out.println("se encontro");
                deserializarMensajes(conversacion);
            }
            return conversacion;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }finally{
            HibernateConfig.closeEntityManager();
        }
    }

    public static List<Conversacion> listarTodas() {
        EntityManager manager = HibernateConfig.getEntityManager();
        try {
            // Usar DISTINCT para evitar duplicados por el join
            TypedQuery<Conversacion> query = manager.createQuery(
                "SELECT DISTINCT c FROM Conversacion c LEFT JOIN FETCH c.participantes", Conversacion.class);
            List<Conversacion> lista = query.getResultList();
            // Deserializar mensajes en cada conversacion
            lista.forEach(ConversacionDao::deserializarMensajes);
            return lista;
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        } finally {
            HibernateConfig.closeEntityManager();
        }
    }

    private static void deserializarMensajes(Conversacion conversacion) {
        
        String json = conversacion.getMensajesJson();
        if (json != null && !json.isBlank()) {
            try {
                List<Mensaje> mensajes = mapper.readValue(json, new TypeReference<List<Mensaje>>() {});
                conversacion.setMensajes(mensajes);
            } catch (Exception e) {
                e.printStackTrace();
                // En caso de error dejamos lista mensajes vacía para evitar NPE
                conversacion.setMensajes(List.of());
            }
        } else {
            conversacion.setMensajes(List.of());
        }
    }

    // Ejemplo para guardar o actualizar Conversacion (serializando mensajes)
    public static boolean guardar(Conversacion conversacion) {
        EntityManager manager = HibernateConfig.getEntityManager();
        try {
            manager.getTransaction().begin();

            // Serializar mensajes antes de persistir
            if (conversacion.getMensajes() != null) {
                String json = mapper.writeValueAsString(conversacion.getMensajes());
                conversacion.setMensajesJson(json);
            }

            if (conversacion.getId() == null) {
                manager.persist(conversacion);
            } else {
                manager.merge(conversacion);
            }

            manager.getTransaction().commit();
            return true;
        } catch (Exception e) {
            if (manager.getTransaction().isActive()) {
                manager.getTransaction().rollback();
            }
            e.printStackTrace();
            return false;
        }finally{
            HibernateConfig.closeEntityManager();
        }
    }

    // Buscar por ID
   public static Conversacion obtenerPorId(Long id) {
    EntityManager manager = HibernateConfig.getEntityManager();
    try {
        TypedQuery<Conversacion> query = manager.createQuery(
            "SELECT c FROM Conversacion c " +
            "JOIN FETCH c.participantes " +
            "WHERE c.id = :id", 
            Conversacion.class
        );
        query.setParameter("id", id);
        Conversacion conv = query.getSingleResult();

        if (conv.getMensajesJson() != null) {
            List<Mensaje> mensajes = mapper.readValue(
                conv.getMensajesJson(),
                new TypeReference<List<Mensaje>>() {}
            );
            conv.setMensajes(mensajes);
        }

        return conv;
    } catch (NoResultException e) {
        return null;
    } catch (IOException e) {
        e.printStackTrace();
        return null;
    } finally {
        HibernateConfig.closeEntityManager();
    }
}

    // Actualizar
    public static boolean actualizarConversacion(Conversacion conv) {
        EntityManager manager = HibernateConfig.getEntityManager();
        try {
            manager.getTransaction().begin();
            if (conv.getMensajes() != null) {
                String mensajesJson = mapper.writeValueAsString(conv.getMensajes());
                conv.setMensajesJson(mensajesJson);
            }
            manager.merge(conv);
            manager.getTransaction().commit();
            return true;
        } catch (Exception e) {
            rollbackTransaction(manager);
            e.printStackTrace();
            return false;
        }finally{
            HibernateConfig.closeEntityManager();
        }
    }

    // Eliminar
    public static boolean eliminarConversacion(Long id) {
        EntityManager manager = HibernateConfig.getEntityManager();
        try {
            manager.getTransaction().begin();
            Conversacion conv = manager.find(Conversacion.class, id);
            if (conv != null) {
                manager.remove(conv);
            }
            manager.getTransaction().commit();
            return true;
        } catch (Exception e) {
            rollbackTransaction(manager);
            e.printStackTrace();
            return false;
        }finally{
            HibernateConfig.closeEntityManager();
        }
    }

    // ---- Auxiliar ----
    private static void rollbackTransaction(EntityManager em) {
        if (em.getTransaction().isActive()) {
            em.getTransaction().rollback();
        }
    }
}
