/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cs255;

import java.awt.image.BufferedImage;
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
 *
 * @author Robert
 */
public class imageConverstion {
    
    BufferedImage image;
	public static final int maxRGB = 255;
	public static final double a =1.0;
	 
   
    public static byte[] GetImageData(BufferedImage image) {
            WritableRaster WR=image.getRaster();
            DataBuffer DB=WR.getDataBuffer();
            if (DB.getDataType() != DataBuffer.TYPE_BYTE)
                throw new IllegalStateException("That's not of type byte");
          
            return ((DataBufferByte) DB).getData();
    }


    public BufferedImage Equalise(BufferedImage image) {
        
    byte[] data = GetImageData(image); 
    int pixelCount = data.length;
    int[] hist = new int[256];
    int[] lut = new int[256];
    int [] outImage = new int[data.length];
    int sum =0;
    int i;
    for ( i = 0; i < pixelCount; ++i )
    {
        hist[data[i]&255]++;
        
    }
    

    
     for ( i=0; i < hist.length; ++i ){
            sum += hist[i];
            lut[i] = sum * 255 / pixelCount;
        }

        // transform image using sum histogram as a LUT
        for ( i = 0; i < pixelCount; ++i ){
            data[i] = (byte)lut[data[i]&255];
        }
        return image;
    
            
    }

    /*
        This function shows how to carry out an operation on an image.
        It obtains the dimensions of the image, and then loops through
        the image carrying out the invert.
    */
    public BufferedImage Invert(BufferedImage image) {
            //Get image dimensions, and declare loop variables
            int w=image.getWidth(), h=image.getHeight(), i, j, c;
            //Obtain pointer to data for fast processing
            byte[] data = GetImageData(image);
            
            //Shows how to loop through each pixel and colour
            //Try to always use j for loops in y, and i for loops in x
            //as this makes the code more readable
           /* 
		   Loop below nice and clever but unessary and overly complex
		   for (j=0; j<h; j++) {
                    for (i=0; i<w; i++) {
                            for (c=0; c<3; c++) {
                                    data[c+3*i+3*j*w]=(byte) (maxRGB-(data[c+3*i+3*j*w]&maxRGB));
                            } // colour loop
                    } // column loop
            } // row loop
            
			*/
			
			for(i=0; i < data.length; i++){
				data[i]=(byte) (maxRGB-(data[i]&maxRGB));
			}
			return image;
    }

    public BufferedImage SlowGamma(BufferedImage image,int gamma) {
            //Get image dimensions, and declare loop variables
            int w=image.getWidth(), h=image.getHeight(), i, j, c;
			byte[] data = GetImageData(image);
			
			for(i=0; i < data.length; i++){
				data[i]=(byte) ((Math.pow((((data[i]&maxRGB)/(maxRGB+0.0))/a),(1.0/(gamma/10.0))))*255);
				
			}
            return image;
    }
	
    
   
    public BufferedImage FastGamma(BufferedImage image, int gamma) {
            //Get image dimensions, and declare loop variables
        
        byte[] lut = new byte[maxRGB+1];
        System.out.println("image size = "+lut.length);
         for(int i = 0; i < 256; i++ ){
            lut[i] = (byte)((Math.pow((((i)/(maxRGB+0.0))/a),(1.0/(gamma/10.0))))*255);			
	}
            int w=image.getWidth(), h=image.getHeight(), i, j, c;
			byte[] data = GetImageData(image);
			for(i= data.length -1; i !=0; i--){
				data[i]= lut[(data[i]&maxRGB)];
			}
            return image;
    }
   
    
    public BufferedImage BlueFade(BufferedImage image) {
            //Get image dimensions, and declare loop variables
            int w=image.getWidth(), h=image.getHeight(), i, j, c;
            //Obtain pointer to data for fast processing
            byte[] data = GetImageData(image);
            int int_image[][][];
            double t;
            
            int_image = new int[h][w][3];
            
            // Copy byte data to new image taking care to treat bytes as unsigned
            for (j=0; j<h; j++) {
                    for (i=0; i<w; i++) {
                            for (c=0; c<3; c++) {
                                    int_image[j][i][c]=data[c+3*i+3*j*w]&maxRGB;
                            } // colour loop
                    } // column loop
            } // row loop
            
            // Now carry out processing on this different data typed image (e.g. correlation or "bluefade"
            for (j=0; j<h; j++) {
                    for (i=0; i<w; i++) {
                                int_image[j][i][0]=maxRGB*j/h; //BLUE
                                int_image[j][i][1]=0; //GREEN
                                int_image[j][i][2]=0; //RED
                    } // column loop
            } // row loop
            
            // Now copy the processed image back to the original
            for (j=0; j<h; j++) {
                    for (i=0; i<w; i++) {
                            for (c=0; c<3; c++) {
                                    data[c+3*i+3*j*w]=(byte) int_image[j][i][c];
                            } // colour loop
                    } // column loop
            } // row loop
            

            return image;
    }
    
    public BufferedImage contrastStretching(int x , int y, int x1, int y1, BufferedImage image){
        final double lowgr = (y-0.0)/(x-0.0);
        final double midgr = (y1-y+0.0)/(x1-x+0.0);
        final double topgr = (maxRGB -y1+0.0)/(maxRGB-x1+0.0);
        byte[] newGammaTable = new byte[256];
        
        System.out.println("low gr = "+lowgr+" mid gr "+midgr+" top gr ="+topgr);
        
        for(int i =0; i<=maxRGB;i++){
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
            data[i]= newGammaTable[(data[i]&maxRGB)];
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
                            
                                     hist[(data[channel+3*i+3*j*w]&maxRGB)]++;
                            
                    }
            } 
        }else{
            for( i = 0; i < pixelCount; i++ ){
                hist[data[i]&255]++;
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
          int[] map = new int[data.length];
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
                                if((((c+3.0*newWPos+3.0*newHPos*w)/data.length)*100)>98){
                                 //System.out.println("error! whp = "+newHPos+" wwp = "+newWPos+" percent = "+((((c+3.0*newWPos+3.0*newHPos*w)/data.length)*100)));   
                                }
                                map[c+3*i+3*j*w]=sum;                                
                                
                                
                            }else  {
                                data[c+3*i+3*j*w]=(byte) (0&255);
                            }
                            
                            
                            
                            
                            
                            
                            
                  }
            }
        }
        System.out.println("true = "+t+" f = "+f+"ratio = "+(((f+0.0)/t)*100)+"%");
        int[] rgbMax = new int[3];
        int[] rgbMin = new int[3];
        
        
        for (j=0; j<h; j++) {
                      for (i=0; i<w; i++) {
                          for (c=0; c<3; c++) {
                                    int test = map[c+3*i+3*j*w];
                                    if(test < rgbMin[c]){
                                        rgbMin[c] = test;
                                    }else if(test > rgbMax[c]){
                                        rgbMax[c] = test;
                                    }                       
                    }
                    
        }
                    
        }
        System.out.println(" Max = R: "+rgbMax[0]+" G: "+rgbMax[1]+" B:"+rgbMax[2]);
        System.out.println(" Min = R: "+rgbMin[0]+" G: "+rgbMin[1]+" B:"+rgbMin[2]);
        
        for (j=0; j<h; j++) {
                              for (i=0; i<w; i++) {
                                  for (c=0; c<3; c++) {
                                       int am = map[c+3*i+3*j*w];
                                       data[c+3*i+3*j*w]=(byte) ( ((am-rgbMin[c])*225)/(rgbMax[c]-rgbMin[c])  );               
                            }
                       }     
                }
        
        
        
        
        
        
        return image;
        
        
    }
   
   
}
