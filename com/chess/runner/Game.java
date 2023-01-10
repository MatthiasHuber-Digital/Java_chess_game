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
    private static List<Location> kingNeighborLocations = new ArrayList<>();
    private static List<AbstractPiece> enemyCheckHolders = new ArrayList<>();
    private static Map<AbstractPiece, List<Location>> checkLockedLocationsMap = new HashMap<>();
    private static Map<AbstractPiece, List<Location>> possiblePieceMoves = new HashMap<>();
    private static Map<AbstractPiece, List<Location>> checkResolvingMoves = new HashMap<>();
    private static AbstractPiece king;

    public static void main(String[] args){
        Board board = new Board();
        board.printBoard();
        //board.getLightPieces().forEach(System.out::println);

        try {
            
            while(!isCheckMate){
                
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
                checkForCheckedKing(board);
                if(isCheck){
                    checkForCheckmate();
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

    private static void getKingOfTurn(Board board){
            // we need to select the king of the color currently active
            List<AbstractPiece> kingList = new ArrayList<>();
            AbstractPiece king = new King(turnOfColor);
            // 
            for (AbstractPiece candidate : kingList){
                if (candidate.getName().equals("King")){
                    king = candidate;
                }
            }
    }

    private static void checkForCheckedKing(Board board){

        getKingOfTurn(board);
        // load the king's neighbouring locations:
        kingNeighborLocations = king.getNeighbourLocations();

        // load all possible moves of the enemy
        possiblePieceMoves(board);

        boolean isCheck = false;
        List<Location> tempList = new ArrayList<>();
        // go through the potential enemy moves
        // a) look if any enemy piece keeps the king in check
        // b) save pieces and locations next to the king which are potential move destinations of the enemy
        // c) save a list of pieces exerting the check
        for (AbstractPiece pieceCandidate : possiblePieceMoves.keySet()){
            tempList = possiblePieceMoves.get(pieceCandidate);

            // only if there are any available moves for current piece
            if (!tempList.isEmpty()){
                // in case the current piece could move to the current position of the king - this is a check
                if (tempList.contains(king.getCurrentSquare().getLocation())){
                    isCheck = true;
                    enemyCheckHolders.add(pieceCandidate);
                }
                // in any case, collect those pieces who contribute to the check itself or to the 'locking up' of the king 
                // in his current position, and their locations in the king's neighborhood they could move to
                tempList = tempList.stream().filter((candidateMove) -> {return kingNeighborLocations.contains(candidateMove);}).collect(Collectors.toList());
                if (!tempList.isEmpty()){
                    checkLockedLocationsMap.put(pieceCandidate, tempList);
                }
            }
        }
    }

    private static void possiblePieceMoves(Board board){

        // we need to load the valid moves of the color currently inactive - in order to assess if there's a check given
        if (turnOfColor.equals(PieceColor.DARK)){
            enemyCheckHolders = board.getLightPieces();
        }
        else{
            enemyCheckHolders = board.getDarkPieces();
        }
        // loading the pieces that could potentially keep the current king in check:
        enemyCheckHolders.stream().filter((candidate) -> {return(!candidate.pieceHasBeenCaptured);}).collect(Collectors.toList());

        for (AbstractPiece piece : enemyCheckHolders){
            List<Location> tempList = piece.getValidMoves(board);
            possiblePieceMoves.put(piece, tempList);
        }
    }

    private static void checkForCheckmate(){
        /* This function covers the following checkmate requirements:
         * 1. King will be in check no matter to which field he moves
         * 2. No piece can shield the king from the check
         * 3. No piece can capture the piece(s) keeping the king in check
         * Prerequisite: king is in check
         */
        isCheckMate = true;
        List<Location> lockedList = new ArrayList<>();
        List<Location> resolvingMovesList = new ArrayList<>();

        // check if ALL king's neighboring locations are locked:
        // ...all non-locked positions need to be added to the resolving-check move candidates.
        lockedList = checkLockedLocationsMap.values().stream().distinct().flatMap(l -> l.stream()).collect(Collectors.toList()); // get all unique locked neighboring positions
        
        boolean locked;
        for (Location neighborLoc : kingNeighborLocations){
            locked = false;

            for (Location lockedLoc : checkLockedLocationsMap){
                if ((neighborLoc.getFile().ordinal() == lockedLoc.getFile().ordinal()) && (neighborLoc.getRank() == lockedLoc.getRank())){
                    locked = true;
                    break;
                }
            }

            if (!locked){
                resolvingMovesList.add(neighborLoc);
            }
        }
        checkResolvingMoves.put(king, resolvingMovesList);

        // Go through all pieces keeping the king in check (enemy pieces) except for the king
        // Check if there's a move of a current player's piece which could capture that figure
        // ...simulate capturing that figure and if the current player's king would be still in check then
        // ...all moves removing the check need to be added to the resolving-check move candidates.

        
        // Go through all pieces keeping the king in check (enemy pieces) except for knights, kings and pawns
        // Go through all enemy moves keeping the king in check
        // Then, go through all friendly pieces except for the king, and simulate shielding the king from the threat
        // ... check if the king will be still in check under these conditions
        // ...all moves removing the check need to be added to the resolving-check move candidates.

    }
}
