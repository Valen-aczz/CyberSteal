import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.net.URL;
import javax.swing.Timer;
import javax.swing.border.Border;

public class CreditosPanel extends JPanel {

    private JLabel titulo;
    private JLabel infoSecreta;
    private Timer textoTimer; // Timer único para escribir el texto lentamente

    public CreditosPanel(Runnable volverAlMenu) {
        setLayout(new BorderLayout());
        setBackground(new Color(10, 10, 10));
        

        // fondo
        JLabel fondo = new JLabel(new ImageIcon(getClass().getResource("/images/fondo.png")));
        fondo.setLayout(null); // superposición
        add(fondo, BorderLayout.CENTER);

        
       titulo = new JLabel("", SwingConstants.CENTER);
titulo.setFont(getCustomFontAlt(38f));
titulo.setForeground(new Color(255, 30, 30));
titulo.setBounds(0, 50, 1000, 60);
fondo.add(titulo);

// Timer para efecto glitch
Timer glitchTimer = new Timer(100, null);
glitchTimer.addActionListener(e -> {
    // pequeñas desviaciones de posición y color
    int dx = (int)(Math.random() * 6 - 3); // -3 a 3 px
    int dy = (int)(Math.random() * 6 - 3);
    int r = 255;
    int g = (int)(Math.random() * 50);
    int b = (int)(Math.random() * 50);
    titulo.setForeground(new Color(r, g, b));
    titulo.setLocation(dx, 50 + dy);
});
glitchTimer.start();

// efecto de tipeo normal
escribirTextoLento("(SISTEMA -Acceso a CRÉDITOS habilitado", titulo, 80);

       // PANEL CENTRAL CON LOS INTEGRANTES:3
JPanel contenido = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 0)); 
// FlowLayout centrado y con 30 px de separación entre las tarjetas
contenido.setOpaque(false);
contenido.setBounds(0, 120, 1000, 850); 
//  El ancho total de la ventana es 1000 px
fondo.add(contenido);

Object[][] integrantes = {
    {"Majo", "Gestora del Proyecto", "/images/Majo.jpg", "Registro: Estrategia general y control de recursos."},
    {"Karla", "Diseñadora", "/images/karla.png", "Registro: Interfaz visual y coherencia estética."},
    {"Valen", "Programadora", "/images/Valen.jpg", "Registro: Arquitectura del sistema, narrativa y eventos."},
    {"Mau", "Documentador", "/images/Mau.png", "Registro: Redacción técnica y documentación de desarrollo."}
};

for (Object[] i : integrantes) {
    contenido.add(crearRegistro(
        (String) i[0],
        (String) i[1],
        (String) i[2],
        (String) i[3]
    ));
}


JPanel panelCreditos = new JPanel();
panelCreditos.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 20));
panelCreditos.setOpaque(false);

panelCreditos.add(crearTarjeta("images/Majo.jpg", "Majo", "Gestora del Proyecto"));
panelCreditos.add(crearTarjeta("images/karla.png", "Karla", "Diseñadora"));
panelCreditos.add(crearTarjeta("images/Valen.jpg", "Valen", "Programadora"));
panelCreditos.add(crearTarjeta("images/Mau.png", "Mau", "Documentador"));



// panel inferior
JPanel panelInferior = new JPanel(null);
panelInferior.setOpaque(false);
panelInferior.setBounds(0, 0, 1000, 600); // ocupa toda la ventana para posicionar libremente
fondo.add(panelInferior);


//Dios mío señor sálvame

//  BOTÓN de volver al menu
JButton backButton = new JButton("← Volver al Menú");
backButton.setFont(new Font("Consolas", Font.BOLD, 16));
backButton.setForeground(Color.WHITE);
backButton.setBackground(new Color(30, 30, 30));
backButton.setFocusPainted(false);
backButton.setBorder(BorderFactory.createLineBorder(new Color(0, 180, 255), 2));
backButton.setBounds(50, 470, 220, 40); // Abajo a la izquierda

// hoover
backButton.addMouseListener(new java.awt.event.MouseAdapter() {
    @Override
    public void mouseEntered(java.awt.event.MouseEvent e) {
        MenuEstilo.reproducirSonido("/sonidos/hover.wav");
        backButton.setBackground(new Color(0, 60, 100));
        backButton.setForeground(new Color(0, 255, 255));
    }

    @Override
    public void mouseExited(java.awt.event.MouseEvent e) {
        backButton.setBackground(new Color(30, 30, 30));
        backButton.setForeground(Color.WHITE);
    }
});

// acción del botón
backButton.addActionListener(e -> {
    MenuEstilo.reproducirSonido("/sonidos/click.wav");
    volverAlMenu.run(); // vuelve al menú principal
});

// Agregar al fondo
fondo.add(backButton);


