package com.bank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
* SISTEMA DE GESTIÓN BANCARIA
* Arquitectura: Hexagonal (Puertos y Adaptadores) + DDD
*
* Capas:
* domain/ → Lógica de negocio pura, sin dependencias de frameworks
* application/ → Casos de uso, Puertos de entrada/salida, DTOs
* adapter/in/web/ → Controladores REST (Adaptadores)
* adapter/out/ → Persistencia JPA (Adaptadores controlados)
* config/ → Conexión Spring, Seguridad, Manejo de excepciones
*/
@SpringBootApplication
@EnableScheduling
public class BankApplication {
    public static void main(String[] args) {
        SpringApplication.run(BankApplication.class, args);
    }
}
