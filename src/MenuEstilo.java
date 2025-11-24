// Importa todas las clases y componentes gráficos de Swing
import javax.swing.*;
// Importa clases básicas de AWT: colores, fuentes, gráficos, layouts, etc.
import java.awt.*;
// Permite trabajar con rutas URL, útil para cargar imágenes, sonidos o recursos externos
import java.net.URL;
import java.util.HashMap;// Estructuras de datos tipo mapa, para almacenar información con clave → valor
import java.util.Map;
// Librerías para reproducir audio (.wav)
import javax.sound.sampled.AudioInputStream; // Flujo de datos del audio
import javax.sound.sampled.AudioSystem;  // Sistema para obtener y gestionar audios
import javax.sound.sampled.Clip;        // Permite reproducir sonidos cortos (efectos)
import javax.sound.sampled.FloatControl;// Control de volumen del audio
// Bordes para componentes gráficos, en este caso permite crear márgenes internos
import javax.swing.border.EmptyBorder;
// Permite acceder a funciones del sistema, como iconos, sonidos o recursos del sistema
import java.awt.Toolkit;
// Permite modificar el cursor (puntero personalizado)
import java.awt.Cursor;
import java.awt.Point;
// Manejo de eventos, sirve para detectar clics y acciones del usuario
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class MenuEstilo extends JFrame {
    static Clip musicaFondo;
    static Map<String, Clip> efectosSonido = new HashMap<>();
    JPanel menuPanel;  // Panel principal del menú
    private JPanel creditsPanel; // Panel de créditos
    private JPanel manualPanel; // Panel de Manual
    private JPanel gamesPanel; //Panel de juego 
    private JPanel tutorialPanel; // Panel de "¿Cómo jugar?" que está en el símbolo de la "i" abajo a la derecha
    private StoryState storyState = new StoryState(); //acá llevaremos cuenta del avance de los capítulos de los juegos en términos de narrativa y así
    //Variables que controlan la música
    private static boolean musicaActivada = true;
    private static Clip musicaFondoGlobal = null;
    private static boolean musicaActiva = true;
    private static Clip musicaActual = null;

    //Se crea el constructor principal
    public MenuEstilo() {
        setTitle("DENY-ALL");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        //Título del juego
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


        menuPanel = createMenuPanel(); //Crear el controller principal
        add(menuPanel);
        setVisible(true); //hacer visible
    }

    
    
    
   public MenuEstilo(StoryState storyState) {
       //establezco el storystate, que funciona como memoria del juego que conserva el progreso del jugador
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
//establezco el cursor
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
    MenuEstilo.setMusicaActiva(!MenuEstilo.isMusicaActiva());
    
    soundButton.setIcon(MenuEstilo.isMusicaActiva() ? soundOnIcon : soundOffIcon);
    
    if (!MenuEstilo.isMusicaActiva()) {
        // Si se desactiva, detener la música
        MenuEstilo.detenerMusicaFondo();
    } else {
        // Si se activa, volver a reproducir la música
        reproducirMusicaFondo("sonidos/musicaelec.wav");
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
//Al colocar el cursor sobre el botón, aparece un mensaje que dice "¿Cómo funciona?"
howToButton.setToolTipText("¿Cómo funciona?");
howToButton.addActionListener(e -> {
    mostrarManual();
});
panel.add(howToButton);
panel.setComponentZOrder(howToButton, 0);


            
            
            
// Efectos visuales para el label (botón)
      label.addMouseListener(new java.awt.event.MouseAdapter() {
    // Variables para guardar el tamaño original del botón
    int originalWidth;
    int originalHeight;
    int originalX;
    int originalY;
    boolean sizeSaved = false; // Para evitar guardar los valores más de una vez

    @Override
    public void mouseEntered(java.awt.event.MouseEvent e) {
        label.setBackground(new Color(120, 0, 0));
                // Cambia el fondo cuando el mouse pasa por encima
                
                //reproduce el sonido de hover al poner el cursor sobre el  botón
        reproducirSonido("/sonidos/hover.wav");

        // Guarda el tamaño y posición solo la primera vez
        if (!sizeSaved) {
            originalWidth = label.getWidth();
            originalHeight = label.getHeight();
            originalX = label.getX();
            originalY = label.getY();
            sizeSaved = true;
        }

        // Animación al pasar el mouse: el botón crece un poquito
        new Thread(() -> {
            for (int i = 0; i < 4; i++) {
                int w = label.getWidth() + 2;     // Hace el ancho un poco mayor
                int h = label.getHeight() + 1;    // Hace la altura un poco mayor
                // Mueve la posición para que crezca hacia afuera y no hacia un solo lado
                label.setBounds(label.getX() - 1, label.getY() - 1, w, h);
                
                                    try {
                          Thread.sleep(10);
                      } catch (InterruptedException ex) {
                      }
                  }
              }).start();
          }

          @Override
          public void mouseExited(java.awt.event.MouseEvent e) {
              label.setBackground(Color.BLACK);
                      // Cuando se sale del botón, vuelve al tamaño original
              label.setBounds(originalX, originalY, originalWidth, originalHeight);
          }

 @Override
    public void mousePressed(java.awt.event.MouseEvent e) {
        // Cuando se presiona, el botón se "hunde" un poquito
        label.setLocation(label.getX() + 2, label.getY() + 2);
        label.setBackground(new Color(80, 0, 0)); // Se oscurece
    }

    @Override
    public void mouseReleased(java.awt.event.MouseEvent e) {
        //suelta el botón y vuelve a su posición original
        label.setLocation(originalX, originalY);
        label.setBackground(new Color(120, 0, 0));
    }

    @Override
    public void mouseClicked(java.awt.event.MouseEvent e) {
        //sonido al hacer click
        MenuEstilo.reproducirSonido("sonidos/click.wav");

        
        //Acciones según el texto del botón
        if (texto.equals("Salir")) {
            System.exit(0); //cierra el programa
      } else if (texto.equals("Créditos")) {
    MenuEstilo.detenerMusicaFondo(); //abrir el panel de créditos y detener la música
    if (MenuEstilo.isMusicaActiva()) {
        reproducirMusicaFondo("sonidos/down.wav"); //reproducir una música distinta en "Créditos"
    }
    
    mostrarCreditos();
        } else if (texto.equals("Iniciar juego")) {
        dispose(); //"destrute" la ventana actual
    new SeleccionPersonaje(); //abre una nueva ventana en el menú de selección del personaje
        } else if (texto.equals("¿Cómo jugar?")) {
            mostrarTutorial(); //mostrar tutorial
        } else {
            mostrarManual(); //mostrar manual técnico
        }
    }
});
// Agrega el label al contenedor con GridBagLayout
            gbc.gridy++;
            contenido.add(label, gbc);
        }

// Asegura que los botones queden por encima del fondo animado
        panel.add(contenido);
        panel.setComponentZOrder(contenido, 0); // Lo pone en el primer plano

        return panel;
        
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
// Nuevo panel para el menú de créditos
private void mostrarCreditos() {
    if (creditsPanel == null) {     // Si el panel de créditos aún no ha sido creado, lo crea
        // 'CreditosPanel' recibe una función (callback) que se ejecutará cuando el usuario
        // presione "Volver" o salga de la pantalla de créditos.
        // Esa función restaura el menú original.
        creditsPanel = new CreditosPanel(() -> {
            getContentPane().removeAll();  // Elimina todo lo que esté actualmente en el JFrame
            add(menuPanel);             // Vuelve a mostrar el panel del menú principal
            // Actualiza la interfaz para que los cambios se reflejen
            revalidate();
            repaint();
        });    }
    getContentPane().removeAll();     // Reemplaza el contenido actual por el panel de créditos
    add(creditsPanel);
    revalidate();     // Refresca la ventana para que los cambios se muestren
    repaint(); }



    public static void main(String[] args) {
        SwingUtilities.invokeLater(MenuEstilo::new);
    }    
    
    //Esto está en otra clase
    
    
    


     private void mostrarJuegos() {
    if (gamesPanel == null) {
        // Panel con fondo degradado animado y bien bonito
        gamesPanel = new JPanel() {
            private float hue = 0f;            {
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
                g2d.fillRect(0, 0, getWidth(), getHeight());            }        };
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
barraProgreso.setForeground(new Color(0, 255, 100)); // verde creo
barraProgreso.setBackground(Color.DARK_GRAY);
barraProgreso.setBorderPainted(false);
barraProgreso.setStringPainted(true); // muestra el texto tipo así "2/3"
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
        };        //Matriz de Strings
// EXPLICACIÓN: Matriz 3x4 (3 filas, 4 columnas) con datos de los juegos
// CONCEPTO: Matrices        
// CONCEPTO: Acceso a elementos de matrices con múltiples índice
        for (String[] juego : juegos) {
            String nombre = juego[0]; //primera columna
            String descripcion = juego[1]; //segunda columna
            String color = juego[2]; //tercera columna
            String icono = juego[3]; //cuarta columna
// EXPLICACIÓN: Cada fila es un array, cada columna un índice
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
    //Este es más simple y no es visible, pero queda aquí por si el que está en la otra clase falla.
    dispose();
}
       

       //La idea es que sea un diseño modular con progresión narrativa acumulativa 
       //cada capítulo es independiente (significa que son jugables en cualquier orden), 
       //pero al completarlos en secuencia el jugador desbloquea más capas de historia y ambientación
//Según yo se entiende la intención
     
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
  
   
    
    
    
    
 
    
    
   // ============================================================================
// TUTORIAL PANEL MEJORADO - ESTILO CYBER
// ============================================================================
private void mostrarTutorial() {
    setResizable(true);

    JPanel contenido = new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            
            // Fondo degradado oscuro
            GradientPaint gradient = new GradientPaint(
                0, 0, new Color(5, 10, 20),
                getWidth(), getHeight(), new Color(15, 25, 40)
            );
            g2d.setPaint(gradient);
            g2d.fillRect(0, 0, getWidth(), getHeight());
            
            // Efecto de grid cyber
            g2d.setColor(new Color(0, 255, 255, 10));
            for (int x = 0; x < getWidth(); x += 40) {
                g2d.drawLine(x, 0, x, getHeight());
            }
            for (int y = 0; y < getHeight(); y += 40) {
                g2d.drawLine(0, y, getWidth(), y);
            }
        }
    };
    
    contenido.setLayout(new BoxLayout(contenido, BoxLayout.Y_AXIS));
    contenido.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

    // ========== HEADER CON TÍTULO ==========
    JPanel headerPanel = new JPanel();
    headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
    headerPanel.setOpaque(false);
    headerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

    JLabel titulo = new JLabel("MANUAL DE OPERACIONES");
    titulo.setFont(getBadComaFont(42f));
    titulo.setForeground(new Color(0, 255, 255));
    titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

    JLabel subtitulo = new JLabel("[ GUÍA TÁCTICA DE MINIJUEGOS ]");
    subtitulo.setFont(new Font("Consolas", Font.BOLD, 16));
    subtitulo.setForeground(new Color(150, 200, 255));
    subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
    subtitulo.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));

    headerPanel.add(titulo);
    headerPanel.add(subtitulo);
    contenido.add(headerPanel);
    contenido.add(Box.createRigidArea(new Dimension(0, 30)));

    // ========== JUEGO 1 ==========
    contenido.add(crearSeccionJuego(
        "JUEGO #1: LOCK & CODE",
        new Color(0, 220, 255),
        "En este juego, un villano intentará robar contraseñas de un banco en crisis.\n" +
        "El jugador, en el rol de programador, deberá generar contraseñas cada vez más seguras para evitar el robo.\n\n" +
        "CONCEPTOS USADOS:\n" +
        " • Cadenas (strings) → para construir contraseñas con letras, números y símbolos.\n" +
        " • Subrutinas → para generar y validar contraseñas de forma modular.\n" +
        " • Estructuras de control → pausar/avanzar niveles y aumentar la complejidad.\n\n" +
        "EJEMPLO DE RETO:\n" +
        "En cada ronda debes añadir o sustituir caracteres para que la contraseña cumpla\n" +
        "mínimos de longitud, mayúsculas, números y símbolos. Si fallas, el villano roba un fragmento."
    ));

    contenido.add(Box.createRigidArea(new Dimension(0, 25)));

    // ========== JUEGO 2 ==========
    contenido.add(crearSeccionJuego(
            "JUEGO #2: THE ZERO-TRUST",
        new Color(255, 100, 100),
        "Tras la derrota del villano, éste libera un virus en el banco. El jugador debe recorrer la oficina enemiga,\n" +
        "reunir fragmentos y ensamblar una 'vacuna' que defenderá la infraestructura.\n\n" +
        "CONCEPTOS USADOS:\n" +
        " • Cadenas → pistas y mensajes ocultos que debes interpretar.\n" +
        " • Vectores y matrices → inventario y mapas donde aparecen objetos clave.\n" +
        " • Subrutinas → etapas como explorar, abrir archivos y descifrar códigos.\n" +
        " • Funciones → validar elecciones y determinar si estás más cerca de la vacuna.\n\n" +
        " TIPS:\n" +
        "Inspecciona archivos, compara fragmentos y guarda progresos entre niveles."
    ));

    contenido.add(Box.createRigidArea(new Dimension(0, 25)));

    // ========== JUEGO 3 ==========
    contenido.add(crearSeccionJuego(
        "JUEGO #3: PHISHING FOR GOLD",
        new Color(255, 220, 0),
        "Ahora, convertido en directivo del banco, debes identificar mensajes dañinos disfrazados de correos legítimos.\n" +
        "Detectarás intentos de phishing aplicando reglas y consejos ofrecidos al inicio de cada nivel.\n\n" +
        "CONCEPTOS USADOS:\n" +
        " • Cadenas → análisis de remitentes, enlaces y textos.\n" +
        " • Matrices / vectores → almacenar y clasificar mensajes por riesgo.\n" +
        " • Condicionales → reglas que deciden si un mensaje es seguro o falso.\n" +
        " • Funciones → llevar control de aciertos y errores para calcular tu puntuación.\n\n" +
        "CONSEJO:\n" +
        "Revisa encabezados, enlaces sospechosos y errores de ortografía: suelen delatar fraudes."
    ));

    contenido.add(Box.createRigidArea(new Dimension(0, 30)));

    // ========== BOTÓN VOLVER ==========
    JButton backButton = crearBotonCyber("← VOLVER AL MENÚ", new Color(50, 100, 150));
    backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    backButton.addActionListener(e -> {
        MenuEstilo.reproducirSonido("/sonidos/click.wav");
        getContentPane().removeAll();
        getContentPane().add(menuPanel);
        setSize(1000, 600);
        setResizable(false);
        setLocationRelativeTo(null);
        revalidate();
        repaint();
    });

    contenido.add(backButton);
    contenido.add(Box.createRigidArea(new Dimension(0, 30)));

    // ========== SCROLL PANE ==========
    JScrollPane scrollPane = new JScrollPane(contenido);
    scrollPane.setBorder(null);
    scrollPane.getViewport().setOpaque(false);
    scrollPane.setOpaque(false);
    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    scrollPane.getVerticalScrollBar().setUnitIncrement(16);

    getContentPane().removeAll();
    getContentPane().add(scrollPane);
    setSize(1000, 650);
    setLocationRelativeTo(null);
    revalidate();
    repaint();
}

