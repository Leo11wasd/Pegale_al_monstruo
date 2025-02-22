import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

public class GridInterface extends JFrame {

    private JLabel[] labels = new JLabel[9];

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
            case 0:
                button1Clicked();
                break;
            case 1:
                button2Clicked();
                break;
            case 2:
                button3Clicked();
                break;
            case 3:
                button4Clicked();
                break;
            case 4:
                button5Clicked();
                break;
            case 5:
                button6Clicked();
                break;
            case 6:
                button7Clicked();
                break;
            case 7:
                button8Clicked();
                break;
            case 8:
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

    private void button1Clicked() {
        /* Acción para el botón 1 */
        // verificamos si el topo esta aqui

        //si si esta, mandamos un mensaje tcp
    }
    public void muestra_topo(int i,int j){
        int pos=(i*3)+j;
        setImageToLabel(pos, "/assets/topo.png");
    }
    private void button2Clicked() { /* la misma idea del 1 */ }

    private void button3Clicked() { /* la misma idea del 1 */ }

    private void button4Clicked() { /* la misma idea del 1 */ }

    private void button5Clicked() { /* la misma idea del 1 */ }

    private void button6Clicked() { /* la misma idea del 1 */ }

    private void button7Clicked() { /* la misma idea del 1 */ }

    private void button8Clicked() { /* la misma idea del 1 */}

    private void button9Clicked() { /* la misma idea del 1 */ }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                GridInterface interfaz=new GridInterface();
                //System.out.println(i+" "+j+" "+pos);
            }
        });
    }
}
