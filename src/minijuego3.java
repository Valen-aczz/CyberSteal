import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

/**
 * ============================================================================
 * CAPÍTULO 3: PHISHING FOR GOLD
 * Sistema de detección de correos fraudulentos
 * ============================================================================
 */
public class minijuego3 extends BaseCapituloFrame {
    
    // ============================================================================
    // VARIABLES PRINCIPALES
    // ============================================================================
    private JPanel contenedorPrincipal;
    private CardLayout cardLayout;
    private List<Escena> escenas;
    private int escenaActual = 0;
    private String nombreJugador;
    private String carpetaGenero;
    
    // Variables del minijuego de phishing
    private int emailsAnalizados = 0;
    private int aciertos = 0;
    private int errores = 0;
    private static final int EMAILS_TOTALES = 8;
    private static final int MIN_ACIERTOS = 6; // 75% de precisión
    
    private List<Email> listaEmails;
    private int emailActual = 0;
    
    // ============================================================================
    // CLASE EMAIL - Representa cada correo a analizar
    // ============================================================================
    private static class Email {
        String remitente;
        String asunto;
        String cuerpo;
        String enlace;
        boolean esPhishing;
        String razonamiento; // Explicación de por qué es/no es phishing
        
        Email(String remitente, String asunto, String cuerpo, String enlace, 
              boolean esPhishing, String razonamiento) {
            this.remitente = remitente;
            this.asunto = asunto;
            this.cuerpo = cuerpo;
            this.enlace = enlace;
            this.esPhishing = esPhishing;
            this.razonamiento = razonamiento;
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
    public minijuego3(StoryState state) {
        super(state, "Capítulo 3: Phishing for Gold", "");
        MenuEstilo.detenerMusicaFondo();      //llamo al método que está en la clase principal MenuEstilo
        this.nombreJugador = state.getNombre();
        this.carpetaGenero = state.getGenero().equals("mujer") ? "mujer" : "hombre";
        this.escenas = new ArrayList<>();
        
        
        
        
        
        //Función de evaluación
        inicializarEmails();
        
        // Panel de carga
        centerPanel.setLayout(new BorderLayout());
        centerPanel.setBackground(Color.BLACK);
        
        JPanel panelCarga = crearPantallaCarga();
        centerPanel.add(panelCarga, BorderLayout.CENTER);
        
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);
        
        iniciarCargaAsincrona();
    }
    
