import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.imageio.ImageIO;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * ============================================================================
 * CAPÍTULO 1: LOCK & CODE
 * Sistema de escenas simplificado para diálogos y minijuegos
 * ============================================================================
 */
public class minijuego1 extends BaseCapituloFrame {
    
    // ============================================================================
    // VARIABLES PRINCIPALES
    // ============================================================================
    private JPanel contenedorPrincipal;
    private CardLayout cardLayout;
    private List<Escena> escenas;
    private int escenaActual = 0;
    private String nombreJugador;
    private String carpetaGenero;
    

    
    // ============================================================================
    // CLASE ESCENA - REPRESENTA CADA PANTALLA DEL JUEGO
    // ============================================================================
    /**
     * Clase interna que representa una escena del juego.
     * Puede ser un diálogo con imagen o un minijuego.
     */
   private static class Escena {
    String imagen;
    String dialogo;
    boolean esMinijuego;
    Runnable minijuegoAccion;
    String sonido; //para el efecto de sonido
    
    // Constructor para diálogos normales
    Escena(String imagen, String dialogo) {
        this.imagen = imagen;
        this.dialogo = dialogo;
        this.esMinijuego = false;
    }
    
    // Constructor para diálogos con sonido
    Escena(String imagen, String dialogo, String sonido) {
        this.imagen = imagen;
        this.dialogo = dialogo;
        this.esMinijuego = false;
        this.sonido = sonido;
    }
    
    // Constructor para minijuegos
    Escena(Runnable minijuegoAccion) {
        this.esMinijuego = true;
        this.minijuegoAccion = minijuegoAccion;
    }
    
    // Método para reproducir sonido si existe
    public void reproducirSonido() {
        if (sonido != null && !sonido.isEmpty()) {
            MenuEstilo.reproducirSonido(sonido);
        }
    }
}
    
