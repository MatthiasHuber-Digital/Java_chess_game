package com.chess.runner;
import com.chess.board.*;
import com.chess.common.*;
import com.chess.piece.Movable;
import com.chess.squares.*;
import java.util.*;


public class Game {
    public static void main(String[] args){
        Board board = new Board();
        board.printBoard();
        //board.getLightPieces().forEach(System.out::println);

        try (Scanner scanner = new Scanner(System.in)) {
            // true means the game is not finished
            while(true){
                // E2 E4  -- origin and destination
                String line = scanner.nextLine();
                String[] fromTo = line.split("-");
                // we need the enum FILE from an integer value, that fom the string conversion of a character 
                // that character is cast to upper case. 
                // the character is the first character of the "origin"
                System.out.println("From-To: " + fromTo[0] + ", " + fromTo[1]);
                File fromFile = File.valueOf(String.valueOf(Character.toUpperCase(fromTo[0].charAt(0))));
                int fromRank = Integer.parseInt(String.valueOf(fromTo[0].charAt(1)));
                // we proceed similarly for the destination:
                File toFile = File.valueOf(String.valueOf(Character.toUpperCase(fromTo[1].charAt(0))));
                int toRank = Integer.parseInt(String.valueOf(fromTo[1].charAt(1)));
            
                Square fromSquare = board.getLocationSquareMap().get(new Location(fromFile, fromRank));
                Square toSquare = board.getLocationSquareMap().get(new Location(toFile, toRank));

                fromSquare.getCurrentPiece().makeMove(toSquare);
                //fromSquare.reset();

                board.printBoard();

            }
        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static void printPiece(Movable piece){
        piece.getValidMoves(null);
    }
}
