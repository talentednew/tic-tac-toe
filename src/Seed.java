/**
 * Seed class represent the three state of cell, which are "X" or "O" or " "
 *
 * Created by TN
 * 4/23/17.
 */
public enum Seed {
    EMPTY(" "), CROSS("X"), NOUGHT("O");

    private final String seed;

    Seed(String seed) {
        this.seed = seed;
    }

    @Override
    public String toString() {
        return seed;
    }
}
