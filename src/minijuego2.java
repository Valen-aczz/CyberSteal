import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class minijuego2 extends JFrame {
    private StoryState storyState;

    public minijuego2(StoryState state) {
        this.storyState = state;
        setTitle("Minijuego 2 - The Zero-Trust");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel titulo = new JLabel("Capítulo 2: The Zero-Trust", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        add(titulo, BorderLayout.NORTH);

        JPanel botonesPanel = new JPanel(new GridLayout(1, 2, 10, 10));

        JButton ganarBtn = new JButton("Ganar");
        JButton perderBtn = new JButton("Perder");

        botonesPanel.add(ganarBtn);
        botonesPanel.add(perderBtn);
        add(botonesPanel, BorderLayout.CENTER);

        ganarBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                finalizar(true);
            }
        });

        perderBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                finalizar(false);
            }
        });

        setVisible(true);
    }

    private void finalizar(boolean victoria) {
        String mensaje;
        if (victoria) {
            mensaje = "¡Has ganado el Capítulo 2!\n¿Qué deseas hacer?";
        } else {
            mensaje = "Perdiste el Capítulo 2.\nPuedes reintentar, salir o continuar igualmente.";
        }

        String[] opciones = {"Reintentar", "Siguiente Capítulo", "Salir al Menú"};
        int eleccion = JOptionPane.showOptionDialog(
                this,
                mensaje,
                "Fin del Capítulo 2",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]
        );

        switch (eleccion) {
            case 0: // Reintentar
                dispose();
                new minijuego2(storyState);
                break;

            case 1: // Siguiente capítulo (va directo al 3)
                if (victoria) {
                    storyState.setCap2Completado(true);
                    storyState.setCap2Ganado(true);
                } else {
                    storyState.setCap2Completado(true);
                    storyState.setCap2Ganado(false);
                }
                dispose();
                new minijuego3(storyState);
                break;

            case 2: // Salir al menú
            default:
                if (victoria) {
                    storyState.setCap2Completado(true);
                    storyState.setCap2Ganado(true);
                }
                dispose();
                new MenuEstilo(storyState);
                break;
        }
    }
}

