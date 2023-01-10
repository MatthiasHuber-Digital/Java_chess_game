package com.chess.piece;
import com.chess.squares.*;
import com.chess.common.*;
import com.chess.runner.Game;
import com.chess.board.*;
import java.util.*;
import java.util.stream.Collectors;
import java.lang.Math;

import javax.management.relation.RelationServiceNotRegisteredException;

public abstract class AbstractPiece implements Movable{
    protected String name;
    protected PieceColor pieceColor;
    protected Square currentSquare;
    public boolean pieceHasBeenCaptured = false;

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

        for (int i=0; i<offsets.length; i++){
            int fileOffset = offsets[i][0];
            int rankOffset = offsets[i][1];
            int targetFile = currentFile + fileOffset;
            int targetRank = currentRank + rankOffset;

            if ( (targetFile>=0) && (targetFile<=7) && (targetRank >=1 ) && (targetRank <= 8) ){
                Location addedLocation = LocationFactory.buildLocation(currentLocation, fileOffset, rankOffset);
                moveCandidates.add(addedLocation);
            }

        }


        return moveCandidates;
    }

    protected ArrayList<Location> filterStraightMovesInBoard(int[][] offsets, Location currentLocation){
        int[][] tempOffsets = new int[offsets.length][2];

        int moveMultiplier = 1;
        ArrayList<Location> moveCandidates = new ArrayList<>();
        ArrayList<Location> newMoveCandidates = this.filterMovesInBoard(offsets, currentLocation);

        while (!newMoveCandidates.isEmpty()){
            for (Location candidate : newMoveCandidates){
                moveCandidates.add(candidate);
            }
            moveMultiplier++;
            for (int i=0; i<offsets.length; i++) {
                tempOffsets[i][0] = offsets[i][0] * moveMultiplier;
                tempOffsets[i][1] = offsets[i][1] * moveMultiplier;
              }
            newMoveCandidates = this.filterMovesInBoard(tempOffsets, currentLocation);
        }

        return moveCandidates;
    }

    protected ArrayList<Location> filterUnblockedStraightMoves(ArrayList<Location> moveCandidates, Map<Location, Square> squareMap, Location currentLocation, int[][] offsets){
        ArrayList<Location> deleteCandidates = new ArrayList<>();
        
        for (int curOffset=0; curOffset<offsets.length; curOffset++){
            int currentFileOrdinal = currentLocation.getFile().ordinal();
            int currentRank = currentLocation.getRank();

            int factor = 1;
            int potentialFileOrdinal = currentFileOrdinal  + (factor * offsets[curOffset][0]);
            int potentialRank = currentRank + (factor * offsets[curOffset][1]);
            boolean removeRemainingCandidates = false;
            while ((potentialFileOrdinal>=0) && (potentialFileOrdinal<=7) && (potentialRank >=1 ) && (potentialRank <= 8)){
                Location potentialLocation = LocationFactory.buildLocation(currentLocation, factor * offsets[curOffset][0], factor * offsets[curOffset][1]);
                
                if (!removeRemainingCandidates){
                    // this is the checking-algorithm for finding out if this direction has already been blocked
                    if (squareMap.get(potentialLocation).getIsOccupied()){
                        if (squareMap.get(potentialLocation).getCurrentPiece().getPieceColor().equals(this.pieceColor)){
                            deleteCandidates.add(potentialLocation);
                        }
                        removeRemainingCandidates = true;
                    }                    
                }
                else{
                    //this is the removing algorithm
                    deleteCandidates.add(potentialLocation);
                }

                factor++;
                potentialFileOrdinal = currentFileOrdinal + (factor * offsets[curOffset][0]);
                potentialRank = currentRank + (factor * offsets[curOffset][1]);
            }
        }

        for (Location removeLocation : deleteCandidates){
            moveCandidates.removeIf(candidate -> (candidate.getFile() == removeLocation.getFile())
                                                    && (candidate.getRank() == removeLocation.getRank()));
        }

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
    

    public List<Location> getNeighbourLocations(Board board) {
        List<Location> neighbourLocations = new ArrayList<>();
        Location currentLocation = this.getCurrentSquare().getLocation();
        int currentFileOrdinal = currentLocation.getFile().ordinal();
        int currentRank = currentLocation.getRank();

        /*
        for (int fileOffset = -1; fileOffset <= +1; fileOffset++) {
            for (int rankOffset = -1; rankOffset <= +1; rankOffset++) {
                // all neighbouring fields: save locations

                neighbourLocations.add();
            }
        }
        */
        
        neighbourLocations = board.getLocationSquareMap().keySet().stream().filter(loc -> { 
            return (Math.abs(loc.getFile().ordinal() - currentFileOrdinal) <= 1) && (Math.abs(loc.getRank() - currentRank) <= 1);
        }).collect(Collectors.toList());

        return neighbourLocations;
    }
}