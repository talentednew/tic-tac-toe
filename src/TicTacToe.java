import java.util.Scanner;

/**
 * Created by BoogieJay
 * 4/23/17.
 */
public class TicTacToe {

    private final Board borad;
    private GameState gameState;
    private Seed player;
    private final Scanner in;
    private final AI ai;

    public TicTacToe() {
        this.borad = new Board();
        this.gameState = GameState.PLAY;
        this.in = new Scanner(System.in);
        this.player = Seed.NOUGHT;
        ai = new AI(this.borad);
    }

    public void play () {

        System.out.println("Welcome to TicTacToe");

        System.out.println("Please select AI level: 1 - easy, 2 - intermediate, 3 - difficult.");
        ai.setLevel(in.nextInt());

        System.out.println("If you want to go first, enter 1, else enter 0");
        setPlayer(in.nextInt());

        System.out.println("Game start");
        borad.paint();
        System.out.println(player + " go first");

        while (gameState == GameState.PLAY) {
            if (player == Seed.NOUGHT) {
                playerMove();
            } else {
                ai.robotMove(borad);
            }
            borad.paint();
            checkBorad();
            player = player == Seed.CROSS ? Seed.NOUGHT : Seed.CROSS;
        }

        if (gameState == GameState.CROSS_WIN) {
            System.out.println(" X wins the game!");
        } else if (gameState == GameState.NOUGHT_WIN) {
            System.out.println(" O wins the game!");
        } else {
            System.out.println(" It's a tie!");
        }

    }

    private void checkBorad() {
        if (borad.hasWin(player)) {
            gameState = player == Seed.CROSS ? GameState.CROSS_WIN : GameState.NOUGHT_WIN;
        } else if (borad.isTie()) {
            gameState = GameState.TIE;
        }
    }

    private void playerMove() {

        boolean validInput = false;

        while (!validInput) {

            System.out.println("Player " + player + " ," + "select row[1 - 4] and column[1 - 4]");

            int row = in.nextInt() - 1;
            int col = in.nextInt() - 1;

            if (row >= 0 && row < borad.getSIZE() && col >= 0 && col < borad.getSIZE()) {

                Cell seed = borad.getCell(row, col);

                if (seed.getContent() == Seed.EMPTY) {
                    seed.setContent(player);
                    borad.setCurrentRow(row);
                    borad.setCurrentCol(col);
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

    public static void main(String[] args) {


        TicTacToe ttt = new TicTacToe();
        ttt.play();
    }


}
