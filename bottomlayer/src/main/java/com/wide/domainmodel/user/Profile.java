package com.wide.domainmodel.user;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.wide.domainmodel.Exercise.SchoolLevel;

@Entity
@Table(name = "Profile")
public class Profile {

    private Long id;
    private String name;
    private Boolean female;
    private Date date_of_birth;
    private String email;
    private SchoolLevel edu_level;
    private String interests;
    private User account;

    public Profile() {

    }

    public Profile(String name, Boolean female, String email, Date date_of_birth, SchoolLevel edu_level, String interests) {
        this.name = name;
        this.female = female;
        this.email = email;
        this.date_of_birth = date_of_birth;
        this.edu_level = edu_level;
        this.interests = interests;
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

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getFemale() {
        return this.female;
    }

    public void setFemale(Boolean female) {
        this.female = female;
    }

    public Date getDate_of_birth() {
        return this.date_of_birth;
    }

    public void setDate_of_birth(Date date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public SchoolLevel getEdu_level() {
        return this.edu_level;
    }

    public void setEdu_level(SchoolLevel edu_level) {
        this.edu_level = edu_level;
    }

    public String getInterests() {
        return this.interests;
    }

    public void setInterests(String interests) {
        this.interests = interests;
    }

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "frn_user_id")
    public User getAccount() {
        return this.account;
    }

    public void setAccount(User account) {
        this.account = account;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
