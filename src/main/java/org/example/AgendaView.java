package org.example;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class AgendaView extends JFrame {
    private JPanel panelCalendario;
    private JLabel lblMesAnio;
    private JLabel lblUsuario;
    private JButton btnRecordatorio;
    private JButton btnEliminar;
    private JButton btnVer;
    private Usuario usuarioActual;
    private JLabel lblTotalTareas;

    public AgendaView(Usuario usuario) {
        this.usuarioActual = usuario;
        setTitle("📅 Agenda Profesional");
        setSize(950, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(0, 0));
        setBackground(new Color(245, 247, 252));

        inicializarUI();
    }

    private void inicializarUI() {
        // Panel superior con color azul sólido
        JPanel panelSuperior = new JPanel();
        panelSuperior.setBackground(new Color(41, 128, 185));
        panelSuperior.setLayout(new BorderLayout(20, 0));
        panelSuperior.setBorder(new EmptyBorder(20, 30, 20, 30));
        panelSuperior.setPreferredSize(new Dimension(0, 120));

        // Título principal
        JPanel panelTitulo = new JPanel();
        panelTitulo.setOpaque(false);
        panelTitulo.setLayout(new BoxLayout(panelTitulo, BoxLayout.Y_AXIS));
        
        JLabel lblTitulo = new JLabel("📅 Agenda Profesional");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblTitulo.setForeground(Color.WHITE);
        
        lblMesAnio = new JLabel("Enero 2025");
        lblMesAnio.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblMesAnio.setForeground(new Color(240, 248, 255));
        
        panelTitulo.add(lblTitulo);
        panelTitulo.add(Box.createVerticalStrut(8));
        panelTitulo.add(lblMesAnio);
        
        panelSuperior.add(panelTitulo, BorderLayout.WEST);

        // Panel usuario info (derecha)
        JPanel panelInfoUsuario = new JPanel();
        panelInfoUsuario.setOpaque(false);
        panelInfoUsuario.setLayout(new BoxLayout(panelInfoUsuario, BoxLayout.Y_AXIS));
        panelInfoUsuario.setAlignmentY(Component.CENTER_ALIGNMENT);
        
        String iconoUsuario = usuarioActual.isAdmin() ? "👑" : "👤";
        lblUsuario = new JLabel(iconoUsuario + " " + usuarioActual.getNombre());
        lblUsuario.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblUsuario.setForeground(Color.WHITE);
        
        String tipoUsuario = usuarioActual.isAdmin() ? "Administrador" : "Usuario";
        JLabel lblTipo = new JLabel(tipoUsuario);
        lblTipo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblTipo.setForeground(new Color(200, 220, 255));
        
        lblTotalTareas = new JLabel("0 tareas");
        lblTotalTareas.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblTotalTareas.setForeground(new Color(200, 220, 255));
        
        panelInfoUsuario.add(lblUsuario);
        panelInfoUsuario.add(lblTipo);
        panelInfoUsuario.add(Box.createVerticalStrut(4));
        panelInfoUsuario.add(lblTotalTareas);
        
        panelSuperior.add(panelInfoUsuario, BorderLayout.EAST);
        add(panelSuperior, BorderLayout.NORTH);

        // Panel calendario central
        JPanel panelCentral = new JPanel();
        panelCentral.setBackground(new Color(245, 247, 252));
        panelCentral.setBorder(new EmptyBorder(20, 30, 20, 30));
        panelCentral.setLayout(new BorderLayout());

        // Contenedor del calendario
        panelCalendario = new JPanel();
        panelCalendario.setBackground(Color.WHITE);
        panelCalendario.setBorder(new EmptyBorder(20, 20, 20, 20));
        panelCalendario.setOpaque(true);

        JScrollPane scrollCalendario = new JScrollPane(panelCalendario, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollCalendario.setBorder(BorderFactory.createLineBorder(new Color(200, 210, 220), 1));
        scrollCalendario.getViewport().setBackground(Color.WHITE);

        panelCentral.add(scrollCalendario, BorderLayout.CENTER);
        add(panelCentral, BorderLayout.CENTER);

        // Panel inferior con botones
        JPanel panelInferior = new JPanel();
        panelInferior.setBackground(new Color(245, 247, 252));
        panelInferior.setBorder(new EmptyBorder(15, 30, 20, 30));
        panelInferior.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 0));

        btnRecordatorio = crearBoton("➕ Nuevo Recordatorio", new Color(46, 204, 113));
        btnVer = crearBoton("👁️ Ver Detalles", new Color(52, 152, 219));
        btnEliminar = crearBoton("❌ Eliminar", new Color(231, 76, 60));

        // Si es usuario normal, deshabilitamos el botón de nuevo recordatorio
        if (!usuarioActual.isAdmin()) {
            btnRecordatorio.setEnabled(false);
            btnRecordatorio.setBackground(new Color(180, 180, 180));
            btnRecordatorio.setForeground(new Color(120, 120, 120));
        }

        panelInferior.add(btnRecordatorio);
        panelInferior.add(btnVer);
        panelInferior.add(btnEliminar);
        add(panelInferior, BorderLayout.SOUTH);
    }

    private JButton crearBoton(String texto, Color color) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setForeground(Color.WHITE);
        btn.setPreferredSize(new Dimension(180, 40));
        btn.setBackground(color);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        return btn;
    }

    public void dibujarCalendario(YearMonth mesActual, Map<LocalDate, List<Recordatorio>> recordatorios) {
        panelCalendario.removeAll();
        panelCalendario.setLayout(new GridLayout(0, 7, 8, 8));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        String mesCapitalizado = mesActual.format(formatter);
        lblMesAnio.setText(mesCapitalizado.substring(0, 1).toUpperCase() + mesCapitalizado.substring(1));

        // Actualizar contador de tareas
        int totalTareas = recordatorios.values().stream().mapToInt(List::size).sum();
        lblTotalTareas.setText(totalTareas + " " + (totalTareas == 1 ? "tarea" : "tareas"));

        String[] diasSemana = {"Lun", "Mar", "Mié", "Jue", "Vie", "Sáb", "Dom"};
        for (String dia : diasSemana) {
            JLabel lblDia = new JLabel(dia, SwingConstants.CENTER);
            lblDia.setFont(new Font("Segoe UI", Font.BOLD, 13));
            lblDia.setForeground(new Color(52, 152, 219));
            lblDia.setOpaque(true);
            lblDia.setBackground(new Color(230, 240, 250));
            lblDia.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));
            panelCalendario.add(lblDia);
        }

        LocalDate primerDia = mesActual.atDay(1);
        for (int i = 1; i < primerDia.getDayOfWeek().getValue(); i++) {
            panelCalendario.add(new JLabel(""));
        }

        for (int dia = 1; dia <= mesActual.lengthOfMonth(); dia++) {
            LocalDate fecha = mesActual.atDay(dia);
            JButton btnDia = new JButton(String.valueOf(dia));

            btnDia.setFont(new Font("Segoe UI", Font.BOLD, 14));
            btnDia.setPreferredSize(new Dimension(0, 70));
            btnDia.setBorderPainted(true);
            btnDia.setFocusPainted(false);
            btnDia.setOpaque(true);

            if (recordatorios.containsKey(fecha) && !recordatorios.get(fecha).isEmpty()) {
                btnDia.setBackground(new Color(52, 152, 219));
                btnDia.setForeground(Color.WHITE);

                StringBuilder tooltipHTML = new StringBuilder("<html><b>📋 Tareas del día:</b><br>");
                for (Recordatorio r : recordatorios.get(fecha)) {
                    String icono = r.getUsuarioId() == usuarioActual.getId() ? "👤" : "👑";
                    tooltipHTML.append(icono).append(" ").append(r.getHoraInicio()).append(" - ")
                               .append(r.getHoraFin()).append("<br>")
                               .append("&nbsp;&nbsp;<i>").append(r.getDescripcion()).append("</i><br>");
                }
                tooltipHTML.append("</html>");

                btnDia.setToolTipText(tooltipHTML.toString());
            } else {
                btnDia.setBackground(Color.WHITE);
                btnDia.setForeground(new Color(80, 80, 80));
            }

            panelCalendario.add(btnDia);
        }

        panelCalendario.revalidate();
        panelCalendario.repaint();
    }

    public void addRecordatorioListener(ActionListener listener) {
        btnRecordatorio.addActionListener(listener);
    }

    public void addEliminarListener(ActionListener listener) {
        btnEliminar.addActionListener(listener);
    }

    public void addVerListener(ActionListener listener) {
        btnVer.addActionListener(listener);
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }

    public Usuario getUsuarioActual() {
        return usuarioActual;
    }
}
