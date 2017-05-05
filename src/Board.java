import java.util.ArrayList;
import java.util.List;

/**
 * Board class represents the board for human-AI competition
 * It has a inner class "Cell" which stands for every square on the board
 *
 * Created by BoogieJay
 * 4/23/17.
 */

public class Board {

    private static final int SIZE = 4;

    private final Cell[][] cells;

    // combo to store the final winning combination(4 cells)
    private final List<int[]> combo;

    private int currentRow;
    private int currentCol;

    /**
     * the cell class represents the square on the board
     */
    private class Cell {

        private Seed content;

        private Cell( ) {
            this.content = Seed.EMPTY;
        }

        private Seed getContent() {
            return this.content;
        }

        private void setContent(Seed seed) {
            this.content = seed;
        }

        private void paint() {
            System.out.print(content);
        }
    }

    /**
     * board construction class
     * initial all the cells on the board
     */
    public Board () {
        this.cells = new Cell[SIZE][SIZE];
        this.combo = new ArrayList<>(SIZE);

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                cells[i][j] = new Cell();
            }
        }
    }

    /**
     * check if the current board is tie
     * @return result
     */
    public boolean isTie () {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (cells[i][j].getContent() == Seed.EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * check if "X" or "O" wins the game
     *
     * @param seed the seed
     * @return result
     */
    public boolean hasWin(Seed seed) {

        combo.clear();

        checkConditionAndReturnCombo(seed);

        return !combo.isEmpty();
    }

    /**
     * check all winning conditions and return the final combo
     * @param seed seed
     * @return combo
     */
    public List<int[]> checkConditionAndReturnCombo(Seed seed) {

        // check row
        if (cells[currentRow][0].getContent() == seed
                && cells[currentRow][1].getContent() == seed
                && cells[currentRow][2].getContent() == seed
                && cells[currentRow][3].getContent() == seed) {
            combo.add(new int[]{currentRow, 0});
            combo.add(new int[]{currentRow, 1});
            combo.add(new int[]{currentRow, 2});
            combo.add(new int[]{currentRow, 3});
        }

        // check column
        if (cells[0][currentCol].getContent() == seed
                && cells[1][currentCol].getContent() == seed
                && cells[2][currentCol].getContent() == seed
                && cells[3][currentCol].getContent() == seed) {
            combo.add(new int[]{0, currentCol});
            combo.add(new int[]{1, currentCol});
            combo.add(new int[]{2, currentCol});
            combo.add(new int[]{3, currentCol});
        }

        // check 45 degree diagonal
        if (currentCol == currentRow
                && cells[0][0].getContent() == seed
                && cells[1][1].getContent() == seed
                && cells[2][2].getContent() == seed
                && cells[3][3].getContent() == seed) {
            combo.add(new int[]{0, 0});
            combo.add(new int[]{1, 1});
            combo.add(new int[]{2, 2});
            combo.add(new int[]{3, 3});
        }

        // check 135 degree diagonal
        if (currentCol + currentRow == SIZE - 1
                && cells[0][3].getContent() == seed
                && cells[1][2].getContent() == seed
                && cells[2][1].getContent() == seed
                && cells[3][0].getContent() == seed) {
            combo.add(new int[]{0, 3});
            combo.add(new int[]{1, 2});
            combo.add(new int[]{2, 1});
            combo.add(new int[]{3, 0});
        }

        return combo;
    }

    /**
     * *********** only for testing ***********
     * print the board on the console
     */
    public void paint() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                cells[i][j].paint();
                System.out.print("|");
            }
            System.out.println();
            System.out.println("--------");
        }
        System.out.println();
    }

    /**
     * set current row
     * @param row
     */
    public void setCurrentRow(int row) {
        this.currentRow = row;
    }

    /**
     * set current column
     * @param col
     */
    public void setCurrentCol(int col) {
        this.currentCol = col;
    }

    /**
     * get the size of the board
     * @return size
     */
    public int getSIZE() {
        return this.SIZE;
    }

    /**
     * get content of cell desired
     * @param row
     * @param col
     * @return seed
     */
    public Seed getCell(int row, int col) {
        if (row < 0 || row >= SIZE || col < 0 || col >= SIZE) {
            throw new IllegalArgumentException("Error in row and col");
        }
        return cells[row][col].getContent();
    }

    /**
     *  set content of cell desired
     * @param row
     * @param col
     * @param seed
     */
    public void setCell(int row, int col, Seed seed) {
        if (row < 0 || row >= SIZE || col < 0 || col >= SIZE) {
            throw new IllegalArgumentException("Error in row and col");
        }

        // update current row and column for later checkConditionAndReturnCombo calling
        setCurrentRow(row);
        setCurrentCol(col);

        cells[currentRow][currentCol].setContent(seed);
    }

}