// ============================================================================
// MÉTODO AUXILIAR PARA CREAR LAS SECCIONES DE JUEGOS
// ============================================================================
private JPanel crearSeccionJuego(String titulo, Color colorTema, String contenido) {
    JPanel panel = new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Fondo con gradiente
            GradientPaint gradient = new GradientPaint(
                0, 0, new Color(20, 25, 40, 200),
                getWidth(), getHeight(), new Color(30, 35, 50, 180)
            );
            g2d.setPaint(gradient);
            g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
            
            // Borde con efecto glow
            g2d.setColor(new Color(colorTema.getRed(), colorTema.getGreen(), colorTema.getBlue(), 100));
            g2d.setStroke(new BasicStroke(3f));
            g2d.drawRoundRect(1, 1, getWidth()-2, getHeight()-2, 15, 15);
        }
    };
    
    panel.setLayout(new BorderLayout(10, 10));
    panel.setOpaque(false);
    panel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

    // Título de la sección
    JLabel lblTitulo = new JLabel(titulo);
    lblTitulo.setFont(new Font("Consolas", Font.BOLD, 22));
    lblTitulo.setForeground(colorTema);

    // Contenido de texto
    JTextArea areaTexto = new JTextArea(contenido);
    areaTexto.setFont(new Font("Consolas", Font.PLAIN, 14));
    areaTexto.setForeground(new Color(220, 220, 240));
    areaTexto.setOpaque(false);
    areaTexto.setLineWrap(true);
    areaTexto.setWrapStyleWord(true);
    areaTexto.setEditable(false);

    panel.add(lblTitulo, BorderLayout.NORTH);
    panel.add(areaTexto, BorderLayout.CENTER);

    return panel;
}

