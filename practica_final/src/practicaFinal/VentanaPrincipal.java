/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practicaFinal;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Shape;
import java.awt.Window;
import java.awt.color.ColorSpace;
import java.awt.color.ICC_ColorSpace;
import java.awt.color.ICC_Profile;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ByteLookupTable;
import java.awt.image.ColorConvertOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.awt.image.LookupOp;
import java.awt.image.LookupTable;
import java.awt.image.RescaleOp;
import java.io.File;
import javax.imageio.ImageIO;
import javax.media.Buffer;
import javax.media.Player;
import javax.media.control.FrameGrabbingControl;
import javax.media.format.VideoFormat;
import javax.media.util.BufferToImage;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.filechooser.FileNameExtensionFilter;
import sm.image.BlendOp;
import sm.mab.iu.Lienzo2D;
import sm.image.KernelProducer;
import sm.image.LookupTableProducer;
import sm.image.SubtractionOp;
import sm.image.TintOp;
import sm.mab.imagen.VisionNocturnaOp;
import sm.mab.imagen.SepiaOp;
import sm.mab.imagen.UmbralizacionOp;
import sm.mab.imagen.multiplicacionOp;
import sm.sound.SMClipPlayer;
import sm.sound.SMPlayer;
import sm.sound.SMSoundRecorder;
/**
 * Es la ventana principal de la aplicación con la que intactuará el usuario
 * @author Manuel Alonso Braojos
 */
public class VentanaPrincipal extends javax.swing.JFrame {

    private Shape figuraShape;
    private Lienzo2D lienzo2d;
    private BufferedImage imgSource=null;
    private BufferedImage imgRight;
    private BufferedImage imgLeft;
    private boolean foco=false;
    private JInternalFrame vi;   
    private JInternalFrame viNext;
    private Player player=null;
    private VentanaInternaJMFPlayer viJMF;
    private VentanaInternaCamara viC;
    private VentanaInternaReproductor viS;
    private VentanaInternaGrabador viR;
    
    /**
     * Constructo por defecto de la clase VentanaPrincipal
     */
    public VentanaPrincipal() {
        
        initComponents();
        this.setSize(850, 650);
        this.botonPunto.setSelected(true);                
    }

    /**
     * Método que modifica el objeto delecionado
     * @param i entero al que determina el tipo de objeto
     */
    public void setObjeto(int i){
        switch(i){
            case 1:
                this.botonPunto.setSelected(true);
                this.botonLinea.setSelected(false);
                this.botonRectangulo.setSelected(false);
                this.botonCirculo.setSelected(false);
                break;
            case 2:
                this.botonLinea.setSelected(true);
                break;
            case 3:
                this.botonRectangulo.setSelected(true);
                break;
            case 4:
                this.botonCirculo.setSelected(true);
                break;
    
        }
    }
    
    /**
     * Método que modifica si el botón de relleno está activo o no
     * @param rell boolean que determina si el boton de relleno está activo o no
     */
    public void setRelleno(boolean rell){
        this.botonRelleno.setSelected(rell);
    }
    
    /**
     * Método que modifica si el botón de editar está activo o no
     * @param edit boolean que determina si el boton de editar está activo o no
     */
    public void setEditar(boolean edit){
        this.botonEditar.setSelected(edit);
    }
    
    /**
     * Método que modifica si el botón de alisado está activo o no
     * @param alis boolean que determina si el boton de alisado está activo o no
     */
    public void setAlisado(boolean alis){
        this.botonAlisar.setSelected(alis);
    }
    
    /**
     * Método que devuelve el grosor que tiene el spinner
     * @return botonGrosor grosor del spinner
     */
    public JSpinner getGrosor(){
        return this.botonGrosor;
    }

    /**
     * Método que modifica el valor de etiqueta para la posición del ratón en el eje x
     * @param posX entero que determina la posición en el eje x del ratón
     */
    public void setPosX(int posX) {
        this.posX.setText("X: "+String.valueOf(posX));
    }

    /**
     * Método que modifica el valor de etiqueta para la posición del ratón en el eje y
     * @param posY entero que determina la posición en el eje y del ratón
     */
    public void setPosY(int posY) {
        this.posY.setText("Y: "+String.valueOf(posY));
    }
    
    /**
     * Método que modifica la etiqueta que muestra el valor de los componentes RGB del pixel en el que se encuentra el ratón
     * @param color el color de pixel en el que se encuentra el puntero del ratón
     */
    public void setRGB(Color color){
        this.labelRGB.setText("RGB: "+String.valueOf(color).substring(14));
    }
    
    public void setbRelleno(boolean estado){
        this.botonRelleno.setSelected(estado);
    }
    
    /**
     * Método para aplicar la transformacion sinoidal
     * @param w velocidad angular
     * @return lt tabla que contiene los arrays de datos para una o más bandas de la imagen
     */
    public LookupTable seno(double w){
        double k = 255.0; //constante de normalización
        byte by[]= new byte[256];
        
        
        for(int i=0; i<256;i++){
            by[i]=(byte)(k*(Math.abs(Math.sin(w*i))));
            
        }
        ByteLookupTable lt=new ByteLookupTable(0,by) ;
        return lt;
    }
    
    /**
     * Método para aplicar la rotación
     * @param grado grados de rotación que se le va aplicar a la imagen
     * @return atop la rotación que se le va aplicar a la imagen
     */
    public AffineTransformOp rotacion(double grado){
        
        double r = Math.toRadians(grado);  
        Point c = new Point(imgSource.getWidth()/2, imgSource.getHeight()/2); //representa el punto central
        AffineTransform at = AffineTransform.getRotateInstance(r,c.x,c.y);
        AffineTransformOp atop; 
        atop = new AffineTransformOp(at,AffineTransformOp.TYPE_BILINEAR);
        return atop;
    }  
  
    //Función para aplicar escalado
    /**
     * Método para aplicar el escalado
     * @param escala valor de la escala
     * @return atop escalado que se le va aplicar a una imagen
     */
    public AffineTransformOp escalar(double escala){
        
        //double r = Math.toRadians(grado);  
        //Point c = new Point(imgSource.getWidth()/2, imgSource.getHeight()/2); //representa el punto central
        AffineTransform at = AffineTransform.getScaleInstance(escala, escala);
        AffineTransformOp atop = new AffineTransformOp(at,AffineTransformOp.TYPE_BILINEAR);
        return atop;
    }  
    
