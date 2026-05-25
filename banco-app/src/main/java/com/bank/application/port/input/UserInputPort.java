package com.bank.application.port.input;

import com.bank.application.dto.UserDto.*;
import java.util.List;

/**
* PUERTO DE ENTRADA DE LA APLICACIÓN - UserInputPort
*
* Define las operaciones que los adaptadores externos (controladores REST, CLI, etc.)
* pueden invocar en la aplicación relacionada con los usuarios.
* Los controladores dependen de esta interfaz, NO de implementaciones de casos de uso concretos.
*/
public interface UserInputPort {
    LoginResponse login(LoginCommand command);
    UserResponse registerUser(RegisterUserCommand command);
    UserResponse getUserById(Long id);
    UserResponse getUserByIdentification(String identificationNumber);
    List<UserResponse> getAllUsers();
    UserResponse updateUserStatus(Long userId, UpdateUserStatusCommand command);
}
