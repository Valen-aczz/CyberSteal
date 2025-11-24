import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class SeleccionMisionPanel extends JPanel {

    private StoryState storyState;
    private JFrame ventanaPadre; 
    private JPanel gamesPanel;

    // Colores constantes para mantener consistencia
    private static final Color COLOR_FONDO_OSCURO = new Color(5, 10, 20);
    private static final Color COLOR_FONDO_MEDIO = new Color(15, 20, 40);
    private static final Color COLOR_CIAN = new Color(0, 255, 255);
    private static final Color COLOR_CIAN_TRANSPARENTE = new Color(0, 255, 255, 40);
    private static final Color COLOR_TEXTO_SECUNDARIO = new Color(150, 150, 255);
    
    public SeleccionMisionPanel(StoryState storyState, JFrame ventanaPadre) {
        this.storyState = storyState;
        this.ventanaPadre = ventanaPadre;
        setLayout(new BorderLayout());
        inicializarInterfaz();
    }

    private void inicializarInterfaz() {
        mostrarJuegos();
    }

    private void mostrarJuegos() {
        if (gamesPanel != null) return;

        crearPanelPrincipal();
        configurarComponentes();
    }

    private void crearPanelPrincipal() {
        gamesPanel = new JPanel() {
            private float hue = 0f;
            private int pulse = 0;
            private boolean pulseUp = true;
            private int scanLine = 0;

            {
                // Animación de fondo
                new javax.swing.Timer(30, e -> {
                    actualizarAnimacion();
                    repaint();
                }).start();
            }

            private void actualizarAnimacion() {
                hue += 0.003f;
                if (hue > 1) hue = 0;
                
                if (pulseUp) {
                    pulse += 2;
                    if (pulse >= 25) pulseUp = false;
                } else {
                    pulse -= 2;
                    if (pulse <= 0) pulseUp = true;
                }
                
                scanLine = (scanLine + 2) % getHeight();
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                dibujarFondo(g2d);
                dibujarPatronGrid(g2d);
                dibujarEfectosAnimados(g2d);
            }

            private void dibujarFondo(Graphics2D g2d) {
                GradientPaint gradient = new GradientPaint(
                    0, 0, COLOR_FONDO_OSCURO, 
                    getWidth(), getHeight(), COLOR_FONDO_MEDIO
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }

            private void dibujarPatronGrid(Graphics2D g2d) {
                g2d.setColor(new Color(0, 255, 255, 15));
                
                // Líneas verticales
                for (int x = 0; x < getWidth(); x += 40) {
                    g2d.drawLine(x, 0, x, getHeight());
                }
                
                // Líneas horizontales
                for (int y = 0; y < getHeight(); y += 40) {
                    g2d.drawLine(0, y, getWidth(), y);
                }
            }

            private void dibujarEfectosAnimados(Graphics2D g2d) {
                // Línea de escaneo
                g2d.setColor(new Color(0, 255, 255, 40 + pulse));
                g2d.fillRect(0, scanLine, getWidth(), 3);
                
                g2d.setColor(new Color(0, 255, 255, 20));
                g2d.fillRect(0, scanLine - 5, getWidth(), 13);

                // Partículas orbitales
                g2d.setColor(new Color(0, 255, 255, 30));
                for (int i = 0; i < 15; i++) {
                    int x = (int)(Math.sin(hue * Math.PI * 2 + i) * 100 + getWidth() / 2);
                    int y = (int)(Math.cos(hue * Math.PI * 2 + i) * 100 + getHeight() / 2);
                    g2d.fillOval(x, y, 3, 3);
                }
            }
        };

        gamesPanel.setLayout(new BorderLayout());
    }

    private void configurarComponentes() {
        gamesPanel.add(crearHeader(), BorderLayout.NORTH);
        gamesPanel.add(crearProgressSection(), BorderLayout.SOUTH);
        gamesPanel.add(crearCenterContent(), BorderLayout.CENTER);
        add(gamesPanel);
    }

    private JPanel crearHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 25, 0));

        JLabel titulo = new JLabel("CYBERSTEAL - SELECCIONAR MISIÓN", SwingConstants.CENTER);
        titulo.setFont(new Font("Consolas", Font.BOLD, 48));
        titulo.setForeground(COLOR_CIAN);
        
        JLabel subtitulo = new JLabel("[ ELIGE UN MINIJUEGO ]", SwingConstants.CENTER);
        subtitulo.setFont(new Font("Consolas", Font.BOLD, 18));
        subtitulo.setForeground(COLOR_TEXTO_SECUNDARIO);
        subtitulo.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        JPanel titleContainer = new JPanel();
        titleContainer.setLayout(new BoxLayout(titleContainer, BoxLayout.Y_AXIS));
        titleContainer.setOpaque(false);
        
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleContainer.add(titulo);
        titleContainer.add(subtitulo);

        headerPanel.add(titleContainer, BorderLayout.CENTER);
        return headerPanel;
    }

    private JPanel crearProgressSection() {
        JPanel progressPanel = new JPanel(new BorderLayout());
        progressPanel.setOpaque(false);
        progressPanel.setBorder(BorderFactory.createEmptyBorder(15, 120, 25, 120));

        JLabel progressLabel = new JLabel("SYSTEM SECURITY LEVEL", SwingConstants.CENTER);
        progressLabel.setFont(new Font("Consolas", Font.BOLD, 18));
        progressLabel.setForeground(COLOR_CIAN);
        progressLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        JProgressBar barraProgreso = crearBarraProgresoPersonalizada();

        JPanel progressContainer = new JPanel(new BorderLayout());
        progressContainer.setOpaque(false);
        progressContainer.add(progressLabel, BorderLayout.NORTH);
        progressContainer.add(barraProgreso, BorderLayout.CENTER);

        return progressContainer;
    }

    private JProgressBar crearBarraProgresoPersonalizada() {
        JProgressBar barraProgreso = new JProgressBar(0, 3) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Fondo de la barra
                g2d.setColor(new Color(20, 30, 50));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                
                // Borde
                g2d.setColor(COLOR_CIAN);
                g2d.setStroke(new BasicStroke(2f));
                g2d.drawRoundRect(1, 1, getWidth()-2, getHeight()-2, 15, 15);
                
                // Progreso
                int progressWidth = (int)((getWidth() - 6) * ((double)getValue() / getMaximum()));
                if (progressWidth > 0) {
                    GradientPaint gradient = new GradientPaint(
                        0, 0, new Color(0, 255, 150), 
                        progressWidth, 0, new Color(0, 200, 255)
                    );
                    g2d.setPaint(gradient);
                    g2d.fillRoundRect(3, 3, progressWidth, getHeight()-6, 12, 12);
                }
                
                // Texto
                g2d.setColor(Color.WHITE);
                g2d.setFont(new Font("Consolas", Font.BOLD, 16));
                String text = getString();
                FontMetrics fm = g2d.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(text)) / 2;
                int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                g2d.drawString(text, x, y);
            }
        };
        
        barraProgreso.setValue(storyState.getCapitulosCompletados());
        barraProgreso.setPreferredSize(new Dimension(0, 40));
        barraProgreso.setStringPainted(false);
        barraProgreso.setString(storyState.getCapitulosCompletados() + " / 3 MISIONES COMPLETADAS");

        return barraProgreso;
    }

    private JPanel crearCenterContent() {
        JPanel centerContainer = new JPanel(new BorderLayout());
        centerContainer.setOpaque(false);
        centerContainer.add(crearPanelMisiones(), BorderLayout.CENTER);
        centerContainer.add(crearFooter(), BorderLayout.SOUTH);
        
        return centerContainer;
    }

    private JPanel crearPanelMisiones() {
        JPanel misionesContainer = new JPanel(new GridLayout(3, 1, 30, 30));
        misionesContainer.setOpaque(false);
        misionesContainer.setBorder(BorderFactory.createEmptyBorder(35, 100, 35, 100));
//matrices
        String[][] misiones = {
            {"LOCK & CODE", "DEFIENDE LOS SISTEMAS CREANDO CONTRASEÑAS", "cyan", "/images/logo1.png", "CRYPTO-SYSTEM DEFENSE | PASSWORD SECURITY"},
            {"ZERO-TRUST PROTOCOL", "CONSTRUYE LA VACUNA DIGITAL", "rojo", "/images/logo2.png", "ANTI-VIRUS CONSTRUCTION | CODE ASSEMBLY"},
            {"PHISHING FOR GOLD", "DETECTA POSIBLE PHISHING", "amarillo", "/images/logo3.png", "SOCIAL ENGINEERING DEFENSE | EMAIL FILTERING"}
        };
        // EXPLICACIÓN: Matriz 3x5 con información extendida de misiones
// CONCEPTO: Estructura matricial para datos organizados jerárquicamente

        for (String[] mision : misiones) {
            JPanel panelMision = crearPanelMisionCyber(
                mision[0], mision[1], mision[2], mision[3], mision[4]
            );
            misionesContainer.add(panelMision);
        }

        return misionesContainer;
    }  
   // Función con parámetro y retorno
    
    
    private JPanel crearFooter() {
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footerPanel.setOpaque(false);
        footerPanel.setBorder(BorderFactory.createEmptyBorder(25, 0, 35, 0));

        JButton btnRegresar = crearBotonCyber("VOLVER AL INICIO", new Color(100, 0, 150));
        btnRegresar.addActionListener(e -> {
            MenuEstilo.reproducirSonido("/sonidos/click.wav");
            ventanaPadre.dispose();
            new MenuEstilo(storyState);
        });

        footerPanel.add(btnRegresar);
        return footerPanel;
    }

    private JPanel crearPanelMisionCyber(String nombre, String tagline, String color, String logoPath, String descripcion) {
        JPanel panel = new JPanel(new BorderLayout(20, 0)) {
            private int glowIntensity = 0;
            private boolean glowUp = true;
            private Timer glowTimer;

            {
                addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseEntered(java.awt.event.MouseEvent e) {
                        iniciarEfectoGlow();
                    }

                    @Override
                    public void mouseExited(java.awt.event.MouseEvent e) {
                        detenerEfectoGlow();
                    }
                });
            }

            private void iniciarEfectoGlow() {
                if (glowTimer == null) {
                    glowTimer = new Timer(50, evt -> {
                        if (glowUp) {
                            glowIntensity += 5;
                            if (glowIntensity >= 80) glowUp = false;
                        } else {
                            glowIntensity -= 5;
                            if (glowIntensity <= 40) glowUp = true;
                        }
                        repaint();
                    });
                    glowTimer.start();
                }
            }

            private void detenerEfectoGlow() {
                if (glowTimer != null) {
                    glowTimer.stop();
                    glowTimer = null;
                    glowIntensity = 0;
                    repaint();
                }
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                dibujarFondoPanel(g2d);
                dibujarEfectosBorde(g2d, color);
            }

            private void dibujarFondoPanel(Graphics2D g2d) {
                Color baseColor = getBackground();
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(baseColor.getRed(), baseColor.getGreen(), baseColor.getBlue(), 200), 
                    getWidth(), getHeight(), new Color(baseColor.getRed() + 20, baseColor.getGreen() + 20, baseColor.getBlue() + 30, 180)
                );
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            }

            private void dibujarEfectosBorde(Graphics2D g2d, String color) {
                Color borderColor = getBorderColor(color);

                // Efecto glow
                if (glowIntensity > 0) {
                    for (int i = 0; i < 3; i++) {
                        g2d.setColor(new Color(borderColor.getRed(), borderColor.getGreen(), 
                            borderColor.getBlue(), glowIntensity / (i + 1)));
                        g2d.setStroke(new BasicStroke(4f + i * 2));
                        g2d.drawRoundRect(i, i, getWidth()-i*2, getHeight()-i*2, 20, 20);
                    }
                }

                // Borde principal
                g2d.setColor(new Color(borderColor.getRed(), borderColor.getGreen(), 
                    borderColor.getBlue(), 150 + glowIntensity));
                g2d.setStroke(new BasicStroke(3f));
                g2d.drawRoundRect(2, 2, getWidth()-4, getHeight()-4, 20, 20);

                // Líneas decorativas
                g2d.setColor(new Color(borderColor.getRed(), borderColor.getGreen(), 
                    borderColor.getBlue(), 40));
                g2d.setStroke(new BasicStroke(1f));
                g2d.drawLine(15, 15, getWidth()-15, 15);
                g2d.drawLine(15, getHeight()-15, getWidth()-15, getHeight()-15);
            }
        };

        panel.setBackground(new Color(15, 20, 30, 220));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        panel.setPreferredSize(new Dimension(950, 160));

        configurarContenidoPanelMision(panel, nombre, tagline, color, logoPath, descripcion);
        return panel;
    }

    private void configurarContenidoPanelMision(JPanel panel, String nombre, String tagline, String color, String logoPath, String descripcion) {
        panel.add(crearLogoPanel(nombre, color, logoPath), BorderLayout.WEST);
        panel.add(crearInfoPanel(nombre, tagline, color, descripcion), BorderLayout.CENTER);
        panel.add(crearStatusPanel(), BorderLayout.EAST);

        agregarEventosPanelMision(panel, nombre);
    }

    private JPanel crearLogoPanel(String nombre, String color, String logoPath) {
        JPanel logoPanel = new JPanel(new BorderLayout());
        logoPanel.setOpaque(false);
        logoPanel.setPreferredSize(new Dimension(100, 100));

        JLabel logoLabel = new JLabel();
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        logoLabel.setVerticalAlignment(SwingConstants.CENTER);
        
        try {
            ImageIcon originalIcon = new ImageIcon(getClass().getResource(logoPath));
            Image scaledImage = originalIcon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
            logoLabel.setIcon(new ImageIcon(scaledImage));
        } catch (Exception e) {
            logoLabel.setText(nombre.substring(0, 1));
            logoLabel.setFont(new Font("Consolas", Font.BOLD, 48));
            logoLabel.setForeground(getTextColor(color));
        }

        logoPanel.add(logoLabel, BorderLayout.CENTER);
        return logoPanel;
    }

    private JPanel crearInfoPanel(String nombre, String tagline, String color, String descripcion) {
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);

        JLabel nombreLabel = new JLabel(nombre);
        nombreLabel.setFont(new Font("Consolas", Font.BOLD, 28));
        nombreLabel.setForeground(getTextColor(color));
        nombreLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel taglineLabel = new JLabel(tagline);
        taglineLabel.setFont(new Font("Consolas", Font.ITALIC, 12));
        taglineLabel.setForeground(new Color(180, 180, 220));
        taglineLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 8, 0));
        taglineLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextArea descripcionArea = new JTextArea(descripcion);
        descripcionArea.setFont(new Font("Consolas", Font.PLAIN, 13));
        descripcionArea.setForeground(new Color(200, 200, 240));
        descripcionArea.setOpaque(false);
        descripcionArea.setEditable(false);
        descripcionArea.setLineWrap(true);
        descripcionArea.setWrapStyleWord(true);
        descripcionArea.setVisible(false);
        descripcionArea.setAlignmentX(Component.LEFT_ALIGNMENT);

        infoPanel.add(nombreLabel);
        infoPanel.add(taglineLabel);
        infoPanel.add(descripcionArea);

        return infoPanel;
    }

    private JPanel crearStatusPanel() {
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setOpaque(false);
        statusPanel.setPreferredSize(new Dimension(150, 100));

        JLabel statusLabel = new JLabel("CLICK TO START", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Consolas", Font.BOLD, 12));
        statusLabel.setForeground(COLOR_CIAN);
        statusLabel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 255, 255, 100), 2),
            BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        statusLabel.setVisible(false);

        statusPanel.add(statusLabel, BorderLayout.CENTER);
        return statusPanel;
    }

    private void agregarEventosPanelMision(JPanel panel, String nombre) {
        panel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                MenuEstilo.reproducirSonido("/sonidos/hover.wav");
                panel.setBackground(getHoverColor(getColorFromNombre(nombre)));
                mostrarContenidoAdicional(panel, true);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                panel.setBackground(new Color(15, 20, 30, 220));
                mostrarContenidoAdicional(panel, false);
            }

            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                MenuEstilo.reproducirSonido("/sonidos/click.wav");
                ejecutarMision(nombre);
            }
        });
    }

    private void mostrarContenidoAdicional(JPanel panel, boolean mostrar) {
        // Buscar los componentes correctamente
        JPanel infoPanel = (JPanel) panel.getComponent(1); // Panel central con la información
        JPanel statusPanel = (JPanel) panel.getComponent(2); // Panel derecho con un "CLICK TO START"
        
        // Encontrar el JTextArea (descripción) dentro del infoPanel
        for (Component comp : infoPanel.getComponents()) {
            if (comp instanceof JTextArea) {
                comp.setVisible(mostrar); // muestra u oculta la descripción técnica
                break;
            }
        }
        
        // Mostrar/ocultar el "CLICK TO START"
        statusPanel.getComponent(0).setVisible(mostrar);
        
        panel.revalidate();
        panel.repaint();
    }

    private void ejecutarMision(String nombreMision) {
        if (nombreMision.contains("LOCK & CODE")) new minijuego1(storyState);
        if (nombreMision.contains("ZERO-TRUST")) new minijuego2(storyState);
        if (nombreMision.contains("PHISHING")) new minijuego3(storyState);

        ventanaPadre.dispose();
    }

    private String getColorFromNombre(String nombre) {
        if (nombre.contains("LOCK & CODE")) return "cyan";
        if (nombre.contains("ZERO-TRUST")) return "rojo";
        if (nombre.contains("PHISHING")) return "amarillo";
        return "cyan";
    }

    private JButton crearBotonCyber(String texto, Color colorBase) {
        JButton btn = new JButton(texto) {
            private boolean hover = false;

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                dibujarFondoBoton(g2d);
                dibujarTextoBoton(g2d);
            }

            private void dibujarFondoBoton(Graphics2D g2d) {
                Color colorSuperior = hover ? colorBase.brighter() : colorBase;
                Color colorInferior = hover ? colorBase : colorBase.darker();
                
                GradientPaint gradient = new GradientPaint(0, 0, colorSuperior, getWidth(), getHeight(), colorInferior);
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);

                if (hover) {
                    g2d.setColor(new Color(0, 255, 255, 100));
                    g2d.setStroke(new BasicStroke(4f));
                    g2d.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 25, 25);
                }
                
                g2d.setColor(COLOR_CIAN);
                g2d.setStroke(new BasicStroke(2f));
                g2d.drawRoundRect(2, 2, getWidth()-4, getHeight()-4, 25, 25);
            }

            private void dibujarTextoBoton(Graphics2D g2d) {
                g2d.setColor(new Color(0, 0, 0, 150));
                g2d.setFont(getFont());
                FontMetrics fm = g2d.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                g2d.drawString(getText(), x+2, y+2);
                
                g2d.setColor(Color.WHITE);
                g2d.drawString(getText(), x, y);
            }
        };

        btn.setFont(new Font("Consolas", Font.BOLD, 18));
        btn.setPreferredSize(new Dimension(350, 55));
        btn.setBorder(BorderFactory.createEmptyBorder(12, 25, 12, 25));
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);

        agregarEventosBoton(btn);
        return btn;
    }

    private void agregarEventosBoton(JButton btn) {
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                MenuEstilo.reproducirSonido("/sonidos/hover.wav");
                setHoverState(btn, true);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                setHoverState(btn, false);
            }
        });
    }

    private void setHoverState(JButton btn, boolean hover) {
        try {
            java.lang.reflect.Field hoverField = btn.getClass().getDeclaredField("hover");
            hoverField.setAccessible(true);
            hoverField.set(btn, hover);
        } catch (Exception ex) {
            // el fallback
        }
        btn.repaint();
    }

    // Métodos auxiliares para los colores
    private Color getBorderColor(String color) {
        switch (color) {
            case "cyan": return COLOR_CIAN;
            case "rojo": return new Color(255, 50, 50);
            case "amarillo": return new Color(255, 220, 0);
            default: return new Color(0, 200, 255);
        }
    }

    private Color getTextColor(String color) {
        switch (color) {
            case "cyan": return COLOR_CIAN;
            case "rojo": return new Color(255, 100, 100);
            case "amarillo": return new Color(255, 230, 100);
            default: return Color.WHITE;
        }
    }

    private Color getHoverColor(String color) {
        switch (color) {
            case "cyan": return new Color(0, 60, 80, 230);
            case "rojo": return new Color(80, 20, 20, 230);
            case "amarillo": return new Color(80, 70, 0, 230);
            default: return new Color(40, 40, 60, 230);
        }
    }
}