/**
 * Created by BoogieJay
 * 4/30/17.
 */
public class StatType {

    private boolean cutOff;
    private int maxDepth;
    private long totalNodeNum;
    private long maxPruning;
    private long minPruning;

    public StatType() {
        this.cutOff = false;
        this.maxDepth = 0;
        this.totalNodeNum = 0;
        this.maxPruning = 0;
        this.minPruning = 0;
    }

    public void setCutOff(boolean cutOff) {
        this.cutOff = cutOff;
    }

    public void setMaxDepth(int maxDepth) {
        this.maxDepth = maxDepth;
    }

    public void setTotalNodeNum(long totalNodeNum) {
        this.totalNodeNum = totalNodeNum;
    }

    public void setMaxPruning(long maxPruning) {
        this.maxPruning = maxPruning;
    }

    public void setMinPruning(long minPruning) {
        this.minPruning = minPruning;
    }

    public boolean isCutOff() {

        return cutOff;
    }

    public int getMaxDepth() {
        return maxDepth;
    }

    public long getTotalNodeNum() {
        return totalNodeNum;
    }

    public long getMaxPruning() {
        return maxPruning;
    }

    public long getMinPruning() {
        return minPruning;
    }

    public boolean getCutOff() {
        return cutOff;
    }
}
