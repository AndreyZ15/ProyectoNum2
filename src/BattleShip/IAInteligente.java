package BattleShip;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class IAInteligente extends BattleShipIA{
    private List<Posicion> Golpesbarco;

    private final boolean debugIA = false;

    private boolean preferirmovilinea;

    private boolean maximizaraleatoadya;

    /*
    Es el metodo que se utiliza para inicializar la ia del modo dificil ya que es mas habil que las demas ias
     */
    public IAInteligente(SeleccionPanel jugadorPanel, boolean preferirmovilinea, boolean maximizaraleatoadya){
        super(jugadorPanel);
        Golpesbarco = new ArrayList<>();
        this.preferirmovilinea = preferirmovilinea;
        this.maximizaraleatoadya = maximizaraleatoadya;
        Collections.shuffle(movimientovalido);

    }
    /*
    Overridea el metodo reset para reiniciar el juego
     */

    @Override
    public void reset() {
        super.reset();
        Golpesbarco.clear();
        Collections.shuffle(movimientovalido);
    }
    /*
    Selecciona un movimiento para y trae al metodo movimiento
    inteligente para hacerlo de una mejor manera explicado abajo
     */
    @Override
    public Posicion seleccionarMovimiento() {
        if (debugIA) System.out.println("\nInicio de turno===========");
        Posicion seleccionarMovimiento;
        if (Golpesbarco.size() > 0) {
            if (preferirmovilinea) {
                seleccionarMovimiento = getSmarterAtaque();
            } else {
                seleccionarMovimiento = getAtaqueInteligente();
            }
        } else {
            if (maximizaraleatoadya) {
                seleccionarMovimiento = EncontrarMejorPosicionAbierta();
            } else {
                // Use a random move
                seleccionarMovimiento = movimientovalido.get(0);
            }
        }
        actualizarGolpesBarco(seleccionarMovimiento);
        movimientovalido.remove(seleccionarMovimiento);
        if (debugIA) {
            System.out.println("Selected Move: " + seleccionarMovimiento);
            System.out.println("END TURN===========");
        }
        return seleccionarMovimiento;
    }
    /*
    Selecciona el movimiento de manera inteligente por si consigue chocar algun barco que se
    siga la logica y ataca cerca de este mismo para hundirlo lo mas rapido
     */
    private Posicion getAtaqueInteligente() {
        List<Posicion> MovimientoSugerido = getMovimientoInteligenteAdya();
        Collections.shuffle(MovimientoSugerido);
        return  MovimientoSugerido.get(0);
    }
    /*
    Hace una lista de cual es el movimiento mas optimo para realizar y lo ejecuta
     */
    private Posicion getSmarterAtaque() {
        List<Posicion> MovimientoSugerido = getMovimientoInteligenteAdya();
        for(Posicion posibleMovimientoOptimo : MovimientoSugerido) {
            if(alMenosDosImpactosEnDirección(posibleMovimientoOptimo,Posicion.Izquierda)) return posibleMovimientoOptimo;
            if(alMenosDosImpactosEnDirección(posibleMovimientoOptimo,Posicion.Derecha)) return posibleMovimientoOptimo;
            if(alMenosDosImpactosEnDirección(posibleMovimientoOptimo,Posicion.Abajo)) return posibleMovimientoOptimo;
            if(alMenosDosImpactosEnDirección(posibleMovimientoOptimo,Posicion.Arriba)) return posibleMovimientoOptimo;
        }

        Collections.shuffle(MovimientoSugerido);
        return  MovimientoSugerido.get(0);
    }
    /*
    Coloca el barco en la mejor posicion con respecto a los otros barcos
     */
    private Posicion  EncontrarMejorPosicionAbierta() {
        Posicion position = movimientovalido.get(0);
        int highestNotAttacked = -1;
        for(int i = 0; i < movimientovalido.size(); i++) {
            int testCount = getCantidadNoAtacadaAdyacente(movimientovalido.get(i));
            if(testCount == 4) { // Maximum found, just return immediately
                return movimientovalido.get(i);
            } else if(testCount > highestNotAttacked) {
                highestNotAttacked = testCount;
                position = movimientovalido.get(i);
            }
        }
        return position;
    }
    /*
    Se trae la cantidad de tiros cercas que no hayan impactado para predecir disparos
     */
    private int getCantidadNoAtacadaAdyacente(Posicion posicion) {
        List<Posicion> celdasadyacentes = getceldasadyacentes(posicion);
        int cantidadNoatacada= 0;
        for(Posicion celdaadyacente : celdasadyacentes) {
            if(!jugadorPanel.getMarcadorenlaPosicion(celdaadyacente).Marca()) {
                cantidadNoatacada++;
            }
        }
        return cantidadNoatacada;
    }
    /*
    Piensa de una mejor manera pare donde puede estar el barco y ya este tiene dos impactos en una dirección
     */
    private boolean alMenosDosImpactosEnDirección(Posicion empezar, Posicion direccion) {
        Posicion probarPosicon = new Posicion(empezar);
        probarPosicon.add(direccion);
        if(!Golpesbarco.contains(probarPosicon)) return false;
        probarPosicon.add(direccion);
        if(!Golpesbarco.contains(probarPosicon)) return false;
        if(debugIA) System.out.println("Smarter match found AT: " + empezar + " TO: " + probarPosicon);
        return true;
    }
    /*
    Piensa cuál es el mejor lugar para disparar pensando en los demas tiros que ya realizo
     */
    private List<Posicion> getMovimientoInteligenteAdya() {
        List<Posicion> resultado = new ArrayList<>();
        for(Posicion PosGolpeBarco : Golpesbarco) {
            List<Posicion> adjacentPositions = getceldasadyacentes(PosGolpeBarco);
            for(Posicion adjacentPosition : adjacentPositions) {
                if(!resultado.contains(adjacentPosition) && movimientovalido.contains(adjacentPosition)) {
                    resultado.add(adjacentPosition);
                }
            }
        }
        if(debugIA) {
            printPosicionList("Ship Hits: ", Golpesbarco);
            printPosicionList("Adjacent Smart Moves: ", resultado);
        }
        return resultado;
    }
    /*
    Imprime la posición en la que está disparando
     */
    private void printPosicionList(String messagePrefix, List<Posicion> data) {
        String resultado = "[";
        for(int i = 0; i < data.size(); i++) {
            resultado += data.get(i);
            if(i != data.size()-1) {
                resultado += ", ";
            }
        }
        resultado += "]";
        System.out.println(messagePrefix + " " + resultado);
    }
    /*
    Revisa las celdas adyacentes para utilizarlo en métodos de arriba
     */
    private List<Posicion> getceldasadyacentes(Posicion posicion) {
        List<Posicion> resultado = new ArrayList<>();
        if(posicion.x != 0) {
            Posicion izquierda = new Posicion(posicion);
            izquierda.add(Posicion.Izquierda);
            resultado.add(izquierda);
        }
        if(posicion.x != SeleccionPanel.GRID_ANCHO-1) {
            Posicion derecha = new Posicion(posicion);
            derecha.add(Posicion.Derecha);
            resultado.add(derecha);
        }
        if(posicion.y != 0) {
            Posicion arriba = new Posicion(posicion);
            arriba.add(Posicion.Arriba);
            resultado.add(arriba);
        }
        if(posicion.y != SeleccionPanel.GRID_LARGO-1) {
            Posicion abajo = new Posicion(posicion);
            abajo.add(Posicion.Abajo);
            resultado.add(abajo);
        }
        return resultado;
    }
    /*
    Actualiza a donde ha realizado golpes de barcos para utilizar esa logica en metodos de arriba
     */
    private void actualizarGolpesBarco(Posicion probarPosicion) {
        Marcador marcador = jugadorPanel.getMarcadorenlaPosicion(probarPosicion);
        if(marcador.esBarco()) {
            Golpesbarco.add(probarPosicion);
            // Check to find if this was the last place to hit on the targeted ship
            List<Posicion> todaslasposicionesdelultimobarco = marcador.getBarcoCerca().getOccupiedCoordinates();
            if(debugIA) printPosicionList("Last Ship", todaslasposicionesdelultimobarco);
            boolean hitAllOfShip = containstodaslasposiciones(todaslasposicionesdelultimobarco, Golpesbarco);
            // If it was remove the ship data from history to now ignore it
            if(hitAllOfShip) {
                for(Posicion shipPosition :todaslasposicionesdelultimobarco) {
                    for(int i = 0; i < Golpesbarco.size(); i++) {
                        if(Golpesbarco.get(i).equals(shipPosition)) {
                            Golpesbarco.remove(i);
                            if(debugIA) System.out.println("Removed " + shipPosition);
                            break;
                        }
                    }
                }
            }
        }
    }
    /*
    Hagarra todas las posiciones en una lista para dividirlo y utilizarlo de manera más rápida o mejor
     */
    private boolean containstodaslasposiciones(List<Posicion> posicionesToBuscar, List<Posicion> listToBuscarIn) {
        for(Posicion buscarPosicion : posicionesToBuscar) {
            boolean encontrar = false;
            for(Posicion buscarInPosicion : listToBuscarIn) {
                if(buscarInPosicion.equals(buscarPosicion)) {
                    encontrar = true;
                    break;
                }
            }
            if(!encontrar) return false;
        }
        return true;
    }
}

