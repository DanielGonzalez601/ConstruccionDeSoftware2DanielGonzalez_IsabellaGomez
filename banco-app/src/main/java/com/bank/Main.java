package com.bank;

import com.bank.config.DatabaseManager;
import com.bank.util.ConsoleUI;
import com.bank.util.DataSeeder;

public class Main {
    public static void main(String[] args) {
        try {
            DatabaseManager.getInstance();

            DataSeeder seeder = new DataSeeder();
            seeder.seedIfEmpty();

            ConsoleUI ui = new ConsoleUI();
            ui.start();

        } catch (Exception e) {
            System.err.println("[ERROR] La aplicación no pudo iniciarse: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
