package com.chess.piece;
import java.util.*;
import java.util.stream.Collectors;
import com.chess.squares.*;
import com.chess.board.*;
import com.chess.common.*;

public class Bishop extends AbstractPiece{
    
    public Bishop(PieceColor pieceColor){
        super(pieceColor);
        this.name = "Bishop";
    }
    /* 
    @Override
    public List<Location> getValidMoves(Board board){
        Map<Location, Square> squareMap = board.getLocationSquareMap();
        return getSquareDependentMoveCandidates(this.getCurrentSquare(), squareMap);
    }

    private List<Location> getSquareDependentMoveCandidates(
        Square square,
        Map<Location, Square> squareMap
        ){
        Location currentLocation = square.getLocation();
        List<Location> moveCandidates = new ArrayList<>();
        getMoves(moveCandidates, squareMap, currentLocation, 1, 1);
        getMoves(moveCandidates, squareMap, currentLocation, -1, -1);
        getMoves(moveCandidates, squareMap, currentLocation, 1, -1);
        getMoves(moveCandidates, squareMap, currentLocation, -1, 1);
        return moveCandidates;
    }

    private void getMoves(
        List<Location> candidates,
        Map<Location, Square> squareMap,
        Location currentLocation,
        int rankOffset,
        int fileOffset
    ){
        Location next = LocationFactory.buildLocation(currentLocation, fileOffset, rankOffset);
        while(squareMap.containsKey(next)){
            // if the next square is occupied
            if (squareMap.get(next).getIsOccupied()){
                // in case of SAME color - don't add the square (obstacle)
                if (squareMap.get(next).getCurrentPiece().pieceColor.equals(this.pieceColor)) 
                    break;
                else{
                    // in case of OTHER color - add square
                    candidates.add(next);
                    // ...and do NOT UPDATE to next potential location!
                    break;
                }
            }
            candidates.add(next); // this candidate is OK
            // update the next location based on the offsets:
            next = LocationFactory.buildLocation(next, fileOffset, rankOffset);
        }
    }
    */

    @Override
    public List<Location> getValidMoves(Board board){

        Map<Location, Square> squareMap = board.getLocationSquareMap();
        Location currentLocation = this.getCurrentSquare().getLocation();
        ArrayList<Location> moveCandidates = new ArrayList<>();
        ArrayList<Location> newMoveCandidates = new ArrayList<>();

        // FILE (A), then RANK(1)
        int[][] offsets = {
            {1, 1}, 
            {-1, -1}, 
            {-1, 1}, 
            {1, -1},
        };

        /* 
         *         int[][] tempOffsets = new int[4][2];

        int moveMultiplier = 1;
        newMoveCandidates = this.filterMovesInBoard(offsets, currentLocation);
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
         * 
        */

        moveCandidates = this.filterStraightMovesInBoard(offsets, currentLocation);

        return this.filterUnblockedStraightMoves(moveCandidates, squareMap, currentLocation, offsets);
    }
}