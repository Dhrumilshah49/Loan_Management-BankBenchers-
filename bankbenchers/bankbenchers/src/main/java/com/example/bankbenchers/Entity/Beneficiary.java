package com.example.bankbenchers.Entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Entity
public class Beneficiary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true)
    private String beneficiaryId;

    private String caste;

    public Beneficiary() {}

    public Beneficiary(Long id, String name, String beneficiaryId, String caste) {
        this.id = id;
        this.name = name;
        this.beneficiaryId = beneficiaryId;
        this.caste = caste;
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getBeneficiaryId() { return beneficiaryId; }
    public void setBeneficiaryId(String beneficiaryId) { this.beneficiaryId = beneficiaryId; }

    public String getCaste() { return caste; }
    public void setCaste(String caste) { this.caste = caste; }
}