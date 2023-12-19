package BattleShip;

import javax.swing.text.Position;

public class Posicion {

    public static final Posicion Abajo = new Posicion(0,1);

    public static final Posicion Arriba = new Posicion(0,-1);

    public static final Posicion Izquierda = new Posicion (-1,0);

    public static final Posicion Derecha = new Posicion(1,0);

    public static final Posicion Zero = new Posicion(0,0);
    public int x;
    /**
     * Y coordinate.
     */
    public int y;

    public Posicion(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Posicion(Posicion posicionToCopy) {
        this.x = posicionToCopy.x;
        this.y = posicionToCopy.y;
    }
    public void setPosicion(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void add(Posicion otherPosicion) {
        this.x += otherPosicion.x;
        this.y += otherPosicion.y;
    }

    public double distanceTo(Posicion otherPosicion) {
        return Math.sqrt(Math.pow(x-otherPosicion.x,2)+Math.pow(y-otherPosicion.y,2));
    }
    public void multiply(int amount) {
        x *= amount;
        y *= amount;
    }

    public void subtract(Posicion otherPosicion) {
        this.x -= otherPosicion.x;
        this.y -= otherPosicion.y;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Posicion posicion = (Posicion) o;
        return x == posicion.x && y == posicion.y;
    }

    /**
     * Gets a string version of the Position.
     *
     * @return A string in the form (x, y)
     */
    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
