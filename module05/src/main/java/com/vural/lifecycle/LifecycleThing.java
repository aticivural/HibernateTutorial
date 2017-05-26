package com.vural.lifecycle;

/**
 * Created by vural on 25-May-17.
 */
import org.jboss.logging.Logger;

import javax.persistence.*;
import java.util.BitSet;
import java.util.Objects;

@Entity
public class LifecycleThing {
    static Logger logger = Logger.getLogger(LifecycleThing.class);
    static BitSet lifecycleCalls = new BitSet();

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer id;

    @Column
    String name;

    public LifecycleThing() {
    }

    public LifecycleThing(String name) {
        this.name = name;
    }

    public static Logger getLogger() {
        return logger;
    }

    public static void setLogger(Logger logger) {
        LifecycleThing.logger = logger;
    }

    public static BitSet getLifecycleCalls() {
        return lifecycleCalls;
    }

    public static void setLifecycleCalls(BitSet lifecycleCalls) {
        LifecycleThing.lifecycleCalls = lifecycleCalls;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LifecycleThing that = (LifecycleThing) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        return name != null ? name.equals(that.name) : that.name == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "LifecycleThing{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    @PostLoad
    public void postLoad() {
        log("postLoad", 0);
    }

    @PrePersist
    public void prePersist() {
        log("prePersist", 1);
    }

    @PostPersist
    public void postPersist() {
        log("postPersist", 2);
    }

    @PreUpdate
    public void preUpdate() {
        log("preUpdate", 3);
    }

    @PostUpdate
    public void postUpdate() {
        log("postUpdate", 4);
    }

    @PreRemove
    public void preRemove() {
        log("preRemove", 5);
    }

    @PostRemove
    public void postRemove() {
        log("postRemove", 6);
    }

    private void log(String method, int index) {
        lifecycleCalls.set(index, true);
        logger.errorf("%12s: %s (%s)", method, this.getClass().getSimpleName(), this.toString());
    }
}