    // ============================================================================
    // INICIALIZACIÓN DE EMAILS (4 legítimos + 4 phishing)
    // ============================================================================
    private void inicializarEmails() {
        listaEmails = new ArrayList<>();
        
        // ========== EMAILS LEGÍTIMOS ==========
        listaEmails.add(new Email(
            "seguridad@bancoglobal.com.co",
            "Actualización de políticas de seguridad",
            "Estimado cliente,\n\n" +
            "Le informamos que hemos actualizado nuestras políticas de seguridad.\n" +
            "Puede revisarlas en su próxima visita a cualquier sucursal.\n\n" +
            "Atentamente,\n" +
            "Departamento de Seguridad",
            "www.bancoglobal.com.co/politicas",
            false,
            "✅ LEGÍTIMO: Dominio oficial, no pide datos sensibles, ortografía correcta"
        ));
        
        listaEmails.add(new Email(
            "notificaciones@bancoglobal.com.co",
            "Resumen mensual de su cuenta",
            "Apreciado usuario,\n\n" +
            "Adjuntamos el resumen de movimientos de su cuenta del mes anterior.\n" +
            "Para más detalles, ingrese a su banca en línea con sus credenciales habituales.\n\n" +
            "Saludos cordiales",
            "www.bancoglobal.com.co/resumen",
            false,
            "✅ LEGÍTIMO: Dominio verificado, lenguaje profesional, no solicita contraseñas"
        ));
        
        listaEmails.add(new Email(
            "soporte@bancoglobal.com.co",
            "Confirmación de cita programada",
            "Hola " + nombreJugador + ",\n\n" +
            "Confirmamos su cita para el día 15 de noviembre a las 10:00 AM en nuestra sucursal principal.\n" +
            "Si necesita reprogramar, llame al 555-1234.\n\n" +
            "Equipo de Atención al Cliente",
            "www.bancoglobal.com.co/citas",
            false,
            "✅ LEGÍTIMO: Email esperado, información específica, canal de contacto oficial"
        ));
        
        listaEmails.add(new Email(
            "marketing@bancoglobal.com.co",
            "Nueva tarjeta de crédito disponible",
            "Estimado cliente,\n\n" +
            "Nos complace informarle sobre nuestra nueva tarjeta Platinum.\n" +
            "Visite cualquier sucursal para conocer los beneficios.\n\n" +
            "Departamento Comercial",
            "www.bancoglobal.com.co/productos/tarjetas",
            false,
            "✅ LEGÍTIMO: Promoción normal, sin urgencia, redirige a sitio oficial"
        ));
        
        // ========== EMAILS PHISHING ==========
        listaEmails.add(new Email(
            "seguridad@bancoglobal-verificacion.com",
            "URGENTE: Verifique su cuenta en 24 horas",
            "ALERTA DE SEGURIDAD\n\n" +
            "Detectamos actividad sospechosa en su cuenta.\n" +
            "DEBE verificar su identidad INMEDIATAMENTE haciendo clic aquí:\n" +
            "[VERIFICAR AHORA]\n\n" +
            "Si no lo hace en 24 horas, su cuenta será BLOQUEADA.\n\n" +
            "Departamento de Seguirdad", // Error ortográfico intencional
            "http://bancoglobal-verificacion.com/login",
            true,
            "🚨 PHISHING: Dominio falso (.com vs .com.co), urgencia extrema, error ortográfico, enlace sospechoso"
        ));
        
        listaEmails.add(new Email(
            "no-reply@banco-global.net",
            "Ha ganado un premio especial",
            "¡¡FELICIDADES!!\n\n" +
            "Usted ha sido seleccionado para recibir un BONO de $5,000,000.\n" +
            "Para reclamar su premio, ingrese sus datos bancarios aquí:\n" +
            "[RECLAMAR PREMIO]\n\n" +
            "Esta oferta expira en 12 horas.",
            "http://banco-global.net/premio?id=123456",
            true,
            "🚨 PHISHING: Promesa de dinero gratis, dominio incorrecto (.net), urgencia, solicita datos bancarios"
        ));
        
        listaEmails.add(new Email(
            "admin@bancoglobal.com",
            "Actualice sus datos de seguridad",
            "Estimado usuario,\n\n" +
            "Por motivos de seguirdad, debe actualizar su clave.\n" +
            "Ingrese aqui para cambiarla:\n" +
            "http://actualizar-datos-banco.com/login.php\n\n" +
            "Este enlace expira en 6 horas.\n\n" +
            "Atte. Administracion",
            "http://actualizar-datos-banco.com/login.php",
            true,
            "🚨 PHISHING: Errores ortográficos múltiples, dominio completamente diferente, urgencia, enlace sospechoso (.php)"
        ));
        
        listaEmails.add(new Email(
            "soporte-tecnico@bancoglobalseguro.com",
            "Problema detectado en su tarjeta",
            "NOTIFICACION IMPORTANTE\n\n" +
            "Su tarjeta ha sido bloqueada por seguridad.\n" +
            "Para desbloquearla, confirme sus datos aquí:\n\n" +
            "Número de tarjeta: _______\n" +
            "CVV: ___\n" +
            "Fecha vencimiento: __/__\n\n" +
            "Responda este correo con la información.",
            "No aplicable (solicita respuesta por email)",
            true,
            "🚨 PHISHING: Solicita CVV por email (NUNCA se debe hacer), dominio sospechoso, pide responder con datos sensibles"
        ));
        
        // Mezclar emails aleatoriamente
        Collections.shuffle(listaEmails);
    }
    
