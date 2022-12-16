package com.chess.board;
import com.chess.squares.*;
import com.chess.common.*;
import java.util.*;

public class Board {
    private static final int BOARD_LENGTH = 8;
    private final Map<Location, Square> locationSquareMap;
    Square[][] boardSquares = new Square[8][8];

    public Board(){
        locationSquareMap = new HashMap<>();
        for (int i=0; i < boardSquares.length; i++){
            int column = 0;
            SquareColor currentColor = (i%2 == 0) ? SquareColor.LIGHT : SquareColor.DARK;

            for(File file : File.values()){
                Square newSquare = new Square(currentColor, new Location(file, BOARD_LENGTH - i));
                locationSquareMap.put(newSquare.getLocation(), newSquare);
                boardSquares[i][column] = newSquare;
                currentColor = (currentColor == SquareColor.LIGHT) ? SquareColor.DARK : SquareColor.LIGHT;
                column++;
            }
        }
    }

    public Map<Location, Square> getLocationSquareMap(){
        return locationSquareMap;
    }

    public void printBoard(){
        int i = 0;
        for(Square[] row : boardSquares){
            for(Square square : row){
                i++;
                System.out.println("Node #:" + i + ", " + square);
            }
            System.out.println('\n');
        }
    }
}
