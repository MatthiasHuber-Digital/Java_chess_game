package com.chess.piece;
import java.util.*;
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
        List<Location> moveCandidates = Collections.emptyList();
        return null;
    }

    @Override
    public void makeMove(Square square){
        System.out.println(this.getName() + "-> makeMove()");
    };
}