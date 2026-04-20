package org.example;

public class Usuario {
    private int id;
    private String nombre;
    private boolean esAdmin;

    public Usuario(int id, String nombre, boolean esAdmin) {
        this.id = id;
        this.nombre = nombre;
        this.esAdmin = esAdmin;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public boolean isAdmin() {
        return esAdmin;
    }

    @Override
    public String toString() {
        return nombre + (esAdmin ? " (Admin)" : " (Usuario)");
    }
}