// Texto Pie de Página
JLabel pie = new JLabel(
    "<html>Asignatura: Algoritmia y Programación II<br>"
            //Jlabel no interpreta \n como salto de línea, por eso uso br
  + "Prof: Leddy Diana Pájaro<br>"
  + "Monitora: Angélica Baños</html>"
);
getCustomFontAlt(24f);
pie.setForeground(new Color(255, 30, 30));
pie.setBounds(560, 460, 480, 70); // coordenadas libres
panelInferior.add(pie);


        
        
        
        
        // ZONA SECRETA
        JLayeredPane layered = new JLayeredPane();
        layered.setBounds(0, 0, 800, 600);
        fondo.add(layered, Integer.valueOf(1));

        JPanel zonaSecreta = new JPanel();
        zonaSecreta.setOpaque(false);
        zonaSecreta.setBounds(40, 40, 150, 150);
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

        // Efecto glitch q cambia color y posición ligeramente
        infoGlitch = new Timer(120, ev -> {
            int dx = (int)(Math.random() * 4 - 2); // -2 a 2 px
            int dy = (int)(Math.random() * 4 - 2);
            int r = 0;
            int g = 180; // fij0
            int b = 200 + (int)(Math.random() * 55);
            infoSecreta.setForeground(new Color(r, g, b));
            infoSecreta.setLocation(200 + dx, 70 + dy);
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
        // restaurar posición y color original
        infoSecreta.setLocation(200, 70);
        infoSecreta.setForeground(new Color(0, 180, 255));
    }
});

    }

// CREAMOS CADA FICHA DE INTEGRANTE
private JPanel crearRegistro(String nombre, String rol, String imagenRuta, String descripcion) {
    JPanel registro = new JPanel(new BorderLayout()) {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            for (int y = 0; y < getHeight(); y++) {
    for (int x = 0; x < getWidth(); x++) {
        int brillo = (int)(Math.random() * 20); // puntitos grises
        g2.setColor(new Color(15+brillo, 15+brillo, 15+brillo));
        g2.fillRect(x, y, 1, 1);
    }
}
            g2.dispose();
        }
    };
    registro.setBackground(new Color(15, 15, 15));
registro.setBackground(new Color(15, 15, 15));

// 🔴🔵 Borde glitch doble (rojo + azul)
    Border bordeGlitch = BorderFactory.createCompoundBorder(
    BorderFactory.createLineBorder(new Color(255, 40, 40), 1), // capa externa: rojo
    BorderFactory.createLineBorder(new Color(0, 180, 255), 2)  // capa interna: azul
);
registro.setBorder(bordeGlitch);

registro.setPreferredSize(new Dimension(210, 280));


    // Imagen circular
    ImageIcon icon = new ImageIcon(getClass().getResource(imagenRuta));
    Image scaled = scaleImage(icon.getImage(), 100, 100);
    JLabel imagen = new JLabel(new ImageIcon(makeCircularImage(scaled)));
    imagen.setHorizontalAlignment(SwingConstants.CENTER);
    imagen.setBorder(new EmptyBorder(10, 10, 0, 10));

    // nombre y rol
    JLabel nombreLabel = new JLabel(nombre, SwingConstants.CENTER);
    nombreLabel.setFont(new Font("Consolas", Font.BOLD, 15));
    nombreLabel.setForeground(new Color(200, 240, 255));

    JLabel rolLabel = new JLabel(rol, SwingConstants.CENTER);
    rolLabel.setFont(new Font("Consolas", Font.PLAIN, 13));
    rolLabel.setForeground(new Color(140, 210, 230));

    JPanel textoPanel = new JPanel(new GridLayout(2, 1));
    textoPanel.setOpaque(false);
    textoPanel.add(nombreLabel);
    textoPanel.add(rolLabel);

    // el panel secreto
    JPanel secretoPanel = new JPanel(new BorderLayout());
    secretoPanel.setBackground(new Color(0, 0, 0, 170));
    secretoPanel.setBorder(BorderFactory.createLineBorder(new Color(0, 180, 255), 1));
    secretoPanel.setVisible(false);

    JLabel secretoLabel = new JLabel("<html><div style='text-align:center;'>" + descripcion + "</div></html>", SwingConstants.CENTER);
    secretoLabel.setFont(new Font("Consolas", Font.ITALIC, 12));
    secretoLabel.setForeground(new Color(180, 230, 255));
    secretoPanel.add(secretoLabel, BorderLayout.CENTER);

