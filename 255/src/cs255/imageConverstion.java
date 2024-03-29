package cs255;

import java.awt.Color;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JSlider;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.statistics.HistogramType;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 * This class has different methods to manipulate the image in different ways
 * @author Robert
 */
public class imageConverstion {
        private final int MAXRGB = 255;
	private final double A =1.0;
	 
   /**
    * Method to convert a BufferedImage to a Byte[]
    * @param BufferedImage - image to covert
    * @return byte[] - byte array of the converted image
    */
    public static byte[] GetImageData(BufferedImage image) {
            WritableRaster WR=image.getRaster();
            DataBuffer DB=WR.getDataBuffer();
            if (DB.getDataType() != DataBuffer.TYPE_BYTE)
                throw new IllegalStateException("That's not of type byte");
          
            return ((DataBufferByte) DB).getData();
    }

/**
 * Method that takes a buffered image and equalise it. 
 * @param BufferedImage image to equalise
 * @return BufferedImahe Equalise image
 */
public BufferedImage equalized(BufferedImage image){
    int w=image.getWidth(), h=image.getHeight(), i, j, c;	
    byte[] data = GetImageData(image);
    int[] brHist= new int[256];
    int[] t = new int[256];
    int[] map = new int[256];
    int count = 0;
    
    
    for (j=0; j<h; j++) {
                    for (i=0; i<w; i++) {
                            int sum =0;
                            
                            sum = (data[0+3*i+3*j*w]&MAXRGB)+(data[1+3*i+3*j*w]&MAXRGB)+(data[2+3*i+3*j*w]&MAXRGB);
        
                            brHist[(int)Math.round(sum/3.0)]++;
                            //System.out.println(sum);
                            
                    } 
            } 
    t[0] = brHist[0];
    
    for(i = 1; i< 256; i++){
        t[i]= t[i-1]+brHist[i];
        map[i]=Math.max(0,(int)Math.round((255.0*t[i])/(h*w))-1);
    }
    
    for (j=0; j<h; j++) {
                    for (i=0; i<w; i++) {
                            for (c=0; c<3; c++) {
                                    data[c+3*i+3*j*w]=(byte) map[(data[c+3*i+3*j*w]&MAXRGB)];
                            } // colour loop
                    } 
            }
    
   return image; 
    
}

public BufferedImage colorEqualized(BufferedImage image){
    int w=image.getWidth(), h=image.getHeight(), i, j, c;	
    byte[] data = GetImageData(image);
    System.out.println("data lengths =" +data.length);
    
    double[] hmap = new double[data.length];
    int [] vHist= new int[256];
    int[] t = new int[256];
    int[] map = new int[256];
    int count = 0;
    
  
    //Color cl = new Color(Color.HSBtoRGB(hsv[0], hsv[1], hsv[2]));
   // int red = cl.getRed();
    
    
    
    for (j=0; j<h; j++) {
                    for (i=0; i<w; i++) {
                            int sum =0;
                            float[] hsv = new float[3];
                            Color.RGBtoHSB((data[0+3*i+3*j*w]&MAXRGB),(data[1+3*i+3*j*w]&MAXRGB),(data[2+3*i+3*j*w]&MAXRGB),hsv);
                            vHist[(int)Math.round((hsv[2]*255))]++;
                            //System.out.println(hsv[2]*255);
                           
                            
                    } 
            } 
   t[0] = vHist[0];
    
    for(i = 1; i< 256; i++){
        t[i]= t[i-1]+vHist[i];
        map[i]=Math.max(0,(int)Math.round((255.0*t[i])/(h*w))-1);
    }
    
 for (j=0; j<h; j++) {
                    for (i=0; i<w; i++) {
                        int r = (data[0+3*i+3*j*w]&MAXRGB);
                        int g = (data[1+3*i+3*j*w]&MAXRGB);
                        int b = (data[2+3*i+3*j*w]&MAXRGB);
                        
                        
                        
                            for (c=0; c<3; c++) {
                                    
                                    data[c+3*i+3*j*w]= (byte)getColor(r,g,b,(data[c+3*i+3*j*w]&MAXRGB),c,map);
                            
                            } // colour loop
                    } 
            }
    
          
    
    
    
    
    for(i =0; i<vHist.length; i++){
        System.out.println(vHist[i]);
    }
   return image; 
    
}
public int getColor(int r , int g, int b,int p, int c, int[] map ){
    float[] hsv = new float[3];
    Color.RGBtoHSB(r,g,b,hsv);
    Color cl = new Color(Color.HSBtoRGB(hsv[0], hsv[1],(float)((map[p])/255.0) ));
   int out = 0;
    switch(c){
        case 0: out = cl.getRed();
            break;
        case 1: out = cl.getGreen();
            break;
        case 2: out = cl.getBlue();
            break;
            
    }
    //System.out.println("out = "+ out);
    
    
  return out;
}
    
    
    
    
    /*
        This function shows how to carry out an operation on an image.
        It obtains the dimensions of the image, and then loops through
        the image carrying out the invert.
    */
    public BufferedImage Invert(BufferedImage image) {
            int w=image.getWidth(), h=image.getHeight(), i, j, c;
            byte[] data = GetImageData(image);
			
			for(i=0; i < data.length; i++){
				data[i]=(byte) (MAXRGB-(data[i]&MAXRGB));
			}
			return image;
    }

