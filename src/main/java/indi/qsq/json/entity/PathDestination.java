package indi.um.json.entity;

import indi.um.util.text.Quote;
import indi.um.util.value.FlagName;

import java.io.Serializable;
import java.util.Objects;

/**
 * Created on 2022/7/26.
 */
class PathDestination implements Serializable {

    private static final long serialVersionUID = 0xA21EB1E3E1081298L;

    static final FlagName TYPE_FILTER = (new FlagName()).addFromClass(JsonTypeFlag.class, null, null);

    int typeFilter = JsonTypeFlag.BOOLEAN | JsonTypeFlag.INTEGRAL | JsonTypeFlag.DECIMAL | JsonTypeFlag.STRING; // all flat types except null

    PathDestination() {
        super();
    }

    @Override
    public int hashCode() {
        return typeFilter;
    }

    @Override
    public boolean equals(Object obj) {
        return PathDestination.class == obj.getClass() && this.typeFilter == ((PathDestination) obj).typeFilter;
    }

    protected void appendString(StringBuilder sb) {
        sb.append(" -> *");
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("DefaultDestination[");
        TYPE_FILTER.stringify(sb, typeFilter, "+");
        return sb.append(']').toString();
    }

    /**
     * Created on 2022/7/26, named Index.
     * Renamed on 2023/5/5.
     */
    static class AtIndex extends PathDestination {

        private static final long serialVersionUID = 0xC2FD0C330266D3CCL;

        int index;

        AtIndex() {
            super();
        }

        AtIndex(int index) {
            super();
            this.index = index;
        }

        @Override
        public int hashCode() {
            return 0xccc3 * index + typeFilter;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof AtIndex) {
                AtIndex that = (AtIndex) obj;
                return this.typeFilter == that.typeFilter && this.index == that.index;
            }
            return false;
        }

        @Override
        protected void appendString(StringBuilder sb) {
            sb.append(" -> ").append(index);
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("IndexDestination[").append(index).append("; ");
            TYPE_FILTER.stringify(sb, typeFilter, "+");
            return sb.append(']').toString();
        }
    }

    /**
     * Created on 2022/7/26, named Key.
     * Renamed on 2023/5/5.
     */
    static class AtKey extends PathDestination {

        private static final long serialVersionUID = 0x1B99D5455F18A9BAL;

        String key;

        AtKey() {
            super();
        }

        AtKey(String key) {
            super();
            this.key = key;
        }

        @Override
        public int hashCode() {
            final String key = this.key;
            if (key != null) {
                return 0xcf * key.hashCode() + typeFilter;
            } else {
                return typeFilter;
            }
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof AtKey) {
                AtKey that = (AtKey) obj;
                return this.typeFilter == that.typeFilter && Objects.equals(this.key, that.key);
            }
            return false;
        }

        @Override
        protected void appendString(StringBuilder sb) {
            sb.append(" -> ");
            final String key = this.key;
            if (key != null) {
                Quote.DEFAULT.append(sb, key);
            } else {
                sb.append("null");
            }
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("KeyDestination[");
            final String key = this.key;
            if (key != null) {
                Quote.DEFAULT.append(sb, key);
            } else {
                sb.append("null");
            }
            sb.append(", ");
            TYPE_FILTER.stringify(sb, typeFilter, "+");
            return sb.append(']').toString();
        }
    }
}