    // ============================================================================
    // CONSTRUCTOR PRINCIPAL D:
    // ============================================================================
public minijuego1(StoryState state) {
    super(state, "Capítulo 1: Lock & Code", "");
    
    this.nombreJugador = state.getNombre();
    this.carpetaGenero = state.getGenero().equals("mujer") ? "mujer" : "hombre";
    this.escenas = new ArrayList<>();
 //ArrayList (vector dinámico)
// EXPLICACIÓN: ArrayList crece dinámicamente según se añaden elementos
// CONCEPTO: Vector de objetos0
    
    
MenuEstilo.detenerMusicaFondo();
    // Configurar panel principal
    centerPanel.setLayout(new BorderLayout());
    centerPanel.setBackground(Color.BLACK);

    // Panel para centrar contenido verticalmente
    JPanel panelCarga = new JPanel();
    panelCarga.setLayout(new BoxLayout(panelCarga, BoxLayout.Y_AXIS));
    panelCarga.setBackground(Color.BLACK);
    panelCarga.setBorder(BorderFactory.createEmptyBorder(80, 0, 80, 0));

    // Imagen de carga (usando una de tus imágenes existentes)
    JLabel lblImagenCarga = new JLabel("", SwingConstants.CENTER);
    try {
        // Intentar usar una imagen existente
        String imagenPath = img("logo1"); 
        java.net.URL url = getClass().getResource(imagenPath);
        
        if (url != null) {
            ImageIcon iconoOriginal = new ImageIcon(url);
            // Redimensionar a tamaño adecuado para pantalla de carga
            Image imagenRedimensionada = iconoOriginal.getImage()
                .getScaledInstance(180, 180, Image.SCALE_SMOOTH);
            lblImagenCarga.setIcon(new ImageIcon(imagenRedimensionada));
        } else {
            throw new Exception("Imagen no encontrada");
        }
    } catch (Exception e) {
        // Fallback elegante si no hay imagen
        lblImagenCarga.setText("🔐");
        lblImagenCarga.setFont(new Font("Arial", Font.BOLD, 100));
        lblImagenCarga.setForeground(new Color(0, 200, 255));
    }
    lblImagenCarga.setAlignmentX(Component.CENTER_ALIGNMENT);

    // Texto de carga (se actualizará con las frases cyber)
    JLabel lblTexto = new JLabel("Iniciando sistemas de seguridad...", SwingConstants.CENTER);
    lblTexto.setFont(new Font("Arial", Font.BOLD, 22));
    lblTexto.setForeground(new Color(0, 255, 255));
    lblTexto.setAlignmentX(Component.CENTER_ALIGNMENT);

    // Espaciador
    panelCarga.add(Box.createRigidArea(new Dimension(0, 30)));
    panelCarga.add(lblImagenCarga);
    panelCarga.add(Box.createRigidArea(new Dimension(0, 40)));

    // BARRA DE PROGRESO
    JProgressBar barraProgreso = new JProgressBar();
    barraProgreso.setIndeterminate(true);
    barraProgreso.setPreferredSize(new Dimension(400, 25));
    barraProgreso.setMaximumSize(new Dimension(400, 25));
    barraProgreso.setBackground(new Color(30, 30, 60));
    barraProgreso.setForeground(new Color(0, 200, 255));
    barraProgreso.setBorder(BorderFactory.createLineBorder(new Color(0, 150, 200), 2));
    barraProgreso.setAlignmentX(Component.CENTER_ALIGNMENT);

    // Texto
    JLabel lblVersion = new JLabel("Lock & Code v1.0 - Sistema Anti-backdoors", SwingConstants.CENTER);
    lblVersion.setFont(new Font("Arial", Font.ITALIC, 14));
    lblVersion.setForeground(new Color(150, 150, 200));
    lblVersion.setAlignmentX(Component.CENTER_ALIGNMENT);

    // Ensamblar todos los componentes
    panelCarga.add(lblTexto);
    panelCarga.add(Box.createRigidArea(new Dimension(0, 30)));
    panelCarga.add(barraProgreso);
    panelCarga.add(Box.createRigidArea(new Dimension(0, 20)));
    panelCarga.add(lblVersion);

    // Agregar al centro
    centerPanel.add(panelCarga, BorderLayout.CENTER);

    // Hacer visible
    setExtendedState(JFrame.MAXIMIZED_BOTH);
    setVisible(true);

    // ============================================================================
    // CARGA EN SEGUNDO PLANO
    // ============================================================================
    
    // Cargar recursos en segundo plano
    SwingWorker<Void, String> cargador = new SwingWorker<Void, String>() {
            @Override
protected Void doInBackground() throws Exception {
    // FASE 1: Configuración inicial
    publish("Iniciando sistemas de seguridad...");
    Thread.sleep(2000);
    
    // FASE 2: Configurar escenas
    publish("Cargando narrativa del capítulo...");
    Thread.sleep(1500);
    agregarEscenas();
    
    // FASE 3: Precarga con MÚLTIPLES frases cyber
    publish("Analizando patrones de ataque de Mario Von Richter...");
    Thread.sleep(1800);
    
    for (int i = 0; i < 3; i++) {
        publish(obtenerFraseCyberAleatoria());
        Thread.sleep(2800); // 2.8 segundos por frase - tiempo para leer
        precargarImagenes(); // Precargar en cada iteración
    }
    
    // FASE 4: Finalización
    publish("Sincronizando con servidores seguros...");
    Thread.sleep(1600);
    publish("¡Sistemas listos! Iniciando misión...");
    Thread.sleep(1200);
    
    return null;
}
            
        @Override
protected void process(List<String> chunks) {
    lblTexto.setText("•" + chunks.get(chunks.size() - 1));
}
            
            @Override
            protected void done() {
                try {
                    get(); // Verificar si hubo errores
                    
                    //Crear la interfaz gráfica
                    centerPanel.removeAll();
                    inicializarUI();
                    
                    // Mostrar la primera escena
                    mostrarEscena();
                    
                    centerPanel.revalidate();
                    centerPanel.repaint();
                    
                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null, 
                        "Error cargando recursos: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        
        cargador.execute();
    }
    
    // ============================================================================
    // CONFIGURACIÓN DE ESCENAS
    // ============================================================================
    /**
     * Para agregar un diálogo:
     *   escenas.add(new Escena(img("nombre_imagen"), "Texto del diálogo"));
     * 
     * Para agregar un minijuego:
     *   escenas.add(new Escena(this::nombreDelMetodoMinijuego));
     */
    private void agregarEscenas() {
        // ========== ESCENA 1: LLAMADA ENTRANTE ==========
            escenas.add(new Escena(
         img("panelinicialhombre"),       
        "-Otra noche más depurando código... Si al menos pagaran las horas extras..."
        ));                           
        
         escenas.add(new Escena(
         img("impaciente"),       
        "´[Sistema] Syntax error 404...´"
        ));  
                 
    escenas.add(new Escena(
        img("llamadaentrante"),
        "*Teléfono sonando insistentemente*",
        "/sonidos/telefono.wav"  // Ruta del sonido del teléfono sonando
    ));
        
        // ========== ESCENA 2: MOLESTO ==========
        escenas.add(new Escena(
            img("llamadaentrante"),
            nombreJugador + ": -¿En serio...? ¿Una llamada a esta hora? Si esto no es urgente, borro el número."
        ));
        
        // ========== ESCENA 3: CONTESTAR TELÉFONO ==========
        escenas.add(new Escena(
            img("contestamal"),
            nombreJugador + ": -¿Sí? Habla " + nombreJugador + "..."
        ));

        // ========== ESCENA 4: ASISTENTE HABLA ==========
        escenas.add(new Escena(
            img("contestandomal"),
            "Asistente: -¡Jefe! Es del Banco Global, dicen que están bajo ataque... quieren nuestra ayuda."
        ));
        
        // ========== ESCENA 5: REACCIÓN NEGATIVA ==========
        escenas.add(new Escena(
            img("escuchando"),
            nombreJugador + ": -¿El banco? ¿Otra vez esos de traje? Seguro rompieron algo y ahora quieren que yo lo arregle gratis."
        ));
        
        // ========== ESCENA 6: EXPLICACIÓN DEL PROBLEMA ==========
        escenas.add(new Escena(
            img("von"),
            "Asistente: -Dicen que Mario Von Richter, el famoso hacker, está intentando robar sus contraseñas. Necesitan que diseñemos un sistema más seguro."
        ));
        
        // ========== ESCENA 7: ESCEPTICISMO ==========
        escenas.add(new Escena(
            img("pensativo"),
            nombreJugador + ": -¿Y por qué debería ayudarlos? La última vez me pagaron con excusas."
        ));
        
     // ========== ESCENA 8: LA OFERTA ==========
escenas.add(new Escena(
    img("feliz"),
    "Asistente: -Esta vez ofrecen el triple de tu tarifa habitual. Y adelantaron el 50%.\n\n\n" +
    "PISTA: Mario Von Richter usa ataques de diccionario modificado"
));

// ========== ESCENA 9: INTERÉS ==========
escenas.add(new Escena(
    img("triple"),
    nombreJugador + ": -... ¿El triple dices?\n\n\n" +
    "PISTA: Siempre incluye el símbolo '#' en sus contraseñas"
));

// ========== ESCENA 10: CONFIRMACIÓN ==========
escenas.add(new Escena(
    img("sijefe"),
    "Asistente: -Sí, jefe. Parece que están desesperados.\n\n\n" +
    "PISTA: Evita secuencias numéricas como 123 o 789"
));

// ========== ESCENA 11: ACEPTAR TRABAJO ==========
escenas.add(new Escena(
    img("decision"),
    nombreJugador + ": -Está bien. Que envíen los detalles. Pero si vuelven a jugarme sucio, les juro que...\n\n\n" +
    "PISTA: Longitudes menores a 12 caracteres son vulnerables"
));
        
        // ========== ESCENA 12: DETALLES DEL TRABAJO ==========
        escenas.add(new Escena(
            img("escuchando"),
            "Asistente: -Ya envié el contrato. Necesitas generar contraseñas seguras para sus sistemas principales."
        ));
        
        // ========== ESCENA 13: CONFIANZA ==========
        escenas.add(new Escena(
            img("prin"),
            nombreJugador + ": -¿Contraseñas? Eso es trabajo de principiantes. ¿Qué tan difícil puede ser?"
        ));
        
        // ========== ESCENA 14: MINIJUEGO DE CONTRASEÑAS ==========
        escenas.add(new Escena(this::iniciarMinijuegoContraseñas));
    }
    
    /**
     * Método helper para crear rutas de imágenes fácilmente.
     * Convierte "pensativo" en "/images/hombre/pensativo.png" (o mujer)
     */
    private String img(String nombreImagen) {
        //concatenación básica.
        return "/images/" + carpetaGenero + "/" + nombreImagen + ".png";
    }
    // EXPLICACIÓN: Se unen literales con variables para formar rutas dinámicas
// CONCEPTO: Concatenación usando el operador +
    
    /**
     * Precarga y optimiza todas las imágenes del capítulo
     * Esto evita OutOfMemoryError y mejora el rendimiento
     * La memoria no debe superar 600mb
     */
    private void precargarImagenes() {
        int maxDimension = 1920; // Tamaño máximo (Full HD)
        
        for (Escena escena : escenas) {
            if (!escena.esMinijuego && escena.imagen != null) {
                try {
                    java.net.URL url = getClass().getResource(escena.imagen);
                    
                    if (url != null) {
                        // Cargar imagen original
                        java.awt.image.BufferedImage original = ImageIO.read(url);
                        
                        if (original != null) {
                            // Calcular nuevo tamaño manteniendo proporción
                            int anchoOriginal = original.getWidth();
                            int altoOriginal = original.getHeight();
                            
                            double escala = Math.min(
                                (double) maxDimension / anchoOriginal,
                                (double) maxDimension / altoOriginal
                            );
                            
                            // Solo redimensionar si es necesario
                            if (escala < 1.0) {
                                int nuevoAncho = (int) (anchoOriginal * escala);
                                int nuevoAlto = (int) (altoOriginal * escala);
                                
                                // Crear imagen optimizada
                                java.awt.image.BufferedImage optimizada = new java.awt.image.BufferedImage(
                                    nuevoAncho, nuevoAlto, java.awt.image.BufferedImage.TYPE_INT_RGB
                                );
                                
                                Graphics2D g2d = optimizada.createGraphics();
                                g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, 
                                                    RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                                g2d.drawImage(original, 0, 0, nuevoAncho, nuevoAlto, null);
                                g2d.dispose();
                                
                                cacheImagenes.put(escena.imagen, optimizada);
                                System.out.println("Optimizada: " + escena.imagen + 
                                                 " (" + anchoOriginal + "x" + altoOriginal + 
                                                 " → " + nuevoAncho + "x" + nuevoAlto + ")");
                            } else {
                                // la imagen ya es pequeña, usar original
                                cacheImagenes.put(escena.imagen, original);
                            }
                        }
                    } else {
                        System.err.println("❌ No encontrada: " + escena.imagen);
                    }
                } catch (Exception e) {
                    System.err.println("❌ Error precargando " + escena.imagen + ": " + e.getMessage());
                }
            }
        }
        
    }
    
    // ============================================================================
    // INICIALIZACIÓN DE LA INTERFAZ GRÁFICA
    // ============================================================================
    /**
     * Crear todos los paneles necesarios para las escenas
     */
    private void inicializarUI() {
        // CardLayout permite cambiar entre diferentes paneles
        cardLayout = new CardLayout();
        contenedorPrincipal = new JPanel(cardLayout);
        contenedorPrincipal.setBackground(Color.BLACK);
        
        // Crear un panel para cada escena
        for (int i = 0; i < escenas.size(); i++) {
            Escena escena = escenas.get(i);
            JPanel panel;
            
            if (escena.esMinijuego) {
                // Panel vacío para minijuegos (se va llenandoo después)
                panel = new JPanel(new BorderLayout());
                panel.setBackground(Color.BLACK);
            } else {
                // Panel de diálogo con imagen y texto
                panel = crearPanelDialogo(escena);
            }
            
            contenedorPrincipal.add(panel, "escena_" + i);
        }
        
        // Agregar al panel central del frame
        centerPanel.setLayout(new BorderLayout());
        centerPanel.add(contenedorPrincipal, BorderLayout.CENTER);
    }
    
    // ============================================================================
    // CREACIÓN DE PANELES DE DIÁLOGO
    // ============================================================================
    /**
     * Crea un panel con imagen de fondo y caja de diálogo
     */
    private JPanel crearPanelDialogo(Escena escena) {
        // Panel principal con imagen de fondo
        JPanel panelFondo = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                dibujarFondo(g, escena.imagen);
            }
        };
        
        // Caja de diálogo semi-transparente en la parte inferior
        JPanel cajaDialogo = crearCajaDialogo(escena.dialogo);
        panelFondo.add(cajaDialogo, BorderLayout.SOUTH);
        
        return panelFondo;
    }
    
    // Cache de imágenes para evitar recargarlas
    private Map<String, Image> cacheImagenes = new HashMap<>();
    
    /**
     * Dibuja la imagen de fondo desde el cache (o sea ya está optimizada)
     */
    private void dibujarFondo(Graphics g, String rutaImagen) {
        Image img = cacheImagenes.get(rutaImagen);
        
        if (img != null) {
            // Dibujar desde cache (ya está optimizada)
            g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
        } else {
            // Si no está en cache mostrar fondo gris con mensaje
            g.setColor(Color.DARK_GRAY);
            g.fillRect(0, 0, getWidth(), getHeight());
            
            g.setColor(Color.YELLOW);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("⚠ Imagen no precargada: " + rutaImagen, 50, getHeight() / 2);
            
            System.err.println("Imagen no en cache: " + rutaImagen);
        }
    }
    
    // ============================================================================
    // CAJA DE DIÁLOGO PARA LAS ESCENAS, BIEN BONITA
    // ============================================================================
    /**
     * esto crea la caja de diálogo con fondo semi-transparente y botones
     */
    private JPanel crearCajaDialogo(String texto) {
        // Contenedor transparente para posicionar la caja
        JPanel contenedor = new JPanel(new BorderLayout());
        contenedor.setOpaque(false);
        contenedor.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Panel con fondo negro semi-transparente y bordes redondeados
        JPanel caja = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setColor(new Color(0, 0, 0, 180)); // Negro 70% opaco
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2d.dispose();
            }
        };
        caja.setOpaque(false);
        caja.setPreferredSize(new Dimension(1500, 150));
        
