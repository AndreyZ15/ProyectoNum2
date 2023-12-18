package BattleShip;
import java.awt.*;

public class Marcador extends Rectangle {
    private final Color Golpeado = new Color(219, 23, 23, 180);

    private final Color Fallado = new Color(26, 26, 97, 180);

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
        g.fillRect(posicion.x+Redondeo+1, posicion.y+Redondeo+1, width-Redondeo*2, height-Redondeo*2);
    }


}
