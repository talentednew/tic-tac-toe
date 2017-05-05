import java.time.LocalDateTime;
import java.util.*;

/**
 * Created by BoogieJay
 * 4/24/17.
 */
public class AI {

    private static final int MIN_UTINITY = -1000;
    private static final int MAX_UTILITY = 1000;
    private static final int CUT_OFF_LEVEL = 7;
    private static final int CUT_OFF_START_TIME = 1;

    private final Seed aiSeed;
    private final Board board;
    private final Random random;
    private int startSecond;
    private Level level;
    private StatType statType;

    public AI (Board board) {
        this.aiSeed = Seed.CROSS;
        this.board = board;
        this.random = new Random();
        this.level = Level.EASY;
    }

    public void setLevel(int level) {
        if (level == 1) {
            this.level = Level.EASY;
        } else if (level == 2) {
            this.level = Level.INTERMEDIATE;
        } else {
            this.level = Level.DIFFICULT;
        }
    }

    public int[] robotMove() {
        startSecond = LocalDateTime.now().getSecond();
        statType = new StatType();

        int[] moves = minimax(level.getDepth(), aiSeed, MIN_UTINITY, MAX_UTILITY);

        int row = moves[0];
        int col = moves[1];

        showInformation();

        board.setCell(row, col, aiSeed);

        return new int[]{row, col};
    }

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

    private int[] minimax(int level, Seed seed, int alpha, int beta) {
        List<int[]> nextMoves = getNextMoves(board);
        Collections.shuffle(nextMoves);
        Set<int[]> set = new HashSet<>();

        return getMaxValue(level, seed, alpha, beta, nextMoves, set, 0);

    }

    private int[] getMaxValue(int level, Seed seed, int alpha, int beta, List<int[]> nextMoves, Set<int[]> set, int currentDepth) {

        statType.setMaxDepth(Math.max(statType.getMaxDepth(), currentDepth));
        int currentLevel = level;
        int bestRow = -1, bestCol = -1;
        int currentAlpha = alpha, currentBeta = beta;

        int score = checkBoard(currentLevel, nextMoves.size() == set.size(), true);

        if (score == Integer.MIN_VALUE) {
            for (int[] moves : nextMoves) {
                if (set.contains(moves)) {
                    continue;
                }
                int row = moves[0];
                int col = moves[1];
                board.setCell(row, col, seed);
                set.add(moves);

                int[] rst = getMinValue(currentLevel - 1, getOpponent(seed), currentAlpha, currentBeta, nextMoves, set, currentDepth + 1);

                statType.setTotalNodeNum(statType.getTotalNodeNum() + 1);
                if (statType.getNeedCutOff() && currentLevel > CUT_OFF_LEVEL) {
                    currentLevel = CUT_OFF_LEVEL;
                }

                set.remove(moves);
                board.setCell(row, col, Seed.EMPTY);

                int rstScore = rst[2];
                if (rstScore > score) {
                    score = rstScore;
                    bestRow = row;
                    bestCol = col;
                }

                if (score >= currentBeta) {
                    statType.setMaxPruning(statType.getMaxPruning() + 1);
                    return new int[]{row, col, score};
                }

                currentAlpha = Math.max(currentAlpha, score);
            }
        } else {
            return new int[]{bestRow, bestCol, score};
        }

        return new int[]{bestRow, bestCol, score};
    }



