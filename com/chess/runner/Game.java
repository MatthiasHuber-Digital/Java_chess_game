package com.chess.runner;
import com.chess.board.*;
import com.chess.common.*;
import com.chess.piece.*;
import com.chess.squares.*;
import java.util.*;


public class Game {
    
    public static Scanner input_scan = new Scanner(System.in);

    public static void main(String[] args){
        Board board = new Board();
        board.printBoard();
        //board.getLightPieces().forEach(System.out::println);

        try {
            // true means the game is not finished
            while(true){
                // E2 E4  -- origin and destination
                String moveLine = input_scan.nextLine();
                movePieceIfPermitted(moveLine, board);

            }
        } catch (UnsupportedOperationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private static void movePieceIfPermitted(String line, Board board){

        String[] fromTo = line.split("-");
        // we need the enum FILE from an integer value, that fom the string conversion of a character 
        // that character is cast to upper case. 
        // the character is the first character of the "origin"
        File fromFile = File.valueOf(String.valueOf(Character.toUpperCase(fromTo[0].charAt(0))));
        int fromRank = Integer.parseInt(String.valueOf(fromTo[0].charAt(1)));
        // we proceed similarly for the destination:
        File toFile = File.valueOf(String.valueOf(Character.toUpperCase(fromTo[1].charAt(0))));
        int toRank = Integer.parseInt(String.valueOf(fromTo[1].charAt(1)));
    
        Square fromSquare = board.getLocationSquareMap().get(new Location(fromFile, fromRank));
        Square toSquare = board.getLocationSquareMap().get(new Location(toFile, toRank));

        AbstractPiece currentPiece = fromSquare.getCurrentPiece();

        List<Location> validMoves = currentPiece.getValidMoves(board);
        // go through validMoves list and see if we have a location with identical rank and file
        Boolean isValidMove = false;
        for (Location l : validMoves) {
            //System.out.println("toFile: " + toFile);
            //System.out.println("toRank: " + toRank);

            if (toFile.equals(l.getFile()) && toRank == l.getRank()){
                isValidMove = true;
                break;
            }
        }

        if (isValidMove){
            currentPiece.makeMove(toSquare, toFile, toRank, board);
            // pawns need special checks
            if (currentPiece.getName().equals("Pawn")){
                // each pawn's first move allows for walking 2 squares straight ahead. Afterwards, we deactivate this possibilty:
                if (currentPiece.getIsFirstMove()){
                    currentPiece.deactivateFirstMove();
                } 
                else{
                    if ((currentPiece.getPieceColor() == PieceColor.LIGHT) && (currentPiece.getCurrentSquare().getLocation().getRank() == 8) ||
                        (currentPiece.getPieceColor() == PieceColor.DARK) && (currentPiece.getCurrentSquare().getLocation().getRank() == 1)){
                            currentPiece.returnChosenPieceToBoard(board);
                        }
                }
            }


            board.printBoard();
        }
        else{
            System.out.println("The planned move is invalid. Enter a valid move.");
        }

    }

}
