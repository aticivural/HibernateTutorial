package com.vural;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

import java.util.List;

/**
 * Created by vural on 19-May-17.
 */
public class Main {

    static SessionFactory sessionFactory;

    public static void main(String[] args) {
        setup();

        saveMessage();

        readMessage();
    }

    private static void setup() {
        Configuration configuration = new Configuration();
        configuration.configure();

        ServiceRegistryBuilder serviceRegistryBuilder = new ServiceRegistryBuilder();
        serviceRegistryBuilder.applySettings(configuration.getProperties());

        ServiceRegistry serviceRegistry = serviceRegistryBuilder.buildServiceRegistry();
        sessionFactory = configuration.buildSessionFactory(serviceRegistry);

    }

    private static void saveMessage() {

        Message message = new Message("Hello, world");
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        
        session.persist(message);
        transaction.commit();
        session.close();
    }

    private static void readMessage() {
        Session session = sessionFactory.openSession();
        @SuppressWarnings("unchecked")
        List<Message> list = (List<Message>) session.createQuery("from Message").list();

        for (Message m : list) {
            System.out.println(m);
        }
        session.close();
    }
}
