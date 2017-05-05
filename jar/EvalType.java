/**
 * Created by BoogieJay
 * 4/25/17.
 */
public class EvalType {
    private int x3;
    private int x2;
    private int x1;
    private int o3;
    private int o2;
    private int o1;

    public int getX3() {
        return x3;
    }

    public void setX3(int x3) {
        this.x3 = x3;
    }

    public int getX2() {
        return x2;
    }

    public void setX2(int x2) {
        this.x2 = x2;
    }

    public int getX1() {
        return x1;
    }

    public void setX1(int x1) {
        this.x1 = x1;
    }

    public int getO3() {
        return o3;
    }

    public void setO3(int o3) {
        this.o3 = o3;
    }

    public int getO2() {
        return o2;
    }

    public void setO2(int o2) {
        this.o2 = o2;
    }

    public int getO1() {
        return o1;
    }

    public void setO1(int o1) {
        this.o1 = o1;
    }

    public EvalType() {
        this.x3 = 0;
        this.x2 = 0;
        this.x1 = 0;
        this.o3 = 0;
        this.o2 = 0;
        this.o1 = 0;

    }

    public void printEvalType() {
        System.out.println("x3 = " + x3);
        System.out.println("x2 = " + x2);
        System.out.println("x1 = " + x1);
        System.out.println("o3 = " + o3);
        System.out.println("o2 = " + o2);
        System.out.println("o1 = " + o1);
    }

}
