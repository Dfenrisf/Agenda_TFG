package org.example;

import org.example.AgendaModel;
import org.example.AgendaView;
import org.example.DatabaseManager;
import org.example.Recordatorio;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class AgendaController {
    private AgendaModel modelo;
    private AgendaView vista;
    private DatabaseManager db;

    public AgendaController(AgendaModel modelo, AgendaView vista, DatabaseManager db) {
        this.modelo = modelo;
        this.vista = vista;
        this.db = db;

        // Listeners
        this.vista.addRecordatorioListener(e -> abrirDialogoNuevo());
        this.vista.addEliminarListener(e -> abrirDialogoEliminar());
        this.vista.addVerListener(e -> abrirDialogoVer());

        // Dibujar calendario inicial
        actualizarVista();
    }

    private void actualizarVista() {
        vista.dibujarCalendario(modelo.getMesActual(), modelo.getRecordatorios());
    }

    /**
     * Abre diálogo para crear nuevo recordatorio (SOLO ADMINS)
     */
    private void abrirDialogoNuevo() {
        if (!modelo.getUsuarioActual().isAdmin()) {
            vista.mostrarMensaje("Solo los administradores pueden crear recordatorios");
            return;
        }

        JDialog dialog = new JDialog(vista, "➕ Nuevo Recordatorio", true);
        dialog.setSize(450, 300);
        dialog.setLocationRelativeTo(vista);
        dialog.setLayout(new GridLayout(5, 2, 15, 15));
        dialog.getRootPane().setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        dialog.add(new JLabel("Día del mes:"));
        JSpinner spinDia = new JSpinner(new SpinnerNumberModel(1, 1, modelo.getMesActual().lengthOfMonth(), 1));
        dialog.add(spinDia);

        dialog.add(new JLabel("Hora Inicio (H:M):"));
        JPanel panelInicio = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        JSpinner spinHoraIni = new JSpinner(new SpinnerNumberModel(9, 0, 23, 1));
        JSpinner spinMinIni = new JSpinner(new SpinnerNumberModel(0, 0, 59, 5));
        panelInicio.add(spinHoraIni);
        panelInicio.add(new JLabel(" : "));
        panelInicio.add(spinMinIni);
        dialog.add(panelInicio);

        dialog.add(new JLabel("Hora Fin (H:M):"));
        JPanel panelFin = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        JSpinner spinHoraFin = new JSpinner(new SpinnerNumberModel(10, 0, 23, 1));
        JSpinner spinMinFin = new JSpinner(new SpinnerNumberModel(0, 0, 59, 5));
        panelFin.add(spinHoraFin);
        panelFin.add(new JLabel(" : "));
        panelFin.add(spinMinFin);
        dialog.add(panelFin);

        dialog.add(new JLabel("Descripción:"));
        JTextField txtDescripcion = new JTextField();
        dialog.add(txtDescripcion);

        JButton btnGuardar = new JButton("💾 Guardar");
        btnGuardar.addActionListener(e -> {
            String desc = txtDescripcion.getText().trim();
            if (desc.isEmpty()) {
                vista.mostrarMensaje("La descripción no puede estar vacía.");
                return;
            }

            LocalDate fecha = modelo.getMesActual().atDay((Integer) spinDia.getValue());
            LocalTime horaInicio = LocalTime.of((Integer) spinHoraIni.getValue(), (Integer) spinMinIni.getValue());
            LocalTime horaFin = LocalTime.of((Integer) spinHoraFin.getValue(), (Integer) spinMinFin.getValue());

            if (horaFin.isBefore(horaInicio)) {
                vista.mostrarMensaje("La hora de fin debe ser posterior a la de inicio.");
                return;
            }

            Recordatorio nuevoRecordatorio = new Recordatorio(fecha, horaInicio, horaFin, desc);
            
            if (modelo.agregarRecordatorio(nuevoRecordatorio)) {
                vista.mostrarMensaje("✅ Recordatorio creado correctamente");
                actualizarVista();
                dialog.dispose();
            } else {
                vista.mostrarMensaje("❌ Error al crear el recordatorio");
            }
        });

        dialog.add(new JLabel(""));
        dialog.add(btnGuardar);
        dialog.setVisible(true);
    }

    /**
     * Abre diálogo para eliminar recordatorio
     */
    private void abrirDialogoEliminar() {
        if (modelo.getRecordatorios().isEmpty()) {
            vista.mostrarMensaje("No hay recordatorios para eliminar.");
            return;
        }

        JDialog dialog = new JDialog(vista, "❌ Eliminar Recordatorio", true);
        dialog.setSize(500, 200);
        dialog.setLocationRelativeTo(vista);
        dialog.setLayout(new GridLayout(2, 1, 15, 15));
        dialog.getRootPane().setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblInfo = new JLabel("Selecciona la tarea a eliminar:");
        lblInfo.setFont(new Font("Segoe UI", Font.BOLD, 12));
        dialog.add(lblInfo);

        JComboBox<String> combo = new JComboBox<>();

        // Obtener todos los recordatorios disponibles
        List<Recordatorio> recordatoriosList = new ArrayList<>();
        for (List<Recordatorio> listaDia : modelo.getRecordatorios().values()) {
            for (Recordatorio r : listaDia) {
                recordatoriosList.add(r);
                
                String icono = r.getUsuarioId() == modelo.getUsuarioActual().getId() ? "👤" : "👑";
                String canEliminar = (modelo.getUsuarioActual().isAdmin() || r.getUsuarioId() == modelo.getUsuarioActual().getId()) 
                                     ? "" : " (No puedes eliminar)";
                
                String display = icono + " " + r.getFecha() + " | " + r.getHoraInicio() + 
                                "-" + r.getHoraFin() + " | " + r.getDescripcion() + canEliminar;
                combo.addItem(display);
            }
        }

        dialog.add(combo);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        JButton btnBorrar = new JButton("🗑️ Eliminar");
        JButton btnCancelar = new JButton("❌ Cancelar");
        
        btnBorrar.addActionListener(e -> {
            int selectedIndex = combo.getSelectedIndex();
            if (selectedIndex >= 0 && selectedIndex < recordatoriosList.size()) {
                Recordatorio seleccionado = recordatoriosList.get(selectedIndex);
                
                if (!modelo.getUsuarioActual().isAdmin() && seleccionado.getUsuarioId() != modelo.getUsuarioActual().getId()) {
                    vista.mostrarMensaje("❌ No puedes eliminar recordatorios de otros usuarios");
                    return;
                }
                
                if (modelo.eliminarRecordatorio(seleccionado)) {
                    vista.mostrarMensaje("✅ Recordatorio eliminado correctamente");
                    actualizarVista();
                    dialog.dispose();
                } else {
                    vista.mostrarMensaje("❌ Error al eliminar el recordatorio");
                }
            }
        });

        btnCancelar.addActionListener(e -> dialog.dispose());
        
        panelBotones.add(btnBorrar);
        panelBotones.add(btnCancelar);
        
        dialog.add(panelBotones);
        dialog.setVisible(true);
    }

    /**
     * Abre diálogo para ver detalles de recordatorios
     */
    private void abrirDialogoVer() {
        if (modelo.getRecordatorios().isEmpty()) {
            vista.mostrarMensaje("No hay recordatorios.");
            return;
        }

        JDialog dialog = new JDialog(vista, "👁️ Ver Recordatorios", true);
        dialog.setSize(700, 500);
        dialog.setLocationRelativeTo(vista);
        dialog.setLayout(new BorderLayout());

        // Panel superior con filtros
        JPanel panelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelFiltros.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JComboBox<String> filtroMes = new JComboBox<>();
        filtroMes.addItem("📅 Todos los meses");
        for (LocalDate fecha : modelo.getRecordatorios().keySet()) {
            String mesAño = String.format("%s %d", getNombreMes(fecha), fecha.getYear());
            if (filtroMes.getItemCount() == 1 || !mesAño.equals(filtroMes.getItemAt(filtroMes.getItemCount() - 1))) {
                filtroMes.addItem(mesAño);
            }
        }

        panelFiltros.add(new JLabel("Filtrar:"));
        panelFiltros.add(filtroMes);
        dialog.add(panelFiltros, BorderLayout.NORTH);

        // Tabla para mostrar recordatorios
        String[] columnNames = {"📅 Fecha", "⏰ Hora Inicio", "⏰ Hora Fin", "📝 Descripción", "👤 Creador"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(tableModel);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        table.setRowHeight(28);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 11));
        table.getTableHeader().setBackground(new Color(52, 152, 219));
        table.getTableHeader().setForeground(Color.WHITE);

        // Cargar datos iniciales
        actualizarTabla(tableModel, modelo.getRecordatorios(), "Todos los meses");

        // Listener para filtro
        filtroMes.addActionListener(e -> {
            String selectedMes = (String) filtroMes.getSelectedItem();
            tableModel.setRowCount(0);
            actualizarTabla(tableModel, modelo.getRecordatorios(), selectedMes);
        });

        JScrollPane scrollPane = new JScrollPane(table);
        dialog.add(scrollPane, BorderLayout.CENTER);

        // Panel inferior
        JPanel panelInferior = new JPanel();
        JButton btnCerrar = new JButton("Cerrar");
        panelInferior.add(btnCerrar);
        btnCerrar.addActionListener(e -> dialog.dispose());
        dialog.add(panelInferior, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    private void actualizarTabla(DefaultTableModel tableModel, java.util.Map<LocalDate, List<Recordatorio>> recordatorios, String filtro) {
        for (LocalDate fecha : recordatorios.keySet()) {
            // Aplicar filtro
            if (!filtro.equals("Todos los meses")) {
                String mesAño = String.format("%s %d", getNombreMes(fecha), fecha.getYear());
                if (!mesAño.equals(filtro)) continue;
            }

            for (Recordatorio r : recordatorios.get(fecha)) {
                // Si es usuario normal, solo mostrar los suyos
                if (!modelo.getUsuarioActual().isAdmin() && r.getUsuarioId() != modelo.getUsuarioActual().getId()) {
                    // Los usuarios normales ven TODAS las tareas pero las de admin tienen otro icono
                }

                String creador = r.getUsuarioId() == modelo.getUsuarioActual().getId() ? 
                                "👤 Tú" : "👑 Admin";

                tableModel.addRow(new Object[]{
                    "📅 " + r.getFecha(),
                    r.getHoraInicio(),
                    r.getHoraFin(),
                    r.getDescripcion(),
                    creador
                });
            }
        }
    }

    private String getNombreMes(LocalDate fecha) {
        String[] meses = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", 
                         "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
        return meses[fecha.getMonthValue() - 1];
    }
}
