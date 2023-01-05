package com.chess.piece;
import java.util.*;
import java.util.stream.Collectors;
import com.chess.squares.*;
import com.chess.board.*;
import com.chess.common.*;

public class Pawn extends AbstractPiece{
    private int rankDirectionMultiplier;
    private boolean isFirstMove = true;

    public Pawn(PieceColor pieceColor){
        super(pieceColor);
        this.name = "Pawn";
        if (this.pieceColor.equals(PieceColor.LIGHT)){
            rankDirectionMultiplier = 1;
        }
        else{
            rankDirectionMultiplier = -1;
        }
    }

    public void deactivateFirstMove(){
        this.isFirstMove = false;
    }

    public boolean getIsFirstMove(){
        return this.isFirstMove;
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
                {0, 2 * rankDirectionMultiplier}, 
                {1, 1 * rankDirectionMultiplier}, 
                {-1, 1 * rankDirectionMultiplier},
            };
            moveCandidates = this.filterMovesInBoard(offsets, currentLocation);
        }
        else{
            int[][] offsets = {
                {0, 1 * rankDirectionMultiplier},
                {1, 1 * rankDirectionMultiplier}, 
                {-1, 1 * rankDirectionMultiplier},
            };
            moveCandidates = this.filterMovesInBoard(offsets, currentLocation);
        }

        return moveCandidates.stream().filter((candidate) -> {
                    return((!squareMap.get(candidate).getIsOccupied() && candidate.getFile().ordinal() == currentLocation.getFile().ordinal()) || 
                        (squareMap.get(candidate).getIsOccupied() && 
                        !squareMap.get(candidate).getCurrentPiece().getPieceColor().equals(this.pieceColor) &&
                        candidate.getFile().ordinal() != currentLocation.getFile().ordinal())
                        );
                }
            ).collect(Collectors.toList());

    }


}
