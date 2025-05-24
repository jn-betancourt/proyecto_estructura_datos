package com.bindr.persistencia;

import jakarta.persistence.*;

public class HibernateConfig {
      private static EntityManagerFactory emf;
    private static final ThreadLocal<EntityManager> threadLocalManager = new ThreadLocal<>();

    // Singleton seguro para multi-hilos
    public static EntityManager getEntityManager() {
        if (emf == null) {
            emf = Persistence.createEntityManagerFactory("bindr");
        }
        
        EntityManager manager = threadLocalManager.get();
        if (manager == null || !manager.isOpen()) {
            manager = emf.createEntityManager();
            threadLocalManager.set(manager);
        }
        return manager;
    }

    // Cierra el EntityManager y limpia el ThreadLocal
    public static void closeEntityManager() {
        EntityManager manager = threadLocalManager.get();
        if (manager != null && manager.isOpen()) {
            manager.close();
        }
        threadLocalManager.remove();
    }

    // Cierra la fábrica al finalizar la aplicación
    public static void shutdown() {
        if (emf != null && emf.isOpen()) {
            System.out.println("se cerro el entity manager");
            emf.close();
        }
    }

}