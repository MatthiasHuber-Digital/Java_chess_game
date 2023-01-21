package com.chess.piece;
import java.util.*;
import com.chess.squares.*;
import com.chess.board.*;
import com.chess.common.*;

public class Queen extends AbstractPiece{

    // FILE (A), then RANK(1)
    int[][] offsets = {
        {1, 0}, 
        {-1, 0}, 
        {0, 1}, 
        {0, -1},
        {1, 1}, 
        {-1, -1}, 
        {-1, 1}, 
        {1, -1},
    };

    public Queen(PieceColor pieceColor, Location initialBoardLocation){
        super(pieceColor, initialBoardLocation);
        this.name = "Queen";
    }

    @Override
    public List<Location> getValidMoves(Board board){

        Map<Location, Square> squareMap = board.getLocationSquareMap();
        Location currentLocation = this.getCurrentSquare().getLocation();
        ArrayList<Location> moveCandidates = new ArrayList<>();

        moveCandidates = this.filterStraightMovesInBoard(offsets, currentLocation);

        return this.filterUnblockedStraightMoves(moveCandidates, squareMap, currentLocation, offsets);

    }
}