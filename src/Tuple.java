/**
 * Created by zxj on 10/27/18.
 */
class Tuple<X, Y> {
    public final X first;
    public final Y second;

    public Tuple(X first, Y second) {
        this.first = first;
        this.second = second;
    }
}

class Triple<X, Y, Z> {
    public final X first;
    public final Y second;
    public final Z third;

    public Triple(X first, Y second, Z third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

}