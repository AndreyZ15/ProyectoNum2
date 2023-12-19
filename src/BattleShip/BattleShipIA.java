package BattleShip;

import java.util.ArrayList;
import java.util.List;

public class BattleShipIA {

    protected SeleccionPanel jugadorPanel;
    protected List<Posicion> movimientovalido;

    public BattleShipIA(SeleccionPanel jugadorPanel){
        this.jugadorPanel = jugadorPanel;
        crearListaMovimientoValido();

    }

    public Posicion seleccionarMovimiento(){return Posicion.Zero;}

    public void reset(){ crearListaMovimientoValido();}

    private void crearListaMovimientoValido(){
        movimientovalido = new ArrayList<>();
        for(int x = 0; x < SeleccionPanel.GRID_ANCHO; x++) {
            for(int y = 0; y < SeleccionPanel.GRID_LARGO; y++) {
                movimientovalido.add(new Posicion(x,y));
            }
        }
    }

}
