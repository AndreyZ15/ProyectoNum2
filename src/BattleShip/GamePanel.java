package BattleShip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;


public class GamePanel extends JPanel implements MouseListener, MouseMotionListener {

    public enum GameState { PlacingShips, FiringShots, GameOver }


    private PanelControl panelControl;

    private SeleccionPanel computadora;

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
        setBackground(new Color(42, 136, 163));
        setPreferredSize(new Dimension(computadora.getAncho(), jugador.getPosicion().y + jugador.getAltura()));
        addMouseListener(this);
        addMouseMotionListener(this);
        if(aiChoice == 0) iaControlador = new IARadom(jugador);
        else iaControlador = new IAInteligente(jugador,aiChoice == 2,aiChoice == 2);
        panelControl = new PanelControl(new Posicion(0,computadora.getAltura()+1),computadora.getAncho(),49);
        restart();
    }


    public void paint(Graphics g) {
        super.paint(g);
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
        if(placingShipIndex < SelectionGrid.BOAT_SIZES.length) {
            placingShip = new Ship(new Position(targetPosition.x, targetPosition.y),
                    new Position(player.getPosition().x + targetPosition.x * SelectionGrid.CELL_SIZE,
                            player.getPosition().y + targetPosition.y * SelectionGrid.CELL_SIZE),
                    SelectionGrid.BOAT_SIZES[placingShipIndex], true);
            updateShipPlacement(tempPlacingPosition);
        } else {
            gameState = GameState.FiringShots;
            statusPanel.setTopLine("Attack the Computer!");
            statusPanel.setBottomLine("Destroy all Ships to win!");
        }
    }


    private void tryFireAtComputer(Position mousePosition) {
        Position targetPosition = computer.getPositionInGrid(mousePosition.x,mousePosition.y);
        // Ignore if position was already clicked
        if(!computer.isPositionMarked(targetPosition)) {
            doPlayerTurn(targetPosition);
            // Only do the AI turn if the game didn't end from the player's turn.
            if(!computer.areAllShipsDestroyed()) {
                doAITurn();
            }
        }
    }


    private void doPlayerTurn(Position targetPosition) {
        boolean hit = computer.markPosition(targetPosition);
        String hitMiss = hit ? "Hit" : "Missed";
        String destroyed = "";
        if(hit && computer.getMarkerAtPosition(targetPosition).getAssociatedShip().isDestroyed()) {
            destroyed = "(Destroyed)";
        }
        statusPanel.setTopLine("Player " + hitMiss + " " + targetPosition + destroyed);
        if(computer.areAllShipsDestroyed()) {
            // Player wins!
            gameState = GameState.GameOver;
            statusPanel.showGameOver(true);
        }
    }

    private void doAITurn() {
        Position aiMove = aiController.seleccionarMovimiento();
        boolean hit = player.markPosition(aiMove);
        String hitMiss = hit ? "Hit" : "Missed";
        String destroyed = "";
        if(hit && player.getMarkerAtPosition(aiMove).getAssociatedShip().isDestroyed()) {
            destroyed = "(Destroyed)";
        }
        statusPanel.setBottomLine("Computer " + hitMiss + " " + aiMove + destroyed);
        if(player.areAllShipsDestroyed()) {
            // Computer wins!
            gameState = GameState.GameOver;
            statusPanel.showGameOver(false);
        }
    }


    private void tryMovePlacingShip(Position mousePosition) {
        if(player.isPositionInside(mousePosition)) {
            Position targetPos = player.getPositionInGrid(mousePosition.x, mousePosition.y);
            updateShipPlacement(targetPos);
        }
    }


    private void updateShipPlacement(Posicion targetPos) {
        // Constrain to fit inside the grid
        if(placingShip.isSideways()) {
            targetPos.x = Math.min(targetPos.x, SelectionGrid.GRID_WIDTH - SelectionGrid.BOAT_SIZES[placingShipIndex]);
        } else {
            targetPos.y = Math.min(targetPos.y, SelectionGrid.GRID_HEIGHT - SelectionGrid.BOAT_SIZES[placingShipIndex]);
        }
        // Update drawing position to use the new target position
        placingShip.setDrawPosition(new Position(targetPos),
                new Position(player.getPosition().x + targetPos.x * SelectionGrid.CELL_SIZE,
                        player.getPosition().y + targetPos.y * SelectionGrid.CELL_SIZE));
        // Store the grid position for other testing cases
        tempPlacingPosition = targetPos;
        // Change the colour of the ship based on whether it could be placed at the current location.
        if(player.canPlaceShipAt(tempPlacingPosition.x, tempPlacingPosition.y,
                SelectionGrid.BOAT_SIZES[placingShipIndex],placingShip.isSideways())) {
            placingShip.setShipPlacementColour(Ship.ShipPlacementColour.Valid);
        } else {
            placingShip.setShipPlacementColour(Ship.ShipPlacementColour.Invalid);
        }
    }


    @Override
    public void mouseReleased(MouseEvent e) {
        Position mousePosition = new Position(e.getX(), e.getY());
        if(gameState == GameState.PlacingShips && player.isPositionInside(mousePosition)) {
            tryPlaceShip(mousePosition);
        } else if(gameState == GameState.FiringShots && computer.isPositionInside(mousePosition)) {
            tryFireAtComputer(mousePosition);
        }
        repaint();
    }


    @Override
    public void mouseMoved(MouseEvent e) {
        if(gameState != GameState.PlacingShips) return;
        tryMovePlacingShip(new Position(e.getX(), e.getY()));
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
