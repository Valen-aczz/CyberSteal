import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.imageio.ImageIO;
import java.util.*;
import java.util.List;
import javax.swing.Timer;

/**
 * ============================================================================
 * CAPÍTULO 2: THE ZERO-TRUST - VERSIÓN ÉPICA MEJORADA
 * Sistema de infiltración con estilo rojo dominante y mejor jugabilidad
 * ============================================================================
 */
public class minijuego2 extends BaseCapituloFrame {
    
    // Variables principales
    private JPanel contenedorPrincipal;
    private CardLayout cardLayout;
    private List<Escena> escenas;
    private int escenaActual = 0;
    private String nombreJugador;
    private String carpetaGenero;
    
    // Variables del minijuego mejorado
    private int nivelActual = 1;
    private static final int NIVELES_TOTALES = 3;
    private int intentosRestantes = 3;
    private List<String> fragmentosRecolectados = new ArrayList<>();
    private Timer timerNivel;
    private int tiempoRestante = 60;
    private JLabel lblTiempo;
    private JLabel lblIntentos;
    private JProgressBar barraProgreso;
    
    // Colores rojos dominantes
    private final Color ROJO_PRINCIPAL = new Color(200, 0, 0);
    private final Color ROJO_OSCURO = new Color(80, 0, 0);
    private final Color ROJO_NEON = new Color(255, 40, 40);
    private final Color NEGRO_CYBER = new Color(10, 5, 5);
    
    // Fragmentos de código por nivel
    private Map<Integer, List<FragmentoCodigo>> fragmentosPorNivel = new HashMap<>();

    @Override
    protected void reiniciar() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    // ============================================================================
    // CLASE FRAGMENTO DE CÓDIGO
    // ============================================================================
    private static class FragmentoCodigo {
        String codigo;
        String descripcion;
        boolean esVerdadero;
        String pista;
        
        FragmentoCodigo(String codigo, String descripcion, boolean esVerdadero, String pista) {
            this.codigo = codigo;
            this.descripcion = descripcion;
            this.esVerdadero = esVerdadero;
            this.pista = pista;
        }
    }
    
    // ============================================================================
    // CLASE ESCENA
    // ============================================================================
    private static class Escena {
        String imagen;
        String dialogo;
        boolean esMinijuego;
        Runnable minijuegoAccion;
        
        Escena(String imagen, String dialogo) {
            this.imagen = imagen;
            this.dialogo = dialogo;
            this.esMinijuego = false;
        }
        
        Escena(Runnable minijuegoAccion) {
            this.esMinijuego = true;
            this.minijuegoAccion = minijuegoAccion;
        }
    }

    // ============================================================================
    // CONSTRUCTOR PRINCIPAL
    // ============================================================================
    public minijuego2(StoryState state) {
        super(state, "Capítulo 2: The Zero-Trust", "");
        MenuEstilo.detenerMusicaFondo();
        
        this.nombreJugador = state.getNombre();
        this.carpetaGenero = state.getGenero().equals("mujer") ? "mujer" : "hombre";
        this.escenas = new ArrayList<>();
        
        // Mostrar instrucciones inmediatamente
        mostrarInstruccionesIniciales();
        
        inicializarFragmentos();
        
        centerPanel.setLayout(new BorderLayout());
        centerPanel.setBackground(NEGRO_CYBER);
        
        JPanel panelCarga = crearPantallaCarga();
        centerPanel.add(panelCarga, BorderLayout.CENTER);
        
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);
        
