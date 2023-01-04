package com.chess.board;

import com.chess.squares.*;
import com.chess.common.*;
import com.chess.piece.AbstractPiece;
import com.chess.piece.PieceColor;
import com.chess.piece.PieceFactory;

import java.util.*;

public class Board {
    private static final int BOARD_LENGTH = 8;
    // Location contains the coordinates
    // Square contains the info if is occupied or not
    private final Map<Location, Square> locationSquareMap;

    // boardSquares is for assigning colors to each square for later graphical
    // dispay:
    Square[][] boardSquares = new Square[8][8];

    // separate pieces lists
    private final List<AbstractPiece> lightPieces = new ArrayList<>();
    private final List<AbstractPiece> darkPieces = new ArrayList<>();

    public Board() {
        // map zipping together the squares and location coordinates
        locationSquareMap = new HashMap<>();

        // Mapping of pieces to their locations:
        Map<Location, AbstractPiece> pieces = PieceFactory.getPieces();

        for (int i = 0; i < boardSquares.length; i++) {
            int column = 0;
            SquareColor currentColor = (i % 2 == 0) ? SquareColor.LIGHT : SquareColor.DARK;

            for (File file : File.values()) {
                // generate the square object
                Square newSquare = new Square(currentColor, new Location(file, BOARD_LENGTH - i));
                // if there is a piece that has a link to the square's location -
                if (pieces.containsKey(newSquare.getLocation())) {
                    // Creating a reference to the piece of the current location
                    AbstractPiece piece = pieces.get(newSquare.getLocation());
                    // Assign the piece to the square
                    newSquare.setCurrentPiece(piece);
                    newSquare.setIsOccupied(true);
                    // The piece also needs to contain information where it is:
                    piece.setCurrentSquare(newSquare);
                    if (piece.getPieceColor().equals(PieceColor.DARK)) {
                        darkPieces.add(piece);
                    } else {
                        lightPieces.add(piece);
                    }
                }
                boardSquares[i][column] = newSquare;
                locationSquareMap.put(newSquare.getLocation(), newSquare);
                currentColor = (currentColor == SquareColor.LIGHT) ? SquareColor.DARK : SquareColor.LIGHT;
                column++;
            }
        }
    }

    public Map<Location, Square> getLocationSquareMap() {
        return locationSquareMap;
    }

    // helper methods
    public List<AbstractPiece> getLightPieces() {
        return lightPieces;
    }

    public List<AbstractPiece> getDarkPieces() {
        return darkPieces;
    }

    // Printing a simple version of the board using characters and without colors:
    public void printBoard() {
        System.out.print("  ");
        for (File file : File.values()) {
            System.out.print(file.name() + " "); // file letters, top
        }
        System.out.println();
        for (int i = 0; i < boardSquares.length; i++) {
            System.out.print(BOARD_LENGTH - i + " "); // row numbers, left side
            for (int j = 0; j < boardSquares[i].length; j++) {
                if (boardSquares[i][j].getIsOccupied()) {
                    AbstractPiece piece = boardSquares[i][j].getCurrentPiece();
                    if (piece.getPieceColor().equals(PieceColor.LIGHT)) {
                        System.out.print(Character.toLowerCase(piece.getName().charAt(0)) + " ");
                    } else {
                        System.out.print(piece.getName().charAt(0) + " ");
                    }
                } else {
                    // empty square printing:
                    System.out.print("- ");
                }
            }
            System.out.print(BOARD_LENGTH - i); // row numbers, right side
            System.out.println();
        }
        System.out.print("  ");
        for (File file : File.values()) {
            System.out.print(file.name() + " "); // file letters bottom
        }
        System.out.println();
    }
}
