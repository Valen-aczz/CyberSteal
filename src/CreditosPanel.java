
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import javax.swing.Timer;

public class CreditosPanel extends JPanel {

    private JLabel titulo;
    private JLabel infoSecreta;
    private Timer textoTimer;
    private Image fondoImage;

    public CreditosPanel(Runnable volverAlMenu) {
        setLayout(new BorderLayout());
        setBackground(new Color(8, 8, 12));
        setPreferredSize(new Dimension(1000, 600));
       
        // Cargar imagen de fondo
        try {
            fondoImage = new ImageIcon(getClass().getResource("/images/fondo.png")).getImage();
        } catch (Exception e) {
            fondoImage = null;
        }

        // Fondo con overlay oscuro para mejor legibilidad
        JPanel fondoPanel = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                
                // Dibujar imagen de fondo si existe
                if (fondoImage != null) {
                    g2d.drawImage(fondoImage, 0, 0, getWidth(), getHeight(), this);
                    // Overlay oscuro para mejor legibilidad
                    g2d.setColor(new Color(0, 0, 0, 150));
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                } else {
                    // Gradiente oscuro de fondo (solo si no hay imagen)
                    GradientPaint gradient = new GradientPaint(
                        0, 0, new Color(10, 10, 20), 
                        getWidth(), getHeight(), new Color(20, 15, 30)
                    );
                    g2d.setPaint(gradient);
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                }
                
                // Efecto de partículas sutiles
                g2d.setColor(new Color(100, 100, 150, 30));
                for (int i = 0; i < 30; i++) {
                    int x = (int)(Math.random() * getWidth());
                    int y = (int)(Math.random() * getHeight());
                    int size = (int)(Math.random() * 2) + 1;
                    g2d.fillOval(x, y, size, size);
                }
            }
        };
        fondoPanel.setPreferredSize(new Dimension(1000, 600));

        // Título principal centrado en la parte superior
        titulo = new JLabel("", SwingConstants.CENTER);
        titulo.setFont(getCyberFont(41f));
        titulo.setForeground(new Color(255, 40, 40));
        titulo.setBounds(0, 20, 1000, 60);
        fondoPanel.add(titulo);

        // Timer para efecto glitch más suave
        Timer glitchTimer = new Timer(150, e -> {
            int dx = (int)(Math.random() * 3 - 1);
            int dy = (int)(Math.random() * 3 - 1);
            int r = 255;
            int g = (int)(Math.random() * 60);
            int b = (int)(Math.random() * 60);
            titulo.setForeground(new Color(r, g, b));
            titulo.setLocation(dx, 20 + dy);
        });
        glitchTimer.start();

        escribirTextoLento("[SISTEMA] - ACCESO A CREDITOS", titulo, 67);

        // Panel de integrantes del equipo
        JPanel equipoPanel = crearPanelEquipo();
        equipoPanel.setBounds(50, 100, 900, 290);
        fondoPanel.add(equipoPanel);

        // Panel del equipo docente
        JPanel docentePanel = crearPanelDocente();
        docentePanel.setBounds(100, 400, 800, 100);
        fondoPanel.add(docentePanel);

        // Botón de volver
        JButton backButton = crearBotonVolver(volverAlMenu);
        backButton.setBounds(50, 500, 200, 45);
        fondoPanel.add(backButton);

        // Zona secreta
        crearZonaSecreta(fondoPanel);

        add(fondoPanel, BorderLayout.CENTER);
    }

    private JPanel crearPanelEquipo() {
        JPanel panel = new JPanel(new GridLayout(1, 4, 15, 0));
        panel.setOpaque(false);

        Object[][] integrantes = {
            {"Majo", "Gestora de proyecto", "/images/Majo.jpg", "Registro: Estrategia general y control de recursos."},
            {"Karla", "Diseñadora", "/images/karla.png", "Registro: Interfaz visual y coherencia estética."},
            {"Valen", "Programadora", "/images/Valen.jpg", "Registro: Arquitectura del sistema, narrativa y eventos."},
            {"Mau", "Documentador técnico", "/images/Mau.png", "Registro: Redacción técnica y documentación de desarrollo"}
        };

        for (Object[] integrante : integrantes) {
            panel.add(crearTarjetaIntegrante(
                (String) integrante[0],
                (String) integrante[1],
                (String) integrante[2],
                (String) integrante[3]
            ));
        }

        return panel;
    }