        // Área de texto del diálogo
        JTextArea areaTexto = new JTextArea(texto);
        areaTexto.setEditable(false); //el jugador no puede editar
        areaTexto.setLineWrap(true); //salto de línea automático
        areaTexto.setWrapStyleWord(true); //Hace que el salto de línea respete palabras completas
        areaTexto.setFont(new Font("Arial", Font.BOLD, 18));
        areaTexto.setForeground(Color.WHITE);
        areaTexto.setOpaque(false); //transparente
        areaTexto.setBackground(new Color(0, 0, 0, 0));
        areaTexto.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // ScrollPane transparente
        JScrollPane scroll = new JScrollPane(areaTexto);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        
        // Panel de botones de navegación
        JPanel panelBotones = crearPanelBotones();
        
        // Ensamblar caja de diálogo
        caja.add(scroll, BorderLayout.CENTER);
        caja.add(panelBotones, BorderLayout.EAST);
        contenedor.add(caja, BorderLayout.SOUTH);
        
        return contenedor;
    }
    
    // ============================================================================
    // BOTONES DE NAVEGACIÓN
    // ============================================================================
    /**
     * Crea los botones de Salir y Continuar
     */
private JPanel crearPanelBotones() {
    JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 20));
    panel.setOpaque(false);
    panel.setPreferredSize(new Dimension(400, 50)); // Aumentado para 3 botones
    
    // Botón Continuar (cyan)
    JButton btnContinuar = new JButton("Continuar");
    estilizarBoton(btnContinuar, new Color(0, 0, 0, 200), Color.CYAN, 120, 40);
    btnContinuar.addActionListener(e -> siguienteEscena());
    
    // ⭐ NUEVO: Botón Saltar al Juego (verde)
    JButton btnSaltar = new JButton("Saltar al Juego");
    estilizarBoton(btnSaltar, new Color(0, 100, 0, 200), Color.GREEN, 150, 40);
    btnSaltar.addActionListener(e -> saltarAlJuego());
    
    // Botón Salir (rojo)
    JButton btnSalir = new JButton("Salir");
    estilizarBoton(btnSalir, new Color(150, 0, 0, 200), Color.RED, 90, 40);
    btnSalir.addActionListener(e -> confirmarSalida());
    
    panel.add(btnContinuar);
    panel.add(btnSaltar);
    panel.add(btnSalir);
    
    return panel;
}