    // ============================================================================
    // PANTALLA DE CARGA
    // ============================================================================
   private JPanel crearPantallaCarga() {
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.setBackground(Color.BLACK);
    panel.setBorder(BorderFactory.createEmptyBorder(80, 0, 80, 0));
    
    // Imagen de carga con logo
    JLabel lblImagenCarga = new JLabel("", SwingConstants.CENTER);
    try {
        String imagenPath = "/images/" + carpetaGenero + "/logo3.png"; 
        java.net.URL url = getClass().getResource(imagenPath);
        
        if (url != null) {
            ImageIcon iconoOriginal = new ImageIcon(url);
            Image imagenRedimensionada = iconoOriginal.getImage()
                .getScaledInstance(180, 180, Image.SCALE_SMOOTH);
            lblImagenCarga.setIcon(new ImageIcon(imagenRedimensionada));
        } else {
            throw new Exception("Imagen no encontrada");
        }
    } catch (Exception e) {
        // Fallback si no hay imagen
        lblImagenCarga.setText("🎣");
        lblImagenCarga.setFont(new Font("Arial", Font.BOLD, 100));
        lblImagenCarga.setForeground(Color.YELLOW);
    }
    lblImagenCarga.setAlignmentX(Component.CENTER_ALIGNMENT);
    
    JLabel lblTexto = new JLabel("Cargando sistema anti-phishing...", SwingConstants.CENTER);
    lblTexto.setFont(new Font("Arial", Font.BOLD, 22));
    lblTexto.setForeground(Color.YELLOW);
    lblTexto.setAlignmentX(Component.CENTER_ALIGNMENT);
    
    JProgressBar barraProgreso = new JProgressBar();
    barraProgreso.setIndeterminate(true);
    barraProgreso.setPreferredSize(new Dimension(400, 25));
    barraProgreso.setMaximumSize(new Dimension(400, 25));
    barraProgreso.setBackground(new Color(30, 30, 60));
    barraProgreso.setForeground(Color.YELLOW);
    barraProgreso.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 2));  // BORDE AMARILLO también
    barraProgreso.setAlignmentX(Component.CENTER_ALIGNMENT);
    
    panel.add(Box.createRigidArea(new Dimension(0, 30)));
    panel.add(lblImagenCarga);
    panel.add(Box.createRigidArea(new Dimension(0, 40)));
    panel.add(lblTexto);
    panel.add(Box.createRigidArea(new Dimension(0, 30)));
    panel.add(barraProgreso);
    
