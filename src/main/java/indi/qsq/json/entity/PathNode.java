package indi.qsq.json.entity;

import indi.qsq.util.text.Quote;

import java.io.Serializable;
import java.util.Objects;

/**
 * Created in infrastructure on 2022/5/17, named PathPattern$Node.
 * Created on 2022/7/26.
 */
class PathNode implements Serializable {

    private static final long serialVersionUID = 0xA5D8AD913109EE01L;

    final PathNode parent; // parent is created before this, so there is no loop

    PathDestination destination;

    PathNode(PathNode parent) {
        super();
        this.parent = parent;
    }

    boolean next() {
        return false;
    }

    int length() {
        int length = 0;
        PathNode node = this;
        do {
            length++;
            node = node.parent;
        } while (node != null);
        return length;
    }

    PathNode[] toArray() {
        int index = length();
        final PathNode[] nodes = new PathNode[index];
        PathNode node = this;
        do {
            nodes[--index] = node;
            node = node.parent;
        } while (node != null);
        return nodes;
    }

    void headToTail(StringBuilder sb) {
        for (PathNode node : toArray()) {
            appendString(sb);
            if (node != this) { // not last
                sb.append(", ");
            }
        }
    }

    String headToTail() {
        final StringBuilder sb = new StringBuilder();
        headToTail(sb);
        return sb.toString();
    }

    void tailToHead(StringBuilder sb) {
        PathNode node = this;
        do {
            if (node != this) { // not first
                sb.append(", ");
            }
            appendString(sb);
            node = node.parent;
        } while (node != null);
    }

    String tailToHead() {
        final StringBuilder sb = new StringBuilder();
        tailToHead(sb);
        return sb.toString();
    }

    @Override
    public int hashCode() {
        return System.identityHashCode(parent);
    }

    @Override
    public boolean equals(Object obj) {
        return this.getClass() == obj.getClass();
    }

    protected void appendString(StringBuilder sb) {
        final PathDestination destination = this.destination;
        if (destination != null) {
            destination.appendString(sb);
        }
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        appendString(sb);
        return sb.toString();
    }

    /**
     * Created on 2022/7/26, named Index.
     * Renamed on 2023/5/5.
     */
    static class MatchIndex extends PathNode {

        private static final long serialVersionUID = 0xC58AAF0A1757798BL;

        int index;

        MatchIndex(PathNode parent) {
            super(parent);
        }

        MatchIndex(PathNode parent, int index) {
            this(parent);
            this.index = index;
        }

        @Override
        boolean next() {
            index++;
            return true;
        }

        @Override
        public int hashCode() {
            return 0xeee3 * index + super.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof MatchIndex) {
                MatchIndex that = (MatchIndex) obj;
                return this.parent == that.parent && this.index == that.index;
            }
            return false;
        }

        @Override
        protected void appendString(StringBuilder sb) {
            sb.append(index);
            super.appendString(sb);
        }
    }

    /**
     * Created on 2022/7/26, named Key.
     * Renamed on 2023/5/5.
     */
    static class MatchKey extends PathNode {

        private static final long serialVersionUID = 0xD7EB6DE18E7B8943L;

        String key;

        MatchKey(PathNode parent) {
            super(parent);
        }

        MatchKey(PathNode parent, String key) {
            this(parent);
            this.key = key;
        }

        @Override
        boolean next() {
            key = null;
            return false;
        }

        @Override
        public int hashCode() {
            final String key = this.key;
            final int hash = super.hashCode();
            if (key != null) {
                return 0xef * key.hashCode() + hash;
            } else {
                return hash;
            }
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof MatchKey) {
                MatchKey that = (MatchKey) obj;
                return this.parent == that.parent && Objects.equals(this.key, that.key);
            }
            return false;
        }

        @Override
        protected void appendString(StringBuilder sb) {
            final String key = this.key;
            if (key != null) {
                Quote.DEFAULT.append(sb, key);
            } else {
                sb.append("null");
            }
            super.appendString(sb);
        }
    }
}
