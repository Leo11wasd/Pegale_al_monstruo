package Cliente;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

public class GridInterface extends JFrame {

    private JLabel[] labels = new JLabel[9];
    public boolean tiene_topo1 = false;
    public boolean tiene_topo2 = false;
    public boolean tiene_topo3 = false;
    public boolean tiene_topo4 = false;
    public boolean tiene_topo5 = false;
    public boolean tiene_topo6 = false;
    public boolean tiene_topo7 = false;
    public boolean tiene_topo8 = false;
    public boolean tiene_topo0 = false;
    public boolean manda_mensaje_hit = false;


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
            setImageToLabel(i, false);

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
        setImageToLabel(index, false);
    }

    // Método para colocar una imagen en el JLabel correspondiente
    public void setImageToLabel(int index, boolean poner_topo) {
        String imagePath;
        if (poner_topo) {
            //System.out.println("poner_topo");
            imagePath = "/assets/topo.jpg";

        } else {
            //System.out.printf("poner_pasto");
            imagePath = "/assets/pasto.jpeg";
        }
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

    public void limpiar() {
        for (int i = 0; i < 9; i++) {
            setImageToLabel(i, false);
            marca(i, false);
        }
    }

    public void marca(int pos, boolean valor) {
        switch (pos) {
            case 0:
                tiene_topo0 = valor;
                break;
            case 1:
                tiene_topo1 = valor;
                break;
            case 2:
                tiene_topo2 = valor;
                break;
            case 3:
                tiene_topo3 = valor;
                break;
            case 4:
                tiene_topo4 = valor;
                break;
            case 5:
                tiene_topo5 = valor;
                break;
            case 6:
                tiene_topo6 = valor;
                break;
            case 7:
                tiene_topo7 = valor;
                break;
            case 8:
                tiene_topo8 = valor;
                break;
            default:
                break;
        }
    }

    public void muestra_topo(int pos) {
        //int pos = (i * 3) + j;
        //System.out.println("Pos: " + pos);
        setImageToLabel(pos, true);
        marca(pos, true);
    }

    private void button1Clicked() {
        setImageToLabel(0, false);
        //verificamos si el topo esta aqui
        if (tiene_topo0) {
            marca(0, false);
            //manda el mensaje
            this.manda_mensaje_hit = true;

        }
    }

    private void button2Clicked() {
        setImageToLabel(1, false);
        //verificamos si el topo esta aqui
        if (tiene_topo1) {
            marca(1, false);
            //manda el mensaje
            this.manda_mensaje_hit = true;
        }
    }

    private void button3Clicked() {
        setImageToLabel(2, false);
        //verificamos si el topo esta aqui
        if (tiene_topo2) {
            marca(0, false);
            //manda el mensaje
            this.manda_mensaje_hit = true;
        }
    }

    private void button4Clicked() {
        setImageToLabel(3, false);
        if (tiene_topo3) {
            marca(3, false);
            this.manda_mensaje_hit = true;
        }
    }

    private void button5Clicked() {
        setImageToLabel(4, false);
        if (tiene_topo4) {
            marca(4, false);
            this.manda_mensaje_hit = true;
        }
    }

    private void button6Clicked() {
        setImageToLabel(5, false);
        if (tiene_topo5) {
            marca(5, false);
            this.manda_mensaje_hit = true;
        }
    }

    private void button7Clicked() {
        setImageToLabel(6, false);
        if (tiene_topo6) {
            marca(6, false);
            this.manda_mensaje_hit = true;
        }
    }

    private void button8Clicked() {
        setImageToLabel(7, false);
        if (tiene_topo7) {
            marca(7, false);
            this.manda_mensaje_hit = true;
        }
    }

    private void button9Clicked() {
        setImageToLabel(8, false);
        if (tiene_topo8) {
            marca(8, false);
            this.manda_mensaje_hit = true;
        }
    }

    /*public static void main(String[] args) {

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

    }*/
}
