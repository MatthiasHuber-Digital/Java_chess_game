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
    private static boolean isCheckMate = false;
    private static boolean isCheck = true;
    private static Map<AbstractPiece, List<Location>> checkResolvingMoves = new HashMap<>();

    public static void main(String[] args){
        Board board = new Board();
        board.printBoard();
        //board.getLightPieces().forEach(System.out::println);

        try {
            // true means the game is not finished
            while(!isCheckMate){
                // E2 E4  -- origin and destination
                isCheck = checkForCheckedKing();
                if (isCheck){
                    System.out.println(String.format("%s player - your king is in check.", turnOfColor));
                    System.out.println(String.format("%s player - enter move:", turnOfColor));
                    String moveLine = input_scan.nextLine();

                    if (isCheckResolvingMove()){
                        movePieceWithoutValidation(moveLine, board);
                    } 
                    else{
                        System.out.println("The move you entered does not resolve the check. Please try again.");
                    }
                }
                else{
                    System.out.println(String.format("%s player - enter move:", turnOfColor));
                    String moveLine = input_scan.nextLine();
                    movePieceIfPermitted(moveLine, board);
                }
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

    private static boolean isCheck(Board board){

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

        List<Location> kingNeighborSquares = king.getNeighbourLocations();
        List<AbstractPiece> checkHolders = new ArrayList<>();
        Map<AbstractPiece, Location> checkLockedLocationsMap = new HashMap<>();
        Map<AbstractPiece, List<Location>> possiblePieceMoves = possiblePieceMoves(checkPieceCandidates, board);
        
        boolean isCheck = false;
        for (AbstractPiece pieceCandidate : checkPieceCandidates){

            // only if there are any available moves for current piece
            if (!tempList.isEmpty()){
                // in case the current piece could move to the current position of the king - this is a check
                if (tempList.contains(king.getCurrentSquare().getLocation())){
                    isCheck = true;
                    checkHolders.add(pieceCandidate);
                }
                // in any case, collect those pieces who contribute to the check itself or to the 'locking up' of the king 
                // in his current position, and their locations in the king's neighborhood they could move to
                tempList = tempList.stream().filter((candidateMove) -> {return kingNeighborSquares.contains(candidateMove);}).collect(Collectors.toList());
                if (!tempList.isEmpty()){
                    for (Location tempLoc : tempList) {
                        checkLockedLocationsMap.put(pieceCandidate, tempLoc);
                    }
                }
            }

        }
        return isCheck;
    }

    private boolean checkForCheckedKing(){
        
    }

    private Map<AbstractPiece, List<Location>> possiblePieceMoves(List<AbstractPiece> pieces, Board board){

        Map<AbstractPiece, List<Location>> moveMap = new HashMap<>();

        for (AbstractPiece piece : pieces){
            List<Location> tempList = piece.getValidMoves(board);
            moveMap.put(piece, tempList);
        }

        return moveMap;
    }

}
