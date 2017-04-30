/**
 * Created by BoogieJay
 * 4/23/17.
 */
public class Cell {

    private Seed content;


    public Cell( ) {
        this.content = Seed.EMPTY;
    }

    public Seed getContent() {
        return this.content;
    }

    public void setContent(Seed seed) {
        this.content = seed;
    }

    public void paint() {
        System.out.print(content);
    }
}
