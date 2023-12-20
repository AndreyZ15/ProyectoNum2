package BattleShip;


public class Posicion {

    public static final Posicion Abajo = new Posicion(0,1);

    public static final Posicion Arriba = new Posicion(0,-1);

    public static final Posicion Izquierda = new Posicion (-1,0);

    public static final Posicion Derecha = new Posicion(1,0);

    public static final Posicion Zero = new Posicion(0,0);
    public int x;

    public int y;

    /*
    Establece el valor de la Posicion
     */
    public Posicion(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /*
    Copia el constructor para crear una nueva posicion
     */
    public Posicion(Posicion posicionToCopy) {
        this.x = posicionToCopy.x;
        this.y = posicionToCopy.y;
    }

    /*
    Actualiza la posicion añadiendo el valor de la otra posicion
     */
    public void add(Posicion otherPosicion) {
        this.x += otherPosicion.x;
        this.y += otherPosicion.y;
    }

    /*
    Compara la posicion de un objeto con otro
    compara x, y para la igualdad
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Posicion posicion = (Posicion) o;
        return x == posicion.x && y == posicion.y;
    }

    /*
        Obtiene una version string de la posicion
     */
    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
