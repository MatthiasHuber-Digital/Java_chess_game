package com.chess.squares;
import com.chess.common.*;

public class Square {
    private final SquareColor squareColor;
    private final Location location;
    private boolean isOccupied;

    public Square (SquareColor squareColor, Location location){
        this.squareColor = squareColor;
        this.location = location;
        this.isOccupied = false;
    }

    public void reset(){
        this.isOccupied = false;
    }


    public SquareColor getSquareColor() {
        return this.squareColor;
    }


    public Location getLocation() {
        return this.location;
    }

    public boolean getIsOccupied() {
        return this.isOccupied;
    }

    public void setIsOccupied(boolean isOccupied) {
        this.isOccupied = isOccupied;
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