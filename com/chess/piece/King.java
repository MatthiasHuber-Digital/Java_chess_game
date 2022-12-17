package com.chess.piece;
import java.util.*;
import java.util.stream.Collectors;
import com.chess.squares.*;
import com.chess.board.*;
import com.chess.common.*;

public class King extends AbstractPiece implements Movable{
    
    public King(PieceColor pieceColor){
        super(pieceColor);
        this.name = "King";
    }

    @Override
    public List<Location> getValidMoves(Board board){
        List<Location> moveCandidates = Collections.emptyList();
        Map<Location, Square> squareMap = board.getLocationSquareMap();
        Location currentLocation = this.getCurrentSquare().getLocation();
        for (int r = -1; r <= 1; r++){
            for (int f = -1; f <= 1; f++){
                if (r==0 ^ f==0){
                    moveCandidates.add(LocationFactory.buildLocation(currentLocation, r, f));
                }
            }
        }
        
        // approach without lambda function by introducing local map:
        List<Location> validMoves = moveCandidates.stream().filter(squareMap::containsKey).collect(Collectors.toList());

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