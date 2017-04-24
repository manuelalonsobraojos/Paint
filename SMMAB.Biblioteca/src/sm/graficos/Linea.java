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
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

/**
 * Figura Línea
 * @author Manuel Alonso Braojos
 */
public class Linea extends Figura {
    
    /**
    * Constructor de Linea
    * @param p_ini punto de creación de la linea en el lienzo
    * @param p_fin punto final de la creación la linea en el lienzo
    * @param color color de creación de la linea en el lienzo
    * @param grosor grosor de creación de la linea en el lienzo
    * @param trazo si la discontinuidad del trazo esta activo o no
    * @param relleno si el relleno está activo o no
    * @param comp valor de transparencia con el que se va crear la linea
    * @param alisado si el alisado está activo o no
    * @param render valor de alisado para la creación de la linea
    */
    public Linea(Point2D p_ini, Point2D p_fin, Color color,float grosor,boolean trazo, boolean relleno, Composite comp,boolean alisado, RenderingHints render ){
        super(color, grosor,trazo, relleno,comp,alisado, render);
        figura=new MiLinea2D(p_ini,p_ini);
    }
    
    /**
    * Actualiza el punto final de la línea
    *
    * @param fin punto final de la creación de la línea en el lienzo
    */
    public void setLinea(Point2D fin){
        ((Line2D)figura).setLine(((Line2D)figura).getP1(), fin);
    }
    
    
    
}
