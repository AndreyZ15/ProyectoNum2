package BattleShip;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.swing.JOptionPane;
import java.net.Socket;



public class Cliente {
    private static final int PUERTO = 12345; // Mismo puerto que en el servidor

    private Socket socket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;

    public Cliente() {
        try {
            String ip = JOptionPane.showInputDialog("Introduzca la dirección IP del servidor:");
            socket = new Socket(ip, PUERTO);
            System.out.println("Conectado al servidor");

            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void enviarDatos(int keyCode) {
        try {
            outputStream.writeInt(keyCode);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int recibirDatos() {
        try {
            return inputStream.readInt();
        } catch (IOException e) {
            e.printStackTrace();
            return -1; // Otra manera de manejar errores, puedes ajustar según tus necesidades.
        }
    }

    public void cerrarConexion() {
        try {
            if (socket != null) socket.close();
            if (outputStream != null) outputStream.close();
            if (inputStream != null) inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}