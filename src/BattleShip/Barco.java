package BattleShip;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.awt.image.BufferedImage;

public class Barco {

    public enum ColorPosicionBarco {Valid, Invalid, Placed}

    private Posicion pantallaPosicion;

    private Posicion crearPosicion;

    private int segmentos;

    private boolean isTorcido;

    private int seccionesDestruidas;

    private ColorPosicionBarco colorPosicionBarco;

    /*
    Este metodo atrae todas las partes del barco y las une en metodo Barco
     */
    public Barco(Posicion pantallaPosicion, Posicion crearPosicion, int segmentos, boolean isTorcido) {
        this.pantallaPosicion = pantallaPosicion;
        this.crearPosicion = crearPosicion;
        this.segmentos = segmentos;
        this.isTorcido = isTorcido;
        seccionesDestruidas = 0;
        colorPosicionBarco = ColorPosicionBarco.Placed;
    }

    /*
    El metodo de paint está varias veces pero este metodo le pone color a los
    barcos tanto cuando se van a poner en pantalla como cuando están puestos
     */
    public void paint(Graphics g) {
        if(colorPosicionBarco == ColorPosicionBarco.Placed) {
            g.setColor(seccionesDestruidas >= segmentos ? Color.RED : Color.GRAY);
        } else {
            g.setColor(colorPosicionBarco == ColorPosicionBarco.Valid ? Color.GREEN : Color.RED);
        }
        if(isTorcido) crearHorizontal(g);
        else crearVertical(g);
    }

    /*
    Este set lo que realiza es setear el color cuando se posisiona el barco en la pantalla
     */

    public void setColorPosicionBarco(ColorPosicionBarco colorPosicionBarco) {
        this.colorPosicionBarco = colorPosicionBarco;
    }

    /*
    Permite ver donde estan los barcos para no solo ponerlos horizontales si no verticales tambien
     */
    public void cambiarTorcido() {
        isTorcido = !isTorcido;
    }

    /*
    Es para marcar la zona del barco a hundir
     */
    public void destruirSeccion() {
        seccionesDestruidas++;
    }

    /*
    Dice cuando un barco del tamaño que sea se destruye
     */

    public boolean isDestruido() {
        return seccionesDestruidas >= segmentos;
    }

    /*
    Se establece la posicion del barco en la pantalla
     */

    public void setCrearPosicion(Posicion pantallaPosicion, Posicion crearPosicion) {
        this.crearPosicion = crearPosicion;
        this.pantallaPosicion = pantallaPosicion;
    }

    /*
    Permite ver si el barco está torcido o no
     */
    public boolean isTorcido() {
        return isTorcido;
    }

    /*
    Trae los segmentos de los barcos que serian los espacios que ocupa el barco en la pantalla
     */
    public int getSegmentos() {
        return segmentos;
    }

    /*
    Es un getter para ver las coordenadas que estan ocupando los barcos para que no deje poner uno en ese mismo lugar
     */
    public java.util.List<Posicion> getOccupiedCoordinates() {
        List<Posicion> result = new ArrayList<>();
        if(isTorcido) { // handle the case when horizontal
            for(int x = 0; x < segmentos; x++) {
                result.add(new Posicion(pantallaPosicion.x+x, pantallaPosicion.y));
            }
        } else { // handle the case when vertical
            for(int y = 0; y < segmentos; y++) {
                result.add(new Posicion(pantallaPosicion.x, pantallaPosicion.y+y));
            }
        }
        return result;
    }

    /*
    Deja que se puedan poner los barcos de forma vertical
     */

    public void crearVertical(Graphics g) {
        int boatWidth = (int)(SeleccionPanel.CELDA_SIZE * 0.8);
        int boatLeftX = crearPosicion.x + SeleccionPanel.CELDA_SIZE / 2 - boatWidth / 2;
        g.fillPolygon(new int[]{crearPosicion.x+SeleccionPanel.CELDA_SIZE/2,boatLeftX,boatLeftX+boatWidth},
                new int[]{crearPosicion.y+SeleccionPanel.CELDA_SIZE/4,crearPosicion.y+SeleccionPanel.CELDA_SIZE,crearPosicion.y+SeleccionPanel.CELDA_SIZE},3);
        g.fillRect(boatLeftX,crearPosicion.y+SeleccionPanel.CELDA_SIZE, boatWidth,
                (int)(SeleccionPanel.CELDA_SIZE * (segmentos-1.2)));
    }
    /*
    Deja que se coloquen los barcos de forma horizontal
     */
    public void crearHorizontal(Graphics g) {
        int boatWidth = (int)(SeleccionPanel.CELDA_SIZE * 0.8);
        int boatTopY = crearPosicion.y + SeleccionPanel.CELDA_SIZE/ 2 - boatWidth / 2;
        g.fillPolygon(new int[]{crearPosicion.x+SeleccionPanel.CELDA_SIZE/4,crearPosicion.x+SeleccionPanel.CELDA_SIZE,crearPosicion.x+SeleccionPanel.CELDA_SIZE},
                new int[]{crearPosicion.y+SeleccionPanel.CELDA_SIZE/2,boatTopY,boatTopY+boatWidth},3);
        g.fillRect(crearPosicion.x+SeleccionPanel.CELDA_SIZE,boatTopY,
                (int)(SeleccionPanel.CELDA_SIZE * (segmentos-1.2)), boatWidth);
    }

}
