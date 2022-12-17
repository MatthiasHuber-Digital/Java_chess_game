package com.chess.piece;
import java.util.*;
import java.util.stream.Collectors;
import com.chess.squares.*;
import com.chess.board.*;
import com.chess.common.*;

public class Knight extends AbstractPiece implements Movable{
    
    public Knight(PieceColor pieceColor){
        super(pieceColor);
        this.name = "Knight";
    }

    @Override
    public List<Location> getValidMoves(Board board){
        List<Location> moveCandidates = new ArrayList<>();
        Map<Location, Square> squareMap = board.getLocationSquareMap();
        Location currentLocation = this.getCurrentSquare().getLocation();
        moveCandidates.add(LocationFactory.buildLocation(currentLocation, -2, 1));
        moveCandidates.add(LocationFactory.buildLocation(currentLocation, -2, -1));
        moveCandidates.add(LocationFactory.buildLocation(currentLocation, 2, 1));
        moveCandidates.add(LocationFactory.buildLocation(currentLocation, 2, -1));
        moveCandidates.add(LocationFactory.buildLocation(currentLocation, -1, 2));
        moveCandidates.add(LocationFactory.buildLocation(currentLocation, -1, -2));
        moveCandidates.add(LocationFactory.buildLocation(currentLocation, 1, 2));
        moveCandidates.add(LocationFactory.buildLocation(currentLocation, 1, -2));

        
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