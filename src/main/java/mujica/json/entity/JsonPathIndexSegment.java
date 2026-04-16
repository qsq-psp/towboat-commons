package mujica.json.entity;

/**
 * Created on 2026/4/4.
 */
class JsonPathIndexSegment extends JsonPathSegment {

    private static final long serialVersionUID = 0x82B3E57B0D437765L;

    final int value;

    JsonPathIndexSegment(int value) {
        super();
        this.value = value;
    }
}
