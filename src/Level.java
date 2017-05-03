/**
 * Created by BoogieJay
 * 4/24/17.
 */
public enum Level {
    DIFFICULT(Integer.MAX_VALUE), INTERMEDIATE(1), EASY(1);

    private final int depth;

    Level(int depth) {
        this.depth = depth;
    }

    public int getDepth() {
        return this.depth;
    }
}
