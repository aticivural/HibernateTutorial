package com.vural.test;

/**
 * Created by vural on 26-May-17.
 */
import com.vural.hibernate.util.SessionUtil;
import com.vural.model.Product;
import com.vural.model.Software;
import com.vural.model.Supplier;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.testng.Assert.assertEquals;

public class QueryTest {
    Session session;
    Transaction tx;

    @BeforeMethod
    public void populateData() {
        Session session = SessionUtil.getSession();
        Transaction tx = session.beginTransaction();

        Supplier supplier = new Supplier("Hardware, Inc.");
        supplier.getProducts().add( new Product(supplier, "Optical Wheel Mouse", "Mouse", 5.00));
        supplier.getProducts().add( new Product(supplier, "Trackball Mouse", "Mouse", 22.00));
        session.save(supplier);

        supplier = new Supplier("Supplier 2");
        supplier.getProducts().add( new Software(supplier, "SuperDetect", "Antivirus", 14.95, "1.0"));
        supplier.getProducts().add( new Software(supplier, "Wildcat", "Browser", 19.95, "2.2"));
        supplier.getProducts().add( new Product(supplier, "AxeGrinder", "Gaming Mouse", 42.00));

        session.save(supplier);
        tx.commit();
        session.close();

        this.session = SessionUtil.getSession();
        this.tx = this.session.beginTransaction();
    }

    @AfterMethod
    public void closeSession() {
        session.createQuery("delete from Product").executeUpdate();
        session.createQuery("delete from Supplier").executeUpdate();
        if (tx.isActive()) {
            tx.commit();
        }
        if (session.isOpen()) {
            session.close();
        }
    }

    @Test
    public void testNamedQuery() {
        Query query = session.getNamedQuery("supplier.findAll");
        List<Supplier> suppliers = query.list();
        assertEquals(suppliers.size(), 2);
    }
    @Test
    public void testJoin() {
        Query query = session.getNamedQuery("product.findProductAndSupplier");
        List<Object[]> suppliers = query.list();
        for(Object[] o:suppliers) {
            Assert.assertTrue(o[0] instanceof Product);
            Assert.assertTrue(o[1] instanceof Supplier);
        }
        assertEquals(suppliers.size(), 5);
    }
    @Test
    public void testSimpleQuery() {
        Query query = session.createQuery("from Product");
        query.setComment("This is only a query for product");
        List<Product> products = query.list();
        assertEquals(products.size(), 5);
    }

    @Test
    public void testSimpleProjection() {
        Query query = session.createQuery("select p.name from Product p");
        query.setFirstResult(1);
        query.setMaxResults(2);
        List<String> suppliers = query.list();
        for(String s:suppliers) {
            System.out.println(s);
        }
        assertEquals(suppliers.size(), 5);
    }

    @Test
    public void testProjection() {
        Query query = session.getNamedQuery("supplier.findAverage");
        List<Object[]> suppliers = query.list();
        for (Object[] o : suppliers) {
            System.out.println(Arrays.toString(o));
        }

        for (Object[] o : suppliers) {
            System.out.print(o[0] + "\t");
            System.out.print(o[1] + "\n");
        }

        assertEquals(suppliers.size(), 2);
    }

    @Test
    public void testPaging() {
        Query query = session.createQuery("select p.name from Product p");
        query.setFirstResult(1);
        query.setMaxResults(2);
        List<String> suppliers = query.list();
        for(String s:suppliers) {
            System.out.println(s);
        }
        assertEquals(suppliers.size(), 2);
    }

    @Test
    public void testLikeQuery() {
        Query query = session.createQuery(
                "from Product p where p.price > :minprice and p.description like :desc");
        query.setParameter("desc", "Mou%");
        query.setParameter("minprice", 15.0);
        List<Product> products = query.list();
        assertEquals(products.size(), 1);
    }

    @DataProvider
    Object[][] queryTypesProvider() {
        return new Object[][]{
                {"Supplier", 2},
                {"Product", 5},
                {"Software", 2},
        };
    }

    @Test(dataProvider = "queryTypesProvider")
    public void testQueryTypes(String type, Integer count) {
        Query query = session.createQuery("from " + type);
        List list = query.list();
        assertEquals(list.size(), count.intValue());
    }

    @Test
    public void searchForProduct() {
        Query query = session.getNamedQuery("product.searchByPhrase");
        query.setParameter("text", "%Mou%");
        List<Product> products = query.list();
        assertEquals(products.size(), 3);
    }

    @Test
    public void testUniqueResult(){
        String hql = "from Product where price>25.0";
        Query query = session.createQuery(hql);
        //query.setMaxResults(1);

//        uniqueResult method returns only one and unique result so if it has one more than result then it throws exception
        Product product = (Product) query.uniqueResult();
        System.out.println(product);
    }
}

