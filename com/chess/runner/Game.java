package com.chess.runner;

import com.chess.game.*;
import com.chess.board.*;
import com.chess.common.*;
import com.chess.piece.*;
import com.chess.squares.*;

import java.util.stream.Collectors;
import java.text.Format;
import java.util.*;

public class Game{
    
    public static Scanner input_scan = new Scanner(System.in);
    private static PieceColor turnOfColor = PieceColor.LIGHT;
    private static PieceColor matchWinner = null;
    private static boolean isCheckMate = false;
    private static boolean isCheck = false;
    private static List<Location> kingNeighborLocations = new ArrayList<>();
    private static List<AbstractPiece> enemyCheckHolders = new ArrayList<>();
    private static Map<AbstractPiece, List<Location>> checkLockedLocationsMap = new HashMap<>();
    private static Map<AbstractPiece, List<Location>> possibleEnemyPieceMoves = new HashMap<>();
    private static Map<AbstractPiece, List<Location>> possibleOwnPieceMoves = new HashMap<>();
    private static Map<AbstractPiece, List<Location>> checkResolvingMoves = new HashMap<>();
    private static Map<AbstractPiece, Location> previousLightLocations = new HashMap<>();
    private static Map<AbstractPiece, Location> previousDarkLocations = new HashMap<>();
    private static AbstractPiece king;
    private static Board board = new Board();
    private static GameDetails gameDetails;
    private static GameMode gameMode;

