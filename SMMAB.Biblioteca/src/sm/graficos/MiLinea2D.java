/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sm.graficos;
import java.awt.geom.Point2D;
import java.awt.geom.Line2D;
/**
 * Clase propia de Línea
 * @author Manuel Alonso Braojos
 */
public class MiLinea2D extends Line2D.Float {
    
    /**
    * Constructor de Linea
    * @param p_ini punto de creación de la linea
    * @param p_fin punto final de la creación la linea
    */
    public MiLinea2D(Point2D p_ini, Point2D p_fin){
        super(p_ini,p_fin);
    }
    
    /**
    * Método para devolver si un punto esta cerca de la línea 
    * @param p El punto
    * @return Si el punto es cercano a la línea o no
    */
    public boolean isNear(Point2D p){
        Point2D p1=getP1();//punto inicial de la linea
        Point2D p2=getP2();//punto final de la linea
        
        if(p1.equals(p2)){
            return p1.distance(p)<=2.0;
        }else{
            return this.ptLineDist(p)<2.0;
        }
        
        
    }
    
    /**
    * Método que comprueba si el punto está dentro de los límites de la línea 
    * @param p El punto
    * @return Si el punto está dentro de los límites de la línea
    */
    @Override
    public boolean contains(Point2D p){
       return this.isNear(p);
    }
}
    
