import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.swing.border.EmptyBorder;
import java.awt.Toolkit;
import java.awt.Cursor;
import java.awt.Point;

public class MenuEstilo extends JFrame {
    static Clip musicaFondo;
    static Map<String, Clip> efectosSonido = new HashMap<>();
    JPanel menuPanel;  // Panel principal del menú
    private JPanel creditsPanel; // Panel de créditos
    private JPanel manualPanel; // Panel de Manual
    private JPanel gamesPanel; //Panel de juego 
    private JPanel tutorialPanel; // Panel de "¿Cómo jugar?"
    private StoryState storyState = new StoryState(); //acá llevaremos cuenta del avance de los capítulos de los juegos en términos de narrativa.

  
    public MenuEstilo() {
        setTitle("Zucaritas");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        setIconImage(new ImageIcon("images/logo.png").getImage());

        // CURSOR PERSONALIZADO
        ImageIcon cursorIcon = new ImageIcon(getClass().getResource("/images/cursor.png"));
        Image cursorImage = cursorIcon.getImage();
        Cursor customCursor = Toolkit.getDefaultToolkit().createCustomCursor(
                cursorImage,
                new Point(0, 0),
                "CursorPersonalizado"
        );
        this.setCursor(customCursor);


        menuPanel = createMenuPanel();
        add(menuPanel);
        setVisible(true);
    }

    
    
    
   public MenuEstilo(StoryState storyState) {
    this.storyState = storyState;
    setTitle("Zucaritas");
    setSize(1000, 600);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLocationRelativeTo(null);
    setResizable(false);
    setIconImage(new ImageIcon("images/logo.png").getImage());

    // Cursor
    ImageIcon cursorIcon = new ImageIcon(getClass().getResource("/images/cursor.png"));
    Image cursorImage = cursorIcon.getImage();
    Cursor customCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImage, new Point(0, 0), "CursorPersonalizado");
    this.setCursor(customCursor);

    menuPanel = createMenuPanel();
    add(menuPanel);
    setVisible(true);
}

    
    
    
    public JPanel createMenuPanel() {
        
        // Otro panel con un layout absoluto para superponer el fondo y los botones. 
        //El layout absoluto sirve para para colocar los componentes exactamente
        // donde quieres que estén dentro de un contenedor (el Jframe)       
        JPanel panel = new JPanel(null);

        // Fondo animado
        JLabel fondoGif = new JLabel(new ImageIcon(getClass().getResource("/images/Fondo3.gif")));
        fondoGif.setBounds(0, 0, 1000, 600);
        panel.add(fondoGif); // va al fondo

        // Este es que panel que contendrá el título y botones y va encima del fondo
        JPanel contenido = new JPanel(new GridBagLayout());
        contenido.setOpaque(false); // solo este panel es transparente, los botones no lo son
        contenido.setBounds(0, 0, 1000, 600);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.CENTER;

//        // Título del juego
        JLabel titulo = new JLabel("CYBERSTEAL - THE FIREWALL SAGA");
        try {
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("/fonts/BadComa.ttf"));
            customFont = customFont.deriveFont(Font.PLAIN, 41f);
            titulo.setFont(customFont);
            titulo.setOpaque(true);

               gbc.gridy = 0;
        gbc.insets = new Insets(20, 0, 40, 0);
        contenido.add(titulo, gbc);
            
        } catch (Exception e) {
            System.out.println("No se pudo cargar la fuente, usando Arial.");
            titulo.setFont(new Font("Arial", Font.BOLD, 48));
            //Acá si hay algún problema con la fuente, simplemente se pondrá arial automáticamente
        }

        titulo.setForeground(Color.RED);
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        titulo.setOpaque(true); // sólido
        titulo.setBackground(Color.BLACK);
        titulo.setPreferredSize(new Dimension(830, 100));

        gbc.gridy = 0;
        gbc.insets = new Insets(30, 1, 50, 0);
        contenido.add(titulo, gbc);

        reproducirMusicaFondo("sonidos/musicaelec.wav");

        Cursor miCursor = CursorUtils.crearCursor("/images/cursor.png", 40, 40);
