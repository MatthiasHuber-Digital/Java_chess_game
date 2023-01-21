package com.chess.gui;

import com.chess.board.*;
import com.chess.squares.*;
import com.chess.piece.*;
import com.chess.runner.*;

import javax.lang.model.util.ElementScanner6;
import javax.imageio.*;
import javax.swing.*;

//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
import java.awt.event.*;
import java.io.IOException;
import java.io.File;
import java.util.ArrayList;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import javax.swing.SwingUtilities.*;

public class Table {
    // This class is for the MAIN WINDOW and MENU CREATION

    // GUI Tutorial:
    // video1 https://www.youtube.com/watch?v=w9HR4VJ8DAw
    // video2 https://www.youtube.com/watch?v=7lXFtjM0c8g
    // video3 https://www.youtube.com/watch?v=AsNcp1VmG1U
    // video4 https://www.youtube.com/watch?v=aHFiRhGnvKE
    // video5 https://www.youtube.com/watch?v=peU0xIhkBws


    private final JFrame gameFrame; // visualization background, i.e. window
    private final BoardPanel boardPanel; // visual component for the board
    private final Board chessboard;

    private static Square sourceSquare;
    private static Square destinationSquare;
    private static TilePanel sourceTile;
    private AbstractPiece humanMovedPiece;

    private static final Dimension OUTER_FRAME_DIMENSION = new Dimension(800,800); // REAL size of outside window - checkerboard adapts
    private static final Dimension BOARD_PANEL_DIMENSION = new Dimension(400,350); // PREFERRED size of checkerboard
    private static final Dimension TILE_PANEL_DIMENSION = new Dimension(10,10);
    private static final int NUM_TILES = 64;
    private static final String PIECE_IMAGES_PATH = new java.io.File("").getAbsolutePath() + "\\com\\chess\\gui\\img\\";
    // sand-color:
    private static final Color ACTIVE_TILE_COLOR = new Color(240,230,140);
    // white:
    private static final Color LIGHT_TILE_COLOR = new Color(255,255,255);
    //Color darkColor = new Color(210,105,30);
    // brown:
    //private static final Color DARK_TILE_COLOR = new Color(160,82,45); 
    // mint-green:
    private static final Color DARK_TILE_COLOR = new Color(153,255,153);

    public Table(Board board){
        // Window object creation
        this.chessboard = board;
        this.gameFrame = new JFrame("JAVA-Chess by Matthias Huber");
        final JMenuBar tableMenuBar = createTableMenuBar(); // create menu bar

        this.gameFrame.setJMenuBar(tableMenuBar); // associate graphics with backend?
        this.gameFrame.setSize(OUTER_FRAME_DIMENSION);

        this.boardPanel = new BoardPanel();
        this.gameFrame.add(this.boardPanel, BorderLayout.CENTER);

        this.gameFrame.setVisible(true);
    }

    private JMenuBar createTableMenuBar(){
        final JMenuBar tableMenuBar = new JMenuBar();
        tableMenuBar.add(createFileMenu()); // add file menu to menu bar
        return tableMenuBar;
    }