// ⭐ NUEVA FUNCIÓN: Saltar directamente al minijuego
private void saltarAlJuego() {
    // Ir directamente a la última escena (el minijuego)
    escenaActual = escenas.size() - 1;
    mostrarEscena();
}
    
    
    /**
     * Método para aplicar estilo consistente a botones
     */
    private void estilizarBoton(JButton btn, Color fondo, Color borde, int ancho, int alto) {
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setBackground(fondo);
        btn.setForeground(Color.WHITE);
        btn.setBorder(BorderFactory.createLineBorder(borde, 3));
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(ancho, alto));
        btn.setOpaque(true);
    }
    
    // ============================================================================
    // NAVEGACIÓN ENTRE LAS ESCENAS
    // ============================================================================
    /**
     * Muestra la escena actual en la pantalla
     */
private void mostrarEscena() {
    cardLayout.show(contenedorPrincipal, "escena_" + escenaActual);
    Escena escena = escenas.get(escenaActual);
    
    // Reproducir sonido si la escena tiene uno
    if (!escena.esMinijuego) {
        escena.reproducirSonido();
    }
    
    // Forzar actualización visual
    contenedorPrincipal.revalidate();
    contenedorPrincipal.repaint();
    
    // Si es un minijuego, ejecutar su código
    if (escena.esMinijuego && escena.minijuegoAccion != null) {
        escena.minijuegoAccion.run();
    }
    
    // Reproducir sonido de avance (excepto primera escena)
    if (escenaActual > 0) {
        MenuEstilo.reproducirSonido("/sonidos/click.wav");
    }
}
    
    /**
     * Avanza a la siguiente escena o finaliza el capítulo
     */

//escena es la lista que guarda todas las escenas
    private void siguienteEscena() {
        if (escenaActual < escenas.size() - 1) {
            escenaActual++;
            mostrarEscena();
        } else {
            // Llegamos al final de todas las escenas
            finalizarCapitulo(1, true, () -> new minijuego2(storyState));
        }
    }
    
    // ============================================================================
    // MINIJUEGO: GENERADOR DE CONTRASEÑAS
    // ============================================================================
    /**
     * Inicia el minijuego de contraseñas en el panel actual
     */
 // ============================================================================
// MINIJUEGO MEJORADO: GENERADOR DE CONTRASEÑAS CON SISTEMA DE NIVELES
// ============================================================================
private void iniciarMinijuegoContraseñas() {
    Component panelActual = contenedorPrincipal.getComponent(escenaActual);
    
    if (panelActual instanceof JPanel) {
        JPanel panel = (JPanel) panelActual;
        panel.removeAll();
        panel.setLayout(new BorderLayout());
        panel.add(crearUIMinijuegoMejorado(), BorderLayout.CENTER);
        panel.revalidate();
        panel.repaint();
    }
}

// ============================================================================
// VARIABLES DEL MINIJUEGO MEJORADO
// ============================================================================
private int nivelPasswordActual = 1;
private static final int NIVELES_TOTALES = 4;
private int intentosNivel = 3;
private String passwordBase = "";
private JTextField campoPassword;
private JLabel lblNivel;
private JLabel lblIntentos;
private JLabel lblObjetivo;
private JProgressBar barraSeguridad;
private JPanel panelOpciones;

// ============================================================================
// UI PRINCIPAL DEL MINIJUEGO
// ============================================================================
private JPanel crearUIMinijuegoMejorado() {
    JPanel panel = new JPanel(new BorderLayout()) {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            
            // Fondo degradado dinámico
            GradientPaint gradient = new GradientPaint(
                0, 0, new Color(10, 15, 30),
                getWidth(), getHeight(), new Color(30, 20, 50)
            );
            g2d.setPaint(gradient);
            g2d.fillRect(0, 0, getWidth(), getHeight());
            
            // Grid animado
            g2d.setColor(new Color(0, 255, 100, 15));
            for (int x = 0; x < getWidth(); x += 50) {
                g2d.drawLine(x, 0, x, getHeight());
            }
            for (int y = 0; y < getHeight(); y += 50) {
                g2d.drawLine(0, y, getWidth(), y);
            }
        }
    };
    
    panel.add(crearHeaderMejorado(), BorderLayout.NORTH);
    panel.add(crearPanelCentral(), BorderLayout.CENTER);
    panel.add(crearPanelInferior(), BorderLayout.SOUTH);
    
    inicializarNivel(nivelPasswordActual);
    
    return panel;
}

// ============================================================================
// HEADER CON INFORMACIÓN DEL NIVEL
// ============================================================================
private JPanel crearHeaderMejorado() {
    JPanel header = new JPanel(new BorderLayout());
    header.setBackground(new Color(20, 25, 40));
    header.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(0, 255, 100)),
        BorderFactory.createEmptyBorder(20, 30, 20, 30)
    ));
    
    // Título principal
    JLabel titulo = new JLabel("LOCK & CODE - PASSWORD GENERATOR", SwingConstants.CENTER);
    titulo.setFont(new Font("Consolas", Font.BOLD, 32));
    titulo.setForeground(new Color(0, 255, 100));
    
    // Panel de estadísticas
    JPanel statsPanel = new JPanel(new GridLayout(1, 3, 20, 0));
    statsPanel.setOpaque(false);
    
    lblNivel = crearLabelStat("NIVEL 1/" + NIVELES_TOTALES, Color.CYAN);
    lblIntentos = crearLabelStat("INTENTOS: 3", Color.YELLOW);
    lblObjetivo = crearLabelStat("OBJETIVO: 60%", Color.GREEN);
    
    statsPanel.add(lblNivel);
    statsPanel.add(lblIntentos);
    statsPanel.add(lblObjetivo);
    
    header.add(titulo, BorderLayout.NORTH);
    header.add(statsPanel, BorderLayout.CENTER);
    
    return header;
}

private JLabel crearLabelStat(String texto, Color color) {
    JLabel lbl = new JLabel(texto, SwingConstants.CENTER);
    lbl.setFont(new Font("Consolas", Font.BOLD, 16));
    lbl.setForeground(color);
    lbl.setOpaque(true);
    lbl.setBackground(new Color(30, 35, 50));
    lbl.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(color, 2),
        BorderFactory.createEmptyBorder(10, 15, 10, 15)
    ));
    return lbl;
}