// Aplicarlo a todo el JFrame
        this.setCursor(miCursor);

        // Botones del menú
        String[] opciones = {
            "Iniciar juego",
            "¿Cómo jugar?",
            "Créditos",
            "Salir",};
  
        gbc.insets = new Insets(10, 0, 10, 0);
        for (String texto : opciones) {
            JLabel label = new JLabel(texto, SwingConstants.CENTER);
            label.setFont(new Font("Verdana", Font.BOLD, 26));
            label.setForeground(Color.WHITE);
            label.setOpaque(true); // SÓLIDOS
            label.setBackground(Color.BLACK);
            label.setPreferredSize(new Dimension(400, 50));
            
            
          



// Panel superpuesto transparente
JPanel overlayPanel = new JPanel(null);
overlayPanel.setOpaque(false);
overlayPanel.setPreferredSize(new Dimension(1000, 600));

// Cargar y escalar los iconos
ImageIcon soundOnIcon = new ImageIcon(
    new ImageIcon(getClass().getResource("/images/sonido.png"))
        .getImage().getScaledInstance(28, 28, Image.SCALE_SMOOTH)
);

ImageIcon soundOffIcon = new ImageIcon(
    new ImageIcon(getClass().getResource("/images/sinsonido.png"))
        .getImage().getScaledInstance(28, 28, Image.SCALE_SMOOTH)
);

// Crear botón con icono inicial
JButton soundButton = new JButton(soundOnIcon);
soundButton.setBackground(Color.BLACK);
soundButton.setFocusPainted(false);
soundButton.setBorderPainted(false);
soundButton.setContentAreaFilled(false); // transparente
soundButton.setBounds(920, 440, 50, 40); // x, y, ancho, alto

// funciona para alternar icono y música
soundButton.addActionListener(e -> {
    if (soundButton.getIcon() == soundOnIcon) {
        soundButton.setIcon(soundOffIcon);
        if (musicaFondo != null && musicaFondo.isRunning()) {
            musicaFondo.stop();
        }
    } else {
        soundButton.setIcon(soundOnIcon);
        if (musicaFondo != null) {
            musicaFondo.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }
});

// Añadir al panel principal
panel.add(soundButton);
panel.setComponentZOrder(soundButton, 0);


// BOTÓN ¿CÓMO FUNCIONA? debajo del de música
JButton howToButton = new JButton("i");
howToButton.setFont(new Font("Arial", Font.PLAIN, 22));
howToButton.setBackground(Color.BLACK);
howToButton.setForeground(Color.RED);
howToButton.setFocusPainted(false);
howToButton.setBounds(920, 500, 50, 40);
howToButton.addActionListener(e -> {
    mostrarManual();
});
panel.add(howToButton);
panel.setComponentZOrder(howToButton, 0);


            
            
            
//Efectos muy kul
      label.addMouseListener(new java.awt.event.MouseAdapter() {

    int originalWidth;
    int originalHeight;
    int originalX;
    int originalY;
    boolean sizeSaved = false;

    @Override
    public void mouseEntered(java.awt.event.MouseEvent e) {
        label.setBackground(new Color(120, 0, 0));
        reproducirSonido("/sonidos/hover.wav");

        // Guarda el tamaño y posición solo la primera vez
        if (!sizeSaved) {
            originalWidth = label.getWidth();
            originalHeight = label.getHeight();
            originalX = label.getX();
            originalY = label.getY();
            sizeSaved = true;
        }

        // Animación de que crece un poquito
        new Thread(() -> {
            for (int i = 0; i < 4; i++) {
                int w = label.getWidth() + 2;
                int h = label.getHeight() + 1;
                label.setBounds(label.getX() - 1, label.getY() - 1, w, h);
                try { Thread.sleep(10); } catch (InterruptedException ex) {}
            }
        }).start();
    }

    @Override
    public void mouseExited(java.awt.event.MouseEvent e) {
        label.setBackground(Color.BLACK);
        label.setBounds(originalX, originalY, originalWidth, originalHeight);
    }

    @Override
    public void mousePressed(java.awt.event.MouseEvent e) {
        //  se hunde un poco al presionar
        label.setLocation(label.getX() + 2, label.getY() + 2);
        label.setBackground(new Color(80, 0, 0));
    }

    @Override
    public void mouseReleased(java.awt.event.MouseEvent e) {
        label.setLocation(originalX, originalY);
        label.setBackground(new Color(120, 0, 0));
    }

    @Override
    public void mouseClicked(java.awt.event.MouseEvent e) {
        MenuEstilo.reproducirSonido("sonidos/click.wav");

        if (texto.equals("Salir")) {
            System.exit(0);
        } else if (texto.equals("Créditos")) {
                   detenerMusicaFondo();
                    reproducirMusicaFondo("sonidos/down.wav");
            mostrarCreditos();
        } else if (texto.equals("Iniciar juego")) {
            mostrarJuegos();
        } else if (texto.equals("¿Cómo jugar?")) {
            mostrarTutorial();
        } else {
            mostrarManual();
        }
    }
});

            gbc.gridy++;
            contenido.add(label, gbc);
        }

        // agregar contenido de´púes del fondo para que quede arriba
        panel.add(contenido);
        panel.setComponentZOrder(contenido, 0); // así aseguramos así que todo quede por encima del GIF

        return panel;
        
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
//Nuevo panel para el menú de créditos
private void mostrarCreditos() {
    if (creditsPanel == null) {
        creditsPanel = new CreditosPanel(() -> {
            getContentPane().removeAll();
            add(menuPanel);
            revalidate();
            repaint();
        });
    }

    getContentPane().removeAll();
    add(creditsPanel);
    revalidate();
    repaint();
}


    public static void main(String[] args) {
        SwingUtilities.invokeLater(MenuEstilo::new);
    }    
    
    //Esto está en otra clase
    
    
    


   
    
    
    
    

     private void mostrarJuegos() {
    if (gamesPanel == null) {
        // Panel con fondo degradado animado y bonito
        gamesPanel = new JPanel() {
            private float hue = 0f;

            {
                // Timer para animar el degradado
                new javax.swing.Timer(50, e -> {
                    hue += 0.002f;
                    if (hue > 1) hue = 0;
                    repaint();
                }).start();
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                Color c1 = Color.getHSBColor(hue, 0.6f, 0.4f);
                Color c2 = Color.getHSBColor((hue + 0.2f) % 1, 0.6f, 0.6f);
                GradientPaint gp = new GradientPaint(0, 0, c1, getWidth(), getHeight(), c2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        gamesPanel.setLayout(new BorderLayout());

        // Título
JLabel titulo = new JLabel("SELECCIONA TU MISION", SwingConstants.CENTER);
titulo.setFont(getBadComaFont(50f)); // usamos la fuente personalizada
titulo.setForeground(Color.RED);
titulo.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));
gamesPanel.add(titulo, BorderLayout.NORTH);
    


// Barra de progreso de la historia
JProgressBar barraProgreso = new JProgressBar(0, 3);
barraProgreso.setValue(storyState.getCapitulosCompletados());
barraProgreso.setBounds(300, 100, 400, 20); // x, y, ancho, alto
barraProgreso.setForeground(new Color(0, 255, 100)); // verde brillante
barraProgreso.setBackground(Color.DARK_GRAY);
barraProgreso.setBorderPainted(false);
barraProgreso.setStringPainted(true); // muestra el texto tipo "2/3"
barraProgreso.setFont(new Font("Arial", Font.BOLD, 14));
barraProgreso.setString(storyState.getCapitulosCompletados() + "/3");

gamesPanel.add(barraProgreso);





        // Panel de juegos
        JPanel juegosContainer = new JPanel(new GridLayout(3, 1, 20, 20));
        juegosContainer.setOpaque(false); // transparente para ver el fondo
        juegosContainer.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        // Datos de cada juego o sea nombre, descripción, color e ícono
        String[][] juegos = {
            {"Lock & Code", "Defiende los sistemas del malvado Lord Baldomero.", "rojo", "🔒"},
            {"The Zero-Trust", "Protege con la política de cero confianza.", "amarillo", "🛡"},
            {"Phishing For Gold", "Evita que roben el banco con técnicas de phishing.", "azul", "🐟️"}
        };
       
        

        for (String[] juego : juegos) {
            String nombre = juego[0];
            String descripcion = juego[1];
            String color = juego[2];
            String icono = juego[3];

            JPanel panelJuego = new JPanel(new BorderLayout()) {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                }
            };
            panelJuego.setPreferredSize(new Dimension(900, 120));
            panelJuego.setBackground(new Color(20, 20, 20, 200));
            panelJuego.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
            panelJuego.setOpaque(true);

            JLabel nombreLabel = new JLabel(icono + " " + nombre, SwingConstants.LEFT);
            nombreLabel.setFont(new Font("Verdana", Font.BOLD, 26));
            nombreLabel.setForeground(Color.WHITE);
            nombreLabel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10));

            JTextArea descripcionArea = new JTextArea(descripcion);
            descripcionArea.setFont(new Font("Monospaced", Font.PLAIN, 18));
            descripcionArea.setForeground(Color.LIGHT_GRAY);
            descripcionArea.setBackground(new Color(0, 0, 0, 0));
            descripcionArea.setLineWrap(true);
            descripcionArea.setWrapStyleWord(true);
            descripcionArea.setEditable(false);
            descripcionArea.setVisible(false);
            descripcionArea.setBorder(BorderFactory.createEmptyBorder(5, 20, 10, 20));

            // Hover
       panelJuego.addMouseListener(new java.awt.event.MouseAdapter() {
    @Override
    public void mouseEntered(java.awt.event.MouseEvent e) {
        reproducirSonido("sonidos/hover.wav");
        switch (color) {
            case "rojo": panelJuego.setBackground(new Color(150, 0, 0, 220)); break;
            case "amarillo": panelJuego.setBackground(new Color(180, 180, 0, 220)); break;
            case "azul": panelJuego.setBackground(new Color(0, 100, 200, 220)); break;
        }
        panelJuego.setBorder(BorderFactory.createLineBorder(Color.CYAN, 3));
        nombreLabel.setFont(new Font("Verdana", Font.BOLD, 28));
        descripcionArea.setVisible(true);
        panelJuego.revalidate();
        panelJuego.repaint();
    }

    @Override
    public void mouseExited(java.awt.event.MouseEvent e) {
 
        panelJuego.setBackground(new Color(20, 20, 20, 200)); 
        panelJuego.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        nombreLabel.setFont(new Font("Verdana", Font.BOLD, 26));
        descripcionArea.setVisible(false);
    }
    
    
    @Override

public void mouseClicked(java.awt.event.MouseEvent e) {
    reproducirSonido("sonidos/click.wav");

    if (nombre.equals("Lock & Code")) {
        new minijuego1(storyState);
    } else if (nombre.equals("The Zero-Trust")) {
        new minijuego2(storyState);
    } else if (nombre.equals("Phishing For Gold")) {
        new minijuego3(storyState);
    }

    dispose();
}

       //La idea es que sea un diseño modular con progresión narrativa acumulativa 
       //cada capítulo es independiente (jugable en cualquier orden), 
       //pero al completarlos en secuencia el jugador desbloquea más capas de historia y ambientación
     
});
 
            panelJuego.add(nombreLabel, BorderLayout.NORTH);
            panelJuego.add(descripcionArea, BorderLayout.CENTER);
            juegosContainer.add(panelJuego);

        }

        // Botón de volver
        JButton back = new JButton("← Volver al Menú Principal");
        back.setFont(new Font("Arial", Font.BOLD, 22));
        back.setBackground(Color.BLACK);
        back.setForeground(Color.red);
        back.setFocusPainted(false);
        back.setBorder(BorderFactory.createLineBorder(Color.CYAN, 6));
        back.addActionListener(e -> {
            reproducirSonido("sonidos/click.wav");
            getContentPane().removeAll();
            add(menuPanel);
            revalidate();
            repaint();
        });

        JPanel southPanel = new JPanel();
        southPanel.setOpaque(false);
        southPanel.add(back);

        gamesPanel.add(juegosContainer, BorderLayout.CENTER);
        gamesPanel.add(southPanel, BorderLayout.SOUTH);
    }

    // Función para abrir el menú
    JOptionPane.showMessageDialog(null,
        "Bienvenido.\nSelecciona tu misión para proteger el ciberespacio.",
        "Centro de Control",
        JOptionPane.INFORMATION_MESSAGE);

    getContentPane().removeAll();
    add(gamesPanel);
    revalidate();
    repaint();
}
   
    
    
   