        iniciarCargaAsincrona();
    }
    
    // ============================================================================
    // INSTRUCCIONES INICIALES - JOptionPane épico
    // ============================================================================
    private void mostrarInstruccionesIniciales() {
        // Crear un panel personalizado para las instrucciones
        JPanel panelInstrucciones = new JPanel(new BorderLayout(10, 10));
        panelInstrucciones.setBackground(NEGRO_CYBER);
        panelInstrucciones.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Título con estilo cyber rojo
        JLabel titulo = new JLabel("INFILTRACIÓN ZERO-TRUST", SwingConstants.CENTER);
        titulo.setFont(new Font("Consolas", Font.BOLD, 24));
        titulo.setForeground(ROJO_NEON);
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        // Texto de instrucciones
        JTextArea instrucciones = new JTextArea(
            "OBJETIVO:\n" +
            "• Infiltrarte en el sistema de Mario Von Richter\n" +
            "• Identificar fragmentos LEGÍTIMOS del código de la vacuna\n" +
            "• Evitar fragmentos CORRUPTOS que contienen virus\n\n" +
            
            "MECÁNICAS:\n" +
            "• Tienes 3 intentos por nivel\n" +
            "• 60 segundos por nivel\n" +
            "• Cada error: -1 intento y -10 segundos\n" +
            "• Necesitas TODOS los fragmentos legítimos para avanzar\n\n" +
            
            "CONSEJOS:\n" +
            "• Analiza el contexto del código\n" +
            "• Los nombres sospechosos suelen ser maliciosos\n" +
            "• Las funciones destructivas son siempre corruptas\n" +
            "• Mantén la calma y piensa antes de clickear"
        );
        instrucciones.setFont(new Font("Consolas", Font.PLAIN, 14));
        instrucciones.setForeground(Color.WHITE);
        instrucciones.setBackground(new Color(30, 0, 0));
        instrucciones.setEditable(false);
        instrucciones.setLineWrap(true);
        instrucciones.setWrapStyleWord(true);
        instrucciones.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ROJO_NEON, 2),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        // Botón épico
        JButton btnEntendido = new JButton("INICIAR INFILTRACIÓN");
        btnEntendido.setFont(new Font("Consolas", Font.BOLD, 16));
        btnEntendido.setBackground(ROJO_PRINCIPAL);
        btnEntendido.setForeground(Color.WHITE);
        btnEntendido.setFocusPainted(false);
        btnEntendido.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ROJO_NEON, 3),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        
        panelInstrucciones.add(titulo, BorderLayout.NORTH);
        panelInstrucciones.add(new JScrollPane(instrucciones), BorderLayout.CENTER);
        panelInstrucciones.add(btnEntendido, BorderLayout.SOUTH);
        
        // Crear diálogo personalizado
        JDialog dialogoInstrucciones = new JDialog(this, "⚡ PROTOCOLO DE INFILTRACIÓN", true);
        dialogoInstrucciones.setSize(700, 600);
        dialogoInstrucciones.setLocationRelativeTo(this);
        dialogoInstrucciones.setContentPane(panelInstrucciones);
        
        btnEntendido.addActionListener(e -> dialogoInstrucciones.dispose());
        
        dialogoInstrucciones.setVisible(true);
    }
    
    // ============================================================================
    // INICIALIZACIÓN DE FRAGMENTOS POR NIVEL
    // ============================================================================
    private void inicializarFragmentos() {
        // ========== NIVEL 1: ACCESO INICIAL ==========
        List<FragmentoCodigo> nivel1 = new ArrayList<>();
        nivel1.add(new FragmentoCodigo(
            "import firewall.ZeroTrust;",
            "Librería de firewall Zero-Trust",
            true,
            "Importación legítima - ZeroTrust es tecnología defensiva"
        ));
        nivel1.add(new FragmentoCodigo(
            "import virus.payload.hidden;",
            "Módulo de carga viral oculta",
            false,
            "ALERTA: 'virus.payload.hidden' es claramente malicioso"
        ));
        nivel1.add(new FragmentoCodigo(
            "class VacunaDigital {",
            "Clase principal de la vacuna",
            true,
            "Estructura base necesaria para la vacuna digital"
        ));
        nivel1.add(new FragmentoCodigo(
            "class BackdoorInyector {",
            "Clase de inyección de backdoor",
            false,
            "ALERTA: 'BackdoorInyector' es una amenaza directa"
        ));
        fragmentosPorNivel.put(1, nivel1);
        
        // ========== NIVEL 2: PROTOCOLO PRINCIPAL ==========
        List<FragmentoCodigo> nivel2 = new ArrayList<>();
        nivel2.add(new FragmentoCodigo(
            "    void neutralizarVirus() {",
            "Método de neutralización viral",
            true,
            "Función defensiva con propósito claro"
        ));
        nivel2.add(new FragmentoCodigo(
            "    void propagarInfeccion() {",
            "Método de propagación de infección",
            false,
            "ALERTA: Una vacuna NUNCA propagaría infecciones"
        ));
        nivel2.add(new FragmentoCodigo(
            "        validarIntegridad();",
            "Validación de integridad del sistema",
            true,
            "Las vacunas siempre validan integridad antes de actuar"
        ));
        nivel2.add(new FragmentoCodigo(
            "        eliminarBackups();",
            "Eliminación de copias de seguridad",
            false,
            "ALERTA CRÍTICA: ¡NUNCA elimines backups!"
        ));
        nivel2.add(new FragmentoCodigo(
            "        restaurarSistema();",
            "Restauración del sistema",
            true,
            "Restaurar es el objetivo final de cualquier vacuna"
        ));
        fragmentosPorNivel.put(2, nivel2);
        
        // ========== NIVEL 3: FINALIZACIÓN ==========
        List<FragmentoCodigo> nivel3 = new ArrayList<>();
        nivel3.add(new FragmentoCodigo(
            "        log(\"Virus neutralizado\");",
            "Registro de éxito",
            true,
            "Confirmación de operación exitosa"
        ));
        nivel3.add(new FragmentoCodigo(
            "        System.exit(1);",
            "Cierre forzado del sistema",
            false,
            "ALERTA: Exit code 1 indica error, no éxito"
        ));
        nivel3.add(new FragmentoCodigo(
            "        generarReporte();",
            "Generación de reporte",
            true,
            "Documentación esencial para auditoría"
        ));
        nivel3.add(new FragmentoCodigo(
            "        deshabilitarFirewall();",
            "Desactivación del firewall",
            false,
            "ALERTA CRÍTICA: ¡Nunca desactives defensas!"
        ));
        nivel3.add(new FragmentoCodigo(
            "    }\n}",
            "Cierre de estructuras",
            true,
            "Sintaxis correcta de cierre"
        ));
        nivel3.add(new FragmentoCodigo(
            "    // BACKDOOR_ACTIVA = true",
            "Comentario de backdoor",
            false,
            "ALERTA: Comentarios sobre backdoors son maliciosos"
        ));
        fragmentosPorNivel.put(3, nivel3);
    }
    
    // ============================================================================
    // PANTALLA DE CARGA CON ESTILO ROJO
    // ============================================================================
