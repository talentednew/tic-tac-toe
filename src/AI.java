import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by BoogieJay
 * 4/24/17.
 */
public class AI {

    private static final int MIN_UTINITY = -1000;
    private static final int MAX_UTILITY = 1000;

    private final Seed robot;
    private final Seed human;
    private final Board board;
    private Level level;


    public AI (Board board) {
        this.robot = Seed.CROSS;
        this.human = Seed.NOUGHT;
        this.board = board;
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

    public void robotMove(Board board) {
        int[] moves = minimax(level.getDepth(), robot, MIN_UTINITY, MAX_UTILITY);

        int row = moves[0];
        int col = moves[1];
        int score = moves[2];

        Cell cell = board.getCell(row, col);
        cell.setContent(robot);
    }

    private int[] minimax(int level, Seed seed, int alpha, int beta) {
        List<int[]> nextMoves = getNextMoves(board);
        Set<int[]> set = new HashSet<>();

        return getMaxValue(level, seed, alpha, beta, nextMoves, set);

    }

    private int[] getMaxValue(int level, Seed seed, int alpha, int beta, List<int[]> nextMoves, Set<int[]> set) {

        int score = checkBoard(level, seed, nextMoves.size() == set.size());
        int bestRow = -1, bestCol = -1;
        int currentAlpha = alpha, currentBeta = beta;

        if (score == Integer.MIN_VALUE) {
            for (int[] moves : nextMoves) {
                if (set.contains(moves)) {
                    continue;
                }
                int row = moves[0];
                int col = moves[1];
                board.getCell(row, col).setContent(seed);
                set.add(moves);

                int[] rst = getMinValue(level - 1, human, currentAlpha, currentBeta, nextMoves, set);
                set.remove(moves);
                board.getCell(row, col).setContent(Seed.EMPTY);

                int rstScore = rst[2];
                if (rstScore >= score) {
                    score = rstScore;
                    bestRow = row;
                    bestCol = col;
                }
                if (score >= currentBeta) {
                    return new int[]{row, col, score};
                }
                currentAlpha = Math.max(currentAlpha, score);
            }
        } else {
            return new int[]{bestRow, bestCol, score};
        }

        return new int[]{bestRow, bestCol, score};
    }



    private int[] getMinValue(int level, Seed seed, int alpha, int beta, List<int[]> nextMoves, Set<int[]> set) {

        int score = checkBoard(level, seed, nextMoves.size() == set.size());
        int bestRow = -1, bestCol = -1;
        int currentAlpha = alpha, currentBeta = beta;

        if (score == Integer.MAX_VALUE) {
            for (int[] moves : nextMoves) {
                if (set.contains(moves)) {
                    continue;
                }
                int row = moves[0];
                int col = moves[1];
                board.getCell(row, col).setContent(seed);
                set.add(moves);

                int[] rst = getMaxValue(level - 1, robot, currentAlpha, currentBeta, nextMoves, set);
                set.remove(moves);
                board.getCell(row, col).setContent(Seed.EMPTY);

                int rstScore = rst[2];
                if (rstScore <= score) {
                    score = rstScore;
                    bestRow = row;
                    bestCol = col;
                }
                if (score <= currentAlpha) {
                    return new int[]{row, col, score};
                }
                currentBeta = Math.min(currentBeta, score);
            }
        } else {
            return new int[]{bestRow, bestCol, score};
        }

        return new int[]{bestRow, bestCol, score};
    }

    private int checkBoard(int level, Seed seed, boolean isTie) {
        if (board.hasWin(Seed.CROSS)) {
            return 1000;
        }
        if (board.hasWin(Seed.NOUGHT)) {
            return -1000;
        }
        if (isTie) {
            return 0;
        }
        if (level == 0) {
            return evaluateValue(board);
        }
        return seed == Seed.CROSS ? Integer.MIN_VALUE : Integer.MAX_VALUE;
    }

    private int evaluateValue(Board board) {
        EvalType evalType = new EvalType();

        Cell[] cellLine = new Cell[4];

        for (int i = 0; i < board.getSIZE(); i++) {
            for (int j = 0; j < board.getSIZE(); j++) {
                cellLine[j] = board.getCell(i, j);
            }
            calculateEvalScore(cellLine, evalType);
        }

        for (int j = 0; j < board.getSIZE(); j++) {
            for (int i = 0; i < board.getSIZE(); i++) {
                cellLine[i] = board.getCell(i, j);
            }
            calculateEvalScore(cellLine, evalType);
        }

        for (int i = 0; i < board.getSIZE(); i++) {
            cellLine[i] = board.getCell(i, i);
        }
        calculateEvalScore(cellLine, evalType);

        for (int i = 0; i < board.getSIZE(); i++) {
            cellLine[i] = board.getCell(i, board.getSIZE() - i - 1);
        }
        calculateEvalScore(cellLine, evalType);

        return calculateFromFunction(evalType);
    }

    private int calculateFromFunction(EvalType evalType) {
        return (6 * evalType.x3 + 3 * evalType.x2 + evalType.x1) - (6 * evalType.o3 + 3 * evalType.o2 + evalType.o1);
    }

    private void calculateEvalScore(Cell[] cellLine, EvalType evalType) {

        int crossNum = 0, noughtNum = 0, emptyNum = 0;

        for (int i = 0; i < board.getSIZE(); i++) {
            if (cellLine[i].getContent() == Seed.CROSS) {
                crossNum++;
            } else if (cellLine[i].getContent() == Seed.NOUGHT) {
                noughtNum++;
            } else {
                emptyNum++;
            }
        }

        if (emptyNum == 1) {
            if (crossNum == 3) {
                evalType.x3++;
            } else if (noughtNum == 3) {
                evalType.o3++;
            }
        } else if (emptyNum == 2) {
            if (crossNum == 2) {
                evalType.x2++;
            } else if (noughtNum == 2) {
                evalType.o2++;
            }
        } else if (emptyNum == 3) {
            if (crossNum == 1) {
                evalType.x1++;
            } else if (noughtNum == 1) {
                evalType.o1++;
            }
        }
    }



    private List<int[]> getNextMoves(Board board) {

        List<int[]> nextMoves = new ArrayList<>();

        for (int i = 0; i < board.getSIZE(); i++) {
            for (int j = 0; j < board.getSIZE(); j++) {
                if (board.getCell(i, j).getContent() == Seed.EMPTY) {
                    nextMoves.add(new int[]{i, j});
                }
            }
        }

        return nextMoves;
    }

}
