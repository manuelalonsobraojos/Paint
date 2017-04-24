/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sm.graficos;

import java.awt.Color;
import java.awt.Composite;
import java.awt.GradientPaint;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

/**
 * Figura Rectángulo con las esquinas redondeadas
 * @author Manuel Alonso Braojos
 */
public class RoundRectangulo extends Figura {
    
    /**
    * Constructor de RoundRectangulo
    * @param p_ini punto de creación del rectángulo redondeado en el lienzo
    * @param p_fin punto final de la creación del rectángulo redondeado en el lienzo
    * @param colorTrazo color del trazo del rectángulo redondeado en el lienzo
    * @param colorRelleno color de relleno del rectángulo redondeado en el lienzo
    * @param grosor grosor del trazo del rectángulo redondeado en el lienzo
    * @param trazo si la discontinuidad del trazo esta activo o no
    * @param relleno si el relleno está activo o no
    * @param comp valor de transparencia con el que se va crear el rectángulo redondeado
    * @param alisado si el alisado está activo o no
    * @param render valor de alisado para la creación de la linea
    * @param degradadoHz si el desgradado horizontal está activo o no
    * @param degradadoV si el degradado vertical está activo o no
    */
    public RoundRectangulo(Point2D p_ini,Point2D p_fin, Color colorTrazo, Color colorRelleno, float grosor, boolean trazo, boolean relleno, Composite comp, boolean alisado, RenderingHints render, boolean degradadoHz, boolean degradadoV){
        super(colorTrazo, colorRelleno, grosor, trazo, relleno, comp, alisado, render,degradadoHz, degradadoV);
        figura = new RoundRectangle2D.Double(p_ini.getX(), p_ini.getY(), 0, 0, 50, 50);
        
    }
    
    /**
    * Actualiza el punto final del rectángulo redondeado
    * @param p_ini punto de creación del rectángulo redondeado en el lienzo
    * @param p_fin punto final de la creación del rectángulo redondeado en el lienzo
    */
    public void setRoundRectangulo(Point2D p_ini,Point2D p_fin){
        
        ((RoundRectangle2D)figura).setFrameFromDiagonal(p_ini,p_fin);
    }
    
    /**
    * Método para devolver el degradado horizontal del rectángulo redondeado
    * 
    * @return degradadoH
    */
    public Paint getPaintRoundRectanguloH(){
        GradientPaint degradadoH= new GradientPaint((int)((RoundRectangle2D)figura).getMinX(), (int)((RoundRectangle2D)figura).getCenterY(), this.getColorRelleno(), (int)((RoundRectangle2D)figura).getMaxX(), (int)((RoundRectangle2D)figura).getCenterY(), this.getColorTrazo());
        return degradadoH;
    }
    
    /**
    * Método para devolver el degradado vertical del rectángulo redondeado
    * 
    * @return degradadoV
    */
    public Paint getPaintRoundRectanguloV(){
        GradientPaint degradadoV= new GradientPaint((int)((RoundRectangle2D)figura).getCenterX(), (int)((RoundRectangle2D)figura).getMinY(), this.getColorRelleno(), (int)((RoundRectangle2D)figura).getCenterX(), (int)((RoundRectangle2D)figura).getMaxY(), this.getColorTrazo());
        return degradadoV;
    }
}

