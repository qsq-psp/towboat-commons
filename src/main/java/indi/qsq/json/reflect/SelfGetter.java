package indi.qsq.json.reflect;

/**
 * Created on 2022/8/23.
 */
class SelfGetter extends Getter {

    static final SelfGetter INSTANCE = new SelfGetter();

    SelfGetter() {
        super();
    }

    @Override
    protected Object invoke(Object self, JsonConverter jv) {
        return self;
    }
}
