/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sm.graficos;

import java.awt.Color;
import java.awt.Composite;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.QuadCurve2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Line2D;
/**
 * Clase padre las figuras
 * @author Manuel Alonso Braojos
 */
public class Figura {
    
   protected Shape figura;
   private Color colorTrazo;
   private Color colorRelleno;
   private Stroke stroke;//Grosor
   private boolean relleno;
   private boolean transparencia;
   private Composite comp;
   private boolean alisado;
   private RenderingHints render;
   private boolean degradadoHz;
   private boolean degradadoV;
   private float grosor;
   private boolean trazo;
   
   /**
    * Constructor por defecto de la clase Figura
    */
   public Figura(){
       
   }
   
   /**
    * Constructor de por parámetros de la clase Figura
    * @param color color del trazo de la figura en el lienzo
    * @param grosor grosor del trazo de la figura en el lienzo
    * @param trazo si la discontinuidad del trazo esta activo o no
    * @param relleno si el relleno está activo o no
    * @param comp valor de transparencia con el que se va crear la figura
    * @param alisado si el alisado está activo o no
    * @param render valor de alisado para la creación de la figura
    */
   public Figura(Color color, float grosor,boolean trazo, boolean relleno, Composite comp, boolean alisado, RenderingHints render){
       this.colorTrazo=color;
       this.relleno=relleno;
       this.comp=comp;
       this.alisado=alisado;
       this.render=render;
       this.grosor=grosor;
       //this.stroke=stroke;
       this.trazo=trazo;
   }
   
   /**
    * Constructor por parámetros de la clase Figura
    * @param colorTrazo color del trazo de la figura en el lienzo
    * @param colorRelleno color de relleno de la figura en el lienzo
    * @param grosor grosor del trazo de la figura en el lienzo
    * @param trazo si la discontinuidad del trazo esta activo o no
    * @param relleno si el relleno está activo o no
    * @param comp valor de transparencia con el que se va crear la figura
    * @param alisado si el alisado está activo o no
    * @param render valor de alisado para la creación de la figura
    * @param degradadoHz si el desgradado horizontal está activo o no
    * @param degradadoV si el degradado vertical está activo o no
    */
   public Figura(Color colorTrazo, Color colorRelleno,float grosor,boolean trazo, boolean relleno, Composite comp, boolean alisado, RenderingHints render, boolean degradadoHz, boolean degradadoV){
       this.colorTrazo=colorTrazo;
       this.colorRelleno=colorRelleno;
       this.relleno=relleno;
       this.grosor=grosor;
       this.trazo=trazo;
       this.comp=comp;
       this.alisado=alisado;
       this.render=render;
       this.degradadoHz=degradadoHz;
       this.degradadoV=degradadoV;
   } 

   /**
    * Método para devolver el color del trazo de la figura
    * 
    * @return colorTrazo el color del trazo de la figura
    */
    public Color getColorTrazo() {
        return colorTrazo;
    }

    /**
    * Método modificar el color del trazo de la figura
    * 
    * @param colorTrazo el nuevo color de trazo de la figura
    */
    public void setColorTrazo(Color colorTrazo) {
        this.colorTrazo = colorTrazo;
    }

    /**
    * Método para devolver el color de relleno de la figura
    * 
    * @return colorRelleno el color de relleno de la figura
    */
    public Color getColorRelleno() {
        return colorRelleno;
    }

    /**
    * Método modificar el color de relleno de la figura
    * 
    * @param colorRelleno el nuevo color de relleno de la figura
    */
    public void setColorRelleno(Color colorRelleno) {
        this.colorRelleno = colorRelleno;
    }
    
    /**
    * Método para devolver la figura
    * 
    * @return figura la figura
    */
    public Shape getFigura() {
        return figura;
    }

    /**
    * Método modificar el la figura
    * 
    * @param figura nueva figura
    */
    public void setFigura(Shape figura) {
        this.figura = figura;
    }

    /**
    * Método para devolver si el trazo discontinuo está activado o no 
    * 
    * @return trazo
    */
    public boolean getTrazo() {
        return trazo;
    }

    /**
    * Método modificar si el trazo discontinuo está activado o no
    * 
    * @param trazo si el trazo discontinuo esta activado o no
    */
    public void setTrazo(boolean trazo) {
        this.trazo = trazo;
    }
    
    /**
    * Método para devolver si el relleno está activado o no 
    * 
    * @return relleno si el relleno está activado o no
    */
    public boolean getRelleno() {
        return relleno;
    }

    /**
    * Método modificar si el relleno está activado o no
    * 
    * @param relleno si el trazo discontinuo esta activado o no
    */
    public void setRelleno(boolean relleno) {
        this.relleno = relleno;
    }

    /**
    * Método para devolver la trasnparencia de la figura 
    * 
    * @return comp Transparencia de una figura
    */
    public Composite getComp() {
        return comp;
    }

    /**
    * Método para modificar la trasnparencia de la figura 
    * 
    * @param comp nueva transparencia de la figura
    */
    public void setComp(Composite comp) {
        this.comp = comp;
    }

    /**
    * Método para devolver si el alisado de la figura está activo o no
    * 
    * @return alisado si el alisado está activo o no
    */
    public boolean getAlisado() {
        return alisado;
    }
    
    /**
    * Método para modificar si el alisado de la figura esta activo o no 
    * 
    * @param alisado nueva transparencia de la figura
    */
    public void setAlisado(boolean alisado) {
        this.alisado = alisado;
    }

    /**
    * Método para devolver el alisado de la figura
    * 
    * @return render alisado de la figura
    */
    public RenderingHints getRender() {
        return render;
    }

    /**
    * Método para modificar el alisado de la figura 
    * 
    * @param render nuevo alisado de la figura
    */
    public void setRender(RenderingHints render) {
        this.render = render;
    }

    /**
    * Método para devolver si el degradado horizontal de la figura está activo o no
    * 
    * @return degradadoHz si el degradado horizontal de la figura está activo o no
    */
    public boolean getDegradadoHz() {
        return degradadoHz;
    }

    /**
    * Método para modificar el degradado horizontal de la figura 
    * 
    * @param degradadoHz nuevo degradado horizontal de la figura
    */
    public void setDegradadoHz(boolean degradadoHz) {
        this.degradadoHz = degradadoHz;
    }

    /**
    * Método para devolver si el degradado vertical de la figura está activo o no
    * 
    * @return degradadoHz si el degradado vertical de la figura está activo o no
    */
    public boolean getDegradadoV() {
        return degradadoV;
    }

    /**
    * Método para modificar el degradado vertical de la figura 
    * 
    * @param degradadoV nuevo degradado vertical de la figura
    */
    public void setDegradadoV(boolean degradadoV) {
        this.degradadoV = degradadoV;
    }

    /**
    * Método para devolver el grosor del trazo de la figura
    * 
    * @return grosor grosor del trazo de la figura
    */
    public float getGrosor() {
        return grosor;
    }

    /**
    * Método para modificar el grosor del trazo de la figura
    * 
    * @param grosor nuevo grosor del trazo de la figura
    */
    public void setGrosor(float grosor) {
        this.grosor = grosor;
    }
    
    
    
}