private JPanel crearPantallaCarga() {
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.setBackground(NEGRO_CYBER);
    panel.setBorder(BorderFactory.createEmptyBorder(80, 0, 80, 0));
    
    // sin el filtro ese
    JLabel lblImagenCarga = new JLabel("", SwingConstants.CENTER);
    try {
        String imagenPath = "/images/" + carpetaGenero + "/logo2.png"; 
        java.net.URL url = getClass().getResource(imagenPath);
        
        if (url != null) {
            ImageIcon iconoOriginal = new ImageIcon(url);
            Image imagenRedimensionada = iconoOriginal.getImage()
                .getScaledInstance(200, 200, Image.SCALE_SMOOTH);
            
            // me vuelo lo del filtro ese
            lblImagenCarga.setIcon(new ImageIcon(imagenRedimensionada));
        } else {
            throw new Exception(" no encontré la imagen");
        }
    } catch (Exception e) {
        lblImagenCarga.setText(" ");
        lblImagenCarga.setFont(new Font("Arial", Font.BOLD, 100));
        lblImagenCarga.setForeground(ROJO_NEON);
    }
    lblImagenCarga.setAlignmentX(Component.CENTER_ALIGNMENT);
    
    JLabel lblTexto = new JLabel("INICIANDO PROTOCOLO ZERO-TRUST...", SwingConstants.CENTER);
    lblTexto.setFont(new Font("Consolas", Font.BOLD, 24));
    lblTexto.setForeground(ROJO_NEON);
    lblTexto.setAlignmentX(Component.CENTER_ALIGNMENT);
    
    // la barrita de progreso
    JProgressBar barraProgreso = new JProgressBar();
    barraProgreso.setIndeterminate(true);
    barraProgreso.setPreferredSize(new Dimension(400, 25));
    barraProgreso.setMaximumSize(new Dimension(400, 25));
    barraProgreso.setBackground(ROJO_OSCURO);
    barraProgreso.setForeground(ROJO_NEON);
    barraProgreso.setBorder(BorderFactory.createLineBorder(ROJO_NEON, 2));
    barraProgreso.setAlignmentX(Component.CENTER_ALIGNMENT);
    
    // Texto secundario
    JLabel lblSubtexto = new JLabel("Preparando sistemas de infiltración...", SwingConstants.CENTER);
    lblSubtexto.setFont(new Font("Consolas", Font.PLAIN, 14));
    lblSubtexto.setForeground(new Color(200, 100, 100));
    lblSubtexto.setAlignmentX(Component.CENTER_ALIGNMENT);
    
    panel.add(Box.createRigidArea(new Dimension(0, 30)));
    panel.add(lblImagenCarga);
    panel.add(Box.createRigidArea(new Dimension(0, 40)));
    panel.add(lblTexto);
    panel.add(Box.createRigidArea(new Dimension(0, 20)));
    panel.add(barraProgreso);
    panel.add(Box.createRigidArea(new Dimension(0, 15)));
    panel.add(lblSubtexto);
    
    return panel;
} //toca reemplazar toda esta vuelta Diomio

    private void iniciarCargaAsincrona() {
        SwingWorker<Void, String> cargador = new SwingWorker<Void, String>() {
            @Override
            protected Void doInBackground() throws Exception {
                publish("BYPASEANDO FIREWALL PRINCIPAL...");
                Thread.sleep(1500);
                
                publish("CARGANDO FRAGMENTOS DE VACUNA...");
                Thread.sleep(1500);
                agregarEscenas();
                
                publish("INICIANDO MODO STEALTH...");
                Thread.sleep(1500);
                
                publish("¡SISTEMAS LISTOS! INICIANDO INFILTRACIÓN...");
                Thread.sleep(1200);
                
                return null;
            }
            
            @Override
            protected void done() {
                centerPanel.removeAll();
                inicializarUI();
                mostrarEscena();
                centerPanel.revalidate();
                centerPanel.repaint();
            }
        };
        
        cargador.execute();
    }
    
    // ============================================================================
    // CONFIGURACIÓN DE ESCENAS (más cortas para ir directo al minijuego)
    // ============================================================================
    private void agregarEscenas() {
        // Solo escenas esenciales para contexto rápido
        escenas.add(new Escena(
            img("trabajando"),
            nombreJugador + ": -Mario Von Richter dejó un virus... Debo infiltrarme en su red y construir la vacuna desde dentro."
        ));
        
        
        
        
         escenas.add(new Escena(
            img("trabajando"),
            "Creí que todo había terminado cuando atrapé a Mario Von Ritcher… Pero el sistema del banco se está viniendo abajo. Archivos desaparecen, contraseñas cambian solas… ¡No puede ser, Mario Von Richter dejó un virus infiltrado!"
        ));
        
        escenas.add(new Escena(
            img("llamada"),
            nombreJugador + ": -Si quiero detenerlo, debo entrar a la red enemiga y eliminar el virus desde su núcleo...\n" +
"suspira — ‘Acceso denegado. Amenaza activa.’ ... Esto será más complicado de lo que pensaba."
        ));
        
        escenas.add(new Escena(
            img("J21"),
            "*El teléfono suena de nuevo* Ugh... ¿qué ahora?"
        ));
        
        escenas.add(new Escena(
            img("J22"),
            nombreJugador +   ": -¿Ahora qué? ¿Es mucho pedir un día sin emergencias tecnológicas?\n" +  
           "Asistente : Tranquilo,  " + nombreJugador + ": esto no puede esperar."
        ));
        
        escenas.add(new Escena(
            img("J23"),
            "Asistente : -" + nombreJugador + ", Tenemos una situación crítica. Mario Von Richter alcanzó a liberar un virus antes de caer. Está afectando nuestros sistemas principales. \n"
                    + nombreJugador+  ": ...Sabía que no se rendiría tan fácil."
        ));
        
        escenas.add(new Escena(
            img("J25"),
            nombreJugador + ": -¿Un virus? Vamos, ¿qué tan grave puede ser? Seguro puedo limpiarlo en unos minutos…\n"
                + "Director (off):  ¡Mucho peor de lo que crees!\n" +
            "SISTEMA: SOLO 3 INTENTOS POR NIVEL\n️ 60 SEGUNDOS POR INFILTRACIÓN\n️ FRAGMENTOS CORRUPTOS RESTAN TIEMPO"
        ));
        
        escenas.add(new Escena(
            img("J24"),
            "Asistente : -¡No entiendes! Está destruyendo nuestros sistemas de seguridad. En seis horas llegará a las bóvedas digitales.\n" 
            + nombreJugador + ": ¡Seis horas! Eso no es nada…"
+ " PISTA: La vacuna debe ensamblarse en el orden correcto... o todo estará perdido."

        ));
        
        // ========== INICIO DEL MINIJUEGO ==========
    
        escenas.add(new Escena(this::iniciarMinijuegoMejorado));
    }
    
    private String img(String nombreImagen) {
        return "/images/" + carpetaGenero + "/" + nombreImagen + ".png";
    }
    
    // ============================================================================
    // INICIALIZACIÓN DE LA INTERFAZ GRÁFICA
    // ============================================================================
    private void inicializarUI() {
        cardLayout = new CardLayout();
        contenedorPrincipal = new JPanel(cardLayout);
        contenedorPrincipal.setBackground(NEGRO_CYBER);
        
        for (int i = 0; i < escenas.size(); i++) {
            Escena escena = escenas.get(i);
            JPanel panel;
            
            if (escena.esMinijuego) {
                panel = new JPanel(new BorderLayout());
                panel.setBackground(NEGRO_CYBER);
            } else {
                panel = crearPanelDialogo(escena);
            }
            
            contenedorPrincipal.add(panel, "escena_" + i);
        }
        
        centerPanel.setLayout(new BorderLayout());
        centerPanel.add(contenedorPrincipal, BorderLayout.CENTER);
    }
    
    // ============================================================================
    // PANELES DE DIÁLOGO CON ESTILO ROJO
    // ============================================================================
    private JPanel crearPanelDialogo(Escena escena) {
        JPanel panelFondo = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                try {
                    Image img = ImageIO.read(getClass().getResource(escena.imagen));
                    g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
                } catch (Exception e) {
                    // Fondo rojo de fallback
                    g.setColor(ROJO_OSCURO);
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        
        JPanel cajaDialogo = crearCajaDialogo(escena.dialogo);
        panelFondo.add(cajaDialogo, BorderLayout.SOUTH);
        
        return panelFondo;
    }
    
    private JPanel crearCajaDialogo(String texto) {
        JPanel contenedor = new JPanel(new BorderLayout());
        contenedor.setOpaque(false);
        contenedor.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JPanel caja = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setColor(new Color(0, 0, 0, 220));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                
                // Borde rojo neon
                g2d.setColor(ROJO_NEON);
                g2d.setStroke(new BasicStroke(3f));
                g2d.drawRoundRect(2, 2, getWidth()-4, getHeight()-4, 20, 20);
                g2d.dispose();
            }
        };
        caja.setOpaque(false);
        caja.setPreferredSize(new Dimension(1500, 150));
        
        JTextArea areaTexto = new JTextArea(texto);
        areaTexto.setEditable(false);
        areaTexto.setLineWrap(true);
        areaTexto.setWrapStyleWord(true);
        areaTexto.setFont(new Font("Arial", Font.BOLD, 18));
        areaTexto.setForeground(Color.WHITE);
        areaTexto.setOpaque(false);
        areaTexto.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JScrollPane scroll = new JScrollPane(areaTexto);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        
        JPanel panelBotones = crearPanelBotones();
        
        caja.add(scroll, BorderLayout.CENTER);
        caja.add(panelBotones, BorderLayout.EAST);
        contenedor.add(caja, BorderLayout.SOUTH);
        
        return contenedor;
    }
    
  private JPanel crearPanelBotones() {
    JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 20));
    panel.setOpaque(false);
    
    JButton btnContinuar = new JButton("CONTINUAR");
    estilizarBotonRojo(btnContinuar, 140, 40);
    btnContinuar.addActionListener(e -> siguienteEscena());
    
    JButton btnSaltar = new JButton("SALTAR AL JUEGO");
    estilizarBotonRojo(btnSaltar, 180, 40);
    btnSaltar.addActionListener(e -> saltarAlJuego());
    
    // ⭐ NUEVO: Botón Salir
    JButton btnSalir = new JButton("SALIR");
    estilizarBotonRojo(btnSalir, 100, 40);
    btnSalir.setBackground(new Color(100, 100, 100, 200)); // Gris para diferenciarlo
    btnSalir.setBorder(BorderFactory.createLineBorder(Color.WHITE, 3));
    btnSalir.addActionListener(e -> confirmarSalida());
    
    panel.add(btnContinuar);
    panel.add(btnSaltar);
    panel.add(btnSalir);
    
    return panel;
}

