package org.example;
import javax.swing.*;


public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::run);
    }

    private static void run() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Crear la base de datos
        DatabaseManager db = new DatabaseManager();

        // Crear la vista de login
        LoginView loginView = new LoginView();

        // Crear el controlador de login
        new LoginController(loginView, db);

        // Mostrar login
        loginView.setVisible(true);
    }
}
