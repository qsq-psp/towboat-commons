package mujica.json.path;

/**
 * Created on 2026/4/4.
 */
class JsonPathNameSegment extends JsonPathSegment {

    private static final long serialVersionUID = 0xdc05c82ae31349dfL;

    final String value;

    JsonPathNameSegment(String value) {
        super();
        this.value = value;
    }
}
