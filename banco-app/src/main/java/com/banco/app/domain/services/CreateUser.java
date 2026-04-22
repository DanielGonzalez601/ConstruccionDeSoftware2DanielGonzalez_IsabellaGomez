package com.banco.app.domain.services;

import com.banco.app.domain.exceptions.BusinessException;
import com.banco.app.domain.enums.UserRoles;
import com.banco.app.domain.models.CompanyClient;
import com.banco.app.domain.models.User;
import com.banco.app.domain.ports.UserPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateUser {

    private final UserPort userPort;

    @Autowired
    public CreateUser(UserPort userPort) {
        this.userPort = userPort;
    }

    public void createUser(User user) throws BusinessException {

        // VALIDACIONES GENERICAS SOBRE EL USUARIO
        // El usuario es obligatorio.
        if (user == null) {
            throw new BusinessException("El usuario es obligatorio");
        }

        // El nombre completo es obligatorio.
        if (user.getfullName() == null || user.getfullName().trim().isEmpty()) {
            throw new BusinessException("El nombre del usuario es obligatorio");
        }

        // El username es obligatorio.
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            throw new BusinessException("El nombre de usuario es obligatorio");
        }

        // La contraseña es obligatoria.
        if (user.getpasswordHash() == null || user.getpasswordHash().trim().isEmpty()) {
            throw new BusinessException("La contraseña es obligatoria");
        }

        // La identificación del usuario es obligatoria.
        if (user.getIdentificationNumber() == null || user.getIdentificationNumber().trim().isEmpty()) {
            throw new BusinessException("La identificación del usuario es obligatoria");
        }

        // La identificación del usuario debe ser única.
        if (userPort.findByIdentificationNumber(user.getIdentificationNumber().trim()) != null) {
            throw new BusinessException("Ya existe un usuario con esa identificación");
        }

        // El correo es obligatorio y debe tener formato válido.
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new BusinessException("El correo del usuario es obligatorio");
        }

        if (!user.getEmail().contains("@") || user.getEmail().lastIndexOf('.') < user.getEmail().indexOf('@')) {
            throw new BusinessException("El correo del usuario no tiene un formato válido");
        }

        // El teléfono es obligatorio.
        if (user.getPhone() == null || user.getPhone().trim().isEmpty()) {
            throw new BusinessException("El teléfono del usuario es obligatorio");
        }
        
        // El teléfono debe tener entre 7 y 15 caracteres.
        if (user.getPhone().trim().length() < 7 || user.getPhone().trim().length() > 15) {
            throw new BusinessException("El teléfono debe tener entre 7 y 15 caracteres");
        }
        
        // El teléfono solo debe contener dígitos.
        if (!user.getPhone().trim().matches("\\d+")) {
            throw new BusinessException("El teléfono solo debe contener dígitos");
        }
        
        // La dirección es obligatoria.
        if (user.getAddress() == null || user.getAddress().trim().isEmpty()) {
            throw new BusinessException("La dirección del usuario es obligatoria");
        }
        
        // El rol del usuario es obligatorio.
        if (user.getRole() == null) {
            throw new BusinessException("El rol del usuario es obligatorio");
        }
        
        // El estado del usuario es obligatorio.
        if (user.getStatus() == null) {
            throw new BusinessException("El estado del usuario es obligatorio");
        }

        userPort.save(user);
    }

}