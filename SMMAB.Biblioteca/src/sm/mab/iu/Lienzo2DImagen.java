/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sm.mab.iu;
import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.Kernel;
import sm.graficos.Elipse;
import sm.graficos.Figura;
import sm.graficos.Rectangulo;
import sm.graficos.RoundRectangulo;
import sm.mab.iu.Lienzo2D;
/**
 *
 * @author Manuel Alonso Braojos
 */
public class Lienzo2DImagen extends Lienzo2D {

    private BufferedImage img;
    private boolean dibujo=false;
    private Lienzo2D lienzo;
    private BasicStroke trazo=null;
    
    /**
     * Constructor por defecto de la clase Lienzo2DImagen
     */
    public Lienzo2DImagen() {
        initComponents();
    }
    
    /**
     * Método para modificar la imagen
     * @param img nueva imagen
     */
    public void setImage(BufferedImage img){   
        this.img = img; 
        if(img!=null) {     
            setPreferredSize(new Dimension(img.getWidth(),img.getHeight()));
            super.setAreaDibujo(new Rectangle2D.Double(0.0,0.0, img.getWidth(), img.getHeight()));
        } 
    }
    
    /**
     * Método que devuelve la imagen en el lienzo
     * @return img imagen en el lienzo
     */
    public BufferedImage getImage(){   
        return img; 
    }
    
    /**
     * 
     * @param drawVector devuelve si hay un vector de figuras en la imagen
     * @return img devuelve la imagen
     */
    public BufferedImage getImage(boolean drawVector){
        if (drawVector) {
            Graphics2D graphics = img.createGraphics();
            //graphics.setPaint(super.getColorTrazo());
            graphics.setStroke(super.getSk());
            
            
            for(Figura s:super.getvFigura()){
                if(s.getRelleno()){
                    if(s.getDegradadoHz() && (s.getFigura() instanceof Rectangle2D)){                    
                    graphics.setPaint(((Rectangulo)s).getPaintRectanguloH());
                
                    }else if(s.getDegradadoV() && (s.getFigura() instanceof Rectangle2D)){
                        graphics.setPaint(((Rectangulo)s).getPaintRectanguloV());  

                    }else if(s.getDegradadoHz() && (s.getFigura() instanceof Ellipse2D)){
                        graphics.setPaint(((Elipse)s).getPaintElipseH()); 

                    }else if(s.getDegradadoV() && (s.getFigura() instanceof Ellipse2D)){
                        graphics.setPaint(((Elipse)s).getPaintElipseV()); 

                    }else if(s.getDegradadoHz() && (s.getFigura() instanceof RoundRectangle2D)){
                        graphics.setPaint(((RoundRectangulo)s).getPaintRoundRectanguloH()); 

                    }else if(s.getDegradadoV() && (s.getFigura() instanceof RoundRectangle2D)){
                        graphics.setPaint(((RoundRectangulo)s).getPaintRoundRectanguloV());    
                    }else{
                       graphics.setPaint(s.getColorRelleno()); 
                    }
                    graphics.setComposite(s.getComp());
                    graphics.fill(s.getFigura());
                    graphics.setPaint(s.getColorTrazo());
                    graphics.draw(s.getFigura());
                }else
                    graphics.setPaint(s.getColorTrazo());
                    graphics.setComposite(s.getComp());
                    graphics.setRenderingHints(s.getRender()); 
                    graphics.draw(s.getFigura());
            }
        }else 
            return getImage(); 
        
        return img;
        
    } 
    
    /**
     * 
     * @return dibujo devuelve el dibujo 
     */
    public boolean getDibujo() {
        return dibujo;
    }
    
    /**
     * Método para pintar imagen y figuras
     * @param g objeto Graphics que se va a pintar
     */
    @Override
    public void paintComponent(Graphics g){   
        super.paintComponent(g);  
        Graphics2D g2d = (Graphics2D)g;
        if(img!=null){ 
            g2d.drawImage(img,0,0,this);
            this.dibujo=true;
            
            float patronDisc[]= { 3.0f,3.0f};
            trazo= new BasicStroke(1.0f, 0, 2, 1.0f, patronDisc,0.0f);
            g2d.setStroke(trazo);
            g2d.draw(getAreaDibujo());
            
        }
        
    }
    
    /**
     * Método que devuelve la coordenada del ratón
     * @return punto coordenada del ratón
     */
    public Point coordRaton(){
        
        Point punto=null;
        punto=MouseInfo.getPointerInfo().getLocation();
        return punto;
    }
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

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


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

    
}
