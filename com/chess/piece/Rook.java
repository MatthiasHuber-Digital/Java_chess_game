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
        return getSquareDependentMoveCandidates(this.getCurrentSquare(), squareMap);
    }

    private List<Location> getSquareDependentMoveCandidates(
        Square square,
        Map<Location, Square> squareMap
        ){
        Location currentLocation = square.getLocation();
        List<Location> moveCandidates = new ArrayList<>();
        getMoves(moveCandidates, squareMap, currentLocation, 1, 0);
        getMoves(moveCandidates, squareMap, currentLocation, -1, 0);
        getMoves(moveCandidates, squareMap, currentLocation, 0, 1);
        getMoves(moveCandidates, squareMap, currentLocation, 0, -1);
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

/*     @Override
    public void makeMove(Square square){
        Square current = this.getCurrentSquare();
        this.setCurrentSquare(square);
        current.reset();
    }; */
}