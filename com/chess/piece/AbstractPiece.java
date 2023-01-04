package com.chess.piece;
import com.chess.squares.*;
import com.chess.common.*;
import com.chess.board.*;
import java.util.*;

public abstract class AbstractPiece implements Movable{
    protected String name;
    protected PieceColor pieceColor;
    protected Square currentSquare;

    public AbstractPiece(PieceColor pieceColor){
        this.pieceColor = pieceColor;
    }

    public PieceColor getPieceColor(){
        return pieceColor;
    }

    public String getName(){
        return name;
    }

    public Square getCurrentSquare(){
        return currentSquare;
    }

    public void setCurrentSquare(Square currentSquare){
        this.currentSquare = currentSquare;
    }

    @Override
    public String toString() {
        return "{" +
            " name='" + getName() + "'" +
            ", pieceColor='" + getPieceColor() + "'" +
            ", currentSquare='" + getCurrentSquare() + "'" +
            "}";
    }

    @Override
    public void makeMove(Square targetSquare, File targetFile, int targetRank, Board board){
        Square currentSquare = this.getCurrentSquare();
        this.setCurrentSquare(targetSquare);
        targetSquare.setCurrentPiece(currentSquare.getCurrentPiece());
        currentSquare.reset();
    };

    protected ArrayList<Location> filterMovesInBoard(int[][] offsets, Location currentLocation){

        ArrayList<Location> moveCandidates = new ArrayList<>();
        int currentFile = currentLocation.getFile().ordinal();
        int currentRank = currentLocation.getRank();

        //System.out.println("current file: " + File.values()[currentFile] + "... and has the ordinal: " + currentFile);
        //System.out.println("current rank: " + currentRank);

        for (int i=0; i<offsets.length; i++){
            int fileOffset = offsets[i][0];
            int rankOffset = offsets[i][1];
            int targetFile = currentFile + fileOffset;
            int targetRank = currentRank + rankOffset;

            //System.out.println("checking targetFile: " + targetFile);
            //System.out.println("checking targetRank: " + targetRank);

            if ( (targetFile>=0) && (targetFile<=7) && (targetRank >=1 ) && (targetRank <= 8) ){
                //System.out.println("...move is OK!");
                Location addedLocation = LocationFactory.buildLocation(currentLocation, fileOffset, rankOffset);
                moveCandidates.add(addedLocation);
                //System.out.println("Candidates: " + moveCandidates.toString());
            }
            //else{
            //    System.out.println("...invalid move.");
            //}
        }

        //System.out.println("FINAL CANDIDATES: " + moveCandidates.toString());

        return moveCandidates;
    }

}