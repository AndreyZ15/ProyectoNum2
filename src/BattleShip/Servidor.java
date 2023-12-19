package BattleShip;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
public class Servidor extends JFrame {

    private JTextField enterField = new JTextField();
    private JTextArea displayArea;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private ServerSocket server;
    private Socket connection;
    private int counter = 1;

    public Servidor() {
        super("Server");
        this.enterField.setEditable(false);
        this.enterField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Servidor.this.sendData(e.getActionCommand());
                Servidor.this.enterField.setText("");
            }
        });
        this.add(this.enterField, "North");
        this.displayArea = new JTextArea();
        this.add(new JScrollPane(this.displayArea), "Center");
        this.setSize(300, 150);
        this.setVisible(true);
    }

    public void runServidor() {
        try {
            this.server = new ServerSocket(12345, 100);

            while(true) {
                while(true) {
                    try {
                        this.waitForConnection();
                        this.getStreams();
                        this.processConnection();
                    } catch (EOFException var6) {
                        this.displayMessage("\nServer terminated connection");
                    } finally {
                        this.closeConnection();
                        ++this.counter;
                    }
                }
            }
        } catch (IOException var8) {
            var8.printStackTrace();
        }
    }

    private void waitForConnection() throws IOException {
        this.displayMessage("Waiting for connection\n");
        this.connection = this.server.accept();
        int var10001 = this.counter;
        this.displayMessage("Connection " + var10001 + " received from: " + this.connection.getInetAddress().getHostName());
    }

    private void getStreams() throws IOException {
        this.output = new ObjectOutputStream(this.connection.getOutputStream());
        this.output.flush();
        this.input = new ObjectInputStream(this.connection.getInputStream());
        this.displayMessage("\nGot I/O streams\n");
    }

    private void processConnection() throws IOException {
        String message = "Connection successful";
        this.sendData(message);
        this.setTextFieldEditable(true);

        do {
            try {
                message = (String)this.input.readObject();
                this.displayMessage("\n" + message);
            } catch (ClassNotFoundException var3) {
                this.displayMessage("\nUnknown object type received");
            }
        } while(!message.equals("CLIENT>>> TERMINATE"));

    }

    private void closeConnection() {
        this.displayMessage("\nTerminating connection\n");
        this.setTextFieldEditable(false);

        try {
            this.output.close();
            this.input.close();
            this.connection.close();
        } catch (IOException var2) {
            var2.printStackTrace();
        }

    }

    private void sendData(String message) {
        try {
            this.output.writeObject("SERVER>>> " + message);
            this.output.flush();
            this.displayMessage("\nSERVER>>> " + message);
        } catch (IOException var3) {
            this.displayArea.append("\nError writing object");
        }

    }

    private void displayMessage(final String messageToDisplay) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Servidor.this.displayArea.append(messageToDisplay);
            }
        });
    }

    private void setTextFieldEditable(final boolean editable) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Servidor.this.enterField.setEditable(editable);
            }
        });
    }

    public static void main(String[] args) {
        Servidor application = new Servidor();
        application.setDefaultCloseOperation(3);
        application.runServidor();
    }
}
