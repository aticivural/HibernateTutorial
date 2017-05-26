package com.vural.test;

import com.vural.model.Person;
import com.vural.model.Ranking;
import com.vural.model.Skill;
import com.vural.service.HibernateRankingService;
import com.vural.service.RankingService;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

/**
 * Created by vural on 19-May-17.
 */

public class RankingTest {
    SessionFactory factory;
    RankingService service = new HibernateRankingService();
    @BeforeMethod
    public void setup() {
        Configuration configuration = new Configuration();
        configuration.configure();
        ServiceRegistryBuilder srBuilder = new ServiceRegistryBuilder();
        srBuilder.applySettings(configuration.getProperties());
        ServiceRegistry serviceRegistry = srBuilder.buildServiceRegistry();
        factory = configuration.buildSessionFactory(serviceRegistry);
    }

    @AfterMethod
    public void shutdown() {
        factory.close();
    }

    @Test
    public void testSaveRanking(){
        Session session = factory.openSession();
        Transaction transaction = session.beginTransaction();

        Person subject = savePerson(session, "J. C. Smell");
        Person observer = savePerson(session, "Drew Lombardo");
        Skill skill = saveSkill(session, "Java");

        Ranking ranking = new Ranking(subject, observer, skill, 8);
        session.save(ranking);

        transaction.commit();
        session.close();

    }

    @Test
    public void testRankings() {
        populateRankingData();

        Session session = factory.openSession();
        Transaction transaction = session.beginTransaction();

        Query query = session.createQuery("from Ranking r where r.subject.name=:name and r.skill.name=:skill");
        query.setString("name", "J. C. Smell");
        query.setString("skill", "Java");

        int sum = 0;
        int count = 0;

        List<Ranking> rankingList = query.list();
        for (Ranking r : rankingList) {
            count++;
            sum += r.getRanking();
            System.out.println(r);
        }

        int average = sum / count;
        session.close();
        assertEquals(average, 7);
    }

    @Test
    public void changeRanking(){
        populateRankingData();
        Session session = factory.openSession();
        Transaction transaction = session.beginTransaction();
        Query query = session.createQuery("from Ranking r " +
                "where r.subject.name=:subject " +
                "and r.observer.name=:observer " +
                "and r.skill.name=:skill");
        query.setString("subject", "J. C. Smell");
        query.setString("observer", "Gene Showrama");
        query.setString("skill", "Java");

        Ranking ranking = (Ranking) query.uniqueResult();
        assertNotNull(ranking, "Could not find matching ranking");
        ranking.setRanking(9);
        transaction.commit();
        session.close();
        assertEquals(getAverage("J. C. Smell", "Java"), 8);
    }

    @Test
    public void testChangeRanking(){
        populateRankingData();
        Session session = factory.openSession();
        Transaction transaction = session.beginTransaction();
        Ranking ranking = findRanking(session, "J. C. Smell", "Gene Showrama", "Java");
        assertNotNull(ranking, "Could not find matching ranking");
        ranking.setRanking(9);
        transaction.commit();
        session.close();
        assertEquals(getAverage("J. C. Smell", "Java"), 8);
    }

    @Test
    public void removeRanking(){
        populateRankingData();
        Session session = factory.openSession();
        Transaction transaction = session.beginTransaction();

        Ranking ranking = findRanking(session, "J. C. Smell", "Gene Showrama", "Java");
        assertNotNull(ranking, "Ranking not found");
        session.delete(ranking);
        transaction.commit();
        session.close();
        assertEquals(getAverage("J. C. Smell", "Java"), 7);
    }

    private int getAverage(String subject, String skill) {
        Session session = factory.openSession();
        Transaction tx = session.beginTransaction();

        Query query = session.createQuery("from Ranking r "
                + "where r.subject.name=:name "
                + "and r.skill.name=:skill");
        query.setString("name", subject);
        query.setString("skill", skill);
        int sum = 0;
        int count = 0;
        for (Ranking r : (List<Ranking>) query.list()) {
            count++;
            sum += r.getRanking();
            System.out.println(r);
        }
        int average = sum / count;
        tx.commit();
        session.close();
        return average;
    }

    private void populateRankingData() {
        Session session = factory.openSession();
        Transaction tx = session.beginTransaction();
        createData(session, "J. C. Smell", "Gene Showrama", "Java", 6);
        createData(session, "J. C. Smell", "Scottball Most", "Java", 7);
        createData(session, "J. C. Smell", "Drew Lombardo", "Java", 8);
        tx.commit();
        session.close();
    }

    private void createData(Session session, String subjectName, String observerName, String skillName, int rank) {
        Person subject = savePerson(session, subjectName);
        Person observer = savePerson(session, observerName);
        Skill skill = saveSkill(session, skillName);

        Ranking ranking = new Ranking();
        ranking.setSubject(subject);
        ranking.setObserver(observer);
        ranking.setSkill(skill);
        ranking.setRanking(rank);
        session.save(ranking);
    }

    private Person findPerson(Session session, String name) {
        Query query = session.createQuery("from Person p where p.name=:name");
        query.setParameter("name", name);
        Person person = (Person) query.uniqueResult();
        return person;
    }