//pongo el hover y el click aquí
    registro.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseEntered(MouseEvent e) {
            secretoPanel.setVisible(true);
            MenuEstilo.reproducirSonido("/sonidos/hover.wav");
            registro.setBackground(new Color(25, 25, 25));
        }

        @Override
        public void mouseExited(MouseEvent e) {
            secretoPanel.setVisible(false);
            registro.setBackground(new Color(15, 15, 15));
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            MenuEstilo.reproducirSonido("/sonidos/click.wav");

            // Datos personalizados
            String infoExtra = switch (nombre) {
    case "Majo" -> """
            🧠 Nombre completo: María José Martínez Fernandez
            🎂 Edad: 18 años
            🎓 Carrera: Ciencia de Datos
            📘 Semestre: 2°
            ☕ Nivel de estrés: 89%
            🧩 Rol: Líder del equipo
            💬 Frase: "Jungkook te amo."
            """;
    case "Karla" -> """
            🎨 Nombre completo: Karla Sofía Paredes
            🎂 Edad: 18 años
            🎓 Carrera: Ciencia de Datos
            📘 Semestre: 2°
            🧩 Rol: Diseñadora
            💾 Error recurrente: 'SyntaxError: missing motivation'
            💬 Frase: "Eche cole."
            """;
    case "Valen" -> """
            💻 Nombre completo: Valentina Arias Cortez
            🎂 Edad: 17 años
            🎓 Carrera: Ciencia de Datos
            📘 Semestre: 2°
            🧩 Rol: Programadora
            💀 Nivel de estrés: 404 Not Found
            💬 Frase: "Quiero una salchipapa."
            """;
    case "Mau" -> """
            📜 Nombre completo: Mauricio Orozco
            🎂 Edad: 18 años
            🎓 Carrera: Ciencia de Datos
            📘 Semestre: 2°
            🧩 Rol: Documentador
            🧠 Lenguaje favorito: no sé, español(?
            💬 Frase: "Touché."
            """;
    default -> "Información no disponible.";
};

            JOptionPane.showMessageDialog(
                registro,
                infoExtra,
                "📁 Expediente de " + nombre,
                JOptionPane.INFORMATION_MESSAGE
            );
        }
    });

    registro.add(imagen, BorderLayout.NORTH);
    registro.add(textoPanel, BorderLayout.CENTER);
    registro.add(secretoPanel, BorderLayout.SOUTH);

    return registro;
}




// Escala laa imagen con graphics2d
private Image scaleImage(Image src, int w, int h) {
    BufferedImage resized = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2 = resized.createGraphics();
    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
    g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g2.drawImage(src, 0, 0, w, h, null);
    g2.dispose();
    return resized;
}



 // crea una imagen circular
private Image makeCircularImage(Image image) {
    int width = image.getWidth(null);
    int height = image.getHeight(null);

    // Si la imagen no se cargó correctamente
    if (width <= 0 || height <= 0) {
        int size = 100;
        BufferedImage placeholder = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = placeholder.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.DARK_GRAY);
        g2.fillOval(0, 0, size, size);
        g2.setColor(Color.LIGHT_GRAY);
        g2.drawOval(0, 0, size - 1, size - 1);
        g2.dispose();
        return placeholder;
    }

    int size = Math.min(width, height);
    BufferedImage circular = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);

    Graphics2D g2 = circular.createGraphics();
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    g2.setClip(new java.awt.geom.Ellipse2D.Float(0, 0, size, size));
    g2.drawImage(image, 0, 0, size, size, null);
    g2.dispose();

    return circular;
}


    // efecto de tipeo
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


private JPanel crearTarjeta(String rutaImagen, String nombre, String rol) {
    // panel contenedor de la tarjeta
    JPanel tarjeta = new JPanel();
    tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
    tarjeta.setPreferredSize(new Dimension(220, 300));  // ancho x alto
    tarjeta.setMaximumSize(new Dimension(220, 300));
    tarjeta.setBackground(Color.BLACK);
    tarjeta.setBorder(BorderFactory.createLineBorder(Color.CYAN));

    // Imagen circular
    ImageIcon icon = new ImageIcon(rutaImagen);
    Image img = icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
    BufferedImage buffered = new BufferedImage(150, 150, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2 = buffered.createGraphics();
    g2.setClip(new java.awt.geom.Ellipse2D.Float(0, 0, 150, 150));
    g2.drawImage(img, 0, 0, null);
    g2.dispose();
    JLabel labelImagen = new JLabel(new ImageIcon(buffered));
    labelImagen.setAlignmentX(Component.CENTER_ALIGNMENT);

    // Nombre
    JLabel labelNombre = new JLabel(nombre, SwingConstants.CENTER);
    labelNombre.setForeground(Color.WHITE);
    labelNombre.setFont(new Font("Consolas", Font.BOLD, 14));
    labelNombre.setAlignmentX(Component.CENTER_ALIGNMENT);

    // Rol
    JLabel labelRol = new JLabel(rol, SwingConstants.CENTER);
    labelRol.setForeground(Color.CYAN);
    labelRol.setFont(new Font("Consolas", Font.PLAIN, 12));
    labelRol.setAlignmentX(Component.CENTER_ALIGNMENT);

    // Agregamos todo al panel
    tarjeta.add(Box.createVerticalStrut(10)); // espacio arribita
    tarjeta.add(labelImagen);
    tarjeta.add(Box.createVerticalStrut(10));
    tarjeta.add(labelNombre);
    tarjeta.add(Box.createVerticalStrut(5));
    tarjeta.add(labelRol);

    return tarjeta;
}

// Método para cargar otra fuente pq la que tenía no me gustó
private Font getCustomFontAlt(float size) {
    try {
        Font customFont = Font.createFont(
            Font.TRUETYPE_FONT,
            getClass().getResourceAsStream("/fonts/fuente.otf") // cambia el nombre aquí
        );
        return customFont.deriveFont(Font.BOLD, size);
    } catch (Exception e) {
        System.out.println("No se pudo cargar la segunda fuente, usando Arial.");
        return new Font("Arial", Font.BOLD, (int) size);
    }
}








}