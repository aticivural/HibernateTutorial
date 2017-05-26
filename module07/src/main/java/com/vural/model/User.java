package com.vural.model;

/**
 * Created by vural on 26-May-17.
 */
import lombok.*;
import lombok.experimental.Builder;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@Builder
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@FilterDefs({
        @FilterDef(name = "byStatus", parameters = @ParamDef(name = "status", type = "boolean")),
        @FilterDef(name = "byGroup", parameters = @ParamDef(name = "group", type = "string")),
        @FilterDef(name = "userEndsWith1")
})
@Filters({
        @Filter(name = "byStatus", condition = "active = :status"),
        @Filter(name = "byGroup", condition = ":group in ( select ug.groups from user_groups ug where ug.user_id = id)"),
        @Filter(name = "userEndsWith1", condition = "name like '%1'")
})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer id;

    @Getter
    @Setter
    @Column(unique = true)
    String name;

    @Getter
    @Setter
    boolean active;

    @Getter
    @Setter
    @ElementCollection
    Set<String> groups;

    public void addGroups(String... groupSet) {
        if (getGroups() == null) {
            setGroups(new HashSet<>());
        }
        getGroups().addAll(Arrays.asList(groupSet));

    }
}
