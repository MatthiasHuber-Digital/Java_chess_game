package com.chess.piece;

public enum PieceColor {
    LIGHT,
    DARK;

    private static final PieceColor[] vals = values();
    
    public PieceColor next() {
        return vals[(this.ordinal() + 1) % vals.length];
    }
}
