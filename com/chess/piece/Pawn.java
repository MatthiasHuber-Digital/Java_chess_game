package com.chess.piece;
import java.util.*;
import java.util.stream.Collectors;
import com.chess.squares.*;
import com.chess.board.*;
import com.chess.common.*;

public class Pawn extends AbstractPiece{
    private int rankDirectionMultiplier;
    private boolean isFirstMove = true;

    this.name = "Pawn";
    
    public Pawn(PieceColor pieceColor){
        super(pieceColor);
        if (this.pieceColor.equals(PieceColor.LIGHT)){
            rankDirectionMultiplier = 1;
        }
        else{
            rankDirectionMultiplier = -1;
        }
    }

    @Override
    public List<Location> getValidMoves(Board board){

        Map<Location, Square> squareMap = board.getLocationSquareMap();
        Location currentLocation = this.getCurrentSquare().getLocation();
        ArrayList<Location> moveCandidates = new ArrayList<>();

        // FILE (A), then RANK(1)
        if (this.isFirstMove){
            int[][] offsets = {
                {0, 1 * rankDirectionMultiplier}, 
                {1, 1 * rankDirectionMultiplier}, 
                {-1, 1 * rankDirectionMultiplier},
            };
            this.isFirstMove = false;
            moveCandidates = this.filterMovesInBoard(offsets, currentLocation);
        }
        else{
            int[][] offsets = {
                {0, 1 * rankDirectionMultiplier},
                {0, 2 * rankDirectionMultiplier}, 
                {1, 1 * rankDirectionMultiplier}, 
                {-1, 1 * rankDirectionMultiplier},
            };
            moveCandidates = this.filterMovesInBoard(offsets, currentLocation);
        }
        
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

    }

}
