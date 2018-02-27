import java.time.LocalDateTime;
import java.util.*;

/**
 * Created by TN
 * 4/24/17.
 *
 * the AI class is implemented according to Minimax Algorithm with Alpha-Beta pruning and it also contains
 * the algorithm to calculate the evaluation value towards the non-terminating node. In addition, the statistic
 * information is calculated and showed in this class.
 */
public class AI {

    private static final int MIN_UTINITY = -1000;
    private static final int MAX_UTILITY = 1000;

    // after 1s, if the searching does not finish, limit all rest search with depth = 7
    private static final int CUT_OFF_LEVEL = 7;
    private static final int CUT_OFF_START_TIME = 1;

    private final Seed aiSeed;
    private final Board board;
    private Level level;

    // for statistic usage.
    private int startSecond;
    private StatType statType;

    /**
     * constructor of AI
     *
     * @param board the board where human and AI compete
     */
    public AI (Board board) {
        this.aiSeed = Seed.CROSS;
        this.board = board;
    }

    /**
     * set the AI level
     * it will be called at the beginning when user choose the AI level
     *
     * @param level desired level
     */
    public void setLevel(int level) {
        if (level == 1) {
            this.level = Level.EASY;
        } else if (level == 2) {
            this.level = Level.INTERMEDIATE;
        } else {
            this.level = Level.DIFFICULT;
        }
    }

    /**
     * call this function will return the next moves calculated by AI
     *
     * @return the next moves of AI
     */
    public int[] robotMove() {

        // get current time for cutoff use.
        startSecond = LocalDateTime.now().getSecond();

        // begin a new statistic calculation every time AI moves
        statType = new StatType();

        // retrieve next moves from Minimax Algorithm
        int[] moves = minimax(level.getDepth(), aiSeed, MIN_UTINITY, MAX_UTILITY);

        int row = moves[0];
        int col = moves[1];

        // print the statistic info
        showInformation();

        // set cell on the board
        board.setCell(row, col, aiSeed);

        return new int[]{row, col};
    }

    /**
     * print statistic info on the console
     */
    private void showInformation() {
        System.out.println("******************************************");
        if (statType.getNeedCutOff()) {
            System.out.println("!! Cutoff Occurred !!");
        }
        System.out.println("Max depth reached: " + statType.getMaxDepth());
        System.out.println("Total number of nodes generated: "  + (statType.getTotalNodeNum() + 1));
        System.out.println("Max pruning occurred: " + statType.getMaxPruning() + " Times");
        System.out.println("Min pruning occurred: " + statType.getMinPruning() + " Times");
        System.out.println("Total time cost: " + checkTimeOut() + "s");
    }

    /**
     * minimax algorithm
     *
     * @param level the level of AI, which also determine the depth of search
     * @param seed  the seed of AI
     * @param alpha initial alpha value
     * @param beta  initial beta value
     * @return a int array contains the row(int[0]), column(int[1]) and the score(int[2])
     */
    private int[] minimax(int level, Seed seed, int alpha, int beta) {

        // generate the next moves and pass it to getMaxValue function
        List<int[]> nextMoves = getNextMoves(board);

        // shuffle the nextMoves for better player exprience
        Collections.shuffle(nextMoves);

        // check repetition
        Set<int[]> set = new HashSet<>();

        // set the root level = 0
        return getMaxValue(level, seed, alpha, beta, nextMoves, set, 0);

    }

    /**
     * check values from the getMinValue function
     * update the score and current alpha
     * decide whether pruning occurs
     *
     * @param level how many depth left for search
     * @param seed  current seed
     * @param alpha alpha
     * @param beta  beta
     * @param nextMoves all available moves calculated in minimax function
     * @param set   set to check repetition
     * @param currentDepth  current depth reached
     * @return  a int array contains the row(int[0]), column(int[1]) and the score(int[2])
     */
    private int[] getMaxValue(int level, Seed seed, int alpha, int beta, List<int[]> nextMoves, Set<int[]> set, int currentDepth) {

        // update the max depth reached
        statType.setMaxDepth(Math.max(statType.getMaxDepth(), currentDepth));

        // initiate all variables
        int currentLevel = level;
        int bestRow = -1, bestCol = -1;
        int currentAlpha = alpha, currentBeta = beta;

        // check whether current node is terminate node. If it is not, score = Integer.MIN_VALUE
        int score = checkBoard(currentLevel, nextMoves.size() == set.size(), true);

        // if current node is not terminate, beginning to check next level
        if (score == Integer.MIN_VALUE) {

            // iterate all next moves
            for (int[] moves : nextMoves) {

                // if it already occupied, continue
                if (set.contains(moves)) {
                    continue;
                }

                // put 'X' or 'O' in curent cell
                int row = moves[0];
                int col = moves[1];
                board.setCell(row, col, seed);
                set.add(moves);

                // go to the next level
                int[] rst = getMinValue(currentLevel - 1, getOpponent(seed), currentAlpha, currentBeta, nextMoves, set, currentDepth + 1);

                // add the number of totoal nodes generated
                statType.setTotalNodeNum(statType.getTotalNodeNum() + 1);

                // if the cutoff already happened and the rest search depth deeper than CUT_OFF_LEVEL, just set it to CUT_OFF_LEVEL
                if (statType.getNeedCutOff() && currentLevel > CUT_OFF_LEVEL) {
                    currentLevel = CUT_OFF_LEVEL;
                }

                // remove from board and set
                set.remove(moves);
                board.setCell(row, col, Seed.EMPTY);

                // get the score return form getMinvalue function
                int rstScore = rst[2];

                // if return larger than current score, just update the score
                if (rstScore > score) {
                    score = rstScore;
                    bestRow = row;
                    bestCol = col;
                }

                // if current score larger or equal to currentBeta, pruning occurs
                if (score >= currentBeta) {
                    statType.setMaxPruning(statType.getMaxPruning() + 1);
                    return new int[]{row, col, score};
                }

                // update current Alpha value
                currentAlpha = Math.max(currentAlpha, score);
            }
        } else {

            // if the current node is terminate node, just return.
            return new int[]{bestRow, bestCol, score};
        }

        return new int[]{bestRow, bestCol, score};
    }


