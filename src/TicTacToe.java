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
import java.util.Scanner;

/**
 * Created by BoogieJay
 * 4/23/17.
 */
public class TicTacToe extends Application{

    private final Board board;
    private final Tile[][] uiBoard;
    private final Scanner in;
    private final AI ai;
    private final AI ai2;
    private int[] moves;
    private GameState gameState;
    private Seed player;
    private Stage window;

    public TicTacToe() {
        this.board = new Board();
        this.uiBoard = new Tile[board.getSIZE()][board.getSIZE()];
        this.gameState = GameState.PLAY;
        this.in = new Scanner(System.in);
        this.player = Seed.NOUGHT;
        ai = new AI(this.board, Seed.CROSS);
        ai2 = new AI(this.board, Seed.NOUGHT);
    }

    private class Tile extends StackPane {
        private Text text;
        private int row;
        private int col;

        public Tile(int row, int col) {

            this.row = row;
            this.col = col;
            this.text = new Text("");

            Rectangle border = new Rectangle(150, 150);
            border.setFill(null);
            border.setStroke(Color.BLACK);


            text.setFont(Font.font(72));

            setAlignment(Pos.CENTER);
            getChildren().addAll(border, text);

            setOnMouseClicked(event -> {
                if (getValue() == "") {
                    drawO();
                    moves = new int[]{this.row, this.col};
                    board.setCell(moves[0], moves[1], Seed.NOUGHT);
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
    }

    private void checkWin() {
        checkBorad();
        String gameName = "Tic Tac Toe";
        String message;
        if (gameState != GameState.PLAY) {
            if (gameState == GameState.NOUGHT_WIN) {
                message = "O wins!";
            } else if (gameState == GameState.CROSS_WIN) {
                message = "X wins!";
            } else {
                message = "Tie!";
            }
            AlertBox.display(gameName, message);
            window.close();
            System.exit(0);
        }
    }

    private void startRobotMoveAndDrawUI() {
        moves = ai.robotMove();
        drawUI(moves[0], moves[1], Seed.CROSS);
    }

    private void drawUI(int row, int col, Seed seed) {
        uiBoard[row][col].drawX();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        window = new Stage();
        window.setTitle("Tic Tac Toe");
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
        root.setPrefSize(600, 600);

        for (int i = 0; i < board.getSIZE(); i++) {
            for (int j = 0; j < board.getSIZE(); j++) {
                Tile tile = new Tile(i, j);
                tile.setTranslateX(j * 150);
                tile.setTranslateY(i * 150);

                root.getChildren().add(tile);

                uiBoard[i][j] = tile;
            }
        }

        return root;
    }

    public void play () {

        launch();

    }


    public String playRobotCompetition (int xLevel, int oLevel, int OGoFirst) {

        ai.setLevel(xLevel);
        ai2.setLevel(oLevel);

        setPlayer(OGoFirst);

        while (gameState == GameState.PLAY) {
            if (player == Seed.NOUGHT) {
                ai2.robotMove();
            } else {
                ai.robotMove();
            }
            board.paint();
            System.out.println("******************************************");
            checkBorad();
            player = player == Seed.CROSS ? Seed.NOUGHT : Seed.CROSS;
        }

        GameState rstState = gameState;
        board.clear();
        gameState = GameState.PLAY;

        if (rstState == GameState.CROSS_WIN) {
            System.out.println(" X wins the game!");
            return "X";
        } else if (rstState == GameState.NOUGHT_WIN) {
            System.out.println(" O wins the game!");
            return "O";
        } else {
            System.out.println(" It's a tie!");
            return "T";
        }

    }


    private void checkBorad() {
        if (board.hasWin(Seed.CROSS)) {
            gameState = GameState.CROSS_WIN;
        } else if (board.hasWin(Seed.NOUGHT)) {
            gameState = GameState.NOUGHT_WIN;
        } else if (board.isTie()){
            gameState = GameState.TIE;
        }
    }

    private void playerMove() {

        boolean validInput = false;

        while (!validInput) {

            System.out.println("Player " + player + " ," + "select row[1 - 4] and column[1 - 4]");

            int row = in.nextInt() - 1;
            int col = in.nextInt() - 1;

            if (row >= 0 && row < board.getSIZE() && col >= 0 && col < board.getSIZE()) {

                if (board.getCell(row, col) == Seed.EMPTY) {
                    board.setCell(row, col, player);
                    validInput = true;
                } else {
                    System.out.println("Sorry, you can only choose empty slot");
                }
            } else {
                System.out.println("Sorry, your input is not valid");
            }

        }
    }

    private void setPlayer(int player) {
        this.player = player == 1 ? Seed.NOUGHT : Seed.CROSS;
    }


}
