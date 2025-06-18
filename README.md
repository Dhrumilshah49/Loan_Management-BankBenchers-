# Loan_Management-BankBenchers-
CodeBase of : Beneficiary and Loan Management

# Bankbenchers Spring Boot App

A Spring Boot application for uploading beneficiary and loan data via CSV, with JWT-based authentication and validation.

---

 Features

JWT Login Authentication
CSV Upload for Beneficiaries & Loans
Input validation with error reporting
MySQL Integration
Java 8 Compatible

---

 🚀 How to Run

 1. Clone the project

```bash
git clone https://github.com/your-username/bankbenchers.git
cd bankbenchers

2. Configure Database
Edit src/main/resources/application.properties:

spring.datasource.url=jdbc:mysql://localhost:3306/loan_management
spring.datasource.username=root
spring.datasource.password=root
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true   

3. Curl Request for Token generation
curl -X POST http://localhost:8080/auth/login \
-H "Content-Type: application/json" \
-d '{"username":"user","password":"password"}'

Response:
{ "token": "<your_token_here>" }

4. Curl for Upload Beneficiaries
curl -X POST http://localhost:8080/upload/beneficiaries \
-H "Authorization: Bearer <token>" \
-F "file=@beneficiaries.csv"

5. Curl for Upload Loans
curl -X POST http://localhost:8080/upload/loans \
-H "Authorization: Bearer <token>" \
-F "file=@loans.csv"

6.Sample CSV Format

beneficiaries.csv

beneficiaryId,name,caste
001,Dhrumil Shah,GEN
002,Jay Jain,OBC

loans.csv
applicationId,beneficiaryId,bank_name,bank_type,account_no,ifsc_code,loan_amount,sanction_amount
A001,001,HDFC,Private,ABC1234XYZ,HDFC0001234,100000,90000