    private int[] getMinValue(int level, Seed seed, int alpha, int beta, List<int[]> nextMoves, Set<int[]> set, int currentDepth) {

        statType.setMaxDepth(Math.max(statType.getMaxDepth(), currentDepth));
        int currentLevel = level;
        int bestRow = -1, bestCol = -1;
        int currentAlpha = alpha, currentBeta = beta;

        int score = checkBoard(currentLevel, nextMoves.size() == set.size(), false);

        if (score == Integer.MAX_VALUE) {
            for (int[] moves : nextMoves) {
                if (set.contains(moves)) {
                    continue;
                }
                int row = moves[0];
                int col = moves[1];
                board.setCell(row, col, seed);
                set.add(moves);

                int[] rst = getMaxValue(currentLevel - 1, getOpponent(seed), currentAlpha, currentBeta, nextMoves, set, currentDepth + 1);

                statType.setTotalNodeNum(statType.getTotalNodeNum() + 1);
                if (statType.getNeedCutOff() && currentLevel > CUT_OFF_LEVEL) {
                    currentLevel = CUT_OFF_LEVEL;
                }

                set.remove(moves);
                board.setCell(row, col, Seed.EMPTY);

                int rstScore = rst[2];
                if (rstScore < score) {
                    score = rstScore;
                    bestRow = row;
                    bestCol = col;
                }

                if (score <= currentAlpha) {
                    statType.setMinPruning(statType.getMinPruning() + 1);
                    return new int[]{row, col, score};
                }

                currentBeta = Math.min(currentBeta, score);
            }
        } else {
            return new int[]{bestRow, bestCol, score};
        }

        return new int[]{bestRow, bestCol, score};
    }

    private Seed getOpponent(Seed seed) {
        if (seed == Seed.NOUGHT) {
            return Seed.CROSS;
        } else {
            return Seed.NOUGHT;
        }
    }

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
        checkTimeOut();
        return isMaxFunction ? Integer.MIN_VALUE : Integer.MAX_VALUE;
    }

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

    private int evaluateValue(Board board) {
        EvalType evalType = new EvalType();
        int boardSize = board.getSIZE();

        List<Seed> seedList = new ArrayList<>(boardSize);

        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                seedList.add(j, board.getCell(i, j));
            }
            calculateEvalScore(seedList, evalType);
        }

        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                seedList.add(j, board.getCell(j, i));
            }
            calculateEvalScore(seedList, evalType);
        }

        for (int i = 0; i < boardSize; i++) {
            seedList.add(i, board.getCell(i, i));
        }
        calculateEvalScore(seedList, evalType);

        for (int i = 0; i < boardSize; i++) {
            seedList.add(i, board.getCell(i, board.getSIZE() - i - 1));
        }
        calculateEvalScore(seedList, evalType);

        return calculateFromFunction(evalType);
    }

    private int calculateFromFunction(EvalType evalType) {
        int score = calculateCrossScore(evalType) - calculateNoughtScore(evalType);

        if (aiSeed == Seed.CROSS) {
            return score;
        } else {
            return -score;
        }
    }

    private int calculateCrossScore(EvalType evalType) {
        int[] factors = levelFactor();
        return factors[0] * evalType.getX3() + factors[1] * evalType.getX2() + factors[2] * evalType.getX1();
    }

    private int calculateNoughtScore(EvalType evalType) {
        int[] factors = levelFactor();
        return factors[0] * evalType.getO3() + factors[1] * evalType.getO2() + factors[2] * evalType.getO1();
    }

    private int[] levelFactor() {
        switch (level) {
            case EASY: return new int[]{1, 3, 6};
            case INTERMEDIATE: return new int[]{1, 0, 0};
            case DIFFICULT: return new int[]{6, 3, 1};
            default: return new int[]{6, 3, 1};
        }
    }

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
                evalType.setX3(evalType.getX3() + 1);
            } else if (noughtNum == 3) {
                evalType.setO3(evalType.getO3() + 1);
            }
        } else if (emptyNum == 2) {
            if (crossNum == 2) {
                evalType.setX2(evalType.getX2() + 1);
            } else if (noughtNum == 2) {
                evalType.setO2(evalType.getO2() + 1);
            }
        } else if (emptyNum == 3) {
            if (crossNum == 1) {
                evalType.setX1(evalType.getX1() + 1);
            } else if (noughtNum == 1) {
                evalType.setO1(evalType.getX1() + 1);
            }
        }
    }


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
