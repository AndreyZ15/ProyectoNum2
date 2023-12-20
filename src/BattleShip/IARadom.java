package BattleShip;
import java.util.Collections;
public class IARadom extends BattleShipIA {
    /*
    Metodo para llamar a los disparos random de la computadora
     */
    public IARadom(SeleccionPanel jugadorPanel) {
        super(jugadorPanel);
        Collections.shuffle(movimientovalido);
    }
    /*
    Overrideo del metodo reset para que se pueda reiniciar el nivel
     */
    @Override
    public void reset() {
        super.reset();
        Collections.shuffle(movimientovalido);
    }
    /*
    Overrideo del metodo seleccionarMovimiento para hacerlo de una forma mas random o segir una mejor logica
     */
    @Override
    public Posicion seleccionarMovimiento() {
        Posicion nextMove = movimientovalido.get(0);
        movimientovalido.remove(0);
        return nextMove;
    }


}