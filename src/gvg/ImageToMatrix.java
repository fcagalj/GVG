/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gvg;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.io.File;
import javax.imageio.ImageIO;

/**
 * @author Aidos
 */
public class ImageToMatrix {
    //method:"convetImage".
    private int map[][];
    private int map_x;
    private int map_y;
    //method:"loadImage". Definition of variables of the paths
    private File inputImageFile;
    private Image mapImage;
    private File outputCSVFile;
    //method:"exportMatrixToImageFile".
    private File outputImageFile;
    private SampleModel sampleModel;
    //method:"ImageToMatrix".Declaration of the height and the width of the image as int
    public int origImgWidth, origImgHeight;
    ////method:"convertImage".
    private String outputImageFilePath;
    private String outputCSVFilePath;
    
    public ImageToMatrix(String inputImagePath, String outputImagePath, String outputCSVPath){
        loadImage(inputImagePath);
        this.outputCSVFilePath=outputCSVPath;
        this.outputImageFilePath=outputImagePath;
        this.outputImageFile=new File(outputImagePath);
        this.outputCSVFile=new File(outputCSVPath);
    }

    public void loadImage(String inputImagePath) {
    /** Load private inputFile from entered String path, and set
     * image info variables:
     * int origImgWidth
     * int origImgHeight
     * Image mapImage
     * File inputImageFile @param inputImagePath 
     */
        if (inputImagePath == null)
	    throw new RuntimeException("loadMap: null filename");
        inputImageFile=new File(inputImagePath);
	Toolkit tk = Toolkit.getDefaultToolkit();
	mapImage = tk.createImage(inputImagePath);
	int tries = 0;
	while (!tk.prepareImage(mapImage, -1, -1, null)) {
	    if (tries >= 20)
		throw new RuntimeException("map not ready after " +
					   tries + " tries");
	    tries++;
	    try {
		Thread.sleep(100);
	    } catch (Exception e) {
		System.out.println("loadMap: " + e);
		System.exit(-1);
	    }
	}
	origImgWidth = mapImage.getWidth(null);
	origImgHeight = mapImage.getHeight(null);
    //this is the end of loadImage method
    }
    

    public Image exportMatrixToImageFile(int pixels[][], String oututFilePath){
        /*
        Convert array back to Image, and export image to output file.
         */
        
         outputImageFile=new File(oututFilePath);
         int w=pixels.length;
         int h=pixels[0].length;
         WritableRaster raster= Raster.createWritableRaster(sampleModel, new Point(0,0));
         
         for(int i=0;i<w;i++)
         {
             for(int j=0;j<h;j++)
             {
                 raster.setSample(i,j,0,pixels[i][j]);
             }
         }
        BufferedImage image=new BufferedImage(w,h,BufferedImage.TYPE_BYTE_BINARY);
        image.setData(raster);
        try {
            ImageIO.write(image,"png",outputImageFile);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        return image;
        //this is the end of "exportMatrixToImageFile"
    }
    
    //??Shoud there be two methods "exportMatrixToImageFile" and "exportImageToFile"
     public void exportImageToFile(Image image, String oututFilePath){
     /*
     * Export image to file.
     * @param image
     * @param oututFilePath 
     */
        
         outputImageFile=new File(oututFilePath);
        if(!(image instanceof BufferedImage)){
            System.out.println("Unrecognized image "+image.toString()+"to export!");
            return;
        }
        BufferedImage bi= (BufferedImage) image;
         try {
            ImageIO.write(bi,"png",outputImageFile);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
         //this is the end of "exportImageToFile"
    }
     
    
     /*
     * Convert Image to array matrix and save matrix to private array 
     * int[][] map. First scale image by subsample prop.
     * Also saves output matrx to outputCSV and outptImage file.
     * @param im
     * @param subsample
     * @param floor
     * @return 
     */
     public int[][] convertImage(float subsample) {
	
        return convetImageImpl(mapImage, subsample);
        //return convertImageByte(mapImage, subsample, 0.9f);
    }
 
    private int[][] convetImageImpl(Image im, float subsample){
        // Image size
	int w = im.getWidth(null);
	int h = im.getHeight(null);

	// Scaled size
	int scw = (int) (w / subsample);
	int sch = (int) (h / subsample);
        
        map_x=scw;
        map_y=sch;
        
        // Draw the image into a black/white buffer
        BufferedImage bufBW = 
           new BufferedImage(scw, sch, BufferedImage.TYPE_BYTE_BINARY); //TYPE_BYTE_INDEXED
	Graphics g2 = bufBW.getGraphics();
	g2.drawImage(im, 0, 0, scw, sch, Color.black, null);
        try 
        {
            Raster raster=bufBW.getData();
            int[][] res=new int[scw][sch];
            for (int x=0;x<scw;x++)
            {
                for(int y=0;y<sch;y++)
                {
                    sampleModel = raster.getSampleModel();
                    res[x][y]=raster.getSample(x,y,0);
                    //res[x][y]=bufBW.getRGB(x,y);
                }
            }
            //exportMatrixToImageFile(res, "outputImageFileEmbed.png");
            MatrixImageTools.exportMatrixToImageFile(res, this.outputImageFilePath);
            MatrixImageTools.exportMapToCSV(res, this.outputCSVFilePath);
            map=res;
            return res;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    //this is the end of "convetImage"
    }
     
//this is the end of ImageToMatrix class


}
