package com.vural;

import com.vural.hibernate.util.SessionUtil;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Created by vural on 22-May-17.
 */
public class Main {
    public static void main(String[] args) {

        Session session = SessionUtil.getSession();
        Transaction transaction = session.beginTransaction();

        Book b1 = new Book("book1");
        Book b2 = new Book("book2");
        Book b3 = new Book("book3");
        Book b4 = new Book("book4");

        List<Book> bookList = new ArrayList<>();
        bookList.add(b1);
        bookList.add(b2);
        bookList.add(b3);
        bookList.add(b4);

        Library library = new Library();
        library.setBooks(bookList);

        session.save(b1);
        session.save(b2);
        session.save(b3);
        session.save(b4);
        session.save(library);

        transaction.commit();
        session.close();


        session = SessionUtil.getSession();
        transaction = session.beginTransaction();


        transaction.commit();
        session.close();
    }
}



class Team_Player_Main {
    public static void main(String[] args) {
        Session session = SessionUtil.getSession();
        Transaction transaction = session.beginTransaction();

        Team team = new Team("Barcelona");
        List<Player> players = new ArrayList<>();

        Player p1 = new Player("Messi");
        Player p2 = new Player("Xavi");
        Player p3 = new Player("Arda");
        Player p4 = new Player("Pique");

        p1.setTeam1(team);
        p2.setTeam1(team);
        p3.setTeam1(team);
        p4.setTeam1(team);

        players.add(p1);
        players.add(p2);
        players.add(p3);
        players.add(p4);

        team.setPlayers(players);

        session.save(team);

        transaction.commit();
        session.close();


        session = SessionUtil.getSession();
        transaction = session.beginTransaction();

        Team t = (Team) session.get(Team.class, 1);
        t.getPlayers().remove(0);
        session.save(t);

        Player player = (Player) session.get(Player.class, 5);
        Query query = session.createQuery("delete from Player where id=:id");
        query.setInteger("id",player.getId());
        query.executeUpdate();


        transaction.commit();
        session.close();

    }
}