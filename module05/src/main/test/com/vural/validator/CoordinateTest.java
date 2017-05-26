package com.vural.validator;

/**
 * Created by vural on 25-May-17.
 */
import com.vural.hibernate.util.SessionUtil;
import com.vural.validated.Coordinate;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import javax.validation.ConstraintViolationException;

import static org.testng.Assert.fail;

public class CoordinateTest {
    private Coordinate persist(Coordinate entity) {
        Session session = SessionUtil.getSession();
        Transaction tx = session.beginTransaction();
        session.persist(entity);
        tx.commit();
        session.close();
        return entity;
    }

    @DataProvider(name = "validCoordinates")
    private Object[][] validCoordinates() {
        return new Object[][]{
                {1, 1},
                {-1, 1},
                {1, -1},
                {0, 0},
        };
    }

    @Test(dataProvider = "validCoordinates")
    public void testValidCoordinate(Integer x, Integer y) {
        Coordinate c = new Coordinate(x, y);
        persist(c);
        // has passed validation, if we reach this point.
    }

    @Test(expectedExceptions = ConstraintViolationException.class)
    public void testInvalidCoordinate() {
        testValidCoordinate(-1, -1);
        fail("Should have gotten a constraint violation");
    }

}
