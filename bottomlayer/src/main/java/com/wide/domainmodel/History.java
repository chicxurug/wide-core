package com.wide.domainmodel;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.wide.domainmodel.user.User;

@Entity
@Table(name = "History")
public class History {

    private Long id;
    private User user;
    private Solvable solvable;
    private Boolean already_seen;
    private Boolean already_solved;

    public History() {

    }

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getAlready_seen() {
        return this.already_seen;
    }

    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    public Solvable getSolvable() {
        return this.solvable;
    }

    public void setSolvable(Solvable solvable) {
        this.solvable = solvable;
    }

    public void setAlready_seen(Boolean already_seen) {
        this.already_seen = already_seen;
    }

    public Boolean getAlready_solved() {
        return this.already_solved;
    }

    public void setAlready_solved(Boolean already_solved) {
        this.already_solved = already_solved;
    }

}
