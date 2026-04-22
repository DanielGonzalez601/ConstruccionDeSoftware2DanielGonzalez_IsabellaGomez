package com.banco.app.domain.ports;

import com.banco.app.domain.models.User;

public interface UserPort {
    User findByIdentificationNumber(String identificationNumber);
    void save(User user);
}