    return panel;
}
    
    // ============================================================================
    // CARGA ASÍNCRONA
    // ============================================================================
    private void iniciarCargaAsincrona() {
        SwingWorker<Void, String> cargador = new SwingWorker<Void, String>() {
            @Override
            protected Void doInBackground() throws Exception {
                publish("Inicializando filtros de seguridad...");
                Thread.sleep(1500);
                
                publish("Cargando base de datos de amenazas...");
                Thread.sleep(1500);
                agregarEscenas();
                
                publish("Preparando bandeja de entrada...");
                Thread.sleep(1500);
                
                publish("¡Sistema listo! Iniciando análisis...");
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
    // CONFIGURACIÓN DE ESCENAS
    // ============================================================================
    private void agregarEscenas() {
        // ========== INTRODUCCIÓN ==========
        escenas.add(new Escena(
            img("contexto"),
            nombreJugador + ": Después de todo lo que pasé... finalmente me nombraron Director de Seguridad del Banco Global. " +
            "Pensé que tendría paz, pero parece que los problemas apenas comienzan..."
        ));
        
        escenas.add(new Escena(
            img("working"),
            nombreJugador +  ": Hmmm este correo parece legítimo, pero el remitente tiene una dirección rara.\n" +
"¿‘soporte@banc0-seguro.com’? Con cero en lugar de ‘o’… clásico truco de engaño, ¿Mario Von Richter estara involucrado en esto?\n\n" +
            "PISTA: Si la cadena contiene caracteres extraños… debe marcarse como sospechoso"
        ));
        
        escenas.add(new Escena(
            img("elproblema"),
            nombreJugador + ": ¡Oh, no! Este mensaje estaba disfrazado como alerta de seguridad!\n" +
"Pero el enlace lleva a una dirección externa. Casi caigo.\n\n" +
            "PISTA: Los phishers crean urgencia falsa para presionarte"
        ));
        
        escenas.add(new Escena(
            img("consejosjuego3"),
            nombreJugador + " veamos las reglas: palabras clave, remitentes y formato de los correos…\n Si"
          + " cualquiera coincide con patrones sospechosos, debe clasificarse como dañino.\n\n" +
                    
            "PISTA: Errores ortográficos son señal de alerta"
        ));
        
        escenas.add(new Escena(
            img("empiezajuego3"),
            nombreJugador + ": Listo. He configurado los filtros con vectores que almacenan cada mensaje.”\n" +
"“Las funciones verificarán uno por uno. Si los resultados son correctos…\n" +
"¡el sistema quedará protegido contra futuros ataques!.\n\n" +
            "PISTA: Nunca compartas CVV o contraseñas por email"
        ));
        
        
        // ========== INICIO DEL MINIJUEGO ==========
        escenas.add(new Escena(this::iniciarMinijuegoPhishing));
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
        contenedorPrincipal.setBackground(Color.BLACK);
        
        for (int i = 0; i < escenas.size(); i++) {
            Escena escena = escenas.get(i);
            JPanel panel;
            
            if (escena.esMinijuego) {
                panel = new JPanel(new BorderLayout());
                panel.setBackground(Color.BLACK);
            } else {
                panel = crearPanelDialogo(escena);
            }
            
            contenedorPrincipal.add(panel, "escena_" + i);
        }
        
        centerPanel.setLayout(new BorderLayout());
        centerPanel.add(contenedorPrincipal, BorderLayout.CENTER);
    }
    
    private JPanel crearPanelDialogo(Escena escena) {
        JPanel panelFondo = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                try {
                    java.awt.image.BufferedImage img = javax.imageio.ImageIO.read(
                        getClass().getResource(escena.imagen)
                    );
                    g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
                } catch (Exception e) {
                    g.setColor(Color.DARK_GRAY);
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
                g2d.setColor(new Color(0, 0, 0, 180));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
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
    
    JButton btnContinuar = new JButton("Continuar");
    estilizarBoton(btnContinuar, new Color(0, 0, 0, 200), Color.CYAN, 120, 40);
    btnContinuar.addActionListener(e -> siguienteEscena());
    
    // ⭐ NUEVO: Botón Saltar al Juego (amarillo para minijuego3)
    JButton btnSaltar = new JButton("Saltar al Juego");
    estilizarBoton(btnSaltar, new Color(100, 100, 0, 200), Color.YELLOW, 150, 40);
    btnSaltar.addActionListener(e -> saltarAlJuego());
    
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
    // NAVEGACIÓN
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
    
    // ============================================================================
    // MINIJUEGO: DETECTOR DE PHISHING
    // ============================================================================
    private void iniciarMinijuegoPhishing() {
        Component panelActual = contenedorPrincipal.getComponent(escenaActual);
        
        if (panelActual instanceof JPanel) {
            JPanel panel = (JPanel) panelActual;
            panel.removeAll();
            panel.setLayout(new BorderLayout());
            panel.add(crearUIMinijuegoPhishing(), BorderLayout.CENTER);
            panel.revalidate();
            panel.repaint();
        }
    }
    
    private JPanel crearUIMinijuegoPhishing() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(15, 20, 30));
        
        // ========== ENCABEZAMIENTO CON ESTADÍSTICAS ==========
        JPanel header = crearHeaderEstadisticas();
        
        // ========== PANEL CENTRAL CON EMAIL ==========
        JPanel emailPanel = crearPanelEmail();
        
        // ========== PANEL INFERIOR CON BOTONES DE DECISIÓN ==========
        JPanel decisionPanel = crearPanelDecision();
        
        panel.add(header, BorderLayout.NORTH);
        panel.add(emailPanel, BorderLayout.CENTER);
        panel.add(decisionPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    // ========== ENCABEZAMIENTO CON ESTADÍSTICAS ==========
    private JPanel crearHeaderEstadisticas() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(25, 30, 40));
        header.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JLabel titulo = new JLabel("PHISHING DETECTION SYSTEM", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 28));
        titulo.setForeground(new Color(255, 140, 0));
        
        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 5));
        statsPanel.setOpaque(false);
        
        JLabel lblProgreso = new JLabel("Email " + (emailActual + 1) + "/" + EMAILS_TOTALES);
        lblProgreso.setFont(new Font("Arial", Font.BOLD, 18));
        lblProgreso.setForeground(Color.WHITE);
        
        JLabel lblAciertos = new JLabel("✅ Aciertos: " + aciertos);
        lblAciertos.setFont(new Font("Arial", Font.BOLD, 18));
        lblAciertos.setForeground(Color.GREEN);
        
        JLabel lblErrores = new JLabel("❌ Errores: " + errores);
        lblErrores.setFont(new Font("Arial", Font.BOLD, 18));
        lblErrores.setForeground(Color.RED);
        
        statsPanel.add(lblProgreso);
        statsPanel.add(lblAciertos);
        statsPanel.add(lblErrores);
        
        header.add(titulo, BorderLayout.NORTH);
        header.add(statsPanel, BorderLayout.CENTER);
        
        return header;
    }
    
    // ========== PANEL CON EL EMAIL ACTUAL ==========
    private JPanel crearPanelEmail() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(15, 20, 30));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        
        Email email = listaEmails.get(emailActual);
        
        // Contenedor estilo "ventana de email"
        JPanel emailContainer = new JPanel(new BorderLayout(10, 10));
        emailContainer.setBackground(Color.WHITE);
        emailContainer.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
        
        // ========== ENCABEZADO DEL EMAIL ==========
        JPanel headerEmail = new JPanel();
        headerEmail.setLayout(new BoxLayout(headerEmail, BoxLayout.Y_AXIS));
        headerEmail.setBackground(new Color(240, 240, 245));
        headerEmail.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JLabel lblDe = new JLabel("De: " + email.remitente);
        lblDe.setFont(new Font("Arial", Font.BOLD, 16));
        
        JLabel lblAsunto = new JLabel("Asunto: " + email.asunto);
        lblAsunto.setFont(new Font("Arial", Font.BOLD, 18));
        lblAsunto.setForeground(new Color(50, 50, 50));
        
        headerEmail.add(lblDe);
        headerEmail.add(Box.createRigidArea(new Dimension(0, 8)));
        headerEmail.add(lblAsunto);
        
        // ========== CUERPO DEL EMAIL ==========
        JTextArea areaCuerpo = new JTextArea(email.cuerpo);
        areaCuerpo.setFont(new Font("Arial", Font.PLAIN, 16));
        areaCuerpo.setLineWrap(true);
        areaCuerpo.setWrapStyleWord(true);
        areaCuerpo.setEditable(false);
        areaCuerpo.setBackground(Color.WHITE);
        areaCuerpo.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JScrollPane scrollCuerpo = new JScrollPane(areaCuerpo);
        scrollCuerpo.setBorder(null);
        
        // ========== ENLACE (si existe) ==========
        JPanel panelEnlace = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelEnlace.setBackground(new Color(250, 250, 250));
        panelEnlace.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        
        JLabel lblEnlace = new JLabel("Enlace: " + email.enlace);
        lblEnlace.setFont(new Font("Consolas", Font.PLAIN, 14));
        lblEnlace.setForeground(Color.BLUE);
        panelEnlace.add(lblEnlace);
        
        // ========== ENSAMBLAR ==========
        emailContainer.add(headerEmail, BorderLayout.NORTH);
        emailContainer.add(scrollCuerpo, BorderLayout.CENTER);
        emailContainer.add(panelEnlace, BorderLayout.SOUTH);
        
        panel.add(emailContainer, BorderLayout.CENTER);
        
        return panel;
    }
    
    // ========== PANEL DE DECISIÓN ==========
    private JPanel crearPanelDecision() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 20));
        panel.setBackground(new Color(15, 20, 30));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        
        JButton btnLegitimo = new JButton("✅ ES LEGÍTIMO");
        btnLegitimo.setFont(new Font("Arial", Font.BOLD, 20));
        btnLegitimo.setBackground(new Color(34, 139, 34));
        btnLegitimo.setForeground(Color.WHITE);
        btnLegitimo.setFocusPainted(false);
        btnLegitimo.setPreferredSize(new Dimension(250, 60));
        btnLegitimo.setBorder(BorderFactory.createLineBorder(Color.GREEN, 3));
        
        JButton btnPhishing = new JButton("🚨 ES PHISHING");
        btnPhishing.setFont(new Font("Arial", Font.BOLD, 20));
        btnPhishing.setBackground(new Color(178, 34, 34));
        btnPhishing.setForeground(Color.WHITE);
        btnPhishing.setFocusPainted(false);
        btnPhishing.setPreferredSize(new Dimension(250, 60));
        btnPhishing.setBorder(BorderFactory.createLineBorder(Color.RED, 3));
        
        // Acciones de los botones
        btnLegitimo.addActionListener(e -> evaluarRespuesta(false));
        btnPhishing.addActionListener(e -> evaluarRespuesta(true));
        
        // Botón de salir
        JButton btnSalir = new JButton("🚪 Salir");
        btnSalir.setFont(new Font("Arial", Font.BOLD, 16));
        btnSalir.setBackground(new Color(80, 80, 80));
        btnSalir.setForeground(Color.WHITE);
        btnSalir.setFocusPainted(false);
        btnSalir.setPreferredSize(new Dimension(150, 50));
        btnSalir.addActionListener(e -> confirmarSalida());
        
        panel.add(btnLegitimo);
        panel.add(btnPhishing);
        panel.add(btnSalir);
        
        return panel;
    }
    
    // ============================================================================
    // EVALUACIÓN DE RESPUESTA
    // ============================================================================
    private void evaluarRespuesta(boolean jugadorDicePhishing) {
        MenuEstilo.reproducirSonido("/sonidos/click.wav");
        
        Email email = listaEmails.get(emailActual);
        boolean esCorrecta = (jugadorDicePhishing == email.esPhishing);
        
        if (esCorrecta) {
            aciertos++;
            mostrarFeedback(true, email);
        } else {
            errores++;
            mostrarFeedback(false, email);
        }
        
        emailsAnalizados++;
        
        // Verificar si terminó el juego
        if (emailsAnalizados >= EMAILS_TOTALES) {
            finalizarJuego();
        } else {
            emailActual++;
            // Actualizar la UI para el siguiente email
            actualizarPanelEmail();
        }
    }
    
    // ============================================================================
    // MOSTRAR FEEDBACK DESPUÉS DE CADA DECISIÓN
    // ============================================================================
    private void mostrarFeedback(boolean correcto, Email email) {
        JDialog dialogo = new JDialog(this, correcto ? "✅ ¡CORRECTO!" : "❌ ERROR", true);
        dialogo.setSize(700, 400);
        dialogo.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(correcto ? new Color(20, 80, 20) : new Color(80, 20, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Título del feedback
        JLabel lblTitulo = new JLabel(
            correcto ? "¡Identificación correcta!" : "Identificación incorrecta",
            SwingConstants.CENTER
        );
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setForeground(Color.WHITE);
        
        // Explicación
        JTextArea areaExplicacion = new JTextArea(
            "Este email " + (email.esPhishing ? "SÍ ES PHISHING" : "ES LEGÍTIMO") + "\n\n" +
            email.razonamiento
        );
        areaExplicacion.setFont(new Font("Arial", Font.PLAIN, 16));
        areaExplicacion.setForeground(Color.WHITE);
        areaExplicacion.setBackground(correcto ? new Color(20, 80, 20) : new Color(80, 20, 20));
        areaExplicacion.setLineWrap(true);
        areaExplicacion.setWrapStyleWord(true);
        areaExplicacion.setEditable(false);
        areaExplicacion.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        areaExplicacion.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.WHITE, 2),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        JScrollPane scroll = new JScrollPane(areaExplicacion);
        scroll.setBorder(null);
        
        // Botón continuar
        JButton btnContinuar = new JButton("Continuar al siguiente email");
        btnContinuar.setFont(new Font("Arial", Font.BOLD, 18));
        btnContinuar.setBackground(Color.WHITE);
        btnContinuar.setForeground(correcto ? new Color(0, 100, 0) : new Color(150, 0, 0));
        btnContinuar.setFocusPainted(false);
        btnContinuar.setPreferredSize(new Dimension(0, 50));
        btnContinuar.addActionListener(e -> dialogo.dispose());
        
        panel.add(lblTitulo, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        panel.add(btnContinuar, BorderLayout.SOUTH);
        
        dialogo.add(panel);
        dialogo.setVisible(true);
    }
    
    // ============================================================================
    // ACTUALIZAR PANEL PARA EL SIGUIENTE EMAIL
    // ============================================================================
    private void actualizarPanelEmail() {
        // Obtener el panel actual del minijuego
        Component panelActual = contenedorPrincipal.getComponent(escenaActual);
        
        if (panelActual instanceof JPanel) {
            JPanel panel = (JPanel) panelActual;
            panel.removeAll();
            panel.setLayout(new BorderLayout());
            panel.add(crearUIMinijuegoPhishing(), BorderLayout.CENTER);
            panel.revalidate();
            panel.repaint();
        }
    }
    
    // ============================================================================
    // FINALIZAR JUEGO
    // ============================================================================
    private void finalizarJuego() {
        boolean victoria = (aciertos >= MIN_ACIERTOS);
        
        // Calcular porcentaje de precisión
        double precision = (aciertos * 100.0) / EMAILS_TOTALES;
        
        String mensaje;
        String titulo;
        
        if (victoria) {
            mensaje = String.format(
                "🎉 ¡EXCELENTE TRABAJO!\n\n" +
                "%s: -Increíble. Detecté %d de %d emails correctamente.\n" +
                "Precisión: %.1f%%\n\n" +
                "Director General: -Impresionante, %s. Has salvado al banco de una crisis masiva.\n" +
                "Los empleados están a salvo gracias a tu experiencia.\n\n" +
                "🏆 RECOMPENSA: Ascenso a Director de Ciberseguridad Global\n" +
                "💰 BONO: $50,000,000",
                nombreJugador, aciertos, EMAILS_TOTALES, precision, nombreJugador
            );
            titulo = "¡MISIÓN COMPLETADA!";
            
            // Mostrar epílogo
            MenuEstilo.reproducirSonido("/sonidos/click.wav");
            JOptionPane.showMessageDialog(this, mensaje, titulo, JOptionPane.INFORMATION_MESSAGE);
            
            storyState.marcarCapitulo(3, true);
            mostrarEpilogo();
            
        } else {
            mensaje = String.format(
                "😰 CRISIS DE SEGURIDAD\n\n" +
                "%s: -No... fallé demasiadas veces.\n" +
                "Precisión: %.1f%% (%d/%d correctos)\n\n" +
                "Director General: -Los hackers lograron comprometer varias cuentas.\n" +
                "Tendremos que cerrar operaciones temporalmente.\n\n" +
                "❌ RESULTADO: Pérdida de confianza de clientes\n" +
                "💔 El banco sufre daños reputacionales graves",
                nombreJugador, precision, aciertos, EMAILS_TOTALES
            );
            titulo = "MISIÓN FALLIDA";
            
            JOptionPane.showMessageDialog(this, mensaje, titulo, JOptionPane.ERROR_MESSAGE);
            
            storyState.marcarCapitulo(3, false);
            finalizarCapitulo(3, false, () -> {
                dispose();
                new MenuEstilo(storyState);
            });
        }
    }
    
    // ============================================================================
    // EPÍLOGO - FINAL DE LA HISTORIA COMPLETA
    // ============================================================================
    private void mostrarEpilogo() {
        JDialog dialogoEpilogo = new JDialog(this, "EPÍLOGO: EL LEGADO", true);
        dialogoEpilogo.setSize(900, 650);
        dialogoEpilogo.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(10, 10, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        // Título épico
        JLabel titulo = new JLabel("🌟 CYBERSTEAL: THE FIREWALL SAGA 🌟", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 32));
        titulo.setForeground(new Color(255, 215, 0));
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        // Texto del epílogo
        JTextArea areaEpilogo = new JTextArea();
        areaEpilogo.setFont(new Font("Arial", Font.PLAIN, 16));
        areaEpilogo.setForeground(Color.WHITE);
        areaEpilogo.setBackground(new Color(10, 10, 20));
        areaEpilogo.setLineWrap(true);
        areaEpilogo.setWrapStyleWord(true);
        areaEpilogo.setEditable(false);
        
        String textoEpilogo = String.format(
            "═══════════════════════════════════════════════════\n\n" +
            "Seis meses después...\n\n" +
            "El Banco Global se ha convertido en la institución financiera más segura de la región.\n\n" +
            "%s, ahora Director de Ciberseguridad Global, implementó protocolos revolucionarios:\n\n" +
            "• Sistema Lock & Code: Generación automática de contraseñas seguras\n" +
            "• Protocolo Zero-Trust: Arquitectura de seguridad multinivel\n" +
            "• Filtro Phishing Gold: IA que detecta fraudes con 99%% de precisión\n\n" +
            "Mario Von Richter fue capturado y ahora trabaja como consultor ético de seguridad " +
            "(bajo estricta supervisión, por supuesto).\n\n" +
            "Los ataques cibernéticos al banco se redujeron en un 95%%.\n\n" +
            "Pero %s sabe que la batalla nunca termina realmente...\n\n" +
            "En el mundo digital, siempre hay una nueva amenaza al acecho.\n\n" +
            "═══════════════════════════════════════════════════\n\n" +
            "\"En ciberseguridad, no se trata de si te atacarán,\n" +
            "sino de cuándo. Y cuando lo hagan, estaremos listos.\"\n\n" +
            "- %s, Director de Ciberseguridad Global\n\n" +
            "═══════════════════════════════════════════════════\n\n" +
            "🏆 LOGROS DESBLOQUEADOS:\n\n" +
            "✅ Capítulo 1: Lock & Code - %s\n" +
            "✅ Capítulo 2: The Zero-Trust - %s\n" +
            "✅ Capítulo 3: Phishing for Gold - %s\n\n" +
            "🎖️ RANGO FINAL: Maestro de la Ciberseguridad\n" +
            "⭐ REPUTACIÓN: Legendaria\n" +
            "💰 GANANCIAS TOTALES: $150,000,000\n\n" +
            "═══════════════════════════════════════════════════\n\n" +
            "¡GRACIAS POR JUGAR!\n\n" +
            "Desarrollado por:\n" +
            "• Majo - Gestora de proyecto\n" +
            "• Karla - Diseñadora\n" +
            "• Valen - Programadora\n" +
            "• Mau - Documentador técnico\n\n" +
            "Algoritmia y Programación II\n" +
            "Universidad del Norte - 2025\n\n" +
            "═══════════════════════════════════════════════════",
            nombreJugador,
            nombreJugador,
            nombreJugador,
            storyState.isCap1Ganado() ? "COMPLETADO" : "Completado (con errores)",
            storyState.isCap2Ganado() ? "COMPLETADO" : "Completado (con errores)",
            storyState.isCap3Ganado() ? "COMPLETADO" : "Completado (con errores)"
        );
        
        areaEpilogo.setText(textoEpilogo);
        areaEpilogo.setCaretPosition(0); // Scroll al inicio
        
        JScrollPane scroll = new JScrollPane(areaEpilogo);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(255, 215, 0), 2));
        
        // Botones finales
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelBotones.setOpaque(false);
        
        JButton btnMenuPrincipal = new JButton("🏠 Volver al Menú Principal");
        btnMenuPrincipal.setFont(new Font("Arial", Font.BOLD, 18));
        btnMenuPrincipal.setBackground(new Color(0, 120, 215));
        btnMenuPrincipal.setForeground(Color.WHITE);
        btnMenuPrincipal.setFocusPainted(false);
        btnMenuPrincipal.setPreferredSize(new Dimension(300, 50));
        btnMenuPrincipal.addActionListener(e -> {
            dialogoEpilogo.dispose();
            dispose();
            new MenuEstilo(storyState);
        });
        
        JButton btnReintentarHistoria = new JButton("🔄 Reiniciar Historia");
        btnReintentarHistoria.setFont(new Font("Arial", Font.BOLD, 18));
        btnReintentarHistoria.setBackground(new Color(150, 0, 150));
        btnReintentarHistoria.setForeground(Color.WHITE);
        btnReintentarHistoria.setFocusPainted(false);
        btnReintentarHistoria.setPreferredSize(new Dimension(250, 50));
        btnReintentarHistoria.addActionListener(e -> {
            int confirmacion = JOptionPane.showConfirmDialog(
                dialogoEpilogo,
                "¿Seguro que quieres reiniciar toda la historia?\n" +
                "Se perderá tu progreso actual.",
                "Confirmar Reinicio",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );
            
            if (confirmacion == JOptionPane.YES_OPTION) {
                storyState.reiniciarHistoria();
                dialogoEpilogo.dispose();
                dispose();
                new SeleccionPersonaje();
            }
        });
        
        panelBotones.add(btnMenuPrincipal);
        panelBotones.add(btnReintentarHistoria);
        
        panel.add(titulo, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        panel.add(panelBotones, BorderLayout.SOUTH);
        
        dialogoEpilogo.add(panel);
        dialogoEpilogo.setVisible(true);
    }
    
    // ============================================================================
    // UTILIDADES
    // ============================================================================
    private void confirmarSalida() {
        int confirmacion = JOptionPane.showConfirmDialog(
            this,
            "¿Estás seguro de que quieres salir?\n" +
            "Tu progreso en este capítulo no se guardará.",
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
    
    @Override
    protected void reiniciar() {
        dispose();
        new minijuego3(storyState);
    }
}