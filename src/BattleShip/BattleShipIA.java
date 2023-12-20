package BattleShip;

import java.util.ArrayList;
import java.util.List;

public class BattleShipIA {

    protected SeleccionPanel jugadorPanel;
    protected List<Posicion> movimientovalido;

    /*
    Es lafuncion para que la computadora pueda jugar contra la persona
    */
    public BattleShipIA(SeleccionPanel jugadorPanel){
        this.jugadorPanel = jugadorPanel;
        crearListaMovimientoValido();

    }
    /*
    Selecciona el movimiento de la computadora a leatoriamente empezando desde cero
     */
    public Posicion seleccionarMovimiento() {
        return Posicion.Zero;
    }
    /*
    Es la opcion para recetear una vez ya terminada la partida por si quiere volver a jugar
    */
    public void reset(){
        crearListaMovimientoValido();
    }
    /*
    Genenra una lista de movimientos validos para que aletoriamente la computadora eliga una casilla
     */
    private void crearListaMovimientoValido(){
        movimientovalido = new ArrayList<>();
        for(int x = 0; x < SeleccionPanel.GRID_ANCHO; x++) {
            for(int y = 0; y < SeleccionPanel.GRID_LARGO; y++) {
                movimientovalido.add(new Posicion(x,y));
            }
        }
    }

}