private JPanel crearTarjetaIntegrante(String nombre, String rol, String imagenRuta, String descripcion) {
    JPanel tarjeta = new JPanel(new BorderLayout(0, 8));
    tarjeta.setPreferredSize(new Dimension(180, 180));
    
    // FONDO CON ESTÁTICA
    tarjeta.setBackground(new Color(15, 15, 25)); // Más opaco
    tarjeta.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(0, 180, 255), 1),
        BorderFactory.createEmptyBorder(12, 12, 12, 12)
    ));

    // Imagen circular
    JLabel imagenLabel = new JLabel();
    imagenLabel.setPreferredSize(new Dimension(80, 80));
    imagenLabel.setHorizontalAlignment(SwingConstants.CENTER);
    
    ImageIcon imagenCircular = crearImagenCircularElegante(imagenRuta, 80, 80);
    imagenLabel.setIcon(imagenCircular);

    // Información del integrante
    JPanel infoPanel = new JPanel();
    infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
    infoPanel.setOpaque(false);

    JLabel nombreLabel = new JLabel(nombre);
    nombreLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
    nombreLabel.setForeground(new Color(0, 220, 255));
    nombreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

    JLabel rolLabel = new JLabel(rol);
    rolLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
    rolLabel.setForeground(new Color(180, 230, 255));
    rolLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

    JTextArea descArea = new JTextArea(descripcion);
    descArea.setFont(new Font("Segoe UI", Font.ITALIC, 11));
    descArea.setForeground(new Color(200, 200, 220));
    descArea.setOpaque(false);
    descArea.setEditable(false);
    descArea.setLineWrap(true);
    descArea.setWrapStyleWord(true);
    descArea.setAlignmentX(Component.CENTER_ALIGNMENT);
    descArea.setBorder(new EmptyBorder(3, 2, 0, 2));

    // ✅ INFORMACIÓN ADICIONAL OCULTA (aparece en hover)
    JTextArea infoExtra = new JTextArea();
    infoExtra.setFont(new Font("Consolas", Font.PLAIN, 9));
    infoExtra.setForeground(new Color(120, 220, 255));
    infoExtra.setOpaque(false);
    infoExtra.setEditable(false);
    infoExtra.setLineWrap(true);
    infoExtra.setWrapStyleWord(true);
    infoExtra.setAlignmentX(Component.CENTER_ALIGNMENT);
    infoExtra.setBorder(new EmptyBorder(5, 2, 0, 2));
    infoExtra.setVisible(false); // ✅ Inicialmente oculta

    // Texto adicional específico para cada integrante
    String textoAdicional = switch (nombre) {
        case "Majo" -> "• ID: CD-047-MJF\n• Especialidad: Liderazgo\n• Estado: Activo";
        case "Karla" -> "• ID: CD-048-KPR\n• Especialidad: UI/UX\n• Estado: Activo";
        case "Valen" -> "• ID: CD-049-VAC\n• Especialidad: Sistemas\n• Estado: Activo";
        case "Mau" -> "• ID: CD-050-MOP\n• Especialidad: Docs\n• Estado: Activo";
        default -> "• ID: Desconocido\n• Especialidad: N/A\n• Estado: Inactivo";
    };
    infoExtra.setText(textoAdicional);

    infoPanel.add(nombreLabel);
    infoPanel.add(Box.createVerticalStrut(3));
    infoPanel.add(rolLabel);
    infoPanel.add(Box.createVerticalStrut(5));
    infoPanel.add(descArea);
    infoPanel.add(Box.createVerticalStrut(8)); // Espacio para la info extra
    infoPanel.add(infoExtra);

    tarjeta.add(imagenLabel, BorderLayout.NORTH);
    tarjeta.add(infoPanel, BorderLayout.CENTER);

    // ✅ EFECTOS HOVER MEJORADOS
    tarjeta.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseEntered(MouseEvent e) {
            // Cambiar fondo a efecto "estática"
            tarjeta.setBackground(new Color(25, 25, 40));
            tarjeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0, 255, 255), 2),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)
            ));
            
            // ✅ MOSTRAR INFORMACIÓN ADICIONAL
            descArea.setVisible(false);
            infoExtra.setVisible(true);
            
            // ✅ EFECTO DE ESTÁTICA VISUAL
            empezarEfectoEstatica(tarjeta);
            
            MenuEstilo.reproducirSonido("/sonidos/hover.wav");
        }

        @Override
        public void mouseExited(MouseEvent e) {
            // Volver al estado normal
            tarjeta.setBackground(new Color(15, 15, 25));
            tarjeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0, 180, 255), 1),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)
            ));
            
            // OCULTAR INFORMACIÓN ADICIONAL
            infoExtra.setVisible(false);
            descArea.setVisible(true);
            
            // DETENER EFECTO DE ESTÁTICA
            detenerEfectoEstatica(tarjeta);
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            MenuEstilo.reproducirSonido("/sonidos/click.wav");
            mostrarInfoDetallada(nombre, imagenRuta);
        }
    });

    return tarjeta;
}

    private ImageIcon crearImagenCircularElegante(String ruta, int width, int height) {
        
        try {
            //  PROBAR MÚLTIPLES RUTAS
            java.net.URL imgURL = null;
            String[] rutasAlternativas = {
                ruta,
                ruta.startsWith("/") ? ruta.substring(1) : "/" + ruta,
                "images/" + (ruta.contains("/") ? ruta.substring(ruta.lastIndexOf("/") + 1) : ruta),
                "/resources" + (ruta.startsWith("/") ? ruta : "/" + ruta)
            };
            
            for (String rutaAlt : rutasAlternativas) {
                imgURL = getClass().getResource(rutaAlt);
                if (imgURL != null) {
                    break;
                }
            }
            
            if (imgURL == null) {
                System.err.println("❌ No se encontró la imagen en ninguna ruta alternativa");
                return crearPlaceholderElegante(width, height);
            }
            
         BufferedImage originalImage;
        try {
            originalImage = javax.imageio.ImageIO.read(imgURL);
            if (originalImage == null) {
                throw new Exception("ImageIO.read returned null");
            }
        } catch (Exception e) {
            System.err.println("❌ Falló ImageIO.read: " + e.getMessage());
            return crearPlaceholderElegante(width, height);
        }
        
        // ✅ CREAR IMAGEN DE SALIDA CON FONDO SÓLIDO (para evitar transparencias problemáticas)
        BufferedImage circularImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = circularImage.createGraphics();
        
        // ✅ CONFIGURACIÓN DE ALTA CALIDAD
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        
        // FONDO SÓLIDO (para evitar problemas de transparencia)
        g2d.setColor(new Color(30, 30, 50)); // Fondo azul oscuro
        g2d.fillOval(0, 0, width, height);
        
        // MÁSCARA CIRCULAR
        Shape circle = new java.awt.geom.Ellipse2D.Float(0, 0, width, height);
        g2d.setClip(circle);
        
        // ✅ CALCULAR ESCALA PARA QUE LA IMAGEN LLENE EL CÍRCULO
        double scale = Math.max((double) width / originalImage.getWidth(), 
                               (double) height / originalImage.getHeight());
        int scaledWidth = (int) (originalImage.getWidth() * scale);
        int scaledHeight = (int) (originalImage.getHeight() * scale);
        int x = (width - scaledWidth) / 2;
        int y = (height - scaledHeight) / 2;
        
        // ✅ DIBUJAR IMAGEN REDIMENSIONADA
        g2d.drawImage(originalImage, x, y, scaledWidth, scaledHeight, null);
        
        // BORDE
        g2d.setClip(null);
        g2d.setStroke(new BasicStroke(3));
        g2d.setColor(new Color(0, 200, 255));
        g2d.drawOval(2, 2, width-5, height-5);
        
        g2d.dispose();
        
        
        //VERIFICACIÓN FINAL - Crear una versión de prueba visible
        if (circularImage != null) {
        }
        
        return new ImageIcon(circularImage);
        
    } catch (Exception e) {
        System.err.println("❌ Error crítico cargando " + ruta + ": " + e.getMessage());
        e.printStackTrace();
        return crearPlaceholderElegante(width, height);
    }
    }

    private JPanel crearPanelDocente() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 20, 0));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        Object[][] equipoDocente = {
            {
                "Leddy Diana Pájaro", 
                "Profesora", 
                "/images/profesora.png",
                "Algoritmia y Programación II"
            },
            {
                "Angélica Baños", 
                "Monitora", 
                "/images/monitora.png",
                "Soporte académico"
            }
        };

        for (Object[] docente : equipoDocente) {
            panel.add(crearTarjetaDocente(
                (String) docente[0],
                (String) docente[1],
                (String) docente[2],
                (String) docente[3]
            ));
        }

        return panel;
    }

   private JPanel crearTarjetaDocente(String nombre, String rol, String imagenRuta, String asignatura) {
        JPanel tarjeta = new JPanel(new BorderLayout(10, 0));
        tarjeta.setPreferredSize(new Dimension(380, 80));
        tarjeta.setBackground(new Color(25, 25, 40, 230));
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(100, 100, 150)),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        JLabel imagenLabel = new JLabel();
        imagenLabel.setPreferredSize(new Dimension(60, 60));
        
        ImageIcon imagenCircular = crearImagenCircularElegante(imagenRuta, 60, 60);
        imagenLabel.setIcon(imagenCircular);

        JPanel textoPanel = new JPanel();
        textoPanel.setLayout(new BoxLayout(textoPanel, BoxLayout.Y_AXIS));
        textoPanel.setOpaque(false);

        JLabel nombreLabel = new JLabel(nombre);
        nombreLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        nombreLabel.setForeground(new Color(255, 215, 0));

        JLabel rolLabel = new JLabel(rol);
        rolLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        rolLabel.setForeground(new Color(180, 230, 255));

        JLabel asigLabel = new JLabel(asignatura);
        asigLabel.setFont(new Font("Segoe UI", Font.ITALIC, 10));
        asigLabel.setForeground(new Color(200, 200, 220));

        textoPanel.add(nombreLabel);
        textoPanel.add(Box.createVerticalStrut(2));
        textoPanel.add(rolLabel);
        textoPanel.add(Box.createVerticalStrut(2));
        textoPanel.add(asigLabel);

        tarjeta.add(imagenLabel, BorderLayout.WEST);
        tarjeta.add(textoPanel, BorderLayout.CENTER);

        // Agregar efectos hover y click para la profesora
        tarjeta.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                tarjeta.setBackground(new Color(35, 35, 55, 250));
                tarjeta.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(2, 2, 2, 2, new Color(255, 215, 0)),
                    BorderFactory.createEmptyBorder(10, 10, 10, 10)
                ));
                tarjeta.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                MenuEstilo.reproducirSonido("/sonidos/hover.wav");
            }

            @Override
            public void mouseExited(MouseEvent e) {
                tarjeta.setBackground(new Color(25, 25, 40, 230));
                tarjeta.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(100, 100, 150)),
                    BorderFactory.createEmptyBorder(10, 10, 10, 10)
                ));
                tarjeta.setCursor(Cursor.getDefaultCursor());
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                MenuEstilo.reproducirSonido("/sonidos/click.wav");
                mostrarInfoDetalladaDocente(nombre, imagenRuta);
            }
        });

        return tarjeta;
    }

    private ImageIcon crearPlaceholderElegante(int width, int height) {
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        GradientPaint bgGradient = new GradientPaint(
            0, 0, new Color(40, 40, 80),
            width, height, new Color(20, 20, 50)
        );
        g2d.setPaint(bgGradient);
        g2d.fillOval(0, 0, width, height);
        
        g2d.setColor(new Color(0, 180, 255));
        g2d.setStroke(new BasicStroke(2));
        g2d.drawOval(1, 1, width-3, height-3);
        
        g2d.setColor(new Color(200, 220, 255));
        g2d.setFont(new Font("Arial", Font.BOLD, width/3));
        FontMetrics fm = g2d.getFontMetrics();
        String icon = "👤";
        int x = (width - fm.stringWidth(icon)) / 2;
        int y = (height - fm.getHeight()) / 2 + fm.getAscent();
        g2d.drawString(icon, x, y);
        
        g2d.dispose();
        return new ImageIcon(img);
    }

    private JButton crearBotonVolver(Runnable volverAlMenu) {
        JButton button = new JButton("← Volver al Menú");
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(30, 30, 45));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 180, 255), 2),
            BorderFactory.createEmptyBorder(8, 20, 8, 20)
        ));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(0, 80, 120));
                button.setForeground(new Color(0, 255, 255));
                MenuEstilo.reproducirSonido("/sonidos/hover.wav");
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(30, 30, 45));
                button.setForeground(Color.WHITE);
            }
        });

