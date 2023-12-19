package BattleShip;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class Barco {

    public enum ColorPosicionBarco {Valid, Invalid, Placed}

    private Posicion pantallaPosicion;

    private Posicion crearPosicion;

    private int segmentos;

    private boolean isTorcido;

    private int seccionesDestruidas;
    private ColorPosicionBarco colorPosicionBarco;

    public Barco(Posicion pantallaPosicion, Posicion crearPosicion, int segmentos, boolean isTorcido) {
        this.pantallaPosicion = pantallaPosicion;
        this.crearPosicion = crearPosicion;
        this.segmentos = segmentos;
        this.isTorcido = isTorcido;
        seccionesDestruidas = 0;
        colorPosicionBarco = ColorPosicionBarco.Placed;
    }

    public void paint(Graphics g) {
        if(colorPosicionBarco == ColorPosicionBarco.Placed) {
            g.setColor(seccionesDestruidas >= segmentos ? Color.RED : Color.DARK_GRAY);
        } else {
            g.setColor(colorPosicionBarco == ColorPosicionBarco.Valid ? Color.GREEN : Color.RED);
        }
        if(isTorcido) crearHorizontal(g);
        else crearVertical(g);
    }

    public void setColorPosicionBarco(ColorPosicionBarco colorPosicionBarco) {
        this.colorPosicionBarco = colorPosicionBarco;
    }


    public void cambiarTorcido() {
        isTorcido = !isTorcido;
    }

    public void destruirSeccion() {
        seccionesDestruidas++;
    }

    public boolean isDestruido() {
        return seccionesDestruidas >= segmentos;
    }


    public void setCrearPosicion(Posicion pantallaPosicion, Posicion crearPosicion) {
        this.crearPosicion = crearPosicion;
        this.pantallaPosicion = pantallaPosicion;
    }

    public boolean isTorcido() {
        return isTorcido;
    }
    public int getSegmentos() {
        return segmentos;
    }

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

    public void crearVertical(Graphics g) {
        int boatWidth = (int)(SeleccionPanel.CELDA_SIZE * 0.8);
        int boatLeftX = crearPosicion.x + SeleccionPanel.CELDA_SIZE / 2 - boatWidth / 2;
        g.fillPolygon(new int[]{crearPosicion.x+SeleccionPanel.CELDA_SIZE/2,boatLeftX,boatLeftX+boatWidth},
                new int[]{crearPosicion.y+SeleccionPanel.CELDA_SIZE/4,crearPosicion.y+SeleccionPanel.CELDA_SIZE,crearPosicion.y+SeleccionPanel.CELDA_SIZE},3);
        g.fillRect(boatLeftX,crearPosicion.y+SeleccionPanel.CELDA_SIZE, boatWidth,
                (int)(SeleccionPanel.CELDA_SIZE * (segmentos-1.2)));
    }

    public void crearHorizontal(Graphics g) {
        int boatWidth = (int)(SeleccionPanel.CELDA_SIZE * 0.8);
        int boatTopY = crearPosicion.y + SeleccionPanel.CELDA_SIZE/ 2 - boatWidth / 2;
        g.fillPolygon(new int[]{crearPosicion.x+SeleccionPanel.CELDA_SIZE/4,crearPosicion.x+SeleccionPanel.CELDA_SIZE,crearPosicion.x+SeleccionPanel.CELDA_SIZE},
                new int[]{crearPosicion.y+SeleccionPanel.CELDA_SIZE/2,boatTopY,boatTopY+boatWidth},3);
        g.fillRect(crearPosicion.x+SeleccionPanel.CELDA_SIZE,boatTopY,
                (int)(SeleccionPanel.CELDA_SIZE * (segmentos-1.2)), boatWidth);
    }

}
