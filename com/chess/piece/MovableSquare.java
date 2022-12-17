package com.chess.piece;
import java.util.List;
import com.chess.common.Location;
import com.chess.board.*;
import com.chess.squares.*;

public interface MovableSquare {
    List<Location> getValidMoves(Board board, Square square);
}
