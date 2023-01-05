package com.chess.piece;
import com.chess.squares.*;
import com.chess.common.*;
import com.chess.runner.Game;
import com.chess.board.*;
import java.util.*;
import java.util.stream.Collectors;

import javax.management.relation.RelationServiceNotRegisteredException;

public abstract class AbstractPiece implements Movable{
    protected String name;
    protected PieceColor pieceColor;
    protected Square currentSquare;
    private boolean pieceHasBeenCaptured = false;

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

        // capturing pieces - means removing them from the board:
        if (targetSquare.getIsOccupied() && !targetSquare.getCurrentPiece().getPieceColor().equals(this.getPieceColor())){
            removePieceFromBoard(targetSquare.getCurrentPiece());
        }

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

    private static void removePieceFromBoard(AbstractPiece piece){
        piece.pieceHasBeenCaptured = true;
        piece.currentSquare = null;
    }

    public void deactivateFirstMove(){
        // only for pawns
    }

    public boolean getIsFirstMove(){
        return false;
    }

    // this method returns a piece of the user's choice to the board IN CASE THE PAWN HAS REACHED THE FARTHEST RANK
    public void returnChosenPieceToBoard(Board board){
        List<AbstractPiece> revivingList = new ArrayList<>();

        if (this.pieceColor.equals(pieceColor.LIGHT)){
            revivingList = board.getLightPieces();
        }
        else{
            revivingList = board.getDarkPieces();
        }

        revivingList = revivingList.stream().filter(piece -> {return(piece.pieceHasBeenCaptured);}).collect(Collectors.toList());

        if (revivingList.isEmpty()){
            System.out.println(String.format("The %s player's pawn reached the farthest rank, but there are no pieces to revive.", this.getPieceColor()));
        }
        else{
            this.pieceHasBeenCaptured = true;
            System.out.println(String.format("The %s player's pawn reached the farthest rank. Please choose which piece to revive: ", this.getPieceColor()));
            
            for (AbstractPiece piece : revivingList){
                System.out.println(piece.getName());
            }
            
            try {
                
                boolean pieceFound = false;
                while(!pieceFound){
                    // E2 E4  -- origin and destination
                    String reviveLine = Game.input_scan.nextLine();
                    
                    for (AbstractPiece checkPiece : revivingList){

                        if (reviveLine.contains(checkPiece.getName())){
                            pieceFound = true;
                            checkPiece.setCurrentSquare(this.currentSquare);
                            checkPiece.pieceHasBeenCaptured = false;
                            removePieceFromBoard(this);
                            break;
                        }
                        
                    }
                    
                    if (!pieceFound){
                        System.out.println("Your choice is invalid. Please try again.");
                    }

                }
            } catch (UnsupportedOperationException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    
}