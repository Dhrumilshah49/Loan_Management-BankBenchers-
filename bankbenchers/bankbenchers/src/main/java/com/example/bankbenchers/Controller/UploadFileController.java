package com.example.bankbenchers.Controller;

import com.example.bankbenchers.Entity.Beneficiary;
import com.example.bankbenchers.Entity.Loan;
import com.example.bankbenchers.Repositories.BeneficiaryRepository;
import com.example.bankbenchers.Repositories.LoanRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

@RestController
@RequestMapping("/upload")
public class UploadFileController {

    @Autowired
    private BeneficiaryRepository beneficiaryRepo;
    @Autowired
    private LoanRepository loanRepo;

    @PostMapping("/beneficiaries")
    public ResponseEntity<?> uploadBeneficiaries(@RequestParam("file") MultipartFile file) throws IOException {
        List<Map<String, Object>> errors = new ArrayList<>();
        System.out.println(">> [Beneficiaries Upload] Started reading CSV...");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
             CSVParser csv = new CSVParser(reader, CSVFormat.DEFAULT.withHeader())) {

            int row = 1;
            for (CSVRecord record : csv) {
                row++;
                String id = record.get("beneficiaryId");
                String name = record.get("name");
                String caste = record.get("caste");

                System.out.println(">> Row " + row + ": Validating beneficiary ID = " + id);

                if (id == null || id.trim().isEmpty()) {
                    System.out.println("!! Row " + row + ": Missing beneficiary ID");
                    Map<String, Object> error = new HashMap<>();
                    error.put("row", row);
                    error.put("message", "Missing beneficiary ID");
                    errors.add(error);
                    continue;
                }

                if (name == null || name.trim().isEmpty()) {
                    System.out.println("!! Row " + row + ": Missing name");
                    Map<String, Object> error = new HashMap<>();
                    error.put("row", row);
                    error.put("message", "Missing name");
                    errors.add(error);
                    continue;
                }

                List<String> validCastes = Arrays.asList("GEN", "OBC", "SC", "ST", "Other");
                if (!validCastes.contains(caste)) {
                    System.out.println("!! Row " + row + ": Invalid caste - " + caste);
                    Map<String, Object> error = new HashMap<>();
                    error.put("row", row);
                    error.put("message", "Invalid caste");
                    errors.add(error);
                    continue;
                }

                if (beneficiaryRepo.existsByBeneficiaryId(id)) {
                    System.out.println("!! Row " + row + ": Duplicate beneficiary ID - " + id);
                    Map<String, Object> error = new HashMap<>();
                    error.put("row", row);
                    error.put("message", "Duplicate beneficiary ID: " + id);
                    errors.add(error);
                    continue;
                }

                System.out.println(">> Row " + row + ": Saving beneficiary: " + name);
                beneficiaryRepo.save(new Beneficiary(null, name, id, caste));
            }
        }

        if (!errors.isEmpty()) {
            System.out.println(">> [Beneficiaries Upload] Completed with " + errors.size() + " errors.");
            Map<String, Object> response = new HashMap<>();
            response.put("errors", errors);
            return ResponseEntity.badRequest().body(response);
        }

        System.out.println(">> [Beneficiaries Upload] Completed successfully.");
        return ResponseEntity.ok("Upload successful");
    }

    @PostMapping("/loans")
    public ResponseEntity<?> uploadLoans(@RequestParam("file") MultipartFile file) throws IOException {
        List<Map<String, Object>> errors = new ArrayList<>();
        System.out.println(">> [Loans Upload] Started reading CSV...");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
             CSVParser csv = new CSVParser(reader, CSVFormat.DEFAULT.withHeader())) {

            int row = 1;
            for (CSVRecord record : csv) {
                row++;
                try {
                    String applicationId = record.get("applicationId");
                    String beneficiaryId = record.get("beneficiaryId");
                    String bankName = record.get("bank_name");
                    String bankType = record.get("bank_type");
                    String accountNo = record.get("account_no");
                    String ifscCode = record.get("ifsc_code");
                    String loanAmountStr = record.get("loan_amount");
                    String sanctionAmountStr = record.get("sanction_amount");

                    System.out.println(">> Row " + row + ": Validating loan for Beneficiary ID = " + beneficiaryId);

                    if (!beneficiaryRepo.existsByBeneficiaryId(beneficiaryId)) {
                        System.out.println("!! Row " + row + ": Beneficiary ID not found - " + beneficiaryId);
                        Map<String, Object> error = new HashMap<>();
                        error.put("row", row);
                        error.put("message", "Beneficiary ID '" + beneficiaryId + "' does not exist");
                        errors.add(error);
                        continue;
                    }

                    if (!accountNo.matches("[a-zA-Z0-9]+")) {
                        System.out.println("!! Row " + row + ": Invalid account number - " + accountNo);
                        Map<String, Object> error = new HashMap<>();
                        error.put("row", row);
                        error.put("message", "Invalid account number: must be alphanumeric");
                        errors.add(error);
                        continue;
                    }

                    if (!ifscCode.matches("^[A-Z]{4}0[A-Z0-9]{6}$")) {
                        System.out.println("!! Row " + row + ": Invalid IFSC code - " + ifscCode);
                        Map<String, Object> error = new HashMap<>();
                        error.put("row", row);
                        error.put("message", "Invalid IFSC code");
                        errors.add(error);
                        continue;
                    }

                    double loanAmount = Double.parseDouble(loanAmountStr);
                    double sanctionAmount = Double.parseDouble(sanctionAmountStr);

                    if (loanAmount < 0 || sanctionAmount < 0) {
                        System.out.println("!! Row " + row + ": Negative loan or sanction amount");
                        Map<String, Object> error = new HashMap<>();
                        error.put("row", row);
                        error.put("message", "Loan or sanction amount must be ≥ 0");
                        errors.add(error);
                        continue;
                    }

                    if (sanctionAmount > loanAmount) {
                        System.out.println("!! Row " + row + ": Sanction > Loan amount");
                        Map<String, Object> error = new HashMap<>();
                        error.put("row", row);
                        error.put("message", "Sanction amount cannot exceed loan amount");
                        errors.add(error);
                        continue;
                    }

                    Loan loan = new Loan();
                    loan.setApplicationId(applicationId);
                    loan.setBeneficiaryId(beneficiaryId);
                    loan.setBankName(bankName);
                    loan.setBankType(bankType);
                    loan.setAccountNo(accountNo);
                    loan.setIfscCode(ifscCode);
                    loan.setLoanAmount(loanAmount);
                    loan.setSanctionAmount(sanctionAmount);

                    System.out.println(" Row " + row + ": Saving loan for beneficiary: " + beneficiaryId);
                    loanRepo.save(loan);

                } catch (Exception e) {
                    System.out.println("Row " + row + ": Exception occurred - " + e.getMessage());
                    Map<String, Object> error = new HashMap<>();
                    error.put("row", row);
                    error.put("message", "Malformed row or missing fields");
                    errors.add(error);
                }
            }
        }

        if (!errors.isEmpty()) {
            System.out.println("Loans File Upload Completed with " + errors.size() + " errors.");
            Map<String, Object> response = new HashMap<>();
            response.put("errors", errors);
            return ResponseEntity.badRequest().body(response);
        }

        System.out.println("Loans File Upload Completed successfully.");
        return ResponseEntity.ok("Loans uploaded successfully");
    }
}
