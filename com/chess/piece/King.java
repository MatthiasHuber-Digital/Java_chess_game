package com.chess.piece;
import java.util.*;
import java.util.stream.Collectors;
import com.chess.squares.*;
import com.chess.board.*;
import com.chess.common.*;

public class King extends AbstractPiece{
    
    public King(PieceColor pieceColor){
        super(pieceColor);
        this.name = "King";
    }

    @Override
    public List<Location> getValidMoves(Board board){

        Map<Location, Square> squareMap = board.getLocationSquareMap();
        Location currentLocation = this.getCurrentSquare().getLocation();
        int[][] offsets = {
            {-1, 1}, 
            {-1, 0},
            {-1, -1}, 
            {0, -1},
            {0, 1}, 
            {1, 0},
            {1, 0}, 
            {1, -1},
        };

        List<Location> moveCandidates = this.filterMovesInBoard(offsets, currentLocation);

        return moveCandidates.stream().filter((candidate) -> {
                    if((squareMap.get(candidate).getIsOccupied() && 
                        squareMap.get(candidate).getCurrentPiece().getPieceColor().equals(this.pieceColor))){
                        return false;
                    }
                    else{
                        return true;
                    }
                }
            ).collect(Collectors.toList());
    }
}