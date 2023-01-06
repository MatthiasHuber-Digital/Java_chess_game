package com.chess.piece;
import java.util.*;
import com.chess.squares.*;
import com.chess.board.*;
import com.chess.common.*;

public class Queen extends AbstractPiece{
    // the sense of these attributes is to combine the moves of a bishop and a rook:
    private Movable bishop;
    private Movable rook;

    // initial constructor:
    public Queen(PieceColor pieceColor){
        super(pieceColor);
        this.name = "Queen";
    }

    // later constructor:
    public Queen(PieceColor pieceColor, Movable bishop, Movable rook){
        super(pieceColor);
        this.bishop = bishop;
        this.rook = rook;
    }

    @Override
    public List<Location> getValidMoves(Board board){
        List<Location> moveCandidates = Collections.emptyList();
        //List<Location> moveCandidates = new ArrayList<>();
        moveCandidates.addAll(bishop.getValidMoves(board));
        moveCandidates.addAll(rook.getValidMoves(board));
        return moveCandidates; 
    }
}