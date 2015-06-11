/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gvg;

import java.util.ArrayList;
import java.util.List;

/*
 * Perform blushfire algorithm on entered array matrix map[][] an map
 * all cells in matrix. If array matrix is not entered in argument, class
 * will perform algorithm on predifend value of variable int[][] map.
 * @author Aidos
 */

public class BrushFire {
    
    // If we don't have the input, we work with the predefined matrix
    private boolean debug=false; //flag to debug logging
    private String outputImageFilePath;
    private String outputCSVFilePath;
    //Array matrix (testin value)
    private int[][] map=   {{  0,   0,   0,   0,    0,   0},
                            {255,   0, 255, 255,    0,   0},
                            {  0,   0, 255, 255,  255,   0},
                            {  0,   0, 255, 255,  255,   0},
                            {255, 255, 255, 255,    2,   0},
                            {255, 255, 255,   1,  255,   0},
                            {  0,   0,   0,   0,  255, 255}};
				  
    private int freeBlockValue=255;
    
    public BrushFire(String outputImagepath, String outputCSVPath)
    {
        this.outputCSVFilePath=outputCSVPath;
        this.outputImageFilePath=outputImagepath;
        //the end of BrushFire
    }
    // If we have the input, we work with it
     public BrushFire(int[][] map, String outputImagepath, String outputCSVPath)
    {
        this(outputImagepath, outputCSVPath);
        this.map=MatrixImageTools.transponseMatrix255values(map);
        mapMatrix(this.map);
        //the end of BrushFire
    }
////////        /*
////////         *  Representing each cell in the matrix with x, y values.
////////        */
////////        private class Cell{
////////        int x,y;
////////        //int value;
////////        Cell(int x, int y){
////////            this.x=x;this.y=y;
////////        }
////////        //This method is used when tw Cells are compareing, when 
////////        //adding or removing from the list.
////////        @Override
////////            public boolean equals(Object obj) {
////////            if (obj == null) {
////////                return false;
////////            }
////////            if (getClass() != obj.getClass()) {
////////                return false;
////////            }
////////            final Cell other = (Cell) obj;
////////            return ((other.x==this.x)&&(other.y==this.y));
////////        }
////////        //the end of "Cell"
////////    }
        