// ============================================================================
// MANUAL TÉCNICO QUE ESTÁ EN LA "i"
// ============================================================================
private void mostrarManual() {
    if (manualPanel == null) {
        manualPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                
                // Fondo degradado cyber
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(5, 10, 20),
                    getWidth(), getHeight(), new Color(20, 30, 50)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                // Grid de fondo
                g2d.setColor(new Color(0, 255, 255, 8));
                for (int x = 0; x < getWidth(); x += 50) {
                    g2d.drawLine(x, 0, x, getHeight());
                }
                for (int y = 0; y < getHeight(); y += 50) {
                    g2d.drawLine(0, y, getWidth(), y);
                }
            }
        };
        manualPanel.setLayout(new BorderLayout());

        // ========== CONTENIDO ==========
        JPanel contenido = new JPanel();
        contenido.setLayout(new BoxLayout(contenido, BoxLayout.Y_AXIS));
        contenido.setOpaque(false);
        contenido.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        // ENCABEZADO
        JLabel titulo = new JLabel("MANUAL TECNICO DEL SISTEMA");
        titulo.setFont(getBadComaFont(38f));
        titulo.setForeground(new Color(0, 255, 255));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel subtitulo = new JLabel("[ ARQUITECTURA Y FUNCIONAMIENTO ]");
        subtitulo.setFont(new Font("Consolas", Font.BOLD, 14));
        subtitulo.setForeground(new Color(150, 200, 255));
        subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitulo.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));

        contenido.add(titulo);
        contenido.add(subtitulo);
        contenido.add(Box.createRigidArea(new Dimension(0, 25)));

        // SECCIONES TÉCNICAS
        agregarSeccionTecnica(contenido, "Resumen General", 
            "Esta clase se llama `MenuEstilo` e implementa un menú principal para seleccionar varios mini-juegos. " +
            "Usa Swing para la UI, `Clip`/`AudioSystem` para audio, y varios paneles " +
            "(menu, créditos, juegos, tutorial, manual). El patrón usado es perezoso (lazy initialization), " +
            "esto significa cada subpanel se crea solo la primera vez que se necesita (if == null).");

        agregarSeccionTecnica(contenido, "Imports y Recursos",
            "• javax.swing / java.awt: componentes gráficos, layouts, eventos.\n" +
            "• javax.sound.sampled: para cargar y reproducir clips (AudioInputStream, Clip, FloatControl).\n" +
            "• Recursos no java (fuentes, imágenes y sonidos) se obtienen vía getResource(...) para funcionar dentro de la clase.\n" +
            "• Esta parte se hizo desde el explorador de archivos del computador, no desde NetBeans, cargando los recursos requeridos dentro de la carpeta resources.");

        agregarSeccionTecnica(contenido, "Sistema de Audio",
            "• AudioSystem.getAudioInputStream(...) abre el stream desde recursos.\n" +
            "• Clip se abre (clip.open(audioIn)), se controla el volumen con FloatControl y se inicia con start() o loop(...).\n" +
            "• Manejo de errores: se capturan excepciones y se muestran mensajes con JOptionPane o se imprime el stacktrace.\n" +
            "• Buenas prácticas: cerrar clips (clip.close()) cuando terminan y usar LineListener para liberar recursos.");

        agregarSeccionTecnica(contenido, "Funcionamiento de los Juegos",
            "Juego #1: Lock & Code\n" +
            "• Cadenas (strings): se implementan como variables que almacenan contraseñas.\n" +
            "• Subrutinas: funciones que generan contraseñas y otras que las validan.\n" +
            "• Estructuras de control: se usan para pausar, avanzar niveles y aumentar complejidad.\n\n" +
            
            "Juego #2: The Zero-Trust\n" +
            "• Cadenas: guardan mensajes ocultos y pistas.\n" +
            "• Vectores y matrices: almacenan inventarios y mapas.\n" +
            "• Subrutinas: dividen el juego en etapas.\n\n" +
            
            "Juego #3: Phishing for Gold\n" +
            "• Cadenas: se usan para analizar remitentes y enlaces.\n" +
            "• Matrices y vectores: almacenan mensajes y clasificación.\n" +
            "• Condicionales: determinan si un mensaje es seguro o falso.");

        contenido.add(Box.createRigidArea(new Dimension(0, 25)));

        // BOTÓN VOLVER
        JButton back = crearBotonCyber("← VOLVER AL MENÚ", new Color(50, 100, 150));
        back.setAlignmentX(Component.CENTER_ALIGNMENT);
        back.addActionListener(e -> {
            MenuEstilo.reproducirSonido("/sonidos/click.wav");
            getContentPane().removeAll();
            getContentPane().add(menuPanel);
            setSize(1000, 600);
            setResizable(false);
            setLocationRelativeTo(null);
            revalidate();
            repaint();
        });

        contenido.add(back);
        contenido.add(Box.createRigidArea(new Dimension(0, 30)));

        // SCROLL PANE
        JScrollPane scroll = new JScrollPane(contenido);
        scroll.setBorder(null);
        scroll.getViewport().setOpaque(false);
        scroll.setOpaque(false);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        manualPanel.add(scroll, BorderLayout.CENTER);
    }

    getContentPane().removeAll();
    getContentPane().add(manualPanel);
    setSize(1000, 650);
    setResizable(true);
    setLocationRelativeTo(null);
    revalidate();
    repaint();
}

