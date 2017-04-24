/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sm.mab.iu;

import sm.graficos.MiLinea2D;
import static com.sun.scenario.effect.impl.prism.PrEffectHelper.render;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.util.List;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.QuadCurve2D;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.Vector;
import javafx.geometry.BoundingBox;
import sm.graficos.Arco;
import sm.graficos.Curva;
import sm.graficos.Elipse;
import sm.graficos.Figura;
import sm.graficos.Linea;
import sm.graficos.Poligono;
import sm.graficos.Punto;
import sm.graficos.Rectangulo;
import sm.graficos.RoundRectangulo;

/**
 *
 * @author Manuel Alonso Braojos
 */
public class Lienzo2D extends javax.swing.JPanel {

    private Shape figuraShape;
    private List<Shape> vShape = new ArrayList();
    private List<Figura> vFigura = new ArrayList();
    private int objeto;
    private boolean relleno=false;
    private Point2D p_ini;
    private Point2D p_fin;
    private Point2D punto_curva;
    private Color color=Color.BLACK;
    private boolean editar=false;
    private Stroke stroke= new BasicStroke();
    private Composite comp=AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f);
    private RenderingHints render= new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF); 
    private boolean transparencia=false;
    private boolean alisado=false;
    private Shape areaDibujo=null;
    private boolean curva=false;
    private Figura figura=null;
    private Figura figura_selec=null;
    private boolean degradado_hz=false;
    private boolean degradado_v=false;
    private boolean dobleclic;
    private int poliX[]={};
    private int poliY[]={};
    private Point2D coord;
    private Point2D coordFin;
    private boolean bloqueo=false;
    private float grosor;
    private Stroke sk=new BasicStroke();
    private boolean trazo;
    private boolean actiColorTr;//booleano de activación del color de trazo
    private boolean actiColorFill;//booleano de activación del color de relleno
    private Color colorRelleno;
    private Color colorTrazo=Color.BLACK;
    private Point2D p_ini_curva;
    private Point2D p_fin_curva;
    private Point2D p_control_curva;
    
    /**
     * Constructor por defecto de la clase Lienzo2D
     */
    public Lienzo2D() {
        initComponents();
    }

    /**
     * Método que devuelve el area de dibujo
     * @return areaDibujo devuelve el area de dibujo
     */
    public Shape getAreaDibujo() {
        return areaDibujo;
    }

    /**
     * método para modificar el area de dibujo
     * @param areaDibujo nueva area de dibujo
     */
    public void setAreaDibujo(Shape areaDibujo) {
        this.areaDibujo = areaDibujo;
    }

    /**
     * Método que devuelve el vector de figuras
     * @return vFigura vector de figuras
     */
    public List<Figura> getvFigura() {
        return vFigura;
    }

    /**
     * Método para modificar el vector de figuras
     * @param vFigura objeto arrayList de tipo Figura que representa el nuevo vector de figuras
     */
    public void setvFigura(List<Figura> vFigura) {
        this.vFigura = vFigura;
    }
    
    /**
     * Metodo que devuelve si la trasnparencia está activada o no
     * @return trasnparencia si la transoarencia está activada o no
     */
    public boolean getTransparencia(){
        return this.transparencia;
    }
    
    /**
     * Método para devolver si el alisado está activo o no
     * @return aliado si esta el alisado activo o no
     */
    public boolean getAlisado(){
        return this.alisado;
    }
    
    /**
     * Método para modificar el punto de punto de inicio 
     * @param p_ini objeto de tipo Point2D que representa el nuevo punto de inicio
     */
    public void setP(Point2D p_ini){
        this.p_ini=p_ini;
    }
    
    /**
     * Método para devolver el punto de inicio
     * @return p_ini obetdo de tipo Point2D que devuelve el punto de inicio
     */
    public Point2D getP(){
        return p_ini;
    }
   
    /**
     * Método para modificar el punto de punto de fin
     * @param p_fin objeto de tipo Point2D que representa el nuevo punto de fin
     */
    public void setPFin(Point2D p_fin){
        this.p_fin=p_fin;
    }
    
    /**
     * Método para devolver el punto de fin
     * @return p_fin obetdo de tipo Point2D que devuelve el punto de inicio
     */
    public Point2D getPFin(){
        return this.p_fin;
    }
    
    /**
     * Método para devolver el tipo de objeto
     * @param objeto entero que representa el nuevo objeto
     */
    public void setObjeto(int objeto){
        this.objeto=objeto;
    }
    
    /**
     * Método para devolver el objeto
     * @return objeto devuelve un entero que representa un objeto
     */
    public int getObjeto(){
        return objeto;
    }
    
    /**
     * Método que modifica la figura
     * @param figura objeto de tipo Figura que representa la nueva figura
     */
    public void setFiguraShape(Figura figura){
        this.figura=figura;
    }
    
    /**
     * Método que devuelve la figura
     * @return figura objeto de tipo Figura que representa la figura
     */
    public Figura getFiguraShape(){
        return this.figura;
    }
    
    /**
     * Método que modificar si el relleno esta activo o no
     * @param relleno booleano que determina si el rellano está activo o no
     */
    public void setRelleno(boolean relleno){
        /*Si estamos editando solo cambiaremos relleno de la figura seleccionada*/
        if(editar==true){
            figura_selec.setRelleno(relleno);
            this.repaint();
        }else this.relleno = relleno;
            
    }
    
    /**
     * Método que devuelve si el relleno está activo o no
     * @return relleno booleano que determina si el rellenos está activo o no 
     */
    public boolean getRelleno(){
        return relleno;
    }
    
    /**
     * Método que modifica si el editar está activo o no
     * @param editar booleano que deterina si el editado está activo o no
     */
    public void setEditar(boolean editar){
        this.editar=editar;
    }
    
    /**
     * Método que devuelve si el editar está activo o no
     * @return editar booleano que determina si el editado está activo o no
     */
    public boolean getEditar(){
        return editar;
    }

    /**
     * Método que devuelve el color del trazo está activo o no
     * @return actiColorTr booleano que determina si el color del trazo está activo o no
     */
    public boolean getActiColorTr() {
        return this.actiColorTr;
    }

    /**
     * Método que modifica si el color del trazo está activo o no
     * @param actiColorTr booleano que determina si el color del trazo está activo o no
     */
    public void setActiColorTr(boolean actiColorTr) {
        this.actiColorTr = actiColorTr;
    }

    /**
     * Método que devuelve si el color de relleno está activo o no
     * @return actiColorFill booleano que determina si el color de rellenos esta activo o no
     */
    public boolean getActiColorFill() {
        return this.actiColorFill;
    }

    /**
     * Método que modifica si el color de relleno está activo o no
     * @param actiColorFill booleano que determina si el color de rellenos esta activo o no
     */
    public void setActiColorFill(boolean actiColorFill) {
        this.actiColorFill = actiColorFill;
    }

    /**
     * Método que devuelve el color de relleno
     * @return colorRelleno objeto de tipo Color  
     */
    public Color getColorRelleno() {
        return this.colorRelleno;
    }

    /**
     * Metodo que modificar el color de relleno
     * @param colorRelleno objeto de tipo Color que representa el nuevo color
     */
    public void setColorRelleno(Color colorRelleno) {
        if(editar==true){
            figura_selec.setColorRelleno(colorRelleno);
        }else{
            this.colorRelleno = colorRelleno;
        }
    }

    /**
     * Método que devuelve el color del trazo
     * @return colorTrazo objeto de tipo color que representa el color del trazo
     */
    public Color getColorTrazo() {
        return this.colorTrazo;
    }

    /**
     * Método que modifica el color del trazo
     * @param colorTrazo objeto de tipo color que representa el nuevo color del trazo 
     */
    public void setColorTrazo(Color colorTrazo) {
        if(editar==true){
            figura_selec.setColorTrazo(colorTrazo);
        }else{
            this.colorTrazo = colorTrazo;
        }
    }
    
    /**
     * Método que modifica la transparencia
     * @param comp objeto de tipo Composite que representa la nueva transparencia
     */
    public void setComp(Composite comp){
        if(editar==true ){     
            figura_selec.setComp(comp);
            this.repaint();           
        }else this.comp = comp;        
    }
    
    /**
     * Método que devuelve la transparencia
     * @return comp objeto de tipo Composite que representa la transparencia
     */
    public Composite getComp(){
        return comp;
    }

    /**
     * Método que devuelve el grosor
     * @return grosor objeto de tipo float que determina el grosor
     */
    public float getGrosor() {
        return grosor;
    }

    /**
     * Método que devuelve el stroke
     * @return sk objeto de tipo Stroke que determina el tipo detrazo y el grosor  
     */
    public Stroke getSk() {
        return sk;
    }
    /**
     * Método que devuelve el stroke
     * @param sk objeto de tipo Stroke que determina el tipo detrazo y el grosor  
     */
    public void setSk(Stroke sk) {
        this.sk = sk;
    }
    
    /**
     * Método que modifica el grosor
     * @param grosor objeto de tipo float que determina el nuevo grosor
     */
    public void setGrosor(int grosor) {
        if(editar==true){
            this.grosor = grosor;
            this.figura_selec.setGrosor(grosor);
            this.trazo(figura_selec.getTrazo());
        }else{
            this.grosor=grosor;
        }
    }
    
    /**
     * Método que modifica si el trazo discontinuo está activo o no
     * @param trazo boolean que determina si el trazo discontinuo está activo o no
     */
    public void trazo(boolean trazo){
        if(editar==true){
            this.figura_selec.setTrazo(trazo);
        }else this.trazo=trazo;
                   
    }
    
    /**
    * Método que modifica el alisado 
    * @param render objeto de tipo RenderingHints que determina el alisado
    */
    public void setRenderingHints(RenderingHints render){
        this.render = render;       
    }
    
    /**
    * Método que devuelve el alisado
    * @return render objeto de tipo RenderingHints que determina el alisado
    */
    public RenderingHints getRenderingHints(){
        return render;
    }
    
    /**
     * Método que modifica si el renderizado está activo o no
     * @param rend boolean que determina si el renderizado está activo o no
     */
    public void renderizado(boolean rend){
        if(rend==true){
            this.render=new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            figura_selec.setRender(render);
            alisado=true;
        }else{
            this.render=new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF); 
            figura_selec.setRender(render);
            alisado=false;
        }
    }

    /**
     * Método que devuelve si el degradado horizontal está activo o no
     * @return degradado_hz boolean que determina si el degradado horizontal está activo o no
     */
    public boolean getDegradadoHz() {
        return degradado_hz;
    }

    /**
     * Método que modifica si el degradado horizontal está activo o no
     * @param degradado_hz boolean que determina si el degradado horizontal está activo o no
     */
    public void setDegradadoHz(boolean degradado_hz) {
        if(editar){ 
            figura_selec.setDegradadoHz(degradado_hz);
            this.repaint();
        }else this.degradado_hz=degradado_hz;
    }

    /**
     * Método que devuelve si el degradado vertical está activo o no
     * @return boolean que determina si el degradado vertical está activo o no
     */
    public boolean getDegradadov() {
        return degradado_v;
    }

    /**
     * Método que modifica si el degradado vertical está activo o no
     * @param degradado_v booleano que indica si la opción de degradado vertical esta seleccioanda
     */
    public void setDegradadoV(boolean degradado_v) {
        if(editar){ 
            figura_selec.setDegradadoHz(degradado_hz);
            this.repaint();
        }else this.degradado_v=degradado_v;
    }
    
    /**
     * Método que modifica el valor de la transparencia
     * @param valor objeto de tipo flotante que depetermina el valor de la transparencia obetenido de un slider
     */ 
    public void setTransparencia(float valor){
        this.comp=AlphaComposite.getInstance(AlphaComposite.SRC_OVER, valor);
        figura_selec.setComp(comp);
        this.repaint();
        
    }
    
    /**
    * Método para devolver qué figura se ha seleccionado
    * 
    * @param p es el punto donde se ha seleccionado
    * @return figura figura que se ha seleccionado
    */   
    public Figura getSelectedShape(Point2D p){   
        for(Figura s:vFigura){     
        
            if(s.getFigura() instanceof MiLinea2D){               
                if(((MiLinea2D) s.getFigura()).contains(p)){                   
                    return s;
                }
                
            }else if(s.getFigura().contains(p)){
                return s;
            }   
        }
        return null; 
    }
    
    /**
     * Método que crea las distintas figuras
     * @param p_ini objeto de tipo Point2D que determina el punto de inicio para la creación de las figuras
     * @return figura devuelve el objeto de la clase figura
     */
    public Figura createShape(Point2D p_ini){
            
            Shape fig=null;
            switch(objeto){
                case 1:
                       
                       figura= new Punto(p_ini, this.colorTrazo, this.grosor, this.trazo, this.relleno, this.comp, this.alisado, this.render);
                    
                    
                break;
                case 2:
                       figura= new Linea(p_ini,p_ini, this.colorTrazo, this.grosor, this.trazo, this.relleno, this.comp, this.alisado, this.render);
                       
                break;
                case 3:
                       figura=new Rectangulo(p_ini, p_ini, this.colorTrazo,this.colorRelleno, this.grosor,this.trazo,this.relleno, this.comp, this.alisado, this.render,this.degradado_hz, this.degradado_v);
                       
                       
                
                break;
                case 4:
                        figura= new Elipse(p_ini,p_ini, this.colorTrazo,this.colorRelleno, this.grosor,this.trazo, this.relleno, this.comp, this.alisado, this.render, this.degradado_hz, this.degradado_v);
                       
                    
                break;
                case 5:
                       figura= new Curva(this.p_ini_curva,this.p_fin_curva,this.p_fin_curva, this.colorTrazo, this.grosor,this.trazo, this.relleno, this.comp, this.alisado, this.render);              
                       //curva=true;
                break;
                case 6:
                    figura=new RoundRectangulo(p_ini, p_ini, this.colorTrazo,this.colorRelleno, this.grosor, this.trazo,this.relleno, this.comp, this.alisado, this.render,this.degradado_hz, this.degradado_v);
                break;
                    
                case 7:
                    figura=new Arco(p_ini, p_ini, this.colorTrazo, this.grosor,this.trazo, this.relleno, this.comp, this.alisado, this.render);
                break;
                    
                case 8:
                    figura=new Poligono(this.poliX,this.poliY, this.colorTrazo,this.colorRelleno, this.grosor,this.trazo,this.relleno, this.comp, this.alisado, this.render,this.degradado_hz, this.degradado_v);
                break;
                
            }
         
           return figura; 
    }
    
    /**
     * Método para crear dinámicamente las figuras
     * @param p objeto de tipo Point2D que representa el punto final de la figura
     */
    private void updateShape(Point2D p){
       Shape fig=null;
        switch(objeto){
                case 2:
                    ((Linea)figura).setLinea(p);
                       
                break;
                case 3:
                    ((Rectangulo)figura).setRectangulo(p_ini,p);
                      
                       
                
                break;
                case 4:
                       ((Elipse)figura).setElipse(p_ini, p);
                    
                break;
                case 5:
                       
                       ((Curva)figura).setCurva(this.p_ini_curva,p, this.p_fin_curva);
                       //curva=true;
                break;
                case 6:
                    ((RoundRectangulo)figura).setRoundRectangulo(p_ini,p);
                break;
                case 7:
                    ((Arco)figura).setArco(p_ini,p);
                break;
        }
      
    }
    
    /**
     * Método para modificar la posición una figura
     * @param fig figura que se va modificar su posición
     * @param pos el punto a donde se va a mover la figura
     */
    private void setLocation(Figura fig, Point2D pos){
        
        if(fig!=null){
            if(fig.getFigura() instanceof Line2D){

                double dx = pos.getX()-((Line2D)fig.getFigura()).getX1();
                double dy = pos.getY()-((Line2D)fig.getFigura()).getY1();
                Point2D newPoint = new Point2D.Double(((Line2D)fig.getFigura()).getX2()+dx,((Line2D)fig.getFigura()).getY2()+dy);
                ((Line2D)fig.getFigura()).setLine(pos, newPoint);

            }else if(fig.getFigura() instanceof Rectangle2D){
                double dx = pos.getX()-((Rectangle2D)fig.getFigura()).getMinX();
                double dy = pos.getY()-((Rectangle2D)fig.getFigura()).getMinY();
                Point2D newPoint = new Point2D.Double(((Rectangle2D)fig.getFigura()).getMaxX()+dx,((Rectangle2D)fig.getFigura()).getMaxY()+dy);
                ((Rectangle2D)fig.getFigura()).setFrameFromDiagonal(pos, newPoint);
           
            }else if(fig.getFigura() instanceof RoundRectangle2D){
                double dx = pos.getX()-((RoundRectangle2D)fig.getFigura()).getMinX();
                double dy = pos.getY()-((RoundRectangle2D)fig.getFigura()).getMinY();
                Point2D newPoint = new Point2D.Double(((RoundRectangle2D)fig.getFigura()).getMaxX()+dx,((RoundRectangle2D)fig.getFigura()).getMaxY()+dy);
                ((RoundRectangle2D)fig.getFigura()).setFrameFromDiagonal(pos, newPoint);

            }else if(fig.getFigura() instanceof Arc2D){
                double dx = pos.getX()-((Arc2D)fig.getFigura()).getMinX();
                double dy = pos.getY()-((Arc2D)fig.getFigura()).getMinY();
                Point2D newPoint = new Point2D.Double(((Arc2D)fig.getFigura()).getMaxX()+dx,((Arc2D)fig.getFigura()).getMaxY()+dy);
                ((Arc2D)fig.getFigura()).setFrameFromDiagonal(pos, newPoint);
            
            }else if(fig.getFigura() instanceof Ellipse2D){
                double dx = pos.getX()-((Ellipse2D)fig.getFigura()).getMinX();
                double dy = pos.getY()-((Ellipse2D)fig.getFigura()).getMinY();
                Point2D newPoint = new Point2D.Double(((Ellipse2D)fig.getFigura()).getMaxX()+dx,((Ellipse2D)fig.getFigura()).getMaxY()+dy);
                ((Ellipse2D)fig.getFigura()).setFrameFromDiagonal(pos, newPoint);
            
            }else if(fig.getFigura() instanceof Polygon){
                
                if(this.bloqueo==false){
                    this.bloqueo=true;
                    this.coord=pos;
                }else{
                    this.coordFin=pos;
                    ((Polygon)fig.getFigura()).translate((int) (coordFin.getX() - coord.getX()), (int) (coordFin.getY() - coord.getY())); 
                    coord.setLocation(coordFin.getX(), coordFin.getY());
                }
                
            }else if(fig.getFigura() instanceof QuadCurve2D){
                double dx = pos.getX()-((QuadCurve2D)fig.getFigura()).getX1();
                double dy = pos.getY()-((QuadCurve2D)fig.getFigura()).getY1();
                Point2D newPoint = new Point2D.Double(((QuadCurve2D)fig.getFigura()).getX2()+dx,((QuadCurve2D)fig.getFigura()).getY2()+dy);
                double cx = pos.getX()-((QuadCurve2D)fig.getFigura()).getX1();
                double cy = pos.getY()-((QuadCurve2D)fig.getFigura()).getY1();
                Point2D newPointControl = new Point2D.Double(((QuadCurve2D)fig.getFigura()).getCtrlX()+cx,((QuadCurve2D)fig.getFigura()).getCtrlY()+cy);
                ((QuadCurve2D)fig.getFigura()).setCurve(pos, newPointControl, newPoint);
            }
        }
    }
    
    /**
     * Método para pintar figuras en el lienzo
     * @param g objeto de tipo Graphics donde se va a pintar
     */
    @Override
    public void paint(Graphics g){   
        
        super.paint(g);   
        Graphics2D g2d = (Graphics2D)g;   
 
        if(areaDibujo!=null){
            g2d.setClip(areaDibujo);
        }
        for(Figura s:vFigura) {     
            
                if(s.getTrazo()){
                  float patronDiscontinuidad[] = {15.0f, 15.0f};
                  this.sk=new BasicStroke(s.getGrosor(),BasicStroke.CAP_ROUND,BasicStroke.JOIN_MITER, 1.0f,patronDiscontinuidad, 0.0f);  
                }else{
                   this.sk=new BasicStroke(s.getGrosor()); 
                }
                g2d.setStroke(this.sk);              
                
            if(s.getRelleno()){
                //g2d.draw(s.getFigura()); 
                if(s.getDegradadoHz() && (s.getFigura() instanceof Rectangle2D)){                    
                    g2d.setPaint(((Rectangulo)s).getPaintRectanguloH());
                
                }else if(s.getDegradadoV() && (s.getFigura() instanceof Rectangle2D)){
                    g2d.setPaint(((Rectangulo)s).getPaintRectanguloV());  
                    
                }else if(s.getDegradadoHz() && (s.getFigura() instanceof Ellipse2D)){
                    g2d.setPaint(((Elipse)s).getPaintElipseH()); 
                 
                }else if(s.getDegradadoV() && (s.getFigura() instanceof Ellipse2D)){
                    g2d.setPaint(((Elipse)s).getPaintElipseV()); 
                    
                }else if(s.getDegradadoHz() && (s.getFigura() instanceof RoundRectangle2D)){
                    g2d.setPaint(((RoundRectangulo)s).getPaintRoundRectanguloH()); 
                 
                }else if(s.getDegradadoV() && (s.getFigura() instanceof RoundRectangle2D)){
                    g2d.setPaint(((RoundRectangulo)s).getPaintRoundRectanguloV()); 
                    
                }else if(s.getDegradadoHz() && (s.getFigura() instanceof Polygon)){
                    g2d.setPaint(new GradientPaint((int)((Polygon)s.getFigura()).xpoints[0],(int)((Polygon)s.getFigura()).ypoints[0],s.getColorRelleno(),(int)((Polygon)s.getFigura()).xpoints[2],(int)((Polygon)s.getFigura()).ypoints[2],s.getColorTrazo()));
                
                }else if(s.getDegradadoV() && (s.getFigura() instanceof Polygon)){
                    g2d.setPaint(new GradientPaint((int)((Polygon)s.getFigura()).xpoints[2],(int)((Polygon)s.getFigura()).ypoints[2],s.getColorRelleno(),(int)((Polygon)s.getFigura()).xpoints[0],(int)((Polygon)s.getFigura()).ypoints[0],s.getColorTrazo()));    
                }else{
                   g2d.setPaint(s.getColorRelleno()); 
                }
                
                
                g2d.setComposite(s.getComp());               
                g2d.fill(s.getFigura());
                g2d.setPaint(s.getColorTrazo());
                g2d.draw(s.getFigura());
            }else{
                 
                
                g2d.setPaint(s.getColorTrazo());
                
                g2d.setComposite(s.getComp());
                g2d.setRenderingHints(s.getRender()); 
                g2d.draw(s.getFigura());
                
                
                                 
            } 
            if(this.editar==true){
                //Rectangle2D boundingBox= s.getFigura().getBounds2D();
                float patronDiscontinuidad[] = {15.0f, 15.0f};
                g2d.setStroke(new BasicStroke(2,BasicStroke.CAP_ROUND,BasicStroke.JOIN_MITER, 1.0f,patronDiscontinuidad, 0.0f));
                g2d.setPaint(Color.red);
                g2d.draw(figura_selec.getFigura().getBounds2D());
                
            }
        } 
    }
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setBackground(new java.awt.Color(255, 255, 255));
        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                formMouseDragged(evt);
            }
        });
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                formMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                formMouseReleased(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    /**
     * Detecta caundo se presiona el botón del ratón
     * @param evt acción de presionar el botón del ratón
     */
    private void formMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMousePressed
        this.p_ini= evt.getPoint();
                   
        if((this.editar==false)&&(this.objeto!=8)&&(this.objeto!=5)){
            this.figura=this.createShape(p_ini);           
            vFigura.add(figura);
         
        }else if((this.curva==false)&&(this.objeto==5)&&(this.editar==false)){
            this.p_ini_curva=evt.getPoint();
            
            //this.curva=true;
            
        }else if((this.curva==true)&&(this.objeto==5)&&(this.editar==false)){
 
            this.p_fin_curva=evt.getPoint();
            this.figura=this.createShape(p_fin_curva);
            vFigura.add(figura);
 
            //curva=false;
            
        }else if((this.dobleclic==false)&&(this.editar==false)&&(this.objeto==8)){
            this.dobleclic=true;
            this.figura = this.createShape(p_ini);
            vFigura.add(figura);
            
            
            ((Polygon)figura.getFigura()).addPoint(evt.getPoint().x, evt.getPoint().y);
            
            
        }else if((this.dobleclic==true)&&(this.editar==false)&&(this.objeto==8)){
            ((Polygon)figura.getFigura()).addPoint(evt.getPoint().x, evt.getPoint().y);
            
        }else{
            figura_selec=this.getSelectedShape(evt.getPoint());
        }
        
    }//GEN-LAST:event_formMousePressed
    /**
     * Detecta cuando se mueve el ratón 
     * @param evt acción de mover el ratón
     */
    private void formMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseDragged
           
            this.p_fin=evt.getPoint();
            
            if((this.editar==false)&&(this.objeto!=5)){
                if(this.objeto !=1){
                    this.updateShape(p_fin);
                }
            }else if((this.editar==false)&&(this.objeto==5)&&(curva==true)){
                this.p_control_curva=evt.getPoint();
                this.updateShape(p_control_curva);
                
           /*}else if((this.curva==false)&&(this.objeto==5)){
                this.updateCurve(evt.getPoint());*/
                                  
            }else{
                if(figura_selec!=null){
                    this.setLocation(figura_selec, evt.getPoint());
                }
            }
            
        
        this.repaint();
    }//GEN-LAST:event_formMouseDragged
    /**
     * Detecta cuando se suelta el botón del ratón
     * @param evt acción de soltar el botón del ratón
     */
    private void formMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseReleased
        this.p_fin=evt.getPoint();
            
            if((this.editar==false)&&(this.objeto!=5)){
                if(this.objeto !=1){
                    this.updateShape(p_fin);
                }
            }else if((this.editar==false)&&(this.objeto==5)&&(this.curva==false)){
                this.curva=true;
            }else if((this.editar==false)&&(this.objeto==5)&&(this.curva==true)){
                this.p_control_curva=evt.getPoint();
                this.updateShape(p_control_curva);
                this.curva=false;
                this.p_ini_curva=null;
                this.p_fin_curva=null;
                this.p_control_curva=null;
                
           /*}else if((this.curva==false)&&(this.objeto==5)){
                this.updateCurve(evt.getPoint());*/
                                  
            }else{
                this.setLocation(figura_selec, evt.getPoint());
            }
            
        
        this.repaint();
       //this.formMouseDragged(evt);
    }//GEN-LAST:event_formMouseReleased
    /**
     * Detecta los click de ratón
     * @param evt acción de cuando se presiona el botón del ratón y se suelta
     */
    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        if (evt.getClickCount() == 2) {
            System.out.println("Se ha producido un doble click");
            this.dobleclic=false;
        }
    }//GEN-LAST:event_formMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