     /*
     * Loop trough matrix and mapping each cell.At the end export output matrix in 
     * outptImage and outputCSV file.
     */
        
public void mapMatrix(int[][] map){
List<Cell> openedList=new ArrayList();
List<Cell> closedList=new ArrayList();
        
        populateOpenedAndClosedList(map, openedList, closedList); // To populate Opened and closed list

        int itter = 0;
        while(!openedList.isEmpty()){ //isempty(open_list)==0
            itter++;
            for(int i=0;i<map.length;i++){ //for i = 1:length(X) 
                for(int j=0;j<map[0].length;j++)//loop trought column for each row
                {
                    Cell current=new Cell(i,j,map[i][j]);
                    if((openedList.contains(current)))//ignore obstacles //&&((map[i][j]==freeBlockValue)||(map[i][j]<itter))
                    {
                        List<Cell> neigh_X=new ArrayList();
                        MatrixImageTools.findCellNeighbors(neigh_X, map, i, j, true);
                        int lengthNeigh_X = neigh_X.size();
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
        System.out.println("*******************\n MAPPED MATRIX: ");
        //MatrixImageTools.printMapState(map, itter);
        MatrixImageTools.printMapToConsole(map);
        MatrixImageTools.exportMapToCSV(map, this.outputCSVFilePath);
        MatrixImageTools.exportMatrixToImageFile(map, this.outputImageFilePath);
//the end of "mapMatrix"
}

/* Printing matrix state.
 */
public void printListState(List<Cell> list){


    for(Cell cell:list){
        System.out.print("|"+cell.x+","+cell.y);
    }
    System.out.print("|\n");
    //the end of "printListState"
}

    /* Looping trought matrix and createnig 
     * @param map
     */
    private void populateOpenedAndClosedList(int[][] map, List<Cell> openedList, List<Cell> closedList){
        //System.out.println("list_x: "+map[0].length+" List_y: "+map.length);
        //System.out.println("Map : "+map[6][5]);
        for(int i=0;i<map.length;i++){
            for(int j=0;j<map[0].length;j++){
                //System.out.println("Map: "+i+", "+j+", value= "+map[i][j]+" freeBlockVal="+freeBlockValue);
                if(map[i][j]==freeBlockValue){
                    //System.out.println("Adding to opeblocks: "+i+", "+j+", value= "+map[i][j]+" freeBlockVal="+freeBlockValue);
                    Cell openedCell=new Cell(i,j,map[i][j]);
                    openedList.add(openedCell);
                }
                else{
                    Cell closedCell=new Cell(i,j,map[i][j]);
                    closedList.add(closedCell);
                }
            }
        }
        //the end of populateOpenedAndClosedList
    }
    
    /*
     * Determine is it entered matrix in edge
     */
    private boolean isInBorder(int x, int[][] map){
        return false;
    }
////    
////    //Exploring the neighbours of the Cell
////    private void findCellNeighbors(List<Cell> listToAddNeighb, int[][] map, int x, int y){
////    /*Searching arround given cell (x, y), and found neighbors add to 
////     * list in argument. It is not important if the cell is on the edge; if 
////     * doesnt exist it just wont be added. Currently a,c,f and h cells are
////     * commented, just uncomment it if you want tu use 8 side neigburs.
////     * 
////     * Neighbors of X are signed as folows:
////     *             "a", "b", "c"
////     *             "d", "X", "e"
////     *             "f", "g", "h"
////     * @param listToAddNeighb
////     * @param map
////     * @param x
////     * @param y 
////     */
////        
////        //Cell a = null,b = null,c = null,d = null,e = null,f = null,g = null,h=null;
////        //a        
////        if(((x-1)>=0)&&((y-1)>=0)){
////                Cell a=new Cell((x-1),(y-1));
////                listToAddNeighb.add(a);
////        }
////        //b
////        if(((y-1)>=0)){
////                Cell b=new Cell((x),(y-1));
////                listToAddNeighb.add(b);
////        }
////        //c
////        if(((x+1)<=(map.length-1)) && ((y-1)>=0)){
////                Cell c=new Cell((x+1),(y-1));
////                listToAddNeighb.add(c);
////        }
//////        //d
////        if(((x-1)>=0)){
////                Cell d=new Cell((x-1),(y));
////                listToAddNeighb.add(d);
////        }
////        //e
////        if(((x+1)<=(map.length-1))){
////                Cell e=new Cell((x+1),(y));
////                listToAddNeighb.add(e);
////        }
//////        //f
////        if(((x-1)>=0) && ((y+1)<=(map[0].length-1))){
////                Cell f=new Cell((x-1),(y+1));
////                listToAddNeighb.add(f);
////        }
////        //g
////        if((y+1)<=(map[0].length-1)){
////                Cell g=new Cell((x),(y+1));
////                listToAddNeighb.add(g);
////        }
//////        //h
////        if(((x+1)<=(map.length-1))&&(y+1)<=(map[0].length-1)){
////                Cell h=new Cell((x+1),(y+1));
////                listToAddNeighb.add(h);
////        }
////        
////    //the end of findCellNeighbors
////    }
////    
    /************************************************/
    /****************Getters, Setters****************/
    /************************************************/
    
    public int[][] getMap() {
        return map;
    }

    public void setMap(int[][] map) {
        this.map = map;
    }

    public int getFreeBlockValue() {
        return freeBlockValue;
    }

    public void setFreeBlockValue(int freeBlockValue) {
        this.freeBlockValue = freeBlockValue;
    }
        
//this is the end of BrushFire
}