    public BufferedImage SlowGamma(BufferedImage image,int gamma) {
            //Get image dimensions, and declare loop variables
            int w=image.getWidth(), h=image.getHeight(), i, j, c;
			byte[] data = GetImageData(image);
			
			for(i=0; i < data.length; i++){
				data[i]=(byte) ((Math.pow((((data[i]&MAXRGB)/(MAXRGB+0.0))/A),(1.0/(gamma/10.0))))*255);
				
			}
            return image;
    }
	
    
   
    public BufferedImage FastGamma(BufferedImage image, int gamma) {
            //Get image dimensions, and declare loop variables
        
        byte[] lut = new byte[MAXRGB+1];
        System.out.println("image size = "+lut.length);
         for(int i = 0; i < 256; i++ ){
            lut[i] = (byte)((Math.pow((((i)/(MAXRGB+0.0))/A),(1.0/(gamma/10.0))))*255);			
	}
            int w=image.getWidth(), h=image.getHeight(), i, j, c;
			byte[] data = GetImageData(image);
			for(i= data.length -1; i !=0; i--){
				data[i]= lut[(data[i]&MAXRGB)];
			}
            return image;
    }

    public BufferedImage contrastStretching(int x , int y, int x1, int y1, BufferedImage image){
        final double lowgr = (y-0.0)/(x-0.0);
        final double midgr = (y1-y+0.0)/(x1-x+0.0);
        final double topgr = (MAXRGB -y1+0.0)/(MAXRGB-x1+0.0);
        byte[] newGammaTable = new byte[256];
        
        System.out.println("low gr = "+lowgr+" mid gr "+midgr+" top gr ="+topgr);
        
        for(int i =0; i<=MAXRGB;i++){
            if(i<x){
                newGammaTable[i] = (byte)(i*lowgr);
            }else if((i>=x)&&(i<x1)){
                newGammaTable[i] = (byte)(midgr * (i-x)+y);
                
            }else{
                newGammaTable[i] = (byte)(topgr*(i-x1)+y1);
            }
            
        }
        
        
        
        byte[] data = GetImageData(image);
	for(int i= data.length -1; i !=0; i--){
            data[i]= newGammaTable[(data[i]&MAXRGB)];
	}
        
        return image;
        
        
        
    }
    
    
    

