package org.example;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class DatabaseManager {
    private static final String DB_NAME = "agenda.db";
    private Connection connection;

    public DatabaseManager() {
        try {
            // Cargar el driver SQLite
            Class.forName("org.sqlite.JDBC");
            // Crear conexión (si no existe, crea la BD)
            connection = DriverManager.getConnection("jdbc:sqlite:" + DB_NAME);
            crearTablas();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Crea las tablas si no existen
     */
    private void crearTablas() {
        String sqlUsuarios = "CREATE TABLE IF NOT EXISTS usuarios (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nombre TEXT UNIQUE NOT NULL," +
                "contraseña TEXT NOT NULL," +
                "esAdmin INTEGER NOT NULL DEFAULT 0)";

        String sqlRecordatorios = "CREATE TABLE IF NOT EXISTS recordatorios (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "usuario_id INTEGER NOT NULL," +
                "fecha TEXT NOT NULL," +
                "hora_inicio TEXT NOT NULL," +
                "hora_fin TEXT NOT NULL," +
                "descripcion TEXT NOT NULL," +
                "FOREIGN KEY(usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE)";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sqlUsuarios);
            stmt.execute(sqlRecordatorios);
            
            // Insertar usuario admin por defecto si no existe
            String checkAdmin = "SELECT COUNT(*) FROM usuarios WHERE nombre = 'admin'";
            ResultSet rs = stmt.executeQuery(checkAdmin);
            if (rs.next() && rs.getInt(1) == 0) {
                registrarUsuario("admin", "admin123", true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Registra un nuevo usuario
     */
    public boolean registrarUsuario(String nombre, String contraseña, boolean esAdmin) {
        String sql = "INSERT INTO usuarios (nombre, contraseña, esAdmin) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, nombre);
            pstmt.setString(2, contraseña);
            pstmt.setInt(3, esAdmin ? 1 : 0);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Valida el login de un usuario
     * Retorna null si no existe, o el Usuario si es válido
     */
    public Usuario validarLogin(String nombre, String contraseña) {
        String sql = "SELECT id, nombre, esAdmin FROM usuarios WHERE nombre = ? AND contraseña = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, nombre);
            pstmt.setString(2, contraseña);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                int id = rs.getInt("id");
                boolean esAdmin = rs.getInt("esAdmin") == 1;
                return new Usuario(id, nombre, esAdmin);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Agrega un recordatorio a un usuario específico
     */
    public boolean agregarRecordatorio(int usuarioId, LocalDate fecha, LocalTime horaInicio, 
                                       LocalTime horaFin, String descripcion) {
        String sql = "INSERT INTO recordatorios (usuario_id, fecha, hora_inicio, hora_fin, descripcion) " +
                     "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, usuarioId);
            pstmt.setString(2, fecha.toString());
            pstmt.setString(3, horaInicio.toString());
            pstmt.setString(4, horaFin.toString());
            pstmt.setString(5, descripcion);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Obtiene todos los recordatorios de un usuario
     */
    public Map<LocalDate, List<Recordatorio>> obtenerRecordatorios(int usuarioId) {
        Map<LocalDate, List<Recordatorio>> recordatorios = new HashMap<>();
        String sql = "SELECT id, fecha, hora_inicio, hora_fin, descripcion FROM recordatorios " +
                     "WHERE usuario_id = ? ORDER BY fecha";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, usuarioId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                int id = rs.getInt("id");
                LocalDate fecha = LocalDate.parse(rs.getString("fecha"));
                LocalTime horaInicio = LocalTime.parse(rs.getString("hora_inicio"));
                LocalTime horaFin = LocalTime.parse(rs.getString("hora_fin"));
                String descripcion = rs.getString("descripcion");
                
                Recordatorio r = new Recordatorio(id, usuarioId, fecha, horaInicio, horaFin, descripcion);
                recordatorios.computeIfAbsent(fecha, k -> new ArrayList<>()).add(r);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return recordatorios;
    }

    /**
     * Elimina un recordatorio
     */
    public boolean eliminarRecordatorio(int recordatorioId, int usuarioId) {
        String sql = "DELETE FROM recordatorios WHERE id = ? AND usuario_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, recordatorioId);
            pstmt.setInt(2, usuarioId);
            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Obtiene TODOS los recordatorios (solo para admin)
     */
    public Map<LocalDate, List<Recordatorio>> obtenerTodosRecordatorios() {
        Map<LocalDate, List<Recordatorio>> recordatorios = new HashMap<>();
        String sql = "SELECT id, usuario_id, fecha, hora_inicio, hora_fin, descripcion FROM recordatorios " +
                     "ORDER BY fecha";
        
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                int id = rs.getInt("id");
                int usuarioId = rs.getInt("usuario_id");
                LocalDate fecha = LocalDate.parse(rs.getString("fecha"));
                LocalTime horaInicio = LocalTime.parse(rs.getString("hora_inicio"));
                LocalTime horaFin = LocalTime.parse(rs.getString("hora_fin"));
                String descripcion = rs.getString("descripcion");
                
                Recordatorio r = new Recordatorio(id, usuarioId, fecha, horaInicio, horaFin, descripcion);
                recordatorios.computeIfAbsent(fecha, k -> new ArrayList<>()).add(r);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return recordatorios;
    }

    /**
     * Cierra la conexión
     */
    public void cerrar() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
