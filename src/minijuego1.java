import javax.swing.*;
import java.awt.*;

public class minijuego1 extends JFrame {
    public minijuego1() {
        setTitle("Lock & Code - Password Defender");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel label = new JLabel("Bienvenido a Lock & Code bestie", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 28));
        add(label);

        setVisible(true);
    }
}