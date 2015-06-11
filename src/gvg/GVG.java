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
        String outpuImagePath="_output//outputImage_2.png";
        String outpuCSVPath="_output//outputCSV_2.csv";

        String outpuGVGImagePath="_output//outputGVGImage_2.png";
        String outpuGVGCSVPath="_output//outputGVGCSV_2.csv";

        int[][] gvgArray;      //gvgArray and gvgList both save same GCG result
        List<Cell> gvgSet;
    
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
        //Set<Cell> GVGset=new HashSet();
        int[][] GVGArray=this.initArray(map);
        
        //populateOpenedList(map, openedList);

        System.out.println("Starting GVG opened list size: "+openedList.size());
        
//        int itter = 0;
//        while(!openedList.isEmpty()){ //isempty(open_list)==0
//            itter++;
            for(int i=0;i<map.length;i++){ //for i = 1:length(X) 
                for(int j=0;j<map[0].length;j++)//loop trought column for each row
                {
                    Cell current=new Cell(i,j,map[i][j]);
                    //if((openedList.contains(current)))//ignore obstacles //&&((map[i][j]==freeBlockValue)||(map[i][j]<itter))
                    if(map[i][j]!=0) //(openedList.contains(current))
                    {
                        List<Cell> neigh_X=new ArrayList();
                        MatrixImageTools.findCellNeighbors(neigh_X, map, i, j, false);
                        //System.out.println(itter+". iteration, for cell ["+i+","+j+"]="+map[i][j]+"("+current.value+"), founded "+neigh_X.size()+"neighburs.");
//                        boolean haveAlowedValue=false;  // Alowed values are itterator plus minus 1, and same as iterator
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
                            //System.out.println("    min_neigh_counter="+min_neigh_counter+" the_same_counter="+the_same_counter+" max_neigh_counter="+max_neigh_counter);
                            if ( (((min_neigh_counter>=2)&&(the_same_counter>=2))   ////• Having two min_neigh_counter, two the_same_counter and not being added in the GVD set before
                                || (((min_neigh_counter>=1)||(max_neigh_counter>=1))&&(the_same_counter>=3))////• Having one min_neigh_counter or max_neigh_counter and three the_same_counter
                                || ((min_neigh_counter>=3)&&(the_same_counter>=1))////• Having three min_neigh_counter and one the_same_counter
                                    
                               ))
                            {
                                //GVGset.add(current);
                                GVGArray[i][j]=1;
                                //haveAlowedValue=true;
                                break;
                            }
                        }
//                        
//                        if(haveAlowedValue){
//                                //map[i][j]=1;
//                        }else{
//                                //map[i][j]=0;
//                        }
                        openedList.remove(current);
                    }
                }
            }
        //}
        System.out.println("*******************\n GVG MATRIX");
        //this.map=map;
        //so that we can output map in image
        //MatrixImageTools.printMapState(map, itter);
        MatrixImageTools.printMapToConsole(GVGArray);
        MatrixImageTools.exportMapToCSV(GVGArray, outpuGVGCSVPath);
        MatrixImageTools.exportMatrixToImageFile(GVGArray, outpuGVGImagePath);
    //the end of "mapMatrix"
    }
    public void populateOpenedList(int[][] map, List<Cell> openedList){
        //System.out.println("list_x: "+map[0].length+" List_y: "+map.length);
        //System.out.println("Map : "+map[6][5]);
        for(int i=0;i<map.length;i++){
            for(int j=0;j<map[0].length;j++){
                //System.out.println("Map: "+i+", "+j+", value= "+map[i][j]+" freeBlockVal="+freeBlockValue);
                if(map[i][j]!=0){
                    //System.out.println("Adding to opeblocks: "+i+", "+j+", value= "+map[i][j]+" freeBlockVal="+freeBlockValue);
                    Cell openedCell=new Cell(i,j,map[i][j]);
                    openedList.add(openedCell);
                }
            }
        }

    }
    // clone two dimensional array
    public int[][] cloneArray(int[][] a) {
        int[][] b = new int[a.length][];
        for (int i = 0; i < a.length; i++) {
            b[i] = a[i].clone();
        }
        return b;
    }
    public int[][] initArray(int[][] a){
        int[][] b = new int[a.length][a[0].length];
        //System.out.println("***********************************");
        for(int i=0;i<a.length;i++){
            for(int j=0;j<a[0].length;j++){
                b[i][j]=0;
            }
        }
        return b;
    }
    
    public int[][] getGvgArray() {
        return gvgArray;
    }

    public List<Cell> getGvgSet() {
        return gvgSet;
    }    

    
}
