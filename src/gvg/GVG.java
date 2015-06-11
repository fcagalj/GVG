/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gvg;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Aidos
 */
public class GVG {
        /**Here we define the input and the output files. It then goes to the
        * "ImageToMatrix.java" class.
        */
        String inputImagePath="1.png";
        String outpuImagePath="_output//outputImage_2.png";
        String outpuCSVPath="_output//outputCSV_2.csv";

        String outpuGVGImagePath="_output//outputGVGImage_2.png";
        String outpuGVGCSVPath="_output//outputGVGCSV_2.csv";

    int[][] gvgArray;      //gvgArray and gvgList both save same GCG result
    List<Cell> gvgList;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // this constructor refers to the ImageToMatrix class 
        GVG gvg=new GVG();
        
        
        ImageToMatrix mapFile=new ImageToMatrix(gvg.inputImagePath, gvg.outpuImagePath, gvg.outpuCSVPath);
        //...and stores the input in the "map" variable 
        int[][] map=mapFile.convertImage(1);
        
        //Refering to BrushFire class to execute the algorithm
        BrushFire bfm=new BrushFire(map,"_output//mappedImage.png","_output//mappedCSV.csv");
        
        int[][] prepairedMap = bfm.getMap();
        
        gvg.doGVG(prepairedMap);

    }
    public void doGVG(int[][] map){
        
        List<Cell> openedList=new ArrayList();
        List<Cell> closedList=new ArrayList();
        

        int itter = 0;
        while(!openedList.isEmpty()){ //isempty(open_list)==0
            itter++;
            for(int i=0;i<map.length;i++){ //for i = 1:length(X) 
                for(int j=0;j<map[0].length;j++)//loop trought column for each row
                {
                    Cell current=new Cell(i,j);
                    if((openedList.contains(current)))//ignore obstacles //&&((map[i][j]==freeBlockValue)||(map[i][j]<itter))
                    {
                        List<Cell> neigh_X=new ArrayList();
                        findCellNeighbors(neigh_X, map, i, j);
                        boolean haveValueIterMinusOne=false;
                        for(Cell neighbor:neigh_X)
                        {
                            if(map[neighbor.x][neighbor.y]==(itter-1))
                            {
                                haveValueIterMinusOne=true;
                                break;
                            }
                        }
                        if(haveValueIterMinusOne){
                            map[i][j]=itter;
                            openedList.remove(current);
                        }
                    }
                }
            }
        }
        System.out.println("*******************\n E N D  S T A T E: ");
        this.map=map;  //so that we can output map in image
        //MatrixImageTools.printMapState(map, itter);
        MatrixImageTools.printMapToConsole(map);
        MatrixImageTools.exportMapToCSV(map, outpuGVGImagePath);
        MatrixImageTools.exportMatrixToImageFile(map, this.outputImageFilePath);
//the end of "mapMatrix"
}

    public int[][] getGvgArray() {
        return gvgArray;
    }

    public List<Cell> getGvgList() {
        return gvgList;
    }
    
}