button.addActionListener(e -> {
    MenuEstilo.reproducirSonido("/sonidos/click.wav");
    volverAlMenu.run();
    if (MenuEstilo.isMusicaActiva()) {
        MenuEstilo.reproducirMusicaFondo("/sonidos/musicaelec.wav");
    }
});

        return button;
    }

    private void crearZonaSecreta(JPanel parent) {
        JLayeredPane layered = new JLayeredPane();
        layered.setBounds(0, 0, 800, 600);
        parent.add(layered, Integer.valueOf(1));

        JPanel zonaSecreta = new JPanel();
        zonaSecreta.setOpaque(false);
        zonaSecreta.setBounds(20, 40, 150, 150);
        zonaSecreta.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));

        infoSecreta = new JLabel(
            "<html><b>[ACCESO RESTRINGIDO]</b><br>Proyecto paralelo detectado...<br>Archivo: RAGHAN.EXE</html>"
        );
        infoSecreta.setFont(new Font("Consolas", Font.PLAIN, 13));
        infoSecreta.setForeground(new Color(0, 180, 255));
        infoSecreta.setOpaque(false);
        infoSecreta.setVisible(false);
        infoSecreta.setBounds(200, 70, 400, 80);

        layered.add(zonaSecreta, Integer.valueOf(1));
        layered.add(infoSecreta, Integer.valueOf(2));

        zonaSecreta.addMouseListener(new MouseAdapter() {
            Timer infoGlitch;

            @Override
            public void mouseEntered(MouseEvent e) {
                infoSecreta.setVisible(true);
                escribirTextoLento("[ACCESO RESTRINGIDO] Proyecto paralelo detectado... Archivo: RAGHAN.EXE", infoSecreta, 30);

                infoGlitch = new Timer(120, ev -> {
                    int dx = (int)(Math.random() * 4 - 2);
                    int dy = (int)(Math.random() * 4 - 2);
                    int r = 0;
                    int g = 180;
                    int b = 200 + (int)(Math.random() * 55);
                    infoSecreta.setForeground(new Color(r, g, b));
                    infoSecreta.setLocation(200 + dx, 50 + dy);
                });
                infoGlitch.start();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                infoSecreta.setVisible(false);
                if (textoTimer != null && textoTimer.isRunning()) {
                    textoTimer.stop();
                }
                if (infoGlitch != null) {
                    infoGlitch.stop();
                }
                infoSecreta.setLocation(200, 70);
                infoSecreta.setForeground(new Color(0, 180, 255));
            }
        });
    }

    private void mostrarInfoDetallada(String nombre, String imagenRuta) {
        JPanel panel = new JPanel(new BorderLayout(15, 10));
        panel.setBackground(new Color(20, 20, 35));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel imagenLabel = new JLabel();
        imagenLabel.setPreferredSize(new Dimension(120, 120));
        ImageIcon imagenGrande = crearImagenCircularElegante(imagenRuta, 120, 120);
        imagenLabel.setIcon(imagenGrande);

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);

        String infoExtra = switch (nombre) {
            case "Majo" -> """
                <html>
                <div style='font-family: Segoe UI; color: white;'>
                <b style='color: #00dcff; font-size: 16px;'>María José Martínez Fernandez</b><br><br>
                🎂 <b>Edad:</b> 18 años<br>
                🎓 <b>Carrera:</b> Ciencia de Datos - 2° Semestre<br>
                ⚡ <b>Rol:</b> Gestora del proyecto<br>
                💖 <b>Número favorito:</b> 1997<br>
                💬 <b>Frase:</b> "Jungkook te amo."
                </div>
                </html>
                """;
            case "Karla" -> """
                <html>
                <div style='font-family: Segoe UI; color: white;'>
                <b style='color: #00dcff; font-size: 16px;'>Karla Sofía Paredes Rojano</b><br><br>
                🎂 <b>Edad:</b> 18 años<br>
                🎓 <b>Carrera:</b> Ciencia de Datos - 2° Semestre<br>
                🎨 <b>Rol:</b> Diseñadora principal<br>
                💾 <b>Error recurrente:</b> 'SyntaxError: missing motivation'<br>
                💬 <b>Frase:</b> "Eche cole."
                </div>
                </html>
                """;
            case "Valen" -> """
                <html>
                <div style='font-family: Segoe UI; color: white;'>
                <b style='color: #00dcff; font-size: 16px;'>Valentina A.C.</b><br><br>
                🎂 <b>Edad:</b> 17 años<br>
                🎓 <b>Carrera:</b> Ciencia de Datos - 2° Semestre<br>
                🔧 <b>Rol:</b> Programadora principal<br>
                💀 <b>Nivel de estrés:</b> 404 Not Found<br>
                💬 <b>Frase:</b> "Quiero una salchipapa."
                </div>
                </html>
                """;
            case "Mau" -> """
                <html>
                <div style='font-family: Segoe UI; color: white;'>
                <b style='color: #00dcff; font-size: 16px;'>Mauricio José Orozco Porto</b><br><br>
                🎂 <b>Edad:</b> 18 años<br>
                🎓 <b>Carrera:</b> Ciencia de Datos - 2° Semestre<br>
                📝 <b>Rol:</b> Documentador técnico<br>
                🧠 <b>Lenguaje favorito:</b> no sé, español(?<br>
                💬 <b>Frase:</b> "Touché."
                </div>
                </html>
                """;
            default -> """
                <html>
                <div style='font-family: Segoe UI; color: white;'>
                <b style='color: #00dcff;'>Información no disponible</b>
                </div>
                </html>
                """;
        };

        JLabel infoLabel = new JLabel(infoExtra);
        infoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        infoPanel.add(infoLabel);

        panel.add(imagenLabel, BorderLayout.WEST);
        panel.add(infoPanel, BorderLayout.CENTER);

        JOptionPane.showMessageDialog(
            this,
            panel,
            "📁 Expediente de " + nombre,
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void escribirTextoLento(String texto, JLabel label, int velocidadMs) {
        if (textoTimer != null && textoTimer.isRunning()) {
            textoTimer.stop();
        }
        label.setText("");
        final int[] i = {0};
        textoTimer = new Timer(velocidadMs, e -> {
            if (i[0] <= texto.length()) {
                label.setText(texto.substring(0, i[0]));
                i[0]++;
            } else {
                textoTimer.stop();
            }
        });
        textoTimer.start();
    }
    
    
    
    private void mostrarInfoDetalladaDocente(String nombre, String imagenRuta) {
    JPanel panel = new JPanel(new BorderLayout(15, 10));
    panel.setBackground(new Color(20, 20, 35));
    panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

    JLabel imagenLabel = new JLabel();
    imagenLabel.setPreferredSize(new Dimension(120, 120));
    ImageIcon imagenGrande = crearImagenCircularElegante(imagenRuta, 120, 120);
    imagenLabel.setIcon(imagenGrande);

    JPanel infoPanel = new JPanel();
    infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
    infoPanel.setOpaque(false);

    String infoExtra = switch (nombre) {
        case "Leddy Diana Pájaro" -> """
            <html>
            <div style='font-family: Segoe UI; color: white;'>
            <b style='color: #ffd700; font-size: 16px;'>Leddy Diana Pájaro</b><br><br>
            🎂 <b>Cumpleaños:</b> 7 de diciembre<br>
            🎓 <b>Rol:</b> Profesora de Algoritmia y Programación II<br>
            📚 <b>Asignatura:</b> Algoritmia y Programación II<br><br>
            💬 <b>Frases célebres:</b><br>
            • "Hey, esa es mala"<br>
            • "Eso es para que lo tengan en cuenta"<br>
            • "Whatever you want"
            </div>
            </html>
            """;
        case "Angélica Baños" -> """
            <html>
            <div style='font-family: Segoe UI; color: white;'>
            <b style='color: #ffd700; font-size: 16px;'>Angélica Baños</b><br><br>
            🎓 <b>Rol:</b> Monitora Académica<br>
            📚 <b>Área:</b> Soporte académico<br><br>
            💬 <b>Descripción:</b><br>
            Apoyo constante para el aprendizaje de los estudiantes,<br>
            facilitando la comprensión de conceptos y resolviendo dudas.
            </div>
            </html>
            """;
        default -> """
            <html>
            <div style='font-family: Segoe UI; color: white;'>
            <b style='color: #ffd700;'>Información no disponible</b>
            </div>
            </html>
            """;
    };

    JLabel infoLabel = new JLabel(infoExtra);
    infoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));

    infoPanel.add(infoLabel);

    panel.add(imagenLabel, BorderLayout.WEST);
    panel.add(infoPanel, BorderLayout.CENTER);

    JOptionPane.showMessageDialog(
        this,
        panel,
        "Expediente docente - " + nombre,
        JOptionPane.INFORMATION_MESSAGE
    );
}
    
    
    
    
    
    
    

    private Font getCyberFont(float size) {
        try {
            Font customFont = Font.createFont(
                Font.TRUETYPE_FONT,
                getClass().getResourceAsStream("/fonts/BadComa.ttf")
            );
            return customFont.deriveFont(Font.BOLD, size);
        } catch (Exception e) {
            return new Font("Segoe UI", Font.BOLD, (int) size);
        }
    }

