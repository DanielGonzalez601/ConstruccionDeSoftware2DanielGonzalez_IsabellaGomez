package com.bank.util;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class InputHelper {
    private static final Scanner scanner = new Scanner(System.in);
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static String readString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    public static String readRequiredString(String prompt) {
        while (true) {
            String value = readString(prompt);
            if (!value.isBlank()) return value;
            System.out.println("  [!] Este campo es obligatorio. Por favor inténtalo de nuevo.");
        }
    }

    public static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("  [!] Por favor ingresa un número entero válido.");
            }
        }
    }

    public static BigDecimal readDecimal(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                BigDecimal value = new BigDecimal(input);
                if (value.compareTo(BigDecimal.ZERO) > 0) return value;
                System.out.println("  [!] El valor debe ser mayor que cero.");
            } catch (NumberFormatException e) {
                System.out.println("  [!] Por favor ingresa un número válido (p. ej. 1000.00).");
            }
        }
    }

    public static LocalDate readDate(String prompt) {
        while (true) {
            System.out.print(prompt + " (DD/MM/YYYY): ");
            String input = scanner.nextLine().trim();
            try {
                return LocalDate.parse(input, DATE_FORMAT);
            } catch (DateTimeParseException e) {
                System.out.println("  [!] Formato de fecha inválido. Por favor usa DD/MM/YYYY.");
            }
        }
    }

    public static int readChoice(String prompt, int min, int max) {
        while (true) {
            int choice = readInt(prompt);
            if (choice >= min && choice <= max) return choice;
            System.out.printf("  [!] Por favor ingresa un número entre %d y %d.%n", min, max);
        }
    }

    public static void pause() {
        System.out.print("\n  Presione ENTER para continuar...");
        scanner.nextLine();
    }
}