// ============================================================================
// MÉTODO AUXILIAR PARA SECCIONES TÉCNICAS
// ============================================================================
private void agregarSeccionTecnica(JPanel contenedor, String titulo, String texto) {
    JPanel seccion = new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            g2d.setColor(new Color(25, 30, 45, 200));
            g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
            
            g2d.setColor(new Color(0, 200, 255, 120));
            g2d.setStroke(new BasicStroke(2f));
            g2d.drawRoundRect(1, 1, getWidth()-2, getHeight()-2, 12, 12);
        }
    };
    
    seccion.setLayout(new BorderLayout(10, 10));
    seccion.setOpaque(false);
    seccion.setBorder(BorderFactory.createEmptyBorder(18, 22, 18, 22));

    JLabel lblTitulo = new JLabel(titulo);
    lblTitulo.setFont(new Font("Consolas", Font.BOLD, 18));
    lblTitulo.setForeground(new Color(0, 255, 200));

    JTextArea areaTexto = new JTextArea(texto);
    areaTexto.setFont(new Font("Consolas", Font.PLAIN, 13));
    areaTexto.setForeground(new Color(220, 220, 240));
    areaTexto.setOpaque(false);
    areaTexto.setLineWrap(true);
    areaTexto.setWrapStyleWord(true);
    areaTexto.setEditable(false);

    seccion.add(lblTitulo, BorderLayout.NORTH);
    seccion.add(areaTexto, BorderLayout.CENTER);

    contenedor.add(seccion);
    contenedor.add(Box.createRigidArea(new Dimension(0, 18)));
}

