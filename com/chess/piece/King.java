package com.chess.piece;

import java.util.*;
import java.util.stream.Collectors;
import com.chess.squares.*;
import com.chess.board.*;
import com.chess.common.*;

public class King extends AbstractPiece {

    public King(PieceColor pieceColor) {
        super(pieceColor);
        this.name = "King";
    }

    @Override
    public List<Location> getValidMoves(Board board) {

        Map<Location, Square> squareMap = board.getLocationSquareMap();
        Location currentLocation = this.getCurrentSquare().getLocation();
        int[][] offsets = {
                { -1, 1 },
                { -1, 0 },
                { -1, -1 },
                { 0, -1 },
                { 0, 1 },
                { 1, 0 },
                { 1, 0 },
                { 1, -1 },
        };

        List<Location> moveCandidates = this.filterMovesInBoard(offsets, currentLocation);

        return moveCandidates.stream().filter((candidate) -> {
            if ((squareMap.get(candidate).getIsOccupied() &&
                    squareMap.get(candidate).getCurrentPiece().getPieceColor().equals(this.pieceColor))) {
                return false;
            } else {
                return true;
            }
        }).collect(Collectors.toList());
    }

    protected List<Location> getCheckResolutionMoves() {
        /*
         * This function returns the permitted moves for the king, in case the king is
         * in checked state.
         * In case the map is empty, there is no move of the king himself which could
         * resolve the check.
         * This means the check would have to be resolved by shielding the king with
         * another piece or by
         * capturing the enemy piece which keeps the king in check.
         */
        List<Location> resolutionMoves = new ArrayList<>();

        return resolutionMoves;
    }


}