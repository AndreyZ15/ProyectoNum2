package BattleShip;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SeleccionPanel extends Rectangulo{

    public static final int CELDA_SIZE = 30;
    public static final int GRID_ANCHO = 10;
    public static final int GRID_LARGO = 10;
    public static final int[] BARCO_SIZES = {5,4,3,3,2};
    private Marcador[][] marcadores = new Marcador[GRID_ANCHO][GRID_LARGO];
    private List<Barco> barcos;
    private Random rand;
    private boolean mostrarBarco;
    private boolean todoslosbarcosDestruidos;

    public SeleccionPanel(int x, int y) {
        super(x, y, CELDA_SIZE*GRID_ANCHO, CELDA_SIZE*GRID_LARGO);
        crearMarcadorPanel();
        barcos = new ArrayList<>();
        rand = new Random();
        mostrarBarco = false;
    }

    public void paint(Graphics g) {
        for(Barco barco : barcos) {
            if(mostrarBarco|| GamePanel.debugModeActive || barco.isDestroyed()) {
                barco.paint(g);
            }
        }
        dibujarMarcadores(g);
        dibujarPanel(g);
    }

    public void setMostrarBarco(boolean mostrarBarco) {
        this.mostrarBarco = mostrarBarco;
    }
    public void reset() {
        for(int x = 0; x < GRID_ANCHO; x++) {
            for(int y = 0; y < GRID_LARGO; y++) {
                marcadores[x][y].reset();
            }
        }
        barcos.clear();
        mostrarBarco = false;
        todoslosbarcosDestruidos = false;
    }
    public boolean markPosition(Posicion posToMark) {
        marcadores[posToMark.x][posToMark.y].mark();

        todoslosbarcosDestruidos = true;
        for(Barco barco : barcos) {
            if(!barco.isDestroyed()) {
                todoslosbarcosDestruidos= false;
                break;
            }
        }
        return marcadores[posToMark.x][posToMark.y].esBarco();
    }

    public boolean estantodoslosbarcosdestruidos() {
        return todoslosbarcosDestruidos;
    }
    public boolean esPosicionMarcada(Posicion posToTest) {
        return marcadores[posToTest.x][posToTest.y].Marca();
    }

    public Marcador getMarcadorenlaPosicion(Posicion posToSelect) {
        return marcadores[posToSelect.x][posToSelect.y];
    }


    public Posicion getPosicionenPanel(int mouseX, int mouseY) {
        if(!esPosicionInside(new Posicion(mouseX,mouseY))) return new Posicion(-1,-1);

        return new Posicion((mouseX - posicion.x)/CELDA_SIZE, (mouseY - posicion.y)/CELDA_SIZE);
    }

    public boolean sepuedePosicionarBarcoen(int gridX, int gridY, int segments, boolean sideways) {
        if(gridX < 0 || gridY < 0) return false;

        if(sideways) { // handle the case when horizontal
            if(gridY > GRID_LARGO || gridX + segments > GRID_ANCHO) return false;
            for(int x = 0; x < segments; x++) {
                if(marcadores[gridX+x][gridY].isBarco()) return false;
            }
        } else { // handle the case when vertical
            if(gridY + segments > GRID_LARGO || gridX > GRID_ANCHO) return false;
            for(int y = 0; y < segments; y++) {
                if(marcadores[gridX][gridY+y].isBarco()) return false;
            }
        }
        return true;
    }

    private void dibujarPanel(Graphics g) {
        g.setColor(Color.BLACK);
        // Draw vertical lines
        int y2 = posicion.y;
        int y1 = posicion.y+altura;
        for(int x = 0; x <= GRID_ANCHO; x++)
            g.drawLine(posicion.x+x * CELDA_SIZE, y1, posicion.x+x * CELDA_SIZE, y2);

        // Draw horizontal lines
        int x2 = posicion.x;
        int x1 = posicion.x+ancho;
        for(int y = 0; y <= GRID_LARGO; y++)
            g.drawLine(x1, posicion.y+y * CELDA_SIZE, x2, posicion.y+y * CELDA_SIZE);
    }
    private void dibujarMarcadores(Graphics g) {
        for(int x = 0; x < GRID_ANCHO; x++) {
            for(int y = 0; y < GRID_LARGO; y++) {
                marcadores[x][y].pintura(g);
            }
        }
    }
    private void crearMarcadorPanel() {
        for(int x = 0; x < GRID_ANCHO; x++) {
            for (int y = 0; y < GRID_LARGO; y++) {
                marcadores[x][y] = new Marcador(posicion.x+x*CELDA_SIZE, posicion.y + y*CELDA_SIZE, CELDA_SIZE, CELDA_SIZE);
            }
        }
    }
    public void populateShips() {
        barcos.clear();
        for(int i = 0; i < BOAT_SIZES.length; i++) {
            boolean sideways = rand.nextBoolean();
            int gridX,gridY;
            do {
                gridX = rand.nextInt(sideways?GRID_ANCHO-BOAT_SIZES[i]:GRID_ANCHO);
                gridY = rand.nextInt(sideways?GRID_LARGO:GRID_LARGO-BOAT_SIZES[i]);
            } while(!sepuedePosicionarBarcoen(gridX,gridY,BOAT_SIZES[i],sideways));
            colocarBarco(gridX, gridY, BOAT_SIZES[i], sideways);
        }
    }

    public void colocarBarco(int gridX, int gridY, int segments, boolean sideways) {
        colocarBarco(new Barco(new Posicion(gridX, gridY),
                new Posicion(posicion.x+gridX*CELDA_SIZE, posicion.y+gridY*CELDA_SIZE),
                segments, sideways), gridX, gridY);
    }

    public void colocarBarco(Barco barco, int gridX, int gridY) {
        barcos.add(barco;
        if(barco.isSideways()) { // If the ship is horizontal
            for(int x = 0; x < barco.getSegments(); x++) {
                marcadores[gridX+x][gridY].setAsShip(barcos.get(barcos.size()-1));
            }
        } else { // If the ship is vertical
            for(int y = 0; y < barco.getSegments(); y++) {
                marcadores[gridX][gridY+y].setAsShip(barcos.get(barcos.size()-1));
            }
        }
    }
}
