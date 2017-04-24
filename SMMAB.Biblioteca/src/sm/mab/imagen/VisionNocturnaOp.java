/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sm.mab.imagen;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.util.Random;
import sm.image.BufferedImageOpAdapter;
import sm.image.TintOp;

/**
 * Aplica a una imagen el efecto visión nocturna (operación propia) 
 * @author Manuel Alonso Braojos
 */
public class VisionNocturnaOp extends BufferedImageOpAdapter  {

    /**
     * Constructor por defecto de la claseSepiaOp
     */
    public VisionNocturnaOp() {
       
    } 

    /**
    * Método para aplicar un filtro a una imagen
    * 
    * @param imgSrc imagen original
    * @param imgDest imagen de destino
    * 
    * @return dest imagen de destino
    */
    @Override
    public BufferedImage filter(BufferedImage imgSrc, BufferedImage imgDest) {
        
        Color color= new Color(255,0,254);
        TintOp tinta= new TintOp(color, (float) 0.5);
        imgDest = tinta.filter(imgSrc, null);//filter(imgSrc, imgSrc);
        
        int r,g,b;
                
            for(int i=0;i<imgDest.getWidth();i++){
                for(int j=0;j<imgDest.getHeight();j++){
                    //se obtiene el color del pixel
                    Color color2 = new Color(imgDest.getRGB(i, j));
                    //se extraen los valores RGB
                    r = color2.getRed();
                    g = color2.getGreen();
                    b = color2.getBlue();
                    //se coloca en la nueva imagen con los valores invertidos
                    imgDest.setRGB(i, j, new Color(255-r,255-g,255-b).getRGB());                                                                    
                }
            }
          
        return imgDest;
    }
    
}
