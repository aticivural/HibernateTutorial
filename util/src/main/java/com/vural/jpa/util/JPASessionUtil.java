package com.vural.jpa.util;

import org.hibernate.Session;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by vural on 20-May-17.
 */
public class JPASessionUtil {

    static Map<String, EntityManagerFactory> persistenceUnits = new HashMap<>();

    public static synchronized EntityManager getEntityManager (String persistanceUnitName){
        persistenceUnits.computeIfAbsent(persistanceUnitName, Persistence::createEntityManagerFactory);

        return persistenceUnits.get(persistanceUnitName).createEntityManager();
    }

    public static Session getSession(String persistenceUnitName){
        return getEntityManager(persistenceUnitName).unwrap(Session.class);
    }

}
