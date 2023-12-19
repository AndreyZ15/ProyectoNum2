package BattleShip;
import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Juego implements KeyListener {
    private static final int PUERTO = 12345; // Puerto para la comunicación

    private GamePanel gamePanel;

    private Socket socket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;

    public Juego() {
        // Seleccionar la dificultad
        String[] options = new String[] {"Facil", "Medio", "Dificil"};
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

        if (setupMultijugador()) {
            // Iniciar el hilo para manejar la comunicación del jugador
            Thread thread = new Thread(this::manejarComunicacion);
            thread.start();
        }
    }
    private boolean setupMultijugador() {
        String[] options = new String[]{"Crear partida", "Unirse a partida"};
        int choice = JOptionPane.showOptionDialog(null, "Seleccione una opción para el multijugador",
                "Multijugador", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, options, options[0]);

        if (choice == 0) { // Crear partida
            try {
                ServerSocket serverSocket = new ServerSocket(PUERTO);
                System.out.println("Esperando a que se conecte un jugador...");
                socket = serverSocket.accept();
                System.out.println("Jugador conectado");
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        } else if (choice == 1) { // Unirse a partida
            String ip = JOptionPane.showInputDialog("Introduzca la dirección IP del servidor:");
            try {
                socket = new Socket(ip, PUERTO);
                System.out.println("Conectado al servidor");
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return false;
        }

        try {
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private void manejarComunicacion() {
        try {
            while (true) {
                // Manejar la comunicación con el otro jugador aquí
                // Puedes usar outputStream.writeObject() y inputStream.readObject() para enviar y recibir datos
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        gamePanel.handleInput(e.getKeyCode());
        if (outputStream != null) {
            try {
                outputStream.writeInt(e.getKeyCode());
                outputStream.flush();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}
}