   public ChartPanel createHistogram(BufferedImage image,int channel, Boolean all, String title,XYBarRenderer br){
        int w=image.getWidth(), h=image.getHeight(), i, j;
        byte[] data = GetImageData(image);
        int[] hist = new int[256];
        int pixelCount = data.length;
        
        if(!all){
            
            for (j=0; j<h; j++) {
                    for (i=0; i<w; i++) {
                            
                                     hist[(data[channel+3*i+3*j*w]&MAXRGB)]++;
                            
                    }
            } 
        }else{
             for (j=0; j<h; j++) {
              for (i=0; i<w; i++) {
                  double sum = 0;
                         sum = sum + (data[0+3*i+3*j*w]&255);
                         sum = sum + (data[1+3*i+3*j*w]&255);
                         sum = sum + (data[2+3*i+3*j*w]&255);
                
                       hist[(int)Math.round(sum/3.0)]++;
               
              } 
            }   
        }
        
        
        XYSeries dataset = new XYSeries("Histogram");
        
        for( i = 0; i<hist.length; i++){
              dataset.add(i,hist[i]);
        }
        
       XYSeriesCollection col = new XYSeriesCollection();
       col.addSeries(dataset);
        
       JFreeChart chart = ChartFactory.createXYBarChart(
            title,
            "Pixel Value", 
            false,
            "Intensity", 
            col,
            PlotOrientation.VERTICAL,
            false, 
            false,
            false
        );
       XYPlot plot = chart.getXYPlot();
       plot.setRenderer(br);
       
        ChartPanel myChart = new ChartPanel(chart);
        myChart.setMouseWheelEnabled(true);
      System.out.println(myChart);
       return myChart;
   }
   
   
   
       public BufferedImage cross (BufferedImage image, int[][] matrix) {
    
          int t =0;
          int f =0;
        
          byte[] data = GetImageData(image);
          int[][] map = new int[data.length][3];
          
          int w=image.getWidth(), h=image.getHeight(), i, j, c;
          for (j=0; j<h; j++) {
              for (i=0; i<w; i++) {
                  for (c=0; c<3; c++) {
                            // data[c+3*i+3*j*w]=(byte) (gammaLUT[(data[c+3*i+3*j*w]&255)]);  <- reference
                            if((j-(matrix.length/2)>0)&&(j+(matrix.length/2)<h)&&((i-(matrix[0].length/2)>0)&&(i+(matrix[0].length/2)<w))){
                               int sum = 0;
                               
                               int newHPos = j-(matrix.length/2);
                               int newWPos = i-(matrix[0].length/2);
                               
                               
                                for (int k = 0; k<matrix.length; k++) {
                                    newWPos = i-(matrix[0].length/2);
                                    for (int l = 0; l<matrix.length; l++) {
                                        sum +=matrix[k][l]*(data[c+3*newWPos+3*newHPos*w]&255);
                                        newWPos++;
                                                                            
                                       
                                    }
                                    newHPos++ ;       
                                }
                              
                                map[c+3*i+3*j*w][c]=sum;                                
                                
                                
                            }else  {
                                data[c+3*i+3*j*w]=(byte) (0&255);
                            }
                            
                            
                            
                            
                            
                            
                            
                  }
            }
        }
        System.out.println("true = "+t+" f = "+f+"ratio = "+(((f+0.0)/t)*100)+"%");
        int rgbMax = 0;
        int rgbMin = 0;
        
        
        for (j=0; j<h; j++) {
                      for (i=0; i<w; i++) {
                          for (c=0; c<3; c++) {
                                    int test = map[c+3*i+3*j*w][c];
                                    if(test < rgbMin){
                                        rgbMin = test;
                                    }else if(test > rgbMax){
                                        rgbMax = test;
                                    }                       
                    }
                    
        }
                    
        }

        for (j=0; j<h; j++) {
                              for (i=0; i<w; i++) {
                                  for (c=0; c<3; c++) {
                                       int am = map[c+3*i+3*j*w][c];
                                       data[c+3*i+3*j*w]=(byte) ( ((am-rgbMin)*255)/(rgbMax-rgbMin)  );               
                            }
                       }     
                }
        
        
        
        
        
        
        return image;
        
        
    }
     
    public BufferedImage grayOut(BufferedImage img) {
        ColorConvertOp colorConvert = new ColorConvertOp(ColorSpace
                .getInstance(ColorSpace.CS_GRAY), null);
        colorConvert.filter(img, img);
 
        return img;
    }
   
   
}
