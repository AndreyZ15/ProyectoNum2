package BattleShip;
import java.util.Collections;
public class IARadom extends BattleShipIA {
    public IARadom(SeleccionPanel jugadorPanel) {
        super(jugadorPanel);
        Collections.shuffle(movimientovalido);
    }


    @Override
    public void reset() {
        super.reset();
        Collections.shuffle(movimientovalido);
    }
    @Override
    public Posicion seleccionarMovimiento() {
        Posicion nextMove = movimientovalido.get(0);
        movimientovalido.remove(0);
        return nextMove;
    }


}