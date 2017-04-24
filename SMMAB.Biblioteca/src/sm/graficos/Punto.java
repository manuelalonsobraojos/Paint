/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sm.graficos;

import java.awt.Color;
import java.awt.Composite;
import java.awt.RenderingHints;
import java.awt.Stroke;
import sm.graficos.Figura;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import sm.graficos.MiLinea2D;

/**
 * Figura punto
 * @author Manuel Alonso Brajos
 */
public class Punto extends Figura {
        
    /**
    * Constructor de Punto
    * @param p_ini punto de creación del punto en el lienzo
    * @param color color de creación del punto en el lienzo
    * @param grosor grosor de creación del punto en el lienzo
    * @param trazo si la discontinuidad del trazo esta activo o no
    * @param relleno si el relleno está activo o no
    * @param comp valor de transparencia con el que se va crear el punto
    * @param alisado si el alisado está activo o no
    * @param render valor de alisado para la creación del punto
    */
    public Punto(Point2D p_ini, Color color, float grosor, boolean trazo, boolean relleno, Composite comp, boolean alisado, RenderingHints render){
        super(color,grosor, trazo, relleno, comp, alisado, render);
        figura = new MiLinea2D(p_ini, p_ini);
    }
}
