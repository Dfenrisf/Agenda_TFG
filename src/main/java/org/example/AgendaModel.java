package org.example;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
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
     * TODOS pueden eliminar CUALQUIER recordatorio
     * (solo admin puede crear, pero cualquiera puede eliminar)
     */
    public boolean eliminarRecordatorio(Recordatorio r) {
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
