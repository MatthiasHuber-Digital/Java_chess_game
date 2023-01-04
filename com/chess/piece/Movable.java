package com.chess.piece;
import java.util.List;
import com.chess.common.*;
import com.chess.board.*;
import com.chess.squares.*;

public interface Movable {
    List<Location> getValidMoves(Board board);
    public void makeMove(Square targetSquare, File targetFile, int targetRank, Board board);
}
