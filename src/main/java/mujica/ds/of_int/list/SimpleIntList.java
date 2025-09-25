package mujica.ds.of_int.list;

/**
 * Created on 2025/5/31.
 */
public class SimpleIntList extends AbstractIntList {

    private static final long serialVersionUID = 0xe52d308dc20935dcL;

    private final IntSequence intSequence;

    public SimpleIntList(IntSequence intSequence) {
        super();
        this.intSequence = intSequence;
    }

    @Override
    public int intLength() {
        return intSequence.intLength();
    }

    @Override
    public int getInt(int i) {
        return intSequence.getInt(i);
    }
}
