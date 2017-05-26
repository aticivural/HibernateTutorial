package com.vural;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by vural on 23-May-17.
 */

@Entity
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column
    String name;

    @ManyToMany(mappedBy = "authors")
    Set<Book> books;
}
