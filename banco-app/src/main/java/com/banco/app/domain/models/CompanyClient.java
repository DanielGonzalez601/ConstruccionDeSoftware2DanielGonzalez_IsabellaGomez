package com.banco.app.domain.models;

import com.banco.app.domain.enums.UserRoles;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyClient extends User {

    private String companyName;
    private String taxIdNumber;
    private String legalRepresentativeId;

    public CompanyClient(String userId, String relationId, String companyName, String taxIdNumber,
                         String email, String phone, String address,
                         String legalRepresentativeId, String username, String passwordHash) {

        super(userId, relationId, companyName, taxIdNumber, email, phone,
              null, address, UserRoles.COMPANY_CLIENT, username, passwordHash);

        this.companyName = companyName;
        this.taxIdNumber = taxIdNumber;
        this.legalRepresentativeId = legalRepresentativeId;
    }

    @Override
    public String toString() {
        return "CompanyClient{userId='" + getUserId() +
               "', companyName='" + companyName +
               "', taxIdNumber='" + taxIdNumber + "'}";
    }
}