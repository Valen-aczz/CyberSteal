import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.net.URL;

public abstract class BaseCapituloFrame extends JFrame {
    protected StoryState storyState;
    protected JPanel centerPanel;
    private Image bgImage;

    public BaseCapituloFrame(StoryState state, String titulo, String rutaFondo) {
        this.storyState = state;
        setTitle(titulo);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Pantalla casi completa ( o sea maximizada)
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setResizable(true);

        // Cargar la imagen desde recursos
        URL bgURL = getClass().getResource(rutaFondo.startsWith("/") ? rutaFondo : "/" + rutaFondo);
        if (bgURL == null) {
            System.err.println("❌ No se encontró la imagen de fondo en: " + rutaFondo);
        } else {
            bgImage = new ImageIcon(bgURL).getImage();
        }

        //  Panel de fondo personalizado
        JPanel backgroundPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (bgImage != null) {
                    int width = getWidth();
                    int height = getHeight();
                    g.drawImage(bgImage, 0, 0, width, height, this);
                }
            }
        };

        backgroundPanel.setOpaque(false);

        // Forzar el coso del repintado cuando la ventana cambia de tamaño
        backgroundPanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                backgroundPanel.repaint();
            }

            @Override
            public void componentShown(ComponentEvent e) {
                backgroundPanel.repaint();
            }
        });

    //Panel central para los elementos
        centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        backgroundPanel.add(centerPanel, BorderLayout.CENTER);
        add(backgroundPanel, BorderLayout.CENTER);

  // Forzar repaint tras mostrar ventana
        SwingUtilities.invokeLater(() -> backgroundPanel.repaint());
    }

    protected abstract void reiniciar();

    protected void finalizarCapitulo(int capitulo, boolean victoria, Runnable siguienteCapitulo) {
        String mensaje = victoria
                ? "¡Has ganado el Capítulo " + capitulo + "!\n¿Qué deseas hacer?"
                : "Perdiste el Capítulo " + capitulo + ".\nPuedes reintentar, salir o continuar igualmente.";

        String[] opciones = {"Reintentar", "Siguiente Capítulo", "Salir al Menú"};
        int eleccion = JOptionPane.showOptionDialog(
                this,
                mensaje,
                "Fin del Capítulo " + capitulo,
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]
        );

        switch (eleccion) {
            case 0:
                dispose();
                reiniciar();
                break;
            case 1:
                storyState.marcarCapitulo(capitulo, victoria);
                dispose();
                siguienteCapitulo.run();
                break;
            case 2:
            default:
                if (victoria) storyState.marcarCapitulo(capitulo, true);
                dispose();
                new MenuEstilo(storyState);
                break;
        }
    }
}