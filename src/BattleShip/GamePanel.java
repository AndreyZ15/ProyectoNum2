package BattleShip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;


public class GamePanel extends JPanel implements MouseListener, MouseMotionListener {

    public enum GameState { PlacingShips, FiringShots, GameOver }

    private Image fondo;
    private PanelControl panelControl;

    private SeleccionPanel computadora;
    private Image imagenBarco1;
    private Image imagenBarco2;
    private Image imagenBarco3;
    private Image imagenBarco4;
    private Image imagenBarco5;

    private SeleccionPanel jugador;

    private BattleShipIA iaControlador;


    private Barco posicionamientoBarco;

    private Posicion tempPosicionPosicionamiento;

    private int posicionBarcoIndex;

    private GameState gameState;

    public static boolean debugModeActive;


    public GamePanel(int aiChoice) {
        computadora = new SeleccionPanel(0,0);
        jugador = new SeleccionPanel(0,computadora.getAltura()+50);
        setBackground(new Color(17, 0, 105));
        setPreferredSize(new Dimension(computadora.getAncho(), jugador.getPosicion().y + jugador.getAltura()));
        addMouseListener(this);
        addMouseMotionListener(this);
        if(aiChoice == 0) iaControlador = new IARadom(jugador);
        else iaControlador = new IAInteligente(jugador,aiChoice == 2,aiChoice == 2);
        panelControl = new PanelControl(new Posicion(0,computadora.getAltura()+1),computadora.getAncho(),49);
        restart();

        try {
            fondo = new ImageIcon(getClass().getResource("mar3.jpg")).getImage();
            System.out.println("Imagen cargada con éxito.");
        } catch (Exception e) {
            System.err.println("Error al cargar la imagen: " + e.getMessage());
        }

    }


    public void paint(Graphics g ) {
        super.paint(g);
        g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
        computadora.paint(g);
        jugador.paint(g);
        if(gameState == GameState.PlacingShips) {
            posicionamientoBarco.paint(g);
        }
        panelControl.paint(g);
    }


    public void handleInput(int keyCode) {
        if(keyCode == KeyEvent.VK_ESCAPE) {
            System.exit(1);
        } else if(keyCode == KeyEvent.VK_R) {
            restart();
        } else if(gameState == GameState.PlacingShips && keyCode == KeyEvent.VK_Z) {
            posicionamientoBarco.cambiarTorcido();
            updateShipPlacement(tempPosicionPosicionamiento);
        } else if(keyCode == KeyEvent.VK_D) {
            debugModeActive = !debugModeActive;
        }
        repaint();
    }


    public void restart() {
        computadora.reset();
        jugador.reset();
        // Player can see their own ships by default
        jugador.setMostrarBarco(true);
        iaControlador.reset();
        tempPosicionPosicionamiento = new Posicion(0,0);
        posicionamientoBarco= new Barco(new Posicion(0,0),
                new Posicion(jugador.getPosicion().x,jugador.getPosicion().y),
                SeleccionPanel.BARCO_SIZES[0], true);
        posicionBarcoIndex = 0;
        updateShipPlacement(tempPosicionPosicionamiento);
        computadora.populateShips();
        debugModeActive = false;
        panelControl.reset();
        gameState = GameState.PlacingShips;
    }


    private void tryPlaceShip(Posicion mousePosition) {
        Posicion targetPosition = jugador.getPosicionenPanel(mousePosition.x, mousePosition.y);
        updateShipPlacement(targetPosition);
        if(jugador.sepuedePosicionarBarcoen(targetPosition.x, targetPosition.y,
                SeleccionPanel.BARCO_SIZES[posicionBarcoIndex],posicionamientoBarco.isTorcido())) {
            placeShip(targetPosition);
        }
    }


    private void placeShip(Posicion targetPosition) {
        posicionamientoBarco.setColorPosicionBarco(Barco.ColorPosicionBarco.Placed);
        jugador.colocarBarco(posicionamientoBarco,tempPosicionPosicionamiento.x,tempPosicionPosicionamiento.y);
        posicionBarcoIndex++;
        // If there are still ships to place
        if(posicionBarcoIndex < SeleccionPanel.BARCO_SIZES.length) {
            posicionamientoBarco = new Barco(new Posicion(targetPosition.x, targetPosition.y),
                    new Posicion(jugador.getPosicion().x + targetPosition.x * SeleccionPanel.CELDA_SIZE,
                            jugador.getPosicion().y + targetPosition.y * SeleccionPanel.CELDA_SIZE),
                    SeleccionPanel.BARCO_SIZES[posicionBarcoIndex], true);
            updateShipPlacement(tempPosicionPosicionamiento);
        } else {
            gameState = GameState.FiringShots;
            panelControl.setLineasuperior("Ataque la computadora!");
            panelControl.setLineainferior("Destruya todos los barcos para ganar!");
        }
    }


