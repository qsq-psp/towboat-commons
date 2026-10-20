package mujica.desktop;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.FieldPosition;
import java.util.List;
import java.util.*;
import java.util.function.Function;
import java.util.spi.CalendarNameProvider;

@CodeHistory(date = "2026/10/12")
public class LocaleContentTreeModel<T extends Comparable<T>> implements TreeModel {

    @CodeHistory(date = "2026/10/12")
    private interface Node {

        @NotNull
        List<?> getList();
    }

    @CodeHistory(date = "2026/10/12")
    private static class LocaleNode implements Node, Comparable<LocaleNode> {

        final int index;

        @NotNull
        final Locale locale;

        LocaleNode(int index, @NotNull Locale locale) {
            super();
            this.index = index;
            this.locale = locale;
        }

        @NotNull
        @Override
        public List<?> getList() {
            return List.of();
        }

        @Override
        public int compareTo(@NotNull LocaleNode that) {
            return Integer.compare(this.index, that.index);
        }

        @NotNull
        @Override
        public String toString() {
            if (Locale.ROOT.equals(locale)) {
                return "(root)";
            }
            return locale.toString();
        }
    }

    @CodeHistory(date = "2026/10/12")
    private static class LanguageNode implements Node, Comparable<LanguageNode> {

        @NotNull
        final ArrayList<LocaleNode> localeList = new ArrayList<>();

        @NotNull
        final String language;

        LanguageNode(@NotNull String language) {
            super();
            this.language = language;
        }

        void add(@NotNull LocaleNode leaf) {
            localeList.add(leaf);
        }

        void seal() {
            // already sorted
            // localeList.sort(null);
            localeList.trimToSize();
        }

        @NotNull
        @Override
        public List<?> getList() {
            return localeList;
        }

        @Override
        public int compareTo(@NotNull LanguageNode that) {
            return this.language.compareTo(that.language);
        }

        @NotNull
        @Override
        public String toString() {
            if (language.isEmpty()) {
                return "(empty)";
            }
            return language;
        }
    }

    @CodeHistory(date = "2026/10/12")
    private static class ContentNode<T extends Comparable<T>> implements Node, Comparable<ContentNode<T>> {

        @NotNull
        final ArrayList<LanguageNode> languageList = new ArrayList<>();

        transient Map<String, LanguageNode> languageMap = new HashMap<>();

        @NotNull
        final T content;

        ContentNode(@NotNull T content) {
            super();
            this.content = content;
        }

        void add(@NotNull LocaleNode leaf) {
            languageMap.computeIfAbsent(leaf.locale.getLanguage(), LanguageNode::new).add(leaf);
        }

        void seal() {
            languageList.addAll(languageMap.values());
            languageMap = null;
            languageList.sort(null);
            languageList.trimToSize();
            for (LanguageNode child : languageList) {
                child.seal();
            }
        }

        @NotNull
        @Override
        public List<?> getList() {
            return languageList;
        }

        @Override
        public int compareTo(@NotNull ContentNode<T> that) {
            return this.content.compareTo(that.content);
        }

        @NotNull
        @Override
        public String toString() {
            return content.toString();
        }
    }

    @CodeHistory(date = "2026/10/12")
    private static class RootNode<T extends Comparable<T>> implements Node {

        @NotNull
        final ArrayList<ContentNode<T>> contentList = new ArrayList<>();

        transient Map<T, ContentNode<T>> contentMap = new HashMap<>();

        RootNode() {
            super();
        }

        void add(@NotNull LocaleNode leaf, @NotNull Function<Locale, T> function) {
            contentMap.computeIfAbsent(function.apply(leaf.locale), ContentNode::new).add(leaf);
        }

