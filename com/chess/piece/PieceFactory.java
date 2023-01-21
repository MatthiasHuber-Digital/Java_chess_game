package com.chess.piece;
import java.util.*;
import com.chess.common.File;
import com.chess.common.Location;

// final class means no inheritance
public final class PieceFactory {

    private PieceFactory(){ }

    public static Map<Location, AbstractPiece> getPieces(){
        Map<Location, AbstractPiece> pieces = new HashMap<>();

        // rooks
        // LIGHT is at the SOUTHERN part of the chessboard:
        Location initialLocation = new Location(File.A, 1);
        pieces.put(initialLocation, new Rook(PieceColor.LIGHT, initialLocation));

        initialLocation = new Location(File.H, 1);
        pieces.put(initialLocation, new Rook(PieceColor.LIGHT, initialLocation));

        // DARK is at the NORTHERN part of the chessboard:
        initialLocation = new Location(File.A, 8);
        pieces.put(initialLocation, new Rook(PieceColor.DARK, initialLocation));

        initialLocation = new Location(File.H, 8);
        pieces.put(initialLocation, new Rook(PieceColor.DARK, initialLocation));
        

        // knights
        // LIGHT is at the SOUTHERN part of the chessboard:
        initialLocation = new Location(File.B, 1);
        pieces.put(initialLocation, new Knight(PieceColor.LIGHT, initialLocation));

        initialLocation = new Location(File.G, 1);
        pieces.put(initialLocation, new Knight(PieceColor.LIGHT, initialLocation));

        // DARK is at the NORTHERN part of the chessboard:
        initialLocation = new Location(File.B, 8);
        pieces.put(initialLocation, new Knight(PieceColor.DARK, initialLocation));

        initialLocation = new Location(File.G, 8);
        pieces.put(initialLocation, new Knight(PieceColor.DARK, initialLocation));
        

        // bishops
        // LIGHT is at the SOUTHERN part of the chessboard:
        initialLocation = new Location(File.C, 1);
        pieces.put(initialLocation, new Bishop(PieceColor.LIGHT, initialLocation));

        initialLocation = new Location(File.F, 1);
        pieces.put(initialLocation, new Bishop(PieceColor.LIGHT, initialLocation));

        // DARK is at the NORTHERN part of the chessboard:
        initialLocation = new Location(File.C, 8);
        pieces.put(initialLocation, new Bishop(PieceColor.DARK, initialLocation));

        initialLocation = new Location(File.F, 8);
        pieces.put(initialLocation, new Bishop(PieceColor.DARK, initialLocation));
        

        // queens
        // LIGHT is at the SOUTHERN part of the chessboard:
        initialLocation = new Location(File.D, 1);
        pieces.put(initialLocation, new Queen(PieceColor.LIGHT, initialLocation));

        // DARK is at the NORTHERN part of the chessboard:
        initialLocation = new Location(File.D, 8);
        pieces.put(initialLocation, new Queen(PieceColor.DARK, initialLocation));
        

        // kings
        // LIGHT is at the SOUTHERN part of the chessboard:
        initialLocation = new Location(File.E, 1);
        pieces.put(initialLocation, new King(PieceColor.LIGHT, initialLocation));

        // DARK is at the NORTHERN part of the chessboard:
        initialLocation = new Location(File.E, 8);
        pieces.put(initialLocation, new King(PieceColor.DARK, initialLocation));
        

        // pawns:
        for (File file : File.values()){
            initialLocation = new Location(file, 2);
            pieces.put(initialLocation, new Pawn(PieceColor.LIGHT, initialLocation));

            // DARK is at the NORTHERN part of the chessboard:
            initialLocation = new Location(file, 7);
            pieces.put(initialLocation, new Pawn(PieceColor.DARK, initialLocation));
        }
        return pieces;
    }
}
