package org.example;

import org.example.DatabaseManager;
import org.example.Recordatorio;
import org.example.Usuario;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AgendaModel {
    private DatabaseManager db;
    private Usuario usuarioActual;
    private Map<LocalDate, List<Recordatorio>> recordatorios;
    private YearMonth mesActual;

    public AgendaModel(DatabaseManager db, Usuario usuario) {
        this.db = db;
        this.usuarioActual = usuario;
        this.recordatorios = new HashMap<>();
        this.mesActual = YearMonth.now();
        cargarRecordatorios();
    }

    /**
     * Carga los recordatorios desde la BD
     * TODOS los usuarios ven TODOS los recordatorios
     * Pero solo pueden eliminar los suyos
     */
    private void cargarRecordatorios() {
        this.recordatorios = db.obtenerTodosRecordatorios();
    }

    /**
     * Solo los ADMINS pueden agregar recordatorios
     */
    public boolean agregarRecordatorio(Recordatorio r) {
        if (!usuarioActual.isAdmin()) {
            return false; // Los usuarios normales no pueden crear
        }

        if (db.agregarRecordatorio(usuarioActual.getId(), r.getFecha(), 
                                   r.getHoraInicio(), r.getHoraFin(), r.getDescripcion())) {
            cargarRecordatorios(); // Recargar desde BD
            return true;
        }
        return false;
    }

    /**
     * Usuarios y admins pueden eliminar recordatorios
     * Pero los usuarios solo pueden eliminar los suyos
     */
    public boolean eliminarRecordatorio(Recordatorio r) {
        // Si es usuario normal, solo puede eliminar los suyos
        if (!usuarioActual.isAdmin() && r.getUsuarioId() != usuarioActual.getId()) {
            return false;
        }

        if (db.eliminarRecordatorio(r.getId(), usuarioActual.getId())) {
            cargarRecordatorios(); // Recargar desde BD
            return true;
        }
        return false;
    }

    public Map<LocalDate, List<Recordatorio>> getRecordatorios() {
        return recordatorios;
    }

    public YearMonth getMesActual() {
        return mesActual;
    }

    public Usuario getUsuarioActual() {
        return usuarioActual;
    }

    public void recargarRecordatorios() {
        cargarRecordatorios();
    }
}
