package com.chess.board;

import com.chess.squares.*;
import com.chess.common.*;
import com.chess.piece.AbstractPiece;
import com.chess.piece.King;
import com.chess.piece.PieceColor;
import com.chess.piece.PieceFactory;

import java.util.*;

public final class Board {
    private static final int BOARD_LENGTH = 8;
    // Location contains the coordinates
    // Square contains the info if is occupied or not
    public final Map<Location, Square> locationSquareMap = new HashMap<>();
    private final Map<Location, AbstractPiece> intialPieceLocationMap = PieceFactory.getPieces(); // Mapping of pieces to their INITIAL locations:
    // separate pieces lists
    private final List<AbstractPiece> lightPieces = new ArrayList<>();
    private final List<AbstractPiece> darkPieces = new ArrayList<>();
    public AbstractPiece lightKing;
    public AbstractPiece darkKing;
    // boardSquares is for assigning colors to each square for later graphical
    // dispay:
    private static final Square[][] boardSquares = new Square[8][8];

    public Board() {
        // map zipping together the squares and location coordinates

        for (int i = 0; i < boardSquares.length; i++) {
            int column = 0;
            SquareColor currentColor = (i % 2 == 0) ? SquareColor.LIGHT : SquareColor.DARK;

            for (File file : File.values()) {
                // generate the square object
                Square newSquare = new Square(currentColor, new Location(file, BOARD_LENGTH - i));
                // if there is a piece that has a link to the square's location -
                if (intialPieceLocationMap.containsKey(newSquare.getLocation())) {
                    // Creating a reference to the piece of the current location
                    AbstractPiece piece = intialPieceLocationMap.get(newSquare.getLocation());
                    // Assign the piece to the square
                    newSquare.setCurrentPiece(piece);
                    newSquare.setIsOccupied(true);
                    // The piece also needs to contain information where it is:
                    piece.setCurrentSquare(newSquare);
                    if (piece.getPieceColor().equals(PieceColor.DARK)) {
                        darkPieces.add(piece);
                        if (piece.getName().equals("King")){
                            darkKing = piece;
                        }
                    } else {
                        lightPieces.add(piece);
                        if (piece.getName().equals("King")){
                            lightKing = piece;
                        }
                    }
                }
                boardSquares[i][column] = newSquare;
                locationSquareMap.put(newSquare.getLocation(), newSquare);
                currentColor = (currentColor == SquareColor.LIGHT) ? SquareColor.DARK : SquareColor.LIGHT;
                column++;
            }
        }
    }

    public void resetBoard(){
        // This method resets the board, all pieces and all squares to their INITIAL STATES:
        
        // Resetting pieces:
        for (AbstractPiece piece : lightPieces){
            piece.resetToInitialState(this);
        }
        for (AbstractPiece piece : darkPieces){
            piece.resetToInitialState(this);
        }

        // Resetting squares:
        Square currentSquare;
        for (int i=0; i < 8; i++) {
            for (int j=0; j < 8; j++){
                currentSquare = boardSquares[i][j];
                if (intialPieceLocationMap.containsKey(currentSquare.getLocation())){
                    currentSquare.setCurrentPiece(intialPieceLocationMap.get(currentSquare.getLocation()));
                    currentSquare.setIsOccupied(true);
                }
                else{
                    currentSquare.reset();
                }
            }
        }
    }

    public Map<Location, Square> getLocationSquareMap() {
        return locationSquareMap;
    }

    // tiles are the graphical representation of the squares
    public static Square getSquareFromTileId(int tileId){
        return boardSquares[tileId / 8][tileId % 8];
    }

    // helper methods
    // light pieces start from the lower half of the board
    public List<AbstractPiece> getLightPieces() {
        return lightPieces;
    }
    // dark pieces start from the upper half of the board
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
