package com.wide.domainmodel.stat;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.wide.domainmodel.user.User;

@Entity
@Table(name = "Log")
public class LogEntry {

    public enum EntryType {
        SEARCH_EXERCISE,
        VIEW_EXERCISE,
        SUBMIT_SOLUTION,
        CHECK_SOLUTION,
        VIEW_PROFILE,
        ADD_EXERCISE,
        MODIFY_EXERCISE
    };

    private Long id;
    private User user;
    private EntryType type;
    private String entry_val;
    private Date time_stamp;
    private String extra;

    public LogEntry() {

    }

    public LogEntry(User user, EntryType type, String entry_val) {
        this.user = user;
        this.type = type;
        this.entry_val = entry_val;
        this.time_stamp = new Date();
    }

    public LogEntry(User user, EntryType type, String entry_val, String extra) {
        this.user = user;
        this.type = type;
        this.entry_val = entry_val;
        this.time_stamp = new Date();
        this.extra = extra;
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

    @ManyToOne(cascade = { CascadeType.ALL, CascadeType.MERGE })
    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public EntryType getType() {
        return this.type;
    }

    public void setType(EntryType type) {
        this.type = type;
    }

    public String getEntry_val() {
        return this.entry_val;
    }

    public void setEntry_val(String entry_val) {
        this.entry_val = entry_val;
    }

    public Date getTime_stamp() {
        return this.time_stamp;
    }

    public void setTime_stamp(Date time_stamp) {
        this.time_stamp = time_stamp;
    }

    public String getExtra() {
        return this.extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }
}
