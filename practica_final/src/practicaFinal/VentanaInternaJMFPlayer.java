/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practicaFinal;

import java.awt.Component;
import java.io.File;
import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.Player;


/**
 * Crea una ventana interna de video
 * @author Manuel Alonso Braojos
 */
public class VentanaInternaJMFPlayer extends javax.swing.JInternalFrame {

    
    private Player player = null;
    
    /**
     * Constructor por parámetros de la clase VentanaInternaJMFPlayer
     * @param f archivo de tipo File 
     */
    private VentanaInternaJMFPlayer(File f) {
        initComponents();
        String sfichero = "file:" + f.getAbsolutePath();
        MediaLocator ml = new MediaLocator(sfichero);
        try {
            player = Manager.createRealizedPlayer(ml);
            Component vc = player.getVisualComponent();
            if(vc!=null)add(vc, java.awt.BorderLayout.CENTER);
            Component cpc = player.getControlPanelComponent();
            if(cpc!=null)add(cpc, java.awt.BorderLayout.SOUTH);
            this.pack();
        }catch(Exception e) {
            System.err.println("VentanaInternaJMFPlayer: "+e);
            player = null;
        }
    }
    
    /**
     * Método que llama al constructor
     * @param f archivo de tipo File
     * @return devuelve la ventana nueva
     */
    public static VentanaInternaJMFPlayer getInstance(File f){
        VentanaInternaJMFPlayer v = new VentanaInternaJMFPlayer(f);
        if(v.player!=null){
            return v;
        }else{
            return null;
        }
    }

    /**
     * Método que devuelver el player
     * 
     * @return player objeto player
     */
    public Player getPlayer() {
        return player;
    }

    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameClosing(evt);
            }
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
            }
        });

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosing
        this.close();
    }//GEN-LAST:event_formInternalFrameClosing
    
    /**
     * Método para activar la cámara
     */
    public void play() {
        if (player != null) {
            try {
                player.start();
            } catch (Exception e) {
                System.err.println("VentanaInternaJMFPlayer: "+e);
            }   
        }         
    }
    
    /**
     * Método para cerrar la cámara
     */
    public void close(){
        if (player != null) {
            try {
                player.close();
            } catch (Exception e) {
                System.err.println("VentanaInternaJMFPlayer: "+e);
            }   
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
