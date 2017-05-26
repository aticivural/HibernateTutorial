package com.vural;

/**
 * Created by vural on 22-May-17.
 */
import javax.persistence.*;

@Entity
public class Player {

    @Id
    @GeneratedValue
    private Integer id;

    private String lastname;


    @JoinColumn(name = "team_id")
    @ManyToOne
    private Team team1;

    public Player(String lastname) {
        this.lastname = lastname;
    }

    public Player() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Team getTeam1() {
        return team1;
    }

    public void setTeam1(Team team) {
        this.team1 = team;
    }
}