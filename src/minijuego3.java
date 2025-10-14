import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class minijuego3 extends JFrame {
    private StoryState storyState;

    public minijuego3(StoryState state) {
        this.storyState = state;
        setTitle("Minijuego 3 - Phishing for Gold");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel titulo = new JLabel("Capítulo 3: Phishing for Gold", SwingConstants.CENTER);
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
            mensaje = "¡Has completado el Capítulo 3 con éxito!\n¿Qué deseas hacer?";
        } else {
            mensaje = "Perdiste el Capítulo 3.\nPuedes reintentar o salir al menú.";
        }

        String[] opciones;
        if (victoria) {
            opciones = new String[]{"Reintentar", "Finalizar Historia", "Salir al Menú"};
        } else {
            opciones = new String[]{"Reintentar", "Salir al Menú"};
        }

        int eleccion = JOptionPane.showOptionDialog(
                this,
                mensaje,
                "Fin del Capítulo 3",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]
        );

        switch (eleccion) {
            case 0: // Reintentar
                dispose();
                new minijuego3(storyState);
                break;

            case 1:
                if (victoria) {
                    storyState.setCap3Completado(true);
                    storyState.setCap3Ganado(true);
                    mostrarFinalHistoria();
                } else {
                    dispose();
                    new MenuEstilo(storyState);
                }
                break;

            case 2: // Salir al menú (solo existe si ganó)
                storyState.setCap3Completado(true);
                storyState.setCap3Ganado(true);
                dispose();
                new MenuEstilo(storyState);
                break;
        }
    }

    private void mostrarFinalHistoria() {
        // Aquí meteré una escena final o escena de créditos
        JOptionPane.showMessageDialog(
                this,
                "Felicidades salchipapo! completaste toda la historia.\n" +
                "Tu camino ha sido registrado y desbloqueaste el epílogo q no he hecho ni consultado a nadie.",
                "Fin de la Historia",
                JOptionPane.INFORMATION_MESSAGE
        );

        dispose();
        new MenuEstilo(storyState);
    }
}
