import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SeleccionJugador extends JFrame {

    private JTextField nombreField;
    private JComboBox<String> pronombresBox;
    private JLabel avatarPreview;
    private String avatarSeleccionado = "src/images/avatar1.png"; // valor por defecto

    public SeleccionJugador() {
        setTitle("Configuración del Agente");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // pantalla casi completa
        setResizable(true);
        setLayout(new BorderLayout());

        // Fondo
        JLabel fondo = new JLabel();
        fondo.setIcon(new ImageIcon(new ImageIcon("src/images/fondo_menu.png")
                .getImage().getScaledInstance(1920, 1080, Image.SCALE_SMOOTH)));
        fondo.setLayout(new BorderLayout());
        add(fondo);

        // Panel central
        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titulo = new JLabel("Configuración del Agente", SwingConstants.CENTER);
        titulo.setFont(new Font("Verdana", Font.BOLD, 38));
        titulo.setForeground(Color.CYAN);
        gbc.gridwidth = 3;
        gbc.gridx = 0; gbc.gridy = 0;
        centerPanel.add(titulo, gbc);

        // Nombre
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel nombreLabel = new JLabel("Nombre del agente:");
        nombreLabel.setForeground(Color.WHITE);
        nombreLabel.setFont(new Font("Arial", Font.BOLD, 24));
        centerPanel.add(nombreLabel, gbc);

        gbc.gridx = 1; gbc.gridy = 1; gbc.gridwidth = 2;
        nombreField = new JTextField(20);
        nombreField.setFont(new Font("Arial", Font.PLAIN, 22));
        centerPanel.add(nombreField, gbc);

        // Pronombres
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 2;
        JLabel pronombresLabel = new JLabel("Pronombres:");
        pronombresLabel.setForeground(Color.WHITE);
        pronombresLabel.setFont(new Font("Arial", Font.BOLD, 24));
        centerPanel.add(pronombresLabel, gbc);

        gbc.gridx = 1; gbc.gridy = 2; gbc.gridwidth = 2;
        pronombresBox = new JComboBox<>(new String[]{"Él", "Ella", "Elle"});
        pronombresBox.setFont(new Font("Arial", Font.PLAIN, 22));
        centerPanel.add(pronombresBox, gbc);

        // Avatares
        gbc.gridwidth = 3;
        gbc.gridx = 0; gbc.gridy = 3;
        JLabel avatarLabel = new JLabel("Selecciona tu avatar:");
        avatarLabel.setForeground(Color.WHITE);
        avatarLabel.setFont(new Font("Arial", Font.BOLD, 24));
        centerPanel.add(avatarLabel, gbc);

        gbc.gridy = 4;
        JPanel avatarPanel = new JPanel();
        avatarPanel.setOpaque(false);
        avatarPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 40, 10));

        String[] avatares = {
            "src/images/avatar1.png",
            "src/images/avatar2.png",
            "src/images/avatar3.png"
        };

        for (String ruta : avatares) {
            ImageIcon icon = new ImageIcon(new ImageIcon(ruta).getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH));
            JButton avatarBtn = new JButton(icon);
            avatarBtn.setFocusPainted(false);
            avatarBtn.setBorder(BorderFactory.createLineBorder(Color.CYAN, 3));
            avatarBtn.setBackground(Color.BLACK);
            avatarBtn.addActionListener(e -> seleccionarAvatar(ruta));
            avatarPanel.add(avatarBtn);
        }
        centerPanel.add(avatarPanel, gbc);

        // Avatar preview
        gbc.gridy = 5;
        avatarPreview = new JLabel(new ImageIcon(new ImageIcon(avatarSeleccionado).getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH)));
        avatarPreview.setHorizontalAlignment(SwingConstants.CENTER);
        centerPanel.add(avatarPreview, gbc);

        // Botón continuar
        gbc.gridy = 6;
        JButton continuarBtn = new JButton("Comenzar misión");
        continuarBtn.setFont(new Font("Arial", Font.BOLD, 26));
        continuarBtn.setBackground(Color.BLACK);
        continuarBtn.setForeground(Color.CYAN);
        continuarBtn.setFocusPainted(false);
        continuarBtn.setBorder(BorderFactory.createLineBorder(Color.CYAN, 4));

        continuarBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                comenzar();
            }
        });

        centerPanel.add(continuarBtn, gbc);

        fondo.add(centerPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    private void seleccionarAvatar(String ruta) {
        avatarSeleccionado = ruta;
        avatarPreview.setIcon(new ImageIcon(new ImageIcon(ruta).getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH)));
    }

    private void comenzar() {
        String nombre = nombreField.getText().trim();
        String pronombre = (String) pronombresBox.getSelectedItem();

        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, ingresa tu nombre.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        StoryState story = new StoryState();
        story.setNombre(nombre);
        story.setPronombre(pronombre);
        story.setAvatar(avatarSeleccionado);

        dispose();
        new minijuego1(story);
    }
}
