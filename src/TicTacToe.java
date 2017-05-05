import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.List;

/**
 * TicTacToe class is the place where main function resides
 * It extends javaFX Application
 * It mainly contains two part, one is the UI code for the board, another is hwo the game actually runs by calling other classes
 *
 * Created by BoogieJay
 * 4/23/17.
 */
public class TicTacToe extends Application{

    private static final String GAME_NAME = "Tic Tac Toe";
    private static final int TILE_SIZE = 150;
    private static final int UI_BOARD_SIZE = 600;


    private final Board board;
    private final Tile[][] UIBoard;
    private final AI ai;
    private int[] moves;
    private GameState gameState;
    private Seed player;
    private Stage window;

    /**
     * constructor of TicTacToe
     */
    public TicTacToe() {
        this.board = new Board();
        this.UIBoard = new Tile[board.getSIZE()][board.getSIZE()];
        this.gameState = GameState.PLAY;
        this.player = Seed.NOUGHT;
        ai = new AI(this.board);
    }

    /**
     * Tile class represent the square in the UIboard
     * it extends the StackPane from javaFX
     */
    private class Tile extends StackPane {

        private final int row;
        private final int col;
        private Text text;
        private final Rectangle border;

        public Tile(int row, int col) {
            this.row = row;
            this.col = col;
            this.text = new Text("");
            this.border = new Rectangle(TILE_SIZE, TILE_SIZE);

            border.setFill(null);
            border.setStroke(Color.BLACK);
            text.setFont(Font.font(72));
            setAlignment(Pos.CENTER);
            getChildren().addAll(border, text);

            // one the mouse click
            setOnMouseClicked(event -> {

                // if the square is not occupied by 'X' or 'O'
                if (getValue() == "") {

                    startHumanMoveAndDrawUI(this);
                    checkWin();

                    startRobotMoveAndDrawUI();
                    checkWin();

                    player = player == Seed.CROSS ? Seed.NOUGHT : Seed.CROSS;
                }
            });
        }

        /**
         * get the current content
         * @return
         */
        public String getValue() {
            return text.getText();
        }

        /**
         * draw 'X' on the current tile
         */
        private void drawX() {
            text.setText("X");
        }

        /**
         * draw 'O' on the current tile
         */
        private void drawO() {
            text.setText("O");
        }

        /**
         * ge the row position of current Tile
         * @return row
         */
        private int getRow() {
            return this.row;
        }

        /**
         * get the column position of current tile
         * @return column
         */
        private int getCol() {
            return this.col;
        }

        /**
         * highlight the border once the tile belongs to the winning combo
         */
        private void highLight() {
            border.setFill(Color.LEMONCHIFFON);
        }
    }

    /**
     * check if 'O' or 'X' wins the game
     */
    private void checkWin() {

        checkBoard();
        String message = "";

        if (gameState != GameState.PLAY) {
            if (gameState == GameState.NOUGHT_WIN) {
                message = "O wins!";
                drawCombo(board.checkConditionAndReturnCombo(Seed.NOUGHT));
            } else if (gameState == GameState.CROSS_WIN) {
                message = "X wins!";
                drawCombo(board.checkConditionAndReturnCombo(Seed.CROSS));
            } else {
                message = "Tie!";
            }

            System.out.println("******************************************");
            System.out.println("Game Over");
            System.out.println(message);

            // display the result window
            AlertBox.display(GAME_NAME, message);

            // close the boardUI
            window.close();

            // terminate the program
            System.exit(0);
        }
    }

    /**
     * draw the combo by highlighting the combo border
     * @param combo
     */
    private void drawCombo(List<int[]> combo) {
        for (int[] position: combo) {
            int row = position[0];
            int col = position[1];
            Tile tile = UIBoard[row][col];
            tile.highLight();
        }
    }

    /**
     * draw the seed on UIboard and also set the seed on the board
     * @param tile
     */
    private void startHumanMoveAndDrawUI(Tile tile) {
        tile.drawO();
        moves = new int[]{tile.getRow(), tile.getCol()};
        board.setCell(moves[0], moves[1], Seed.NOUGHT);
    }

    /**
     * get the moves from AI and also draw 'X' on the UIBoard
     */
    private void startRobotMoveAndDrawUI() {
        moves = ai.robotMove();
        UIBoard[moves[0]][moves[1]].drawX();
    }


    /**
     * override start method from javaFX
     * create the scene for UIboard
     * get the choice from user by calling welcome.display()
     *
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {

        window = new Stage();
        window.setTitle(GAME_NAME);
        Scene scene = new Scene(createContent());
        window.setScene(scene);

        // userChoices[0] represents game level, userChoices[1] represents the starting person
        int[] userChoices = WelcomePage.display();
        ai.setLevel(userChoices[0]);
        setPlayer(userChoices[1]);
        System.out.println("Game start");

        // if user want to AI go first
        if(player == Seed.CROSS) {
            startRobotMoveAndDrawUI();
        }

        window.show();

    }


    /**
     * create 4 * 4 tiles on the UIboard
     *
     * @return root
     */
    private Parent createContent() {
        Pane root = new Pane();
        root.setPrefSize(UI_BOARD_SIZE, UI_BOARD_SIZE);

        for (int i = 0; i < board.getSIZE(); i++) {
            for (int j = 0; j < board.getSIZE(); j++) {
                Tile tile = new Tile(i, j);

                // shift the tile to its desired position
                tile.setTranslateX(j * TILE_SIZE);
                tile.setTranslateY(i * TILE_SIZE);

                // add tile to the root
                root.getChildren().add(tile);

                // add tile to the UIBoard
                UIBoard[i][j] = tile;
            }
        }

        return root;
    }

    /**
     * check if game terminated
     */
    private void checkBoard() {
        if (board.hasWin(Seed.CROSS)) {
            gameState = GameState.CROSS_WIN;
        } else if (board.hasWin(Seed.NOUGHT)) {
            gameState = GameState.NOUGHT_WIN;
        } else if (board.isTie()){
            gameState = GameState.TIE;
        }
    }

    /**
     * set player to 'X' or 'O'
     * @param player
     */
    private void setPlayer(int player) {
        this.player = player == 1 ? Seed.NOUGHT : Seed.CROSS;
    }


    public static void main(String[] args) {
        launch(args);
    }

}
