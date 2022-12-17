package com.chess.piece;
import java.util.*;
import java.util.stream.Collectors;
import com.chess.squares.*;
import com.chess.board.*;
import com.chess.common.*;

public class Pawn extends AbstractPiece implements Movable{
    private boolean isFirstMove = true;

    public Pawn(PieceColor pieceColor){
        super(pieceColor);
        this.name = "Pawn";
    }

    @Override
    public List<Location> getValidMoves(Board board){
        List<Location> moveCandidates = Collections.emptyList();
        Location currentLocation = this.getCurrentSquare().getLocation();
        moveCandidates.add(LocationFactory.buildLocation(currentLocation, 0, 1));
        if (isFirstMove){
            moveCandidates.add(LocationFactory.buildLocation(currentLocation, 0, 2));
                return moveCandidates;
        }
        moveCandidates.add(LocationFactory.buildLocation(currentLocation, 1, 1));
        moveCandidates.add(LocationFactory.buildLocation(currentLocation, -1, 1));
        
        // approach without lambda function by introducing local map:
        Map<Location, Square> squareMap = board.getLocationSquareMap();
        /* produce list of locations
         * use a filtering function, the criteria of which are:
         * that they (Locations in the list of the moveCandidates)
         * are WITHIN the hash-map
         * (all data NOT being inside the hash map is filtered out)
         */
        List<Location> validMoves = moveCandidates.stream().filter(squareMap::containsKey).collect(Collectors.toList());
/*         // LAMBDA FUNCTION FOR FILTERING OUT ONLY VALID MOVES:
        List<Location> validMoves = moveCandidates.stream().filter(
            (candidate) -> { return(board.getLocationSquareMap().containsKey(candidate));}
        ).collect(Collectors.toList());
 */
        return validMoves.stream().filter((candidate) -> {
                    // in case the file is equal to the current one AND the candidate is occupied, then leave away candidate
                    // this one is for STRAIGHT moves
                    if(
                        candidate.getFile().equals(this.getCurrentSquare().getLocation().getFile()) 
                        && squareMap.get(candidate).getIsOccupied()
                        )
                    {return false;}
                    else{
                        // this one is for CAPTURING moves: the color needs to be the one of the opponent
                        return !squareMap.get(candidate).getCurrentPiece().pieceColor.equals(this.getPieceColor());
                    }
                }
            ).collect(Collectors.toList());
    }

    @Override
    public void makeMove(Square square){
        System.out.println(this.getName() + "-> makeMove()");
    };

}
