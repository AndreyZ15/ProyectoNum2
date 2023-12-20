package BattleShip;
import java.awt.*;

public class Marcador extends Rectangulo {
    private final Color Golpeado = new Color(178, 1, 1, 180);

    private final Color Fallado = new Color(0, 0, 0, 180);

    //Redondea las esquinas para que llene el rectangulo un poco menos
    private final int Redondeo = 3;

    private boolean verMarca;

    private Barco barcoMarcado;

    /*
    Prepara el marcador con un estado predeterminado en el que,
    está listo para dibujar en una posicion especificada
     */
    public Marcador(int x, int y, int width, int height) {
        super(x, y, width, height);
        reset();
    }


    /*
    Se restablece a ningun barco referenciado y con el marcador invisble
     */
    public void reset() {
        barcoMarcado = null;
        verMarca = false;
    }

    /*
    Si no está marcado le dira al barco asociado que otra seccion,
    fue destruida hace que la marca sea visible
     */
    public void Marcado() {
        if(!verMarca && esBarco()) {
            barcoMarcado.destruirSeccion();
        }
        verMarca = true;
    }

    /*
    Obtiene si el marcador ya ha interactuado
     */
    public boolean Marca() {
        return verMarca;
    }

    /*
    Establece el barco en la referencia especificada
    y cambia de color si el marcador está revelado
     */
    public void setBarco(Barco barco) {
        this.barcoMarcado = barco;
    }
    /*
    Obtiene si el marcador tiene un barco asociado
     */
    public boolean esBarco() {
        return barcoMarcado != null;
    }

    /*
    Obtiene el barco si no hay ninguno es null
     */
    public Barco getBarcoCerca() {
        return barcoMarcado;
    }

    /*
    Si es fallo marca de color negro y si acierta marca de color rojo
     */
    public void paint(Graphics g) {
        if(!verMarca) return;

        g.setColor(esBarco() ? Golpeado : Fallado);
        g.fillRect(posicion.x+Redondeo+1, posicion.y+Redondeo+1, ancho-Redondeo*2, altura-Redondeo*2);
    }


}