    /**
     * Método para obtener la extensión de un archivo
     * @param filename nombre del archivo
     * @return devuelve a extensión del archivo
     */
    public String getExtension(String filename) {
            int index = filename.lastIndexOf('.');
            if (index == -1) {
                  return "";
            } else {
                  return filename.substring(index + 1);
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

        buttonGroup1 = new javax.swing.ButtonGroup();
        jFileChooser1 = new javax.swing.JFileChooser();
        buttonGroup2 = new javax.swing.ButtonGroup();
        jPanel8 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        barraGeneral = new javax.swing.JToolBar();
        botonNuevo2 = new javax.swing.JButton();
        botonAbrir2 = new javax.swing.JButton();
        botonGuardar2 = new javax.swing.JButton();
        barraFormas = new javax.swing.JToolBar();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        botonPunto = new javax.swing.JToggleButton();
        botonLinea = new javax.swing.JToggleButton();
        botonRectangulo = new javax.swing.JToggleButton();
        botonCirculo = new javax.swing.JToggleButton();
        botonEditar = new javax.swing.JToggleButton();
        botonCurva = new javax.swing.JToggleButton();
        botonRoundRectangulo = new javax.swing.JToggleButton();
        botonArco = new javax.swing.JToggleButton();
        botonPoligono = new javax.swing.JToggleButton();
        barraAtributos = new javax.swing.JToolBar();
        jSeparator3 = new javax.swing.JToolBar.Separator();
        jPanel5 = new javax.swing.JPanel();
        botonColorTrazo = new javax.swing.JToggleButton();
        botonColorRelleno = new javax.swing.JToggleButton();
        panelColores = new javax.swing.JPanel();
        botonNegro = new javax.swing.JToggleButton();
        botonRojo = new javax.swing.JToggleButton();
        botonAzul = new javax.swing.JToggleButton();
        botonBlanco = new javax.swing.JToggleButton();
        botonAmarillo = new javax.swing.JToggleButton();
        botonVerde = new javax.swing.JToggleButton();
        botonSeleccionColor = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        botonGrosor = new javax.swing.JSpinner();
        botonTrazo = new javax.swing.JToggleButton();
        botonRelleno = new javax.swing.JToggleButton();
        botonAlisar = new javax.swing.JToggleButton();
        botonDegradadoH = new javax.swing.JToggleButton();
        botonDegradadoV = new javax.swing.JToggleButton();
        sliderTransparencia = new javax.swing.JSlider();
        jPanel10 = new javax.swing.JPanel();
        barraAudio = new javax.swing.JToolBar();
        botonPlay = new javax.swing.JButton();
        botonStop = new javax.swing.JButton();
        botonRecord = new javax.swing.JButton();
        botonCamara2 = new javax.swing.JButton();
        botonCaptura2 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        barraEstado = new javax.swing.JLabel();
        posX = new javax.swing.JLabel();
        posY = new javax.swing.JLabel();
        labelRGB = new javax.swing.JLabel();
        barraOperacionesImg = new javax.swing.JToolBar();
        jPanel2 = new javax.swing.JPanel();
        panelBrillo = new javax.swing.JPanel();
        jSlider = new javax.swing.JSlider();
        panelFiltros = new javax.swing.JPanel();
        boxFiltros = new javax.swing.JComboBox();
        panelContraste = new javax.swing.JPanel();
        botonNormal = new javax.swing.JButton();
        botonIluminado = new javax.swing.JButton();
        botonOscurecer = new javax.swing.JButton();
        panelSenoidal = new javax.swing.JPanel();
        botonSenoidal = new javax.swing.JButton();
        botonSepia = new javax.swing.JButton();
        botonNegativo = new javax.swing.JButton();
        botonGrises = new javax.swing.JButton();
        botonDuplicado = new javax.swing.JButton();
        botonTintado = new javax.swing.JButton();
        botonVisionNocturna = new javax.swing.JToggleButton();
        panelRotacion = new javax.swing.JPanel();
        sliderRotacion = new javax.swing.JSlider();
        botonRotacion90 = new javax.swing.JButton();
        botonRotacion180 = new javax.swing.JButton();
        botonRotacion270 = new javax.swing.JButton();
        panelEscalar = new javax.swing.JPanel();
        botonAumentarEscala = new javax.swing.JButton();
        botonDisminEscala = new javax.swing.JButton();
        panelBinario = new javax.swing.JPanel();
        botonSuma = new javax.swing.JButton();
        botonResta = new javax.swing.JButton();
        botonMultiplicacion = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        sliderUmbralizacion = new javax.swing.JSlider();
        sliderBinario = new javax.swing.JSlider();
        panelEscritorio = new javax.swing.JDesktopPane();
        MenuPrincipal = new javax.swing.JMenuBar();
        botonArchivo = new javax.swing.JMenu();
        botonNuevo = new javax.swing.JMenuItem();
        botonAbrir = new javax.swing.JMenuItem();
        botonGuardar = new javax.swing.JMenuItem();
        botonEdicion = new javax.swing.JMenu();
        botonVistaBarraEstado = new javax.swing.JCheckBoxMenuItem();
        botonVistaBarraFormas = new javax.swing.JCheckBoxMenuItem();
        botonBarraOperaciones = new javax.swing.JCheckBoxMenuItem();
        botonBarraAtributos = new javax.swing.JCheckBoxMenuItem();
        botonBarraGeneral = new javax.swing.JCheckBoxMenuItem();
        menuAyuda = new javax.swing.JMenu();
        botonInformacion = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel8.setLayout(new java.awt.BorderLayout());

        jPanel6.setLayout(new javax.swing.BoxLayout(jPanel6, javax.swing.BoxLayout.LINE_AXIS));

        barraGeneral.setFloatable(false);
        barraGeneral.setRollover(true);

        botonNuevo2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/nuevo.png"))); // NOI18N
        botonNuevo2.setToolTipText("Nuevo");
        botonNuevo2.setFocusable(false);
        botonNuevo2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botonNuevo2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        botonNuevo2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonNuevo2ActionPerformed(evt);
            }
        });
        barraGeneral.add(botonNuevo2);

        botonAbrir2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/abrir.png"))); // NOI18N
        botonAbrir2.setToolTipText("Abrir");
        botonAbrir2.setFocusable(false);
        botonAbrir2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botonAbrir2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        botonAbrir2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonAbrir2ActionPerformed(evt);
            }
        });
        barraGeneral.add(botonAbrir2);

        botonGuardar2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/guardar.png"))); // NOI18N
        botonGuardar2.setToolTipText("Guardar");
        botonGuardar2.setFocusable(false);
        botonGuardar2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botonGuardar2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        botonGuardar2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonGuardar2ActionPerformed(evt);
            }
        });
        barraGeneral.add(botonGuardar2);

        jPanel6.add(barraGeneral);

        barraFormas.setBorder(null);
        barraFormas.setFloatable(false);
        barraFormas.setRollover(true);
        barraFormas.add(jSeparator2);

        buttonGroup1.add(botonPunto);
        botonPunto.setIcon(new javax.swing.ImageIcon("C:\\Users\\USUARIO\\OneDrive\\uni\\tercero\\segundo_cuatrimestre\\SMM\\practicas\\practica10\\practica_imagen_P10\\src\\iconos\\punto.png")); // NOI18N
        botonPunto.setToolTipText("Punto");
        botonPunto.setFocusable(false);
        botonPunto.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botonPunto.setOpaque(true);
        botonPunto.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        botonPunto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonPuntoActionPerformed(evt);
            }
        });
        barraFormas.add(botonPunto);

        buttonGroup1.add(botonLinea);
        botonLinea.setIcon(new javax.swing.ImageIcon("C:\\Users\\USUARIO\\OneDrive\\uni\\tercero\\segundo_cuatrimestre\\SMM\\practicas\\practica10\\practica_imagen_P10\\src\\iconos\\linea.png")); // NOI18N
        botonLinea.setToolTipText("Línea");
        botonLinea.setFocusable(false);
        botonLinea.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botonLinea.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        botonLinea.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonLineaActionPerformed(evt);
            }
        });
        barraFormas.add(botonLinea);

        buttonGroup1.add(botonRectangulo);
        botonRectangulo.setIcon(new javax.swing.ImageIcon("C:\\Users\\USUARIO\\OneDrive\\uni\\tercero\\segundo_cuatrimestre\\SMM\\practicas\\practica10\\practica_imagen_P10\\src\\iconos\\rectangulo.png")); // NOI18N
        botonRectangulo.setToolTipText("Rectangulo");
        botonRectangulo.setFocusable(false);
        botonRectangulo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botonRectangulo.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        botonRectangulo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonRectanguloActionPerformed(evt);
            }
        });
        barraFormas.add(botonRectangulo);

        buttonGroup1.add(botonCirculo);
        botonCirculo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/elipse.png"))); // NOI18N
        botonCirculo.setToolTipText("Elipse");
        botonCirculo.setFocusable(false);
        botonCirculo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botonCirculo.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        botonCirculo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonCirculoActionPerformed(evt);
            }
        });
        barraFormas.add(botonCirculo);

        buttonGroup1.add(botonEditar);
        botonEditar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/seleccion.png"))); // NOI18N
        botonEditar.setToolTipText("Editar");
        botonEditar.setFocusable(false);
        botonEditar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botonEditar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        botonEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonEditarActionPerformed(evt);
            }
        });
        barraFormas.add(botonEditar);

        buttonGroup1.add(botonCurva);
        botonCurva.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/curva.png"))); // NOI18N
        botonCurva.setToolTipText("Linea Curva");
        botonCurva.setFocusable(false);
        botonCurva.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botonCurva.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        botonCurva.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonCurvaActionPerformed(evt);
            }
        });
        barraFormas.add(botonCurva);

        buttonGroup1.add(botonRoundRectangulo);
        botonRoundRectangulo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/RoundRectangulo.png"))); // NOI18N
        botonRoundRectangulo.setToolTipText("Rectangulo Redondeado");
        botonRoundRectangulo.setFocusable(false);
        botonRoundRectangulo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botonRoundRectangulo.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        botonRoundRectangulo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonRoundRectanguloActionPerformed(evt);
            }
        });
        barraFormas.add(botonRoundRectangulo);

        buttonGroup1.add(botonArco);
        botonArco.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/arco.png"))); // NOI18N
        botonArco.setToolTipText("Arco");
        botonArco.setFocusable(false);
        botonArco.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botonArco.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        botonArco.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonArcoActionPerformed(evt);
            }
        });
        barraFormas.add(botonArco);

        buttonGroup1.add(botonPoligono);
        botonPoligono.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/poligono.png"))); // NOI18N
        botonPoligono.setToolTipText("Polígono");
        botonPoligono.setFocusable(false);
        botonPoligono.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botonPoligono.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        botonPoligono.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonPoligonoActionPerformed(evt);
            }
        });
        barraFormas.add(botonPoligono);

        jPanel6.add(barraFormas);

        barraAtributos.setFloatable(false);
        barraAtributos.setRollover(true);
        barraAtributos.add(jSeparator3);

        jPanel5.setLayout(new java.awt.GridLayout(1, 0));

        buttonGroup2.add(botonColorTrazo);
        botonColorTrazo.setSelected(true);
        botonColorTrazo.setText("Color 1");
        botonColorTrazo.setToolTipText("Color 1");
        botonColorTrazo.setFocusable(false);
        botonColorTrazo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botonColorTrazo.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        botonColorTrazo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonColorTrazoActionPerformed(evt);
            }
        });
        jPanel5.add(botonColorTrazo);

        buttonGroup2.add(botonColorRelleno);
        botonColorRelleno.setText("Color 2");
        botonColorRelleno.setToolTipText("color 2");
        botonColorRelleno.setFocusable(false);
        botonColorRelleno.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botonColorRelleno.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        botonColorRelleno.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonColorRellenoActionPerformed(evt);
            }
        });
        jPanel5.add(botonColorRelleno);

        barraAtributos.add(jPanel5);

        panelColores.setPreferredSize(new java.awt.Dimension(95, 60));
        panelColores.setLayout(new java.awt.GridLayout(2, 3));

        botonNegro.setBackground(new java.awt.Color(0, 0, 0));
        botonNegro.setSelected(true);
        botonNegro.setToolTipText("Negro");
        botonNegro.setMargin(new java.awt.Insets(2, 2, 2, 2));
        botonNegro.setMaximumSize(new java.awt.Dimension(25, 25));
        botonNegro.setMinimumSize(new java.awt.Dimension(25, 25));
        botonNegro.setPreferredSize(new java.awt.Dimension(24, 24));
        botonNegro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonNegroActionPerformed(evt);
            }
        });
        panelColores.add(botonNegro);

        botonRojo.setBackground(new java.awt.Color(255, 0, 0));
        botonRojo.setToolTipText("Rojo");
        botonRojo.setMargin(new java.awt.Insets(2, 2, 2, 2));
        botonRojo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonRojoActionPerformed(evt);
            }
        });
        panelColores.add(botonRojo);

        botonAzul.setBackground(new java.awt.Color(0, 0, 204));
        botonAzul.setToolTipText("Azul");
        botonAzul.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonAzulActionPerformed(evt);
            }
        });
        panelColores.add(botonAzul);

        botonBlanco.setBackground(new java.awt.Color(255, 255, 255));
        botonBlanco.setToolTipText("Blanco");
        botonBlanco.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonBlancoActionPerformed(evt);
            }
        });
        panelColores.add(botonBlanco);

        botonAmarillo.setBackground(new java.awt.Color(255, 255, 0));
        botonAmarillo.setToolTipText("Amarillo");
        botonAmarillo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonAmarilloActionPerformed(evt);
            }
        });
        panelColores.add(botonAmarillo);

        botonVerde.setBackground(new java.awt.Color(0, 255, 0));
        botonVerde.setToolTipText("Verde");
        botonVerde.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonVerdeActionPerformed(evt);
            }
        });
        panelColores.add(botonVerde);

        barraAtributos.add(panelColores);

        botonSeleccionColor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/paleta.png"))); // NOI18N
        botonSeleccionColor.setToolTipText("Seleccion de color");
        botonSeleccionColor.setFocusable(false);
        botonSeleccionColor.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botonSeleccionColor.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        botonSeleccionColor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonSeleccionColorActionPerformed(evt);
            }
        });
        barraAtributos.add(botonSeleccionColor);

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);
        barraAtributos.add(jSeparator1);

        botonGrosor.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(0), Integer.valueOf(0), null, Integer.valueOf(1)));
        botonGrosor.setToolTipText("Grosor");
        botonGrosor.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        botonGrosor.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                botonGrosorStateChanged(evt);
            }
        });
        barraAtributos.add(botonGrosor);

        botonTrazo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/discontinuidad.png"))); // NOI18N
        botonTrazo.setToolTipText("Trazo Discontinuo");
        botonTrazo.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        botonTrazo.setFocusable(false);
        botonTrazo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botonTrazo.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        botonTrazo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonTrazoActionPerformed(evt);
            }
        });
        barraAtributos.add(botonTrazo);

        botonRelleno.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/rellenar.png"))); // NOI18N
        botonRelleno.setToolTipText("Rellenar");
        botonRelleno.setBorder(null);
        botonRelleno.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        botonRelleno.setFocusable(false);
        botonRelleno.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botonRelleno.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        botonRelleno.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonRellenoActionPerformed(evt);
            }
        });
        barraAtributos.add(botonRelleno);

        botonAlisar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/alisar.png"))); // NOI18N
        botonAlisar.setToolTipText("Alisar");
        botonAlisar.setBorder(null);
        botonAlisar.setFocusable(false);
        botonAlisar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botonAlisar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        botonAlisar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonAlisarActionPerformed(evt);
            }
        });
        barraAtributos.add(botonAlisar);

        botonDegradadoH.setText("degra H");
        botonDegradadoH.setToolTipText("Degradado Horizontal");
        botonDegradadoH.setFocusable(false);
        botonDegradadoH.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botonDegradadoH.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        botonDegradadoH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonDegradadoHActionPerformed(evt);
            }
        });
        barraAtributos.add(botonDegradadoH);

        botonDegradadoV.setText("degra V");
        botonDegradadoV.setToolTipText("degradado Vertical");
        botonDegradadoV.setFocusable(false);
        botonDegradadoV.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botonDegradadoV.setMaximumSize(new java.awt.Dimension(40, 23));
        botonDegradadoV.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        botonDegradadoV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonDegradadoVActionPerformed(evt);
            }
        });
        barraAtributos.add(botonDegradadoV);

        sliderTransparencia.setToolTipText("Transparencia");
        sliderTransparencia.setValue(100);
        sliderTransparencia.setBorder(javax.swing.BorderFactory.createTitledBorder("Transparencia"));
        sliderTransparencia.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                sliderTransparenciaStateChanged(evt);
            }
        });
        barraAtributos.add(sliderTransparencia);

        jPanel6.add(barraAtributos);

        jPanel8.add(jPanel6, java.awt.BorderLayout.PAGE_START);

        jPanel10.setLayout(new javax.swing.BoxLayout(jPanel10, javax.swing.BoxLayout.LINE_AXIS));

        barraAudio.setFloatable(false);
        barraAudio.setRollover(true);

        botonPlay.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/play24x24.png"))); // NOI18N
        botonPlay.setToolTipText("Play");
        botonPlay.setFocusable(false);
        botonPlay.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botonPlay.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        botonPlay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonPlayActionPerformed(evt);
            }
        });
        barraAudio.add(botonPlay);

        botonStop.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/stop24x24.png"))); // NOI18N
        botonStop.setToolTipText("Stop");
        botonStop.setFocusable(false);
        botonStop.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botonStop.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        botonStop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonStopActionPerformed(evt);
            }
        });
        barraAudio.add(botonStop);

        botonRecord.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/record24x24.png"))); // NOI18N
        botonRecord.setToolTipText("Grabar Audio");
        botonRecord.setFocusable(false);
        botonRecord.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botonRecord.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        botonRecord.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonRecordActionPerformed(evt);
            }
        });
        barraAudio.add(botonRecord);

        botonCamara2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/Camara.png"))); // NOI18N
        botonCamara2.setToolTipText("Abrir Cámara");
        botonCamara2.setFocusable(false);
        botonCamara2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botonCamara2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        botonCamara2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonCamara2ActionPerformed(evt);
            }
        });
        barraAudio.add(botonCamara2);

        botonCaptura2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/Capturar.png"))); // NOI18N
        botonCaptura2.setToolTipText("Captura");
        botonCaptura2.setFocusable(false);
        botonCaptura2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botonCaptura2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        botonCaptura2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonCaptura2ActionPerformed(evt);
            }
        });
        barraAudio.add(botonCaptura2);

        jPanel10.add(barraAudio);

        jPanel8.add(jPanel10, java.awt.BorderLayout.PAGE_END);

        getContentPane().add(jPanel8, java.awt.BorderLayout.PAGE_START);

        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel3.setPreferredSize(new java.awt.Dimension(77, 14));
        jPanel3.setLayout(new java.awt.GridLayout(1, 0));

        barraEstado.setText("Barra de Estado");
        jPanel3.add(barraEstado);

        posX.setText("X:");
        posX.setToolTipText("");
        jPanel3.add(posX);

        posY.setText("Y:");
        jPanel3.add(posY);

        labelRGB.setText("RGB:");
        jPanel3.add(labelRGB);

        jPanel1.add(jPanel3, java.awt.BorderLayout.PAGE_END);

        barraOperacionesImg.setRollover(true);
        barraOperacionesImg.setPreferredSize(new java.awt.Dimension(690, 70));

        jPanel2.setLayout(new javax.swing.BoxLayout(jPanel2, javax.swing.BoxLayout.LINE_AXIS));

        panelBrillo.setLayout(new javax.swing.BoxLayout(panelBrillo, javax.swing.BoxLayout.LINE_AXIS));

        jSlider.setMaximum(250);
        jSlider.setMinimum(-250);
        jSlider.setToolTipText("Brillo");
        jSlider.setBorder(javax.swing.BorderFactory.createTitledBorder("Brillo"));
        jSlider.setPreferredSize(new java.awt.Dimension(180, 49));
        jSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSliderStateChanged(evt);
            }
        });
        jSlider.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                brilloFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                brilloFocusLost(evt);
            }
        });
        panelBrillo.add(jSlider);

        jPanel2.add(panelBrillo);

        panelFiltros.setBorder(javax.swing.BorderFactory.createTitledBorder("Filtros"));
        panelFiltros.setLayout(new javax.swing.BoxLayout(panelFiltros, javax.swing.BoxLayout.LINE_AXIS));

        boxFiltros.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Media", "Binomial", "Enfoque", "Relieve", "Laplacaino" }));
        boxFiltros.setToolTipText("Filtros");
        boxFiltros.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boxFiltrosActionPerformed(evt);
            }
        });
        panelFiltros.add(boxFiltros);

        jPanel2.add(panelFiltros);

        panelContraste.setBorder(javax.swing.BorderFactory.createTitledBorder("Contraste"));
        panelContraste.setLayout(new javax.swing.BoxLayout(panelContraste, javax.swing.BoxLayout.LINE_AXIS));

        botonNormal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/contraste.png"))); // NOI18N
        botonNormal.setToolTipText("Contraste");
        botonNormal.setPreferredSize(new java.awt.Dimension(40, 25));
        botonNormal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonNormalActionPerformed(evt);
            }
        });
        panelContraste.add(botonNormal);

        botonIluminado.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/iluminar.png"))); // NOI18N
        botonIluminado.setToolTipText("Iluminar");
        botonIluminado.setPreferredSize(new java.awt.Dimension(40, 25));
        botonIluminado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonIluminadoActionPerformed(evt);
            }
        });
        panelContraste.add(botonIluminado);

        botonOscurecer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/oscurecer.png"))); // NOI18N
        botonOscurecer.setToolTipText("Oscurecer");
        botonOscurecer.setPreferredSize(new java.awt.Dimension(40, 25));
        botonOscurecer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonOscurecerActionPerformed(evt);
            }
        });
        panelContraste.add(botonOscurecer);

        jPanel2.add(panelContraste);

        panelSenoidal.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panelSenoidal.setLayout(new java.awt.GridLayout(2, 3));

        botonSenoidal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/sinusoidal.png"))); // NOI18N
        botonSenoidal.setToolTipText("Operador Sinuidal");
        botonSenoidal.setPreferredSize(new java.awt.Dimension(40, 33));
        botonSenoidal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonSenoidalActionPerformed(evt);
            }
        });
        panelSenoidal.add(botonSenoidal);

        botonSepia.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/sepia.png"))); // NOI18N
        botonSepia.setToolTipText("Sepia");
        botonSepia.setPreferredSize(new java.awt.Dimension(40, 33));
        botonSepia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonSepiaActionPerformed(evt);
            }
        });
        panelSenoidal.add(botonSepia);

        botonNegativo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/negativo.png"))); // NOI18N
        botonNegativo.setToolTipText("Negativo");
        botonNegativo.setPreferredSize(new java.awt.Dimension(40, 33));
        botonNegativo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonNegativoActionPerformed(evt);
            }
        });
        panelSenoidal.add(botonNegativo);

        botonGrises.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/escala-de-grises.png"))); // NOI18N
        botonGrises.setToolTipText("Escala de grises");
        botonGrises.setPreferredSize(new java.awt.Dimension(40, 33));
        botonGrises.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonGrisesActionPerformed(evt);
            }
        });
        panelSenoidal.add(botonGrises);

        botonDuplicado.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/foto-duplicada.png"))); // NOI18N
        botonDuplicado.setToolTipText("Duplicar Imagen");
        botonDuplicado.setPreferredSize(new java.awt.Dimension(40, 33));
        botonDuplicado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonDuplicadoActionPerformed(evt);
            }
        });
        panelSenoidal.add(botonDuplicado);

        botonTintado.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/tintado.png"))); // NOI18N
        botonTintado.setToolTipText("Tintado");
        botonTintado.setPreferredSize(new java.awt.Dimension(40, 33));
        botonTintado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonTintadoActionPerformed(evt);
            }
        });
        panelSenoidal.add(botonTintado);

        botonVisionNocturna.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/vn.png"))); // NOI18N
        botonVisionNocturna.setToolTipText("Visión Nocturna");
        botonVisionNocturna.setFocusable(false);
        botonVisionNocturna.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botonVisionNocturna.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        botonVisionNocturna.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonVisionNocturnaActionPerformed(evt);
            }
        });
        panelSenoidal.add(botonVisionNocturna);

        jPanel2.add(panelSenoidal);

        panelRotacion.setBorder(javax.swing.BorderFactory.createTitledBorder("Rotacion"));
        panelRotacion.setLayout(new javax.swing.BoxLayout(panelRotacion, javax.swing.BoxLayout.LINE_AXIS));

        sliderRotacion.setMaximum(360);
        sliderRotacion.setMinorTickSpacing(90);
        sliderRotacion.setPaintTicks(true);
        sliderRotacion.setToolTipText("Rotación");
        sliderRotacion.setValue(0);
        sliderRotacion.setPreferredSize(new java.awt.Dimension(96, 31));
        sliderRotacion.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                sliderRotacionStateChanged(evt);
            }
        });
        sliderRotacion.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                sliderRotacionFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                sliderRotacionFocusLost(evt);
            }
        });
        panelRotacion.add(sliderRotacion);

        botonRotacion90.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/rotacion90.png"))); // NOI18N
        botonRotacion90.setToolTipText("Rotación 90º");
        botonRotacion90.setPreferredSize(new java.awt.Dimension(40, 25));
        botonRotacion90.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonRotacion90ActionPerformed(evt);
            }
        });
        panelRotacion.add(botonRotacion90);

        botonRotacion180.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/rotacion180.png"))); // NOI18N
        botonRotacion180.setToolTipText("Rotación 180º");
        botonRotacion180.setPreferredSize(new java.awt.Dimension(40, 25));
        botonRotacion180.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonRotacion180ActionPerformed(evt);
            }
        });
        panelRotacion.add(botonRotacion180);

        botonRotacion270.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/rotacion270.png"))); // NOI18N
        botonRotacion270.setToolTipText("Rotación 270º");
        botonRotacion270.setPreferredSize(new java.awt.Dimension(40, 25));
        botonRotacion270.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonRotacion270ActionPerformed(evt);
            }
        });
        panelRotacion.add(botonRotacion270);

        jPanel2.add(panelRotacion);

        panelEscalar.setBorder(javax.swing.BorderFactory.createTitledBorder("Escalar"));
        panelEscalar.setLayout(new javax.swing.BoxLayout(panelEscalar, javax.swing.BoxLayout.LINE_AXIS));

        botonAumentarEscala.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/aumentar.png"))); // NOI18N
        botonAumentarEscala.setToolTipText("Aumentar");
        botonAumentarEscala.setPreferredSize(new java.awt.Dimension(40, 25));
        botonAumentarEscala.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonAumentarEscalaActionPerformed(evt);
            }
        });
        panelEscalar.add(botonAumentarEscala);

        botonDisminEscala.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/disminuir.png"))); // NOI18N
        botonDisminEscala.setToolTipText("Disminuir");
        botonDisminEscala.setPreferredSize(new java.awt.Dimension(40, 25));
        botonDisminEscala.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonDisminEscalaActionPerformed(evt);
            }
        });
        panelEscalar.add(botonDisminEscala);

        jPanel2.add(panelEscalar);

        panelBinario.setBorder(javax.swing.BorderFactory.createTitledBorder("Binarias"));
        panelBinario.setLayout(new javax.swing.BoxLayout(panelBinario, javax.swing.BoxLayout.LINE_AXIS));

        botonSuma.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/suma.png"))); // NOI18N
        botonSuma.setToolTipText("Suma Binaria");
        botonSuma.setPreferredSize(new java.awt.Dimension(41, 41));
        botonSuma.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonSumaActionPerformed(evt);
            }
        });
        panelBinario.add(botonSuma);

        botonResta.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/resta.png"))); // NOI18N
        botonResta.setToolTipText("Resta Binaria");
        botonResta.setPreferredSize(new java.awt.Dimension(41, 41));
        botonResta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonRestaActionPerformed(evt);
            }
        });
        panelBinario.add(botonResta);

        botonMultiplicacion.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/multiplicacion.png"))); // NOI18N
        botonMultiplicacion.setToolTipText("Multiplicación Binaria");
        botonMultiplicacion.setPreferredSize(new java.awt.Dimension(41, 41));
        botonMultiplicacion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonMultiplicacionActionPerformed(evt);
            }
        });
        panelBinario.add(botonMultiplicacion);

        jPanel2.add(panelBinario);

        jPanel4.setLayout(new java.awt.GridLayout(2, 1));

        sliderUmbralizacion.setMaximum(255);
        sliderUmbralizacion.setToolTipText("Umbralización");
        sliderUmbralizacion.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1), "Umbralización"));
        sliderUmbralizacion.setMinimumSize(new java.awt.Dimension(34, 47));
        sliderUmbralizacion.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                sliderUmbralizacionStateChanged(evt);
            }
        });
        sliderUmbralizacion.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                sliderUmbralizacionFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                sliderUmbralizacionFocusLost(evt);
            }
        });
        jPanel4.add(sliderUmbralizacion);

        sliderBinario.setToolTipText("Mezcla Binaria de imagenes");
        sliderBinario.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1), "Mezcla binaria"));
        sliderBinario.setPreferredSize(new java.awt.Dimension(150, 26));
        sliderBinario.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                sliderBinarioStateChanged(evt);
            }
        });
        sliderBinario.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                sliderBinarioFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                sliderBinarioFocusLost(evt);
            }
        });
        jPanel4.add(sliderBinario);

        jPanel2.add(jPanel4);

        barraOperacionesImg.add(jPanel2);

        jPanel1.add(barraOperacionesImg, java.awt.BorderLayout.PAGE_START);

        getContentPane().add(jPanel1, java.awt.BorderLayout.PAGE_END);

        panelEscritorio.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        javax.swing.GroupLayout panelEscritorioLayout = new javax.swing.GroupLayout(panelEscritorio);
        panelEscritorio.setLayout(panelEscritorioLayout);
        panelEscritorioLayout.setHorizontalGroup(
            panelEscritorioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1741, Short.MAX_VALUE)
        );
        panelEscritorioLayout.setVerticalGroup(
            panelEscritorioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 237, Short.MAX_VALUE)
        );

        getContentPane().add(panelEscritorio, java.awt.BorderLayout.CENTER);

        botonArchivo.setText("Archivo");

        botonNuevo.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.ALT_MASK));
        botonNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/nuevo.png"))); // NOI18N
        botonNuevo.setText("Nuevo");
        botonNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonNuevoActionPerformed(evt);
            }
        });
        botonArchivo.add(botonNuevo);

        botonAbrir.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.ALT_MASK));
        botonAbrir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/abrir.png"))); // NOI18N
        botonAbrir.setText("Abrir");
        botonAbrir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonAbrirActionPerformed(evt);
            }
        });
        botonArchivo.add(botonAbrir);

        botonGuardar.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_G, java.awt.event.InputEvent.ALT_MASK));
        botonGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/guardar.png"))); // NOI18N
        botonGuardar.setText("Guardar");
        botonGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonGuardarActionPerformed(evt);
            }
        });
        botonArchivo.add(botonGuardar);

        MenuPrincipal.add(botonArchivo);

        botonEdicion.setText("Edición");

        botonVistaBarraEstado.setSelected(true);
        botonVistaBarraEstado.setText("Ver barra de estado");
        botonVistaBarraEstado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonVistaBarraEstadoActionPerformed(evt);
            }
        });
        botonEdicion.add(botonVistaBarraEstado);

        botonVistaBarraFormas.setSelected(true);
        botonVistaBarraFormas.setText("Ver barra de formas");
        botonVistaBarraFormas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonVistaBarraFormasActionPerformed(evt);
            }
        });
        botonEdicion.add(botonVistaBarraFormas);

        botonBarraOperaciones.setSelected(true);
        botonBarraOperaciones.setText("Ver barra de operaciones de imagen");
        botonBarraOperaciones.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonBarraOperacionesActionPerformed(evt);
            }
        });
        botonEdicion.add(botonBarraOperaciones);

        botonBarraAtributos.setSelected(true);
        botonBarraAtributos.setText("Ver barra de Atributos");
        botonBarraAtributos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonBarraAtributosActionPerformed(evt);
            }
        });
        botonEdicion.add(botonBarraAtributos);

        botonBarraGeneral.setSelected(true);
        botonBarraGeneral.setText("Ver barra general");
        botonBarraGeneral.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonBarraGeneralActionPerformed(evt);
            }
        });
        botonEdicion.add(botonBarraGeneral);

        MenuPrincipal.add(botonEdicion);

        menuAyuda.setText("Ayuda");

        botonInformacion.setText("Acerca de ");
        botonInformacion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonInformacionActionPerformed(evt);
            }
        });
        menuAyuda.add(botonInformacion);

        MenuPrincipal.add(menuAyuda);

        setJMenuBar(MenuPrincipal);

        pack();
    }// </editor-fold>//GEN-END:initComponents
