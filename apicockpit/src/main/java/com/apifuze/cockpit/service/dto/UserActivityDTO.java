package com.apifuze.cockpit.service.dto;

import java.io.Serializable;

/**
 * A DTO for the DashBoard entity.
 */
public class UserActivityDTO implements Serializable {

    private String name;

    private String description;

    private Integer count;


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
