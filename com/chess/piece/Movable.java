package com.chess.piece;
import java.util.List;
import com.chess.common.Location;
import com.chess.board.*;
import com.chess.squares.*;

public interface Movable {
    List<Location> getValidMoves(Board board);
    List<Location> getValidMoves(Board board, Square square);
    void makeMove(Square square);
}
