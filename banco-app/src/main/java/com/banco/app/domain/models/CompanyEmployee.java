package com.banco.app.domain.models;

import com.banco.app.domain.enums.UserRoles;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CompanyEmployee extends User {

    public CompanyEmployee(String userId, String relationId, String fullName, String identificationNumber,
                           String email, String phone, LocalDate birthDate,
                           String address, String username, String passwordHash) {

        super(userId, relationId, fullName, identificationNumber, email, phone,
              birthDate, address, UserRoles.COMPANY_EMPLOYEE, username, passwordHash);
    }

    @Override
    public String toString() {
        return "CompanyEmployee{userId='" + getUserId() +
               "', fullName='" + getFullName() + "'}";
    }
}