    /**
     * Check values from the getMaxValue function
     * Update the score and current beta
     * Decide whether pruning occurs
     *
     * @param level how many depth left for search
     * @param seed  current seed
     * @param alpha alpha
     * @param beta  beta
     * @param nextMoves all available moves calculated in minimax function
     * @param set   set to check repetition
     * @param currentDepth  current depth reached
     * @return  a int array contains the row(int[0]), column(int[1]) and the score(int[2])
     */
    private int[] getMinValue(int level, Seed seed, int alpha, int beta, List<int[]> nextMoves, Set<int[]> set, int currentDepth) {

        // update the max depth reached
        statType.setMaxDepth(Math.max(statType.getMaxDepth(), currentDepth));

        // initiate all variables
        int currentLevel = level;
        int bestRow = -1, bestCol = -1;
        int currentAlpha = alpha, currentBeta = beta;

        // check whether current node is terminate node. If it is not, score = Integer.MAX_VALUE
        int score = checkBoard(currentLevel, nextMoves.size() == set.size(), false);

        // if current node is not terminate, beginning to check next level
        if (score == Integer.MAX_VALUE) {
            for (int[] moves : nextMoves) {

                // iterate all next moves
                if (set.contains(moves)) {
                    continue;
                }

                // put 'X' or 'O' in curent cell
                int row = moves[0];
                int col = moves[1];
                board.setCell(row, col, seed);
                set.add(moves);

                // go to the next level
                int[] rst = getMaxValue(currentLevel - 1, getOpponent(seed), currentAlpha, currentBeta, nextMoves, set, currentDepth + 1);

                // add the number of totoal nodes generated
                statType.setTotalNodeNum(statType.getTotalNodeNum() + 1);

                // if the cutoff already happened and the rest search depth deeper than CUT_OFF_LEVEL, just set it to CUT_OFF_LEVEL
                if (statType.getNeedCutOff() && currentLevel > CUT_OFF_LEVEL) {
                    currentLevel = CUT_OFF_LEVEL;
                }

                // remove from board and set
                set.remove(moves);
                board.setCell(row, col, Seed.EMPTY);

                // if return smaller than current score, just update the score
                int rstScore = rst[2];
                if (rstScore < score) {
                    score = rstScore;
                    bestRow = row;
                    bestCol = col;
                }

                // if current score smaller or equal to currentAlpha, pruning occurs
                if (score <= currentAlpha) {
                    statType.setMinPruning(statType.getMinPruning() + 1);
                    return new int[]{row, col, score};
                }

                // update current Beta value
                currentBeta = Math.min(currentBeta, score);
            }
        } else {

            // if the current node is terminate node, just return.
            return new int[]{bestRow, bestCol, score};
        }

        return new int[]{bestRow, bestCol, score};
    }

    /**
     * return the opponent seed for given seed
     *
     * @param seed given seed
     * @return  opponent seed
     */
    private Seed getOpponent(Seed seed) {
        if (seed == Seed.NOUGHT) {
            return Seed.CROSS;
        } else {
            return Seed.NOUGHT;
        }
    }

    /**
     *  check weather a node is a terminate node
     *  if the node shows Cross win, return MAX_UTILITY
     *  if the node shows Nought win, return MIN_UTILITY
     *  if the node shows tie, return 0
     *  if the node is terminate because of limited-depth reached, return the value calculated from evaluation function
     *
     * @param level current AI level
     * @param isTie weather current node is tie
     * @param isMaxFunction who calls this checkBoard function, getMaxValue or getMinValue
     * @return score
     */
    private int checkBoard(int level, boolean isTie, boolean isMaxFunction) {
        if (board.hasWin(Seed.CROSS)) {
            return MAX_UTILITY;
        }
        if (board.hasWin(Seed.NOUGHT)) {
            return MIN_UTINITY;
        }
        if (isTie) {
            return 0;
        }
        if (level == 0 ) {
            return evaluateValue(board);
        }

        // check if time out
        checkTimeOut();

        return isMaxFunction ? Integer.MIN_VALUE : Integer.MAX_VALUE;
    }

