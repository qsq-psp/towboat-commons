package mujica.ds.of_int.list;

/**
 * Created on 2025/9/25.
 */
public class NaturalIntList extends AbstractIntList {

    private static final long serialVersionUID = 0x8f13c2c70799ad85L;

    public final int n;

    public NaturalIntList(int n) {
        this.n = n;
    }

    @Override
    public int intLength() {
        return n;
    }

    @Override
    public int getInt(int i) {
        return i;
    }
}
