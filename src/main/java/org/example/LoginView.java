package org.example;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;

public class LoginView extends JFrame {
    private JTextField txtNombre;
    private JPasswordField txtContraseña;
    private JButton btnLogin;
    private JButton btnRegistro;
    private JLabel lblError;
    private JCheckBox chkRegistrarse;

    public LoginView() {
        setTitle("📅 Agenda Profesional - Login");
        setSize(500, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setBackground(new Color(245, 247, 252));

        inicializarUI();
    }

    private void inicializarUI() {
        // Panel principal con fondo personalizado
        JPanel panelPrincipal = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                
                // Gradiente vertical
                Color colorArriba = new Color(41, 128, 185);
                Color colorAbajo = new Color(52, 152, 219);
                GradientPaint gp = new GradientPaint(0, 0, colorArriba, 0, getHeight(), colorAbajo);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                super.paintComponent(g);
            }
        };
        panelPrincipal.setLayout(new BorderLayout());
        setContentPane(panelPrincipal);

        // Panel superior con gradiente
        JPanel panelTop = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                Color c1 = new Color(41, 128, 185);
                Color c2 = new Color(52, 152, 219);
                GradientPaint gp = new GradientPaint(0, 0, c1, 0, getHeight(), c2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panelTop.setLayout(new BoxLayout(panelTop, BoxLayout.Y_AXIS));
        panelTop.setBorder(new EmptyBorder(40, 20, 40, 20));
        panelTop.setPreferredSize(new Dimension(0, 180));

        JLabel lblTitulo = new JLabel("📅 Agenda");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 42));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSubtitulo = new JLabel("Profesional");
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        lblSubtitulo.setForeground(new Color(200, 220, 255));
        lblSubtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        panelTop.add(lblTitulo);
        panelTop.add(Box.createVerticalStrut(8));
        panelTop.add(lblSubtitulo);

        panelPrincipal.add(panelTop, BorderLayout.NORTH);

        // Panel central con formulario
        JPanel panelCentral = new JPanel();
        panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.Y_AXIS));
        panelCentral.setBorder(new EmptyBorder(40, 40, 40, 40));
        panelCentral.setBackground(new Color(245, 247, 252));

        // Usuario
        JLabel lblNombre = new JLabel("Usuario");
        lblNombre.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblNombre.setForeground(new Color(41, 128, 185));
        panelCentral.add(lblNombre);

        txtNombre = crearTextField();
        panelCentral.add(txtNombre);
        panelCentral.add(Box.createVerticalStrut(15));

        // Contraseña
        JLabel lblContraseña = new JLabel("Contraseña");
        lblContraseña.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblContraseña.setForeground(new Color(41, 128, 185));
        panelCentral.add(lblContraseña);

        txtContraseña = crearPasswordField();
        panelCentral.add(txtContraseña);
        panelCentral.add(Box.createVerticalStrut(15));

        // Checkbox para registro
        chkRegistrarse = new JCheckBox("¿No tienes cuenta? Regístrate");
        chkRegistrarse.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        chkRegistrarse.setForeground(new Color(80, 80, 80));
        chkRegistrarse.setBackground(new Color(245, 247, 252));
        chkRegistrarse.setFocusPainted(false);
        panelCentral.add(chkRegistrarse);
        panelCentral.add(Box.createVerticalStrut(20));

        // Botones
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new BoxLayout(panelBotones, BoxLayout.Y_AXIS));
        panelBotones.setBackground(new Color(245, 247, 252));

        btnLogin = crearBotonPrincipal("Iniciar Sesión", new Color(46, 204, 113));
        btnRegistro = crearBotonPrincipal("Registrarse", new Color(52, 152, 219));
        btnRegistro.setEnabled(false);
        btnRegistro.setVisible(false);

        panelBotones.add(btnLogin);
        panelBotones.add(Box.createVerticalStrut(10));
        panelBotones.add(btnRegistro);

        panelCentral.add(panelBotones);
        panelCentral.add(Box.createVerticalStrut(15));

        // Error
        lblError = new JLabel("");
        lblError.setForeground(new Color(231, 76, 60));
        lblError.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblError.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelCentral.add(lblError);

        // Listener para checkbox
        chkRegistrarse.addActionListener(e -> {
            boolean mostrarRegistro = chkRegistrarse.isSelected();
            btnLogin.setVisible(!mostrarRegistro);
            btnRegistro.setVisible(mostrarRegistro);
            if (mostrarRegistro) {
                lblError.setText("");
                txtNombre.setText("");
                txtContraseña.setText("");
            }
        });

        JScrollPane scroll = new JScrollPane(panelCentral);
        scroll.setBorder(null);
        scroll.setBackground(new Color(245, 247, 252));
        scroll.getViewport().setBackground(new Color(245, 247, 252));
        panelPrincipal.add(scroll, BorderLayout.CENTER);
    }

    private JTextField crearTextField() {
        JTextField txt = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2d.setColor(Color.WHITE);
                g2d.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
                
                g2d.setColor(new Color(200, 210, 220));
                g2d.setStroke(new BasicStroke(1.5f));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
                
                super.paintComponent(g);
            }
        };
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txt.setBorder(BorderFactory.createEmptyBorder(12, 15, 12, 15));
        txt.setBackground(Color.WHITE);
        txt.setForeground(new Color(50, 50, 50));
        txt.setOpaque(false);
        txt.setPreferredSize(new Dimension(0, 42));
        return txt;
    }

    private JPasswordField crearPasswordField() {
        JPasswordField pwd = new JPasswordField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2d.setColor(Color.WHITE);
                g2d.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
                
                g2d.setColor(new Color(200, 210, 220));
                g2d.setStroke(new BasicStroke(1.5f));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
                
                super.paintComponent(g);
            }
        };
        pwd.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        pwd.setBorder(BorderFactory.createEmptyBorder(12, 15, 12, 15));
        pwd.setBackground(Color.WHITE);
        pwd.setForeground(new Color(50, 50, 50));
        pwd.setOpaque(false);
        pwd.setPreferredSize(new Dimension(0, 42));
        return pwd;
    }

    private JButton crearBotonPrincipal(String texto, Color color) {
        JButton btn = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                Color btnColor;
                if (!isEnabled()) {
                    btnColor = new Color(180, 180, 180);
                } else if (getModel().isArmed()) {
                    btnColor = darkenColor(color, 0.85f);
                } else if (getModel().isRollover()) {
                    btnColor = darkenColor(color, 0.95f);
                } else {
                    btnColor = color;
                }
                
                g2d.setColor(btnColor);
                g2d.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
                
                super.paintComponent(g);
            }
        };
        
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(Color.WHITE);
        btn.setPreferredSize(new Dimension(0, 45));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        return btn;
    }

    private Color darkenColor(Color color, float factor) {
        return new Color(
            (int)(color.getRed() * factor),
            (int)(color.getGreen() * factor),
            (int)(color.getBlue() * factor)
        );
    }

    public String getNombre() {
        return txtNombre.getText().trim();
    }

    public String getContraseña() {
        return new String(txtContraseña.getPassword());
    }

    public boolean esRegistro() {
        return chkRegistrarse.isSelected();
    }

    public void setError(String error) {
        lblError.setText(error);
    }

    public void limpiar() {
        txtNombre.setText("");
        txtContraseña.setText("");
        chkRegistrarse.setSelected(false);
        lblError.setText("");
        btnLogin.setVisible(true);
        btnRegistro.setVisible(false);
    }

    public void addLoginListener(ActionListener listener) {
        btnLogin.addActionListener(listener);
    }

    public void addRegistroListener(ActionListener listener) {
        btnRegistro.addActionListener(listener);
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }
}