        void seal() {
            contentList.addAll(contentMap.values());
            contentMap = null;
            contentList.sort(null);
            contentList.trimToSize();
            for (ContentNode<T> child : contentList) {
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

    @NotNull
    final RootNode<T> root = new RootNode<>();

    public LocaleContentTreeModel(@NotNull List<Locale> localeList, @NotNull Function<Locale, T> function) {
        super();
        final int size = localeList.size();
        for (int index = 0; index < size; index++) {
            root.add(new LocaleNode(index, localeList.get(index)), function);
        }
        root.seal();
    }

    @Override
    public Object getRoot() {
        return root;
    }

    @Override
    public int getChildCount(Object parent) {
        if (parent instanceof Node) {
            return ((Node) parent).getList().size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getChild(Object parent, int index) {
        if (parent instanceof Node) {
            return ((Node) parent).getList().get(index);
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public int getIndexOfChild(Object parent, Object child) {
        if (parent instanceof Node) {
            return ((Node) parent).getList().indexOf(child);
        } else {
            return -1;
        }
    }

    @Override
    public boolean isLeaf(Object node) {
        return node instanceof LocaleNode;
    }

    @Override
    public void valueForPathChanged(TreePath path, Object newValue) {
        // immutable model
    }

    @Override
    public void addTreeModelListener(TreeModelListener l) {
        // immutable model
    }

    @Override
    public void removeTreeModelListener(TreeModelListener l) {
        // immutable model
    }

    @CodeHistory(date = "2026/10/13")
    static class CalendarNameProviderAccess {

        @Nullable
        @SuppressWarnings("Java9ReflectionClassVisibility")
        static CalendarNameProvider tryAccess() {
            try {
                Class<?> cls = Class.forName("sun.util.locale.provider.LocaleProviderAdapter");
                Method method = cls.getMethod("forJRE");
                Object obj = method.invoke(null);
                method = cls.getMethod("getCalendarNameProvider");
                obj = method.invoke(obj);
                return (CalendarNameProvider) obj;
            } catch (Exception e) {
                return null;
            }
        }

        static final CalendarNameProvider CNP = tryAccess();
    }

    @CodeHistory(date = "2026/9/29", project = "coo")
    @CodeHistory(date = "2026/10/12")
    private static class WeekDayNames implements Comparable<WeekDayNames> {

        static final int LENGTH = Calendar.SATURDAY - Calendar.SUNDAY + 1;

        final String[] array = new String[LENGTH];

        int hash;

        WeekDayNames(@NotNull CalendarNameProvider provider, @NotNull Locale locale, int style) {
            super();
            final Calendar calendar = Calendar.getInstance(locale);
            final int offset = calendar.getFirstDayOfWeek() - Calendar.SUNDAY;
            for (int i = 0; i < LENGTH; i++) {
                array[i] = provider.getDisplayName("gregory", Calendar.DAY_OF_WEEK, Calendar.SUNDAY + (offset + i) % LENGTH, style, locale);
            }
            hash = Arrays.hashCode(array);
        }

        WeekDayNames(@NotNull Locale locale, boolean shortFormat) {
            super();
            final Calendar calendar = Calendar.getInstance(locale);
            final int offset = calendar.getFirstDayOfWeek() - Calendar.SUNDAY;
            final DateFormatSymbols dateFormatSymbols = new DateFormatSymbols(locale);
            final String[] rawArray = shortFormat ? dateFormatSymbols.getShortWeekdays() : dateFormatSymbols.getWeekdays();
            for (int i = 0; i < LENGTH; i++) {
                array[i] = rawArray[Calendar.SUNDAY + (offset + i) % LENGTH];
            }
            hash = Arrays.hashCode(array);
        }

        @Override
        public int compareTo(@NotNull WeekDayNames that) {
            for (int i = 0; i < LENGTH; i++) {
                int r = this.array[i].compareTo(that.array[i]);
                if (r != 0) {
                    return r;
                }
            }
            return 0;
        }

        @Override
        public int hashCode() {
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof LocaleContentTreeModel.WeekDayNames && Arrays.equals(this.array, ((WeekDayNames) obj).array);
        }

        @Override
        @NotNull
        public String toString() {
            return Arrays.toString(array);
        }
    }

    @NotNull
    private static TreeModel weekDayNamesModel(boolean shortFormat) {
        final CalendarNameProvider cnp = CalendarNameProviderAccess.CNP;
        if (cnp != null) {
            int style = shortFormat ? Calendar.SHORT_FORMAT : Calendar.LONG_FORMAT;
            return new LocaleContentTreeModel<>(Arrays.asList(cnp.getAvailableLocales()), locale -> new WeekDayNames(cnp, locale, style));
        } else {
            return new LocaleContentTreeModel<>(Arrays.asList(Locale.getAvailableLocales()), locale -> new WeekDayNames(locale, shortFormat));
        }
    }

    @CodeHistory(date = "2026/9/30", project = "coo")
    @CodeHistory(date = "2026/10/12")
    private static class MonthNames implements Comparable<MonthNames> {

        static final int LENGTH = Calendar.DECEMBER - Calendar.JANUARY + 1;

        final String[] array = new String[LENGTH];

        int hash;

        MonthNames(@NotNull CalendarNameProvider provider, @NotNull Locale locale, int style) {
            super();
            for (int i = 0; i < LENGTH; i++) {
                array[i] = provider.getDisplayName("gregory", Calendar.MONTH, Calendar.JANUARY + i, style, locale);
            }
            hash = Arrays.hashCode(array);
        }

        MonthNames(@NotNull Locale locale, boolean shortFormat) {
            super();
            final DateFormatSymbols dateFormatSymbols = new DateFormatSymbols(locale);
            final String[] rawArray = shortFormat ? dateFormatSymbols.getShortMonths() : dateFormatSymbols.getMonths();
            System.arraycopy(rawArray, Calendar.JANUARY, array, 0, LENGTH);
            hash = Arrays.hashCode(array);
        }

        @Override
        public int compareTo(@NotNull MonthNames that) {
            for (int i = 0; i < LENGTH; i++) {
                int r = this.array[i].compareTo(that.array[i]);
                if (r != 0) {
                    return r;
                }
            }
            return 0;
        }

        @Override
        public int hashCode() {
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof MonthNames && Arrays.equals(this.array, ((MonthNames) obj).array);
        }

        @Override
        @NotNull
        public String toString() {
            return Arrays.toString(array);
        }
    }

    @NotNull
    private static TreeModel monthNamesModel(boolean shortFormat) {
        final CalendarNameProvider cnp = CalendarNameProviderAccess.CNP;
        if (cnp != null) {
            int style = shortFormat ? Calendar.SHORT_FORMAT : Calendar.LONG_FORMAT;
            return new LocaleContentTreeModel<>(Arrays.asList(cnp.getAvailableLocales()), locale -> new MonthNames(cnp, locale, style));
        } else {
            return new LocaleContentTreeModel<>(Arrays.asList(Locale.getAvailableLocales()), locale -> new MonthNames(locale, shortFormat));
        }
    }

    @CodeHistory(date = "2026/10/16")
    private static class FormattedDateOrTime implements Comparable<FormattedDateOrTime> {

        @NotNull
        final String content;

        private FormattedDateOrTime(@NotNull String content) {
            super();
            this.content = content;
        }

        @Override
        public int compareTo(@NotNull FormattedDateOrTime that) {
            return this.content.compareTo(that.content);
        }

        @Override
        public int hashCode() {
            return content.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof FormattedDateOrTime && this.content.equals(((FormattedDateOrTime) obj).content);
        }

        @Override
        @NotNull
        public String toString() {
            return content;
        }
    }

    @NotNull
    private static TreeModel formattedDateModel(int style) {
        final Date date = new Date();
        final FieldPosition fieldPosition = new FieldPosition(DateFormat.YEAR_FIELD);
        return new LocaleContentTreeModel<>(Arrays.asList(Locale.getAvailableLocales()), locale -> {
            DateFormat dateFormat = DateFormat.getDateInstance(style, locale);
            StringBuffer stringBuffer = new StringBuffer();
            dateFormat.format(date, stringBuffer, fieldPosition);
            return new FormattedDateOrTime(stringBuffer.toString());
        });
    }

    @NotNull
    private static TreeModel formattedTimeModel(int style) {
        final Date date = new Date();
        final FieldPosition fieldPosition = new FieldPosition(DateFormat.HOUR0_FIELD);
        return new LocaleContentTreeModel<>(Arrays.asList(Locale.getAvailableLocales()), locale -> {
            DateFormat dateFormat = DateFormat.getTimeInstance(style, locale);
            StringBuffer stringBuffer = new StringBuffer();
            dateFormat.format(date, stringBuffer, fieldPosition);
            return new FormattedDateOrTime(stringBuffer.toString());
        });
    }

    @CodeHistory(date = "2026/10/16")
    enum ContentType {

        WEEK_DAY("WeekDay"), SHORT_WEEK_DAY("WeekDay(short)"),
        MONTH("Month"), SHORT_MONTH("Month(short)"),
        FULL_DATE("Date(full)"), LONG_DATE("Date(long)"), MEDIUM_DATE("Date(medium)"), SHORT_DATE("Date(short)"),
        FULL_TIME("Time(full)"), LONG_TIME("Time(long)"), MEDIUM_TIME("Time(medium)"), SHORT_TIME("Time(short)");

        private static final long serialVersionUID = 0xCD7AD12862DAB459L;

        @NotNull
        final String displayName;

        ContentType(@NotNull String displayName) {
            this.displayName = displayName;
        }
    }

    @CodeHistory(date = "2026/10/17")
    static class ContentPane extends LazyContentPane {

        @NotNull
        final ContentType type;

        public ContentPane(@NotNull ContentType type) {
            super();
            this.type = type;
            setName(getClass().getName() + "[" + type + "]");
            setLayout(new BorderLayout());
        }

        @NotNull
        TreeModel createModel() {
            switch (type) {
                case WEEK_DAY:
                    return weekDayNamesModel(false);
                case SHORT_WEEK_DAY:
                    return weekDayNamesModel(true);
                case MONTH:
                    return monthNamesModel(false);
                case SHORT_MONTH:
                    return monthNamesModel(true);
                case FULL_DATE:
                    return formattedDateModel(DateFormat.FULL);
                case LONG_DATE:
                    return formattedDateModel(DateFormat.LONG);
                case MEDIUM_DATE:
                    return formattedDateModel(DateFormat.MEDIUM);
                case SHORT_DATE:
                    return formattedDateModel(DateFormat.SHORT);
                case FULL_TIME:
                    return formattedTimeModel(DateFormat.FULL);
                case LONG_TIME:
                    return formattedTimeModel(DateFormat.LONG);
                case MEDIUM_TIME:
                    return formattedTimeModel(DateFormat.MEDIUM);
                case SHORT_TIME:
                    return formattedTimeModel(DateFormat.SHORT);
                default:
                    return new DefaultTreeModel(new DefaultMutableTreeNode());
            }
        }

        @Override
        public void load() {
            final JTree tree = new JTree(createModel());
            tree.expandRow(0);
            for (int i = tree.getRowCount() - 1; i > 0; i--) {
                tree.expandRow(i);
            }
            final JScrollPane scrollPane = new JScrollPane(
                    tree,
                    JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                    JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
            );
            scrollPane.setName("treeScrollPane");
            add(scrollPane, BorderLayout.CENTER);
            super.load();
        }
    }
}
