package indi.qsq.json.entity;

import indi.qsq.json.api.FrameStructure;
import indi.qsq.json.reflect.ConverterFrame;
import indi.qsq.json.reflect.JsonConverter;
import indi.qsq.json.reflect.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

/**
 * Created on 2022/7/26.
 */
public class PathTree implements FrameStructure {

    private static final Logger LOGGER = LoggerFactory.getLogger(PathTree.class);

    final HashMap<PathNode, PathNode> map;

    final PathNode root;

    public PathTree() {
        super();
        map = new HashMap<>();
        root = new PathNode(null);
    }

    public PathTree(PathTree that) {
        super();
        map = that.map;
        root = that.root;
    }

    PathDestination get(Object... segments) {
        PathNode current = root;
        for (Object segment : segments) {
            if (segment instanceof Number) {
                long index = ((Number) segment).longValue();
                if (0 <= index && index <= Integer.MAX_VALUE) {
                    PathNode next = map.get(new PathNode.MatchIndex(current, (int) index));
                    if (next != null) {
                        current = next;
                        continue;
                    }
                }
            } else if (segment instanceof String) {
                PathNode next = map.get(new PathNode.MatchKey(current, (String) segment));
                if (next != null) {
                    current = next;
                    continue;
                }
            }
            return null;
        }
        return current.destination;
    }

    boolean put(PathDestination destination, Object... segments) {
        PathNode current = root;
        for (Object segment : segments) {
            if (segment instanceof Number) {
                long index = ((Number) segment).longValue();
                if (0 <= index && index <= Integer.MAX_VALUE) {
                    PathNode node = new PathNode.MatchIndex(current, (int) index);
                    PathNode next = map.get(node);
                    if (next != null) {
                        current = next;
                    } else {
                        map.put(node, node);
                        current = node;
                    }
                    continue;
                }
            } else if (segment instanceof String) {
                PathNode node = new PathNode.MatchKey(current, (String) segment);
                PathNode next = map.get(node);
                if (next != null) {
                    current = next;
                } else {
                    map.put(node, node);
                    current = node;
                }
                continue;
            }
            return false;
        }
        current.destination = destination;
        return true;
    }

    public void build(CharSequence string) {
        (new JsonParser()).parse(string, this);
    }

    @Override
    public ConverterFrame frame(boolean isObject, JsonConverter jv) {
        if (isObject) {
            return new ToObjectBuilderFrame();
        } else {
            return new ToArrayBuilderFrame();
        }
    }

    class ToArrayBuilderFrame extends ConverterFrame {

        int index;

        @Override
        protected ConverterFrame openValue(boolean isObject, JsonConverter jv) {
            if (isObject) {
                return INSTANCE;
            }
            return new PathBuilderFrame(new PathDestination.AtIndex(index++));
        }
    }

    class ToObjectBuilderFrame extends ConverterFrame {

        String key;

        @Override
        protected void key(String key, JsonParser jp) {
            this.key = key;
        }

        @Override
        protected ConverterFrame openValue(boolean isObject, JsonConverter jv) {
            if (isObject) {
                return INSTANCE;
            }
            return new PathBuilderFrame(new PathDestination.AtKey(key));
        }
    }

    class PathBuilderFrame extends ConverterFrame {

        final PathDestination destination;

        PathNode current = root;

        PathBuilderFrame(PathDestination destination) {
            super();
            this.destination = destination;
        }

        void addNode(PathNode node) {
            final PathNode next = map.get(node);
            if (next != null) {
                current = next;
            } else {
                map.put(node, node);
                current = node;
            }
        }

        @Override
        protected Object finish(JsonParser jp) {
            if (current.destination != null && jp.logEnabled()) {
                LOGGER.debug("{} override {} at {} in {}", destination, current.destination, current, jp);
            }
            current.destination = destination;
            return null;
        }

        @Override
        protected void numberValue(long value, JsonConverter jv) {
            if (0 <= value && value <= Integer.MAX_VALUE) {
                addNode(new PathNode.MatchIndex(current, (int) value));
            }
        }

        @Override
        protected void stringValue(String value, JsonConverter jv) {
            addNode(new PathNode.MatchKey(current, value));
        }
    }

    @Override
    public String toString() {
        return map.keySet().toString();
    }
}
