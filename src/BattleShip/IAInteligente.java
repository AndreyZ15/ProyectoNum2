package BattleShip;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class IAInteligente extends BattleShipIA{


    private List<Posicion> Golpesbarco;

    private final boolean debugIA = false;

    private boolean preferirmovilinea;

    private boolean maximizaraleatoadya;


    public IAInteligente(SeleccionPanel jugadorPanel, boolean preferirmovilinea, boolean maximizaraleatoadya){
        super(jugadorPanel);
        Golpesbarco = new ArrayList<>();
        this.preferirmovilinea = preferirmovilinea;
        this.maximizaraleatoadya = maximizaraleatoadya;
        Collections.shuffle(movimientovalido);

    }

    @Override
    public void reset() {
        super.reset();
        Golpesbarco.clear();
        Collections.shuffle(movimientovalido);
    }
    @Override
    public Posicion selecionarMovimiento() {
        if (debugIA) System.out.println("\nInicio de turno===========");
        Posicion seleccionarMovimiento;
        // If a ship has been hit, but not destroyed
        if (Golpesbarco.size() > 0) {
            if (preferirmovilinea) {
                seleccionarMovimiento = getAtaqueInteligente();
            } else {
                seleccionarMovimiento = getAtaqueInteligente;
            }
        } else {
            if (maximizaraleatoadya) {
                seleccionarMovimiento = EncontrarMejorPosicionAbierta();
            } else {
                // Use a random move
                seleccionarMovimiento = movimientovalido.get(0);
            }
        }
        ActualizarGolpesBarco(seleccionarMovimiento);
        movimientovalido.remove(seleccionarMovimiento);
        if (debugIA) {
            System.out.println("Selected Move: " + seleccionarMovimiento);
            System.out.println("END TURN===========");
        }
        return seleccionarMovimiento;
    }

        private Posicion getAtaqueInteligente() {
            List<Posicion> MovimientoSugerido = getMovimientoInteligenteAdya();
            Collections.shuffle(MovimientoSugerido);
            return  MovimientoSugerido.get(0);
        }

        private Posicion getSmarterAttack() {
            List<Posicion> MovimientoSugerido = getMovimientoInteligenteAdya();
            for(Posicion posibleMovimientoOptimo : MovimientoSugerido) {
                if(alMenosDosImpactosEnDirección(posibleMovimientoOptimo,Posicion.Izquierda)) return posibleMovimientoOptimo;
                if(alMenosDosImpactosEnDirección(posibleMovimientoOptimo,Posicion.Derecha)) return posibleMovimientoOptimo;
                if(alMenosDosImpactosEnDirección(posibleMovimientoOptimo,Posicion.Abajo)) return posibleMovimientoOptimo;
                if(alMenosDosImpactosEnDirección(posibleMovimientoOptimo,Posicion.Arriba)) return posibleMovimientoOptimo;
            }
            // No optimal choice found, just randomise the move.
            Collections.shuffle(MovimientoSugerido);
            return  MovimientoSugerido.get(0);
        }

        private Posicion  EncontrarMejorPosicionAbierta() {
            Posicion position = movimientovalido.get(0);;
            int highestNotAttacked = -1;
            for(int i = 0; i < movimientovalido.size(); i++) {
                int testCount = getCantidadDeAdyacentesNoAtacados(movimientovalido.get(i));
                if(testCount == 4) { // Maximum found, just return immediately
                    return movimientovalido.get(i);
                } else if(testCount > highestNotAttacked) {
                    highestNotAttacked = testCount;
                    position = movimientovalido.get(i);
                }
            }
            return position;
        }

    private int getAdjacentNotAttackedCount(Posicioj position) {
        List<Posicion> adjacentCells = getAdjacentCells(position);
        int notAttackedCount = 0;
        for(Position adjacentCell : adjacentCells) {
            if(!playerGrid.getMarkerAtPosition(adjacentCell).isMarked()) {
                notAttackedCount++;
            }
        }
        return notAttackedCount;
    }













}

