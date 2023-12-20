package BattleShip;
import java.awt.*;

public class Marcador extends Rectangulo {
    private final Color Golpeado = new Color(178, 1, 1, 180);

    private final Color Fallado = new Color(0, 0, 0, 180);

    //Redondea las esquinas para que llene el rectangulo un poco menos
    private final int Redondeo = 3;

    private boolean verMarca;

    private Barco barcoMarcado;

    public Marcador(int x, int y, int width, int height) {
        super(x, y, width, height);
        reset();
    }

    public void reset() {
        barcoMarcado = null;
        verMarca = false;
    }

    public void Marcado() {
        if(!verMarca && esBarco()) {
            barcoMarcado.destruirSeccion();
        }
        verMarca = true;
    }

    public boolean Marca() {
        return verMarca;
    }

    public void setBarco(Barco barco) {
        this.barcoMarcado = barco;
    }

    public boolean esBarco() {
        return barcoMarcado != null;
    }

    public Barco getBarcoCerca() {
        return barcoMarcado;
    }

    public void paint(Graphics g) {
        if(!verMarca) return;

        g.setColor(esBarco() ? Golpeado : Fallado);
        g.fillRect(posicion.x+Redondeo+1, posicion.y+Redondeo+1, ancho-Redondeo*2, altura-Redondeo*2);
    }


}
