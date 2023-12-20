package BattleShip;
import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Juego implements KeyListener {
    private GamePanel gamePanel;
    /*
    Metodo que comvoca a todo lo importante del juego y muestra los paneles de interfaz
    */
    public Juego() {
        String[] initialOptions = new String[]{"PvP", "PvC"};
        String initialMessage = "Escoge una opcion";
        int initialChoice = JOptionPane.showOptionDialog(null, initialMessage,
                "Opciones iniciales",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, initialOptions, initialOptions[0]);

        if (initialChoice == 0) {
            String[] opciones3 = new String[]{"Servidor", "Cliente"};
            String mensaje3 = "Escoge una opcion";
            int initialChoice3 = JOptionPane.showOptionDialog(null, mensaje3,
                    "Opciones iniciales",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                    null, opciones3, opciones3[0]);

            if(initialChoice3 == 0){

                String[] opciones4 = new String[]{"Lo", "Sentimos"};
                String message = "No se pudo realizar esta parte por diversas cosas";
                JOptionPane.showOptionDialog(null, message,
                        "Perdon",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                        null, opciones4, opciones4[0]);


            } else if(initialChoice3 == 1){
                String[] opciones4 = new String[]{"Lo", "Sentimos"};
                String message = "No se pudo realizar esta parte por diversas cosas";
                JOptionPane.showOptionDialog(null, message,
                        "Perdon",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                        null, opciones4, opciones4[0]);
            }
        } else if (initialChoice == 1) {
            String[] options = new String[]{"Facil", "Medio", "Dificil"};
            String message = "Selecione la dificultad con la que desea jugar";
            int difficultyChoice = JOptionPane.showOptionDialog(null, message,
                    "Seleccione la dificultad",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                    null, options, options[0]);

            JFrame frame = new JFrame("Battleship");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);

            gamePanel = new GamePanel(difficultyChoice);
            frame.getContentPane().add(gamePanel);

            frame.addKeyListener(this);
            frame.pack();
            frame.setVisible(true);
        }
    }

    /*
    Metodos para que la interfaz pueda entender los clicks y las teclas apretadas
     */
    @Override
    public void keyPressed(KeyEvent e) {
        gamePanel.handleInput(e.getKeyCode());
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}
}
