/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sm.mab.imagen;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import sm.image.BufferedImageOpAdapter;
import sm.image.BufferedImagePixelIterator;

/**
 * Aplica a una imagen el filtro Sepia
 * @author Manuel Alonso Braojos
 */
public class SepiaOp extends BufferedImageOpAdapter {
    
    /**
     * Constructor por defecto de la claseSepiaOp
    */
    public SepiaOp () {    
    }
    
    /**
    * Método para aplicar un filtro a una imagen
    * 
    * @param src imagen original
    * @param dest imagen de destino
    * 
    * @return dest imagen de destino
    */
    public BufferedImage filter(BufferedImage src, BufferedImage dest){ 
        if (src == null) { 
            throw new NullPointerException("src image is null"); 
        }
        if (dest == null) {
            dest = createCompatibleDestImage(src, null);
        }
        float rgb[]=new float[3];
        float destA[]=new float[3];
        BufferedImagePixelIterator.PixelData pixel;
        WritableRaster raster= dest.getRaster();
         
        for(BufferedImagePixelIterator it=new BufferedImagePixelIterator(src); it.hasNext();) { 
            pixel = it.next();
            rgb[0]=pixel.sample[0];
            rgb[1]=pixel.sample[1];
            rgb[2]=pixel.sample[1];
            
            destA[0]= (float) (rgb[0] * 0.393 + rgb[1] * 0.769 + rgb[2] * 0.189);
            destA[1]= (float) (rgb[0] * 0.349 + rgb[1] * 0.686 + rgb[2] * 0.168);
            destA[2]= (float) (rgb[0] * 0.272 + rgb[1] * 0.534 + rgb[2] * 0.131);
            
            if(destA[0] > 255.0F){
                pixel.sample[0] = (int) 255;
            }else{
                pixel.sample[0] = (int) destA[0];
            }
            
            if(destA[1] > 255.0F){
                pixel.sample[1] = (int) 255;
            }else{
                pixel.sample[1] = (int) destA[1];
            }  
            if(destA[2] > 255.0F){
                pixel.sample[2] = (int) 255;
            }else{
                pixel.sample[2] = (int) destA[2];
            }  
            raster.setPixel(pixel.col, pixel.row, pixel.sample);
        }
        return dest;
    }
}
