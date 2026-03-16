package com.banco.app.domain.models;

import com.banco.app.domain.enums.UserRoles;
import com.banco.app.domain.enums.UserStatus;

import lombok.Getter;
import lombok.Setter;
//import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Getter
@Setter
//@NoArgsConstructor
public abstract class User {
    
    private String userId;
    private String relationId;
    private String fullName;
    private String identificationNumber;
    private String email;
    private String phone;
    private LocalDate birthDate;
    private String address;
    private UserRoles role;
    private UserStatus status;
    private String username;
    private String passwordHash;

    public User(String userId, String relationId, String fullName, String identificationNumber,
                String email, String phone, LocalDate birthDate,String address, UserRoles role, 
                String username, String passwordHash) {

        this.userId = userId;
        this.relationId = relationId;
        this.fullName = fullName;
        this.identificationNumber = identificationNumber;
        this.email = email;
        this.phone = phone;
        this.birthDate = birthDate;
        this.address = address;
        this.role = role;
        this.status = UserStatus.ACTIVE; 
        this.username = username;
        this.passwordHash = passwordHash;
    }

    @Override
    public String toString() {
        return "User{userId=" + userId +
               ", fullName='" + fullName +
               "', role=" + role +
               ", status=" + status + "}";
    }
}