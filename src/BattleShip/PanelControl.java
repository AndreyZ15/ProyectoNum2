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

    /*
    Configura el estado del panel para dibujar el fondo
     */
    public PanelControl(Posicion posicion, int ancho, int alto) {
        super(posicion, ancho, alto);
        reset();
    }

    /*
    Restablece a los valores predeterminados,
    para la ubicacion del barco
     */
    public void reset() {
        lineasuperior = colocacionBarco1;
        lineainferior = colocacionBarco2;
    }

    /*
    Establece el mensaje para mostrar dependiendo si el jugador gano o no
     */
    public void JuegoTerminado(boolean playerWon) {
        lineasuperior = (playerWon) ? juegoGanado : juegoPerdido;
        lineainferior = reinicio;
    }

    /*
    Establece el mensaje a mostrar en la línea superior
     */
    public void setLineasuperior(String message) {
        lineasuperior = message;
    }

    /*
    Estblece el mensaje a mostrar en la línea inferior
     */
    public void setLineainferior(String message) {
        lineainferior = message;
    }

    /*
    Dibuja el cuadro de color gris con la fuente color negra
    usando los mensajes de la línea superior e inferior.
     */
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
