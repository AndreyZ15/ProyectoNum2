package BattleShip;

import java.awt.*;

public class PanelControl extends Rectangulo{
    private final Font font = new Font("Arial", Font.BOLD, 20);

    private final String colocacionBarco1 = "Coloca los barcos en la pantalla de abajo!";

    private final String colocacionBarco2 = "Z para rotar los barcos.";

    private final String juegoPerdido = "Game Over! Perdiste :(";

    private final String juegoGanado = "Ganaste!!";

    private final String reinicio = "R para reiniciar.";

    private String lineasuperior;

    private String lineainferior;

    public PanelControl(Posicion posicion, int ancho, int alto) {
        super(posicion, ancho, alto);
        reset();
    }

    public void reset() {
        lineasuperior = colocacionBarco1;
        lineainferior = colocacionBarco2;
    }

    public void JuegoTerminado(boolean playerWon) {
        lineasuperior = (playerWon) ? juegoGanado : juegoPerdido;
        lineainferior = juegoPerdido;
    }


    public void setLineasuperior(String message) {
        lineasuperior = message;
    }

    public void setLineainferior(String message) {
        lineainferior = message;
    }
    public void paint(Graphics g) {
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(posicion.x, posicion.y, ancho, altura);
        g.setColor(Color.BLACK);
        g.setFont(font);
        int strWidth = g.getFontMetrics().stringWidth(lineasuperior);
        g.drawString(lineasuperior, posicion.x+ancho/2-strWidth/2, posicion.y+20);
        strWidth = g.getFontMetrics().stringWidth(lineainferior);
        g.drawString(lineainferior, posicion.x+ancho/2-strWidth/2, posicion.y+40);
    }
}
