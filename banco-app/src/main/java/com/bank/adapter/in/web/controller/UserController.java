package com.bank.adapter.in.web.controller;

import com.bank.application.dto.BankingDto.ApiResponse;
import com.bank.application.dto.UserDto.*;
import com.bank.application.port.input.UserInputPort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
* ADAPTADOR (Controlador) - UserController
*
* Controlador REST para operaciones de gestión de usuarios.
* Requiere autenticación JWT.
*/
@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "Gestión de usuarios")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private final UserInputPort userInputPort;

    public UserController(UserInputPort userInputPort) {
        this.userInputPort = userInputPort;
    }

    @GetMapping
    @Operation(summary = "Obtener todos los usuarios", description = "INTERNAL_ANALYST only")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() {
        return ResponseEntity.ok(ApiResponse.ok("Usuarios encontrados.", userInputPort.getAllUsers()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener usuario por ID")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok("Usuario encontrado.", userInputPort.getUserById(id)));
    }

    @GetMapping("/by-identification/{identificationNumber}")
    @Operation(summary = "Obtener usuario por número de identificación")
    public ResponseEntity<ApiResponse<UserResponse>> getByIdentification(@PathVariable String identificationNumber) {
        return ResponseEntity.ok(ApiResponse.ok("Usuario encontrado.", userInputPort.getUserByIdentification(identificationNumber)));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Actualizar estado del usuario", description = "Solo para los INTERNAL_ANALYST. Estados: ACTIVE, INACTIVE, BLOCKED")
    public ResponseEntity<ApiResponse<UserResponse>> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserStatusCommand command) {
        return ResponseEntity.ok(ApiResponse.ok("Estado del usuario actualizado.", userInputPort.updateUserStatus(id, command)));
    }
}
