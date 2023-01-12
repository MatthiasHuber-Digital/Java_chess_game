package com.chess.game;

public enum GameMode {
    MANUAL,
    AUTO;

    private static final GameMode[] vals = values();
    
    public GameMode next() {
        return vals[(this.ordinal() + 1) % vals.length];
    }
}