    /**
     * check if time duration reach the CUT_OFF_START_TIME
     * if reached, set statType.needCutOff = true
     * also return the time passed so far from beginning.
     *
     * @return the time passed so far
     */
    private int checkTimeOut() {
        int currentSecond = LocalDateTime.now().getSecond();
        int diff = 0;
        diff = currentSecond - startSecond;
        if (currentSecond < startSecond) {
            diff += 60;
        }
        if (diff > CUT_OFF_START_TIME) {
            statType.setCutOff(true);
        }
        return diff;
    }

    /**
     * evaluate the board and return the value according to  evaluation function
     *
     * @param board
     * @return value
     */
    private int evaluateValue(Board board) {
        EvalType evalType = new EvalType();
        int boardSize = board.getSIZE();

        List<Seed> seedList = new ArrayList<>(boardSize);

        // check every horizontal combo
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                seedList.add(j, board.getCell(i, j));
            }
            calculateEvalScore(seedList, evalType);
        }

        // check every vertial combo
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                seedList.add(j, board.getCell(j, i));
            }
            calculateEvalScore(seedList, evalType);
        }

        // check 45 degree diagonal
        for (int i = 0; i < boardSize; i++) {
            seedList.add(i, board.getCell(i, i));
        }
        calculateEvalScore(seedList, evalType);

        // check 135 degree diagonal
        for (int i = 0; i < boardSize; i++) {
            seedList.add(i, board.getCell(i, board.getSIZE() - i - 1));
        }
        calculateEvalScore(seedList, evalType);

        // return value from evaluation function
        return calculateFromFunction(evalType);
    }

    /**
     * calculate the evaluated value from evaluation function
     * @param evalType current evaluation instance
     * @return the socre evaluated
     */
    private int calculateFromFunction(EvalType evalType) {
        int[] factors = levelFactor();
        int XScore = factors[0] * evalType.getX3() + factors[1] * evalType.getX2() + factors[2] * evalType.getX1();
        int OScore = factors[0] * evalType.getO3() + factors[1] * evalType.getO2() + factors[2] * evalType.getO1();
        return XScore - OScore;
    }

    /**
     * return factors according to current level
     * @return factors
     */
    private int[] levelFactor() {
        switch (level) {
            case EASY: return new int[]{1, 3, 6};
            case INTERMEDIATE: return new int[]{1, 0, 0};
            case DIFFICULT: return new int[]{6, 3, 1};
            default: return new int[]{6, 3, 1};
        }
    }

    /**
     * check every combo and update evalType
     * @param seedList combo
     * @param evalType evaluation type
     */
    private void calculateEvalScore(List<Seed> seedList, EvalType evalType) {

        int crossNum = 0, noughtNum = 0, emptyNum = 0;

        for (int i = 0; i < board.getSIZE(); i++) {
            if (seedList.get(i) == Seed.CROSS) {
                crossNum++;
            } else if (seedList.get(i) == Seed.NOUGHT) {
                noughtNum++;
            } else {
                emptyNum++;
            }
        }

        if (emptyNum == 1) {
            if (crossNum == 3) {
                // three X and one Empty cell
                evalType.setX3(evalType.getX3() + 1);
            } else if (noughtNum == 3) {
                // three O and one Empty cell
                evalType.setO3(evalType.getO3() + 1);
            }
        } else if (emptyNum == 2) {
            if (crossNum == 2) {
                // two X and two Empty cells
                evalType.setX2(evalType.getX2() + 1);
            } else if (noughtNum == 2) {
                // two O and two Empty cells
                evalType.setO2(evalType.getO2() + 1);
            }
        } else if (emptyNum == 3) {
            if (crossNum == 1) {
                // one X and three Empty cells
                evalType.setX1(evalType.getX1() + 1);
            } else if (noughtNum == 1) {
                // one O and three Empty cells
                evalType.setO1(evalType.getX1() + 1);
            }
        }
    }


    /**
     * generate next available moves and put it into a list
     * @param board current board
     * @return the list contains all next available moves
     */
    private List<int[]> getNextMoves(Board board) {

        List<int[]> nextMoves = new ArrayList<>();

        for (int i = 0; i < board.getSIZE(); i++) {
            for (int j = 0; j < board.getSIZE(); j++) {
                if (board.getCell(i, j) == Seed.EMPTY) {
                    nextMoves.add(new int[]{i, j});
                }
            }
        }

        return nextMoves;
    }

}
