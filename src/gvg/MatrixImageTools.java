/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gvg;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.MultiPixelPackedSampleModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import javax.imageio.ImageIO;
/**
 * Class with some static usefol methods that can be used in program.
 * @author frane
 */
public class MatrixImageTools {
    
    //Exploring the neighbours of the Cell
    public static  void findCellNeighbors(List<Cell> listToAddNeighb, int[][] map, int x, int y, boolean eightConnectivity){
    /*Searching arround given cell (x, y), and found neighbors add to 
     * list in argument. It is not important if the cell is on the edge; if 
     * doesnt exist it just wont be added. If parametar eightConnectivity is 
     * set true, function will search for eight neighburs, and if it is set to
     * false, it will search 4 neighburs.
     * 
     * Neighbors of X are signed as folows:
     *             "a", "b", "c"
     *             "d", "X", "e"
     *             "f", "g", "h"
     * @param listToAddNeighb
     * @param map
     * @param x
     * @param y 
     */
        
        //Cell a = null,b = null,c = null,d = null,e = null,f = null,g = null,h=null;
        //a        
        if(eightConnectivity&&((x-1)>=0)&&((y-1)>=0)){
                int neig_x, neig_y;
                neig_x=x-1;
                neig_y=y-1;
                Cell a=new Cell(neig_x, neig_y, map[neig_x][neig_y]);
                //Cell a=new Cell((x-1),(y-1),map[][]);
                listToAddNeighb.add(a);
        }
        //b
        if(((y-1)>=0)){
                int neig_x, neig_y;
                neig_x=x;
                neig_y=y-1;
                Cell b=new Cell(neig_x, neig_y, map[neig_x][neig_y]);
               // Cell b=new Cell((x),(y-1));
                listToAddNeighb.add(b);
        }
        //c
        if(eightConnectivity&&((x+1)<=(map.length-1)) && ((y-1)>=0)){
                int neig_x, neig_y;
                neig_x=x+1;
                neig_y=y-1;
                Cell c=new Cell(neig_x, neig_y, map[neig_x][neig_y]);
                //Cell c=new Cell((x+1),(y-1));
                listToAddNeighb.add(c);
        }
//        //d
        if(((x-1)>=0)){
                int neig_x, neig_y;
                neig_x=x-1;
                neig_y=y;
                Cell d=new Cell(neig_x, neig_y, map[neig_x][neig_y]);
                //Cell d=new Cell((x-1),(y));
                listToAddNeighb.add(d);
        }
        //e
        if(((x+1)<=(map.length-1))){
                int neig_x, neig_y;
                neig_x=x+1;
                neig_y=y;
                Cell e=new Cell(neig_x, neig_y, map[neig_x][neig_y]);
                //Cell e=new Cell((x+1),(y));
                listToAddNeighb.add(e);
        }
//        //f
        if(eightConnectivity&&((x-1)>=0) && ((y+1)<=(map[0].length-1))){
                int neig_x, neig_y;
                neig_x=x-1;
                neig_y=y+1;
                Cell f=new Cell(neig_x, neig_y, map[neig_x][neig_y]);
                //Cell f=new Cell((x-1),(y+1));
                listToAddNeighb.add(f);
        }
        //g
        if((y+1)<=(map[0].length-1)){
                int neig_x, neig_y;
                neig_x=x;
                neig_y=y+1;
                Cell g=new Cell(neig_x, neig_y, map[neig_x][neig_y]);
                //Cell g=new Cell((x),(y+1));
                listToAddNeighb.add(g);
        }
//        //h
        if(eightConnectivity&&((x+1)<=(map.length-1))&&(y+1)<=(map[0].length-1)){
                int neig_x, neig_y;
                neig_x=x+1;
                neig_y=y+1;
                Cell h=new Cell(neig_x, neig_y, map[neig_x][neig_y]);
                //Cell h=new Cell((x+1),(y+1));
                listToAddNeighb.add(h);
        }
        
    //the end of findCellNeighbors
    }
    
    /**
     * Output map to the console.
     */
    public static void printMapToConsole(int[][] map){
        int calc_x=0;
        // to find real y_size we have to find max y_size          
        for (int[] row : map) {
            if((row.length-1)>calc_x)
                calc_x=row.length;
            }
        System.out.println("***********************************");
        for(int i=0;i<map.length;i++){
            String line="";
            for(int j=0;j<map[0].length;j++){
                line+=map[i][j]+" ";
            }
            System.out.println("    Line "+i+": "+line);
        }
        System.out.println("***********************************");
    }
    /**
     * Output matrix state to the console during loop.
     */
    public static void printMapState(int[][] map, int iter){

        System.out.println("****************************");
        System.out.println(iter+". iteration:");
        for(int i=0;i<map.length;i++){
            String line="";
            for(int j=0;j<map[0].length;j++){
                line+=map[i][j]+" ";
            }
            System.out.println("    Line "+i+": "+line);
        }
        System.out.println("****************************");
    }
    /**
     * Output map to the CSV file.
     */
    public static void exportMapToCSV(int[][] map, String outputCSVpath){
        
        try
	{
	    FileWriter writer = new FileWriter(outputCSVpath);
 
            for(int i=0;i<map.length;i++){
                String line="";
                for(int j=0;j<map[0].length;j++){
                    line+=map[i][j]+";";
                    writer.append(String.valueOf(map[i][j]));
                    writer.append(';');
                }
                //System.out.println("    Line "+i+": "+line);
                writer.append('\n');
            }
	    //generate whatever data you want
 
	    writer.flush();
	    writer.close();
	}
	catch(IOException e)
	{
	     e.printStackTrace();
	} 
        
    }
    /**
     * Convert array matrix to Image, and export image to output file
     * on path given in argument oututFilePath.
     */
    public static void exportMatrixToImageFile(int[][] map, String oututFilePath)
    {
        
         File outputImageFile=new File(oututFilePath);
         int w=map.length;
         int h=map[0].length;
         
         
         //SampleModel sampleModel=buffImage.getData().getSampleModel();
         //WritableRaster raster= Raster.createWritableRaster(sampleModel, new Point(0,0));
         //WritableRaster raster=(WritableRaster)image.getData();
         
         MultiPixelPackedSampleModel sampleModel = new MultiPixelPackedSampleModel(DataBuffer.TYPE_BYTE,w,h,1); // one bit per pixel
         WritableRaster raster= Raster.createWritableRaster(sampleModel, new Point(0,0));
         //TiledImage tiledImage = new TiledImage(0,0,w,h,0,0,sampleModel,null); // Create a TiledImage using the SampleModel.
         
        // WritableRaster raster = tiledImage.getWritableTile(0,0); // Get a raster for the single tile.
         
         
         for(int i=0;i<w;i++)
         {
             for(int j=0;j<h;j++)
             {
                 raster.setSample(i,j,0,map[i][j]);
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
    }
    /**
     * Transpone matrix so that value "1" become value "255"
     * @param origMap
     * @return 
     */
    public static int[][] transponseMatrix255values(int[][] origMap){
        int[][] map=new int[origMap.length][origMap[0].length];;
        for (int i=0;i<origMap.length;i++){
            for(int j=0;j<origMap[0].length;j++){
                if(origMap[i][j]==1){
                    map[i][j]=255;
                }else{
                    map[i][j]=origMap[i][j];
                }
            }
        }
//        System.out.println(" O R I G I N A L");
//        printMapToConsole(origMap);
//        System.out.println(" 255 V A L U E S");
//        printMapToConsole(map);
        return map;
    }
}
