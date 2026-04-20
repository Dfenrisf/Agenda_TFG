package org.example;

import org.example.*;

public class LoginController {
    private LoginView loginView;
    private DatabaseManager db;

    public LoginController(LoginView loginView, DatabaseManager db) {
        this.loginView = loginView;
        this.db = db;

        // Listeners
        this.loginView.addLoginListener(e -> procesarLogin());
        this.loginView.addRegistroListener(e -> procesarRegistro());
    }

    private void procesarLogin() {
        String nombre = loginView.getNombre();
        String contraseña = loginView.getContraseña();

        if (nombre.isEmpty() || contraseña.isEmpty()) {
            loginView.setError("Por favor completa todos los campos");
            return;
        }

        Usuario usuario = db.validarLogin(nombre, contraseña);
        
        if (usuario != null) {
            loginView.setError("");
            // Login exitoso
            abrirAgenda(usuario);
        } else {
            loginView.setError("Usuario o contraseña incorrectos");
        }
    }

    private void procesarRegistro() {
        String nombre = loginView.getNombre();
        String contraseña = loginView.getContraseña();

        if (nombre.isEmpty() || contraseña.isEmpty()) {
            loginView.setError("Por favor completa todos los campos");
            return;
        }

        if (contraseña.length() < 4) {
            loginView.setError("La contraseña debe tener al menos 4 caracteres");
            return;
        }

        if (db.registrarUsuario(nombre, contraseña, false)) {
            loginView.setError("");
            loginView.mostrarMensaje("¡Usuario registrado correctamente!");
            loginView.limpiar();
        } else {
            loginView.setError("Este usuario ya existe");
        }
    }

    private void abrirAgenda(Usuario usuario) {
        // Cerramos login
        loginView.dispose();

        // Abrimos la agenda
        AgendaModel modelo = new AgendaModel(db, usuario);
        AgendaView vista = new AgendaView(usuario);
        new AgendaController(modelo, vista, db);

        vista.setVisible(true);
    }
}