// ============================================================================
// PANEL CENTRAL CON LA CONTRASEÑA Y BARRA DE SEGURIDAD
// ============================================================================
private JPanel crearPanelCentral() {
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.setOpaque(false);
    panel.setBorder(BorderFactory.createEmptyBorder(40, 80, 40, 80));
    
    // Instrucción del nivel
    JLabel lblInstruccion = new JLabel("Construye una contraseña que cumpla los requisitos", SwingConstants.CENTER);
    lblInstruccion.setFont(new Font("Arial", Font.BOLD, 20));
    lblInstruccion.setForeground(Color.WHITE);
    lblInstruccion.setAlignmentX(Component.CENTER_ALIGNMENT);
    
    // Campo de contraseña con diseño mejorado
    campoPassword = new JTextField(20) {
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Fondo negro con borde gradiente
            g2d.setColor(Color.BLACK);
            g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
            
            // Borde dinámico según seguridad
            int seguridad = calcularSeguridadMejorado(getText());
            Color borderColor = getColorSeguridad(seguridad);
            g2d.setColor(borderColor);
            g2d.setStroke(new BasicStroke(3f));
            g2d.drawRoundRect(1, 1, getWidth()-2, getHeight()-2, 10, 10);
            
            super.paintComponent(g);
        }
    };
    
    campoPassword.setFont(new Font("Consolas", Font.BOLD, 28));
    campoPassword.setHorizontalAlignment(JTextField.CENTER);
    campoPassword.setEditable(false);
    campoPassword.setMaximumSize(new Dimension(700, 60));
    campoPassword.setBackground(Color.BLACK);
    campoPassword.setForeground(Color.WHITE);
    campoPassword.setOpaque(false);
    campoPassword.setAlignmentX(Component.CENTER_ALIGNMENT);
    
    // Barra de seguridad visual
    barraSeguridad = new JProgressBar(0, 100) {
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Fondo
            g2d.setColor(new Color(30, 30, 30));
            g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
            
            // Barra de progreso con gradiente
            int fillWidth = (int)((getWidth() - 6) * (getValue() / 100.0));
            if (fillWidth > 0) {
                Color c1 = getColorSeguridad(getValue());
                Color c2 = c1.brighter();
                GradientPaint gradient = new GradientPaint(0, 0, c1, fillWidth, 0, c2);
                g2d.setPaint(gradient);
                g2d.fillRoundRect(3, 3, fillWidth, getHeight()-6, 12, 12);
            }
            
            // Borde
            g2d.setColor(new Color(100, 100, 100));
            g2d.setStroke(new BasicStroke(2f));
            g2d.drawRoundRect(1, 1, getWidth()-2, getHeight()-2, 15, 15);
            
            // Texto
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Consolas", Font.BOLD, 14));
            String text = getValue() + "% - " + getNivelSeguridad(getValue());
            FontMetrics fm = g2d.getFontMetrics();
            int x = (getWidth() - fm.stringWidth(text)) / 2;
            int y = (getHeight() + fm.getAscent()) / 2 - 2;
            g2d.drawString(text, x, y);
        }
    };
    
    barraSeguridad.setValue(0);
    barraSeguridad.setPreferredSize(new Dimension(700, 35));
    barraSeguridad.setMaximumSize(new Dimension(700, 35));
    barraSeguridad.setStringPainted(false);
    barraSeguridad.setAlignmentX(Component.CENTER_ALIGNMENT);
    
    // Panel de requisitos del nivel
    JPanel requisitosPanel = crearPanelRequisitos();
    requisitosPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
    
    // Ensamblar
    panel.add(lblInstruccion);
    panel.add(Box.createRigidArea(new Dimension(0, 30)));
    panel.add(campoPassword);
    panel.add(Box.createRigidArea(new Dimension(0, 20)));
    panel.add(barraSeguridad);
    panel.add(Box.createRigidArea(new Dimension(0, 30)));
    panel.add(requisitosPanel);
    
    return panel;
}

// ============================================================================
// PANEL DE REQUISITOS CON CHECKMARKS VISUALES
// ============================================================================
private JPanel crearPanelRequisitos() {
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.setOpaque(false);
    panel.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(0, 255, 100, 100), 2),
        BorderFactory.createEmptyBorder(15, 20, 15, 20)
    ));
    
    JLabel titulo = new JLabel("REQUISITOS DEL NIVEL " + nivelPasswordActual);
    titulo.setFont(new Font("Consolas", Font.BOLD, 18));
    titulo.setForeground(new Color(0, 255, 100));
    titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
    
    panel.add(titulo);
    panel.add(Box.createRigidArea(new Dimension(0, 10)));
    
    // Los requisitos se actualizarán dinámicamente
    panel.setName("requisitosPanel");
    
    return panel;
}

// ============================================================================
// PANEL INFERIOR CON OPCIONES DE MODIFICACIÓN
// ============================================================================
private JPanel crearPanelInferior() {
    JPanel panel = new JPanel(new BorderLayout());
    panel.setOpaque(false);
    panel.setBorder(BorderFactory.createEmptyBorder(20, 80, 30, 80));
    
    // Panel de opciones (se creará dinámicamente por nivel)
    panelOpciones = new JPanel(new GridLayout(2, 3, 15, 15));
    panelOpciones.setOpaque(false);
    
    // Botones de control
    JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
    controlPanel.setOpaque(false);
    
    JButton btnReset = crearBotonControl("REINICIAR", new Color(150, 0, 150));
    btnReset.addActionListener(e -> resetearPassword());
    
    JButton btnVerificar = crearBotonControl("VERIFICAR", new Color(0, 150, 0));
    btnVerificar.addActionListener(e -> verificarPasswordMejorado());
    
    JButton btnSalir = crearBotonControl("SALIR", new Color(150, 0, 0));
    btnSalir.addActionListener(e -> confirmarSalida());
    
    controlPanel.add(btnReset);
    controlPanel.add(btnVerificar);
    controlPanel.add(btnSalir);
    
    panel.add(panelOpciones, BorderLayout.CENTER);
    panel.add(controlPanel, BorderLayout.SOUTH);
    
    return panel;
}

private JButton crearBotonControl(String texto, Color colorBase) {
    JButton btn = new JButton(texto);
    btn.setFont(new Font("Consolas", Font.BOLD, 16));
    btn.setBackground(colorBase);
    btn.setForeground(Color.WHITE);
    btn.setFocusPainted(false);
    btn.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(colorBase.brighter(), 2),
        BorderFactory.createEmptyBorder(12, 25, 12, 25)
    ));
    
    btn.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseEntered(MouseEvent e) {
            btn.setBackground(colorBase.brighter());
            MenuEstilo.reproducirSonido("/sonidos/hover.wav");
        }
        
        @Override
        public void mouseExited(MouseEvent e) {
            btn.setBackground(colorBase);
        }
    });
    
    return btn;
}

// ============================================================================
// INICIALIZACIÓN DE NIVELES
// ============================================================================
private void inicializarNivel(int nivel) {
    intentosNivel = 3;
    
    switch (nivel) {
        case 1:
            passwordBase = "pass";
            lblObjetivo.setText("OBJETIVO: 60%");
            break;
        case 2:
            passwordBase = "user123";
            lblObjetivo.setText("OBJETIVO: 70%");
            break;
        case 3:
            passwordBase = "admin2024";
            lblObjetivo.setText("OBJETIVO: 80%");
            break;
        case 4:
            passwordBase = "system";
            lblObjetivo.setText("OBJETIVO: 90%");
            break;
    }
    
    campoPassword.setText(passwordBase);
    lblNivel.setText("NIVEL " + nivel + "/" + NIVELES_TOTALES);
    lblIntentos.setText("INTENTOS: " + intentosNivel);
    
    actualizarBarraSeguridad();
    actualizarOpcionesNivel();
    actualizarRequisitos();
}

