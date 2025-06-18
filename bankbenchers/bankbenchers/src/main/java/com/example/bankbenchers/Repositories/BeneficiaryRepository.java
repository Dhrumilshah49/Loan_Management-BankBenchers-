package com.example.bankbenchers.Repositories;

import com.example.bankbenchers.Entity.Beneficiary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BeneficiaryRepository extends JpaRepository<Beneficiary, Long> {
    boolean existsByBeneficiaryId(String beneficiaryId);
}
