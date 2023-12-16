package BattleShip;



public class Rectangulo {
    protected Posicion posicion;

    protected int ancho;

    protected int altura;

    public Rectangulo(Posicion posicion, int ancho, int altura) {
        this.posicion = posicion;
        this.ancho = ancho;
        this.altura = altura;
    }
    public Rectangulo(int x, int y, int ancho, int altura) {
        this(new Posicion(x,y),ancho,altura);
    }

    public int getAncho() {
        return ancho;
    }
    public int getAltura() {
        return altura;
    }

    public Posicion getPosicion() {
        return posicion;
    }

    public boolean esPosicionInside(Posicion targetPosicion) {
        return targetPosicion.x >= posicion.x && targetPosicion.y >= posicion.y
                && targetPosicion.x < posicion.x + ancho && targetPosicion.y < position.y + altura;
    }

}