// ============================================================================
// ACTUALIZACIÓN DE OPCIONES SEGÚN EL NIVEL
// ============================================================================
private void actualizarOpcionesNivel() {
    panelOpciones.removeAll();
    
    // Opciones básicas (todos los niveles)
    panelOpciones.add(crearBotonOpcion("Añadir Mayúscula", () -> {
        String pass = campoPassword.getText();
        if (pass.length() > 0) {
            campoPassword.setText(pass.substring(0, 1).toUpperCase() + pass.substring(1));
        }
    }));
    
    panelOpciones.add(crearBotonOpcion("Añadir Número", () -> {
        campoPassword.setText(campoPassword.getText() + generarNumeroAleatorio());
    }));
    
    panelOpciones.add(crearBotonOpcion("Añadir Símbolo", () -> {
        campoPassword.setText(campoPassword.getText() + generarSimboloAleatorio());
    }));
    
    // Opciones nivel 2+
    if (nivelPasswordActual >= 2) {
        panelOpciones.add(crearBotonOpcion("Mezclar Texto", () -> {
            String pass = campoPassword.getText();
            campoPassword.setText(mezclarCadena(pass));
        }));
    }
    
    // Opciones nivel 3+
    if (nivelPasswordActual >= 3) {
        panelOpciones.add(crearBotonOpcion("Añadir Frase", () -> {
            campoPassword.setText(campoPassword.getText() + "Secure");
        }));
    }
    
    // Opciones nivel 4
    if (nivelPasswordActual >= 4) {
        panelOpciones.add(crearBotonOpcion("Modo Experto", () -> {
            campoPassword.setText(generarPasswordExperto());
        }));
    }
    
    panelOpciones.revalidate();
    panelOpciones.repaint();
}

private JButton crearBotonOpcion(String texto, Runnable accion) {
    JButton btn = new JButton(texto);
    btn.setFont(new Font("Arial", Font.BOLD, 14));
    btn.setBackground(new Color(40, 45, 60));
    btn.setForeground(Color.WHITE);
    btn.setFocusPainted(false);
    btn.setBorder(BorderFactory.createLineBorder(new Color(0, 255, 100), 2));
    
    btn.addActionListener(e -> {
        MenuEstilo.reproducirSonido("/sonidos/click.wav");
        accion.run();
        actualizarBarraSeguridad();
        actualizarRequisitos();
    });
    
    btn.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseEntered(MouseEvent e) {
            btn.setBackground(new Color(60, 70, 90));
            MenuEstilo.reproducirSonido("/sonidos/hover.wav");
        }
        
        @Override
        public void mouseExited(MouseEvent e) {
            btn.setBackground(new Color(40, 45, 60));
        }
    });
    
    return btn;
}