// NUEVA FUNCIÓN: Confirmar salida (similar a minijuego1)
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

    
    private void estilizarBotonRojo(JButton btn, int ancho, int alto) {
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setBackground(ROJO_PRINCIPAL);
        btn.setForeground(Color.WHITE);
        btn.setBorder(BorderFactory.createLineBorder(ROJO_NEON, 3));
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(ancho, alto));
        btn.setOpaque(true);
        
        // Efecto hover
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(ROJO_NEON);
                MenuEstilo.reproducirSonido("/sonidos/hover.wav");
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(ROJO_PRINCIPAL);
            }
        });
    }
    
    // ============================================================================
    // NAVEGACIÓN MEJORADA
    // ============================================================================
    private void mostrarEscena() {
        cardLayout.show(contenedorPrincipal, "escena_" + escenaActual);
        Escena escena = escenas.get(escenaActual);
        
        contenedorPrincipal.revalidate();
        contenedorPrincipal.repaint();
        
        if (escena.esMinijuego && escena.minijuegoAccion != null) {
            escena.minijuegoAccion.run();
        }
        
        if (escenaActual > 0) {
            MenuEstilo.reproducirSonido("/sonidos/click.wav");
        }
    }
    
    private void siguienteEscena() {
        if (escenaActual < escenas.size() - 1) {
            escenaActual++;
            mostrarEscena();
        }
    }
    
    private void saltarAlJuego() {
        // Saltar directamente al minijuego
        escenaActual = escenas.size() - 1;
        mostrarEscena();
    }
    
    // ============================================================================
    // MINIJUEGO MEJORADO CON ESTILO ROJO BONITO
    // ============================================================================
    private void iniciarMinijuegoMejorado() {
        Component panelActual = contenedorPrincipal.getComponent(escenaActual);
        
        if (panelActual instanceof JPanel) {
            JPanel panel = (JPanel) panelActual;
            panel.removeAll();
            panel.setLayout(new BorderLayout());
            panel.add(crearUIMinijuegoEpico(), BorderLayout.CENTER);
            panel.revalidate();
            panel.repaint();
            
            iniciarTimer();
        }
    }
    
    private JPanel crearUIMinijuegoEpico() {
        JPanel panel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Fondo animado con efecto de scanning
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(NEGRO_CYBER);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                // Efecto de grid rojo
                g2d.setColor(new Color(100, 0, 0, 30));
                for (int x = 0; x < getWidth(); x += 50) {
                    g2d.drawLine(x, 0, x, getHeight());
                }
                for (int y = 0; y < getHeight(); y += 50) {
                    g2d.drawLine(0, y, getWidth(), y);
                }
                
                // Efecto de scanning
                long time = System.currentTimeMillis() / 20;
                int scanY = (int) (time % getHeight());
                g2d.setColor(new Color(255, 0, 0, 80));
                g2d.fillRect(0, scanY, getWidth(), 3);
            }
        };
        
        panel.add(crearHeaderEpico(), BorderLayout.NORTH);
        panel.add(crearPanelFragmentosEpico(), BorderLayout.CENTER);
        panel.add(crearPanelInferiorEpico(), BorderLayout.SOUTH);
        
        return panel;
    }
    
    // ============================================================================
    // HEADER ÉPICO CON ESTADÍSTICAS
    // ============================================================================
    private JPanel crearHeaderEpico() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(20, 0, 0));
        header.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ROJO_NEON, 2),
            BorderFactory.createEmptyBorder(15, 30, 15, 30)
        ));
        
        // Título con glow effect
        JLabel titulo = new JLabel("ZERO-TRUST INFILTRATION - NIVEL " + nivelActual, SwingConstants.CENTER);
        titulo.setFont(new Font("Consolas", Font.BOLD, 28));
        titulo.setForeground(ROJO_NEON);
        
        // Panel de stats con estilo cyber
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        statsPanel.setOpaque(false);
        
        lblTiempo = new JLabel("" + tiempoRestante + "s", SwingConstants.CENTER);
        lblIntentos = new JLabel("" + intentosRestantes + "/3", SwingConstants.CENTER);
        JLabel lblFragmentos = new JLabel("" + fragmentosRecolectados.size(), SwingConstants.CENTER);
        
        for (JLabel lbl : new JLabel[]{lblTiempo, lblIntentos, lblFragmentos}) {
            lbl.setFont(new Font("Consolas", Font.BOLD, 20));
            lbl.setForeground(Color.WHITE);
            lbl.setBorder(BorderFactory.createLineBorder(ROJO_PRINCIPAL, 2));
            lbl.setOpaque(true);
            lbl.setBackground(new Color(40, 0, 0));
        }
        
        statsPanel.add(lblTiempo);
        statsPanel.add(lblIntentos);
        statsPanel.add(lblFragmentos);
        
        header.add(titulo, BorderLayout.NORTH);
        header.add(statsPanel, BorderLayout.CENTER);
        
        return header;
    }
    
    // ============================================================================
    // PANEL DE FRAGMENTOS ÉPICO
    // ============================================================================
    private JPanel crearPanelFragmentosEpico() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(NEGRO_CYBER);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        
        List<FragmentoCodigo> fragmentos = fragmentosPorNivel.get(nivelActual);
        Collections.shuffle(fragmentos);
        
        JPanel gridPanel = new JPanel(new GridLayout(0, 2, 15, 15));
        gridPanel.setOpaque(false);
        
        for (FragmentoCodigo fragmento : fragmentos) {
            JPanel cardFragmento = crearTarjetaFragmentoEpica(fragmento);
            gridPanel.add(cardFragmento);
        }
        
        JScrollPane scroll = new JScrollPane(gridPanel);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        
        panel.add(scroll, BorderLayout.CENTER);
        
        return panel;
    }
    
    // ============================================================================
    // TARJETA DE FRAGMENTO ÉPICA
    // ============================================================================
    private JPanel crearTarjetaFragmentoEpica(FragmentoCodigo fragmento) {
        JPanel card = new JPanel(new BorderLayout(10, 10)) {
            private boolean hover = false;
            
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Fondo con gradiente rojo
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(40, 0, 0, 250),
                    getWidth(), getHeight(), new Color(80, 0, 0, 250)
                );
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                
                // Borde con efecto glow
                Color borderColor = hover ? ROJO_NEON : new Color(150, 0, 0);
                g2d.setColor(borderColor);
                g2d.setStroke(new BasicStroke(hover ? 3f : 2f));
                g2d.drawRoundRect(1, 1, getWidth()-2, getHeight()-2, 15, 15);
                
                // Efecto de pulso en hover
                if (hover) {
                    g2d.setColor(new Color(255, 100, 100, 80));
                    for (int i = 0; i < 3; i++) {
                        g2d.drawRoundRect(2+i, 2+i, getWidth()-4-i*2, getHeight()-4-i*2, 15, 15);
                    }
                }
            }
        };
        
        card.setOpaque(false);
        card.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        card.setPreferredSize(new Dimension(400, 120));
        
        // Código con estilo terminal
        JTextArea areaCodigo = new JTextArea(fragmento.codigo);
        areaCodigo.setFont(new Font("Consolas", Font.BOLD, 14));
        areaCodigo.setForeground(new Color(0, 255, 100));
        areaCodigo.setBackground(new Color(0, 0, 0, 200));
        areaCodigo.setEditable(false);
        areaCodigo.setLineWrap(true);
        areaCodigo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 100, 0), 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        
        // Botón de selección con estilo cyber
        JButton btnSeleccionar = new JButton("ANALIZAR FRAGMENTO");
        btnSeleccionar.setFont(new Font("Consolas", Font.BOLD, 12));
        btnSeleccionar.setBackground(new Color(100, 0, 0));
        btnSeleccionar.setForeground(Color.WHITE);
        btnSeleccionar.setFocusPainted(false);
        btnSeleccionar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ROJO_NEON, 2),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        
        btnSeleccionar.addActionListener(e -> {
            MenuEstilo.reproducirSonido("/sonidos/click.wav");
            verificarFragmento(fragmento, card);
        });
        
        // Eventos hover épicos
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
//                hover = true;
                card.repaint();
                MenuEstilo.reproducirSonido("/sonidos/hover.wav");
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
//                hover = false;
                card.repaint();
            }
        });
        
        card.add(areaCodigo, BorderLayout.CENTER);
        card.add(btnSeleccionar, BorderLayout.SOUTH);
        
        return card;
    }
    
    // ============================================================================
    // VERIFICACIÓN MEJORADA DE FRAGMENTO
    // ============================================================================
    private void verificarFragmento(FragmentoCodigo fragmento, JPanel card) {
        if (fragmento.esVerdadero) {
            // ¡CORRECTO! - Efecto visual épico
            fragmentosRecolectados.add(fragmento.codigo);
            animarAcierto(card);
            mostrarFeedbackEpico(true, fragmento);
            
            // Verificar si completó el nivel
            if (fragmentosRecolectados.size() == contarFragmentosVerdaderos(nivelActual)) {
                completarNivel();
            }
        } else {
            // ¡ERROR! - Efecto visual dramático
            intentosRestantes--;
            tiempoRestante = Math.max(10, tiempoRestante - 10); // Mínimo 10 segundos
            animarError(card);
            mostrarFeedbackEpico(false, fragmento);
            actualizarUI();
            
            if (intentosRestantes <= 0) {
                gameOver();
            }
        }
    }
    
    // ============================================================================
    // ANIMACIONES lindas
    // ============================================================================
    private void animarAcierto(JPanel card) {
        Timer animacion = new Timer(50, new ActionListener() {
            int pulsos = 0;
            @Override
            public void actionPerformed(ActionEvent e) {
                if (pulsos < 6) {
                    card.setBorder(BorderFactory.createLineBorder(
                        pulsos % 2 == 0 ? Color.GREEN : new Color(0, 200, 0), 4
                    ));
                    pulsos++;
                } else {
                    ((Timer)e.getSource()).stop();
                    card.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
                }
            }
        });
        animacion.start();
        MenuEstilo.reproducirSonido("/sonidos/acierto.wav");
    }
    
    private void animarError(JPanel card) {
        Timer animacion = new Timer(50, new ActionListener() {
            int pulsos = 0;
            @Override
            public void actionPerformed(ActionEvent e) {
                if (pulsos < 6) {
                    card.setBorder(BorderFactory.createLineBorder(
                        pulsos % 2 == 0 ? ROJO_NEON : new Color(255, 100, 100), 4
                    ));
                    pulsos++;
                } else {
                    ((Timer)e.getSource()).stop();
                    card.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
                }
            }
        });
        animacion.start();
        MenuEstilo.reproducirSonido("/sonidos/error.wav");
    }
    
    // ============================================================================
    // FEEDBACK ÉPICO CON JOPTIONPANE PERSONALIZADO
    // ============================================================================
    private void mostrarFeedbackEpico(boolean esCorrecto, FragmentoCodigo fragmento) {
        // Crear panel personalizado
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(NEGRO_CYBER);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Icono y título
        String titulo = esCorrecto ? "¡FRAGMENTO LEGÍTIMO!" : "¡FRAGMENTO CORRUPTO!";
        Color colorTitulo = esCorrecto ? Color.GREEN : ROJO_NEON;
        
        JLabel lblTitulo = new JLabel(" " + titulo, SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Consolas", Font.BOLD, 20));
        lblTitulo.setForeground(colorTitulo);
        
        // Información del fragmento
        JTextArea info = new JTextArea(
            "Código: " + fragmento.codigo + "\n\n" +
            "Análisis: " + fragmento.pista
        );
        info.setFont(new Font("Consolas", Font.PLAIN, 14));
        info.setForeground(Color.WHITE);
        info.setBackground(new Color(30, 0, 0));
        info.setEditable(false);
        info.setLineWrap(true);
        info.setWrapStyleWord(true);
        info.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Botón
        JButton btnContinuar = new JButton(esCorrecto ? "CONTINUAR INFILTRACIÓN" : "⚠️ INTENTAR DE NUEVO");
        btnContinuar.setFont(new Font("Consolas", Font.BOLD, 14));
        btnContinuar.setBackground(esCorrecto ? new Color(0, 100, 0) : ROJO_PRINCIPAL);
        btnContinuar.setForeground(Color.WHITE);
        btnContinuar.setFocusPainted(false);
        btnContinuar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(esCorrecto ? Color.GREEN : ROJO_NEON, 3),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        
        panel.add(lblTitulo, BorderLayout.NORTH);
        panel.add(new JScrollPane(info), BorderLayout.CENTER);
        panel.add(btnContinuar, BorderLayout.SOUTH);
        
        // Crear diálogo personalizado
        JDialog dialogo = new JDialog(this, "ANÁLISIS DEL SISTEMA", true);
        dialogo.setSize(500, 350);
        dialogo.setLocationRelativeTo(this);
        dialogo.setContentPane(panel);
        
        btnContinuar.addActionListener(e -> dialogo.dispose());
        
        dialogo.setVisible(true);
    }
    
    // ============================================================================
    // COMPLETAR NIVEL - CORREGIDO
    // ============================================================================
    private void completarNivel() {
        if (timerNivel != null) {
            timerNivel.stop();
        }
        
        if (nivelActual < NIVELES_TOTALES) {
            // Mostrar mensaje de nivel completado
            mostrarMensajeNivelCompletado();
            
            // Preparar siguiente nivel
            nivelActual++;
            intentosRestantes = 3;
            tiempoRestante = 60;
            fragmentosRecolectados.clear();
            
            // Reiniciar UI para nuevo nivel
            reiniciarMinijuego();
        } else {
            // ¡JUEGO COMPLETADO!
            mostrarVictoriaEpica();
        }
    }
    
    // ======================MENSAJE DE NIVEL CIMPLEATO======================================================
    private void mostrarMensajeNivelCompletado() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBackground(NEGRO_CYBER);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        JLabel lblTitulo = new JLabel("🎊 NIVEL " + (nivelActual) + " COMPLETADO 🎊", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Consolas", Font.BOLD, 28));
        lblTitulo.setForeground(ROJO_NEON);
        
        JLabel lblMensaje = new JLabel(
            "<html><div style='text-align: center;'>" +
            "¡INFILTRACIÓN EXITOSA!<br><br>" +
            "Has recolectado todos los fragmentos legítimos del nivel " + nivelActual + ".<br>" +
            "Preparando acceso al nivel " + (nivelActual + 1) + "...<br><br>" +
            "Fragmentos recuperados: " + fragmentosRecolectados.size() + "<br>" +
            "Tiempo restante: " + tiempoRestante + "s<br>" +
            "Intentos restantes: " + intentosRestantes +
            "</div></html>", SwingConstants.CENTER
        );
        lblMensaje.setFont(new Font("Consolas", Font.PLAIN, 16));
        lblMensaje.setForeground(Color.WHITE);
        
        JButton btnSiguiente = new JButton("ACCEDER AL NIVEL " + (nivelActual + 1));
        btnSiguiente.setFont(new Font("Consolas", Font.BOLD, 16));
        btnSiguiente.setBackground(ROJO_NEON);
        btnSiguiente.setForeground(Color.WHITE);
        btnSiguiente.setFocusPainted(false);
        btnSiguiente.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.WHITE, 3),
            BorderFactory.createEmptyBorder(15, 25, 15, 25)
        ));
        
        panel.add(lblTitulo, BorderLayout.NORTH);
        panel.add(lblMensaje, BorderLayout.CENTER);
        panel.add(btnSiguiente, BorderLayout.SOUTH);
        
        JDialog dialogo = new JDialog(this, "PROGRESO DE INFILTRACIÓN", true);
        dialogo.setSize(600, 400);
        dialogo.setLocationRelativeTo(this);
        dialogo.setContentPane(panel);
        
        btnSiguiente.addActionListener(e -> dialogo.dispose());
        
        dialogo.setVisible(true);
    }
    
    // ============================================================================
    // REINICIAR MINIJUEGO PARA NUEVO NIVEL
    // ============================================================================
    private void reiniciarMinijuego() {
        Component panelActual = contenedorPrincipal.getComponent(escenaActual);
        
        if (panelActual instanceof JPanel) {
            JPanel panel = (JPanel) panelActual;
            panel.removeAll();
            panel.setLayout(new BorderLayout());
            panel.add(crearUIMinijuegoEpico(), BorderLayout.CENTER);
            panel.revalidate();
            panel.repaint();
            
            iniciarTimer();
        }
    }
    
    // ============================================================================
    // GAME OVER ÉPICO
    // ============================================================================
    private void gameOver() {
        if (timerNivel != null) {
            timerNivel.stop();
        }
        
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBackground(NEGRO_CYBER);
        panel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        
        JLabel lblTitulo = new JLabel("GAME OVER", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Consolas", Font.BOLD, 32));
        lblTitulo.setForeground(ROJO_NEON);
        
        JLabel lblMensaje = new JLabel(
            "<html><div style='text-align: center;'>" +
            "¡LA INFILTRACIÓN HA FALLADO!<br><br>" +
            "Mario Von Richter detectó tu presencia.<br>" +
            "El sistema se ha bloqueado y la vacuna no pudo ser completada.<br><br>" +
            "Nivel alcanzado: " + nivelActual + "<br>" +
            "Fragmentos recuperados: " + fragmentosRecolectados.size() +
            "</div></html>", SwingConstants.CENTER
        );
        lblMensaje.setFont(new Font("Consolas", Font.PLAIN, 16));
        lblMensaje.setForeground(Color.WHITE);
        
        JButton btnReintentar = new JButton("REINTENTAR NIVEL " + nivelActual);
        btnReintentar.setFont(new Font("Consolas", Font.BOLD, 16));
        btnReintentar.setBackground(ROJO_PRINCIPAL);
        btnReintentar.setForeground(Color.WHITE);
        btnReintentar.setFocusPainted(false);
        btnReintentar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ROJO_NEON, 3),
            BorderFactory.createEmptyBorder(15, 25, 15, 25)
        ));
        
        panel.add(lblTitulo, BorderLayout.NORTH);
        panel.add(lblMensaje, BorderLayout.CENTER);
        panel.add(btnReintentar, BorderLayout.SOUTH);
        
        JDialog dialogo = new JDialog(this, "FALLA CRÍTICA", true);
        dialogo.setSize(500, 350);
        dialogo.setLocationRelativeTo(this);
        dialogo.setContentPane(panel);
        
        btnReintentar.addActionListener(e -> {
            dialogo.dispose();
            reiniciarNivel();
        });
        
        dialogo.setVisible(true);
    }
    
    // ============================================================================
    // REINICIAR NIVEL ACTUAL
    // ============================================================================
    private void reiniciarNivel() {
        intentosRestantes = 3;
        tiempoRestante = 60;
        fragmentosRecolectados.clear();
        
        Component panelActual = contenedorPrincipal.getComponent(escenaActual);
        
        if (panelActual instanceof JPanel) {
            JPanel panel = (JPanel) panelActual;
            panel.removeAll();
            panel.setLayout(new BorderLayout());
            panel.add(crearUIMinijuegoEpico(), BorderLayout.CENTER);
            panel.revalidate();
            panel.repaint();
            
            iniciarTimer();
        }
    }
    
    // ============================================================================
    // VICTORIA ÉPICA
    // ============================================================================
  // ============================================================================
