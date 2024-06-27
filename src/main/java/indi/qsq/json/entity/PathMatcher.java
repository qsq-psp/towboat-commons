package indi.um.json.entity;

import indi.um.json.api.JsonConsumer;
import indi.um.json.io.JsonCharSequenceReader;
import indi.um.json.io.JsonStringWriter;
import org.jetbrains.annotations.NotNull;

/**
 * Created in infrastructure on 2022/5/17.
 * Created on 2022/7/26.
 */
public abstract class PathMatcher extends PathTree implements JsonConsumer {

    PathNode current;

    PathDestination destination;

    public PathMatcher() {
        super();
    }

    public PathMatcher(PathTree tree) {
        super(tree);
    }

    void anyValue() {
        destination = null;
        if (current.next()) { // increase index if in array
            PathNode node = map.get(current);
            if (node != null) {
                destination = node.destination;
            }
        }
    }

    @Override
    public void openArray() {
        PathNode node = map.get(current);
        if (node == null) {
            node = current;
        }
        current = new PathNode.MatchIndex(node); // index starts at 0
        node = map.get(current);
        if (node != null) {
            destination = node.destination;
        }
    }

    @Override
    public void closeArray() {
        current = current.parent;
        anyValue();
    }

    @Override
    public void openObject() {
        PathNode node = map.get(current);
        if (node == null) {
            node = current;
        }
        current = new PathNode.MatchKey(node);
    }

    @Override
    public void closeObject() {
        current = current.parent;
        anyValue();
    }

    @Override
    public void key(@NotNull String key) {
        destination = null;
        ((PathNode.MatchKey) current).key = key;
        PathNode node = map.get(current);
        if (node != null) {
            destination = node.destination;
        }
    }

    @Override
    public void jsonValue(@NotNull CharSequence json) {
        throw new UnsupportedOperationException();
    }

    public static class ToObjectConsumer extends PathMatcher {

        public static String transform(PathTree tree, CharSequence json) {
            final JsonStringWriter stringWriter = new JsonStringWriter();
            stringWriter.openObject();
            (new JsonCharSequenceReader(json)).read(new ToObjectConsumer(tree, stringWriter));
            stringWriter.closeObject();
            return stringWriter.get();
        }

        final JsonConsumer out;

        int depth;

        public ToObjectConsumer(JsonConsumer out) {
            super();
            this.out = out;
        }

        public ToObjectConsumer(PathTree tree, JsonConsumer out) {
            super(tree);
            this.out = out;
        }

        boolean acceptValue() {
            if (destination instanceof PathDestination.AtKey) {
                if (depth == 0) {
                    out.key(((PathDestination.AtKey) destination).key);
                }
                return true;
            } else {
                anyValue();
                return false;
            }
        }

        @Override
        public void openArray() {
            if (destination instanceof PathDestination.AtKey) {
                if (depth++ == 0) {
                    out.key(((PathDestination.AtKey) destination).key);
                }
                out.openArray();
            } else {
                super.openArray();
            }
        }

        @Override
        public void closeArray() {
            if (destination instanceof PathDestination.AtKey) {
                if (--depth == 0) {
                    destination = null;
                }
                out.closeArray();
            } else {
                super.closeArray();
            }
        }

        @Override
        public void openObject() {
            if (destination instanceof PathDestination.AtKey) {
                if (depth++ == 0) {
                    out.key(((PathDestination.AtKey) destination).key);
                }
                out.openObject();
            } else {
                super.openArray();
            }
        }

        @Override
        public void closeObject() {
            if (destination instanceof PathDestination.AtKey) {
                if (--depth == 0) {
                    destination = null;
                }
                out.closeObject();
            } else {
                super.closeObject();
            }
        }

        @Override
        public void jsonValue(@NotNull CharSequence json) {
            if (acceptValue()) {
                out.jsonValue(json);
            }
        }

        @Override
        public void nullValue() {
            if (acceptValue()) {
                out.nullValue();
            }
        }

        @Override
        public void booleanValue(boolean value) {
            if (acceptValue()) {
                out.booleanValue(value);
            }
        }

        @Override
        public void numberValue(long value) {
            if (acceptValue()) {
                out.numberValue(value);
            }
        }

        @Override
        public void numberValue(double value) {
            if (acceptValue()) {
                out.numberValue(value);
            }
        }

        @Override
        public void numberValue(@NotNull RawNumber value) {
            if (acceptValue()) {
                out.numberValue(value);
            }
        }

        @Override
        public void stringValue(@NotNull String value) {
            if (acceptValue()) {
                out.stringValue(value);
            }
        }
    }

    public static class ToJsonObject extends PathMatcher {

        final JsonObject out;

        public ToJsonObject(JsonObject out) {
            super();
            this.out = JsonObject.requireNotNull(out);
        }

        public ToJsonObject(PathTree tree, JsonObject out) {
            super(tree);
            this.out = JsonObject.requireNotNull(out);
        }
    }

    public static class ToJsonArray extends PathMatcher {

        final JsonArray out;

        public ToJsonArray(JsonArray out) {
            super();
            this.out = JsonArray.requireNonNull(out);
        }

        public ToJsonArray(PathTree tree, JsonArray out) {
            super(tree);
            this.out = JsonArray.requireNonNull(out);
        }
    }
}
