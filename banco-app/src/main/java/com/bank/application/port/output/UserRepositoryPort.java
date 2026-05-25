package com.bank.application.port.output;

import com.bank.domain.model.entity.User;
import com.bank.domain.model.valueobject.UserRole;
import java.util.List;
import java.util.Optional;

/**
* PUERTO DE SALIDA DE LA APLICACIÓN - UserRepositoryPort
*
* La capa de aplicación se comunica con la persistencia a través de esta interfaz.
* La capa de infraestructura proporciona la implementación concreta (adaptador JPA).
* Esto mantiene la aplicación independiente de la tecnología de base de datos.
*/
public interface UserRepositoryPort {
    User save(User user);
    Optional<User> findById(Long id);
    Optional<User> findByIdentificationNumber(String identificationNumber);
    boolean existsByIdentificationNumber(String identificationNumber);
    List<User> findAll();
    List<User> findByCompanyId(String companyId);
    List<User> findByRole(UserRole role);
}
