package com.vural;

/**
 * Created by vural on 22-May-17.
 */
import java.util.List;
import java.util.Set;

import javax.persistence.*;

@Entity
public class Team {

    @Id
    @GeneratedValue
    private Integer id;

    private String name;

    @OneToMany(mappedBy="team1", cascade=CascadeType.ALL, orphanRemoval = true)
    private List<Player> players;

    public Team() {
    }

    public Team(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }
}