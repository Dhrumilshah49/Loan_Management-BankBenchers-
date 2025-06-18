package com.example.bankbenchers.Entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

@Entity
public class Loan implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String applicationId;

    @NotBlank
    private String beneficiaryId;

    private String bankName;
    private String bankType;

    @Pattern(regexp = "^[a-zA-Z0-9]+$")
    private String accountNo;

    @Pattern(regexp = "^[A-Z]{4}0[A-Z0-9]{6}$")
    private String ifscCode;

    @Min(0)
    private Double loanAmount;

    @Min(0)
    private Double sanctionAmount;

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getApplicationId() { return applicationId; }
    public void setApplicationId(String applicationId) { this.applicationId = applicationId; }

    public String getBeneficiaryId() { return beneficiaryId; }
    public void setBeneficiaryId(String beneficiaryId) { this.beneficiaryId = beneficiaryId; }

    public String getBankName() { return bankName; }
    public void setBankName(String bankName) { this.bankName = bankName; }

    public String getBankType() { return bankType; }
    public void setBankType(String bankType) { this.bankType = bankType; }

    public String getAccountNo() { return accountNo; }
    public void setAccountNo(String accountNo) { this.accountNo = accountNo; }

    public String getIfscCode() { return ifscCode; }
    public void setIfscCode(String ifscCode) { this.ifscCode = ifscCode; }

    public Double getLoanAmount() { return loanAmount; }
    public void setLoanAmount(Double loanAmount) { this.loanAmount = loanAmount; }

    public Double getSanctionAmount() { return sanctionAmount; }
    public void setSanctionAmount(Double sanctionAmount) { this.sanctionAmount = sanctionAmount; }
}
