package com.vural.lifecycle;

/**
 * Created by vural on 25-May-17.
 */
import com.vural.jpa.util.JPASessionUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.testng.annotations.Test;

import java.util.Date;

import static org.testng.Assert.*;

public class ExternalListenerTest {
    @Test
    public void testExternalListener() {
        Integer id;
        String accountName = Long.toString(new Date().getTime());
        Session session = JPASessionUtil.getSession("utiljpa");
        Transaction tx = session.beginTransaction();
        UserAccount ua = new UserAccount();

        ua.setName(accountName);
        ua.setPassword("foobar");

        session.persist(ua);
        id = ua.getId();
        tx.commit();
        session.close();

        session = JPASessionUtil.getSession("utiljpa");
        //tx = session.beginTransaction();
        UserAccount ua2 = (UserAccount) session.byId(UserAccount.class).getReference(id);
        assertEquals(ua.getName(), ua2.getName());
        assertEquals(ua.getPasswordHash(), ua2.getPasswordHash());

        assertTrue(ua2.validPassword("foobar"));
        assertFalse(ua2.validPassword("barfoo"));

        //tx.commit();
        session.close();
    }
}