package com.wide.domainmodel;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "Feature")
public class Feature {

    public enum FEATURE_TYPES {
        SHORT_TEXT,
        LONG_TEXT,
        VERIFYABLE_STEP,
        TAG
    };

    private Long id;
    private String name;
    private String value;
    private FEATURE_TYPES type;

    public Feature() {

    }

    public Feature(String name, String value, FEATURE_TYPES type) {
        this.name = name;
        this.value = value;
        this.type = type;
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

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public FEATURE_TYPES getType() {
        return this.type;
    }

    public void setType(FEATURE_TYPES type) {
        this.type = type;
    }
}
