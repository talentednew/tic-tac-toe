/**
 * Created by BoogieJay
 * 4/24/17.
 */
public enum Level {
    DIFFICULT(5), INTERMEDIATE(1), EASY(1);

    private final int depth;

    private Level(int depth) {
        this.depth = depth;
    }

    public int getDepth() {
        return this.depth;
    }
}
