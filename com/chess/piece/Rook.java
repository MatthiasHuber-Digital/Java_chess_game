package com.chess.piece;
import java.util.*;
import com.chess.squares.*;
import com.chess.board.*;
import com.chess.common.*;

public class Rook extends AbstractPiece{
    
    public Rook(PieceColor pieceColor){
        super(pieceColor);
        this.name = "Rook";
    }

    @Override
    public List<Location> getValidMoves(Board board){

        Map<Location, Square> squareMap = board.getLocationSquareMap();
        Location currentLocation = this.getCurrentSquare().getLocation();
        ArrayList<Location> moveCandidates = new ArrayList<>();

        // FILE (A), then RANK(1)
        int[][] offsets = {
            {1, 0}, 
            {-1, 0}, 
            {0, 1}, 
            {0, -1},
        };

        /* 
                int[][] tempOffsets= new int[4][2];

            int moveMultiplier = 1;
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
        */

        moveCandidates = this.filterStraightMovesInBoard(offsets, currentLocation);

        return this.filterUnblockedStraightMoves(moveCandidates, squareMap, currentLocation, offsets);

    }
}