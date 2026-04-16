package mujica.json.entity;

/**
 * Created on 2026/4/4.
 */
class JsonPathNameSegment extends JsonPathSegment {

    private static final long serialVersionUID = 0xDC05C82AE31349DFL;

    final String value;

    JsonPathNameSegment(String value) {
        super();
        this.value = value;
    }
}
