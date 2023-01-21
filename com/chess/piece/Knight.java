package com.chess.piece;
import java.util.*;
import java.util.stream.Collectors;
import com.chess.squares.*;
import com.chess.board.*;
import com.chess.common.*;

public class Knight extends AbstractPiece{
    
    public Knight(PieceColor pieceColor, Location initialBoardLocation){
        super(pieceColor, initialBoardLocation);
        this.name = "Knight";
    }

    @Override
    public List<Location> getValidMoves(Board board){

        Map<Location, Square> squareMap = board.getLocationSquareMap();
        Location currentLocation = this.getCurrentSquare().getLocation();
        int[][] offsets = {
            {-2, 1}, 
            {-2, -1},
            {2, 1}, 
            {2, -1},
            {-1, 2}, 
            {-1, -2},
            {1, 2}, 
            {1, -2},
        };

        List<Location> moveCandidates = this.filterMovesInBoard(offsets, currentLocation);

/*         for (int i =0; i<=Offsets.length; i++){
            int fileOffset = Offsets[i][0];
            int rankOffset = Offsets[i][1];
            int targetFile = currentFile + fileOffset;
            int targetRank = currentRank + rankOffset;
            if ( (targetFile>=0) && (targetFile<=7) && (targetRank >=0 ) && (targetRank <= 7) )
            moveCandidates.add(LocationFactory.buildLocation(currentLocation, fileOffset, rankOffset));
        } */


/*         moveCandidates.add(LocationFactory.buildLocation(currentLocation, -2, 1));
        moveCandidates.add(LocationFactory.buildLocation(currentLocation, -2, -1));
        moveCandidates.add(LocationFactory.buildLocation(currentLocation, 2, 1));
        moveCandidates.add(LocationFactory.buildLocation(currentLocation, 2, -1));
        moveCandidates.add(LocationFactory.buildLocation(currentLocation, -1, 2));
        moveCandidates.add(LocationFactory.buildLocation(currentLocation, -1, -2));
        moveCandidates.add(LocationFactory.buildLocation(currentLocation, 1, 2));
        moveCandidates.add(LocationFactory.buildLocation(currentLocation, 1, -2));
 */
        
        // approach without lambda function by introducing local map:
        //List<Location> validMoves = moveCandidates.stream().filter(squareMap::containsKey).collect(Collectors.toList());
        
        //System.out.println("Candidates that are on the board: " + moveCandidates.toString());
        /*
        for (Location candidate : moveCandidates){
            if ((squareMap.get(candidate).getIsOccupied() && 
                squareMap.get(candidate).getCurrentPiece().getPieceColor().equals(this.pieceColor))){
                moveCandidates.remove(candidate);
            }
        }
        

        return moveCandidates;
        
         */
        return moveCandidates.stream().filter((candidate) -> {
                    // in case the file is equal to the current one AND the candidate is occupied, then leave away candidate
                    // this one is for STRAIGHT moves
                    if((squareMap.get(candidate).getIsOccupied() && 
                        squareMap.get(candidate).getCurrentPiece().getPieceColor().equals(this.pieceColor))){
                        return false;
                    }
                    else{
                        return true;
                    }
                    //else{
                    // this one is for CAPTURING moves: the color needs to be the one of the opponent
                    //    return !squareMap.get(candidate).getCurrentPiece().pieceColor.equals(this.getPieceColor());
                    //}
                }
            ).collect(Collectors.toList());
    

/*     @Override
    public void makeMove(Square square){
        Square current = this.getCurrentSquare();
        this.setCurrentSquare(square);
        current.reset();
    }; */
    }
}