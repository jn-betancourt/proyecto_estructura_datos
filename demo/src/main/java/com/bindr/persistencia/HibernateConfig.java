package com.bindr.persistencia;

import jakarta.persistence.*;

public class HibernateConfig {
    private static EntityManagerFactory emf;

    public static EntityManager getEntityManager() {
        if (emf == null) {
            emf = Persistence.createEntityManagerFactory("bindr");
        }
        return emf.createEntityManager();
    }

}