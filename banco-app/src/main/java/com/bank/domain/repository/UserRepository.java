package com.bank.domain.repository;

import com.bank.domain.model.entity.User;
import com.bank.domain.model.valueobject.UserRole;

import java.util.List;
import java.util.Optional;

/**
* INTERFAZ DE REPOSITORIO DE DOMINIO (Output Port at Domain Level)
*
* Define las operaciones de persistencia que necesita el dominio.
* La implementación reside en la capa de infraestructura.
* Esta interfaz NO depende de JPA, SQL ni de ningún otro framework.
*/
public interface UserRepository {

    User save(User user);

    Optional<User> findById(Long id);

    Optional<User> findByIdentificationNumber(String identificationNumber);

    boolean existsByIdentificationNumber(String identificationNumber);

    List<User> findAll();

    List<User> findByCompanyId(String companyId);

    List<User> findByRole(UserRole role);
}
