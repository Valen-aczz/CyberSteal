import javax.swing.*;
import java.awt.*;

public class SeleccionPersonaje extends JFrame {

    private JTextField nombreField;
    private String generoSeleccionado = "hombre"; // valor por defecto
    private JLabel previewLabel;

    public SeleccionPersonaje() {
        
          // CURSOR PERSONALIZADOOp
        ImageIcon cursorIcon = new ImageIcon(getClass().getResource("/images/cursor.png"));
        Image cursorImage = cursorIcon.getImage();
        Cursor customCursor = Toolkit.getDefaultToolkit().createCustomCursor(
                cursorImage,
                new Point(0, 0),
                "CursorPersonalizado"
        );
        this.setCursor(customCursor);
        
        
        
        setTitle("Selección de Personaje - CYBERSTEAL");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Panel principal con fondo
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.BLACK);

        // Título
        JLabel titulo = new JLabel("SELECCIÓN DE AGENTE", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 36));
        titulo.setForeground(Color.CYAN);
        titulo.setBorder(BorderFactory.createEmptyBorder(30, 0, 20, 0));
        mainPanel.add(titulo, BorderLayout.NORTH);

        // Panel central con contenido
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(Color.BLACK);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Campo de nombre
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel nombreLabel = new JLabel("Nombre del agente:");
        nombreLabel.setForeground(Color.WHITE);
        nombreLabel.setFont(new Font("Arial", Font.BOLD, 20));
        centerPanel.add(nombreLabel, gbc);

        gbc.gridx = 1; gbc.gridy = 0;
        gbc.gridwidth = 2;
        nombreField = new JTextField(20);
        nombreField.setFont(new Font("Arial", Font.PLAIN, 18));
        nombreField.setPreferredSize(new Dimension(300, 35));
        centerPanel.add(nombreField, gbc);

        // Selección de género
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.gridwidth = 1;
        JLabel generoLabel = new JLabel("Género:");
        generoLabel.setForeground(Color.WHITE);
        generoLabel.setFont(new Font("Arial", Font.BOLD, 20));
        centerPanel.add(generoLabel, gbc);

        gbc.gridx = 1; gbc.gridy = 1;
        gbc.gridwidth = 2;

        JPanel generoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 10));
        generoPanel.setBackground(Color.BLACK);

        JRadioButton hombreBtn = new JRadioButton("Hombre");
        JRadioButton mujerBtn = new JRadioButton("Mujer");

        Font radioFont = new Font("Arial", Font.BOLD, 18);
        hombreBtn.setFont(radioFont);
        mujerBtn.setFont(radioFont);
        hombreBtn.setForeground(Color.WHITE);
        mujerBtn.setForeground(Color.WHITE);
        hombreBtn.setBackground(Color.BLACK);
        mujerBtn.setBackground(Color.BLACK);

        ButtonGroup generoGroup = new ButtonGroup();
        generoGroup.add(hombreBtn);
        generoGroup.add(mujerBtn);
        hombreBtn.setSelected(true);

        hombreBtn.addActionListener(e -> {
            generoSeleccionado = "hombre";
            actualizarPreview();
        });

        mujerBtn.addActionListener(e -> {
            generoSeleccionado = "mujer";
            actualizarPreview();
        });

        generoPanel.add(hombreBtn);
        generoPanel.add(mujerBtn);
        centerPanel.add(generoPanel, gbc);

        // Preview
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 3;
        JLabel previewTitle = new JLabel("Vista previa:", SwingConstants.CENTER);
        previewTitle.setForeground(Color.YELLOW);
        previewTitle.setFont(new Font("Arial", Font.BOLD, 22));
        centerPanel.add(previewTitle, gbc);

        gbc.gridy = 3;
        previewLabel = new JLabel("", SwingConstants.CENTER);
        previewLabel.setPreferredSize(new Dimension(300, 200));
        previewLabel.setBorder(BorderFactory.createLineBorder(Color.CYAN, 2));
        actualizarPreview();
        centerPanel.add(previewLabel, gbc);

        // Botón de comenzar
        gbc.gridy = 4;
        gbc.insets = new Insets(30, 15, 15, 15);
        JButton comenzarBtn = new JButton("INICIAR MISIÓN");
        comenzarBtn.setFont(new Font("Arial", Font.BOLD, 24));
        comenzarBtn.setBackground(Color.BLACK);
        comenzarBtn.setForeground(Color.CYAN);
        comenzarBtn.setFocusPainted(false);
        comenzarBtn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.CYAN, 3),
                BorderFactory.createEmptyBorder(10, 30, 10, 30)
        ));
        comenzarBtn.addActionListener(e -> comenzarMision());
        centerPanel.add(comenzarBtn, gbc);

        mainPanel.add(centerPanel, BorderLayout.CENTER);
        add(mainPanel);
        setVisible(true);
    }
//el jugador puede usar el avatar femenino o masculino si prefiere.
    private void actualizarPreview() {
        previewLabel.setText("<html><center style='color:white;'>"
                + (generoSeleccionado.equals("hombre") ? "🔧 Agente Masculino<br>🎯 Especialista en Sistemas"
                : "💻 Agente Femenino<br>🎯 Experta en Ciberseguridad")
                + "</center></html>");
    }

    private void comenzarMision() {
        String nombre = nombreField.getText().trim();
        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Pon un nombre 😭", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        StoryState storyState = new StoryState();
        storyState.setNombre(nombre);
        storyState.setGenero(generoSeleccionado);
        storyState.setPronombre(generoSeleccionado.equals("hombre") ? "Él" : "Ella");

        MenuEstilo.reproducirSonido("/sonidos/click.wav");

        //CAMBIAMOS DE PANTALLA SIN CERRAR
        setContentPane(new SeleccionMisionPanel(storyState, this));
        revalidate();
        repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SeleccionPersonaje::new);
    }
}
