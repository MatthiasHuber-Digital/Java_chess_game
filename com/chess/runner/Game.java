package com.chess.runner;
import com.chess.board.*;
import com.chess.common.*;
import com.chess.piece.*;
import com.chess.squares.*;

import java.util.stream.Collectors;
import java.text.Format;
import java.util.*;


public class Game {
    
    public static Scanner input_scan = new Scanner(System.in);
    private static PieceColor turnOfColor = PieceColor.LIGHT;
    private static PieceColor matchWinner = null;

    public static void main(String[] args){
        Board board = new Board();
        board.printBoard();
        //board.getLightPieces().forEach(System.out::println);

        try {
            // true means the game is not finished
            while(!isCheckMate(board)){
                // E2 E4  -- origin and destination
                System.out.println("Enter move:");
                String moveLine = input_scan.nextLine();
                movePieceIfPermitted(moveLine, board);

            }
            System.out.println(String.format("Checkmate: %s won the match.", matchWinner));

        } catch (UnsupportedOperationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private static void movePieceIfPermitted(String line, Board board){
        
        if (inputCoordinatesInChessboard(line)){
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

            if (fromSquare.getIsOccupied()) {
                AbstractPiece currentPiece = fromSquare.getCurrentPiece();

                if (currentPiece.getPieceColor().equals(turnOfColor)){
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
                        turnOfColor = turnOfColor.next();
                    }
                    else{
                        System.out.println("The chosen piece cannot move to this square.");
                    }
                }
                else{
                    System.out.println("The picked piece's color is: " + currentPiece.getPieceColor() + ", but actually it's the other color's turn.");
                }
            }
            else{
                System.out.println("The origin square is invalid because it's empty.");
            }
        }
        else{
            System.out.println("Invalid input characters or coordinates not in chess board. Please try again.");
        }

    }

    private static boolean inputCoordinatesInChessboard(String line){
        return line.matches("[a-hA-H]+[1-8]+['-]+[a-hA-H]+[1-8]");
    }

    private static boolean isCheckMate(Board board){

        List<AbstractPiece> checkPieceCandidates = new ArrayList<>();
        if (turnOfColor.equals(PieceColor.DARK)){
            checkPieceCandidates = board.getDarkPieces();
        }
        else{
            checkPieceCandidates = board.getLightPieces();
        }

        checkPieceCandidates.stream().filter((candidate) -> {return(!candidate.pieceHasBeenCaptured);}).collect(Collectors.toList());
        AbstractPiece king = new King(turnOfColor);

        for (AbstractPiece candidate : checkPieceCandidates){
            if (candidate.getName().equals("King")){
                king = candidate;
            }
        }

        for (AbstractPiece candidate : checkPieceCandidates){
            if (!candidate.getName().equals("Queen")){
                try{
                    List<Location> tempList = candidate.getValidMoves(board);
                    if ((!tempList.isEmpty()) && (tempList.contains(king.getCurrentSquare().getLocation()))){
                        return true;
                    }
                }
                catch (NullPointerException e){
                    e.printStackTrace();
                }
            }
        }
        return false;
    }
}
