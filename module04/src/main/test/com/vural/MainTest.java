package com.vural;

import com.vural.hibernate.util.SessionUtil;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

import java.util.List;

/**
 * Created by vural on 22-May-17.
 */
public class MainTest {

    @Test
    public void testBookSave(){
        Session session = SessionUtil.getSession();
        Transaction transaction = session.beginTransaction();

        Book b1 = new Book("book1");
        Book b2 = new Book("book2");

        session.save(b1);
        session.save(b2);

        transaction.commit();
        session.close();

        session = SessionUtil.getSession();
        transaction = session.beginTransaction();

        Query query = session.createQuery("from Book ");
        List list = query.list();

        assertEquals(list.size(), 2);

        transaction.commit();
        session.close();
    }

    @Test
    public void testBookSave2(){
        Session session = SessionUtil.getSession();
        Transaction transaction = session.beginTransaction();

        Book b1 = new Book("book3");
        Book b2 = new Book("book4");

        session.save(b1);
        session.save(b2);

        transaction.commit();
        session.close();

    }
}
