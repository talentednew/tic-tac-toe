/**
 * Created by BoogieJay
 * 4/23/17.
 */
public enum Seed {
    EMPTY(" "), CROSS("X"), NOUGHT("O");

    private final String seed;

    private Seed(String seed) {
        this.seed = seed;
    }

    @Override
    public String toString() {
        return seed;
    }
}