//    private void MenuEstilo.reproducirSonido(String ruta) {
//        try {
//            System.out.println("🔊 Reproduciendo: " + ruta);
//        } catch (Exception e) {
//            System.err.println("❌ Error reproduciendo sonido: " + e.getMessage());
//        }
//    }
//
//    private void MenuEstilo.reproducirMusicaFondo(String ruta) {
//        try {
//            System.out.println("🎵 Reproduciendo música: " + ruta);
//        } catch (Exception e) {
//            System.err.println("❌ Error reproduciendo música: " + e.getMessage());
//        }
//    }



private void empezarEfectoEstatica(JPanel panel) {
    Timer staticTimer = new Timer(100, e -> {
        Graphics2D g2d = (Graphics2D) panel.getGraphics();
        if (g2d != null) {
            // Dibujar puntos aleatorios para simular estática
            g2d.setColor(new Color(100, 100, 150, 80));
            for (int i = 0; i < 10; i++) {
                int x = (int)(Math.random() * panel.getWidth());
                int y = (int)(Math.random() * panel.getHeight());
                int size = (int)(Math.random() * 3) + 1;
                g2d.fillRect(x, y, size, size);
            }
            g2d.dispose();
        }
    });
    staticTimer.start();
    panel.putClientProperty("staticTimer", staticTimer);
}

// MÉTODO PARA DETENER ESTÁTICA
private void detenerEfectoEstatica(JPanel panel) {
    Timer staticTimer = (Timer) panel.getClientProperty("staticTimer");
    if (staticTimer != null) {
        staticTimer.stop();
    }
    panel.repaint(); // Redibujar para limpiar la estática
}

// MÉTODO ALTERNATIVO PARA ESTÁTICA MÁS SIMPLE
private void empezarEfectoEstaticaSimple(JPanel panel) {
    Timer staticTimer = new Timer(200, e -> {
        // Cambiar ligeramente el color de fondo para el efecto de parpadeo
        int variation = (int)(Math.random() * 10) - 5;
        Color newColor = new Color(
            Math.max(0, Math.min(255, 25 + variation)),
            Math.max(0, Math.min(255, 25 + variation)), 
            Math.max(0, Math.min(255, 40 + variation))
        );
        panel.setBackground(newColor);
    });
    staticTimer.start();
    panel.putClientProperty("staticTimer", staticTimer);
}








}