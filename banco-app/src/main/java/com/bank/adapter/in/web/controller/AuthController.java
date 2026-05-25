package com.bank.adapter.in.web.controller;

import com.bank.application.dto.BankingDto.ApiResponse;
import com.bank.application.dto.UserDto.*;
import com.bank.application.port.input.UserInputPort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
* ADAPTADOR (Controlador) - AuthController
*
* Punto de entrada REST para la autenticación.
* Llama al UserInputPort (caso de uso de la aplicación).
* No contiene lógica de negocio; solo traducción HTTP.
*/
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Inicio de sesión y registro de usuario.")
public class AuthController {

    private final UserInputPort userInputPort;

    public AuthController(UserInputPort userInputPort) {
        this.userInputPort = userInputPort;
    }

    @PostMapping("/login")
    @Operation(summary = "Login", description = "Autentícate con tu número de identificación y contraseña. Devuelve un token JWT.")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginCommand command) {
        LoginResponse response = userInputPort.login(command);
        return ResponseEntity.ok(ApiResponse.ok("Inicio de sesión exitoso", response));
    }

    @PostMapping("/register")
    @Operation(summary = "Register User", description = "Registra un nuevo usuario. Roles: CLIENTE_INDIVIDUAL, CLIENTE_COMPAÑÍA, EMPLEADO_COMPAÑÍA, SUPERVISOR_COMPAÑÍA")
    public ResponseEntity<ApiResponse<UserResponse>> register(@Valid @RequestBody RegisterUserCommand command) {
        UserResponse response = userInputPort.registerUser(command);
        return ResponseEntity.status(201).body(ApiResponse.ok("Usuario registrado exitosamente", response));
    }
}
