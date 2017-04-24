/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sm.graficos;

import java.awt.Color;
import java.awt.Composite;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.Point2D;
import java.util.Vector;


/**
 * Figura polígono
 * @author Manuel Alonso Braojos
 */
public class Poligono extends Figura {
    
    /**
    * Constructor de Poligono
    * @param x vector de coordenadas X de un punto de creación del polígono en el lienzo
    * @param y vector de coordenadas Y de un punto de creación del polígono en el lienzo
    * @param colorTrazo color del trazo del polígono en el lienzo
    * @param colorRelleno color de relleno del polígono en el lienzo
    * @param grosor grosor del trazo del polígono en el lienzo
    * @param trazo si la discontinuidad del trazo esta activo o no
    * @param relleno si el relleno está activo o no
    * @param comp valor de transparencia con el que se va crear el polígono
    * @param alisado si el alisado está activo o no
    * @param render valor de alisado para la creación de la linea
    * @param degradadoHz si el desgradado horizontal está activo o no
    * @param degradadoV si el degradado vertical está activo o no
    */
    public Poligono(int x[], int y[], Color colorTrazo, Color colorRelleno, float grosor, boolean trazo, boolean relleno, Composite comp, boolean alisado, RenderingHints render, boolean degradadoHz, boolean degradadoV){
        super(colorTrazo, colorRelleno, grosor, trazo, relleno, comp, alisado, render, degradadoHz, degradadoV);
        
        figura = new Polygon(x,y,y.length);
        
    }
    
    
}
