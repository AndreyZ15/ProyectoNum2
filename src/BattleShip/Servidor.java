package BattleShip;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {
    private ServerSocket serverSocket;

    public Servidor(int puerto) throws IOException {
        serverSocket = new ServerSocket(puerto);
    }

    public Socket esperarConexion() throws IOException {
        System.out.println("Esperando a que se conecte un jugador...");
        return serverSocket.accept();
    }

    public void cerrar() throws IOException {
        serverSocket.close();
    }
}
