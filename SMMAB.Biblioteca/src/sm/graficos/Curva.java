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
import java.awt.geom.Point2D;
import java.awt.geom.QuadCurve2D;
import java.awt.geom.Rectangle2D;

/**
 * Figura Curva con punto de control
 * @author Manuel Alonso Braojos
 */
public class Curva extends Figura {
    
    /**
    * Constructor de Curva
    * @param p_ini punto de creación de la curva en el lienzo
    * @param p_fin punto de fin de la creación de la curva en el lienzo
    * @param control punto de control de la creación de la curva en el lienzo
    * @param color color de creación de la curva punto en el lienzo
    * @param grosor grosor de creación de la curva en el lienzo
    * @param trazo si la discontinuidad del trazo esta activo o no
    * @param relleno si el relleno está activo o no
    * @param comp valor de transparencia con el que se va crear la curva
    * @param alisado si el alisado está activo o no
    * @param render valor de alisado para la creación de la curva
    */
    public Curva(Point2D p_ini,Point2D p_fin,Point2D control, Color color, float grosor, boolean trazo, boolean relleno, Composite comp, boolean alisado, RenderingHints render){
        super(color, grosor,trazo, relleno, comp, alisado, render);
        figura = new QuadCurve2D.Double();
        ((QuadCurve2D)figura).setCurve(p_ini,p_fin,p_fin);
    }
    
    /**
    * Actualiza el punto final de la curva
    * @param p_ini punto de creación de la curva en el lienzo
    * @param pcontrol punto de control para la creación de la curva
    * @param p_fin punto final de la creación de la curva en el lienzo
    */
    public void setCurva(Point2D p_ini,Point2D pcontrol,Point2D p_fin){
        
        ((QuadCurve2D)figura).setCurve(p_ini, pcontrol, p_fin);
    }
    
    
}