    public static void main(String[] args){

        System.out.println("+ + +   JAVA-CHESS by Matthias Huber   + + +");
        System.out.println("partially based on the YT-tutorials of Gerard Taylor (https://www.youtube.com/watch?v=xaJxBsxqkyM)");
        
        while (true){
            System.out.println("\nMode: Manual (M) or Auto (A)?");
            String inputManualOrAutoPlay = input_scan.nextLine();
            if (validGameModeChoiceInput(inputManualOrAutoPlay)){
                if (inputManualOrAutoPlay.equalsIgnoreCase("a")){
                    gameMode = GameMode.AUTO;
                    gameDetails.splitAutoGameStringInMoves();
                }
                else{
                    gameMode = GameMode.MANUAL;
                }
                break;
            }
            else{
                System.out.println("Invalid input. Please enter m/M or a/A.");
            }
        }


        board.printBoard();
        try {
            
            while(!isCheckMate){
                
                System.out.println("\n");
                if (isCheck){
                    System.out.println(String.format("%s player - your king is in check.", turnOfColor));
                    System.out.println(String.format("%s player - enter move:", turnOfColor));
                    String moveLine;
                    if (gameMode == GameMode.MANUAL){
                        moveLine = input_scan.nextLine();
                    }
                    else{
                        moveLine = gameDetails.getAutoMove();
                        System.out.println("...auto-player: " + moveLine);
                    }
                    
                    if (inputCoordinatesInChessboard(moveLine) && moveLine.length() == 4){
                        String fromLoc = moveLine.substring(0,2);
                        String toLoc = moveLine.substring(2,4);
            
                        // we need the enum FILE from an integer value, that fom the string conversion of a character 
                        // that character is cast to upper case. 
                        // the character is the first character of the "origin"
                        File fromFile = File.valueOf(String.valueOf(Character.toUpperCase(fromLoc.charAt(0))));
                        int fromRank = Integer.parseInt(String.valueOf(fromLoc.charAt(1)));
                        
                        // we proceed similarly for the destination:
                        File toFile = File.valueOf(String.valueOf(Character.toUpperCase(toLoc.charAt(0))));
                        int toRank = Integer.parseInt(String.valueOf(toLoc.charAt(1)));
                        
                        Square fromSquare = board.getLocationSquareMap().get(new Location(fromFile, fromRank));
                        Square toSquare = board.getLocationSquareMap().get(new Location(toFile, toRank));


                        if (isCheckResolvingMove(fromFile, toFile, fromRank, toRank)){
                            try{
                                movePieceIfPermitted(fromFile, toFile, fromRank, toRank, board);

                                // switching the player happens here in order to evaluate check and checkmate BEFORE the turn of the player
                                turnOfColor = turnOfColor.next();
                                checkForCheckedKing(board);

                                if(isCheck){
                                    checkForCheckmate();
                                    if (checkResolvingMoves.isEmpty()){
                                        isCheckMate = true;
                                        matchWinner = turnOfColor.next();
                                        break;
                                    }
                                }
                            }
                            catch (InvalidMoveException i){
                                System.out.println(i);
                            }
                        } 
                        else{
                            System.out.println("The move you entered does not resolve the check. Please try again.");
                        }
                    }
                    else{
                        System.out.println("Invalid input characters or coordinates not in chess board. Please try again.");
                    }
                }
                else{
                    
                    System.out.println(String.format("%s player - enter move:", turnOfColor));
                    String moveLine;
                    if (gameMode == GameMode.MANUAL){
                        moveLine = input_scan.nextLine();
                    }
                    else{
                        moveLine = gameDetails.getAutoMove();
                        System.out.println("...auto-player: " + moveLine);
                    }

                    if (inputCoordinatesInChessboard(moveLine) && moveLine.length() == 4){
                        String fromLoc = moveLine.substring(0,2);
                        String toLoc = moveLine.substring(2,4);
            
                        // we need the enum FILE from an integer value, that fom the string conversion of a character 
                        // that character is cast to upper case. 
                        // the character is the first character of the "origin"
                        File fromFile = File.valueOf(String.valueOf(Character.toUpperCase(fromLoc.charAt(0))));
                        int fromRank = Integer.parseInt(String.valueOf(fromLoc.charAt(1)));
                        
                        // we proceed similarly for the destination:
                        File toFile = File.valueOf(String.valueOf(Character.toUpperCase(toLoc.charAt(0))));
                        int toRank = Integer.parseInt(String.valueOf(toLoc.charAt(1)));

                        try{
                            movePieceIfPermitted(fromFile, toFile, fromRank, toRank, board);
                            
                            // switching the player happens here in order to evaluate check and checkmate BEFORE the turn of the player
                            turnOfColor = turnOfColor.next();
                            checkForCheckedKing(board);
    
                            if(isCheck){
                                checkForCheckmate();
                                if (checkResolvingMoves.isEmpty()){
                                    isCheckMate = true;
                                    matchWinner = turnOfColor.next();
                                    break;
                                }
                            }
                        }
                        catch (InvalidMoveException i){
                            System.out.println(i);
                        }
                    }
                    else{
                        System.out.println("Invalid input characters or coordinates not in chess board. Please try again.");
                    }
                }
                

            }
            System.out.println(String.format("CHECKMATE: The %s player won the match.", matchWinner));

        } catch (UnsupportedOperationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private static void movePieceIfPermitted (File fromFile, File toFile, int fromRank, int toRank, Board board) 
                                            throws InvalidMoveException{
                                
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
                    }
                    else{
                        throw new InvalidMoveException("The chosen piece cannot move to this square.");
                    }
                }
                else{
                    throw new InvalidMoveException("The picked piece's color is: " + currentPiece.getPieceColor() + ", but actually it's the other color's turn.");
                }
            }
            else{
                throw new InvalidMoveException("The origin square is invalid because it's empty.");
            }

    }

    
    private static boolean isCheckResolvingMove(File fromFile, File toFile, int fromRank, int toRank){

        Square fromSquare = board.getLocationSquareMap().get(new Location(fromFile, fromRank));

        AbstractPiece piece = fromSquare.getCurrentPiece();

        List<Location> pieceDestinations = checkResolvingMoves.get(piece);
        pieceDestinations.stream().filter(loc -> {return loc.getFile()==toFile && loc.getRank() == toRank;}).collect(Collectors.toList());

        return !pieceDestinations.isEmpty();
    }


    private static boolean inputCoordinatesInChessboard(String line){
        return line.matches("[a-hA-H]+[1-8]+[a-hA-H]+[1-8]");
    }


    private static boolean validGameModeChoiceInput(String line){
        return line.matches("[aAmM]");
    }


    private static void checkForCheckedKing(Board board){
        
        if (turnOfColor==PieceColor.LIGHT){
            king = board.lightKing;
        }else{
            king = board.darkKing;
        }
        // load the king's neighbouring locations:
        kingNeighborLocations = king.getNeighbourLocations(board);

        // load all possible moves of the enemy
        // we need to load the valid ENEMY moves (i.e. of the color currently inactive) - in order to assess if there's a check given
        if (turnOfColor.equals(PieceColor.DARK)){
            possibleEnemyPieceMoves = possibleColorPieceMoves(board, PieceColor.LIGHT);
        }
        else{
            possibleEnemyPieceMoves = possibleColorPieceMoves(board, PieceColor.DARK);
        }

        //boolean isCheck = false;
        List<Location> tempList = new ArrayList<>();
        // go through the potential enemy moves
        // a) look if any enemy piece keeps the king in check
        // b) save pieces and locations next to the king which are potential move destinations of the enemy
        // c) save a list of pieces exerting the check
        for (AbstractPiece pieceCandidate : possibleEnemyPieceMoves.keySet()){
            tempList = possibleEnemyPieceMoves.get(pieceCandidate);

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


    private static Map<AbstractPiece, List<Location>> possibleColorPieceMoves(Board board, PieceColor color){
        List<AbstractPiece> piecesOfCertainColor = new ArrayList<>();
        Map<AbstractPiece, List<Location>> possibleColorPieceMoves = new HashMap<>();

        // we need to load the valid moves of the color currently inactive - in order to assess if there's a check given
        if (color.equals(PieceColor.DARK)){
            piecesOfCertainColor = board.getDarkPieces();
        }
        else{
            piecesOfCertainColor = board.getLightPieces();
        }
        // loading the pieces that could potentially keep the current king in check:
        piecesOfCertainColor.stream().filter((candidate) -> {return(!candidate.pieceHasBeenCaptured);}).collect(Collectors.toList());

        for (AbstractPiece piece : piecesOfCertainColor){
            List<Location> tempList = piece.getValidMoves(board);
            possibleColorPieceMoves.put(piece, tempList);
        }

        return possibleColorPieceMoves;
    }


    private static void checkForCheckmate(){
        /* This function covers the following checkmate requirements:
         * 1. King will be in check no matter to which field he moves
         * 2. No piece can shield the king from the check
         * 3. No piece can capture the piece(s) keeping the king in check
         * Prerequisite: king is in check
         */

        // retain all king's moves which could resolve the check
        addResolvingMovesKing();

        possibleOwnPieceMoves = possibleColorPieceMoves(board, turnOfColor);
        // retain all piece moves which resolve the check via capturing the corresponding enemy pieces
        addResolvingMovesCapturing();
        
        // Go through all pieces keeping the king in check (enemy pieces) except for knights, kings and pawns
        // Go through all enemy moves keeping the king in check
        // Then, go through all friendly pieces except for the king, and simulate shielding the king from the threat
        // ... check if the king will be still in check under these conditions
        // ...all moves removing the check need to be added to the resolving-check move candidates.
        addResolvingMovesShielding();
    }
    
     
    private static List<Location> filterCheckLine(AbstractPiece pieceCheckHolder){
        // This function compiles the valid move destinations of a CHECKHOLDER enemy piece towards the KING which it is keeping in check.
        
        List<Location> listCheckLine = new ArrayList<>();
        int checkHolderFileOrdinal = pieceCheckHolder.getCurrentSquare().getLocation().getFile().ordinal();
        int checkHolderRank = pieceCheckHolder.getCurrentSquare().getLocation().getRank();

        int kingFileOrdinal = king.getCurrentSquare().getLocation().getFile().ordinal();
        int kingRank = king.getCurrentSquare().getLocation().getRank();

        int deltaFile = checkHolderFileOrdinal - kingFileOrdinal;
        int deltaRank = checkHolderRank - kingRank;

        int stepFile;
        if (deltaFile == 0){
            stepFile = 0;
        }else{
            if (deltaFile > 0){
                stepFile = 1;
            }
            else{ //deltaFile<0
                stepFile = -1;
            }
        }
        
        
        int stepRank;
        if (deltaRank == 0){
            stepRank = 0;
        }else{
            if (deltaRank > 0){
                stepRank = 1;
            }
            else{ //deltaRank<0
                stepRank = -1;
            }
        }

        //int iterFileOrdinal;
        //int iterRank;
        List<Location> tempList = new ArrayList<>();
        for (int locMultiplier=1; (Math.abs(locMultiplier * stepFile) < Math.abs(deltaFile)) && (Math.abs(locMultiplier * stepRank) < Math.abs(deltaRank)); locMultiplier++){
            //iterFileOrdinal = kingFileOrdinal + (locMultiplier * stepFile);
            //iterRank = kingRank + (locMultiplier * stepRank);
            listCheckLine.add(LocationFactory.buildLocation(king.getCurrentSquare().getLocation(), (locMultiplier * stepFile), (locMultiplier * stepRank)));
        }
        
        listCheckLine = listCheckLine.stream().filter(possibleEnemyPieceMoves.get(pieceCheckHolder)::contains).collect(Collectors.toList());

        return listCheckLine;
    }
    
    
    private static void addResolvingMovesShielding(){
        // This function adds (to the list of check resolving moves) all effective moves which shield the king from the piece(s) keeping the king in check.

        // Go through all pieces keeping the king in check (enemy pieces) except for the king
        // Check if there's a move of a current player's piece which could shield the king from ALL pieces exerting the check
        // ...simulate capturing that figure and if the current player's king would be still in check then
        // ...all moves removing the check need to be added to the resolving-check move candidates.
        List<AbstractPiece> longRangeCheckHolders = enemyCheckHolders.stream().filter(holder -> (holder instanceof Queen) || (holder instanceof Bishop) ||(holder instanceof Rook)).collect(Collectors.toList());

        
        for (AbstractPiece pieceCheckHolder : longRangeCheckHolders){
            
            // get the connection line of squares between the check holder and the own king
            // all locations of the connection line should be assessed
            List<Location> checkHolderCheckLine = filterCheckLine(pieceCheckHolder);

            for (AbstractPiece ownPiece : possibleOwnPieceMoves.keySet()){
                if (!ownPiece.getName().equalsIgnoreCase("king")){
                    List<Location> ownMoveList = possibleOwnPieceMoves.get(ownPiece);
                    ownMoveList = ownMoveList.stream().filter(checkHolderCheckLine::contains).collect(Collectors.toList());
                    
                    if (!ownMoveList.isEmpty()){
                        moveCheckResolveSimulation(ownMoveList, ownPiece, pieceCheckHolder);
                    }
                }
            }
        }
    }
    
    
    private static void addResolvingMovesCapturing(){
        // This function adds (to the list of check resolving moves) all effective check resolving moves which capture the piece keeping the king in check.

        // Go through all pieces keeping the king in check (enemy pieces) except for the king
        // Check if there's a move of a current player's piece which could capture that figure
        // ...simulate capturing that figure and if the current player's king would be still in check then
        // ...all moves removing the check need to be added to the resolving-check move candidates.
        for (AbstractPiece pieceCheckHolder : enemyCheckHolders){
            
            for (AbstractPiece ownPiece : possibleOwnPieceMoves.keySet()){
                if (!ownPiece.getName().equalsIgnoreCase("king")){
                    List<Location> ownMoveList = possibleOwnPieceMoves.get(ownPiece);
                    ownMoveList = ownMoveList.stream().filter((ownMove) -> {return pieceCheckHolder.getCurrentSquare().getLocation().equals(ownMove);}).collect(Collectors.toList());
                    
                    if (!ownMoveList.isEmpty()){
                        moveCheckResolveSimulation(ownMoveList, ownPiece, pieceCheckHolder);
                    }
                }
            }
        }
    }

    
    private static void addResolvingMovesKing(){
        // This function adds all the king's potential moves to the hashmap which contains the check-resolving moves.
        
        List<Location> lockedList = new ArrayList<>();
        List<Location> resolvingMovesList = new ArrayList<>();
        
        // check if ALL king's neighboring locations are locked:
        // ...all non-locked positions need to be added to the resolving-check move candidates.
        lockedList = checkLockedLocationsMap.values().stream().distinct().flatMap(l -> l.stream()).collect(Collectors.toList()); // get all unique locked neighboring positions
        
        resolvingMovesList.addAll(kingNeighborLocations);
        resolvingMovesList.removeAll(lockedList);

        if (!resolvingMovesList.isEmpty()){
            checkResolvingMoves.put(king, resolvingMovesList);
        }
    }
    

    private static void moveCheckResolveSimulation(List<Location> ownMoveList, AbstractPiece ownPiece, AbstractPiece pieceCheckHolder){
        // This function simulates the moves which could potentially resolve the check. 

        List<AbstractPiece> darkPieces = board.getDarkPieces();
        List<AbstractPiece> lightPieces = board.getLightPieces();
        List<Location> uselessMovesList = new ArrayList<>();
        
        // get pieces according to color:
        darkPieces.stream().filter((candidate) -> {return(!candidate.pieceHasBeenCaptured);}).collect(Collectors.toList());
        lightPieces.stream().filter((candidate) -> {return(!candidate.pieceHasBeenCaptured);}).collect(Collectors.toList());
        
        // save current locations as "previous", since we're about to simulate moves and subsequently undo them again
        Location currentLocation = null;
        for (AbstractPiece darkPiece : darkPieces){
            currentLocation = darkPiece.getCurrentSquare().getLocation();
            previousDarkLocations.put(darkPiece, currentLocation);
        }
        for (AbstractPiece lightPiece : lightPieces){
            currentLocation = lightPiece.getCurrentSquare().getLocation();
            previousDarkLocations.put(lightPiece, currentLocation);
        }
        
        // simulate the move
        for (Location moveDestination : ownMoveList){
            //AbstractPiece.simulatePieceRemovalFromBoard(pieceCheckHolder);
            AbstractPiece.simulateMove(ownPiece, moveDestination, board);
            //ownPiece.makeMove(board.locationSquareMap.get(moveDestination), 
            //                    moveDestination.getFile(), moveDestination.getRank(), 
            //                    board);
            if (simulatedMoveCheckAssessment()){
                uselessMovesList.add(moveDestination);
            };
            
            AbstractPiece.rollBackSimulatedMove(ownPiece);
            //AbstractPiece.rollBackPieceRemovalFromBoard(pieceCheckHolder);
        }
        ownMoveList.removeAll(uselessMovesList);

        if (!ownMoveList.isEmpty()){
            checkResolvingMoves.put(ownPiece, ownMoveList);
        }
        
    }


    private static boolean simulatedMoveCheckAssessment(){
        // This function assesses if there's a check situation still given after the simulated move has been conducted.

        Map<AbstractPiece, List<Location>> possibleEnemySimulationPieceMoves = new HashMap<>();
        if (turnOfColor.equals(PieceColor.DARK)){
            possibleEnemySimulationPieceMoves = possibleColorPieceMoves(board, PieceColor.LIGHT);
        }
        else{
            possibleEnemySimulationPieceMoves = possibleColorPieceMoves(board, PieceColor.DARK);
        }

        boolean isSimulatedCheck = false;
        List<Location> tempList = new ArrayList<>();

        for (AbstractPiece pieceCandidate : possibleEnemySimulationPieceMoves.keySet()){
            tempList = possibleEnemySimulationPieceMoves.get(pieceCandidate);

            if (!tempList.isEmpty()){
                if (tempList.contains(king.getCurrentSquare().getLocation())){
                    isSimulatedCheck = true;
                }
            }
        }

        return isSimulatedCheck;
    } 
}