private void mostrarTutorial() {
    // Permitimos redimensionar en el tutorial para que el usuario pueda maximizar si quiere
    setResizable(true);

    // Contenido principal
    JPanel contenido = new JPanel();
    contenido.setLayout(new BoxLayout(contenido, BoxLayout.Y_AXIS));
    contenido.setOpaque(false);
    contenido.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
    // Tamaño base ancho x alto, lo hacemos largo para forzar scroll vertical si es necesario
    contenido.setPreferredSize(new Dimension(1000, 1400)); 

    // Título
    JLabel titulo = new JLabel("Tutorial de Juegos");
    titulo.setFont(getCustomFont(34f));
    titulo.setForeground(Color.WHITE);
    titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
    contenido.add(titulo);
    contenido.add(Box.createRigidArea(new Dimension(0, 20)));

    // Juego 1
    JLabel j1 = new JLabel("Juego #1: \"Lock & Code\"");
    j1.setFont(new Font("Arial", Font.BOLD, 22));
    j1.setForeground(Color.YELLOW);
    j1.setAlignmentX(Component.LEFT_ALIGNMENT);
    contenido.add(j1);
    contenido.add(Box.createRigidArea(new Dimension(0, 6)));

    JTextArea desc1 = new JTextArea(
        "En este juego, un villano intentará robar contraseñas de un banco en crisis.\n" +
        "El jugador, en el rol de programador, deberá generar contraseñas cada vez más seguras para evitar el robo.\n\n" +
        "Conceptos usados:\n" +
        " • Cadenas (strings) → para construir contraseñas con letras, números y símbolos.\n" +
        " • Subrutinas → para generar y validar contraseñas de forma modular.\n" +
        " • Estructuras de control → pausar/avanzar niveles y aumentar la complejidad.\n\n" +
        "Ejemplo de reto: en cada ronda debes añadir o sustituir caracteres para que la contraseña cumpla\n" +
        "mínimos de longitud, mayúsculas, números y símbolos. Si fallas, el villano roba un fragmento."
    );
    configurarTexto(desc1);
    contenido.add(desc1);

    contenido.add(Box.createRigidArea(new Dimension(0, 20)));

    // Juego 2
    JLabel j2 = new JLabel("Juego #2: \"The Zero-Trust\"");
    j2.setFont(new Font("Arial", Font.BOLD, 22));
    j2.setForeground(Color.CYAN);
    j2.setAlignmentX(Component.LEFT_ALIGNMENT);
    contenido.add(j2);
    contenido.add(Box.createRigidArea(new Dimension(0, 6)));

    JTextArea desc2 = new JTextArea(
        "Tras la derrota del villano, éste libera un virus en el banco. El jugador debe recorrer la oficina enemiga,\n" +
        "reunir fragmentos y ensamblar una \"vacuna\" que defenderá la infraestructura.\n\n" +
        "Conceptos usados:\n" +
        " • Cadenas → pistas y mensajes ocultos que debes interpretar.\n" +
        " • Vectores y matrices → inventario y mapas donde aparecen objetos clave.\n" +
        " • Subrutinas → etapas como explorar, abrir archivos y descifrar códigos.\n" +
        " • Funciones → validar elecciones y determinar si estás más cerca de la vacuna.\n\n" +
        "Tips: inspecciona archivos, compara fragmentos y guarda progresos entre niveles."
    );
    configurarTexto(desc2);
    contenido.add(desc2);

    contenido.add(Box.createRigidArea(new Dimension(0, 20)));

    // Juego 3
    JLabel j3 = new JLabel("Juego #3: \"Phishing for Gold\"");
    j3.setFont(new Font("Arial", Font.BOLD, 22));
    j3.setForeground(Color.ORANGE);
    j3.setAlignmentX(Component.LEFT_ALIGNMENT);
    contenido.add(j3);
    contenido.add(Box.createRigidArea(new Dimension(0, 6)));

    JTextArea desc3 = new JTextArea(
        "Ahora, convertido en directivo del banco, debes identificar mensajes dañinos disfrazados de correos legítimos.\n" +
        "Detectarás intentos de phishing aplicando reglas y consejos ofrecidos al inicio de cada nivel.\n\n" +
        "Conceptos usados:\n" +
        " • Cadenas → análisis de remitentes, enlaces y textos.\n" +
        " • Matrices / vectores → almacenar y clasificar mensajes por riesgo.\n" +
        " • Condicionales → reglas que deciden si un mensaje es seguro o falso.\n" +
        " • Funciones → llevar control de aciertos y errores para calcular tu puntuación.\n\n" +
        "Consejo: revisa encabezados, enlaces sospechosos y errores de ortografía: suelen delatar fraudes."
    );
    configurarTexto(desc3);
    contenido.add(desc3);

    // espacio extra para que el botón no quede pegado al final
    contenido.add(Box.createRigidArea(new Dimension(0, 18)));

    // --- Botón de volver
    JButton backButton = new JButton("← Volver al Menú");
    backButton.setFont(new Font("Arial", Font.BOLD, 20));
    backButton.setPreferredSize(new Dimension(260, 60));
    backButton.setMaximumSize(new Dimension(260, 60));
    backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    backButton.addActionListener(e -> {
        // Volver al menú y restaurar comportamiento fijo
        getContentPane().removeAll();
        getContentPane().add(menuPanel);
        setSize(1000, 600);
        setResizable(false);
        setLocationRelativeTo(null);
        revalidate();
        repaint();
    });

    // Agregamos el botón antes de un espaciador grande para que quede más arriba.
    contenido.add(backButton);

    // Espacio final largo para que si el panel se muestra completo el botón no quede pegado abajo.
    contenido.add(Box.createRigidArea(new Dimension(0, 300)));

    //centrar
    JPanel centerWrapper = new JPanel(new GridBagLayout());
    centerWrapper.setOpaque(false);
    centerWrapper.add(contenido);

    JScrollPane scrollPane = new JScrollPane(centerWrapper);
    scrollPane.setBorder(null);
    scrollPane.getViewport().setBackground(Color.BLACK);
    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

    // Mostrar en la ventana
    getContentPane().removeAll();
    getContentPane().add(scrollPane);
    setSize(1000, 600);
    setLocationRelativeTo(null);
    revalidate();
    repaint();
}

