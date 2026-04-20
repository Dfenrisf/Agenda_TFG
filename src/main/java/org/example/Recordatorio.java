package org.example;

import java.time.LocalDate;
import java.time.LocalTime;

public class Recordatorio {
    private int id; // ID del registro en BD
    private int usuarioId; // Quién creó este recordatorio
    private LocalDate fecha;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private String descripcion;

    public Recordatorio(int id, int usuarioId, LocalDate fecha, LocalTime horaInicio, 
                       LocalTime horaFin, String descripcion) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.descripcion = descripcion;
    }

    // Constructor para crear nuevos recordatorios (sin ID aún)
    public Recordatorio(LocalDate fecha, LocalTime horaInicio, LocalTime horaFin, String descripcion) {
        this(-1, -1, fecha, horaInicio, horaFin, descripcion);
    }

    public int getId() { return id; }
    public int getUsuarioId() { return usuarioId; }
    public LocalDate getFecha() { return fecha; }
    public LocalTime getHoraInicio() { return horaInicio; }
    public LocalTime getHoraFin() { return horaFin; }
    public String getDescripcion() { return descripcion; }

    @Override
    public String toString() {
        return fecha + " | " + horaInicio + "-" + horaFin + " | " + descripcion;
    }
}
