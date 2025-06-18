package com.example.bankbenchers.Repositories;

import com.example.bankbenchers.Entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanRepository extends JpaRepository<Loan, Long> {
}