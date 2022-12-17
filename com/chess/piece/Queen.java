package com.chess.piece;
import java.util.*;
import com.chess.squares.*;
import com.chess.board.*;
import com.chess.common.*;

public class Queen extends AbstractPiece implements Movable{
    // the sense of these attributes is to combine the moves of a bishop and a rook:
    private MovableSquare bishop;
    private MovableSquare rook;

    // initial constructor:
    public Queen(PieceColor pieceColor){
        super(pieceColor);
        this.name = "Queen";
    }

    // later constructor:
    public Queen(PieceColor pieceColor, MovableSquare bishop, MovableSquare rook){
        super(pieceColor);
        this.bishop = bishop;
        this.rook = rook;
    }

    // this one is interesting because it creates an empty list of locations
    // ...and then adds valid moves of rook and bishop
    @Override
    public List<Location> getValidMoves(Board board){
        List<Location> moveCandidates = Collections.emptyList();
        moveCandidates.addAll(bishop.getValidMoves(board, this.getCurrentSquare()));
        moveCandidates.addAll(rook.getValidMoves(board, this.getCurrentSquare()));
        return moveCandidates;
    }

    @Override
    public void makeMove(Square square){
        Square current = this.getCurrentSquare();
        this.setCurrentSquare(square);
        current.reset();
    };
}