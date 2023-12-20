package BattleShip;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SeleccionPanel extends Rectangulo{

    public static final int CELDA_SIZE = 40;
    public static final int GRID_ANCHO = 10;
    public static final int GRID_LARGO = 10;
    public static final int[] BARCO_SIZES = {5,4,3,3,2};
    private Marcador[][] marcadores = new Marcador[GRID_ANCHO][GRID_LARGO];
    private List<Barco> barcos;
    private Random rand;
    private boolean mostrarBarco;
    private boolean todoslosbarcosDestruidos;

    /*
    Public SeleccionPanel
    Configura el panel para crear una configuracion default de
    los marcadores
     */
    public SeleccionPanel(int x, int y) {
        super(x, y, CELDA_SIZE*GRID_ANCHO, CELDA_SIZE*GRID_LARGO);
        crearMarcadorPanel();
        barcos = new ArrayList<>();
        rand = new Random();
        mostrarBarco = false;
    }

    /*
    Dibuja los barcos y muestra si cada barco está marcado como destruido
    dibuja los marcadores que deben mostrar para los ataques realizados hasta
    el momento
     */
    public void paint(Graphics g) {
        for(Barco barco : barcos) {
            if(mostrarBarco|| GamePanel.debugModeActive || barco.isDestruido()) {
                barco.paint(g);
            }
        }
        dibujarMarcadores(g);
        dibujarPanel(g);
    }

    /*
    Modifica el estado de la cuadrícula para mostrar todas todos
    los barcos
     */
    public void setMostrarBarco(boolean mostrarBarco) {
        this.mostrarBarco = mostrarBarco;
    }

    /*
    Restablece la SeleccionPanel diciendole a todos los marcadores
    que se restablezcan, removiendo todos los barcos del panel y sin,
    mostrar ningun barco.
     */
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

    /*
    Marca la posicion especifica y verifica todos los barcos,
    para determinar si han sido destruidos
     */
    public boolean markPosition(Posicion posToMark) {
        marcadores[posToMark.x][posToMark.y].Marcado();

        todoslosbarcosDestruidos = true;
        for(Barco barco : barcos) {
            if(!barco.isDestruido()) {
                todoslosbarcosDestruidos= false;
                break;
            }
        }
        return marcadores[posToMark.x][posToMark.y].esBarco();
    }

    /*
    Verifica si todos los barcos estan destruidos
     */
    public boolean estantodoslosbarcosdestruidos() {
        return todoslosbarcosDestruidos;
    }

    /*
    Verifica si la posicion está marcada
     */
    public boolean esPosicionMarcada(Posicion posToTest) {
        return marcadores[posToTest.x][posToTest.y].Marca();
    }

    /*
    Obtiene el marcador específico en la posicion especificada
     */
    public Marcador getMarcadorenlaPosicion(Posicion posToSelect) {
        return marcadores[posToSelect.x][posToSelect.y];
    }



    /*
     Traduce la posicion del mouse a la de la cuadrícula si es posible
     */
    public Posicion getPosicionenPanel(int mouseX, int mouseY) {
        if(!esPosicionInside(new Posicion(mouseX,mouseY))) return new Posicion(-1,-1);

        return new Posicion((mouseX - posicion.x)/CELDA_SIZE, (mouseY - posicion.y)/CELDA_SIZE);
    }

    /*
      Verifica si se pueden posicionar el barco en la posicion,
      seleccionada ya sea que no se salga del panel o que colisione,
      con otro barco
     */
    public boolean sepuedePosicionarBarcoen(int gridX, int gridY, int segments, boolean sideways) {
        if(gridX < 0 || gridY < 0) return false;

        if(sideways) {
            if(gridY > GRID_LARGO || gridX + segments > GRID_ANCHO) return false;
            for(int x = 0; x < segments; x++) {
                if(marcadores[gridX+x][gridY].esBarco()) return false;
            }
        } else {
            if(gridY + segments > GRID_LARGO || gridX > GRID_ANCHO) return false;
            for(int y = 0; y < segments; y++) {
                if(marcadores[gridX][gridY+y].esBarco()) return false;
            }
        }
        return true;
    }


    /*
    Dibuja cada cuadricula hecha de un solo pixel con líneas negras
     */
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

    /*
    Dibuja todos los marcadores
     */
    private void dibujarMarcadores(Graphics g) {
        for(int x = 0; x < GRID_ANCHO; x++) {
            for(int y = 0; y < GRID_LARGO; y++) {
                marcadores[x][y].paint(g);
            }
        }
    }

    /*
    Crea todos los objetos del marcador estableciendo sus posiciones
    en la cuadrícula para inicializarlos
     */
    private void crearMarcadorPanel() {
        for(int x = 0; x < GRID_ANCHO; x++) {
            for (int y = 0; y < GRID_LARGO; y++) {
                marcadores[x][y] = new Marcador(posicion.x+x*CELDA_SIZE, posicion.y + y*CELDA_SIZE, CELDA_SIZE, CELDA_SIZE);
            }
        }
    }

    /*
    Limpia todos los barcos actuales y coloca aleatoriamente
    todos los barcos, los barcos no se colocarán uno encima del otro
     */
    public void populateShips() {
        barcos.clear();
        for(int i = 0; i < BARCO_SIZES.length; i++) {
            boolean sideways = rand.nextBoolean();
            int gridX,gridY;
            do {
                gridX = rand.nextInt(sideways?GRID_ANCHO-BARCO_SIZES[i]:GRID_ANCHO);
                gridY = rand.nextInt(sideways?GRID_LARGO:GRID_LARGO-BARCO_SIZES[i]);
            } while(!sepuedePosicionarBarcoen(gridX,gridY,BARCO_SIZES[i],sideways));
            colocarBarco(gridX, gridY, BARCO_SIZES[i], sideways);
        }
    }


    /*
    Coloca el barco en las cuadrículas con las propiedades indicadas
     */
    public void colocarBarco(int gridX, int gridY, int segments, boolean sideways) {
        colocarBarco(new Barco(new Posicion(gridX, gridY),
                new Posicion(posicion.x+gridX*CELDA_SIZE, posicion.y+gridY*CELDA_SIZE),
                segments, sideways), gridX, gridY);
    }

    /*
    Coloca los barcos en las cuadrículas con las propiedades indicadas
     */
    public void colocarBarco(Barco barco, int gridX, int gridY) {
        barcos.add(barco);
        if(barco.isTorcido()) { // If the ship is horizontal
            for(int x = 0; x < barco.getSegmentos(); x++) {
                marcadores[gridX+x][gridY].setBarco(barcos.get(barcos.size()-1));
            }
        } else { // If the ship is vertical
            for(int y = 0; y < barco.getSegmentos(); y++) {
                marcadores[gridX][gridY+y].setBarco(barcos.get(barcos.size()-1));
            }
        }
    }
}