// ============================================================================
// BOTÓN CYBER REUTILIZABLE
// ============================================================================
private JButton crearBotonCyber(String texto, Color colorBase) {
    JButton btn = new JButton(texto) {
        private boolean hover = false;

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Color c1 = hover ? colorBase.brighter() : colorBase;
            Color c2 = hover ? colorBase : colorBase.darker();
            
            GradientPaint gradient = new GradientPaint(0, 0, c1, getWidth(), getHeight(), c2);
            g2d.setPaint(gradient);
            g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

            if (hover) {
                g2d.setColor(new Color(0, 255, 255, 100));
                g2d.setStroke(new BasicStroke(3f));
                g2d.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 20, 20);
            }
            
            g2d.setColor(new Color(0, 200, 255));
            g2d.setStroke(new BasicStroke(2f));
            g2d.drawRoundRect(2, 2, getWidth()-4, getHeight()-4, 20, 20);

            // Texto con sombra
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

    btn.setFont(new Font("Consolas", Font.BOLD, 16));
    btn.setPreferredSize(new Dimension(300, 50));
    btn.setMaximumSize(new Dimension(300, 50));
    btn.setContentAreaFilled(false);
    btn.setFocusPainted(false);
    btn.setBorderPainted(false);

    btn.addMouseListener(new java.awt.event.MouseAdapter() {
        @Override
        public void mouseEntered(java.awt.event.MouseEvent e) {
            MenuEstilo.reproducirSonido("/sonidos/hover.wav");
            try {
                java.lang.reflect.Field hoverField = btn.getClass().getDeclaredField("hover");
                hoverField.setAccessible(true);
                hoverField.set(btn, true);
            } catch (Exception ex) { }
            btn.repaint();
        }

        @Override
        public void mouseExited(java.awt.event.MouseEvent e) {
            try {
                java.lang.reflect.Field hoverField = btn.getClass().getDeclaredField("hover");
                hoverField.setAccessible(true);
                hoverField.set(btn, false);
            } catch (Exception ex) { }
            btn.repaint();
        }
    });
    return btn;
}
    
    

    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
// -------------- Abajo dejé varios de los Method importantes para no confundirme ---------------    
   




    
    
    private Font getBadComaFont(float size) {
    try {
        Font customFont = Font.createFont(
            Font.TRUETYPE_FONT,
            getClass().getResourceAsStream("/fonts/BadComa.ttf") // ruta dentro de resources
        );
        return customFont.deriveFont(Font.PLAIN, size);
    } catch (Exception e) {
        System.out.println("No se pudo cargar la fuente Bad Coma, estamos usando Arial.");
        return new Font("Arial", Font.BOLD, (int) size);
    }
} //cambia a arial si la fuente falla.

    
    

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
            if (!musicaActiva) return;
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
    
    
    
    public static boolean isMusicaActiva() {
        return musicaActiva;
    }
    
    public static void setMusicaActiva(boolean activa) {
        musicaActiva = activa;
    }
    
    public static void setMusicaActual(Clip clip) {
        musicaActual = clip;
    }
   
    
}    
    