    private Skill findSkill(Session session, String name) {
        Query query = session.createQuery("from Skill s where s.name=:name");
        query.setParameter("name", name);
        Skill skill = (Skill) query.uniqueResult();
        return skill;
    }

    private Skill saveSkill(Session session, String skillName) {
        Skill skill = findSkill(session, skillName);
        if (skill == null) {
            skill = new Skill();
            skill.setName(skillName);
            session.save(skill);
        }
        return skill;
    }

    private Person savePerson(Session session, String name) {
        Person person = findPerson(session, name);
        if (person == null) {
            person = new Person();
            person.setName(name);
            session.save(person);
        }
        return person;
    }

    private Ranking findRanking(Session session, String subject, String observer, String skill) {
        Query query = session.createQuery("from Ranking r "
                + "where r.subject.name=:subject and "
                + "r.observer.name=:observer and "
                + " r.skill.name=:skill");
        query.setString("subject", subject);
        query.setString("observer", observer);
        query.setString("skill", skill);
        Ranking ranking = (Ranking) query.uniqueResult();
        return ranking;
    }


//    from here it uses SessionUtil and HibernateRankingSercive for test
    @Test
    public void updateExistingRanking(){
        service.addRanking("Gene Showrama", "Scottball Most", "Ceylon", 6);
        assertEquals(service.getRankingFor("Gene Showrama", "Ceylon"), 6);

        service.updateRanking("Gene Showrama", "Scottball Most", "Ceylon", 7);
        assertEquals(service.getRankingFor("Gene Showrama", "Ceylon"), 7);
    }

    @Test
    public void updateNonexistentRanking() {
        assertEquals(service.getRankingFor("Scottball Most", "Ceylon"), 0);
        service.updateRanking("Scottball Most", "Gene Showrama", "Ceylon", 7);
        assertEquals(service.getRankingFor("Scottball Most", "Ceylon"), 7);
    }

    @Test
    public void remveRanking() {
        service.addRanking("R1", "R2", "RS1", 8);
        assertEquals(service.getRankingFor("R1", "RS1"), 8);
        service.removeRanking("R1", "R2", "RS1");
        assertEquals(service.getRankingFor("R1", "RS1"), 0);
    }
    @Test
    public void removeNonexistentRanking() {
        service.removeRanking("R3", "R4", "RS2");
    }

    @Test
    public void validateRankingAverage(){
        service.addRanking("A", "B", "C", 4);
        service.addRanking("A", "B", "C", 5);
        service.addRanking("A", "B", "C", 6);
        assertEquals(service.getRankingFor("A", "C"), 5);

        service.addRanking("A", "B", "C", 7);
        service.addRanking("A", "B", "C", 8);
        assertEquals(service.getRankingFor("A", "C"), 6);
    }

    @Test
    public void findAllRankingsEmptySet(){
        assertEquals(service.getRankingFor("Nobody", "Java"), 0);
        assertEquals(service.getRankingFor("Nobody", "Python"), 0);

        Map<String, Integer> rankings = service.findRankingsFor("Nobody");
        assertEquals(rankings.size(), 0);
    }

    @Test
    public void findAllRankings(){
        assertEquals(service.getRankingFor("Somebody", "Java"),0);
        assertEquals(service.getRankingFor("Somebody", "Python"),0);
        service.addRanking("Somebody", "Nobody", "Java", 9);
        service.addRanking("Somebody", "Nobody", "Java", 7);
        service.addRanking("Somebody", "Nobody", "Python", 7);
        service.addRanking("Somebody", "Nobody", "Python", 5);

        Map<String, Integer> rankings = service.findRankingsFor("Somebody");
        assertEquals(rankings.size(), 2);
        assertNotNull(rankings.get("Java"));
        assertEquals(rankings.get("Java"), new Integer(8));
        assertNotNull(rankings.get("Python"));
        assertEquals(rankings.get("Python"), new Integer(6));
    }

    @Test
    public void findBestForNonexistentSkill() {
        Person p = service.findBestPersonFor("no skill");
        assertNull(p);
    }

    @Test
    public void findBestForSkill() {
        service.addRanking("S1", "O1", "Sk1", 6);
        service.addRanking("S1", "O2", "Sk1", 8);
        service.addRanking("S2", "O1", "Sk1", 5);
        service.addRanking("S2", "O2", "Sk1", 7);
        service.addRanking("S3", "O1", "Sk1", 7);
        service.addRanking("S3", "O2", "Sk1", 9);
        // data that should not factor in!
        //service.addRanking("S3", "O1", "Sk2", 2);
        Person p = service.findBestPersonFor("Sk1");
        assertEquals(p.getName(), "S3");
    }





    //    personal tests
    @Test
    public void testPersistentLayer(){
        Session session = factory.openSession();
        Transaction transaction = session.beginTransaction();
        Person person = new Person("vural");
        session.save(person);

        Person person1 = findPerson(session, "vural");
        System.out.println("person1 " + person1);
        person.setName("ufuk");


        Query query = session.createQuery("from Person p where p.name=:name");
        query.setString("name", "ufuk");
        Person person2 = (Person) query.uniqueResult();
        System.out.println("person2 " + person2);

        transaction.commit();
        session.close();
    }

}
