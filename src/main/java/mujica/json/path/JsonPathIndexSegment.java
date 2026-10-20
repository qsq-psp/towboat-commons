package mujica.json.path;

/**
 * Created on 2026/4/4.
 */
class JsonPathIndexSegment extends JsonPathSegment {

    private static final long serialVersionUID = 0x82b3e57b0d437765L;

    final int value;

    JsonPathIndexSegment(int value) {
        super();
        this.value = value;
    }
}
