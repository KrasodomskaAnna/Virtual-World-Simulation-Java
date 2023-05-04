package pl.edu.pg.eti.po.project.worldsimulator.graphics;

import pl.edu.pg.eti.po.project.worldsimulator.Game;
import pl.edu.pg.eti.po.project.worldsimulator.organisms.Organism;
import pl.edu.pg.eti.po.project.worldsimulator.organisms.animals.*;
import pl.edu.pg.eti.po.project.worldsimulator.organisms.plants.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class Graphics {
    private final JFrame mainFrame;
    private ArrayList<ArrayList<JButton>> board;
    private final Game game;
    private int boardWidth = 0;
    private int boardHeight = 0;
    private int gameWidth;
    private int gameHeight;

    private static final int MENU_WIDTH = 600;
    private static final int MENU_HEIGHT = 400;

    private static final Color boardColor = Color.LIGHT_GRAY;
    private static final Color borderColor = Color.GRAY;
    private final Rectangle fieldSize = new Rectangle(20, 20);

    private final JLabel labelTurn = new JLabel();
    private final JLabel labelWarnings = new JLabel();

    public Graphics(Game game){
        this.game = game;
        this.mainFrame = new JFrame("Animal World - Anna Krasodomska - 188863");
        this.mainFrame.setSize(MENU_WIDTH, MENU_HEIGHT);
        this.mainFrame.setLayout(null);
        this.mainFrame.setResizable(false);

        this.board = new ArrayList<>();

        this.addMenu();

        this.addKeyListener();

        this.mainFrame.setVisible(true);
    }

    public final void setBoardDimensions(int _width, int _height) {
        this.boardWidth = _width;
        this.boardHeight = _height;
        this.mainFrame.getContentPane().removeAll();
        this.gameWidth = this.boardWidth*fieldSize.width+15;
        this.gameHeight = this.boardHeight*fieldSize.height+160;
        this.mainFrame.setSize(gameWidth, gameHeight);


        this.board = new ArrayList<>();

        this.populateBoard();
        this.addButtons();
        this.addComments();
    }
    public final void endGame(String report) {
        this.mainFrame.getContentPane().removeAll();
        this.mainFrame.setSize(MENU_WIDTH, MENU_HEIGHT);
        addGameImage(10, 50 , 565, 300);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel label = new JLabel();
        label.setText(report);
        label.setFont(new Font("Helvetica", Font.BOLD, 32));
        label.setBounds(0, 0, 400, 50);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(label);

        panel.setBounds(0, 0, 400, 50);
        this.mainFrame.add(panel);
    }
    public final void updateComments() {
        labelTurn.setText("Turns: "+game.getTurnsNumber());
        labelWarnings.setText(game.getWarnings());
    }
    public final void clearBoard() {
        this.board.parallelStream().forEach(row -> row.parallelStream().forEach(btn -> btn.setBackground(boardColor)));
    }
    public final void setColor(int x, int y, Color color) {
        this.board.get(y).get(x).setBackground(color);
    }

    private void addComments() {
        labelTurn.setText(String.valueOf(game.getTurnsNumber()));
        labelTurn.setBounds(0, 0, 100, 30);
        labelTurn.setAlignmentX(Component.CENTER_ALIGNMENT);

        labelWarnings.setText(game.getWarnings());
        labelWarnings.setBounds(0, 0, 300, 30);
        labelWarnings.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel commentsPanel = new JPanel();
        commentsPanel.setLayout(new BoxLayout(commentsPanel, BoxLayout.Y_AXIS));
        commentsPanel.add(labelTurn);
        commentsPanel.add(labelWarnings);
        commentsPanel.setBounds(this.mainFrame.getWidth()/2-200, this.boardHeight*fieldSize.height+65, 400, 60);
        this.mainFrame.add(commentsPanel);
    }
    private void returnToGame() {
        this.mainFrame.getContentPane().removeAll();
        this.mainFrame.setSize(gameWidth, gameHeight);
        var panel = new JPanel(new GridLayout(boardHeight, boardWidth, 0, 0));
        for (int row = 0; row < boardHeight; row++) {
            this.board.add(new ArrayList<>());
            for (int column = 0; column < boardWidth; column++) {
                var button = board.get(row).get(column);
                panel.add(button);
                this.board.get(row).add(button);
            }
        }
        panel.setBounds(0, 0, this.boardWidth*fieldSize.width, this.boardHeight*fieldSize.height);
        this.mainFrame.add(panel);
        addButtons();
        addComments();
    }
    private void addMessagePanel() {
        this.mainFrame.getContentPane().removeAll();
        this.mainFrame.setSize(MENU_WIDTH, MENU_HEIGHT);

        var save = new JButton("Save game");
        save.setBounds(10, MENU_HEIGHT-80, 100, 40);
        save.addActionListener(e -> {
            game.saveGame();
        });
        this.mainFrame.add(save);

        var ret = new JButton("Return");
        ret.setBounds(MENU_WIDTH-120, MENU_HEIGHT-80, 100, 40);
        ret.addActionListener(e -> {
            returnToGame();
        });
        this.mainFrame.add(ret);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        game.getMessages().forEach(message -> {
            JLabel label = new JLabel();
            label.setText(message);
            label.setBounds(0, 0, 100, 30);
            label.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(label);
        });
        panel.setBounds(0, 10, MENU_WIDTH, MENU_HEIGHT-100);
        this.mainFrame.add(panel);
    }
    private void addMenu() {
        var load = new JButton("Load save");
        load.setBounds(MENU_WIDTH/2-100, MENU_HEIGHT-85, 100, 40);
        load.addActionListener(e -> {
            game.loadSave();
        });
        this.mainFrame.add(load);

        var conf = new JButton("Load config");
        conf.setBounds(MENU_WIDTH/2, MENU_HEIGHT-85, 100, 40);
        conf.addActionListener(e -> {
            game.loadConfig();
        });
        this.mainFrame.add(conf);

        addGameImage(10, 10 , 565, 300);
    }
    private void addOrganismPanel(Point2D position) {
        JFrame frame = new JFrame("Choose Organism");
        frame.setSize(200, 300);
        frame.setLayout(null);
        frame.setResizable(false);
        JPanel panel = new JPanel();
        //panel.setLayout(null);
        panel.setBounds(0,0,200,300);

        //AtomicInteger i= new AtomicInteger();
        Consumer<Organism> addNewButton = (Organism orgType) -> {
            var button = new JButton(orgType.getOrganismType());
            //button.setBounds(0, i.get() * 26,200,26);
            button.setBounds(0, 0 * 26,200,26);
            button.addActionListener(e -> {
                game.organisms.add(orgType);
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
                setColor((int)position.getX(), (int)position.getY(), orgType.getColor());
            });
            panel.add(button);
            //i.getAndIncrement();
        };
        addNewButton.accept(new Antelope(position, game));
        addNewButton.accept(new Fox(position, game));
        addNewButton.accept(new Lamp(position, game));
        addNewButton.accept(new Turtle(position, game));
        addNewButton.accept(new Wolf(position, game));

        addNewButton.accept(new Dandelion(position, game));
        addNewButton.accept(new Grass(position, game));
        addNewButton.accept(new Guarana(position, game));
        addNewButton.accept(new Nightshade(position, game));
        addNewButton.accept(new Sosnowsky_hogweed(position, game));

        frame.add(panel);
        frame.setVisible(true);
    }

    private void addGameImage(int x, int y, int width, int height) {
        try {
            String imagePath = "C:\\Users\\Ewa\\Desktop\\semestr 2\\PO\\projekty\\java\\src\\pl\\edu\\pg\\eti\\po\\project\\worldsimulator\\graphics\\pexels-pixabay-247431.jpg";
            BufferedImage image = ImageIO.read(new File(imagePath));
            JLabel picLabel = new JLabel(new ImageIcon(image));

            JPanel panel = new JPanel();
            panel.setBounds(x, y, width, height);
            panel.add(picLabel);

            this.mainFrame.add(panel);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void addButtons() {
        int x = this.boardWidth*fieldSize.width/6;
        int currentX = 0;
        var save = new JButton("Save game");
        save.setBounds(currentX, this.boardHeight*fieldSize.height+20, x, 40);
        save.addActionListener(e -> {
            game.saveGame();
        });
        this.mainFrame.add(save);
        currentX += x;

        var load = new JButton("Load save");
        load.setBounds(currentX, this.boardHeight*fieldSize.height+20, x, 40);
        load.addActionListener(e -> {
            game.loadSave();
        });
        this.mainFrame.add(load);
        currentX += x;

        var conf = new JButton("Load config");
        conf.setBounds(currentX, this.boardHeight*fieldSize.height+20, x, 40);
        conf.addActionListener(e -> {
            game.loadConfig();
        });
        this.mainFrame.add(conf);
        currentX += x;

        var azur = new JButton("Azur Shield");
        azur.setBounds(currentX, this.boardHeight*fieldSize.height+20, x, 40);
        azur.addActionListener(e -> {
            if(game.activeAzurShield()) return;
            game.addWarning("You cannot activate alzurShield!\n");
            labelWarnings.setText(game.getWarnings());
        });
        this.mainFrame.add(azur);
        currentX += x;

        var turn = new JButton("Next Turn");
        turn.setBounds(currentX, this.boardHeight*fieldSize.height+20, x, 40);
        turn.addActionListener(e -> {
            game.nextTurn();
        });
        this.mainFrame.add(turn);
        currentX += x;

        var mess = new JButton("Messages");
        mess.setBounds(currentX, this.boardHeight*fieldSize.height+20, 100, 40);
        mess.addActionListener(e -> {
            addMessagePanel();
        });
        this.mainFrame.add(mess);
    }
    private void populateBoard() {
        var panel = new JPanel(new GridLayout(boardHeight, boardWidth, 0, 0));
        for (int row = 0; row < boardHeight; row++) {
            this.board.add(new ArrayList<>());
            for (int column = 0; column < boardWidth; column++) {
                var newButton = new JButton();
                newButton.setBounds(fieldSize);
                newButton.setBorder(new LineBorder(borderColor, 1));
                newButton.setBackground(boardColor);
                int finalColumn = column;
                int finalRow = row;
                newButton.addActionListener(e -> {
                    Point2D position = new Point2D.Double(finalColumn, finalRow);
                    for(var organism : game.organisms) {
                        if(position.equals(organism.getPosition())) {
                            JOptionPane.showMessageDialog(null, "This field is already occupied");
                            return;
                        };
                    }
                    addOrganismPanel(position);
                });
                panel.add(newButton);
                this.board.get(row).add(newButton);
            }
        }
        panel.setBounds(0, 0, this.boardWidth*fieldSize.width, this.boardHeight*fieldSize.height);
        this.mainFrame.add(panel);
    }

    private void addKeyListener() {
        var rootPane = this.mainFrame.getRootPane();
        rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0),"handleUp");
        rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0),"handleDown");
        rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0),"handleRight");
        rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0),"handleLeft");
        rootPane.getActionMap().put("handleUp", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                game.setInput("UP");
                game.nextTurn();
            }
        });
        rootPane.getActionMap().put("handleDown", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                game.setInput("DOWN");
                game.nextTurn();
            }
        });
        rootPane.getActionMap().put("handleRight", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                game.setInput("RIGHT");
                game.nextTurn();
            }
        });
        rootPane.getActionMap().put("handleLeft", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                game.setInput("LEFT");
                game.nextTurn();
            }
        });
    }
}