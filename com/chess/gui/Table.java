package com.chess.gui;

import com.chess.board.*;
import com.chess.squares.*;

import javax.lang.model.util.ElementScanner6;
import javax.imageio.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.File;
import java.util.ArrayList;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

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

    private static final Dimension OUTER_FRAME_DIMENSION = new Dimension(600,600);
    private static final Dimension BOARD_PANEL_DIMENSION = new Dimension(400,350);
    private static final Dimension TILE_PANEL_DIMENSION = new Dimension(10,10);
    private static final int NUM_TILES = 64;
    private static final String PIECE_IMAGES_PATH = new java.io.File("").getAbsolutePath() + "\\com\\chess\\gui\\img\\";
    // sand-color:
    //private static final Color LIGHT_TILE_COLOR = new Color(240,230,140);
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
        // Listener waits for your click and triggers the ActionEvent subsequently
        openPGN.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                System.out.println("open PGN file");
            }
        });
        fileMenu.add(openPGN);

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
        // This is the board graphical area

        final List<TilePanel> boardTiles;

        BoardPanel() {
            super(new GridLayout(8,8)); // creates a grid
            this.boardTiles = new ArrayList<>();

            for (int i = 0; i < NUM_TILES; i++){
                final TilePanel tilePanel = new TilePanel(this, i);
                this.boardTiles.add(tilePanel);
                add(tilePanel);
            }

            setPreferredSize(BOARD_PANEL_DIMENSION);
            validate();
        }
    }


    private class TilePanel extends JPanel{
        // This is the cell/tile graphical area
        private final int tileId;

        TilePanel (final BoardPanel boardPanel, final int tileId){
            super(new GridBagLayout());
            this.tileId = tileId;
            setPreferredSize(TILE_PANEL_DIMENSION);
            assignTileColor();
            assignTilePieceIcon(chessboard);
            validate();
        }

        
        private void assignTilePieceIcon(final Board board){
            this.removeAll(); // clear tile
            Square currentSquare = board.getSquareFromTileId(this.tileId);
            if (currentSquare.getIsOccupied()){
                try{
                    // access a piece icon data file e.g. like this: C:/<somedir>/LIGHT_bishop.gif
                    String imgPath = PIECE_IMAGES_PATH + currentSquare.getCurrentPiece().getPieceColor().toString() + "_" + currentSquare.getCurrentPiece().getName() + ".gif";
                    System.out.println("IMAGE PATH: " + imgPath);
                    final BufferedImage image = ImageIO.read(new java.io.File(imgPath));
                    add(new JLabel(new ImageIcon(image)));
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        private void assignTileColor(){

            if ((tileId>=8) && (tileId<=15) ||
                (tileId>=24) && (tileId<=31) ||
                (tileId>=40) && (tileId<=47) ||
                (tileId>=56) && (tileId<=63)
            ){
                setBackground(this.tileId % 2 == 0 ? LIGHT_TILE_COLOR : DARK_TILE_COLOR);
            } else {
                setBackground(this.tileId % 2 != 0 ? LIGHT_TILE_COLOR : DARK_TILE_COLOR);
            }
        }
    }
}