// Auxiliar
private void configurarTexto(JTextArea area) {
    area.setFont(new Font("Arial", Font.PLAIN, 16));
    area.setForeground(Color.WHITE);
    area.setOpaque(false);
    area.setLineWrap(true);
    area.setWrapStyleWord(true);
    area.setEditable(false);
    area.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
    area.setAlignmentX(Component.LEFT_ALIGNMENT);
    // que la caja pueda crecer en alto pero no exceder el ancho base
    area.setMaximumSize(new Dimension(1000, Short.MAX_VALUE));
    area.setPreferredSize(new Dimension(1000, 120)); // hint inicial por bloque
}






    private void mostrarManual() {
    if (manualPanel == null) {
        // Panel principal donde guardaremos el scroll
        manualPanel = new JPanel(new BorderLayout());
        manualPanel.setBackground(Color.BLACK);

        //Contenido
        JPanel contenido = new JPanel();
        contenido.setLayout(new BoxLayout(contenido, BoxLayout.Y_AXIS));
        contenido.setOpaque(false);
        contenido.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
contenido.setMaximumSize(new Dimension(1000, Integer.MAX_VALUE));

        // Título
        JLabel titulo = new JLabel("Manual técnico del código");
        titulo.setFont(getCustomFont(30f));
        titulo.setForeground(Color.WHITE);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        contenido.add(titulo);
        contenido.add(Box.createRigidArea(new Dimension(0, 18)));

        // --- Sección: Resumen general ---
        JLabel sResumen = new JLabel("Resumen general");
        sResumen.setFont(new Font("Arial", Font.BOLD, 20));
        sResumen.setForeground(Color.YELLOW);
        sResumen.setAlignmentX(Component.LEFT_ALIGNMENT);
        contenido.add(sResumen);
        contenido.add(Box.createRigidArea(new Dimension(0, 6)));

        JTextArea resumen = new JTextArea(
            "Esta clase se llama`MenuEstilo` e implementa un menú principal para seleccionar varios mini-juegos. " +
            "Usa Swing para la UI, `Clip`/`AudioSystem` para audio, y varios paneles " +
            "(menu, créditos, juegos, tutorial, manual). El patrón usado es perezoso, " +
            "esto significa cada subpanel se crea solo la primera vez que se necesita (if == null)."
        );
        configurarTexto(resumen);
        contenido.add(resumen);
        contenido.add(Box.createRigidArea(new Dimension(0, 14)));

        // --- Sección: Imports y recursos ---
        JLabel sImports = new JLabel("Imports y recursos");
        sImports.setFont(new Font("Arial", Font.BOLD, 20));
        sImports.setForeground(Color.CYAN);
        sImports.setAlignmentX(Component.LEFT_ALIGNMENT);
        contenido.add(sImports);
        contenido.add(Box.createRigidArea(new Dimension(0, 6)));

        JTextArea imports = new JTextArea(
            "- `javax.swing` / `java.awt`: componentes gráficos, layouts, eventos.\n" +
            "- `javax.sound.sampled`: para cargar y reproducir clips (`AudioInputStream`, `Clip`, `FloatControl`).\n" +
            "- Recursos no java (fuentes, imágenes y sonidos) se obtienen vía `getResource(...)` para funcionar dentro de la clase.\n" +
            "Esta parte se hizo desde el explorador de archivos del computador, no desde netbeans, cargando los recursos requeridos dentro de la carpeta \n" +
            "resources, que sigue esta ruta: (NetBeansProjects\\LABORATORIO\\MenuEstilo\\src\\main\\resources)\n\n"     
        );
        configurarTexto(imports);
        contenido.add(imports);
        contenido.add(Box.createRigidArea(new Dimension(0, 14)));

        // --- Sección: Campos estáticos y mapas ---
        JLabel sCampos = new JLabel("Campos estáticos y mapas");
        sCampos.setFont(new Font("Arial", Font.BOLD, 20));
        sCampos.setForeground(Color.CYAN);
        sCampos.setAlignmentX(Component.LEFT_ALIGNMENT);
        contenido.add(sCampos);
        contenido.add(Box.createRigidArea(new Dimension(0, 6)));

        JTextArea campos = new JTextArea(
            "- `static Clip musicaFondo;` : clip global para la música de fondo. Se abre, se ajusta volumen con `FloatControl` \n" +
            "y se reproduce en loop hasta poner el comando que lo detiene." +       
            "- `static Map<String, Clip> efectosSonido` : este mapa está pensado para cachear efectos y evitar recargas repetidas.\n" +
                "Nota: Cachear es básicamente guardar algo que ya cargaste para no tener que volver a cargarlo.\n\n"
        );
        configurarTexto(campos);
        contenido.add(campos);
        contenido.add(Box.createRigidArea(new Dimension(0, 14)));

        // --- Sección: Constructor (inicialización del JFrame) ---
        JLabel sConstructor = new JLabel("Constructor (inicialización del JFrame)");
        sConstructor.setFont(new Font("Arial", Font.BOLD, 20));
        sConstructor.setForeground(Color.ORANGE);
        sConstructor.setAlignmentX(Component.LEFT_ALIGNMENT);
        contenido.add(sConstructor);
        contenido.add(Box.createRigidArea(new Dimension(0, 6)));

        JTextArea constructorTxt = new JTextArea(
            "- `setTitle`, `setSize`, `setDefaultCloseOperation`, `setLocationRelativeTo(null)`: para la configuración básica del frame.\n" +
            "- `setResizable(false)` en el menú principal: impide redimensionado (el tutorial y el manual son las únicas excepciones).\n" +
            "- Cursor personalizado: se cargó la imagen con `getResource` y se creó con `Toolkit.createCustomCursor(...)`.\n" +
            "- `menuPanel = createMenuPanel(); add(menuPanel); setVisible(true);` : se muestra el menú principal."
        );
        configurarTexto(constructorTxt);
        contenido.add(constructorTxt);
        contenido.add(Box.createRigidArea(new Dimension(0, 14)));

        // --- Sección: createMenuPanel (estructura y comportamiento) ---
        JLabel sMenu = new JLabel("createMenuPanel() — Estructura y comportamiento");
        sMenu.setFont(new Font("Arial", Font.BOLD, 20));
        sMenu.setForeground(Color.ORANGE);
        sMenu.setAlignmentX(Component.LEFT_ALIGNMENT);
        contenido.add(sMenu);
        contenido.add(Box.createRigidArea(new Dimension(0, 6)));

        JTextArea menuTxt = new JTextArea(
            "- Se usa `JPanel(null)` (layout absoluto) para poder colocar el GIF de fondo con coordenadas fijas y superponer un contenido transparente.\n" +
            "- `fondoGif` (JLabel con ImageIcon) se coloca primero, luego se añade `contenido` y se fuerza su orden para que quede encima.\n" +
            "- `contenido` usa `GridBagLayout` para centrar título y los botones; cada botón es un `JLabel` con `MouseListener` para efectos de sonido de hover y click.\n" +
            "- Manejo de eventos: `mouseEntered` cambia color; `mouseClicked` reproduce sonidos y llama a métodos como `mostrarTutorial()` o `mostrarCreditos()`."
        );
        configurarTexto(menuTxt);
        contenido.add(menuTxt);
        contenido.add(Box.createRigidArea(new Dimension(0, 14)));

        // --- Sección: Patrones en mostrarX (lazy init y volver) ---
        JLabel sLazy = new JLabel("Patrón: creación perezosa en mostrarX()");
        sLazy.setFont(new Font("Arial", Font.BOLD, 20));
        sLazy.setForeground(Color.YELLOW);
        sLazy.setAlignmentX(Component.LEFT_ALIGNMENT);
        contenido.add(sLazy);
        contenido.add(Box.createRigidArea(new Dimension(0, 6)));

        JTextArea lazyTxt = new JTextArea(
            "- Cada método `mostrarCreditos()`, `mostrarJuegos()` o `mostrarTutorial()` verifica `if (panel == null)` y crea el panel solo la primera vez.\n" +
            "- Esto ahorra memoria y tiempo de inicio. Al volver, se hace `getContentPane().removeAll(); add(menuPanel); revalidate(); repaint();`."
        );
        configurarTexto(lazyTxt);
        contenido.add(lazyTxt);
        contenido.add(Box.createRigidArea(new Dimension(0, 14)));

        // --- Sección: Audio (reproducirMusicaFondo / reproducirSonido) ---
        JLabel sAudio = new JLabel("Audio: reproducirMusicaFondo(), reproducirSonido()");
        sAudio.setFont(new Font("Arial", Font.BOLD, 20));
        sAudio.setForeground(Color.CYAN);
        sAudio.setAlignmentX(Component.LEFT_ALIGNMENT);
        contenido.add(sAudio);
        contenido.add(Box.createRigidArea(new Dimension(0, 6)));

        JTextArea audioTxt = new JTextArea(
            "- `AudioSystem.getAudioInputStream(...)` abre el stream desde recursos.\n" +
            "- `Clip` se abre (`clip.open(audioIn)`), se controla el volumen con `FloatControl` y se inicia con `start()` o `loop(...)`.\n" +
            "- Manejo de errores: se capturan excepciones y se muestran mensajes con `JOptionPane` o se imprime el stacktrace.\n" +
            "- Buenas prácticas: cerrar clips (`clip.close()`) cuando terminan y usar `LineListener` para liberar recursos."
        );
        configurarTexto(audioTxt);
        contenido.add(audioTxt);
        contenido.add(Box.createRigidArea(new Dimension(0, 14)));

        // --- Sección: CursorUtils ---
        JLabel sCursor = new JLabel("CursorUtils (crearCursor)");
        sCursor.setFont(new Font("Arial", Font.BOLD, 20));
        sCursor.setForeground(Color.ORANGE);
        sCursor.setAlignmentX(Component.LEFT_ALIGNMENT);
        contenido.add(sCursor);
        contenido.add(Box.createRigidArea(new Dimension(0, 6)));

        JTextArea cursorTxt = new JTextArea(
            "- Método utilitario que carga una imagen desde recursos y la redimensiona con `getScaledInstance`.\n" +
            "- Crea un cursor con `Toolkit.createCustomCursor(image, new Point(0,0), name)`.\n" +
            "- Si falla, retorna `Cursor.getDefaultCursor()` (fallback seguro)."
        );
        configurarTexto(cursorTxt);
        contenido.add(cursorTxt);
        contenido.add(Box.createRigidArea(new Dimension(0, 14)));

        // --- Sección: EscalablePanel (explicación técnica) ---
        JLabel sEscalable = new JLabel("EscalablePanel (paintComponent override)");
        sEscalable.setFont(new Font("Arial", Font.BOLD, 20));
        sEscalable.setForeground(Color.CYAN);
        sEscalable.setAlignmentX(Component.LEFT_ALIGNMENT);
        contenido.add(sEscalable);
        contenido.add(Box.createRigidArea(new Dimension(0, 6)));

        JTextArea escalableTxt = new JTextArea(
            "- `EscalablePanel extends JPanel` contiene un contenido que se coloca con `setLayout(null)`.\n" +
            "- En `paintComponent(Graphics g)` se calcula una `scale = min(currentW/baseW, currentH/baseH)` para mantener la proporción.\n" +
            "- Se calcula el nuevo tamaño `newW = baseW * scale`, `newH = baseH * scale` y se centra con offsets `(x,y)`.\n" +
            "- Finalmente se llama `contenido.setBounds(x,y,newW,newH)` para que todo el panel siguiente se redimensione como una sola unidad.\n" +
            "- Así se logra que el panel haga zoom proporcionalmente sin deformar sus elementos para que no se vea feo."
        );
        configurarTexto(escalableTxt);
        contenido.add(escalableTxt);
        contenido.add(Box.createRigidArea(new Dimension(0, 18)));

        // --- Funcionamiento de los juegos (usa exactamente tus textos) ---
        JLabel sJuegos = new JLabel("Funcionamiento de los juegos (idea)");
        sJuegos.setFont(new Font("Arial", Font.BOLD, 20));
        sJuegos.setForeground(Color.PINK);
        sJuegos.setAlignmentX(Component.LEFT_ALIGNMENT);
        contenido.add(sJuegos);
        contenido.add(Box.createRigidArea(new Dimension(0, 6)));

        // --- Bloque Juego 1
JTextArea juego1Txt = new JTextArea(
    "Juego #1: Lock & Code\n" +
    "   - Cadenas (strings): se implementan como variables que almacenan contraseñas. El jugador modifica estas cadenas agregando o sustituyendo caracteres.\n" +
    "   - Subrutinas: funciones que generan contraseñas y otras que las validan de forma modular.\n" +
    "   - Estructuras de control: se usan para pausar, avanzar niveles y aumentar la complejidad.\n" +
    "   - Funcionamiento: en cada ronda el sistema llama a una subrutina de validación que revisa longitud, uso de mayúsculas, números y símbolos.\n" +
    "     Si la validación retorna “falso”, una variable de control indica que el villano roba un fragmento.\n\n"
);
configurarTexto(juego1Txt);
contenido.add(juego1Txt);

// --- Bloque Juego 2 ---
JTextArea juego2Txt = new JTextArea(
    "Juego #2: The Zero-Trust\n" +
    "   - Cadenas: guardan mensajes ocultos y pistas que el jugador interpreta.\n" +
    "   - Vectores y matrices: almacenan inventarios y mapas con objetos clave.\n" +
    "   - Subrutinas: dividen el juego en etapas como explorar, abrir archivos o descifrar códigos.\n" +
    "   - Funciones: procesan las elecciones y devuelven si acercan o alejan al objetivo de la vacuna.\n" +
    "   - Funcionamiento: cada acción ejecuta funciones que trabajan con datos de vectores/matrices y cadenas de texto.\n" +
    "     El progreso se guarda en variables que se actualizan entre niveles.\n\n"
);
configurarTexto(juego2Txt);
contenido.add(juego2Txt);

// --- Bloque Juego 3 -
JTextArea juego3Txt = new JTextArea(
    "Juego #3: Phishing for Gold\n" +
    "   - Cadenas: se usan para analizar remitentes, enlaces y textos de correos electrónicos.\n" +
    "   - Matrices y vectores: almacenan los mensajes y los clasifican según riesgo.\n" +
    "   - Condicionales: reglas que determinan si un mensaje es seguro o falso.\n" +
    "   - Funciones: registran aciertos y errores para calcular la puntuación.\n" +
    "   - Funcionamiento: cada mensaje se recorre en un vector y pasa por condicionales que validan encabezados y enlaces.\n" +
    "     Una función lleva el conteo de resultados y actualiza la puntuación del jugador.\n\n"
            );
configurarTexto(juego3Txt);
contenido.add(juego3Txt);

JTextArea nota = new JTextArea(
    "📌 Nota: estos funcionamientos son propuestas de diseño; los juegos aún no están implementados.\n" +
    "   Posiblemente se agregue algo extra en el proceso.\n\n"
);
configurarTexto(nota);
contenido.add(nota);


// volver
JButton back = new JButton("← Volver al Menú");
back.setFont(new Font("Arial", Font.BOLD, 20));
back.setPreferredSize(new Dimension(260, 60));
back.setMaximumSize(new Dimension(260, 60));
back.setAlignmentX(Component.CENTER_ALIGNMENT);
back.addActionListener(e -> {
    getContentPane().removeAll();
    getContentPane().add(menuPanel);
    setSize(1000, 600);
    setResizable(false);
    setLocationRelativeTo(null);
    revalidate();
    repaint();
});

contenido.add(Box.createRigidArea(new Dimension(0, 30)));
contenido.add(back);
contenido.add(Box.createRigidArea(new Dimension(0, 40)));

        // Centrar contenido
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setOpaque(false);
        centerWrapper.add(contenido);

        JScrollPane scroll = new JScrollPane(centerWrapper);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(Color.BLACK);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.getVerticalScrollBar().setUnitIncrement(16); // scroll más suave

        manualPanel.add(scroll, BorderLayout.CENTER);
    }

    // Mostrar manual
    getContentPane().removeAll();
    getContentPane().add(manualPanel);
    setSize(1000, 600);
    setResizable(true);
    setLocationRelativeTo(null);
    revalidate();
    repaint();
}

    
    

    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    private Font getBadComaFont(float size) {
    try {
        Font customFont = Font.createFont(
            Font.TRUETYPE_FONT,
            getClass().getResourceAsStream("/fonts/BadComa.ttf") // ruta dentro de resources
        );
        return customFont.deriveFont(Font.PLAIN, size);
    } catch (Exception e) {
        System.out.println("No se pudo cargar la fuente Bad Coma, usando Arial.");
        return new Font("Arial", Font.BOLD, (int) size);
    }
}

    
    

    // Método auxiliar para cargar fuente personalizada
    private Font getCustomFont(float size) {
        try {
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("/fonts/fuente.otf"));
            return customFont.deriveFont(Font.PLAIN, size);
        } catch (Exception e) {
            System.out.println("No se pudo cargar la fuente, usando Arial.");
            return new Font("Arial", Font.BOLD, (int) size);
        }
    }
   
    //Creamos una subtutina para reproducir el sonido. La música está configurada para sonar en loop hasta que se le de la indicación de terminarse               
    public static void reproducirMusicaFondo(String ruta) {
        try {
            detenerMusicaFondo();

            AudioInputStream audioInput = AudioSystem.getAudioInputStream(MenuEstilo.class.getResource(ruta));
            musicaFondo = AudioSystem.getClip();
            musicaFondo.open(audioInput);

            FloatControl volumenControl = (FloatControl) musicaFondo.getControl(FloatControl.Type.MASTER_GAIN);
            float volumenReducido = -10.0f;
            volumenControl.setValue(volumenReducido);
            musicaFondo.loop(Clip.LOOP_CONTINUOUSLY);
            musicaFondo.start();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al cargar el audio: " + e.getMessage());
        }
    }

    public static void detenerMusicaFondo() {
        if (musicaFondo != null && musicaFondo.isRunning()) {
            musicaFondo.stop();
            musicaFondo.close();
        }
    }

    public static void reproducirSonido(String ruta) {
        try {
            URL sonidoURL = MenuEstilo.class.getResource(ruta);
            if (sonidoURL == null) {
                System.err.println("No se encontró el archivo de sonido: " + ruta);
                return;
            }

            AudioInputStream audioIn = AudioSystem.getAudioInputStream(sonidoURL);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);

            FloatControl volumenControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float volumenMaximo = volumenControl.getMaximum();
            volumenControl.setValue(volumenMaximo);

            clip.start();

            clip.addLineListener(event -> {
                if (event.getType() == javax.sound.sampled.LineEvent.Type.STOP) {
                    clip.close();
                }
            });

        } catch (Exception e) {
            System.err.println("Error al reproducir el sonido: " + ruta);
            e.printStackTrace();
        }
    }

    public class CursorUtils {

        /**
         * Crea un cursor personalizado a partir de cualquier imagen y la
         * redimensiona.
         *
         * @param path ruta de la imagen en recursos (ej: "/images/cursor.png")
         * @param width ancho deseado del cursor
         * @param height alto deseado del cursor
         * @return Cursor listo para usar
         */
        public static Cursor crearCursor(String path, int width, int height) {
            try {
                ImageIcon icon = new ImageIcon(CursorUtils.class.getResource(path));
                Image image = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
                return Toolkit.getDefaultToolkit().createCustomCursor(
                        image,
                        new Point(0, 0),
                        "CursorPersonalizado"
                );
            } catch (Exception e) {
                System.err.println("No se pudo cargar el cursor: " + e.getMessage());
                return Cursor.getDefaultCursor(); // cursor por defecto si falla
            }
        }
    }
    
    
  
class EscalablePanel extends JPanel {
    private JPanel contenido;

    public EscalablePanel(JPanel contenido) {
        this.contenido = contenido;
        setLayout(null); // para controlar manualmente
        add(contenido);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Tamaño base de diseño
        int baseW = 1000;
        int baseH = 600;

        // Tamaño actual de la ventana
        int currentW = getWidth();
        int currentH = getHeight();

        // Escala proporcional
        double scale = Math.min((double) currentW / baseW, (double) currentH / baseH);

        int newW = (int) (baseW * scale);
        int newH = (int) (baseH * scale);

        int x = (currentW - newW) / 2;
        int y = (currentH - newH) / 2;

        contenido.setBounds(x, y, newW, newH);
    }
}  

    public void volverAlMenu() {
    getContentPane().removeAll();
    add(menuPanel);
    revalidate();
    repaint();
    setSize(1000, 600);
    setResizable(false);
    setLocationRelativeTo(null);
}
    
}    
    

