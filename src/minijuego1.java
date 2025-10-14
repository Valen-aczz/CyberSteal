import javax.swing.*;
import java.awt.*;

public class minijuego1 extends BaseCapituloFrame {

    public minijuego1(StoryState state) {
        super(state, "Capítulo 1: Lock & Code", "images/Fondo2.png");

        centerPanel.setLayout(new GridLayout(1, 3, 30, 0));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(0, 200, 100, 200));

        JButton ganarBtn = crearBoton("Ganar", () -> finalizarCapitulo(1, true, () -> new minijuego2(storyState)));
        JButton perderBtn = crearBoton("Perder", () -> finalizarCapitulo(1, false, () -> new minijuego2(storyState)));
        JButton salirBtn = crearBoton("Salir", () -> {
            dispose();
            new MenuEstilo(storyState);
        });

        centerPanel.add(ganarBtn);
        centerPanel.add(perderBtn);
        centerPanel.add(salirBtn);

        setVisible(true);
    }

    @Override
    protected void reiniciar() {
        new minijuego1(storyState);
    }

    private JButton crearBoton(String texto, Runnable accion) {
        JButton b = new JButton(texto);
        b.setFont(new Font("Arial", Font.BOLD, 28));
        b.setBackground(Color.BLACK);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createLineBorder(Color.CYAN, 4));
        b.addActionListener(e -> accion.run());
        return b;
    }
}
