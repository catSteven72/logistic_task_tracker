package com.task_helper.task_helper.DTO;

import java.util.List;

public class PlaceDTO {
    private Long id;
    private String city;
    private String country;
    private String forwarder;
    private Short index;
    private List<SubtaskDTO> subtasks;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Short getIndex() {
        return index;
    }

    public void setIndex(Short index) {
        this.index = index;
    }

    public List<SubtaskDTO> getSubtasks() {
        return subtasks;
    }

    public void setSubtasks(List<SubtaskDTO> subtasks) {
        this.subtasks = subtasks;
    }

    public String getForwarder() {
        return forwarder;
    }

    public void setForwarder(String forwarder) {
        this.forwarder = forwarder;
    }
}
