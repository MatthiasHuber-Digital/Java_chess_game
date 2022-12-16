package com.chess.piece;
import com.chess.squares.*;

public abstract class AbstractPiece {
    protected String name;
    protected PieceColor pieceColor;
    protected Square currentSquare;

    public AbstractPiece(PieceColor pieceColor){
        this.pieceColor = pieceColor;
    }

    public PieceColor getPieceColor(){
        return this.pieceColor;
    }

    public String getName(){
        return this.name;
    }

    public Square getCurrentSquare(){
        return this.currentSquare;
    }

    public void setCurrentSquare(Square currentSquare){
        this.currentSquare = currentSquare;
    }

    @Override
    public String toString() {
        return "{" +
            " name='" + getName() + "'" +
            ", pieceColor='" + getPieceColor() + "'" +
            ", currentSquare='" + getCurrentSquare() + "'" +
            "}";
    }
}