    private void tryFireAtComputer(Posicion mousePosition) {
        Posicion targetPosition = computadora.getPosicionenPanel(mousePosition.x,mousePosition.y);
        // Ignore if position was already clicked
        if(!computadora.esPosicionMarcada(targetPosition)) {
            doPlayerTurn(targetPosition);
            // Only do the AI turn if the game didn't end from the player's turn.
            if(!computadora.estantodoslosbarcosdestruidos()) {
                doAITurn();
            }
        }
    }


    private void doPlayerTurn(Posicion targetPosition) {
        boolean hit = computadora.markPosition(targetPosition);
        String hitMiss = hit ? "Chocado" : "Fallado";
        String destroyed = "";
        if(hit && computadora.getMarcadorenlaPosicion(targetPosition).getBarcoCerca().isDestruido()) {
            destroyed = "(Destruido)";
        }
        panelControl.setLineasuperior("Jugador " + hitMiss + " " + targetPosition + destroyed);
        if(computadora.estantodoslosbarcosdestruidos()) {
            // Player wins!
            gameState = GameState.GameOver;
            panelControl.JuegoTerminado(true);
        }
    }

    private void doAITurn() {
        Posicion aiMove = iaControlador.seleccionarMovimiento();
        boolean hit = jugador.markPosition(aiMove);
        String hitMiss = hit ? "Chocado" : "Fallado";
        String destroyed = "";
        if(hit && jugador.getMarcadorenlaPosicion(aiMove).getBarcoCerca().isDestruido()) {
            destroyed = "(Destruido)";
        }
        panelControl.setLineainferior("Computadora " + hitMiss + " " + aiMove + destroyed);
        if(jugador.estantodoslosbarcosdestruidos()) {
            // Computer wins!
            gameState = GameState.GameOver;
            panelControl.JuegoTerminado(false);
        }
    }


    private void tryMovePlacingShip(Posicion mousePosition) {
        if(jugador.esPosicionInside(mousePosition)) {
            Posicion targetPos = jugador.getPosicionenPanel(mousePosition.x, mousePosition.y);
            updateShipPlacement(targetPos);
        }
    }


    private void updateShipPlacement(Posicion targetPos) {
        // Constrain to fit inside the grid
        if(posicionamientoBarco.isTorcido()) {
            targetPos.x = Math.min(targetPos.x, SeleccionPanel.GRID_ANCHO - SeleccionPanel.BARCO_SIZES[posicionBarcoIndex]);
        } else {
            targetPos.y = Math.min(targetPos.y, SeleccionPanel.GRID_LARGO - SeleccionPanel.BARCO_SIZES[posicionBarcoIndex]);
        }
        // Update drawing position to use the new target position
        posicionamientoBarco.setCrearPosicion(new Posicion(targetPos),
                new Posicion(jugador.getPosicion().x + targetPos.x * SeleccionPanel.CELDA_SIZE,
                        jugador.getPosicion().y + targetPos.y * SeleccionPanel.CELDA_SIZE));
        // Store the grid position for other testing cases
        tempPosicionPosicionamiento = targetPos;
        // Change the colour of the ship based on whether it could be placed at the current location.
        if(jugador.sepuedePosicionarBarcoen(tempPosicionPosicionamiento.x, tempPosicionPosicionamiento.y,
                SeleccionPanel.BARCO_SIZES[posicionBarcoIndex],posicionamientoBarco.isTorcido())) {
            posicionamientoBarco.setColorPosicionBarco(Barco.ColorPosicionBarco.Valid);
        } else {
            posicionamientoBarco.setColorPosicionBarco(Barco.ColorPosicionBarco.Invalid);
        }
    }


    @Override
    public void mouseReleased(MouseEvent e) {
        Posicion mousePosition = new Posicion(e.getX(), e.getY());
        if(gameState == GameState.PlacingShips && jugador.esPosicionInside(mousePosition)) {
            tryPlaceShip(mousePosition);
        } else if(gameState == GameState.FiringShots && computadora.esPosicionInside(mousePosition)) {
            tryFireAtComputer(mousePosition);
        }
        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if(gameState != GameState.PlacingShips) return;
        tryMovePlacingShip(new Posicion(e.getX(), e.getY()));
        repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void mouseDragged(MouseEvent e) {}
}
