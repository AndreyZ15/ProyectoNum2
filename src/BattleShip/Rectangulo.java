package BattleShip;



public class Rectangulo {
    protected Posicion posicion;

    protected int ancho;

    protected int altura;

    /*
    Crea el rectangulo con las propiedades indicadas
     */
    public Rectangulo(Posicion posicion, int ancho, int altura) {
        this.posicion = posicion;
        this.ancho = ancho;
        this.altura = altura;
    }
    public Rectangulo(int x, int y, int ancho, int altura) {
        this(new Posicion(x,y),ancho,altura);
    }

    /*
    Obtiene el ancho del rectangulo
     */
    public int getAncho() {
        return ancho;
    }

    /*
    Obtiene la altura del rectangulo
     */
    public int getAltura() {
        return altura;
    }

    /*
    Obtiene la posicion del rectangulo
     */
    public Posicion getPosicion() {
        return posicion;
    }

    /*
    Prueba que el target este dentro del rectangulo
     */
    public boolean esPosicionInside(Posicion targetPosicion) {
        return targetPosicion.x >= posicion.x && targetPosicion.y >= posicion.y
                && targetPosicion.x < posicion.x + ancho && targetPosicion.y < posicion.y + altura;
    }

}