    private JMenu createFileMenu(){
        final JMenu fileMenu = new JMenu("File");

        // PGN format is a standard format
        // This creates a menu entry:
        final JMenuItem openPGN = new JMenuItem("Load PGN file");


        /* // Listener waits for your click and triggers the ActionEvent subsequently
        openPGN.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                System.out.println("open PGN file");
            }
        });
        fileMenu.add(openPGN); */

        // Start a new game and throw away the current one
        final JMenuItem newGameItem = new JMenuItem("New game");
        newGameItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                System.out.println("Starting new game...");
                chessboard.resetBoard();
            }
        });
        fileMenu.add(newGameItem);

        // Exit the chess application
        final JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                System.out.println("Exit program");
                System.exit(0);
            }
        });
        fileMenu.add(exitMenuItem);

        return fileMenu;
    }

    private class BoardPanel extends JPanel{
        // This is the real checkerboard area

        final List<TilePanel> boardTiles;

        BoardPanel() {
            super(new GridLayout(8,8)); // creates a grid
            this.boardTiles = new ArrayList<>();

            for (int i = 0; i < NUM_TILES; i++){
                final TilePanel tilePanel = new TilePanel(this, i);
                this.boardTiles.add(tilePanel);
                add(tilePanel);
            }

            setSize(BOARD_PANEL_DIMENSION);
            validate();
        }
    
        public void drawBoard(final Board board){
            removeAll();
            for (final TilePanel tilePanel : boardTiles){
                tilePanel.drawTile(board);
                add(tilePanel);
            }
            validate();
            repaint();
        }
    }


    private class TilePanel extends JPanel implements MouseListener{
        // This is the cell/tile graphical area
        private final int tileId;
        private Color inactiveTileColor;

        TilePanel (final BoardPanel boardPanel, final int tileId){
            super(new GridBagLayout());
            this.tileId = tileId;
            setPreferredSize(TILE_PANEL_DIMENSION);
            assignTileColor();
            assignTilePieceIcon(chessboard);
            addMouseListener(this);
            

        }
        
        @Override
        public void mouseClicked(MouseEvent e){
            System.out.println("Mouse clicked");
            // only kick-off the code in case there is no move currently being processed by the backend.
            if (!Game.getProcessingMove()){

                if (javax.swing.SwingUtilities.isRightMouseButton(e)){
                    clearMouseListenerFields();

                }
                else if (javax.swing.SwingUtilities.isLeftMouseButton(e)) {
                    // on right-click, ANY PIECE SELECTIONS ARE CANCELLED.
                    if (sourceSquare == null){

                        System.out.println("Mouse SOURCE square initially NULL");
                        // in the past, no square has been selected
                        sourceTile = this;
                        sourceSquare = chessboard.getSquareFromTileId(tileId);
                        System.out.println("Mouse SOURCE changed: " + sourceSquare.getLocation().getFile().toString() + sourceSquare.getLocation().getRank().toString());
                        humanMovedPiece = sourceSquare.getCurrentPiece();
                        //if (humanMovedPiece == null || humanMovedPiece.getPieceColor().equals(Game.getTurnColor())){
                        if (humanMovedPiece == null){
                            // in case there is no piece on the current square
                            sourceSquare = null;
                            sourceTile = null;
                            System.out.println("Resetting source square!!!");
                        }
                        else{
                            //setActiveTileColor();
                            System.out.println("Source piece: " + sourceSquare.getCurrentPiece().getName());    
                        }
                    }
                    else{
                        System.out.println("Mouse DESTINATION");
                        // once a source square has been already selected
                        destinationSquare = chessboard.getSquareFromTileId(tileId);
                        System.out.println("Mouse source square: " + sourceSquare.getLocation().getFile() + sourceSquare.getLocation().getRank().toString());
                        System.out.println("Mouse DESTINATION square: " + destinationSquare.getLocation().getFile() + destinationSquare.getLocation().getRank().toString());
                        if (!Game.getProcessingMove()){
                            // MAKE THE MOVE HERE
                            Game.setUserMoveCoordsFromMouse(sourceSquare, destinationSquare);
                            Game.gameTurnHandling();
                            // UPDATE chessboard
                            boardPanel.drawBoard(chessboard);
                        }
                        // clear mouse listener fields
                        clearMouseListenerFields();
                        //setInactiveTileColor();
                        //System.out.println("Source/destination after clearing: " + sourceSquare.getLocation().getFile().toString() + sourceSquare.getLocation().getRank().toString() + "->" + 
                        //                    destinationSquare.getLocation().getFile().toString() + destinationSquare.getLocation().getRank().toString());
                    }
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run(){
                            boardPanel.drawBoard(chessboard);
                        }
                    });
                }
            }
        }

        @Override
        public void mousePressed(MouseEvent e){
            
        }

        @Override
        public void mouseReleased(MouseEvent e){
        }

        @Override
        public void mouseExited(MouseEvent e){
            
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            // TODO Auto-generated method stub
            
        }

        private void clearMouseListenerFields(){
            sourceSquare = null;
            destinationSquare = null;
            humanMovedPiece = null;
            sourceTile = null;
        }

        public void drawTile(final Board board){
            assignTileColor();
            assignTilePieceIcon(board);
            validate();
            repaint();
        }

        private void assignTilePieceIcon(final Board board){
            this.removeAll(); // clear tile
            Square currentSquare = board.getSquareFromTileId(this.tileId);
            if (currentSquare.getIsOccupied()){
                try{
                    // access a piece icon data file e.g. like this: C:/<somedir>/LIGHT_bishop.gif
                    String imgPath = PIECE_IMAGES_PATH + currentSquare.getCurrentPiece().getPieceColor().toString() + "_" + currentSquare.getCurrentPiece().getName() + ".gif";
                    final BufferedImage image = ImageIO.read(new java.io.File(imgPath));
                    add(new JLabel(new ImageIcon(image)));
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void assignTileColor(){
            if (this.equals(sourceTile)){
                setActiveTileColor();
            }
            else{
                if ((tileId>=8) && (tileId<=15) ||
                    (tileId>=24) && (tileId<=31) ||
                    (tileId>=40) && (tileId<=47) ||
                    (tileId>=56) && (tileId<=63)
                ){
                    setBackground(this.tileId % 2 == 0 ? DARK_TILE_COLOR : LIGHT_TILE_COLOR);
                } else {
                    setBackground(this.tileId % 2 != 0 ? DARK_TILE_COLOR : LIGHT_TILE_COLOR);
                }
                this.inactiveTileColor = this.getBackground();
            }
        }

        private void setActiveTileColor(){
            sourceTile.setBackground(ACTIVE_TILE_COLOR);
        }

        private void setInactiveTileColor(){
            sourceTile.setBackground(sourceTile.inactiveTileColor);
        }
    }
}