/**
 * Crea una nueva ventana Interna 
 * @param evt acción de pulsar el botón Nuevo del menú Archivo 
 */
    private void botonNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonNuevoActionPerformed
        VentanaInterna vi=new VentanaInterna(this);
        this.panelEscritorio.add(vi);
        vi.setVisible(true);
        
        BufferedImage img;    
        img = new BufferedImage(700,700,BufferedImage.TYPE_INT_RGB);    
        img.createGraphics().fill(new Rectangle2D.Double(0.0,0.0,img.getWidth(),img.getHeight()));
        vi.getLienzo().setImage(img);
        
        
    }//GEN-LAST:event_botonNuevoActionPerformed
    /**
     * Oculta o hace visible la barra de Formas
     * @param evt acción de pulsar el botón Ver barra de formas
     */
    private void botonVistaBarraFormasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonVistaBarraFormasActionPerformed
        this.barraFormas.setVisible(botonVistaBarraFormas.getState());
    }//GEN-LAST:event_botonVistaBarraFormasActionPerformed
    /**
     * Oculta o hace visible la barra de estado
     * @param evt acción de pulsar el botón Ver barra de estado
     */
    private void botonVistaBarraEstadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonVistaBarraEstadoActionPerformed
        this.barraEstado.setVisible(botonVistaBarraEstado.getState());
    }//GEN-LAST:event_botonVistaBarraEstadoActionPerformed
    /**
     * Oculta o hace visible la barra de operacionnes
     * @param evt acción de pulsar el botón Ver barra de operaciones
     */
    private void botonBarraOperacionesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonBarraOperacionesActionPerformed
        this.barraOperacionesImg.setVisible(botonBarraOperaciones.getState());
    }//GEN-LAST:event_botonBarraOperacionesActionPerformed

    /**
     * Configura el punto como la figura a dibujar
     * @param evt acción de pulsar el botón de Punto
     */
    private void botonPuntoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonPuntoActionPerformed
        
            this.barraEstado.setText("Puntos");
            JInternalFrame vi;
            vi= panelEscritorio.getSelectedFrame(); 

            if((vi != null)&&(vi instanceof VentanaInterna)){
                ((VentanaInterna)vi).getLienzo().setObjeto(1);
                ((VentanaInterna)vi).getLienzo().setEditar(false);
            }
        
    }//GEN-LAST:event_botonPuntoActionPerformed
    /**
     * Configura la línea como la figura a dibujar
     * @param evt acción de pulsar el boton Línea
     */
    private void botonLineaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonLineaActionPerformed
        this.barraEstado.setText("Lineas");
        JInternalFrame vi;
        vi= panelEscritorio.getSelectedFrame(); 
        
        if((vi != null)&&(vi instanceof VentanaInterna)){
            ((VentanaInterna)vi).getLienzo().setObjeto(2);
            ((VentanaInterna)vi).getLienzo().setEditar(false);
        }
    }//GEN-LAST:event_botonLineaActionPerformed
    /**
     * Configura el rectángulo como la figura a dibujar
     * @param evt acción de pulsar el botón Rectángulo
     */
    private void botonRectanguloActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonRectanguloActionPerformed
        this.barraEstado.setText("Rectangulos");
        JInternalFrame vi;
        vi= panelEscritorio.getSelectedFrame(); 
        
        if((vi != null)&&(vi instanceof VentanaInterna)){
            ((VentanaInterna)vi).getLienzo().setObjeto(3);
            ((VentanaInterna)vi).getLienzo().setEditar(false);
        }
        
    }//GEN-LAST:event_botonRectanguloActionPerformed
    /**
     * Configura la elipse como la figura a dibujar
     * @param evt acción de pulsar el botón Elipse
     */
    private void botonCirculoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonCirculoActionPerformed
        this.barraEstado.setText("Elipses");
        JInternalFrame vi;
        vi= panelEscritorio.getSelectedFrame(); 
        
        if((vi != null)&&(vi instanceof VentanaInterna)){
            ((VentanaInterna)vi).getLienzo().setObjeto(4);
            ((VentanaInterna)vi).getLienzo().setEditar(false);
        }
    }//GEN-LAST:event_botonCirculoActionPerformed
    /**
     * Selecciona el color rojo para dibujar
     * @param evt acción de pulsar el botón del color rojo
     */
    private void botonRojoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonRojoActionPerformed
        JInternalFrame vi;
        vi= panelEscritorio.getSelectedFrame();
        
        if((vi != null)&&(vi instanceof VentanaInterna)){
            if(this.botonColorTrazo.isSelected()){
                this.botonColorTrazo.setBackground(Color.RED);
                this.botonColorTrazo.setForeground(Color.BLACK);
                ((VentanaInterna)vi).getLienzo().setColorTrazo(Color.RED);
            }
            if(this.botonColorRelleno.isSelected()){
                this.botonColorRelleno.setBackground(Color.RED);
                this.botonColorRelleno.setForeground(Color.BLACK);
                ((VentanaInterna)vi).getLienzo().setColorRelleno(Color.RED);
            }
            
            ((VentanaInterna)vi).getLienzo().repaint();
        }
        
    }//GEN-LAST:event_botonRojoActionPerformed
    /**
     * Selecciona el color negro para dibujar
     * @param evt acción de pulsar el botón del color negro
     */
    private void botonNegroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonNegroActionPerformed
        JInternalFrame vi;
        vi= panelEscritorio.getSelectedFrame();
        
        if((vi != null)&&(vi instanceof VentanaInterna)){
            if(this.botonColorTrazo.isSelected()){
                this.botonColorTrazo.setBackground(Color.BLACK);
                this.botonColorTrazo.setForeground(Color.WHITE);
                ((VentanaInterna)vi).getLienzo().setColorTrazo(Color.BLACK);
            }
            if(this.botonColorRelleno.isSelected()){
                this.botonColorRelleno.setBackground(Color.BLACK);
                this.botonColorRelleno.setForeground(Color.WHITE);
                ((VentanaInterna)vi).getLienzo().setColorRelleno(Color.BLACK);
            }
            ((VentanaInterna)vi).getLienzo().repaint();
        }
        
    }//GEN-LAST:event_botonNegroActionPerformed
    /**
     * Selecciona el color azul para dibujar
     * @param evt acción de pulsar el botón del color azul
     */
    private void botonAzulActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonAzulActionPerformed
        JInternalFrame vi;
        vi= panelEscritorio.getSelectedFrame();
        
        if((vi != null)&&(vi instanceof VentanaInterna)){
            if(this.botonColorTrazo.isSelected()){
                this.botonColorTrazo.setBackground(Color.BLUE);
                this.botonColorTrazo.setForeground(Color.BLACK);
                ((VentanaInterna)vi).getLienzo().setColorTrazo(Color.BLUE);
            }
            if(this.botonColorRelleno.isSelected()){
                this.botonColorRelleno.setBackground(Color.BLUE);
                this.botonColorRelleno.setForeground(Color.BLACK);
                ((VentanaInterna)vi).getLienzo().setColorRelleno(Color.BLUE);
            }

            ((VentanaInterna)vi).getLienzo().repaint();
        }
        
    }//GEN-LAST:event_botonAzulActionPerformed
    /**
     * Selecciona el color blanco para dibujar
     * @param evt acción de pulsar el botón del color blanco
     */
    private void botonBlancoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonBlancoActionPerformed
        JInternalFrame vi;
        vi= panelEscritorio.getSelectedFrame();
        
        if((vi != null)&&(vi instanceof VentanaInterna)){
            if(this.botonColorTrazo.isSelected()){
                this.botonColorTrazo.setBackground(Color.WHITE);
                this.botonColorTrazo.setForeground(Color.BLACK);
                ((VentanaInterna)vi).getLienzo().setColorTrazo(Color.WHITE);
            }
            if(this.botonColorRelleno.isSelected()){
                this.botonColorRelleno.setBackground(Color.WHITE);
                this.botonColorRelleno.setForeground(Color.BLACK);
                ((VentanaInterna)vi).getLienzo().setColorRelleno(Color.WHITE);
            }
            
            ((VentanaInterna)vi).getLienzo().repaint();
        }
        
    }//GEN-LAST:event_botonBlancoActionPerformed
    /**
     * Selecciona el color amarillo para dibujar
     * @param evt acción de pulsar el botón del color amarillo
     */
    private void botonAmarilloActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonAmarilloActionPerformed
        JInternalFrame vi;
        vi= panelEscritorio.getSelectedFrame();
        
        if((vi != null)&&(vi instanceof VentanaInterna)){
            if(this.botonColorTrazo.isSelected()){
                this.botonColorTrazo.setBackground(Color.YELLOW);
                this.botonColorTrazo.setForeground(Color.BLACK);
                ((VentanaInterna)vi).getLienzo().setColorTrazo(Color.YELLOW);
            }
            if(this.botonColorRelleno.isSelected()){
                this.botonColorRelleno.setBackground(Color.YELLOW);
                this.botonColorRelleno.setForeground(Color.BLACK);
                ((VentanaInterna)vi).getLienzo().setColorRelleno(Color.YELLOW);
            }
            
            ((VentanaInterna)vi).getLienzo().repaint();
        }
        
    }//GEN-LAST:event_botonAmarilloActionPerformed
    /**
     * Selecciona el color verde para dibujar
     * @param evt acción de pulsar el botón del color verde
     */
    private void botonVerdeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonVerdeActionPerformed
        JInternalFrame vi;
        vi= panelEscritorio.getSelectedFrame();
        
        if((vi != null)&&(vi instanceof VentanaInterna)){
            if(this.botonColorTrazo.isSelected()){
                this.botonColorTrazo.setBackground(Color.GREEN);
                this.botonColorTrazo.setForeground(Color.BLACK);
                ((VentanaInterna)vi).getLienzo().setColorTrazo(Color.GREEN);
            }
            if(this.botonColorRelleno.isSelected()){
                this.botonColorRelleno.setBackground(Color.GREEN);
                this.botonColorRelleno.setForeground(Color.BLACK);
                ((VentanaInterna)vi).getLienzo().setColorRelleno(Color.GREEN);
            }
            
            ((VentanaInterna)vi).getLienzo().repaint();
        }
        
    }//GEN-LAST:event_botonVerdeActionPerformed
   /**
    * Configura el grosor del trazo
    * @param evt acción de cambiar el valor del grosor
    */
    private void botonGrosorStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_botonGrosorStateChanged
        JInternalFrame vi;
        vi= panelEscritorio.getSelectedFrame();
        
        if((vi != null)&&(vi instanceof VentanaInterna)){
            //vi.getLienzo().setStroke((new BasicStroke(((Integer) botonGrosor.getValue()).floatValue()))); 
            ((VentanaInterna)vi).getLienzo().setGrosor((int) botonGrosor.getValue());
            ((VentanaInterna)vi).getLienzo().repaint();
        }
        
    }//GEN-LAST:event_botonGrosorStateChanged
    /**
     * Este método abre un archivo (imagen, audio, video)
     * @param evt acción de pulsar el botón Abrir del menú Archivo 
     */
    private void botonAbrirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonAbrirActionPerformed
        JFileChooser dlg = new JFileChooser();
        dlg.setFileFilter(new FileNameExtensionFilter("Imagenes [jpg,jpeg,png]", "jpg","jpeg","png"));
        dlg.setFileFilter(new FileNameExtensionFilter("Audio [au,wav,mp3]","au","mp3","wav"));
        dlg.setFileFilter(new FileNameExtensionFilter("Video [avi,mp4]","avi","mp4"));
        
        int resp = dlg.showOpenDialog(this);
        if( resp == JFileChooser.APPROVE_OPTION) {
            try{ 
                File f = dlg.getSelectedFile(); 
                String extension=this.getExtension(f.getName()); 
                
                if((extension.equalsIgnoreCase("jpg"))||(extension.equalsIgnoreCase("png"))||(extension.equalsIgnoreCase("jpeg"))){
                    
                    BufferedImage img = ImageIO.read(f);
                    VentanaInterna vi = new VentanaInterna(this); 
                    vi.getLienzo().setImage(img);
                    this.panelEscritorio.add(vi);
                    vi.setTitle(f.getName());
                    vi.setVisible(true);
                }else if((extension.equalsIgnoreCase("avi"))||(extension.equalsIgnoreCase("mp4"))||(extension.equalsIgnoreCase("mp3"))){
                    viJMF=VentanaInternaJMFPlayer.getInstance(f);                              
                    this.panelEscritorio.add(viJMF);
                    viJMF.setTitle(f.getName());
                    viJMF.setVisible(true);
                    
                }else if((extension.equalsIgnoreCase("wav"))||(extension.equalsIgnoreCase("au"))){
                    VentanaInternaReproductor vi = new VentanaInternaReproductor(f);                 
                    this.panelEscritorio.add(vi);
                    vi.setTitle(f.getName());
                    vi.setVisible(true);
                }else{
                    JOptionPane ventError = new JOptionPane();
                    ventError.showMessageDialog(this, "Formato Desconocido", "ERROR",0);
                    ventError.setVisible(true);
                }
                
            }catch(Exception ex){
                System.err.println("Error al leer la imagen");                
                JOptionPane ventError = new JOptionPane();
                ventError.showMessageDialog(this, "Formato Desconocido");
                ventError.setVisible(true);
            }   
        }     
    }//GEN-LAST:event_botonAbrirActionPerformed
    /**
     * Este método guarda la imagen de la ventana seleccionada
     * @param evt Acción de pulsar el botón Guardar del menú Archivo
     */
    private void botonGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonGuardarActionPerformed
        VentanaInterna vi=(VentanaInterna) 
        panelEscritorio.getSelectedFrame();   
        if (vi != null) {
            JFileChooser dlg = new JFileChooser();
            dlg.setFileFilter(new FileNameExtensionFilter("JPG file", "jpg"));
            dlg.setFileFilter(new FileNameExtensionFilter("JPEG file","jpeg"));
            dlg.setFileFilter(new FileNameExtensionFilter("PNG file","png"));
            int resp = dlg.showSaveDialog(this);
            if (resp == JFileChooser.APPROVE_OPTION) {
                try { 
                    BufferedImage img = vi.getLienzo().getImage(true);
                    if (img != null) {
                        if(dlg.getFileFilter().getDescription()=="JPG file"){
                            File f = new File(dlg.getSelectedFile().getAbsoluteFile()+ ".jpg");
                            ImageIO.write(img, "jpg", f);
                            vi.setTitle(f.getName());
                            
                        }else if(dlg.getFileFilter().getDescription()=="JPEG file"){
                            File f = new File(dlg.getSelectedFile().getAbsoluteFile()+ ".jpeg");
                            ImageIO.write(img, "jpeg", f);
                            vi.setTitle(f.getName());
                            
                        }else if(dlg.getFileFilter().getDescription()=="PNG file"){
                            File f = new File(dlg.getSelectedFile().getAbsoluteFile()+ ".png");
                            ImageIO.write(img, "png", f);
                            vi.setTitle(f.getName());
                        }
                    }
                }catch (Exception ex) {
                    System.err.println("Error al guardar la imagen");
                }
            }
        }
    }//GEN-LAST:event_botonGuardarActionPerformed
    /**
     * Actualiza el brillo de una imagen mediante una barra deslizadora
     * @param evt Acción de cambiar el estado de la barra deslizadora
     */
    private void jSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSliderStateChanged
        JInternalFrame vi = (panelEscritorio.getSelectedFrame());
        if ((vi != null)&&(vi instanceof VentanaInterna)) {
            if(this.imgSource!=null){
                try{
                    RescaleOp rop = new RescaleOp(1.0F, jSlider.getValue(), null);
                    if(this.imgSource.getColorModel().hasAlpha()){
                        float brillo= this.jSlider.getValue();
                        float[] alfa= {1.0F, 1.0F, 1.0F, 1.0F};
                        float[] beta={brillo, brillo, brillo, 0.0F};
                        rop= new RescaleOp(alfa, beta, null);
                    }else{    
                        rop = new RescaleOp(1.0F, jSlider.getValue(), null);                    
                        
                    }
                    BufferedImage imgdest = rop.filter(imgSource, null);
                    ((VentanaInterna)vi).getLienzo().setImage(imgdest);
                    ((VentanaInterna)vi).getLienzo().repaint();
                } catch(IllegalArgumentException e){
                    System.err.println(e.getLocalizedMessage());
                }
            }
            
        }
             
    }//GEN-LAST:event_jSliderStateChanged
    /**
     * La imagen se actualiza con el nuevo brillo
     * @param evt Acción de soltar la barra deslizadora
     */
    private void brilloFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_brilloFocusLost
        JInternalFrame vi =(panelEscritorio.getSelectedFrame());
        if((vi != null)&&(vi instanceof VentanaInterna)){
            this.imgSource=null;
        }
    }//GEN-LAST:event_brilloFocusLost
    /**
     * Prepara la imagen seleccionada para cambiarle el brillo
     * @param evt Acción de pulsar la barra desizadora
     */
    private void brilloFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_brilloFocusGained
        JInternalFrame vi = (panelEscritorio.getSelectedFrame());
        if((vi != null)&&(vi instanceof VentanaInterna)){
            this.imgSource=((VentanaInterna)vi).getLienzo().getImage();
        }
    }//GEN-LAST:event_brilloFocusGained
    /**
     * Configura el relleno a dibujar
     * @param evt Acción de pulsar el botón relleno
     */
    private void botonRellenoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonRellenoActionPerformed
        JInternalFrame vi;
        vi= panelEscritorio.getSelectedFrame();

        if((vi != null)&&(vi instanceof VentanaInterna)){
            if(this.botonRelleno.isSelected()){
                ((VentanaInterna)vi).getLienzo().setRelleno(true);
            }else{
                ((VentanaInterna)vi).getLienzo().setRelleno(false);
            }
            ((VentanaInterna)vi).getLienzo().repaint();
        }
        
    }//GEN-LAST:event_botonRellenoActionPerformed
    /**
     * Alisa el los bordes de las figuras a dibujar en el lienzo
     * @param evt acción de pulsar el botón Alisar
     */
    private void botonAlisarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonAlisarActionPerformed
        JInternalFrame vi;
        vi= panelEscritorio.getSelectedFrame();
        
        if((vi != null)&&(vi instanceof VentanaInterna)){
            if(this.botonAlisar.isSelected()){
                ((VentanaInterna)vi).getLienzo().renderizado(true);
            }else{
                ((VentanaInterna)vi).getLienzo().renderizado(false);
            }
        }
        this.repaint();
    }//GEN-LAST:event_botonAlisarActionPerformed
    /**
     * Configura la opción Editar del lienzo
     * @param evt acción de pulsar el botón Editar
     */
    private void botonEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonEditarActionPerformed
        JInternalFrame vi;
        vi= panelEscritorio.getSelectedFrame();
        if((vi != null)&&(vi instanceof VentanaInterna)){
            
            if(this.botonEditar.isSelected()){
                ((VentanaInterna)vi).getLienzo().setEditar(true);
            }else{
               ((VentanaInterna)vi).getLienzo().setEditar(false);
            }
            
        }
    }//GEN-LAST:event_botonEditarActionPerformed
    /**
     * Aplica el filtro seleccionado
     * @param evt acción de seleccionar un filtro
     */
    private void boxFiltrosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boxFiltrosActionPerformed
        JInternalFrame vi;
        vi= panelEscritorio.getSelectedFrame();
        int n=boxFiltros.getSelectedIndex();
        BufferedImage imagenDest=null;
        Kernel k=null;
        ConvolveOp cop=null; 
                
        if((vi != null)&&(vi instanceof VentanaInterna)){
            switch(n){
                
                case 0:
                    k = KernelProducer.createKernel(KernelProducer.TYPE_MEDIA_3x3); 
                    cop = new ConvolveOp(k,ConvolveOp.EDGE_NO_OP,null);
                    imagenDest=cop.filter(((VentanaInterna)vi).getLienzo().getImage(),null);
                break;
                case 1:
                    k = KernelProducer.createKernel(KernelProducer.TYPE_BINOMIAL_3x3); 
                    cop = new ConvolveOp(k,ConvolveOp.EDGE_NO_OP,null);
                    imagenDest=cop.filter(((VentanaInterna)vi).getLienzo().getImage(),null);
                break;
                case 2:
                    k = KernelProducer.createKernel(KernelProducer.TYPE_ENFOQUE_3x3); 
                    cop = new ConvolveOp(k,ConvolveOp.EDGE_NO_OP,null);
                    imagenDest=cop.filter(((VentanaInterna)vi).getLienzo().getImage(),null);
                break;
                case 3:
                    k = KernelProducer.createKernel(KernelProducer.TYPE_RELIEVE_3x3); 
                    cop = new ConvolveOp(k,ConvolveOp.EDGE_NO_OP,null);
                    imagenDest=cop.filter(((VentanaInterna)vi).getLienzo().getImage(),null);
                break;
                case 4:
                    k = KernelProducer.createKernel(KernelProducer.TYPE_LAPLACIANA_3x3); 
                    cop = new ConvolveOp(k,ConvolveOp.EDGE_NO_OP,null);
                    imagenDest=cop.filter(((VentanaInterna)vi).getLienzo().getImage(),null);
                break;
                
            }
            ((VentanaInterna)vi).getLienzo().setImage(imagenDest);
            ((VentanaInterna)vi).getLienzo().repaint();
        }
    }//GEN-LAST:event_boxFiltrosActionPerformed
    /**
    * Crea una nueva ventana Interna 
    * @param evt acción de pulsar el botón Nuevo de la barra general
    */
    private void botonNuevo2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonNuevo2ActionPerformed
        TamañoVentana ventEmergente=new TamañoVentana(this, true);
        ventEmergente.setTitle("Tamaño de nueva imagen");
        ventEmergente.setVisible(true);
        VentanaInterna vi=new VentanaInterna(this);
        this.panelEscritorio.add(vi);
        vi.setVisible(true);
        
        BufferedImage img;    
        img = new BufferedImage(ventEmergente.getHigh(),ventEmergente.getAncho(),BufferedImage.TYPE_INT_RGB); 
        vi.setSize(ventEmergente.getHigh(), ventEmergente.getAncho());
        img.createGraphics().fill(new Rectangle2D.Double(0.0,0.0,img.getWidth(),img.getHeight()));
       
        vi.getLienzo().setImage(img);
    }//GEN-LAST:event_botonNuevo2ActionPerformed
    /**
     * Este método abre un archivo (imagen, audio, video)
     * @param evt acción de pulsar el botón Abrir de la barra general 
     */
    private void botonAbrir2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonAbrir2ActionPerformed
        JFileChooser dlg = new JFileChooser();
        dlg.setFileFilter(new FileNameExtensionFilter("Imagenes [jpg,jpeg,png]", "jpg","jpeg","png"));
        dlg.setFileFilter(new FileNameExtensionFilter("Audio [au,wav,mp3]","au","mp3","wav"));
        dlg.setFileFilter(new FileNameExtensionFilter("Video [avi,mp4]","avi","mp4"));
        
        int resp = dlg.showOpenDialog(this);
        if( resp == JFileChooser.APPROVE_OPTION) {
            try{ 
                File f = dlg.getSelectedFile(); 
                String extension=this.getExtension(f.getName()); 
                
                if((extension.equalsIgnoreCase("jpg"))||(extension.equalsIgnoreCase("png"))||(extension.equalsIgnoreCase("jpeg"))){
                    
                    BufferedImage img = ImageIO.read(f);
                    VentanaInterna vi = new VentanaInterna(this); 
                    vi.getLienzo().setImage(img);
                    this.panelEscritorio.add(vi);
                    vi.setTitle(f.getName());
                    vi.setVisible(true);
                }else if((extension.equalsIgnoreCase("avi"))||(extension.equalsIgnoreCase("mp4"))||(extension.equalsIgnoreCase("mp3"))){
                    viJMF=VentanaInternaJMFPlayer.getInstance(f);                              
                    this.panelEscritorio.add(viJMF);
                    viJMF.setTitle(f.getName());
                    viJMF.setVisible(true);
                    
                }else if((extension.equalsIgnoreCase("wav"))||(extension.equalsIgnoreCase("au"))){
                    VentanaInternaReproductor vi = new VentanaInternaReproductor(f);                 
                    this.panelEscritorio.add(vi);
                    vi.setTitle(f.getName());
                    vi.setVisible(true);
                }else{
                    JOptionPane ventError = new JOptionPane();
                    ventError.showMessageDialog(this, "Formato Desconocido", "ERROR",0);
                    ventError.setVisible(true);
                }
                
            }catch(Exception ex){
                System.err.println("Error al leer la imagen");                                
            }   
        }
    }//GEN-LAST:event_botonAbrir2ActionPerformed
    /**
     * Este método guarda la imagen de la ventana seleccionada
     * @param evt Acción de pulsar el botón Guardar de la barra general
     */
    private void botonGuardar2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonGuardar2ActionPerformed
        VentanaInterna vi=(VentanaInterna) 
        panelEscritorio.getSelectedFrame();   
        if (vi != null) {
            JFileChooser dlg = new JFileChooser();
            dlg.setFileFilter(new FileNameExtensionFilter("JPG file", "jpg"));
            dlg.setFileFilter(new FileNameExtensionFilter("JPEG file","jpeg"));
            dlg.setFileFilter(new FileNameExtensionFilter("PNG file","png"));
            int resp = dlg.showSaveDialog(this);
            if (resp == JFileChooser.APPROVE_OPTION) {
                try { 
                    BufferedImage img = vi.getLienzo().getImage(true);
                    if (img != null) {
                        if(dlg.getFileFilter().getDescription()=="JPG file"){
                            File f = new File(dlg.getSelectedFile().getAbsoluteFile()+ ".jpg");
                            ImageIO.write(img, "jpg", f);
                            vi.setTitle(f.getName());
                            
                        }else if(dlg.getFileFilter().getDescription()=="JPEG file"){
                            File f = new File(dlg.getSelectedFile().getAbsoluteFile()+ ".jpeg");
                            ImageIO.write(img, "jpeg", f);
                            vi.setTitle(f.getName());
                            
                        }else if(dlg.getFileFilter().getDescription()=="PNG file"){
                            File f = new File(dlg.getSelectedFile().getAbsoluteFile()+ ".png");
                            ImageIO.write(img, "png", f);
                            vi.setTitle(f.getName());
                        }
                    }
                }catch (Exception ex) {
                    System.err.println("Error al guardar la imagen");
                }
            }
        }
    }//GEN-LAST:event_botonGuardar2ActionPerformed
        
    /**
     * Rota la imagen en función del valor de la barra deslizadora
     * @param evt acción cambiar el estado de la barra deslizadora
     */
    private void sliderRotacionStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_sliderRotacionStateChanged
        JInternalFrame vi= panelEscritorio.getSelectedFrame();
                                      
        if ((vi != null)&&(vi instanceof VentanaInterna)) {
            
            if(imgSource!=null){
                
                AffineTransformOp atop= this.rotacion((double)this.sliderRotacion.getValue());
                BufferedImage imagenDest=atop.filter(this.imgSource, null);
                
                ((VentanaInterna)vi).getLienzo().setImage(imagenDest);
                vi.repaint();
            }
        }
    }//GEN-LAST:event_sliderRotacionStateChanged
    /**
     * Prepara la imagen seleccionada para rotarla
     * @param evt acción de pulsar la barra deslizadora
     */
    private void sliderRotacionFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_sliderRotacionFocusGained
        JInternalFrame vi= panelEscritorio.getSelectedFrame();
        
        if ((vi != null)&&(vi instanceof VentanaInterna)) {
            this.imgSource=((VentanaInterna)vi).getLienzo().getImage();
        }
    }//GEN-LAST:event_sliderRotacionFocusGained
    /**
     * Este método actualiza la imagen con la nueva rotación 
     * @param evt Acción de sotar la barra deslizadora
     */
    private void sliderRotacionFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_sliderRotacionFocusLost
        JInternalFrame vi=panelEscritorio.getSelectedFrame();
        
        if ((vi != null)&&(vi instanceof VentanaInterna)){
            this.imgSource=null;
        }
    }//GEN-LAST:event_sliderRotacionFocusLost
    /**
     * Realiza la suma binaria de dos imágenes
     * @param evt acción de pulsar el botón Suma
     */
    private void botonSumaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonSumaActionPerformed
        JInternalFrame vi= panelEscritorio.getSelectedFrame();
        if ((vi != null)&&(vi instanceof VentanaInterna)) {
            JInternalFrame viNext =  panelEscritorio.selectFrame(false);
            if ((viNext != null)&&(viNext instanceof VentanaInterna)) {
                BufferedImage imgRight = ((VentanaInterna)vi).getLienzo().getImage();
                BufferedImage imgLeft = ((VentanaInterna)viNext).getLienzo().getImage();
                if (imgRight != null && imgLeft != null) {
                    try {
                        BlendOp op = new BlendOp(imgLeft);
                        BufferedImage imgdest = op.filter(imgRight, null);
                        vi = new VentanaInterna();
                        ((VentanaInterna)vi).getLienzo().setImage(imgdest);
                        this.panelEscritorio.add(vi);
                        vi.setVisible(true);
                    }
                    catch (IllegalArgumentException e) {
                        System.err.println("Error: "+e.getLocalizedMessage());
                    } 
                } 
            } 
        } 
    }//GEN-LAST:event_botonSumaActionPerformed
    /**
     * Realiza la resta binaria de dos imágenes
     * @param evt acción de pulsar el botón Resta
     */
    private void botonRestaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonRestaActionPerformed
        JInternalFrame vi= panelEscritorio.getSelectedFrame();
        if ((vi != null)&&(vi instanceof VentanaInterna)) {
            JInternalFrame viNext =  panelEscritorio.selectFrame(false);
            if ((viNext != null)&&(viNext instanceof VentanaInterna)) {
                BufferedImage imgRight = ((VentanaInterna)vi).getLienzo().getImage();
                BufferedImage imgLeft = ((VentanaInterna)viNext).getLienzo().getImage();
                if (imgRight != null && imgLeft != null) {
                    try {
                        SubtractionOp op = new SubtractionOp(imgLeft);
                        BufferedImage imgdest = op.filter(imgRight, null);
                        vi = new VentanaInterna();
                        ((VentanaInterna)vi).getLienzo().setImage(imgdest);
                        this.panelEscritorio.add(vi);
                        vi.setVisible(true);
                    }
                    catch (IllegalArgumentException e) {
                        System.err.println("Error: "+e.getLocalizedMessage());
                    } 
                } 
            } 
        } 
    }//GEN-LAST:event_botonRestaActionPerformed
    /**
     * Aplica a la imagen seleccionada un contraste normal
     * @param evt acción de pulsar el botón de Contraste Normal
     */
    private void botonNormalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonNormalActionPerformed
        JInternalFrame vi= panelEscritorio.getSelectedFrame();
        
                
        if ((vi != null)&&(vi instanceof VentanaInterna)) {
            BufferedImage imagenDest=((VentanaInterna)vi).getLienzo().getImage();
            LookupTable lt;
            LookupOp lop=null;
            lt=LookupTableProducer.createLookupTable(LookupTableProducer.TYPE_SFUNCION); 
            lop= new LookupOp(lt,null);
            imagenDest=lop.filter(imagenDest,imagenDest);
            ((VentanaInterna)vi).getLienzo().repaint();
        }
    }//GEN-LAST:event_botonNormalActionPerformed
    /**
     * Aplica a la imagen seleccionada un contraste iluminado
     * @param evt acción de pulsar el botón de contraste iluminado
     */
    private void botonIluminadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonIluminadoActionPerformed
        JInternalFrame vi=panelEscritorio.getSelectedFrame();
        
                
        if ((vi != null)&&(vi instanceof VentanaInterna)) {
            BufferedImage imagenDest=((VentanaInterna)vi).getLienzo().getImage();
            LookupTable lt;
            LookupOp lop=null;
            lt=LookupTableProducer.createLookupTable(LookupTableProducer.TYPE_LOGARITHM); 
            lop= new LookupOp(lt,null);
            imagenDest=lop.filter(imagenDest,imagenDest);
            ((VentanaInterna)vi).getLienzo().repaint();
        }
    }//GEN-LAST:event_botonIluminadoActionPerformed
    /**
     * Aplica a la imagen seleccionada un contraste oscurecido
     * @param evt acción de pulsar el botón Contraste Oscurecido
     */
    private void botonOscurecerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonOscurecerActionPerformed
        JInternalFrame vi= panelEscritorio.getSelectedFrame();
        
                
        if ((vi != null)&&(vi instanceof VentanaInterna)) {
            BufferedImage imagenDest=((VentanaInterna)vi).getLienzo().getImage();
            LookupTable lt;
            LookupOp lop=null;
            lt=LookupTableProducer.createLookupTable(LookupTableProducer.TYPE_POWER); 
            lop= new LookupOp(lt,null);
            imagenDest=lop.filter(imagenDest,imagenDest);
            
            ((VentanaInterna)vi).getLienzo().repaint();
        }
    }//GEN-LAST:event_botonOscurecerActionPerformed
    /**
     * Aplica la función seno a la imagen seleccionada
     * @param evt acción de pulsar el botón Seno
     */
    private void botonSenoidalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonSenoidalActionPerformed
        JInternalFrame vi=panelEscritorio.getSelectedFrame();
                                
        if ((vi != null)&&(vi instanceof VentanaInterna)) {
            BufferedImage imagenDest=((VentanaInterna)vi).getLienzo().getImage();
            if(imagenDest!=null){
                LookupTable lt=this.seno(0.045);
                LookupOp lop=new LookupOp(lt,null);             
                lop.filter(imagenDest,imagenDest);

                ((VentanaInterna)vi).getLienzo().setImage(imagenDest);
                vi.repaint();
            }
        }
    }//GEN-LAST:event_botonSenoidalActionPerformed
    /**
     * Rota 90 grados la imagen seleccionada
     * @param evt acción de pulsar el botón de Rotacion90°
     */
    private void botonRotacion90ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonRotacion90ActionPerformed
        JInternalFrame vi= panelEscritorio.getSelectedFrame();
       
                                
        if ((vi != null)&&(vi instanceof VentanaInterna)) {
            this.imgSource=((VentanaInterna)vi).getLienzo().getImage();
            if(imgSource!=null){
                
                AffineTransformOp atop= this.rotacion(90.0);
                BufferedImage imagenDest=atop.filter(imgSource, null);
                
                ((VentanaInterna)vi).getLienzo().setImage(imagenDest);
                vi.repaint();
            }
        }
    }//GEN-LAST:event_botonRotacion90ActionPerformed
    /**
     * Rota 180 grados la imagen seleccionada
     * @param evt acción de pulsar el botón de Rotacion1800°
     */
    private void botonRotacion180ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonRotacion180ActionPerformed
        JInternalFrame vi= panelEscritorio.getSelectedFrame();
       
                                
        if ((vi != null)&&(vi instanceof VentanaInterna)) {
            this.imgSource=((VentanaInterna)vi).getLienzo().getImage();
            if(imgSource!=null){
                
                AffineTransformOp atop= this.rotacion(180.0);
                BufferedImage imagenDest=atop.filter(imgSource, null);
                
                ((VentanaInterna)vi).getLienzo().setImage(imagenDest);
                vi.repaint();
            }
        }
    }//GEN-LAST:event_botonRotacion180ActionPerformed
    /**
     * Rota 270 grados la imagen seleccionada
     * @param evt acción de pulsar el botón de Rotacion270°
     */
    private void botonRotacion270ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonRotacion270ActionPerformed
        JInternalFrame vi= panelEscritorio.getSelectedFrame();
                                      
        if ((vi != null)&&(vi instanceof VentanaInterna)) {
            this.imgSource=((VentanaInterna)vi).getLienzo().getImage();
            if(imgSource!=null){
                
                AffineTransformOp atop= this.rotacion(270.0);
                BufferedImage imagenDest=atop.filter(imgSource, null);
                
                ((VentanaInterna)vi).getLienzo().setImage(imagenDest);
                vi.repaint();
            }
        }
    }//GEN-LAST:event_botonRotacion270ActionPerformed
    /**
     * Aumenta la escala de la imagen seleccionada
     * @param evt Acción de pulsar el botón AumentarEscala
     */
    private void botonAumentarEscalaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonAumentarEscalaActionPerformed
        JInternalFrame vi= panelEscritorio.getSelectedFrame();
                                      
        if ((vi != null)&&(vi instanceof VentanaInterna)) {
            this.imgSource=((VentanaInterna)vi).getLienzo().getImage();
            if(imgSource!=null){
                
                AffineTransformOp atop= this.escalar(1.25);
                BufferedImage imagenDest=atop.filter(this.imgSource, null);
                
                ((VentanaInterna)vi).getLienzo().setImage(imagenDest);
                vi.repaint();
            }
        }
    }//GEN-LAST:event_botonAumentarEscalaActionPerformed
    /**
     * Disminuir la escala de la imagen seleccionada
     * @param evt Acción de pulsar el botón DisminEscala
     */
    private void botonDisminEscalaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonDisminEscalaActionPerformed
        JInternalFrame vi= panelEscritorio.getSelectedFrame();
                                      
        if ((vi != null)&&(vi instanceof VentanaInterna)) {
            this.imgSource=((VentanaInterna)vi).getLienzo().getImage();
            if(imgSource!=null){
                
                AffineTransformOp atop= this.escalar(0.75);
                BufferedImage imagenDest=atop.filter(this.imgSource, null);
                
                ((VentanaInterna)vi).getLienzo().setImage(imagenDest);
                vi.repaint();
            }
        }
    }//GEN-LAST:event_botonDisminEscalaActionPerformed
    /**
     * Aplica a la imagen seleccionada la operación Sepia
     * @param evt acción de pulsar el botón Sepia
     */
    private void botonSepiaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonSepiaActionPerformed
        JInternalFrame vi=panelEscritorio.getSelectedFrame();
                                       
        if ((vi != null) && (vi instanceof VentanaInterna)) {
            BufferedImage imagenSrc=((VentanaInterna)vi).getLienzo().getImage();
            if(imagenSrc!=null){
               SepiaOp sepia= new SepiaOp();
                BufferedImage imgDest = sepia.filter(imagenSrc, null);
                
                ((VentanaInterna)vi).getLienzo().setImage(imgDest);
                ((VentanaInterna)vi).getLienzo().repaint();
            }
            
        }
    }//GEN-LAST:event_botonSepiaActionPerformed
    /**
     * Realiza la suma binaria de dos imagenes mediante el valor de la barra deslizadora 
     * @param evt acción cambiar el estado de la barra deslizadora 
     */
    private void sliderBinarioStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_sliderBinarioStateChanged

        if ((this.vi != null) && (this.vi instanceof VentanaInterna)) {
                       
            if ((this.viNext != null)&&(this.viNext instanceof VentanaInterna)) {

                if (this.imgRight != null && this.imgLeft != null) {
                    try {
                                               
                        float valor=(float) this.sliderBinario.getValue()/100;
                        BlendOp op = new BlendOp(this.imgLeft,valor);                   
                        BufferedImage imgdest = op.filter(this.imgRight, null);
                        ((VentanaInterna)this.vi).getLienzo().setImage(imgdest);
                        ((VentanaInterna)this.vi).repaint();                                                                          
                    }
                    catch (IllegalArgumentException e) {
                        System.err.println("Error: "+e.getLocalizedMessage());
                    } 
                } 
            } 
        } 
    }//GEN-LAST:event_sliderBinarioStateChanged
    /**
     * prepara las imagenes para aplicar la suma binaria
     * @param evt acción de pulsar la barra deslizadora
     */
    private void sliderBinarioFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_sliderBinarioFocusGained
        this.vi = (panelEscritorio.getSelectedFrame());
        if((vi != null) && (vi instanceof VentanaInterna)){
            this.viNext = panelEscritorio.selectFrame(false);
           
            if ((this.viNext != null) &&(viNext instanceof VentanaInterna)) {
                
                this.imgRight = ((VentanaInterna)this.vi).getLienzo().getImage();
                this.imgLeft = ((VentanaInterna)this.viNext).getLienzo().getImage();
                this.vi = new VentanaInterna();
                            
                this.panelEscritorio.add(this.vi);
                this.vi.setVisible(true);
                
            }
        }
    }//GEN-LAST:event_sliderBinarioFocusGained
    /**
     * Actualiza el valor de la nueva imagen con la suma binaria
     * @param evt acción de soltar la barra deslizadora
     */
    private void sliderBinarioFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_sliderBinarioFocusLost
        JInternalFrame vi=panelEscritorio.getSelectedFrame();
        
        if((vi != null) && (vi instanceof VentanaInterna)){
            VentanaInterna viNext = (VentanaInterna) panelEscritorio.selectFrame(false);
          
            if ((this.viNext != null) &&(viNext instanceof VentanaInterna)) {                
                //this.imgRight=null;
                //this.imgLeft=null;
            }
        }
    }//GEN-LAST:event_sliderBinarioFocusLost
    /**
     * Realiza la multiplicación binaria con los pixeles de dos imágenes
     * @param evt acción de pulsar el botón multiplicación
     */
    private void botonMultiplicacionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonMultiplicacionActionPerformed
        JInternalFrame vi= panelEscritorio.getSelectedFrame();
        if ((vi != null)&&(vi instanceof VentanaInterna)) {
            JInternalFrame viNext = panelEscritorio.selectFrame(false);
            if ((viNext != null)&&(viNext instanceof VentanaInterna)) {
                BufferedImage imgRight = ((VentanaInterna)vi).getLienzo().getImage();
                BufferedImage imgLeft =((VentanaInterna) viNext).getLienzo().getImage();
                if (imgRight != null && imgLeft != null) {
                    try {
                        multiplicacionOp op = new multiplicacionOp(imgLeft);
                        BufferedImage imgdest = op.filter(imgRight, null);
                        vi = new VentanaInterna();
                        ((VentanaInterna)vi).getLienzo().setImage(imgdest);
                        this.panelEscritorio.add(vi);
                        vi.setVisible(true);
                    }
                    catch (IllegalArgumentException e) {
                        System.err.println("Error: "+e.getLocalizedMessage());
                    } 
                } 
            } 
        } 
    }//GEN-LAST:event_botonMultiplicacionActionPerformed
    /**
     * Graba un archivo de audio del exterior mediante el micrófono
     * @param evt acción de pulsar el botón Record
     */
    private void botonRecordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonRecordActionPerformed
        JFileChooser dlg = new JFileChooser();
        int resp = dlg.showSaveDialog(this);
        if( resp == JFileChooser.APPROVE_OPTION) {
            try{ 
                File sonido = dlg.getSelectedFile();
                VentanaInternaGrabador vi = new VentanaInternaGrabador(sonido);                 
                this.panelEscritorio.add(vi);
                
                vi.setTitle(sonido.getName());
                vi.setVisible(true);
                SMSoundRecorder player=vi.getPlayer();
                player.record();
            }catch(Exception ex){
                System.err.println("Error al leer el archivo");     
            }   
        }
    }//GEN-LAST:event_botonRecordActionPerformed
    /**
     * Configura curva como la figura a dibujar
     * @param evt acción de pulsar el botónn Curva
     */
    private void botonCurvaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonCurvaActionPerformed
        this.barraEstado.setText("Curvas");
        JInternalFrame vi;
        vi= panelEscritorio.getSelectedFrame(); 
        
        if((vi != null)&&(vi instanceof VentanaInterna)){
            ((VentanaInterna)vi).getLienzo().setObjeto(5);
            ((VentanaInterna)vi).getLienzo().setEditar(false);
        }
    }//GEN-LAST:event_botonCurvaActionPerformed
    /**
     * Configura el valor de transparencia de dibujado mediante el valor de una barra deslizadora
     * @param evt acción de cambiar el estado de la barra deslizadora
     */
    private void sliderTransparenciaStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_sliderTransparenciaStateChanged
        JInternalFrame vi;
        vi= panelEscritorio.getSelectedFrame();
        
        if((vi != null)&&(vi instanceof VentanaInterna)){
            int valor=(this.sliderTransparencia.getValue());
            ((VentanaInterna)vi).getLienzo().setTransparencia((float)valor/100);
            
            ((VentanaInterna)vi).getLienzo().repaint();
        }
        
    }//GEN-LAST:event_sliderTransparenciaStateChanged
    /**
     * Configura el Rectángulo Redondeado como la figura a dibujar
     * @param evt acción de pulsar el botón RoundRectangulo
     */
    private void botonRoundRectanguloActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonRoundRectanguloActionPerformed
        this.barraEstado.setText("Rectangulos Redondeados");
        JInternalFrame vi;
        vi= panelEscritorio.getSelectedFrame(); 
        
        if((vi != null)&&(vi instanceof VentanaInterna)){
            ((VentanaInterna)vi).getLienzo().setObjeto(6);
            ((VentanaInterna)vi).getLienzo().setEditar(false);
        }
    }//GEN-LAST:event_botonRoundRectanguloActionPerformed
    /**
     * Configura el Arco como a figura a dibujar
     * @param evt acción de pulsar el botón Arco
     */
    private void botonArcoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonArcoActionPerformed
        this.barraEstado.setText("Rectangulos Redondeados");
        JInternalFrame vi;
        vi= panelEscritorio.getSelectedFrame(); 
        
        if((vi != null)&&(vi instanceof VentanaInterna)){
            ((VentanaInterna)vi).getLienzo().setObjeto(7);
            ((VentanaInterna)vi).getLienzo().setEditar(false);
        }
    }//GEN-LAST:event_botonArcoActionPerformed
    /**
     * Aplica al relleno un degradado horizontal
     * @param evt acción de pulsar el botón DegradadoH
     */
    private void botonDegradadoHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonDegradadoHActionPerformed
        JInternalFrame vi;
        vi= panelEscritorio.getSelectedFrame();

        if((vi != null)&&(vi instanceof VentanaInterna)){
            if(this.botonRelleno.isSelected()){
                ((VentanaInterna)vi).getLienzo().setDegradadoHz(true);
            }else{
                ((VentanaInterna)vi).getLienzo().setDegradadoHz(false);
            }
            ((VentanaInterna)vi).getLienzo().repaint();
        }
        
    }//GEN-LAST:event_botonDegradadoHActionPerformed
    /**
     * Aplica al relleno un degradado vertical
     * @param evt acción de pulsar el botón DegradadoV
     */
    private void botonDegradadoVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonDegradadoVActionPerformed
        JInternalFrame vi;
        vi= panelEscritorio.getSelectedFrame();

        if((vi != null)&&(vi instanceof VentanaInterna)){
            if(this.botonRelleno.isSelected()){
                ((VentanaInterna)vi).getLienzo().setDegradadoV(true);
            }else{
                ((VentanaInterna)vi).getLienzo().setDegradadoV(false);
            }
            ((VentanaInterna)vi).getLienzo().repaint();
        }
        
    }//GEN-LAST:event_botonDegradadoVActionPerformed
    /**
     * Abre la VentanaInternaCamara en la que se podrá ver la imagen que capta la WebCam
     * @param evt acción de pulsar el botón Camara
     */
    private void botonCamara2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonCamara2ActionPerformed
        try{                              
                viC=VentanaInternaCamara.getInstance();                             
                this.panelEscritorio.add(viC);
                viC.setVisible(true);
            }catch(Exception ex){
                System.err.println("Error al abrir la cámara");     
            }
    }//GEN-LAST:event_botonCamara2ActionPerformed
    /**
     * Realiza una captura de un video o de la imagen que esté captando la WebCam
     * @param evt acción de pulsar el botón Captura
     */
    private void botonCaptura2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonCaptura2ActionPerformed
        if(panelEscritorio.getSelectedFrame() instanceof VentanaInternaJMFPlayer){
            this.viJMF= (VentanaInternaJMFPlayer)(panelEscritorio.getSelectedFrame());
            player=viJMF.getPlayer();
        }else if(panelEscritorio.getSelectedFrame() instanceof VentanaInternaCamara){
            this.viC=(VentanaInternaCamara)(panelEscritorio.getSelectedFrame());
            player=viC.getPlayer();
        }
        try{
            FrameGrabbingControl fgc;
            String  claseCtr = "javax.media.control.FrameGrabbingControl";
            fgc = (FrameGrabbingControl)player.getControl(claseCtr );
            Buffer bufferFrame = fgc.grabFrame(); 

            BufferToImage bti;
            bti=new BufferToImage((VideoFormat)bufferFrame.getFormat());
            Image img = bti.createImage(bufferFrame);
            
            vi= new VentanaInterna();
            ((VentanaInterna)this.vi).getLienzo().setImage((BufferedImage)img);
            this.panelEscritorio.add(vi);
            vi.setTitle("Captura");
            vi.setVisible(true);            
            
        }catch(Exception ex){
                System.err.println("Error al realizar captura");
        }
    }//GEN-LAST:event_botonCaptura2ActionPerformed
    /**
     * Aplica el Negativo a la imagen seleccionada
     * @param evt acción de pulsar el botón Negativo
     */
    private void botonNegativoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonNegativoActionPerformed
        JInternalFrame vi= panelEscritorio.getSelectedFrame();
                                      
        if ((vi != null)&&(vi instanceof VentanaInterna)) {
            this.imgSource=((VentanaInterna)vi).getLienzo().getImage();
            if(imgSource!=null){
                
                int r,g,b;
                
                for(int i=0;i<imgSource.getWidth();i++){
                    for(int j=0;j<imgSource.getHeight();j++){
                        //se obtiene el color del pixel
                        Color color = new Color(imgSource.getRGB(i, j));
                        //se extraen los valores RGB
                        r = color.getRed();
                        g = color.getGreen();
                        b = color.getBlue();
                        //se coloca en la nueva imagen con los valores invertidos
                        imgSource.setRGB(i, j, new Color(255-r,255-g,255-b).getRGB());                                                                    
                    }
                }               
                ((VentanaInterna)vi).getLienzo().repaint();
            }
        }
    }//GEN-LAST:event_botonNegativoActionPerformed
    /**
     * Configura el polígono como la figura dibujar
     * @param evt acción de pulsar el botón Poligono
     */
    private void botonPoligonoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonPoligonoActionPerformed
        this.barraEstado.setText("Rectangulos Redondeados");
        JInternalFrame vi= panelEscritorio.getSelectedFrame();
        
        if((vi != null)&&(vi instanceof VentanaInterna)){
            ((VentanaInterna)vi).getLienzo().setObjeto(8);
            ((VentanaInterna)vi).getLienzo().setEditar(false);
        }
    }//GEN-LAST:event_botonPoligonoActionPerformed
    /**
     * hace que el trazo de dibujado sea discontinuo 
     * @param evt acción de pulsar el botón de trazo discontinuo
     */
    private void botonTrazoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonTrazoActionPerformed
        JInternalFrame vi=panelEscritorio.getSelectedFrame();

        if((vi != null)&&(vi instanceof VentanaInterna)){
            if(this.botonTrazo.isSelected()){
                ((VentanaInterna)vi).getLienzo().trazo(true);
            }else{
                ((VentanaInterna)vi).getLienzo().trazo(false);
            }
            ((VentanaInterna)vi).getLienzo().repaint();
        }       
    }//GEN-LAST:event_botonTrazoActionPerformed
    /**
     * Selecciona el color del trazo al que se le asignará un color
     * @param evt acción de pulsar el botón del color  del trazo
     */
    private void botonColorTrazoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonColorTrazoActionPerformed
        JInternalFrame vi= panelEscritorio.getSelectedFrame();

        if((vi != null)&&(vi instanceof VentanaInterna)){
            if(this.botonColorTrazo.isSelected()){
                ((VentanaInterna)vi).getLienzo().setActiColorTr(true);
                //vi.getLienzo().setColorTrazo(Color.BLACK);
            }else{
                ((VentanaInterna)vi).getLienzo().setActiColorTr(false);
            }
            ((VentanaInterna)vi).getLienzo().repaint();
        }
     
        
    }//GEN-LAST:event_botonColorTrazoActionPerformed
    /**
     * Selecciona el color del Relleno al que se le asignará un color
     * @param evt acción de pulsar el botón del color del Relleno
     */
    private void botonColorRellenoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonColorRellenoActionPerformed
        JInternalFrame vi= panelEscritorio.getSelectedFrame();

        if((vi != null)&&(vi instanceof VentanaInterna)){
            if(this.botonColorRelleno.isSelected()){
                ((VentanaInterna)vi).getLienzo().setActiColorFill(true);
            }else{
                ((VentanaInterna)vi).getLienzo().setActiColorFill(false);
            }
            ((VentanaInterna)vi).getLienzo().repaint();
        }       
    }//GEN-LAST:event_botonColorRellenoActionPerformed
    /**
     * Aplica la operación de escala de grises a la imagen seleccioanda
     * @param evt acción de pulsar el botón de la escala de grises
     */
    private void botonGrisesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonGrisesActionPerformed
        JInternalFrame vi= panelEscritorio.getSelectedFrame();
        
                                
        if ((vi != null)&&(vi instanceof VentanaInterna)) {
            BufferedImage imagenSrc=((VentanaInterna)vi).getLienzo().getImage();
            if(imagenSrc!=null){
               ICC_Profile ip;  
               ip = ICC_Profile.getInstance(ColorSpace.CS_GRAY); 
               ColorSpace cs = new ICC_ColorSpace(ip); 
               ColorConvertOp ccop = new ColorConvertOp(cs,null); 
               BufferedImage imgDest = ccop.filter(imagenSrc,null); 
                
                ((VentanaInterna)vi).getLienzo().setImage(imgDest);
                ((VentanaInterna)vi).getLienzo().repaint();
            }
            
        }
       
    }//GEN-LAST:event_botonGrisesActionPerformed
    /**
     * Duplica la imagen seleccionada creando una nueva ventana interna con la imagen
     * @param evt acción de pulsar el botón de duplicado de una imagen
     */
    private void botonDuplicadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonDuplicadoActionPerformed
        JInternalFrame vi= panelEscritorio.getSelectedFrame();
        if ((vi != null)&&(vi instanceof VentanaInterna)) {
            BufferedImage imgSrc =  ((VentanaInterna)vi).getLienzo().getImage();
            
            if(imgSrc!=null){
                try {
                        
                        vi = new VentanaInterna();
                         ((VentanaInterna)vi).getLienzo().setImage(imgSrc);
                        this.panelEscritorio.add(vi);
                        vi.setVisible(true);
                    }
                    catch (IllegalArgumentException e) {
                        System.err.println("Error: "+e.getLocalizedMessage());
                    } 
            }
             
        }
    }//GEN-LAST:event_botonDuplicadoActionPerformed
    /**
     * Aplica la operación  de tintado a la imagen seleccionada
     * @param evt acción de pulsar el botón de tintado
     */
    private void botonTintadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonTintadoActionPerformed
        JInternalFrame vi=panelEscritorio.getSelectedFrame();
        
                                
        if ((vi != null)&&(vi instanceof VentanaInterna)) {
            BufferedImage imagenSrc=((VentanaInterna)vi).getLienzo().getImage();
            if(imagenSrc!=null){
               TintOp tinta= new TintOp(((VentanaInterna)vi).getLienzo().getColorTrazo(), (float) 0.5);
                BufferedImage imgDest = tinta.filter(imagenSrc, null);
                
                ((VentanaInterna)vi).getLienzo().setImage(imgDest);
                ((VentanaInterna)vi).getLienzo().repaint();
            }
            
        }
    }//GEN-LAST:event_botonTintadoActionPerformed
    /**
     * Aplica el umbralizado a la imagen seleccionada  con el valor de la barra deslizadora
     * @param evt acción de cambiar el estado de la barra deslizadora
     */
    private void sliderUmbralizacionStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_sliderUmbralizacionStateChanged
        JInternalFrame vi= panelEscritorio.getSelectedFrame();
        if ((vi != null)&&(vi instanceof VentanaInterna)) {
            if(this.imgSource!=null){
                try{
                    UmbralizacionOp umbral = new UmbralizacionOp(this.sliderUmbralizacion.getValue()); 
                    BufferedImage imgdest = umbral.filter(imgSource, null);
                    ((VentanaInterna)vi).getLienzo().setImage(imgdest);
                    ((VentanaInterna)vi).getLienzo().repaint();
                } catch(IllegalArgumentException e){
                    System.err.println(e.getLocalizedMessage());
                }
            }
            
        }
    }//GEN-LAST:event_sliderUmbralizacionStateChanged
    /**
     * Prapara la imagen para aplicarle la opción de umbralizado
     * @param evt acción de pulsar la barra deslizadora
     */
    private void sliderUmbralizacionFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_sliderUmbralizacionFocusGained
        JInternalFrame vi= panelEscritorio.getSelectedFrame();
        if((vi != null)&&(vi instanceof VentanaInterna)){
            this.imgSource=((VentanaInterna)vi).getLienzo().getImage();
        }
    }//GEN-LAST:event_sliderUmbralizacionFocusGained
    /**
     * Actualiza la imagen con el valor de la barra deslizadora
     * @param evt acción de soltar la barra deslizadora
     */
    private void sliderUmbralizacionFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_sliderUmbralizacionFocusLost
        JInternalFrame vi= panelEscritorio.getSelectedFrame();
        if((vi != null)&&(vi instanceof VentanaInterna)){
            this.imgSource=null;
        }
    }//GEN-LAST:event_sliderUmbralizacionFocusLost
    /**
     * Selecciona el color del diálogo JColorChooser para dibujar
     * @param evt acción de pulsar el botón de la paleta de colores
     */
    private void botonSeleccionColorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonSeleccionColorActionPerformed
        JInternalFrame vi;
        vi= panelEscritorio.getSelectedFrame();
        
        if((vi != null)&&(vi instanceof VentanaInterna)){
            JColorChooser selectColor= new JColorChooser();
            Color color=selectColor.showDialog(this, "Selección de color", Color.BLACK);
            selectColor.setVisible(true);
            if(this.botonColorTrazo.isSelected()){
                this.botonColorTrazo.setBackground(color);
                this.botonColorTrazo.setForeground(Color.BLACK);
                ((VentanaInterna)vi).getLienzo().setColorTrazo(color);
            }
            if(this.botonColorRelleno.isSelected()){
                this.botonColorRelleno.setBackground(color);
                this.botonColorRelleno.setForeground(Color.BLACK);
                ((VentanaInterna)vi).getLienzo().setColorRelleno(color);
            }
            ((VentanaInterna)vi).getLienzo().repaint();
        }
        
    }//GEN-LAST:event_botonSeleccionColorActionPerformed
    /**
     * Muestra información de la aplicación
     * @param evt acción de pulsar el botón Acerca de del menú Ayuda
     */
    private void botonInformacionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonInformacionActionPerformed
        JOptionPane ventError = new JOptionPane();
        ventError.showMessageDialog(this, "Paint\nVersión Final\nManuel Alonso Braojos", "Acerca de",1);
        ventError.setVisible(true);
    }//GEN-LAST:event_botonInformacionActionPerformed
    /**
     * Reproduce Archivos de audio y de video
     * @param evt acción de pulsar el botón Play de reproducción
     */
    private void botonPlayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonPlayActionPerformed
        try{
        if(panelEscritorio.getSelectedFrame() instanceof VentanaInternaReproductor){ 
            this.viS=(VentanaInternaReproductor)(panelEscritorio.getSelectedFrame());
            SMClipPlayer player=viS.getPlayer();
            player.play();
        
        }else if(panelEscritorio.getSelectedFrame() instanceof VentanaInternaJMFPlayer){
            this.viJMF= (VentanaInternaJMFPlayer)(panelEscritorio.getSelectedFrame());
            this.player=viJMF.getPlayer();
            this.player.start();
            
        }
        
        
            
            
            
        }catch(Exception ex){
            System.err.println("Error al reproducir");
        }
    }//GEN-LAST:event_botonPlayActionPerformed
    /**
     * Detiene la reproducción de los archivos de audio y video, y detiene la grabación de audio
     * @param evt 
     */
    private void botonStopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonStopActionPerformed
        
        try{
            if(panelEscritorio.getSelectedFrame() instanceof VentanaInternaGrabador){
                this.viR= (VentanaInternaGrabador)(panelEscritorio.getSelectedFrame());
                SMSoundRecorder player=viR.getPlayer();
                player.stop();

            }else if(panelEscritorio.getSelectedFrame() instanceof VentanaInternaReproductor){ 
                this.viS=(VentanaInternaReproductor)(panelEscritorio.getSelectedFrame());
                SMClipPlayer player=viS.getPlayer();
                player.stop();
            }else if(panelEscritorio.getSelectedFrame() instanceof VentanaInternaJMFPlayer){
                this.viJMF= (VentanaInternaJMFPlayer)(panelEscritorio.getSelectedFrame());
                player=viJMF.getPlayer();
                player.stop();
            }
         
        }catch(Exception ex){
                System.err.println("Error al reproducir");
        }
    }//GEN-LAST:event_botonStopActionPerformed
    /**
     * Oculta o hace visible la barra de atributos
     * @param evt acción de pulsar el botón Ver barra de atributos
     */
    private void botonBarraAtributosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonBarraAtributosActionPerformed
        this.barraAtributos.setVisible(botonBarraAtributos.getState());
    }//GEN-LAST:event_botonBarraAtributosActionPerformed
    /**
     * Oculta o hace visible la barra general
     * @param evt acción de pulsar el botón Ver barra general
     */
    private void botonBarraGeneralActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonBarraGeneralActionPerformed
        this.barraGeneral.setVisible(botonBarraGeneral.getState());
    }//GEN-LAST:event_botonBarraGeneralActionPerformed
    /**
     * Aplica la operación propia Visión Nocturna
     * @param evt acción de pulsar el botón de visión nocturna
     */
    private void botonVisionNocturnaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonVisionNocturnaActionPerformed
        JInternalFrame vi= panelEscritorio.getSelectedFrame();
        
                                
        if ((vi != null)&&(vi instanceof VentanaInterna)) {
            BufferedImage imagenSrc=((VentanaInterna)vi).getLienzo().getImage();
            if(imagenSrc!=null){
               VisionNocturnaOp vNocturna= new VisionNocturnaOp();
                imagenSrc = vNocturna.filter(imagenSrc, imagenSrc);
                
                ((VentanaInterna)vi).getLienzo().setImage(imagenSrc);
                ((VentanaInterna)vi).getLienzo().repaint();
            }
            
        }
    }//GEN-LAST:event_botonVisionNocturnaActionPerformed

   
    
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuBar MenuPrincipal;
    private javax.swing.JToolBar barraAtributos;
    private javax.swing.JToolBar barraAudio;
    private javax.swing.JLabel barraEstado;
    private javax.swing.JToolBar barraFormas;
    private javax.swing.JToolBar barraGeneral;
    private javax.swing.JToolBar barraOperacionesImg;
    private javax.swing.JMenuItem botonAbrir;
    private javax.swing.JButton botonAbrir2;
    private javax.swing.JToggleButton botonAlisar;
    private javax.swing.JToggleButton botonAmarillo;
    private javax.swing.JMenu botonArchivo;
    private javax.swing.JToggleButton botonArco;
    private javax.swing.JButton botonAumentarEscala;
    private javax.swing.JToggleButton botonAzul;
    private javax.swing.JCheckBoxMenuItem botonBarraAtributos;
    private javax.swing.JCheckBoxMenuItem botonBarraGeneral;
    private javax.swing.JCheckBoxMenuItem botonBarraOperaciones;
    private javax.swing.JToggleButton botonBlanco;
    private javax.swing.JButton botonCamara2;
    private javax.swing.JButton botonCaptura2;
    private javax.swing.JToggleButton botonCirculo;
    private javax.swing.JToggleButton botonColorRelleno;
    private javax.swing.JToggleButton botonColorTrazo;
    private javax.swing.JToggleButton botonCurva;
    private javax.swing.JToggleButton botonDegradadoH;
    private javax.swing.JToggleButton botonDegradadoV;
    private javax.swing.JButton botonDisminEscala;
    private javax.swing.JButton botonDuplicado;
    private javax.swing.JMenu botonEdicion;
    private javax.swing.JToggleButton botonEditar;
    private javax.swing.JButton botonGrises;
    private javax.swing.JSpinner botonGrosor;
    private javax.swing.JMenuItem botonGuardar;
    private javax.swing.JButton botonGuardar2;
    private javax.swing.JButton botonIluminado;
    private javax.swing.JMenuItem botonInformacion;
    private javax.swing.JToggleButton botonLinea;
    private javax.swing.JButton botonMultiplicacion;
    private javax.swing.JButton botonNegativo;
    private javax.swing.JToggleButton botonNegro;
    private javax.swing.JButton botonNormal;
    private javax.swing.JMenuItem botonNuevo;
    private javax.swing.JButton botonNuevo2;
    private javax.swing.JButton botonOscurecer;
    private javax.swing.JButton botonPlay;
    private javax.swing.JToggleButton botonPoligono;
    private javax.swing.JToggleButton botonPunto;
    private javax.swing.JButton botonRecord;
    private javax.swing.JToggleButton botonRectangulo;
    private javax.swing.JToggleButton botonRelleno;
    private javax.swing.JButton botonResta;
    private javax.swing.JToggleButton botonRojo;
    private javax.swing.JButton botonRotacion180;
    private javax.swing.JButton botonRotacion270;
    private javax.swing.JButton botonRotacion90;
    private javax.swing.JToggleButton botonRoundRectangulo;
    private javax.swing.JButton botonSeleccionColor;
    private javax.swing.JButton botonSenoidal;
    private javax.swing.JButton botonSepia;
    private javax.swing.JButton botonStop;
    private javax.swing.JButton botonSuma;
    private javax.swing.JButton botonTintado;
    private javax.swing.JToggleButton botonTrazo;
    private javax.swing.JToggleButton botonVerde;
    private javax.swing.JToggleButton botonVisionNocturna;
    private javax.swing.JCheckBoxMenuItem botonVistaBarraEstado;
    private javax.swing.JCheckBoxMenuItem botonVistaBarraFormas;
    private javax.swing.JComboBox boxFiltros;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JFileChooser jFileChooser1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar.Separator jSeparator3;
    private javax.swing.JSlider jSlider;
    private javax.swing.JLabel labelRGB;
    private javax.swing.JMenu menuAyuda;
    private javax.swing.JPanel panelBinario;
    private javax.swing.JPanel panelBrillo;
    private javax.swing.JPanel panelColores;
    private javax.swing.JPanel panelContraste;
    private javax.swing.JPanel panelEscalar;
    private javax.swing.JDesktopPane panelEscritorio;
    private javax.swing.JPanel panelFiltros;
    private javax.swing.JPanel panelRotacion;
    private javax.swing.JPanel panelSenoidal;
    private javax.swing.JLabel posX;
    private javax.swing.JLabel posY;
    private javax.swing.JSlider sliderBinario;
    private javax.swing.JSlider sliderRotacion;
    private javax.swing.JSlider sliderTransparencia;
    private javax.swing.JSlider sliderUmbralizacion;
    // End of variables declaration//GEN-END:variables
}
