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
import java.awt.geom.Arc2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * Figura Arco
 * @author Manuel Alonso Braojos
 */
public class Arco extends Figura {
    
    /**
    * Constructor de Arco
    * @param p_ini punto de creación del arco en el lienzo
    * @param p_fin punto final de la creación del arco en el lienzo
    * @param color color de creación del arco en el lienzo
    * @param grosor grosor de creación del arco en el lienzo
    * @param trazo si la discontinuidad del trazo esta activo o no
    * @param relleno si el relleno está activo o no
    * @param comp valor de transparencia con el que se va crear el arco
    * @param alisado si el alisado está activo o no
    * @param render valor de alisado para la creación del arco
    */
    public Arco(Point2D p_ini,Point2D p_fin, Color color, float grosor, boolean trazo, boolean relleno, Composite comp, boolean alisado, RenderingHints render){
        super(color, grosor, trazo, relleno, comp, alisado, render);
        figura = new Arc2D.Double(p_ini.getX(), p_ini.getY(), 0,0,90, 135,Arc2D.OPEN);
        
    }
    
    /**
    * Actualiza el punto final del arco
    * @param p_ini punto de creación del arco en el lienzo
    * @param p_fin punto final de la creación del arco en el lienzo
    */
    public void setArco(Point2D p_ini,Point2D p_fin){
        
        ((Arc2D)figura).setFrameFromDiagonal(p_ini,p_fin);
    }
}

