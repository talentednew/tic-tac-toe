import com.sun.tools.doclets.formats.html.SourceToHTMLConverter;

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
    private final AI ai2;

    public TicTacToe() {
        this.borad = new Board();
        this.gameState = GameState.PLAY;
        this.in = new Scanner(System.in);
        this.player = Seed.NOUGHT;
        ai = new AI(this.borad, Seed.CROSS);
        ai2 = new AI(this.borad, Seed.NOUGHT);
    }

    public void play () {

        System.out.println("Welcome to TicTacToe");

        System.out.println("Please choose model: 1 - Human vs Human, 2 - Human vs Robot, 3 - Robot vs Robot");

        int model = in.nextInt();

        if (model == 1) {
            System.out.println("You choose Human VS Human");
        } else {

            System.out.println("Please select 'X' AI level: 1 - easy, 2 - intermediate, 3 - difficult.");
            ai.setLevel(in.nextInt());

            if (model == 2) {
                System.out.println("If you want to go first, enter 1, else enter 0");
                setPlayer(in.nextInt());
            }

            if (model == 3) {
                System.out.println("Please select 'O' AI level: 1 - easy, 2 - intermediate, 3 - difficult.");
                ai2.setLevel(in.nextInt());
            }


        }


        System.out.println("Game start");
        borad.paint();

        while (gameState == GameState.PLAY) {
            if (player == Seed.NOUGHT) {
                if (model == 1 || model == 2) {
                    playerMove();
                } else {
                    ai2.robotMove();
                }
            } else {
                if (model == 1) {
                    playerMove();
                } else {
                    ai.robotMove();
                }
            }
            borad.paint();
            checkBorad();
            player = player == Seed.CROSS ? Seed.NOUGHT : Seed.CROSS;
        }

        borad.clear();
        GameState rstState = gameState;
        gameState = GameState.PLAY;

        if (rstState == GameState.CROSS_WIN) {
            System.out.println(" X wins the game!");
        } else if (rstState == GameState.NOUGHT_WIN) {
            System.out.println(" O wins the game!");
        } else {
            System.out.println(" It's a tie!");
        }

    }

    public String playRobotCompetition (int xLevel, int oLevel, int OGoFirst) {

        System.out.println("Game start");

        ai.setLevel(xLevel);
        ai2.setLevel(oLevel);

        setPlayer(OGoFirst);

        while (gameState == GameState.PLAY) {
            if (player == Seed.NOUGHT) {
                ai2.robotMove();
            } else {
                ai.robotMove();
            }
            borad.paint();
            System.out.println("******************************************");
            checkBorad();
            player = player == Seed.CROSS ? Seed.NOUGHT : Seed.CROSS;
        }

        GameState rstState = gameState;
        borad.clear();
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

                if (borad.getCell(row, col) == Seed.EMPTY) {
                    borad.setCell(row, col, player);
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
