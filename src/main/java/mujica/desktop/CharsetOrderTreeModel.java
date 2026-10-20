package mujica.desktop;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.Charset;
import java.util.*;

@CodeHistory(date = "2026/10/17")
public class CharsetOrderTreeModel {

    @CodeHistory(date = "2026/10/19")
    private enum CodecFailureReason {

        CAN_NOT_ENCODE, REPLACEMENT_CHARACTER, CAN_NOT_DISTINGUISH, CAN_NOT_GO_AROUND;

        private static final long serialVersionUID = 0x4B7B25702D67B99FL;
    }

    @CodeHistory(date = "2026/10/18")
    private static class OrdinalItem implements Comparable<OrdinalItem> {

        final int originalIndex;

        final String string;

        @NotNull
        final byte[] encoded;

        OrdinalItem(int originalIndex, String string, @NotNull byte[] encoded) {
            super();
            this.originalIndex = originalIndex;
            this.string = string;
            this.encoded = encoded;
        }

        @Override
        public int compareTo(@NotNull OrdinalItem that) {
            return Arrays.compare(this.encoded, that.encoded);
        }
    }

    @CodeHistory(date = "2026/10/18")
    private static class OrdinalArray implements Comparable<OrdinalArray> {

        @NotNull
        final int[] ordinal;

        OrdinalArray(@NotNull int[] ordinal) {
            super();
            this.ordinal = ordinal;
        }

        @Override
        public int compareTo(@NotNull OrdinalArray that) {
            return Arrays.compare(this.ordinal, that.ordinal);
        }
    }

    // CodecFailureOrdinalArray, ConcatOrdinalArray, SeparatedOrdinalArray

    @CodeHistory(date = "2026/10/17")
    private interface Node {

        @NotNull
        List<?> getList();
    }

    @CodeHistory(date = "2026/10/17")
    private static class CharsetNode implements Node {

        @NotNull
        final Charset charset;

        @NotNull
        final ArrayList<String> aliasList;

        CharsetNode(@NotNull Charset charset) {
            super();
            this.charset = charset;
            this.aliasList = new ArrayList<>(charset.aliases());
            this.aliasList.sort(null);
        }

        @NotNull
        @Override
        public List<?> getList() {
            return aliasList;
        }

        @NotNull
        public String toString() {
            return charset.name();
        }
    }

    @CodeHistory(date = "2026/10/17")
    private static class PackageNode implements Node, Comparable<PackageNode> {

        @NotNull
        final ArrayList<CharsetNode> charsetList = new ArrayList<>();

        @NotNull
        final String packageName;

        PackageNode(@NotNull String packageName) {
            super();
            this.packageName = packageName;
        }

        void add(@NotNull Charset charset) {
            charsetList.add(new CharsetNode(charset));
        }

        void seal() {
            charsetList.trimToSize();
        }

        @NotNull
        @Override
        public List<?> getList() {
            return charsetList;
        }

        @Override
        public int compareTo(@NotNull PackageNode that) {
            return this.packageName.compareTo(that.packageName);
        }

        @NotNull
        public String toString() {
            return packageName;
        }
    }

    @CodeHistory(date = "2026/10/17")
    private static class ContentNode implements Node, Comparable<ContentNode> {

        @NotNull
        final ArrayList<PackageNode> packageList = new ArrayList<>();

        transient Map<String, PackageNode> packageMap = new HashMap<>();

        @NotNull
        final OrdinalArray content;

        ContentNode(@NotNull OrdinalArray content) {
            super();
            this.content = content;
        }

        void add(@NotNull Charset charset) {
            packageMap.computeIfAbsent(charset.getClass().getPackageName(), PackageNode::new).add(charset);
        }

        void seal() {
            packageList.addAll(packageMap.values());
            packageMap = null;
            packageList.sort(null);
            packageList.trimToSize();
            for (PackageNode child : packageList) {
                child.seal();
            }
        }

        @NotNull
        @Override
        public List<?> getList() {
            return packageList;
        }

        @Override
        public int compareTo(@NotNull ContentNode that) {
            return this.content.compareTo(that.content);
        }

        @NotNull
        public String toString() {
            return content.toString();
        }
    }

    @CodeHistory(date = "2026/10/18")
    private static class RootNode implements Node {

        @NotNull
        final ArrayList<ContentNode> contentList = new ArrayList<>();

        transient Map<OrdinalArray, ContentNode> contentMap = new HashMap<>();

        RootNode() {
            super();
        }

        void add(@NotNull Charset charset, @NotNull OrdinalArray content) {

        }

        void seal() {
            contentList.addAll(contentMap.values());
            contentMap = null;
            contentList.sort(null);
            contentList.trimToSize();
            for (ContentNode child : contentList) {
                child.seal();
            }
        }

        @NotNull
        @Override
        public List<?> getList() {
            return contentList;
        }

        @NotNull
        @Override
        public String toString() {
            return "(all)";
        }
    }

    final RootNode root = new RootNode();

    public CharsetOrderTreeModel(@NotNull Collection<Charset> charsetCollection, @NotNull String string) {
        super();
    }
}
