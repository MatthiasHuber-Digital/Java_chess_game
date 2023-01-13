package com.chess.game;

public final class GameDetails {
    private static GameDetails INSTANCE;
    //private static String autoMovesString = "c2 c4 f7 f5";
    //private static String autoMovesString = "d4 d5 Nc3 Nf6 Bf4 Bf5 Nf3 e6 e3 Be7 Bb5+ c6 Bd3 Bxd3 Qxd3 O-O Bg5 Ng4 Bxe7 Qxe7 O-O Qg5 h3 Qf6 hxg4 Qh6 Na4 b5 Nc5 Kh8 Nb7 g6 Nd6 Na6 Ne5 Kg8 Ndxf7 Rxf7 Nxf7 Kxf7 f4 g5 fxg5+ Kg7 gxh6+ Kxh6 Rf6+ Kg7 g5 h6 Qg6+ Kh8 Qxh6+ Kg8 Rxe6 Kf7 Qf6+ Kg8 Re8+ Rxe8 Qg6+ Kf8 Qh6+ Kg8 g6 Nb4 Rf1 Nxc2 Qh7#";
    private static String autoMovesString = "d2 d4 d7 d5 b1 c3 g8 f6 c1 f4 c8 f5 g1 f3 e7 e6 e2 e3 f8 e7 f1 b5 c7 c6";
    private static int moveIndex = 0;
    private static String currentAutoMove;
    private static String[] autoMovesArray;
    
    private GameDetails(){
    }

    public static GameDetails getInstance(){
        if (INSTANCE == null){
            INSTANCE = new GameDetails();
        }
        return INSTANCE;
    }

    public static void splitAutoGameStringInMoves(){
        autoMovesArray = autoMovesString.split(" ");
    }

    public static String getAutoMove(){
        currentAutoMove = autoMovesArray[moveIndex] + autoMovesArray[moveIndex+1];
        moveIndex = moveIndex + 2;

        return currentAutoMove;
    }
}