// ============================================================================
// SISTEMA DE CÁLCULO DE SEGURIDAD MEJORADO
// ============================================================================
private int calcularSeguridadMejorado(String password) {
    int puntos = 0;
    
    // Longitud (máximo 30 puntos)
    int longitud = password.length();
    if (longitud >= 12) puntos += 30;
    else if (longitud >= 8) puntos += 20;
    else if (longitud >= 6) puntos += 10;
    else puntos += longitud * 2;
    
    // Mayúsculas (20 puntos)
    if (password.matches(".*[A-Z].*")) {
        long mayusculas = password.chars().filter(Character::isUpperCase).count();
        puntos += Math.min(20, mayusculas * 5);
    }
    
    // Minúsculas (15 puntos)
    if (password.matches(".*[a-z].*")) {
        puntos += 15;
    }
    
    // Números (20 puntos)
    if (password.matches(".*[0-9].*")) {
        long numeros = password.chars().filter(Character::isDigit).count();
        puntos += Math.min(20, numeros * 5);
    }
    
    // Símbolos (25 puntos)
if (password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*")) {
    long simbolos = password.chars()
        .filter(c -> "!@#$%^&*()_+-=[]{};':\"\\|,.<>/?".indexOf(c) >= 0)
        .count();
    puntos += Math.min(25, simbolos * 8);
}
    
    // Variedad (bonus de 10 puntos)
    int tipos = 0;
    if (password.matches(".*[A-Z].*")) tipos++;
    if (password.matches(".*[a-z].*")) tipos++;
    if (password.matches(".*[0-9].*")) tipos++;
    if (password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*")) tipos++;
    
    if (tipos == 4) puntos += 10;
    
    // Penalizaciones
    if (password.matches(".*(123|234|345|456|567|678|789|890|012).*")) puntos -= 15;
    if (password.matches(".*(abc|bcd|cde|def|efg|fgh).*")) puntos -= 10;
    if (password.matches("(.)\\1{2,}")) puntos -= 10; // Caracteres repetidos
    
    return Math.max(0, Math.min(puntos, 100));
}

// ============================================================================
// ACTUALIZACIÓN DE INTERFAZ
// ============================================================================
private void actualizarBarraSeguridad() {
    int seguridad = calcularSeguridadMejorado(campoPassword.getText());
    barraSeguridad.setValue(seguridad);
    barraSeguridad.repaint();
    campoPassword.repaint();
}

private void actualizarRequisitos() {
    // Buscar el panel de requisitos
    JPanel requisitosPanel = null;
    for (Component comp : campoPassword.getParent().getComponents()) {
        if (comp instanceof JPanel && "requisitosPanel".equals(comp.getName())) {
            requisitosPanel = (JPanel) comp;
            break;
        }
    }
    
    if (requisitosPanel == null) return;
    
    // Limpiar requisitos anteriores (excepto el título)
    Component[] components = requisitosPanel.getComponents();
    for (int i = components.length - 1; i >= 0; i--) {
        if (components[i] instanceof JLabel && !((JLabel)components[i]).getText().startsWith("")) {
            requisitosPanel.remove(i);
        }
    }
    
    // Agregar requisitos según el nivel
    String password = campoPassword.getText();
    
    requisitosPanel.add(crearRequisitoLabel("Longitud mínima 8", password.length() >= 8));
    requisitosPanel.add(crearRequisitoLabel("Al menos 1 mayúscula", password.matches(".*[A-Z].*")));
    requisitosPanel.add(crearRequisitoLabel("Al menos 1 número", password.matches(".*[0-9].*")));
    requisitosPanel.add(crearRequisitoLabel("Al menos 1 símbolo", password.matches(".*[!@#$%^&*()].*")));
    
    if (nivelPasswordActual >= 2) {
        requisitosPanel.add(crearRequisitoLabel("Sin secuencias 123", !password.matches(".*(123|234|345).*")));
    }
    
    if (nivelPasswordActual >= 3) {
        requisitosPanel.add(crearRequisitoLabel("Longitud mínima 12", password.length() >= 12));
    }
    
    if (nivelPasswordActual >= 4) {
        requisitosPanel.add(crearRequisitoLabel("Variedad completa", 
            password.matches(".*[A-Z].*") && password.matches(".*[a-z].*") && 
            password.matches(".*[0-9].*") && password.matches(".*[!@#$%^&*()].*")));
    }
    
    requisitosPanel.revalidate();
    requisitosPanel.repaint();
}

private JLabel crearRequisitoLabel(String texto, boolean cumplido) {
    String icono = cumplido ? "✅" : "❌";
    JLabel lbl = new JLabel(icono + " " + texto);
    lbl.setFont(new Font("Consolas", Font.PLAIN, 14));
    lbl.setForeground(cumplido ? new Color(0, 255, 100) : new Color(255, 100, 100));
    lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
    return lbl;
}

// ============================================================================
// VERIFICACIÓN DE CONTRASEÑA
// ============================================================================
private void verificarPasswordMejorado() {
    int seguridad = calcularSeguridadMejorado(campoPassword.getText());
    int objetivo = getObjetivoNivel(nivelPasswordActual);
    
    if (seguridad >= objetivo) {
        // ¡NIVEL SUPERADO!
        MenuEstilo.reproducirSonido("/sonidos/acierto.wav");
        
        if (nivelPasswordActual < NIVELES_TOTALES) {
            mostrarMensajeNivelSuperado();
            nivelPasswordActual++;
            inicializarNivel(nivelPasswordActual);
        } else {
            // ¡JUEGO COMPLETADO!
            mostrarVictoriaFinal();
        }
    } else {
        // Error
        MenuEstilo.reproducirSonido("/sonidos/error.wav");
        intentosNivel--;
        lblIntentos.setText("INTENTOS: " + intentosNivel);
        lblIntentos.setForeground(intentosNivel <= 1 ? Color.RED : Color.YELLOW);
        
        if (intentosNivel <= 0) {
            mostrarGameOver();
        } else {
            mostrarFeedbackNegativo(seguridad, objetivo);
        }
    }
}

private int getObjetivoNivel(int nivel) {
    switch (nivel) {
        case 1: return 60;
        case 2: return 70;
        case 3: return 80;
        case 4: return 90;
        default: return 60;
    }
}

// ============================================================================
// MENSAJES Y DIÁLOGOS
// ============================================================================
private void mostrarMensajeNivelSuperado() {
    JOptionPane.showMessageDialog(this,
        "🎉 ¡NIVEL " + (nivelPasswordActual) + " SUPERADO!\n\n" +
        "Seguridad alcanzada: " + calcularSeguridadMejorado(campoPassword.getText()) + "%\n" +
        "Contraseña: " + campoPassword.getText() + "\n\n" +
        "Preparando nivel " + (nivelPasswordActual + 1) + "...",
        "¡Excelente!",
        JOptionPane.INFORMATION_MESSAGE);
}

private void mostrarFeedbackNegativo(int actual, int objetivo) {
    JOptionPane.showMessageDialog(this,
        "⚠️ Contraseña insuficiente\n\n" +
        "Seguridad actual: " + actual + "%\n" +
        "Objetivo: " + objetivo + "%\n" +
        "Te faltan " + (objetivo - actual) + " puntos\n\n" +
        "Intentos restantes: " + intentosNivel,
        "Intenta de nuevo",
        JOptionPane.WARNING_MESSAGE);
}

private void mostrarGameOver() {
    JOptionPane.showMessageDialog(this,
        "GAME OVER\n\n" +
        "Te quedaste sin intentos en el nivel " + nivelPasswordActual + "\n" +
        "Mario Von Richter ha hackeado el sistema.\n\n" +
        "¿Deseas reintentar?",
        "Nivel Fallido",
        JOptionPane.ERROR_MESSAGE);
    
    storyState.marcarCapitulo(1, false);
    finalizarCapitulo(1, false, () -> new minijuego2(storyState));
}

private void mostrarVictoriaFinal() {
    JOptionPane.showMessageDialog(this,
        "¡TODOS LOS NIVELES COMPLETADOS!\n\n" +
        nombreJugador + ": -¡Lo logré! He creado contraseñas ultra-seguras.\n" +
        "Director: -Impresionante. Mario Von Richter no podrá romper estas defensas.\n\n" +
        "RECOMPENSA: Contrato permanente en CyberShield International",
        "¡VICTORIA TOTAL!",
        JOptionPane.INFORMATION_MESSAGE);
    
    storyState.marcarCapitulo(1, true);
    finalizarCapitulo(1, true, () -> new minijuego2(storyState));
}

// ============================================================================
// UTILIDADES
// ============================================================================
private void resetearPassword() {
    campoPassword.setText(passwordBase);
    actualizarBarraSeguridad();
    actualizarRequisitos();
    MenuEstilo.reproducirSonido("/sonidos/click.wav");
}

private Color getColorSeguridad(int seguridad) {
    if (seguridad >= 80) return new Color(0, 255, 100);
    if (seguridad >= 60) return new Color(255, 220, 0);
    if (seguridad >= 40) return new Color(255, 150, 0);
    return new Color(255, 50, 50);
}

private String getNivelSeguridad(int seguridad) {
    if (seguridad >= 90) return "ULTRA SEGURA";
    if (seguridad >= 80) return "MUY SEGURA";
    if (seguridad >= 60) return "SEGURA";
    if (seguridad >= 40) return "MEDIA";
    return "DÉBIL";
}

private int generarNumeroAleatorio() {
    return new Random().nextInt(10);
}

private String generarSimboloAleatorio() {
    String simbolos = "!@#$%^&*()_+-=[]{}";
    return String.valueOf(simbolos.charAt(new Random().nextInt(simbolos.length())));
}

private String mezclarCadena(String input) {
    char[] caracteres = input.toCharArray();
    for (int i = caracteres.length - 1; i > 0; i--) {
        int j = new Random().nextInt(i + 1);
        char temp = caracteres[i];
        caracteres[i] = caracteres[j];
        caracteres[j] = temp;
    }
    return new String(caracteres);
}

private String generarPasswordExperto() {
    String base = campoPassword.getText();
    StringBuilder resultado = new StringBuilder();
    
    // Añadir variedad automática
    resultado.append(base.substring(0, 1).toUpperCase());
    resultado.append(base.substring(1));
    resultado.append(generarNumeroAleatorio());
    resultado.append(generarSimboloAleatorio());
    resultado.append("Sec");
    resultado.append(generarNumeroAleatorio());
    resultado.append(generarSimboloAleatorio());
    
    return resultado.toString();
}
    
    // ============================================================================
    // UTILIDADES Y MÉTODOS DE CONTROL
    // ============================================================================
    /**
     * Muestra confirmación antes de salir del juego
     */
    private void confirmarSalida() {
        int confirmacion = JOptionPane.showConfirmDialog(
            this,
            "¿Estás seguro de que quieres salir del juego?\nTu progreso no se guardará.",
            "Confirmar Salida",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );
        
        if (confirmacion == JOptionPane.YES_OPTION) {
            MenuEstilo.reproducirSonido("/sonidos/click.wav");
            dispose();
         JFrame ventanaMision = new JFrame("Misiones - CYBERSTEAL");
ventanaMision.setSize(1000, 700);
ventanaMision.setLocationRelativeTo(null);
ventanaMision.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
ventanaMision.setContentPane(new SeleccionMisionPanel(storyState, ventanaMision));
ventanaMision.setVisible(true);

        }
    }
    
    /**
     * Reinicia el capítulo desde el principio
     */
    @Override
    protected void reiniciar() {
        dispose();
        new minijuego1(storyState);
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    // ============================================================================
    // MÉTODO PÚBLICO PARA AGREGAR ESCENAS DINÁMICAMENTE (OPCIONAL)
    // ============================================================================
    /**
     * Permite agregar escenas después de la inicialización.
     * Útil para contenido dinámico o DLC.
     * 
     * @param rutaImagen Ruta completa de la imagen de fondo
     * @param texto Texto del diálogo
     */
    public void agregarEscenaDinamica(String rutaImagen, String texto) {
        Escena nueva = new Escena(rutaImagen, texto);
        JPanel panel = crearPanelDialogo(nueva);
        contenedorPrincipal.add(panel, "escena_dinamica_" + escenas.size());
        escenas.add(nueva);
    }
    
    /**
     * Permite agregar un minijuego dinámicamente.
     * 
     * @param accionMinijuego Método que ejecuta el minijuego
     */
    public void agregarMinijuegoDinamico(Runnable accionMinijuego) {
        Escena nueva = new Escena(accionMinijuego);
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.BLACK);
        contenedorPrincipal.add(panel, "minijuego_dinamico_" + escenas.size());
        escenas.add(nueva);
    }
    
    
    
    
    
    // ============================================================================
// FRASES DE CIBERSEGURIDAD - DATOS HIPER INTERESANTES
// ============================================================================

/**
 * Genera frases aleatorias de ciberseguridad con datos curiosos y avanzados
*/
private String obtenerFraseCyberAleatoria() {
    String[] frasesCyber = {
        // CONTRASEÑAS AVANZADAS
        "¿Sabías que 'Password123!' puede crackearse en 3 horas? Mejor usa frases largas como 'MiPerroTiene7Años!'",
        "Pro tip: Las contraseñas deben ser como cepillos de dientes: cámbialas cada 3 meses y no las compartas",
        "El 80% de las brechas usan contraseñas robadas o débiles. ¡La longitud vence a la complejidad!",
        "¿Por qué 'Hunter2' es mala? Los hackers tienen 'diccionarios de contraseñas' con millones de variantes",
        
        //️ HACKING PSICOLÓGICO
        "El 'social engineering' causa el 98% de los hackeos. Los humanos somos el eslabón más débil",
        "Mario Von Richter probablemente usaría 'phishing': emails falsos que parecen legítimos para robar credenciales",
        "¿Sabías que los ataques 'zero-day' explotan vulnerabilidades que NI SIQUIERA LOS FABRICANTES conocen?",
        
        // DATOS CURIOSOS AVANZADOS
        "La 'criptografía cuántica' podría hacer obsoletas TODAS las contraseñas actuales en 10 años",
        "En la dark web, las credenciales de bancos valen hasta $500 USD por cuenta",
        "El primer virus informático se llamó 'Creeper' en 1971 y solo mostraba 'I'm the creeper, catch me if you can!'",
        
        //  SEGURIDAD PRÁCTICA
        "El 'doxing' es cuando hackers publican tu información personal online como venganza",
        "Los 'keyloggers' pueden registrar cada tecla que presionas sin que lo sepas",
        "¿Sabías que el 'ping' original era un sonar naval adaptado para redes? Ahora los hackers lo usan para escanear",
        
        //CONCEPTOS AVANZADOS
        "La 'esteganografía' es ocultar mensajes dentro de imágenes - como hacerlo con Mario Von Richter",
        "Un 'APT' (Advanced Persistent Threat) es un ataque continuo de meses, no un hackeo rápido",
        "El 'bug bounty' paga millones a hackers éticos que encuentran vulnerabilidades",
        
        // HISTORIAS REALES
        "En 2016, hackers robaron $81 millones del Banco de Bangladesh usando solo transferencias SWIFT",
        "El virus 'Stuxnet' fue el primer arma digital que dañó hardware físico - centrifugadoras nucleares",
        "¿Sabías que el 95% de los ataques exitosos necesitan solo 2 técnicas de las 300 conocidas?",
        
        // FUTURO CYBER
        "La 'biometría conductual' analiza cómo tecleas o mueves el mouse para identificarte",
        "Los 'honeypots' son sistemas trampa que fingen ser vulnerables para estudiar hackers",
        "El 'GDPR' multa hasta el 4% de ingresos globales por filtraciones de datos en Europa",
        
        // CONSEJOS PARA EL JUEGO
        "Contra Mario Von Richter: mezcla símbolos raros como § o ¶ que no están en diccionarios comunes",
        "¿Sabías que 'Password1!' es la contraseña más común en empresas?",
        "Los humanos somos predecibles: el 30% de contraseñas contienen fechas de nacimiento",
        
        // CURIOSIDADES TÉCNICAS
        "El 'hashing' convierte contraseñas en strings irreversibles - por eso los sitios no 'ven' tu password",
        "La 'sal' en criptografía son datos random agregados a contraseñas antes del hashing",
        "¿Sabías que '123456' sigue siendo la contraseña más usada mundialmente desde 2013?",
        
        // ⚡ SEGURIDAD OFENSIVA
        "Un 'pentester' (hacker ético) gana hasta $150,000 anuales probando seguridad",
        "El 'OSINT' es buscar información pública - hackers usan tus redes sociales para adivinar contraseñas",
        "Los 'rainbow tables' son tablas precalculadas para crackear hashes rápidamente"
    };
    

    List<String> listaFrases = new ArrayList<>(Arrays.asList(frasesCyber));
    Collections.shuffle(listaFrases, new Random(System.nanoTime() + Thread.currentThread().getId()));
    
    // Elegir la primera frase de la lista mezclada
    return listaFrases.get(0);
}    
//Acceder a un elemento especifico en el vector
// EXPLICACIÓN: get(index) accede a elemento en posición específica
    
    /**
 * Precarga imágenes de forma progresiva para mostrar múltiples frases
 */
private void precargarImagenesProgresivo(int batch) {
    int inicio = batch * 5; // 5 imágenes por lote
    int fin = Math.min(inicio + 5, escenas.size());
    
    for (int i = inicio; i < fin; i++) {
        Escena escena = escenas.get(i);
        if (!escena.esMinijuego && escena.imagen != null) {
            // Tu código de precarga normal aquí
            precargarImagenIndividual(escena.imagen);
        }
    }
}

private void precargarImagenIndividual(String rutaImagen) {
    
    try {
        java.net.URL url = getClass().getResource(rutaImagen);
        if (url != null) {
           
        }
    } catch (Exception e) {
        System.err.println("Error precargando " + rutaImagen);
    }
}


private String convertirEmojisAHTML(String texto) {
    return "<html><body style='font-family: Arial, sans-serif; font-size: 18pt; color: white;'>" +
           texto
               .replace("⚠", "&#9888;")
               .replace("🔐", "&#128275;")
               .replace("⏳", "&#9203;")
               .replace("✅", "&#9989;")
               .replace("❌", "&#10060;")
               .replace("🚪", "&#128682;") +
           "</body></html>";
}



   }