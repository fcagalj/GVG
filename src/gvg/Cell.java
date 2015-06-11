/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gvg;
    /*
     *  Representing each cell in the matrix with x, y values.
    */
    public class Cell{
    int x,y, value;
    //int value;
    Cell(int x, int y, int value){
        this.x=x;this.y=y;this.value=value;
    }
    //This method is used when tw Cells are compareing, when 
    //adding or removing from the list.
    @Override
        public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Cell other = (Cell) obj;
        return ((other.x==this.x)&&(other.y==this.y));
    }
    }