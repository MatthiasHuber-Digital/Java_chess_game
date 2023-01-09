package com.chess.squares;
import com.chess.common.*;
import com.chess.piece.*;

public class Square {
    private final SquareColor squareColor;
    private final Location location;
    private boolean isOccupied;
    private AbstractPiece currentPiece;

    public Square (SquareColor squareColor, Location location){
        this.squareColor = squareColor;
        this.location = location;
        this.isOccupied = false;
    }

    public AbstractPiece getCurrentPiece(){
        return this.currentPiece;
    }

    public void setCurrentPiece(AbstractPiece currentPiece){
        this.currentPiece = currentPiece;
        this.isOccupied = true;
    }

    public void reset(){
        this.isOccupied = false;
        this.currentPiece = null;
    }


    public SquareColor getSquareColor() {
        return squareColor;
    }


    public Location getLocation() {
        return location;
    }

    public boolean getIsOccupied() {
        return isOccupied;
    }

    public void setIsOccupied(boolean occupied) {
        isOccupied = occupied;
    }
    

    @Override
    public String toString() {
        return "{" +
            " squareColor='" + getSquareColor() + "'" +
            ", location='" + getLocation() + "'" +
            ", isOccupied='" + getIsOccupied() + "'" +
            "}";
    }

}