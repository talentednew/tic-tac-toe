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
 * Created by BoogieJay
 * 4/23/17.
 */
public class TicTacToe extends Application{

    private static final String GAME_NAME = "Tic Tac Toe";
    private static final int TILE_SIZE = 150;
    private static final int UI_BOARD_SIZE = 600;


    private final Board board;
    private final Tile[][] uiBoard;
    private final AI ai;
    private int[] moves;
    private GameState gameState;
    private Seed player;
    private Stage window;

    public TicTacToe() {
        this.board = new Board();
        this.uiBoard = new Tile[board.getSIZE()][board.getSIZE()];
        this.gameState = GameState.PLAY;
        this.player = Seed.NOUGHT;
        ai = new AI(this.board);
    }

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

            setOnMouseClicked(event -> {
                if (getValue() == "") {

                    startHumanMoveAndDrawUI(this);
                    checkWin();

                    startRobotMoveAndDrawUI();
                    checkWin();

                    player = player == Seed.CROSS ? Seed.NOUGHT : Seed.CROSS;
                }
            });
        }

        public String getValue() {
            return text.getText();
        }

        private void drawX() {
            text.setText("X");
        }

        private void drawO() {
            text.setText("O");
        }

        private int getRow() {
            return this.row;
        }

        private int getCol() {
            return this.col;
        }

        private void highLight() {
            border.setFill(Color.LEMONCHIFFON);
        }
    }

    private void checkWin() {

        checkBoard();
        String gameName = GAME_NAME;
        String message;

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

            AlertBox.display(gameName, message);
            window.close();
            System.exit(0);
        }
    }

    private void drawCombo(List<int[]> combo) {
        for(int i = 0; i < combo.size(); i++) {
            int[] position = combo.get(i);
            int row = position[0];
            int col = position[1];
            Tile tile = uiBoard[row][col];
            tile.highLight();
        }
    }

    private void startHumanMoveAndDrawUI(Tile tile) {
        tile.drawO();
        moves = new int[]{tile.getRow(), tile.getCol()};
        board.setCell(moves[0], moves[1], Seed.NOUGHT);
    }

    private void startRobotMoveAndDrawUI() {
        moves = ai.robotMove();
        uiBoard[moves[0]][moves[1]].drawX();
    }


    @Override
    public void start(Stage primaryStage) throws Exception {

        window = new Stage();
        window.setTitle(GAME_NAME);
        Scene scene = new Scene(createContent());
        window.setScene(scene);

        int[] userChoices = WelcomePage.display();
        ai.setLevel(userChoices[0]);
        setPlayer(userChoices[1]);
        System.out.println("Game start");

        if(player == Seed.CROSS) {
            startRobotMoveAndDrawUI();
        }

        window.show();

    }


    private Parent createContent() {
        Pane root = new Pane();
        root.setPrefSize(UI_BOARD_SIZE, UI_BOARD_SIZE);

        for (int i = 0; i < board.getSIZE(); i++) {
            for (int j = 0; j < board.getSIZE(); j++) {
                Tile tile = new Tile(i, j);
                tile.setTranslateX(j * TILE_SIZE);
                tile.setTranslateY(i * TILE_SIZE);
                root.getChildren().add(tile);
                uiBoard[i][j] = tile;
            }
        }

        return root;
    }

    private void checkBoard() {
        if (board.hasWin(Seed.CROSS)) {
            gameState = GameState.CROSS_WIN;
        } else if (board.hasWin(Seed.NOUGHT)) {
            gameState = GameState.NOUGHT_WIN;
        } else if (board.isTie()){
            gameState = GameState.TIE;
        }
    }

    private void setPlayer(int player) {
        this.player = player == 1 ? Seed.NOUGHT : Seed.CROSS;
    }


    public static void main(String[] args) {
        launch(args);
    }

}
