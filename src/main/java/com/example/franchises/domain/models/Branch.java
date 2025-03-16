package com.example.franchises.domain.models;


public class Branch {

    private Long id;
    private String name;
    private Long franchiseId;

    public Branch(Long id, String name, Long franchiseId) {
        this.id = id;
        this.name = name;
        this.franchiseId = franchiseId;
    }

    public Branch() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getFranchiseId() {
        return franchiseId;
    }

    public void setFranchiseId(Long franchiseId) {
        this.franchiseId = franchiseId;
    }
}
