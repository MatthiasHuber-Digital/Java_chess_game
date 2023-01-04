package com.chess.piece;
import java.util.*;
import java.util.stream.Collectors;
import com.chess.squares.*;
import com.chess.board.*;
import com.chess.common.*;

public class Pawn extends AbstractPiece{
    private boolean isFirstMove = true;
    private int rankDirectionMultiplier;

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

    @Override
    public List<Location> getValidMoves(Board board){
        //List<Location> moveCandidates = Collections.emptyList();
        List<Location> moveCandidates = new ArrayList<>();
        Location currentLocation = this.getCurrentSquare().getLocation();
        
        moveCandidates.add(LocationFactory.buildLocation(currentLocation, 0, 1 * rankDirectionMultiplier));
        if (isFirstMove){
            moveCandidates.add(LocationFactory.buildLocation(currentLocation, 0, 2 * rankDirectionMultiplier));
            isFirstMove = false;
            return moveCandidates;
        }
        moveCandidates.add(LocationFactory.buildLocation(currentLocation, 1, 1 * rankDirectionMultiplier));
        moveCandidates.add(LocationFactory.buildLocation(currentLocation, -1, 1 * rankDirectionMultiplier));
        
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

}
