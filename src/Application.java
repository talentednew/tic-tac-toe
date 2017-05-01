/**
 * Created by BoogieJay
 * 4/25/17.
 */
public class Application {

    public static void main(String[] args) {
        TicTacToe ttt = new TicTacToe();

//        robotCompetition(ttt, 1, 3);

        ttt.play();

    }

    public static void robotCompetition(TicTacToe ttt, int xLevel, int oLevel) {
        int xWin = 0;
        int oWin = 0;
        int tie = 0;
        int OGoFirst = 0;
        for (int i = 0; i < 5; i++){
            String rst = ttt.playRobotCompetition(xLevel, oLevel, OGoFirst);
            if (rst.equals("X")) {
                xWin++;
            } else if (rst.equals("O")) {
                oWin++;
            } else {
                tie++;
            }
            OGoFirst = OGoFirst == 1 ? 0 : 1;
        }

        System.out.println("X wins: "  + xWin);
        System.out.println("O wins: "  + oWin);
        System.out.println("Tie   : "  + tie);
    }
}

