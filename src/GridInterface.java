import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

public class GridInterface extends JFrame {

    private JLabel[] labels = new JLabel[9];
    public boolean dio_click = false;

    public GridInterface() {
        setTitle("Grid Interface");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 600);
        setLayout(new GridLayout(3, 3));

        // Crear paneles para cada celda con label y botón
        for (int i = 0; i < 9; i++) {
            JPanel panel = new JPanel();
            panel.setLayout(new BorderLayout());

            labels[i] = new JLabel("Label " + (i + 1), SwingConstants.CENTER);
            labels[i].setHorizontalAlignment(JLabel.CENTER);
            labels[i].setVerticalAlignment(JLabel.CENTER);

            JButton button = new JButton("Button " + (i + 1));

            int index = i; // Necesario para usar en el listener
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    onButtonClick(index);
                }

            });

            panel.add(labels[i], BorderLayout.CENTER);
            panel.add(button, BorderLayout.SOUTH);
            add(panel);

        }
        setVisible(true);

        for (int i = 0; i < 9; i++) {
            setImageToLabel(i, "/assets/pasto.jpeg");

        }
    }

    // Métodos vacíos para manejar clics en los botones
    private void onButtonClick(int index) {
        switch (index) {
            case 1:
                button1Clicked();
                break;
            case 2:
                button2Clicked();
                break;
            case 3:
                button3Clicked();
                break;
            case 4:
                button4Clicked();
                break;
            case 5:
                button5Clicked();
                break;
            case 6:
                button6Clicked();
                break;
            case 7:
                button7Clicked();
                break;
            case 8:
                button8Clicked();
                break;
            case 9:
                button9Clicked();
                break;
        }
        // Cargar imagen al hacer clic desde la carpeta assets
        setImageToLabel(index, "/assets/pasto.jpeg");
    }

    // Método para colocar una imagen en el JLabel correspondiente
    private void setImageToLabel(int index, String imagePath) {
        URL imageURL = getClass().getResource(imagePath);
        if (imageURL != null) {
            ImageIcon icon = new ImageIcon(imageURL);
            Image scaledImage = icon.getImage().getScaledInstance(labels[index].getWidth(), labels[index].getHeight(), Image.SCALE_SMOOTH);
            labels[index].setIcon(new ImageIcon(scaledImage));
            labels[index].setText(""); // Quitar texto al colocar la imagen
        } else {
            System.out.println("No se encontró la imagen: " + imagePath);
        }
    }

    public void muestra_topo(int i, int j) {
        int pos = (i * 3) + j;
        setImageToLabel(pos, "/assets/topo.png");
    }

    private void button1Clicked() {
        /* Acción para el botón 1 */
        // verificamos si el topo esta aqui
        setImageToLabel(1, "/assets/pasto.jpeg");
        dio_click = true;
        //si si esta, mandamos un mensaje tcp
    }

    private void button2Clicked() {

        setImageToLabel(2, "/assets/pasto.jpeg");
        dio_click = true;
    }

    private void button3Clicked() {

        setImageToLabel(3, "/assets/pasto.jpeg");
        dio_click = true;
    }

    private void button4Clicked() {

        setImageToLabel(4, "/assets/pasto.jpeg");
        dio_click = true;
    }

    private void button5Clicked() {
        setImageToLabel(5, "/assets/pasto.jpeg");
        dio_click = true;
    }

    private void button6Clicked() {
        setImageToLabel(6, "/assets/pasto.jpeg");
        dio_click = true;
    }

    private void button7Clicked() {
        setImageToLabel(7, "/assets/pasto.jpeg");
        dio_click = true;
    }

    private void button8Clicked() {
        setImageToLabel(8, "/assets/pasto.jpeg");
        dio_click = true;
    }

    private void button9Clicked() {
        setImageToLabel(9, "/assets/pasto.jpeg");
        dio_click = true;
    }

    public static void main(String[] args) {

        int coordenadas, i, j;
        GridInterface interfaz = new GridInterface();
        int cont = 0;
        while (cont < 10) {
            coordenadas = (int) (Math.random() * (8));
            i = coordenadas / 3;
            j = coordenadas % 3;
            interfaz.muestra_topo(i, j);
            interfaz.dio_click = false;
            while (interfaz.dio_click==false) {
            }
            System.out.println("ya sali");
            interfaz.dio_click = true;
            System.out.println("van :" + cont);
            cont++;
        }

    }
}
