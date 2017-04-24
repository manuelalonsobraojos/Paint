/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sm.mab.imagen;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import sm.image.BufferedImageOpAdapter;
import sm.image.BufferedImageSampleIterator;

/**
 * Aplica umbralización sobre una imagen, el umbral se modificará desde 0 hasta 255
 * @author Manuel Alonso Braojos
 */
public class UmbralizacionOp extends BufferedImageOpAdapter{

    private int umbral;    
    
    /**
     * Constructor por parámetros de la clase UmbralizacionOp
     * @param umbral Valor de umbralización
     */
    public UmbralizacionOp(int umbral) {
        this.umbral = umbral;   
     }
    /**
     * 
     * @param src imagen original
     * @param dest imagen de destino
     * @return dest imagen de destino
     */
    @Override
    public BufferedImage filter(BufferedImage src, BufferedImage dest) {
        
        //Comprobamos que la imagen destino es null, en caso de que lo sea crearemos una imagen destino compatible
        if(dest==null){
            dest = createCompatibleDestImage(src, null);
        }
        WritableRaster wr= dest.getRaster();
        
        for (BufferedImageSampleIterator it = new BufferedImageSampleIterator(src); it.hasNext();) { 
            BufferedImageSampleIterator.SampleData sample = it.next() ; 
            
            if(sample.value >= umbral){ // Se comprueba que el valor RGB sea mayor que el umbral
                sample.value = 255;
            
            }else{
                sample.value = 0;
            }
            wr.setSample(sample.col, sample.row, sample.band, sample.value); 
        }
        return dest;
    }
    
}