// VICTORIA ÉPICA - VERSIÓN CORREGIDA
// ============================================================================
private void mostrarVictoriaEpica() {
    JPanel panel = new JPanel(new BorderLayout(20, 20));
    panel.setBackground(NEGRO_CYBER);
    panel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
    
    JLabel lblTitulo = new JLabel("¡VACUNA COMPLETADA!", SwingConstants.CENTER);
    lblTitulo.setFont(new Font("Consolas", Font.BOLD, 32));
    lblTitulo.setForeground(ROJO_NEON);
    
    JLabel lblMensaje = new JLabel(
        "<html><div style='text-align: center;'>" +
        "¡INFILTRACIÓN EXITOSA!<br><br>" +
        "Has logrado infiltrarte en el sistema de Mario Von Richter<br>" +
        "y construir la vacuna digital completa.<br><br>" +
        "El código de la vacuna está ahora en tus manos.<br>" +
        "La humanidad tiene una oportunidad...<br><br>" +
        "<b>" + nombreJugador + "</b>, has salvado el mundo.<br>" +
        "Por ahora..." +
        "</div></html>", SwingConstants.CENTER
    );
    lblMensaje.setFont(new Font("Consolas", Font.PLAIN, 16));
    lblMensaje.setForeground(Color.WHITE);
    
    JButton btnContinuar = new JButton("CONTINUAR LA HISTORIA");
    btnContinuar.setFont(new Font("Consolas", Font.BOLD, 16));
    btnContinuar.setBackground(ROJO_NEON);
    btnContinuar.setForeground(Color.WHITE);
    btnContinuar.setFocusPainted(false);
    btnContinuar.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(Color.WHITE, 3),
        BorderFactory.createEmptyBorder(15, 25, 15, 25)
    ));
    
    panel.add(lblTitulo, BorderLayout.NORTH);
    panel.add(lblMensaje, BorderLayout.CENTER);
    panel.add(btnContinuar, BorderLayout.SOUTH);
    
    JDialog dialogo = new JDialog(this, "MISIÓN CUMPLIDA", true);
    dialogo.setSize(600, 400);
    dialogo.setLocationRelativeTo(this);
    dialogo.setContentPane(panel);
    
    // ✅ AQUÍ ESTÁ LA CORRECCIÓN - Llamar a finalizarCapitulo después de cerrar el diálogo
    btnContinuar.addActionListener(e -> {
        dialogo.dispose();
        
        // ⭐ ESTA ES LA LÍNEA CRUCIAL QUE FALTABA
        storyState.marcarCapitulo(2, true);
        finalizarCapitulo(2, true, () -> new minijuego3(storyState));
    });
    
    dialogo.setVisible(true);
}
    
    
    
    // ============================================================================
    // PANEL INFERIOR ÉPICO
    // ============================================================================
    private JPanel crearPanelInferiorEpico() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(20, 0, 0));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(2, 0, 0, 0, ROJO_NEON),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        
        // Barra de progreso del nivel
        barraProgreso = new JProgressBar(0, contarFragmentosVerdaderos(nivelActual));
        barraProgreso.setValue(fragmentosRecolectados.size());
        barraProgreso.setString("PROGRESO: " + fragmentosRecolectados.size() + "/" + 
                              contarFragmentosVerdaderos(nivelActual));
        barraProgreso.setStringPainted(true);
        barraProgreso.setFont(new Font("Consolas", Font.BOLD, 12));
        barraProgreso.setBackground(ROJO_OSCURO);
        barraProgreso.setForeground(ROJO_NEON);
        barraProgreso.setBorder(BorderFactory.createLineBorder(ROJO_PRINCIPAL, 2));
        
        panel.add(barraProgreso, BorderLayout.CENTER);
        
        return panel;
    }
    
    // ============================================================================
    // TIMER MEJORADO
    // ============================================================================
    private void iniciarTimer() {
        if (timerNivel != null) {
            timerNivel.stop();
        }
        
        tiempoRestante = 60;
        actualizarUI();
        
        timerNivel = new Timer(1000, e -> {
            tiempoRestante--;
            actualizarUI();
            
            if (tiempoRestante <= 0) {
                timerNivel.stop();
                gameOver();
            }
        });
        
        timerNivel.start();
    }
    
    // ============================================================================
    // ACTUALIZACIÓN DE UI
    // ============================================================================
    private void actualizarUI() {
        if (lblTiempo != null) {
            lblTiempo.setText(" " + tiempoRestante + "s");
            
            // Cambiar color según tiempo restante
            if (tiempoRestante <= 15) {
                lblTiempo.setForeground(ROJO_NEON);
                // Efecto de parpadeo en tiempo crítico
                lblTiempo.setVisible(tiempoRestante % 2 == 0);
            } else if (tiempoRestante <= 30) {
                lblTiempo.setForeground(Color.YELLOW);
            } else {
                lblTiempo.setForeground(Color.WHITE);
            }
        }
        
        if (lblIntentos != null) {
            lblIntentos.setText("️ " + intentosRestantes + "/3");
            
            // Cambiar color según intentos
            if (intentosRestantes == 1) {
                lblIntentos.setForeground(ROJO_NEON);
            } else if (intentosRestantes == 2) {
                lblIntentos.setForeground(Color.YELLOW);
            } else {
                lblIntentos.setForeground(Color.WHITE);
            }
        }
        
        if (barraProgreso != null) {
            barraProgreso.setValue(fragmentosRecolectados.size());
            barraProgreso.setString("PROGRESO: " + fragmentosRecolectados.size() + "/" + 
                                  contarFragmentosVerdaderos(nivelActual));
        }
    }
    
    // ============================================================================
    // MÉTODOS AUXILIARES
    // ============================================================================
    private int contarFragmentosVerdaderos(int nivel) {
        return (int) fragmentosPorNivel.get(nivel).stream()
                .filter(f -> f.esVerdadero)
                .count();
    }
    
    // ============================================================================
    // MÉTODO MAIN PARA PRUEBAS
    // ============================================================================
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            StoryState state = new StoryState();
            state.setNombre("TESTER");
            state.setGenero("hombre");
            new minijuego2(state);
        });
    }
}