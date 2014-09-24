package com.wide.wideweb.beans;

import java.io.Serializable;
import java.util.Date;

import org.springframework.security.authentication.encoding.ShaPasswordEncoder;

import com.wide.domainmodel.Exercise.SchoolLevel;
import com.wide.domainmodel.user.Profile;
import com.wide.domainmodel.user.User;

public class UserProfileBean implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 5821508529940785366L;

    private String username;
    private String password;
    private String name;
    private String email;
    private String gender;
    private Date date_of_birth;
    private SchoolLevel edu_level;
    private String interests;

    public UserProfileBean() {
        this.gender = "Female";
        this.date_of_birth = new Date();
        this.interests = "";
        this.edu_level = SchoolLevel.ELEMENTARY;
    }

    public User getAccount() {
        ShaPasswordEncoder passwordEncoder = new ShaPasswordEncoder();
        String encPassword = passwordEncoder.encodePassword(this.password, null);
        User newUser = new User(this.username, encPassword, true);
        Profile newProfile = new Profile(this.name, this.gender.equals("Female"), this.email, this.date_of_birth, this.edu_level, this.interests);
        newProfile.setAccount(newUser);
        newUser.setProfile(newProfile);
        return newUser;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return this.gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
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

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
