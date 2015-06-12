/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gvg;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Aidos
 */
public class GVG {
        /**Here we define the input and the output files. It then goes to the
        * "ImageToMatrix.java" class.
        */
        String inputImagePath="1.png";
        
        //Output of image convereted in matrix, without any changes to them
        String outpuImagePath="_output//1_outputImage.png";
        String outpuCSVPath="_output//1_outputCSV.csv";
        
        //Output of originalMap getted with BlushFire algorithm 
        String outpuMappedImagePath="_output//2_outputMappedImage.png";
        String outpuMappedCSVPath="_output//2_outputMappedCSV.csv";

        //Output of GVG matrix
        String outpuGVGImagePath="_output//3_outputGVGImage.png";
        String outpuGVGCSVPath="_output//3_outputGVGCSV.csv";

        //Output of combined GVG matrix on top of original image, where 
        String outpuCombinedImagePath="_output//4_outputCombinedImage.png";
        String outpuCombinedCSVPath="_output//4_outputCombinedCSV.csv";
        
        int[][] gvgArray;      //gvgArray and gvgList both save same GCG result
        List<Cell> gvgSet;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // this constructor refers to the ImageToMatrix class 
        GVG gvg=new GVG();
        
        
        ImageToMatrix mapFile=new ImageToMatrix(gvg.inputImagePath, gvg.outpuImagePath, gvg.outpuCSVPath);
        //...and stores the input in the "originalMap" variable 
        int[][] originalMap=mapFile.convertImage(1);
        
        //Refering to BrushFire class to execute the algorithm
        BrushFire bfm=new BrushFire(originalMap, gvg.outpuMappedImagePath, gvg.outpuMappedCSVPath);
        
        int[][] prepairedMap = bfm.getMap();
        
        int[][] GVGSet=gvg.doGVG(prepairedMap);

        int[][] combinedMatrix=gvg.combineMatrix(originalMap, GVGSet);
                
        System.out.println("*******************\n COMBINED GVG ON TOP OF ORIGINAL MATRIX");

        MatrixImageTools.printMapToConsole(combinedMatrix);
        MatrixImageTools.exportMapToCSV(combinedMatrix, gvg.outpuCombinedCSVPath);
        MatrixImageTools.exportMatrixToImageFile(combinedMatrix, gvg.outpuCombinedImagePath);
        
    }
    /**
     * Produce matrix with background=1, and GVG cells=0;
     * @param map
     * @return 
     */
    public int[][] doGVG(int[][] map){

        int[][] GVGSet=this.initArray(map);
        
        for(int i=0;i<map.length;i++){ //for i = 1:length(X) 
            for(int j=0;j<map[0].length;j++)//loop trought column for each row
            {
                Cell current=new Cell(i,j,map[i][j]);
                //if((openedList.contains(current)))//ignore obstacles //&&((originalMap[i][j]==freeBlockValue)||(originalMap[i][j]<itter))
                if(map[i][j]!=0) //(openedList.contains(current))
                {
                    List<Cell> neigh_X=new ArrayList();
                    MatrixImageTools.findCellNeighbors(neigh_X, map, i, j, false);
                    int min_neigh_counter=0, max_neigh_counter=0, the_same_counter=0;
                    
                    for(Cell neighbor:neigh_X)
                    {
                        if((map[neighbor.x][neighbor.y]==(map[i][j]-1)))//&& !GVGset.contains(current)
                        {
                            min_neigh_counter++;
                        }else if((map[neighbor.x][neighbor.y]==(map[i][j])))//&& !GVGset.contains(current)
                        {
                            the_same_counter++;
                        }else if((map[neighbor.x][neighbor.y]==(map[i][j]+1)))//&& !GVGset.contains(current)
                        {
                            max_neigh_counter++;
                        }
                        if ( (((min_neigh_counter>=2)&&(the_same_counter>=2))   ////• Having two min_neigh_counter, two the_same_counter and not being added in the GVD set before
                            || (((min_neigh_counter>=1)||(max_neigh_counter>=1))&&(the_same_counter>=3))////• Having one min_neigh_counter or max_neigh_counter and three the_same_counter
                            || ((min_neigh_counter>=3)&&(the_same_counter>=1))////• Having three min_neigh_counter and one the_same_counter

                           ))
                        {
                            //GVGset.add(current);
                            GVGSet[i][j]=0; //1 for white
                            break;
                        }
                    }
                    //openedList.remove(current);
                }
            }
        }

        System.out.println("*******************\n GVG MATRIX");

        MatrixImageTools.printMapToConsole(GVGSet);
        MatrixImageTools.exportMapToCSV(GVGSet, outpuGVGCSVPath);
        MatrixImageTools.exportMatrixToImageFile(GVGSet, outpuGVGImagePath);
        
        return GVGSet;
    }
    public void populateOpenedList(int[][] map, List<Cell> openedList){
        //System.out.println("list_x: "+originalMap[0].length+" List_y: "+originalMap.length);
        //System.out.println("Map : "+originalMap[6][5]);
        for(int i=0;i<map.length;i++){
            for(int j=0;j<map[0].length;j++){
                //System.out.println("Map: "+i+", "+j+", value= "+originalMap[i][j]+" freeBlockVal="+freeBlockValue);
                if(map[i][j]!=0){
                    //System.out.println("Adding to opeblocks: "+i+", "+j+", value= "+originalMap[i][j]+" freeBlockVal="+freeBlockValue);
                    Cell openedCell=new Cell(i,j,map[i][j]);
                    openedList.add(openedCell);
                }
            }
        }

    }
    /**
     * Initiate matrix with all values 1.
     * @param a
     * @return 
     */
    public int[][] initArray(int[][] a){
        int[][] b = new int[a.length][a[0].length];
        //System.out.println("***********************************");
        for(int i=0;i<a.length;i++){
            for(int j=0;j<a[0].length;j++){
                b[i][j]=1; //1 - white, 0-black
            }
        }
        return b;
    }
    /**
     * Combine two matrixs so that they act as two layers, where second is on top
     * of first. Second layer's baskground is considered value 1, so if seconds
     * matrix value is different from 1, it will be displayed, otherway it will
     * be displayed first matrix.
     * @return 
     */
    public int[][] combineMatrix(int[][] bottom, int[][] top){
        int[][] combined=new int[bottom.length][bottom[0].length];
        for (int i=0;i<bottom.length;i++){
            for (int j=0;j<bottom.length;j++){
                if(top[i][j]!=1){
                    combined[i][j]=top[i][j];
                }else{
                    combined[i][j]=bottom[i][j];
                }
            }
        }
        
        return combined;
    }
    public int[][] getGvgArray() {
        return gvgArray;
    }

    public List<Cell> getGvgSet() {
        return gvgSet;
    }    
}
