package com.chess.runner;
import com.chess.board.*;
import com.chess.piece.*;

// https://www.youtube.com/watch?v=BVhIylwf4sA

public class Game {
    public static void main(String[] args){
        PieceColor pieceColor = PieceColor.DARK;
        Pawn pawn = new Pawn(pieceColor);
        Queen queen = new Queen(pieceColor);
        Game.printPiece(pawn);
        Game.printPiece(queen);
    }

/*     public static void main(String[] args){
        Board board = new Board();
        board.printBoard();
    } */

    public static void printPiece(AbstractPiece piece){
        System.out.println(piece.toString());
    }
}
