import java.util.ArrayList;
import java.util.List;

/**
 * Created by BoogieJay
 * 4/23/17.
 */

public class Board {

    private final int SIZE = 4;

    private final Cell[][] cells;
    private final List<int[]> combo;
    private int currentRow;
    private int currentCol;

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

    public Board () {
        this.cells = new Cell[SIZE][SIZE];
        this.combo = new ArrayList<>(4);

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                cells[i][j] = new Cell();
            }
        }
    }

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

    public boolean hasWin(Seed seed) {

        combo.clear();

        checkConditionAndReturnCombo(seed);

        return !combo.isEmpty();
    }

    public List<int[]> checkConditionAndReturnCombo(Seed seed) {

        if (cells[currentRow][0].getContent() == seed
                && cells[currentRow][1].getContent() == seed
                && cells[currentRow][2].getContent() == seed
                && cells[currentRow][3].getContent() == seed) {
            combo.add(new int[]{currentRow, 0});
            combo.add(new int[]{currentRow, 1});
            combo.add(new int[]{currentRow, 2});
            combo.add(new int[]{currentRow, 3});
        }

        if (cells[0][currentCol].getContent() == seed
                && cells[1][currentCol].getContent() == seed
                && cells[2][currentCol].getContent() == seed
                && cells[3][currentCol].getContent() == seed) {
            combo.add(new int[]{0, currentCol});
            combo.add(new int[]{1, currentCol});
            combo.add(new int[]{2, currentCol});
            combo.add(new int[]{3, currentCol});
        }

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

    public void setCurrentRow(int row) {
        this.currentRow = row;
    }

    public void setCurrentCol(int col) {
        this.currentCol = col;
    }

    public int getSIZE() {
        return this.SIZE;
    }

    public Seed getCell(int row, int col) {
        if (row < 0 || row >= SIZE || col < 0 || col >= SIZE) {
            throw new IllegalArgumentException("Error in row and col");
        }
        return cells[row][col].getContent();
    }

    public void setCell(int row, int col, Seed seed) {
        if (row < 0 || row >= SIZE || col < 0 || col >= SIZE) {
            throw new IllegalArgumentException("Error in row and col");
        }
        setCurrentRow(row);
        setCurrentCol(col);
        cells[currentRow][currentCol].setContent(seed);
    }

}
