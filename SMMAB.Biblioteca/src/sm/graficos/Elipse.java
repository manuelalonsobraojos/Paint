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
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * Figura Elipse
 * @author Manuel Alonso Braojos
 */
public class Elipse extends Figura {
      
    /**
    * Constructor de Elipse
    * @param p_ini punto de creación de la elipse en el lienzo
    * @param p_fin punto final de la creación de la elipse en el lienzo
    * @param colorTrazo color del trazo de la elipse en el lienzo
    * @param colorRelleno color de relleno de la elipse en el lienzo
    * @param grosor grosor del trazo de la elipse en el lienzo
    * @param trazo si la discontinuidad del trazo esta activo o no
    * @param relleno si el relleno está activo o no
    * @param comp valor de transparencia con el que se va crear la elipse
    * @param alisado si el alisado está activo o no
    * @param render valor de alisado para la creación de la elipse
    * @param degradadoHz si el desgradado horizontal está activo o no
    * @param degradadoV si el degradado vertical está activo o no
    */
    public Elipse(Point2D p_ini, Point2D p_fin, Color colorTrazo, Color colorRelleno, float grosor, boolean trazo, boolean relleno,Composite comp, boolean alisado, RenderingHints render, boolean degradadoHz, boolean degradadoV ){
        super(colorTrazo, colorRelleno, grosor, trazo, relleno, comp, alisado, render, degradadoHz, degradadoV);
        figura= new Ellipse2D.Double();
        ((Ellipse2D)figura).setFrameFromDiagonal(p_ini,p_ini);
    }
    
    /**
    * Actualiza el punto final de la elipse
    * @param p_ini punto de creación del rectángulo en el lienzo
    * @param p_fin punto final de la creación del rectángulo en el lienzo
    */
    public void setElipse(Point2D p_ini, Point2D p_fin){
        ((Ellipse2D)figura).setFrameFromDiagonal(p_ini,p_fin);
    }
    
    /**
    * Método para devolver el degradado horizontal de la elipse
    * 
    * @return degradadoH
    */
    public Paint getPaintElipseH(){
        GradientPaint degradadoH= new GradientPaint((int)((Ellipse2D)figura).getMinX(), (int)((Ellipse2D)figura).getCenterY(), this.getColorRelleno(), (int)((Ellipse2D)figura).getMaxX(), (int)((Ellipse2D)figura).getCenterY(), this.getColorTrazo());
        return degradadoH;
    }
    
    /**
    * Método para devolver el degradado vertical de la elipse
    * 
    * @return degradadoV
    */
    public Paint getPaintElipseV(){
        GradientPaint degradadoV= new GradientPaint((int)((Ellipse2D)figura).getCenterX(), (int)((Ellipse2D)figura).getMinY(), this.getColorRelleno(), (int)((Ellipse2D)figura).getCenterX(), (int)((Ellipse2D)figura).getMaxY(), this.getColorTrazo());
        return degradadoV;
    }
}
