/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sm.mab.imagen;

import java.awt.image.BufferedImage;
import sm.image.BinaryOp;

/**
 * Realiza la multiplicación binaria de los pixeles de dos imágenes 
 * @author Manuel Alonso Braojos
 */
public class multiplicacionOp extends BinaryOp {
 
    /**
     * 
     * @param img imagen original
     */
    public multiplicacionOp(BufferedImage img) {
        super(img);
    }
    
    /**
     * 
     * @param v1 valor 1
     * @param v2 valor 2
     * @return producto entre los dos valores
     */
    public int binaryOp(int v1, int v2){
        int producto = v1*v2;
         
        if(producto<0){
            producto = 0;
        }
        else if(producto>255){
            producto = producto/255;
        }
        return producto;
    }
    
    
    
}
